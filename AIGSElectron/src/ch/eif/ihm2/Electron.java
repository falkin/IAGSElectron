package ch.eif.ihm2;

import ch.eif.ihm2.ihm.GameFrame;
import ch.eif.ihm2.ihm.Translate;
import ch.eif.ihm2.model.IModelOperations;
import ch.eif.ihm2.model.Model;
import ch.eif.ihm2.model.Settings;


/**
 * Game Mainclass
 * @author 	Stefan Aebischer
 * 			Michael Heinzer
 * 			Bernhard Leutwiler
 */
public class Electron {
	
	public static GameFrame f;
	public static void main(String[] args) {
		Translate.setLocale(Settings.getInstance().getLanguage());
		IModelOperations m = new Model();
	    f = new GameFrame(m);
		f.setVisible(true); //have fun
	}
	

	
}