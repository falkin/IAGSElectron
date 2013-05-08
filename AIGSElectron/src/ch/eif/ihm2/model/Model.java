package ch.eif.ihm2.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

import ch.eif.ai.BorderPlayerPlaying;
import ch.eif.ihm2.cmds.*;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.exception.CollisionDetectedException;
import ch.eif.ihm2.ihm.GameFrame;
import ch.eif.ihm2.ihm.Translate;
import ch.eif.ihm2.manager.GameState;

/**
 * Central game logic behind the game.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 * 
 */

public class Model implements IModelOperations {
   private PropertyChangeSupport gamePCS                       = new PropertyChangeSupport(this);
   private PropertyChangeSupport infoPCS                       = new PropertyChangeSupport(this);
   private double weaponChargePerTick;
   private double cellAdvancementPerTick;
   private double weaponRemainingCharge;
   private double cellRemainingAdvancement;
   private ISettings             settings                      = Settings.getInstance();
   private int                   ticks                         = 0;
   private GameFrame frame;

   private PlayerPlaying         p1;
   private BorderPlayerPlaying         p2;

   /**
    * Updates the two head segments of each player
    * 
    */
   private void updateHeadSegments(IPlayerPlaying p1, IPlayerPlaying p2) {
      gamePCS.firePropertyChange("add", null, new ISegment[] { p1.head(), p2.head() });
   }

   /**
    * Updates the two tail segments of each player
    * 
    */
   private void updateTailSegments(ISegment s1, ISegment s2) {
      gamePCS.firePropertyChange("remove", null, new ISegment[] { s1, s2 });
   }

   /**
    * Tells the IHM to draw new bullets on the screen
    * 
    */
   private void fireAShot(IBullet b1, IBullet b2) {
      gamePCS.firePropertyChange("shot", null, new IBullet[] { b1, b2 });
   }

   /**
    * Returns the property change listener channel
    */
   public PropertyChangeSupport getInfoPropertyChangeSupport() {
      return infoPCS;
   }

   /**
    * Adds the listener for the game channel
    * 
    */
   public void addGamePropertyChangeListener(PropertyChangeListener pcl) {
      gamePCS.addPropertyChangeListener("add", pcl);
      gamePCS.addPropertyChangeListener("remove", pcl);
      gamePCS.addPropertyChangeListener("shot", pcl);
   }

   /**
    * Adds the listeners for the info channel, this channel is will also be used
    * by the menu
    * 
    */
   public void addInfoPropertyChangeListener(PropertyChangeListener pcl) {
      infoPCS.addPropertyChangeListener("color1", pcl);
      infoPCS.addPropertyChangeListener("color2", pcl);
      infoPCS.addPropertyChangeListener("name1", pcl);
      infoPCS.addPropertyChangeListener("name2", pcl);
      infoPCS.addPropertyChangeListener("progress1", pcl);
      infoPCS.addPropertyChangeListener("progress2", pcl);
      infoPCS.addPropertyChangeListener("highscore", pcl);
   }

   /**
    * Updates the progress bar of each player
    * 
    */
   private void updateProgessBars(IPlayerPlaying p1, IPlayerPlaying p2) {
      infoPCS.firePropertyChange("progress1", null, new Integer(p1.getWeaponStatus()));
      infoPCS.firePropertyChange("progress2", null, new Integer(p2.getWeaponStatus()));
   }
   
   private void updateHighScore() {
      infoPCS.firePropertyChange("highscore", null, settings.getHighScore());
   }

   /**
    * Principal method which is called after each tick. It will move player,
    * fire shots and detect collisions. Throws an exception if a collision is
    * detected.
    * 
    */
   public void tick() throws CollisionDetectedException {
      ticks++;
      handleWeapons();
      movePlayers();
   }

   /**
    * Calls the collision detection method of each player in order to determine
    * the status of the game. This is done in an arbitrary order to ensure
    * fairness and avoid a situation where each player is presumed dead, but one
    * is preferred due to a fix execution order of the collision detection
    * methods. A draw situation is not possible.
    * 
    */
   public void collisionDetection() throws CollisionDetectedException {
      boolean p1hasCollided = p1.hasCollided(p2);
      boolean p2hasCollided = p2.hasCollided(p1);
      
      if(p1hasCollided && p2hasCollided)
         throw new CollisionDetectedException(Translate.fromKey("info.draw.msg"),
               GameState.draw);
      else if(p1hasCollided)
         throw new CollisionDetectedException(Translate.fromKeyWithParams("info.winner.msg", new Object[]{p2.getName(),new Integer(ticks*settings.getGameSpeed())}),
               GameState.player2wins);
      else if(p2hasCollided)
         throw new CollisionDetectedException(Translate.fromKeyWithParams("info.winner.msg", new Object[]{p1.getName(),new Integer(ticks*settings.getGameSpeed())}),
               GameState.player1wins);
   }

