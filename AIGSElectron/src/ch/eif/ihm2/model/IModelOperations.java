package ch.eif.ihm2.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.eif.ihm2.exception.CollisionDetectedException;
import ch.eif.ihm2.ihm.GameFrame;
import ch.eif.ihm2.manager.GameState;

/**
 * Interface to the controller of the application.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public interface IModelOperations {

	public ISettings getSettings();
	public void addGamePropertyChangeListener(PropertyChangeListener pcl);
	public void addInfoPropertyChangeListener(PropertyChangeListener pcl);
	public PropertyChangeSupport getInfoPropertyChangeSupport();
	
	public void tick() throws CollisionDetectedException;
	public void terminateGame(GameState state);
	public void init(GameFrame frame);
	
}