package ch.eif.ihm2.ihm;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.eif.ihm2.cst.Constants;

/**
 * Singleton class used by the UI to translate text messages
 * @author Stefan Aebischer
 */
public class Translate {
	/* Singleton instance */
	private static Translate instance;
	/* I18n */
	private ResourceBundle rb;
	private Locale locale;
	
	/**
	 * Private Singleton Constructor
	 * @param locale - specific Locale
	 */
	private Translate(Locale locale){
		this.locale = locale;
		rb = ResourceBundle.getBundle(Constants.LANGRESSOURCE, locale);
	}
	/**
	 * static method, will use the singleton to translate a given key into its translated message
	 * @param key - key in the properties file
	 * @return - translated string
	 */
	public static String fromKey(String key){
		if(instance == null) instance = new Translate(Locale.getDefault());
		return instance.rb.getString(key);
	}
	/**
	 * like {@link fromKey(String key)} method with a single property parameter
	 * @param key - key in the properties file
	 * @param param - single parameter used in the property file entry
	 * @return - translated string
	 */
	public static String fromKeyWithParam(String key, Object param){
		if(instance == null) instance = new Translate(Locale.getDefault());
		String msg = instance.rb.getString(key);
		return MessageFormat.format(msg, param);
	}
	/**
	 * like {@link fromKey(String key)} method with a single property parameter
	 * @param key - key in the properties file
	 * @param params - parameters used in the property file entry
	 * @return - translated string
	 */
	public static String fromKeyWithParams(String key, Object[] params){
		if(instance == null) instance = new Translate(Locale.getDefault());
		String msg = instance.rb.getString(key);
		return MessageFormat.format(msg, params);
	}
	/**
	 * @return - the currently used locale for the translations
	 */
	public static Locale getLocale(){
		if(instance == null) instance = new Translate(Locale.getDefault());
		return instance.locale;
	}
	/**
	 * @param locale - sets the current locale for the translations
	 */
	public static void setLocale(Locale locale){
		instance = new Translate(locale);
		Locale.setDefault(locale); //without this MessageFormat.format doesnt work...
	}
}
