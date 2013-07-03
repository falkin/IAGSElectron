package ch.eif.ai;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import ch.eif.ihm2.cst.*;
import ch.eif.ihm2.model.*;

public class BorderPlayerPlaying extends Player implements IPlayerPlaying {

	private static final long serialVersionUID = 1L;

	private Direction direction;
	private int weaponStatus = 100;
	private LinkedList<Segment> segments = new LinkedList<Segment>();
	private LinkedList<Segment> logicalSegments = new LinkedList<Segment>();
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private boolean shotRequested = false;
	private boolean directionChanged = false;
	private Random random = new Random();
	private int getCut = 0;;
	private World world = World.getInstance();
	private  Random randomGenerator = new Random();
	/**
	 * Creates a Playing player for a normal player.
	 * 
	 */
	public BorderPlayerPlaying(String name, Color color) {
		super(name, color);
	}

	/**
	 * Creates a Playing player for a normal player.
	 * 
	 */
	public BorderPlayerPlaying(Player player) {
		this(player.getName(), player.getColor());
	}

	public Direction getDirection() {
		return direction;
	}

	/**
	 * Avoid changing the direction two times in the same tick
	 */
	public void setDirection(Direction d) {
		if (d != direction && !directionChanged) {
			direction = d;
			directionChanged = true;
		}
	}

	public LinkedList<Segment> getSegments() {
		return segments;
	}

	@Override
	public int getWeaponStatus() {
		return weaponStatus;
	}

	/**
	 * Recharges the weapon with a given amount, if the weaponStatus is more
	 * than 100%, the surplus is deducted.
	 * 
	 */
	public void rechargeWeapon(int amount) {
		if (weaponStatus < 100) {
			weaponStatus += amount;
			if (weaponStatus > 100)
				weaponStatus -= weaponStatus - 100;
		}
	}

	public boolean readyToShoot() {
		return weaponStatus >= Constants.BULLET_COST;
	}

	/**
	 * Returns a bullet for a fire instruction. Tests if the bullet is within
	 * the playing field
	 * 
	 */
	public IBullet fire() {
		int posX = head().getToX();
		int posY = head().getToY();
		// Calculate the impact position of the bullet
		switch (this.direction) {
		case UP:
			posY -= Constants.WEAPON_FIRE_RANGE;
			break;
		case DOWN:
			posY += Constants.WEAPON_FIRE_RANGE;
			break;
		case LEFT:
			posX -= Constants.WEAPON_FIRE_RANGE;
			break;
		case RIGHT:
			posX += Constants.WEAPON_FIRE_RANGE;
			break;
		default:
			return null;
		}
		// Pay for the bullet
		weaponStatus -= Constants.BULLET_COST;
		// Is the position valid?
		if (posX < 0 || posY < 0 || posY >= Constants.WORLD_HEIGHT
				|| posX >= Constants.WORLD_WIDTH)
			return null;
		Bullet b = new Bullet(posX, posY);
		bullets.add(b);
		return b;
	}

	/**
	 * Tests if this player has collided with anything on the field, tails or
	 * bullets. This is not very efficient, but we lack the time to use a proper
	 * data structure (for example a two dimensional interval tree)
	 */
	public boolean hasCollided(IPlayerPlaying p2) {
		int headx = head().getToX();
		int heady = head().getToY();
		// Test if the player has left the world
		if (headx < 0 || headx > Constants.WORLD_WIDTH)
			return true;
		if (heady < 0 || heady > Constants.WORLD_HEIGHT)
			return true;
		// Test if the two heads collide
		if (p2 !=null && headx == p2.head().getToX() && heady == p2.head().getToY())
			return true;
		// Test if the player has crashed into a tail
		for (Segment s : logicalSegments)
			if (s.onSegment(headx, heady))
				return true;
		if (p2 !=null)
			for (Segment s : p2.getLogicalSegments())
				if (s.onSegment(headx, heady))
					return true;
		// Test if the player has collided with a bullet
		Point p = new Point(headx, heady);
		for (Bullet b : bullets)
			if (p.distance(b.getCenter()) <= Constants.BULLET_RADIUS)
				return true;
		if (p2 !=null)
			for (Bullet b : p2.getBullets())
				if (p.distance(b.getCenter()) <= Constants.BULLET_RADIUS)
					return true;
		return false;
	}

