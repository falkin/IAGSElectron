package ch.eif.ihm2.model;

import java.awt.Color;
import java.io.Serializable;

import ch.eif.ihm2.cst.Constants;

/**
 * Contains basic information about a player.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class Player implements IPlayer, Serializable {
   
  
   /**
	 * 
	 */
	private static final long serialVersionUID = -316588156939713709L;
   private String name;
   private Color color;

   public Player(String name, Color color) {
      this.name = name;
      this.color = color;
   }

   public Color getColor(){
      return color;
   }

   public String getName(){
      return name;
   }
   
   /**
    * Returns a Player instance with default values for player one.
    * 
    */
   public static Player getDefaultP1(){
      return new Player(Constants.DEFAULT_P1_NAME, Constants.DEFAULT_P1_COLOR);
   }
   
   /**
    * Returns a Player instance with default values for player two.
    * 
    */
   public static Player getDefaultP2(){
      return new Player(Constants.DEFAULT_P2_NAME, Constants.DEFAULT_P2_COLOR);
   }
   
   /**
    * Returns a Player instance with default values for player two.
    * 
    */
   public static Player getDefaultIA1(){
      return new Player(Constants.DEFAULT_AI, Constants.DEFAULT_P1_COLOR);
   }
   
   /**
    * Returns a Player instance with default values for player two.
    * 
    */
   public static Player getDefaultIA2(){
      return new Player(Constants.DEFAULT_AI, Constants.DEFAULT_P2_COLOR);
   }
   @Override
   public void setColor(Color color) {
      this.color = color;
   }

   @Override
   public void setName(String name) {
      this.name = name;
   }

   
   
}