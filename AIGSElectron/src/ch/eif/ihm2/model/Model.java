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
   private String         infoP1;
   private String         infoP2;
   private boolean         isP2Active;
   private int ptsP1;
   private int ptsP2;
   private IPlayerPlaying         p1;
   private IPlayerPlaying         p2;
  

   /**
    * Updates the two head segments of each player
    * 
    */
   private void updateHeadSegments(IPlayerPlaying p1, IPlayerPlaying p2) {
	 if(p2 == null)
		 gamePCS.firePropertyChange("add", null, new ISegment[] { p1.head(),null});
	 else
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
      infoPCS.addPropertyChangeListener("uName1", pcl);
      infoPCS.addPropertyChangeListener("uName2", pcl);
      infoPCS.addPropertyChangeListener("coverPlayer2", pcl);
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
      if(p2!=null)
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
		boolean p1hasCollided = false;
		boolean p2hasCollided = false;
		if (p2 != null) {
			p1hasCollided = p1.hasCollided(p2);
			p2hasCollided = p2.hasCollided(p1);
		}
 else {
			p1hasCollided = p1.hasCollided(null);
		}
		if (p1hasCollided && p2hasCollided) {
			if (frame.getNbrWinGame() == 0) {
				messageEndGame();
			} else {
	            ISegment seg1 = p1.move(0);
	            ISegment seg2 = p2.move(0);
	            updateTailSegments(seg1, seg2);
				frame.setNbrWinGame(frame.getNbrWinGame() - 1);
				
				frame.refrechPanel();
				this.init(frame);
			}
		} else if (p1hasCollided && p2 != null) {
			ptsP2 +=1;
			if (frame.getNbrWinGame() == 0) {
				messageEndGame();
			} else {
	            ISegment seg1 = p1.move(0);
	            ISegment seg2 = p2.move(0);
	            updateTailSegments(seg1, seg2);
				frame.setNbrWinGame(frame.getNbrWinGame() - 1);
				frame.refrechPanel();
				updateGameWin();
				this.init(frame);
				
			}
		} else if (p1hasCollided) {
			if (frame.getNbrWinGame() == 0) {
				messageEndGame();
			} else {
	            ISegment seg1 = p1.move(0);
	            ISegment seg2 = p2.move(0);
	            updateTailSegments(seg1, seg2);
				frame.setNbrWinGame(frame.getNbrWinGame() - 1);
				frame.refrechPanel();
				this.init(frame);
				 
			}
		} else if (p2hasCollided) {
			ptsP1 +=1;
			if (frame.getNbrWinGame() == 0) {
				messageEndGame();
			} else {
	            ISegment seg1 = p1.move(0);
	            ISegment seg2 = p2.move(0);
	            updateTailSegments(seg1, seg2);
				frame.setNbrWinGame(frame.getNbrWinGame() - 1);
				frame.refrechPanel();
				updateGameWin();
				this.init(frame);
				
				 
			}
		}
	}
	public void messageEndGame() throws CollisionDetectedException {
		if(ptsP1 == ptsP2){
			throw new CollisionDetectedException(
					Translate.fromKey("info.draw.msg"), GameState.draw);
		}
		else if (ptsP1> ptsP2){
			throw new CollisionDetectedException(
					Translate.fromKeyWithParams(
							"info.winner.msg",
							new Object[] {
									p1.getName(),ptsP1,ptsP2,
									new Integer(ticks
											* settings.getGameSpeed()) }),
					GameState.player1wins);
		}
		else {
			throw new CollisionDetectedException(
					Translate.fromKeyWithParams(
							"info.winner.msg",
							new Object[] {
									"Personne",ptsP2,ptsP1,
									new Integer(ticks
											* settings.getGameSpeed()) }),
					GameState.player2wins);
		}
	}

   /**
    * Will fire a shot if necessary and possible due to cost constraints of the
    * weapons.
    * 
    */
   public void handleWeapons() {
      weaponRemainingCharge += weaponChargePerTick;
    
      if(weaponRemainingCharge >= 8.0){
         int weaponRecharge = (int)2;
         weaponRemainingCharge = 1;
         	p1.rechargeWeapon(weaponRecharge);
         if (p2 != null) {
        	 p2.rechargeWeapon(weaponRecharge);
         }
         updateProgessBars(p1, p2);
       
      }
      IBullet b1 = null;
      IBullet b2 = null;
      if (p1.isShotRequested() && p1.readyToShoot()) {
         b1 = p1.fire();
      }
      if (p2 != null && p2.isShotRequested() && p2.readyToShoot()) {
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
            ISegment seg2 = null;
            if (p2 != null) {
            	 seg2 = p2.move(settings.getMaxPlayerLength());
            }
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
   public void updateGameWin(){
	  infoPCS.firePropertyChange("uName1", null, ptsP1);
	   if(p2!=null)
	  infoPCS.firePropertyChange("uName2", null, ptsP2);
   }
   /**
    * Initializes the whole model, especially the players. Calculates how often
    * a player can move or use his weapon. Creates the commands for each player.
    * 
    */
   public void init(GameFrame gf) {
      this.frame = gf;
      // Calculate how often we have to recalculate positions
      if(gf.getNbrWinGame()==4 ){
    	  ptsP1 = 0;
          ptsP2=0;
    	  ticks = 0;
      }
      double ticksPerSecond = 1000.0 / ((double)Constants.TIMER_BREAK);
      weaponRemainingCharge = 0.0;
      cellRemainingAdvancement = 0.0;
      cellAdvancementPerTick = ((double)settings.getGameSpeed())/ticksPerSecond;
     
      if (settings.getWeaponRechargeTime() == 0) 
         weaponChargePerTick = 1.0;
      else 
         weaponChargePerTick = 100.0/(ticksPerSecond*settings.getWeaponRechargeTime());

      // Initialize players
      World.getInstance().init();
      
      if(gf.getInfoP1()=="AI"){
    	  p1 = new BorderPlayerPlaying(settings.getAiP1().getName(), settings.getAiP1().getColor());
    	  p1.reset(true);
      }
      else{
    	  p1 = new PlayerPlaying(settings.getPlayer1().getName(), settings.getPlayer1().getColor());
    	  p1.reset(true);
      }
      if(gf.isPlayer2Select() && gf.getInfoP2()=="AI"){
    	  p2 = new BorderPlayerPlaying(settings.getAiP2().getName(), settings.getAiP2().getColor());
    	  p2.reset(false);
      }
      else if (gf.isPlayer2Select()){
    	  p2 = new PlayerPlaying(settings.getPlayer2().getName(), settings.getPlayer2().getColor());
    	  p2.reset(false);
      }  
      else{
    	  p2=null;
      }
      isP2Active = gf.isPlayer2Select();
      infoP1 = gf.getInfoP1();
      infoP2 = gf.getInfoP2();
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
        	   
             CommandDown p2down =  null;
             CommandUp p2up =  null;
             CommandLeft p2left =  null;
             CommandRight p2right = null;
             CommandShoot p2shoot =  null;      	
            
             CommandDown	 p1down = new CommandDown(p1);
             CommandUp	 	 p1up = new CommandUp(p1);
             CommandLeft	 p1left = new CommandLeft(p1);
             CommandRight	 p1right = new CommandRight(p1);
             CommandShoot    p1shoot = new CommandShoot(p1);      	
         
             if (isP2Active){
                 p2down = new CommandDown(p2);
                 p2up = new CommandUp(p2);
                 p2left = new CommandLeft(p2);
                 p2right = new CommandRight(p2);
                 p2shoot = new CommandShoot(p2);            	
            
            }
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
      else if(p2 == null && state == GameState.player2wins)
         settings.saveHighScore(null, ticks);
      else if( state == GameState.player2wins)
          settings.saveHighScore(p2, ticks);
      updateHighScore();
   }
}