	/**
	 * Creates a new segment for the player. Tests bounds of the playing field.
	 * 
	 */
	public ISegment move(int maxLength) {
		directionChanged = false;
		int posX = head().getToX();
		int posY = head().getToY();

		switch (this.direction) {
		case UP:
			if (!world.isObstacle(posX, posY - 1)) {
				posY--;
			} else {
				direction = Direction.LEFT;
				posX--;
			}
			break;
		case DOWN:
			if (!world.isObstacle(posX, posY+1)) {
				posY++;
			} else {
				direction = Direction.RIGHT;
				posX++;
			}
			break;
		case LEFT:
			if (!world.isObstacle(posX-1, posY)) {
				posX--;
			} else {
				direction = Direction.DOWN;
				posY++;
			}
			break;
		case RIGHT:
			if (!world.isObstacle(posX+1, posY)) {
				posX++;
			} else {
				direction = Direction.UP;
				posY--;
			}
			break;
		default:
			return null;
		}

		Segment seg = new Segment(head().getToX(), head().getToY(), posX, posY);
		segments.addLast(seg);
		Segment retVal = null;

		getCut+=1;
		if ( getCut>2){
			retVal = segments.pollFirst();
			getCut = randomGenerator.nextInt(16);
		}
		updateLogicalSegments(seg, retVal);

		return retVal;
	}

	public void updateLogicalSegments(Segment front, Segment tail) {
		if (logicalSegments.getLast().getDirection() == front.getDirection())
			logicalSegments.getLast().add(front);
		else
			logicalSegments.add(front.clone());
		// DEBUG
		if (logicalSegments.getLast().getToX() != front.getToX()
				|| logicalSegments.getLast().getToY() != front.getToY())
			System.out.println("Head of logical segments het gicht! Is: "
					+ logicalSegments.getLast() + " should be: " + front);
		if (tail != null) {
			if (logicalSegments.getFirst().getLength() == 0)
				logicalSegments.pollFirst();
			logicalSegments.getFirst().subtract(tail);
			// DEBUG
			if (logicalSegments.getFirst().getFromX() != tail.getToX()
					|| logicalSegments.getFirst().getFromY() != tail.getToY())
				System.out.println("Tail of logical segments het Gicht! Is: "
						+ logicalSegments.getFirst() + " should be: " + tail);
		}
	}

	/**
	 * Returns the head or first segment of the player
	 * 
	 */
	public ISegment head() {
		return segments.getLast();
	}

	/**
	 * Returns the tail or the last segment of the player
	 * 
	 */
	public ISegment tail() {
		return segments.getFirst();
	}

	/**
	 * Informs the player that a shot is requested.
	 * 
	 */
	public void requestShot() {
		setShotRequested(true);
	}

	public boolean hasDirectionChanged() {
		return directionChanged;
	}

	public void setShotRequested(boolean shotRequested) {
		this.shotRequested = shotRequested;
	}

	/**
	 * Removes all segments and bullets from this player. The position is reset
	 * to the start.
	 * 
	 */
	public void reset(boolean isP1) {
		while (!segments.isEmpty())
			segments.remove();
		while (!bullets.isEmpty())
			bullets.remove();
		Segment first;

		if (isP1)
			setDirection(Direction.DOWN);
		else
			setDirection(Direction.UP);
		int x = random.nextInt(Constants.WORLD_WIDTH);
		if (isP1){
			first = new Segment(x, 0, x, 1);
			
		}
		else{
			first = new Segment(x, Constants.WORLD_HEIGHT, x,
					Constants.WORLD_HEIGHT - 1);
		

			}
		segments.add(first);
		logicalSegments.add(first.clone());
		
	}

	/**
	 * Informs the caller if a shot has been requested by a command, will reset
	 * the variable after the call.
	 * 
	 */
	public boolean isShotRequested() {
		boolean tempRequested = shotRequested;
		shotRequested = false;
		return tempRequested;
	}

	@Override
	public LinkedList<Segment> getLogicalSegments() {
		return logicalSegments;
	}

	@Override
	public LinkedList<Bullet> getBullets() {
		return bullets;
	}

	@Override
	public void setScore(int score, int numberGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}
}
