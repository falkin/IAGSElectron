package ch.eif.ihm2.model;

/**
 * Simple interface to a score bean
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public interface IScore extends Comparable<IScore>{

	public String getPlayerName();

	public int getScore();

         int compareTo(IScore score);
}