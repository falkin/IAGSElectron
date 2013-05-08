package ch.eif.ihm2.model;

import java.awt.Point;

/**
 * Bullet represents a shot from a player.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class Bullet implements IBullet {
   private int x;
   private int y;

   public Bullet(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }
   
   public Point getCenter(){
      return new Point(x,y);
   }
}