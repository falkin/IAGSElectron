package ch.eif.ihm2.ihm;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import ch.eif.ihm2.cmds.Command;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.manager.StateManager;
import ch.eif.ihm2.model.IModelOperations;
import ch.eif.ihm2.model.IPlayer;
import ch.eif.ihm2.model.KeyBean;
import ch.eif.ihm2.model.Settings;

/**
 * Main UI Frame - contains an infopanel on the right, the gamepanel 
 * on the left and the game menu dialog.
 * @author Stefan Aebischer
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyEventDispatcher{
	private final int INFOPANELWIDTH = 380;
	private final int WIDTH = (Constants.WORLD_WIDTH*Constants.CELL_WIDTH)+INFOPANELWIDTH-150;
	private final int HEIGHT = 50+(Constants.WORLD_HEIGHT*Constants.CELL_WIDTH);
	private InfoPanel infoPanel;
	private GamePanel gamePanel;
	private MenuPanel menuPanel;
	private IModelOperations model;
	private KeyBean k1; 
	private KeyBean k2;
	private IPlayer p1;
	private IPlayer p2;
	private Command p1up,p1right,p1down,p1left, p1shoot; //commands for player 1
	private Command p2up,p2right,p2down,p2left, p2shoot; //commands for player 2
	private StateManager s = null;
	
	/**
	 * GameFrame constructor
	 * @param m - IModelOperations
	 */
	public GameFrame(IModelOperations m){		
		super(Translate.fromKey("title"));
		model = m;		
	    setSize(WIDTH, HEIGHT);
	    setMinimumSize(new Dimension(WIDTH, HEIGHT));
	    setBackground(Color.BLACK);
		initLayout();
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				menuPanel.setVisible(true);
				menuPanel.requestFocus();
			}
		});
		addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        	quitGame();
		      }
		});
	}
	/**
	 * called by the menu, starts the game
	 */
	public void startGame(){
		menuPanel.setVisible(false);

        k1 = Settings.getInstance().getKeyboardLayoutP1();
        k2 = Settings.getInstance().getKeyboardLayoutP2();
        p1 = Settings.getInstance().getPlayer1();
        p2 = Settings.getInstance().getPlayer2();
        gamePanel.setColorP1(p1.getColor());
        gamePanel.setColorP2(p2.getColor());
        
        //start the game!
        if(s==null) s = new StateManager(model, this);
        s.start();
	}
	
	/**
	 * called when closing the window or choosing the exit option in the message box
	 */
	public void quitGame(){
		Settings.getInstance().saveSettingsToDisk();
		System.exit(0);
	}
	/**
	 * called by the model, will setup the command pattern
	 * @param - player commands
	 */
	public void setCommands(Command p1up, Command p1down, Command p1left, Command p1right, Command p2up, Command p2down, Command p2left, Command p2right, Command p1shoot, Command p2shoot){
		this.p1up = p1up;
		this.p1right = p1right;
		this.p1down = p1down;
		this.p1left = p1left;
		this.p2up = p2up;
		this.p2right = p2right;
		this.p2down = p2down;
		this.p2left = p2left;
		this.p1shoot = p1shoot;
		this.p2shoot = p2shoot;
		
		//change the event dispatcher to get keyboard inputs
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        //repaint();
	}
	
	/**
	 * will initialize the MIG layout and sets the default look and feel
	 */
	private void initLayout() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
	    MigLayout l = new MigLayout("wrap 2, insets 0px");	   
	    setLayout(l);
	}
	
	/**
	 * setups the gamepanels
	 */
	private void initComponents() {
	    infoPanel = new InfoPanel(model);
	    gamePanel = new GamePanel(model);
	    add(gamePanel,"w 100%, h 100%");
	    add(infoPanel, "w "+INFOPANELWIDTH+"px, growy");
	    menuPanel = new MenuPanel(this,this.model);	    
	}

	/**
	 * will retranslate the text boxes into the new locale
	 */
	public void retranslate(){
		infoPanel.retranslate();
		menuPanel.retranslate();
	}
	
	public InfoPanel getInfoPanel() {return infoPanel;}
	public GamePanel getGamePanel() {return gamePanel;}
	public MenuPanel getMenuPanel(){return menuPanel;}
	
	/**
	 * will be active once the game has started, 
	 * receives all the keyboard input and calls the 
	 * corresponding command
	 * @param KeyEvent
	 */
	public boolean dispatchKeyEvent(KeyEvent e) {
		 //if (e.getID() == KeyEvent.KEY_PRESSED) {}
		 //if (e.getID() == KeyEvent.KEY_TYPED) {}
		 if (e.getID() == KeyEvent.KEY_PRESSED) {
			int c = e.getKeyCode();
			if(c == k1.getUp())
				p1up.execute();
			else if(c == k1.getRight())
				p1right.execute();
			else if(c == k1.getDown())
				p1down.execute();
			else if(c == k1.getLeft())
				p1left.execute();
			else if(c == k1.getShoot())
				p1shoot.execute();
			else if(c == k2.getUp())
				p2up.execute();
			else if(c == k2.getRight())
				p2right.execute();
			else if(c == k2.getDown())
				p2down.execute();
			else if(c == k2.getLeft())
				p2left.execute();
			else if(c == k2.getShoot())
				p2shoot.execute();
         } 
		 return false;
	}
	/**
	 * @param msg - Message to display
	 */
	public void endOfGameMessage(String msg){
		Object[] options = {Translate.fromKey("dialog.restart"),Translate.fromKey("dialog.menu"),Translate.fromKey("dialog.exit")};
		int result = JOptionPane.showOptionDialog(this,msg,Translate.fromKey("dialog.title"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		
		if(result == 0)
			s.start();
		else if(result == 1)
			menuPanel.setVisible(true);
		else
			quitGame();
		gamePanel.reset();
	}
	
	/*@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		if(menuPanel.isVisible()){
			Paint shadowPaint = new Color(0, 0, 0, 30); // Translucent black
			g2d.setPaint(shadowPaint);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}*/
}
