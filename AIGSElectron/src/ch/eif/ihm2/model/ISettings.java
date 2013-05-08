package ch.eif.ihm2.model;

import java.util.LinkedList;
import java.util.Locale;

import ch.eif.ihm2.cst.Constants;

/**
 * Interface to the settings bean.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public interface ISettings {

	public KeyBean getKeyboardLayoutP1();

	public KeyBean getKeyboardLayoutP2();

	public int getWeaponRechargeTime();

	public int getMaxPlayerLength();

	public Locale getLanguage();
	
	public void setLocale(Locale locale);
	
	public Constants.DIFFICULTY getDifficulty();

	public int getGameSpeed();

	public IPlayer getPlayer1();

	public IPlayer getPlayer2();
	
	public void saveSettingsToDisk();
	
	public void loadSettingsFromDisk();
	
	public void resetSettings();
	
	public void setWeaponRechargeTime(int weaponRechargeTime);
	
	public void setMaxPlayerLength(int maxPlayerLength);
	
	public void setDifficulty(Constants.DIFFICULTY difficulty);
	
	public void saveHighScore(Player winner, int ticks);
	
	public LinkedList<IScore> getHighScore();
}