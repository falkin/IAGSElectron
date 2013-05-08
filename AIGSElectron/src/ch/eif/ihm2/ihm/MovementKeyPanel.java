package ch.eif.ihm2.ihm;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import ch.eif.ihm2.model.*;
import net.miginfocom.swing.MigLayout;

/**
 * Key map panel User Interface
 * 
 * @author Bernhard Leutwiler, Stefan Aebischer
 * @version 1.0 - 02.01.2012
 */
@SuppressWarnings("serial")
public class MovementKeyPanel extends JPanel{
	private ISettings settings;
	private KeyBean keymap;
	private JLabel playerLabel;
	private JButton btUp = new JButton();
	private JButton btRight = new JButton();
	private JButton btDown = new JButton();
	private JButton btLeft = new JButton();
	private JButton btShot = new JButton();
	
	private final JDialog pm = new JDialog();
	private int[] occupiedkeys = new int[10];
	private int player;
	
	/**
	 * Constructor
	 * 
	 * @param player - Player ID
	 */
	public MovementKeyPanel(int player){
		super();
		settings = Settings.getInstance();
		this.player = player;
		if(player == 1){
			this.keymap = settings.getKeyboardLayoutP1();
			playerLabel = new JLabel(settings.getPlayer1().getName());
		}
		else{
			this.keymap = settings.getKeyboardLayoutP2();
			playerLabel = new JLabel(settings.getPlayer2().getName());
		}
		setOccupiedKeys();
		initLayout();
		reloadContent();
		initComponents();
	}
	
