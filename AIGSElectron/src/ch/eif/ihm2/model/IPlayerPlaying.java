package ch.eif.ihm2.model;

import java.awt.Color;
import java.util.LinkedList;

/**
 * Interface to a playing player bean. Only two setter methods are available.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 * 
 */

public interface IPlayerPlaying {
	public LinkedList<Segment> getLogicalSegments(); 
	
	public LinkedList<Bullet> getBullets();
	
	public int getWeaponStatus();

	public String getName();

	public Color getColor();

	public ISegment head();

	public ISegment tail();

	public void setDirection(Direction d);

	public void requestShot();

	public Direction getDirection();
	
	public void reset(boolean isP1);
	
	public boolean hasCollided(IPlayerPlaying p2);
	
	public void rechargeWeapon(int amount); 
	
	public boolean isShotRequested() ;
	
	public boolean readyToShoot();
	
	public  IBullet fire();
	
	public ISegment move(int maxLength);

}
