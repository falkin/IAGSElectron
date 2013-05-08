package ch.eif.ihm2.model;

import java.io.*;
import java.util.LinkedList;
import java.util.Locale;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.cst.Constants.DIFFICULTY;

/**
 * Settings bean, can be serialized. Contains settings as well as highscores.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 * 
 */

public class Settings implements ISettings, Serializable {

   private static final long    serialVersionUID  = 3792227699605484460L;
   private int                  gameSpeed;
   private KeyBean              keyboardLayoutP1;
   private KeyBean              keyboardLayoutP2;
   private Locale               locale;
   private int                  maxPlayerLength;
   private int                  weaponRechargeTime;
   private Player               p1;
   private Player               p2;
   private Constants.DIFFICULTY difficulty;
   private LinkedList<IScore>   scores;

   private static Settings      settings_instance = null;

   /**
    * Returns an instance of the settings class, will try to recover an
    * serialized version of itself. If this fails, an default version will be
    * created and returned.
    * 
    */
   public static ISettings getInstance() {
      // Try to deserialize
      if (settings_instance == null)
         retrieveSettingsFromDisk();
      // If no serialization is available, create a new settings object with
      // default values
      if (settings_instance == null)
         loadDefaultSettings();
      return settings_instance;
   }

   /**
    * Fills the settings bean with default values
    */
   public static void loadDefaultSettings() {
      if (settings_instance == null)
         settings_instance = new Settings();
      settings_instance.keyboardLayoutP1 = KeyBean.getP1Standard();
      settings_instance.keyboardLayoutP2 = KeyBean.getP2Standard();
      settings_instance.locale = Locale.getDefault();
      settings_instance.maxPlayerLength = Constants.DEFAULT_MAXPLAYERLENGTH;
      settings_instance.weaponRechargeTime = Constants.DEFAULT_WEAPONRECHARGETIME;
      settings_instance.p1 = Player.getDefaultP1();
      settings_instance.p2 = Player.getDefaultP2();
      settings_instance.setDifficulty(DIFFICULTY.NORMAL);
      settings_instance.scores = new LinkedList<IScore>();
   }

   /**
    * Writes a serialized version of the settings instance to the disk.
    * 
    */
   public void saveSettingsToDisk() {
      ObjectOutput out = null;
      try {
         out = new ObjectOutputStream(new FileOutputStream(Constants.SETTINGS_FILENAME));
         out.writeObject(getInstance());
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (out != null)
            try {
               out.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
      }

   }

   /**
    * Loads a serialized object from the disk to the memory.
    * 
    */
   private static void retrieveSettingsFromDisk() {
      File file = new File(Constants.SETTINGS_FILENAME);
      ObjectInputStream in = null;
      try {
         in = new ObjectInputStream(new FileInputStream(file));
         settings_instance = (Settings) in.readObject();
         // Workaround: if the constants have changed, but not the difficulty
         // -> update the game speed
         settings_instance.setDifficulty(settings_instance.getDifficulty());
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } finally {
         if (in != null)
            try {
               in.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
      }
   }

   /**
    * Saves the highscore and stores the result on the disk.
    * 
    */
   public void saveHighScore(Player winner, int ticks) {
      boolean todo = true;
      for (IScore s : scores)
         if (s.getPlayerName().equals(winner.getName())) {
            if (s.getScore() < ticks * getGameSpeed())
               ((Score) s).setScore(ticks * getGameSpeed());
            todo = false;
         }

      if (todo)
         scores.add(new Score(winner.getName(), ticks * getGameSpeed()));
      this.saveSettingsToDisk();
   }

   public int getGameSpeed() {
      return gameSpeed;
   }

   public void setKeyboardLayoutP1(KeyBean keyboardLayoutP1) {
      this.keyboardLayoutP1 = keyboardLayoutP1;
   }

   public KeyBean getKeyboardLayoutP1() {
      return keyboardLayoutP1;
   }

   public void setKeyboardLayoutP2(KeyBean keyboardLayoutP2) {
      this.keyboardLayoutP2 = keyboardLayoutP2;
   }

   public KeyBean getKeyboardLayoutP2() {
      return keyboardLayoutP2;
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   public Locale getLocale() {
      return locale;
   }

   public void setWeaponRechargeTime(int weaponRechargeTime) {
      this.weaponRechargeTime = weaponRechargeTime;
   }

   public int getWeaponRechargeTime() {
      return weaponRechargeTime;
   }

   public void setMaxPlayerLength(int maxPlayerLength) {
      this.maxPlayerLength = maxPlayerLength;
   }

   public int getMaxPlayerLength() {
      return maxPlayerLength;
   }

   public void setP1(Player p1) {
      this.p1 = p1;
   }

   public Player getP1() {
      return p1;
   }

   public void setP2(Player p2) {
      this.p2 = p2;
   }

   public Player getP2() {
      return p2;
   }

   @Override
   public Locale getLanguage() {
      return locale;
   }

   @Override
   public IPlayer getPlayer1() {
      return p1;
   }

   @Override
   public IPlayer getPlayer2() {
      return p2;
   }

   public Constants.DIFFICULTY getDifficulty() {
      return difficulty;
   }

   /**
    * Setting the difficulty to a specific level, updates the speed of the game
    * as well.
    * 
    */
   public void setDifficulty(DIFFICULTY difficulty) {
      this.difficulty = difficulty;
      switch (difficulty) {
         case SLOW:
            gameSpeed = Constants.CELLS_PER_SECOND_SLOW;
            break;
         case NORMAL:
            gameSpeed = Constants.CELLS_PER_SECOND_NORMAL;
            break;
         case FAST:
            gameSpeed = Constants.CELLS_PER_SECOND_FAST;
            break;
         case GODLIKE:
            gameSpeed = Constants.CELLS_PER_SECOND_GODLIKE;
            break;
         default:
            gameSpeed = Constants.CELLS_PER_SECOND_NORMAL;
            break;
      }

   }

   @Override
   public void loadSettingsFromDisk() {
      Settings.getInstance();

   }

   @Override
   public void resetSettings() {
      Settings.loadDefaultSettings();
   }

   @Override
   public LinkedList<IScore> getHighScore() {
      return scores;
   }
}