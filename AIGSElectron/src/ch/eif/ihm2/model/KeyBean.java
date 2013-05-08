package ch.eif.ihm2.model;

import java.io.Serializable;
import ch.eif.ihm2.cst.Constants;

/**
 * Class to stock keyboard layouts
 * 
 * @author Bene
 * @version 1.0 - 29.12.2011
 */
@SuppressWarnings("serial")
public class KeyBean implements Serializable{
	private int up = 0;
	private int down = 0;
	private int left = 0;
	private int right = 0;
	private int shoot = 0;
	
	public int getUp() { return up;}
	public void setUp(int up) { this.up = up;}
	public int getDown() {return down;}
	public void setDown(int down) {this.down = down;}
	public int getLeft() {return left;}
	public void setLeft(int left) {this.left = left;}
	public int getRight() {return right;}
	public void setRight(int right) {this.right = right;}
	public int getShoot() {return shoot;}
	public void setShoot(int shoot) {this.shoot = shoot;}

	
	/**
	 * Internal Constructor
	 * 
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 * @param shoot
	 */
	private KeyBean(int up, int down, int left, int right, int shoot){
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.shoot = shoot;
	}
	
	
	/**
	 * Getting Standard Keys for Player 1
	 * 
	 * @return - KeyBean
	 */
	public static KeyBean getP1Standard(){
		return new KeyBean(Constants.P1_DEF_UP,Constants.P1_DEF_DN,Constants.P1_DEF_LT,Constants.P1_DEF_RT,Constants.P1_DEF_SH);
	}
	
	/**
	 * Getting Standard Keys for Player 2
	 * 
	 * @return
	 */
	public static KeyBean getP2Standard(){
		return new KeyBean(Constants.P2_DEF_UP,Constants.P2_DEF_DN,Constants.P2_DEF_LT,Constants.P2_DEF_RT,Constants.P2_DEF_SH);
	}
}