	/**
	 * Initialize Panel
	 */
	private void initLayout(){
		setLayout(new MigLayout("","0[50!]10[50!]10[50!]0","0[]10[]10[]0"));
	}
	/**
	 * Paint components
	 */
	private void initComponents(){
		playerLabel.setForeground(Color.GRAY);
		playerLabel.setHorizontalAlignment(JLabel.CENTER);
				
		btUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pm.setName("0");
				pm.setVisible(true);
			}
		});	
		btDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pm.setName("1");
				pm.setVisible(true);
			}
		});
		btLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pm.setName("2");
				pm.setVisible(true);
			}
		});
		btRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pm.setName("3");
				pm.setVisible(true);
			}
		});
		btShot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pm.setName("4");
				pm.setVisible(true);
			}
		});
		
		add(playerLabel,"cell 0 0 3 1, grow"); //will occupy 3 cells
		add(btUp, "cell 1 1, w 50!");
		add(btRight, "cell 2 2, w 50!");
		add(btDown, "cell 1 2, w 50!");
		add(btLeft, "cell 0 2, w 50!");
		add(btShot, "cell 0 3 3 1, w 100%");
		
		pm.setAlwaysOnTop(true);
		pm.add(new JLabel(Translate.fromKey("menu.settingstab.selectkey")));
		pm.setResizable(false);
		pm.pack();
		pm.setLocationRelativeTo(null);
		pm.addKeyListener(kl);
		pm.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				pm.setVisible(false);
			}
			public void focusGained(FocusEvent arg0) {}
		});
	}
	
	/**
	 * Selects the corresponding text to show on button
	 * 
	 * @param key - key code chosen
	 * @return - String
	 */
	private String getKeyText(int key){
		String keyText = KeyEvent.getKeyText(key);
		if(keyText.length()>1){
			switch(key){
				case KeyEvent.VK_UP:keyText = "up"; break;
				case KeyEvent.VK_DOWN:keyText = "dn"; break;
				case KeyEvent.VK_LEFT:keyText = "lt"; break;
				case KeyEvent.VK_RIGHT:keyText = "rt"; break;
				case KeyEvent.VK_DEAD_CIRCUMFLEX:keyText = "^"; break;
				case KeyEvent.VK_DOLLAR:keyText = "$"; break;
				// TODO suite
				default:break;
			}
		}
		return keyText;
	}
	
	/**
	 * Method to fill key array from settings
	 */
	private void setOccupiedKeys(){
		occupiedkeys = new int[10];
		occupiedkeys[0] = settings.getKeyboardLayoutP1().getUp();
		occupiedkeys[1] = settings.getKeyboardLayoutP1().getDown();
		occupiedkeys[2] = settings.getKeyboardLayoutP1().getLeft();
		occupiedkeys[3] = settings.getKeyboardLayoutP1().getRight();
		occupiedkeys[4] = settings.getKeyboardLayoutP1().getShoot();
		occupiedkeys[5] = settings.getKeyboardLayoutP2().getUp();
		occupiedkeys[6] = settings.getKeyboardLayoutP2().getDown();
		occupiedkeys[7] = settings.getKeyboardLayoutP2().getLeft();
		occupiedkeys[8] = settings.getKeyboardLayoutP2().getRight();
		occupiedkeys[9] = settings.getKeyboardLayoutP2().getShoot();
	}
	
	/**
	 * Method to verify if key is not yet used
	 * 
	 * @param keyCode - key to check
	 * @return Boolean
	 */
	private boolean checkKeysNotUsed(int keyCode, int self){
		for(int i = 0; i<occupiedkeys.length; i++){
			if(occupiedkeys[i] == keyCode && i != self){
				System.out.println(occupiedkeys[i]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Associated KeyListener
	 */
	private KeyListener kl = new KeyListener() {
		public void keyTyped(KeyEvent arg0) {}
		public void keyReleased(KeyEvent evt) {
			JDialog src = (JDialog)evt.getSource();
			int key = evt.getKeyCode();
			int self = Integer.parseInt(src.getName());
			if(player != 1)
				self += 5;
			if(checkKeysNotUsed(key, self)){
				JOptionPane.showMessageDialog(null, Translate.fromKey("menu.settingstab.keyalreadyused"), "", JOptionPane.ERROR_MESSAGE);
			}
			else {
				switch(self){
					case 0:settings.getKeyboardLayoutP1().setUp(key);btUp.setText(getKeyText(key));break;
					case 1:settings.getKeyboardLayoutP1().setDown(key);btDown.setText(getKeyText(key));break;
					case 2:settings.getKeyboardLayoutP1().setLeft(key);btLeft.setText(getKeyText(key));break;
					case 3:settings.getKeyboardLayoutP1().setRight(key);btRight.setText(getKeyText(key));break;
					case 4:settings.getKeyboardLayoutP1().setShoot(key);btShot.setText(getKeyText(key));break;
					case 5:settings.getKeyboardLayoutP2().setUp(key);btUp.setText(getKeyText(key));break;
					case 6:settings.getKeyboardLayoutP2().setDown(key);btDown.setText(getKeyText(key));break;
					case 7:settings.getKeyboardLayoutP2().setLeft(key);btLeft.setText(getKeyText(key));break;
					case 8:settings.getKeyboardLayoutP2().setRight(key);btRight.setText(getKeyText(key));break;
					case 9:settings.getKeyboardLayoutP2().setShoot(key);btShot.setText(getKeyText(key));break;
					default:break;
				}
				setOccupiedKeys();
			}
			pm.setVisible(false);
		}
		public void keyPressed(KeyEvent arg0) {}
	};
	
	/**
	 * Method to reload keymaps
	 */
	public void reloadContent(){
		if(player == 1){
			this.keymap = settings.getKeyboardLayoutP1();
			playerLabel.setText(settings.getPlayer1().getName());
		}
		else{
			this.keymap = settings.getKeyboardLayoutP2();
			playerLabel.setText(settings.getPlayer2().getName());
		}
		setOccupiedKeys();
		
		btUp.setText(getKeyText(keymap.getUp()));
		btDown.setText(getKeyText(keymap.getDown()));
		btLeft.setText(getKeyText(keymap.getLeft()));
		btRight.setText(getKeyText(keymap.getRight()));
		btShot.setText(getKeyText(keymap.getShoot()));
	}
}