   /**
    * Will fire a shot if necessary and possible due to cost constraints of the
    * weapons.
    * 
    */
   public void handleWeapons() {
      weaponRemainingCharge += weaponChargePerTick;
      if(weaponRemainingCharge >= 1.0){
         int weaponRecharge = (int)weaponRemainingCharge;
         p1.rechargeWeapon(weaponRecharge);
         p2.rechargeWeapon(weaponRecharge);
         updateProgessBars(p1, p2);
         weaponRemainingCharge -= weaponRecharge;
      }
      IBullet b1 = null;
      IBullet b2 = null;
      if (p1.isShotRequested() && p1.readyToShoot()) {
         b1 = p1.fire();
      }
      if (p2.isShotRequested() && p2.readyToShoot()) {
         b2 = p2.fire();
      }
      if (b1 != null || b2 != null) {
         fireAShot(b1, b2);
         updateProgessBars(p1, p2);
      }
   }

   /**
    * Calculates the path of each player and draws the segments, tests
    * if the players collide with anything on the map
    * 
    */
   public void movePlayers() throws CollisionDetectedException{
      cellRemainingAdvancement += cellAdvancementPerTick;
      if (cellRemainingAdvancement >= 1.0) {
         int nbMovements = (int)cellRemainingAdvancement;
         for(int i = 0; i < nbMovements; i++){
            ISegment seg1 = p1.move(settings.getMaxPlayerLength());
            ISegment seg2 = p2.move(settings.getMaxPlayerLength());

            if (seg1 != null || seg2 != null)
               updateTailSegments(seg1, seg2);
            updateHeadSegments(p1, p2);
            collisionDetection();
         }
         cellRemainingAdvancement -= nbMovements;
      }
      else
         collisionDetection();
   }

   /**
    * Returns a settings instance
    * 
    */
   public ISettings getSettings() {
      return Settings.getInstance();
   }

   /**
    * Initializes the whole model, especially the players. Calculates how often
    * a player can move or use his weapon. Creates the commands for each player.
    * 
    */
   public void init(GameFrame gf) {
      this.frame = gf;
      // Calculate how often we have to recalculate positions
      ticks = 0;
      double ticksPerSecond = 1000.0 / ((double)Constants.TIMER_BREAK);
      weaponRemainingCharge = 0.0;
      cellRemainingAdvancement = 0.0;
      cellAdvancementPerTick = ((double)settings.getGameSpeed())/ticksPerSecond;
     
      if (settings.getWeaponRechargeTime() == 0) 
         weaponChargePerTick = 100.0;
      else 
         weaponChargePerTick = 100.0/(ticksPerSecond*settings.getWeaponRechargeTime());

      // Initialize players
      p1 = new PlayerPlaying(settings.getPlayer1().getName(), settings.getPlayer1().getColor());
      p2 = new BorderPlayerPlaying(settings.getPlayer2().getName(), settings.getPlayer2().getColor());
      p1.reset(true);
      p2.reset(false);

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            CommandDown p1down = new CommandDown(p1);
            CommandDown p2down = new CommandDown(p2);
            CommandUp p1up = new CommandUp(p1);
            CommandUp p2up = new CommandUp(p2);
            CommandLeft p1left = new CommandLeft(p1);
            CommandLeft p2left = new CommandLeft(p2);
            CommandRight p1right = new CommandRight(p1);
            CommandRight p2right = new CommandRight(p2);
            CommandShoot p1shoot = new CommandShoot(p1);
            CommandShoot p2shoot = new CommandShoot(p2);
            frame.setCommands(p1up, p1down, p1left, p1right, p2up, p2down, p2left, p2right, p1shoot, p2shoot);
         }
      });
      updateHeadSegments(p1, p2);
   }

   /**
    * Finalizes the game state and saves the highscore list to the disk
    * 
    */
   public void terminateGame(GameState state) {
      if (state == GameState.player1wins)
         settings.saveHighScore(p1, ticks);
      else if(state == GameState.player2wins)
         settings.saveHighScore(p2, ticks);
      updateHighScore();
   }
}