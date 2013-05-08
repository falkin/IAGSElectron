package ch.eif.ihm2.model;
import java.io.Serializable;

/**
 * A bean which stores the score of a player
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class Score implements IScore, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7166326280017309258L;
	private String playerName;
	private int score;
	
	public Score(String playerName, int score){
	   this.playerName = playerName;
	   this.score = score;
	}
	
	public Score(Player player){
	   this(player.getName(), 0);
	}
	
	public Score(Player player, int score){
	   this(player.getName(), score);
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setScore(int score) {
		this.score = score;
	}
	public String getPlayerName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}
	
	public void addScore(int newPoints){
	   score+= newPoints;
	}

   @Override
   public int compareTo(IScore score) {
      return score.getScore()- this.score;
   }
}