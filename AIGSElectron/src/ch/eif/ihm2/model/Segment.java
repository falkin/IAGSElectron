package ch.eif.ihm2.model;


/**
 * A segment is a part of a players line on the screen
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class Segment implements ISegment{
   
   private int fromX;
   private int fromY;
   private int toX;
   private int toY;
   
   public Segment(int fromX, int fromY, int toX, int toY){
      this.fromX = fromX;
      this.fromY = fromY;
      this.toX = toX;
      this.toY = toY;
   }
   
   public void setFromX(int fromX) {
      this.fromX = fromX;
   }
   public int getFromX() {
      return fromX;
   }
   public void setFromY(int fromY) {
      this.fromY = fromY;
   }
   public int getFromY() {
      return fromY;
   }
   public void setToX(int toX) {
      this.toX = toX;
   }
   public int getToX() {
      return toX;
   }
   public void setToY(int toY) {
      this.toY = toY;
   }
   public int getToY() {
      return toY;
   }
   
   public String toString(){
      return "[F("+fromX+","+fromY+"),T("+toX+","+toY+")]";
   }
   
   public Direction getDirection(){
      if(fromX == toX)
         if(fromY > toY)
            return Direction.UP;
         else
            return Direction.DOWN;
      else
         if(fromX > toX)
            return Direction.LEFT;
         else
            return Direction.RIGHT;      
   }
   
   public Segment clone(){      
      return new Segment(fromX, fromY, toX, toY);
   }
   
   public void subtract(Segment s){
      if(getDirection() == Direction.LEFT)
         fromX -= s.getLength();
      else if(getDirection() == Direction.RIGHT)
         fromX += s.getLength();
      else if(getDirection() == Direction.UP)
         fromY -= s.getLength();
      else
         fromY += s.getLength();
   }
   
   public void add(Segment s){
      if(getDirection() == Direction.LEFT)
         toX -= s.getLength();
      else if(getDirection() == Direction.RIGHT)
         toX += s.getLength();
      else if(getDirection() == Direction.UP)
         toY -= s.getLength();
      else
         toY += s.getLength();
         
   }
   
   public int getLength(){
      if(getDirection() == Direction.LEFT)
         return fromX-toX;
      else if(getDirection() == Direction.RIGHT)
         return toX-fromX;
      else if(getDirection() == Direction.UP)
         return fromY-toY;
      else
         return toY-fromY;
   }
   
   public boolean onSegment(int x, int y){
      if(getDirection() == Direction.LEFT)
         return (x <= fromX && x > toX) && (fromY == y);
      else if(getDirection() == Direction.RIGHT)
         return (x >= fromX && x < toX) && (fromY == y);
      else if(getDirection() == Direction.UP)
         return (y <= fromY && y > toY) && (fromX == x);
      else
         return (y >= fromY && y < toY) && (fromX == x);
   }

}
