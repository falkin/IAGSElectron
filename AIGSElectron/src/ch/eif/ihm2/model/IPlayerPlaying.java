package ch.eif.ihm2.model;

import java.awt.Color;

/**
 * Interface to a playing player bean. Only two setter methods are available.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public interface IPlayerPlaying {
   
   public int getWeaponStatus();
   public String getName();
   public Color getColor();
   
   public ISegment head();
   public ISegment tail();
   
   public void setDirection(Direction d);
   public void requestShot();
   
   public Direction getDirection();

}
