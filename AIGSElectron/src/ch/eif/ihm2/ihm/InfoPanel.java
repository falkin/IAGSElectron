package ch.eif.ihm2.ihm;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
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
	private JProgressBar chargeP2 = new JProgressBar();
	
	private JLabel highscoreLabel = new JLabel(Translate.fromKey("highscore"));
	private JTable highscore;
	private HighscoreModel highscoreModel = new HighscoreModel();
	
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
		MigLayout l = new MigLayout("wrap 1, insets 10","[center]",""); //or insets top left bottom right
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
		highscoreLabel.setForeground(Color.GREEN);
		
		/*Player 1 Box*/
		colorP1.setSize(100, 100);
		//colorP1.setBackground(new Color(220,220,220));
		colorP1.setBackground(Color.BLACK);
		colorP1.setBorder(BorderFactory.createLineBorder (Settings.getInstance().getPlayer1().getColor(), 2));
		nameP1.setForeground(Settings.getInstance().getPlayer1().getColor());
		chargeP1.setMaximum(100);
		chargeP1.setValue(0);
		
		/*Player 2 Box*/
		colorP2.setSize(100, 100);
		colorP2.setBorder(BorderFactory.createLineBorder (Settings.getInstance().getPlayer2().getColor(), 2));
		//colorP2.setBackground(new Color(220,220,220));
		colorP2.setBackground(Color.BLACK);
		nameP2.setForeground(Settings.getInstance().getPlayer2().getColor());
		chargeP2.setMaximum(100);
		chargeP2.setValue(0);
	
		/* highscore table */
		highscore = new JTable(highscoreModel);
		TableColumn column = highscore.getColumnModel().getColumn(0);
		column.setMaxWidth(25);
		highscore.setBackground(Color.BLACK);
		highscore.setForeground(Color.GREEN);
		highscore.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		highscore.setShowHorizontalLines(false);
		highscore.setShowVerticalLines(false);
		highscore.setRowSelectionAllowed(false);
		highscore.setColumnSelectionAllowed(false);
		highscore.setCellSelectionEnabled(false);
		highscore.setFocusable(false);
		
		//add(lab,"gapbottom 50");
		add(colorP1,"w 40, h 40,gaptop 80");
		add(nameP1,"growx");
		add(chargeP1,"gapbottom 50, growx");
		add(colorP2,"w 40, h 40");
		add(nameP2,"growx");
		add(chargeP2,"gapbottom 50, growx");
		add(highscoreLabel,"growx");
		add(highscore,"growx");
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
	/**
	 * receives the updates from the model
	 */
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		if(name.equals("color1")){
			colorP1.setBorder(BorderFactory.createLineBorder((Color) e.getNewValue()));
			nameP1.setForeground((Color) e.getNewValue());
		}
		else if(name.equals("color2")){
			colorP2.setBorder(BorderFactory.createLineBorder((Color) e.getNewValue()));
			nameP2.setForeground((Color) e.getNewValue());
		}
		else if(name.equals("name1")){
			nameP1.setText((String) e.getNewValue()+": 0");
		}
		else if(name.equals("name2")){
			if(e.getNewValue() !=null)
				nameP2.setText((String) e.getNewValue()+": 0");
		}
		else if(name.equals("uName1")){
			int b =nameP1.getText().lastIndexOf(":");
			String names = nameP1.getText().substring(0,b);
			nameP1.setText(names+": "+ e.getNewValue());
		}
		else if(name.equals("uName2")){
			int b =nameP2.getText().lastIndexOf(":");
			String names = nameP2.getText().substring(0,b);
			nameP2.setText(names+": "+ e.getNewValue());
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
