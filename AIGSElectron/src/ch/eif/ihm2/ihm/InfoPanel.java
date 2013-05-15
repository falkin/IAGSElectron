package ch.eif.ihm2.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import ch.eif.ihm2.model.IModelOperations;
import ch.eif.ihm2.model.IScore;
import ch.eif.ihm2.model.Settings;

/**
 * The InfoPanel (on the right side of the Frame) displays some 
 * important information of the game like the player names, player colors,
 * the weapon recharge status or the game highscore.
 * 
 * @author Stefan Aebischer
 */
@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements PropertyChangeListener {
	//private JLabel lab = new JLabel(Translate.fromKey("info.title"));
	//private GameFrame gameFrame;
	/*Player 1 Box*/
	private JPanel colorP1 = new JPanel();
	private JLabel nameP1 = new JLabel(" ");
	private JProgressBar chargeP1 = new JProgressBar();
	
	/*Player 2 Box*/
	private JPanel colorP2 = new JPanel();
	private JLabel nameP2 = new JLabel(" ");
	private JLabel scoreP2 = new JLabel(" ");
	private JLabel scoreP1 = new JLabel(" ");
	private JProgressBar chargeP2 = new JProgressBar();
	
	private JLabel highscoreLabel = new JLabel(Translate.fromKey("highscore"));
	private JTable highscore;
	private HighscoreModel highscoreModel = new HighscoreModel();
	private String fclFileB = "src/ch/eif/ihm2/ressources/back.png";
	Image bg = new ImageIcon(fclFileB).getImage();

	/**
	 * InfoPanel constructor
	 * @param model used to add the property change listener
	 */
	public InfoPanel(IModelOperations model){
		super();
		initLayout();
		initComponents();
		model.addInfoPropertyChangeListener(this);
		highscoreModel.setScores(Settings.getInstance().getHighScore());
		highscore.getTableHeader().setVisible(true);
	}
	
	/*private void testHighScore(){
	int max = 5;
	IScore[] score = new IScore[max];
	for(int i = 0; i<max;i++){
		Score s  = new Score();
		s.setPlayerName("Player "+i);
		s.setScore(1000-i*100);
		score[i] = s;
	}
	highscoreModel.setScores(score);
	}*/
	
	/**
	 * setups the MIG layout
	 */
	private void initLayout(){
		MigLayout l = new MigLayout("insets 10","0[]10[]0",""); //or insets top left bottom right
		//additional setup
		setLayout(l);
	}
	/**
	 * setups the infopanel components
	 */
	private void initComponents(){
		//setBackground(new Color(240, 240, 240));
		setBackground(Color.BLACK);
		/*Game Info Label on the top*/
		//lab.setAlignmentX(CENTER_ALIGNMENT);
		//lab.setForeground(new Color(0,102,0));
		highscoreLabel.setForeground(Color.black);
		highscoreLabel.setFont(new Font("ARIAL", Font.BOLD, 15));
		
		
		/*Player 1 Box*/
		colorP1.setSize(100, 100);
		//colorP1.setBackground(new Color(220,220,220));
		colorP1.setBackground(Settings.getInstance().getPlayer1().getColor());
		colorP1.setBorder(BorderFactory.createLineBorder (Color.black, 2));
		nameP1.setForeground(Color.black);
		nameP1.setFont(new Font("ARIAL", Font.BOLD, 20));
		scoreP1.setFont(new Font("ARIAL", Font.BOLD, 30));
		scoreP1.setForeground(Settings.getInstance().getPlayer1().getColor());
		chargeP1.setMaximum(100);
		chargeP1.setValue(0);
		
		/*Player 2 Box*/
		colorP2.setSize(100, 100);
		colorP2.setBorder(BorderFactory.createLineBorder (Color.black, 2));
		//colorP2.setBackground(new Color(220,220,220));
		colorP2.setBackground(Settings.getInstance().getPlayer2().getColor());
		nameP2.setForeground(Color.black);
		nameP2.setFont(new Font("ARIAL", Font.BOLD, 20));
		scoreP2.setFont(new Font("ARIAL", Font.BOLD, 30));
		scoreP2.setForeground(Settings.getInstance().getPlayer2().getColor());
		chargeP2.setMaximum(100);
		chargeP2.setValue(0);

			/* highscore table */
		highscore = new JTable(highscoreModel);
		TableColumn column = highscore.getColumnModel().getColumn(0);
		column.setMaxWidth(25);
		highscore.setOpaque(false);
		((DefaultTableCellRenderer)highscore.getDefaultRenderer(Object.class)).setOpaque(false);
		highscore.setForeground(Color.black);
	//	highscore.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		highscore.setShowHorizontalLines(false);
		highscore.setShowVerticalLines(false);
		highscore.setRowSelectionAllowed(false);
		highscore.setColumnSelectionAllowed(false);
		highscore.setCellSelectionEnabled(false);
		highscore.setFocusable(false);
		
		//add(lab,"gapbottom 50");
		// add(colorP1,"w 70, h 10,gaptop 40,wrap 1");
		add(nameP1,"cell 1 1 1 1,w 40%,growx,gapleft 14,gaptop 69,gapbottom 21");
		add(scoreP1,"cell 1 1 2 1,growx,gaptop 20,wrap 1");
		add(chargeP1,"cell 1 2 2 1,gapbottom 30,gapleft 14,gapright 25, growx,wrap 1");
		//add(colorP2,"w 70, h 10,gaptop 20");
		add(nameP2,"cell 1 3 1 1,w 40%,growx,gapleft 14,gaptop 38,gapbottom 21");
		add(scoreP2,"cell 1 3 1 1,growx,gaptop 20,wrap 1");
		add(chargeP2,"cell 1 4 2 1,gapbottom 30,gapleft 14,gapright 25, growx,wrap 1");
		add(highscoreLabel,"cell 1 5 2 1,growx,gapleft 24, gaptop 13,wrap 1");
		add(highscore,"cell 1 6 2 1,growx,gapleft 14,gaptop 10,wrap 1");
		highscore.setFont(new Font("ARIAL", Font.PLAIN, 12));
	}
	/**
	 * will translate all text boxes into the new language
	 */
	public void retranslate(){
		//lab.setText(Translate.fromKey("info.title"));
		highscoreLabel.setText(Translate.fromKey("highscore"));
		highscoreModel.retranslate();
		highscore.updateUI();
		highscore.revalidate();
	}
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
	/**
	 * receives the updates from the model
	 */
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		if(name.equals("color1")){
			colorP1.setBorder(BorderFactory.createLineBorder((Color) e.getNewValue()));
			nameP1.setForeground((Color) e.getNewValue());
			nameP1.setOpaque(false);
		}
		else if(name.equals("color2")){
			colorP2.setBorder(BorderFactory.createLineBorder((Color) e.getNewValue()));
			nameP2.setForeground((Color) e.getNewValue());
			nameP2.setOpaque(false);
		}
		else if(name.equals("name1")){
			nameP1.setText((String) e.getNewValue());
			scoreP1.setText(""+0);
		}
		else if(name.equals("name2")){
			if(e.getNewValue() !=null)
				nameP2.setText((String) e.getNewValue());
			scoreP2.setText(""+0);
		}
		else if(name.equals("uName1")){
			scoreP1.setText(""+e.getNewValue());
		}
		else if(name.equals("uName2")){			
			scoreP2.setText(""+e.getNewValue());
		}
		else if(name.equals("progress1")){
			//values 0 - 100
			chargeP1.setValue((Integer)e.getNewValue());
		}
		else if(name.equals("progress2")){
			chargeP2.setValue((Integer)e.getNewValue());
		}
		else if(name.equals("highscore")){
			LinkedList<IScore> val = (LinkedList<IScore>) e.getNewValue();
			
			highscoreModel.setScores(val);
			highscore.updateUI();
			highscore.revalidate();
		}
		else if(name.equals("coverPlayer2")){
			colorP2.setVisible((Boolean)e.getNewValue());
			chargeP2.setVisible((Boolean)e.getNewValue());
		}
	}
}

