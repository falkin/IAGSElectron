package ch.eif.ihm2.manager;

import java.util.TimerTask;

/**
 * This timer will trigger an update of the model each time he wakes up.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */
public class GameTimer extends TimerTask{
   
   private IStateOperations operations;
   
   public GameTimer(IStateOperations ops){
      this.operations = ops;
   }

   /**
    * Wakes up and triggers an update of the model as long as no collision
    * is detected.
    * 
    */
   public void run()  {
	   operations.tick();
   }	
}