package ch.eif.ihm2.manager;

import java.util.Timer;

import javax.swing.SwingUtilities;

import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.exception.CollisionDetectedException;
import ch.eif.ihm2.ihm.GameFrame;
import ch.eif.ihm2.model.IModelOperations;

/**
 * Main controller used to start and end the model behind the game.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 * 
 */

public class StateManager implements IStateOperations {

   private GameState        state = GameState.menu;
   private IModelOperations model;
   private Timer            timer;
   private GameFrame        gameframe;

   public StateManager(IModelOperations model, GameFrame frame) {
      this.model = model;
      this.gameframe = frame;
   }

   /**
    * As long as the game is playing, the model controller will be called.
    * 
    */
   public void tick() {
      if (state == GameState.playing) {
         try {
            model.tick();
         } catch (final CollisionDetectedException e) {
            timer.cancel();
            state = e.getGameState();
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  gameframe.endOfGameMessage(e.getMessage());
               }
            });
            model.terminateGame(state);
         }
      }
   }

   /**
    * Initializes the model and starts the timer
    * 
    */
   public void start() {
      model.init(gameframe);
      state = GameState.playing;
      timer = new Timer();
      timer.schedule(new GameTimer(this), 100, Constants.TIMER_BREAK); // starts
                                                                       // after
                                                                       // 100ms
   }
}