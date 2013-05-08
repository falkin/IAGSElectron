package ch.eif.ihm2.model;

import java.awt.Color;

/**
 * Interface to a player bean.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public interface IPlayer{
  
   public Color getColor();
   public String getName();
   public void setColor(Color color);
   public void setName(String name);

}