package ch.eif.ihm2.ihm;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.model.IScore;

/**
 * Table Model class used for the highscore view
 * @author Stefan Aebischer
 *
 */
@SuppressWarnings("serial")
public class HighscoreModel extends AbstractTableModel {
	private final int TOPCOUNT = Constants.MAX_HIGHSCORE_ENTRIES;
	private String[] COLNAMES = {"", Translate.fromKey("player"),Translate.fromKey("score")};
	private String[][] data = {};
	//private boolean noData = true;
	
	public int getColumnCount() {
		return COLNAMES.length;
	}
	public int getRowCount() {
		if(data.length == 0) return 0;
		else if (data.length > TOPCOUNT) return TOPCOUNT;
		return data.length;
	}
	public Object getValueAt(int row, int col) {
		if(col==2)
			return MessageFormat.format("{0,number}", Integer.parseInt(data[row][col]));
		return data[row][col];
	}
	public void setScores(LinkedList<IScore> linkedList){		
		if(linkedList != null){
			//noData = false;
			data = new String[linkedList.size()][COLNAMES.length];
			int i = 0;
			/*for(int j = 0; j < scores.length; j++){
				if(scores[j] == null)
					System.out.println("null at " + j);
				else
					System.out.println(scores[j].getPlayerName());
			}*/
	        Collections.sort(linkedList);
			
			for(IScore score:linkedList){
				data[i][0] = (i+1)+".";
				data[i][1] = score.getPlayerName();
				data[i][2] = score.getScore()+"";
				i++;
			}
		}
	}
	public void retranslate() {
		COLNAMES[1] = Translate.fromKey("player");
		COLNAMES[2] = Translate.fromKey("score");
		/*if(noData){ //we dint receive a highscore update, so change the default values
			data[0][0] = Translate.fromKey("info.highscore.no");
			data[0][1] = Translate.fromKey("highscore");
			data[0][2] = Translate.fromKey("info.highscore.yet");
		}*/
	}

}
