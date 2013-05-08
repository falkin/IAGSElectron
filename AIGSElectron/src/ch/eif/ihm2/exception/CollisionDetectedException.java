package ch.eif.ihm2.exception;

import ch.eif.ihm2.manager.GameState;

public class CollisionDetectedException extends Exception {
   
   /**
    * Exception used to signal the end of the game.
    * 
    * @author Michael Heinzer
    * @version 1.0 - 02.01.2012
    * 
    */
	private static final long serialVersionUID = 1L;
private GameState state;

   public CollisionDetectedException(String s, GameState state) {
      super(s);
      this.state = state;
   }
   
   public GameState getGameState(){
      return state;
   }

}
