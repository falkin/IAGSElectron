package ch.eif.ihm2.cst;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * Central class for all constants.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class Constants {
   // Shared constants
   public static final Locale DEFAULT_LOCALE = new Locale("en");
   public static final int WORLD_WIDTH = 180;
   public static final int WORLD_HEIGHT = 140;
   public static final int BULLET_RADIUS = 2;
   public static final int CELL_WIDTH = 4;
   
   // Model constants
   public static final int BULLET_COST = 100; //or changed back to 25
   public static final int WEAPON_FIRE_RANGE = 40;
   public static final int PLAYER_MAXLENGTH_FACTOR = 20;
   public static final int CELLS_PER_SECOND_SLOW = 10;
   public static final int CELLS_PER_SECOND_NORMAL = 20;
   public static final int CELLS_PER_SECOND_FAST = 30;
   public static final int CELLS_PER_SECOND_GODLIKE = 60;
   
   // IHM constants
   public static final String LANGRESSOURCE = "ch.eif.ihm2.ressources.electron";
   public static final int MENUWIDTH = 400;
   public static final int MENUHEIGHT = 300;
   public static final int P1_DEF_UP = KeyEvent.VK_W;
   public static final int P1_DEF_DN = KeyEvent.VK_S;
   public static final int P1_DEF_LT = KeyEvent.VK_A;
   public static final int P1_DEF_RT = KeyEvent.VK_D;
   public static final int P1_DEF_SH = KeyEvent.VK_SPACE;
   public static final int P2_DEF_UP = KeyEvent.VK_UP;
   public static final int P2_DEF_DN = KeyEvent.VK_DOWN;
   public static final int P2_DEF_LT = KeyEvent.VK_LEFT;
   public static final int P2_DEF_RT = KeyEvent.VK_RIGHT;
   public static final int P2_DEF_SH = KeyEvent.VK_ENTER;
   public static final int MAX_HIGHSCORE_ENTRIES = 5;
   public static final int MINIMAL_COLOR_DIFFERENCE = 80; // 10 - 100 (very close - not very close)
   public static final int COLOR_BRIGHTNESS_LEVEL = 120; //100 - 150 (quiet dark - middletone)
   public static final int MAX_RECTIME = 5000;
   public static final int MAX_QUEUELEN = 5000;
   
   // Controller constants
   public static final int TIMER_BREAK = 20;
   public static final int NBR_GAME = 5;
   // Settings constants
   public static final String DEFAULT_P1_NAME = "Player 1";
   public static final String DEFAULT_P2_NAME = "Player 2";
   public static final String DEFAULT_AI  = "AI";
   public static final  String[] NAME_P1_LIST = {  DEFAULT_P1_NAME, DEFAULT_AI};
   public static final  String[] NAME_P2_LIST = {  DEFAULT_P2_NAME, DEFAULT_AI};
   public static final String SETTINGS_FILENAME = "settings.ser";
   public static final Color DEFAULT_P1_COLOR = Color.RED;
   public static final Color DEFAULT_P2_COLOR = Color.BLUE;
   public static enum DIFFICULTY {SLOW,NORMAL,FAST,GODLIKE};
   public static final int DEFAULT_MAXPLAYERLENGTH = 20;
   public static final int DEFAULT_WEAPONRECHARGETIME = 0;
   public static final String[] LANGUAGES = {"en","de","fr"};
   
}
