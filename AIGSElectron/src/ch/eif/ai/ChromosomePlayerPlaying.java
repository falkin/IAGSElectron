package ch.eif.ai;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jgap.*;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.event.EventManager;
import org.jgap.impl.*;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;

import ch.eif.ihm2.cst.*;
import ch.eif.ihm2.model.*;

public class ChromosomePlayerPlaying extends Player implements IPlayerPlaying {

	private static final long serialVersionUID = 1L;

	private static Configuration configuration;
	private static int popSize = 10;
	private static Genotype population;

	private Direction direction;
	private int weaponStatus = 100;
	private LinkedList<Segment> segments = new LinkedList<Segment>();
	private LinkedList<Segment> logicalSegments = new LinkedList<Segment>();
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private boolean shotRequested = false;
	private boolean directionChanged = false;
	private Random random = new Random();
	private int getCut = 0;
	private World world = World.getInstance();
	private Random randomGenerator = new Random();

	private boolean evolve=true;
	private static double[] score = new double[popSize + 1];
	private static int currentChromosome = 0;

	public void setScore(int score, int partie) {
		score *= Settings.getInstance().getGameSpeed();
		double s = 0;
		if (score < 0) {
			s = ((-0.5 / 500000) * score);
			if (s > 0.5) {
				s = 0.5;
			}
		} else {
			s = 1 - ((0.5 / 500000) * score);
		}
		if (s < 0) {
			s = 0;
		} else if (s > 1) {
			s = 1;
		}
		System.out.println(s);
		ChromosomePlayerPlaying.score[currentChromosome] = s;
		if (currentChromosome == popSize - 1) {
			try {
				evolution();
				currentChromosome = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			currentChromosome++;
		}
		Chromosome chromo = (Chromosome) population.getPopulation()
				.getChromosomes().get(currentChromosome);
		System.out.println(Integer.parseInt(chromo.getGene(1).getAllele()
				.toString()));
	}

	/**
	 * Creates a Playing player for a normal player.
	 * 
	 */
	public ChromosomePlayerPlaying(String name, Color color) {
		super(name, color);
		init();
	}

	/**
	 * Creates a Playing player for a normal player.
	 * 
	 */
	public ChromosomePlayerPlaying(Player player) {
		this(player.getName(), player.getColor());
		init();
	}

	private void init() {
		if (population == null) {
			try {
				Configuration.reset();
				configuration = new Configuration("ChromosomePlayerPlaying");
				// -------------------- Custom configuration
				// ----------------------------------
				// fitness function
				configuration
						.setFitnessEvaluator(new DefaultFitnessEvaluator());
				// selection
				BestChromosomesSelector bestChromsSelector;
				bestChromsSelector = new BestChromosomesSelector(configuration,
						0.6);
				configuration.addNaturalSelector(bestChromsSelector, false);
				// elitism
				configuration.setPreservFittestIndividual(true);
				configuration.setKeepPopulationSizeConstant(false);
				// random generator
				configuration.setRandomGenerator(new StockRandomGenerator());
				configuration.setEventManager(new EventManager());
				// crossover
				configuration.addGeneticOperator(new CrossoverOperator(
						configuration, 0.35, true));
				// mutation
				configuration.addGeneticOperator(new MutationOperator(
						configuration, 2));
				int i;

				Gene[] sampleGenes = new Gene[10000];
				for (i = 0; i < sampleGenes.length; i++) {
					sampleGenes[i] = new IntegerGene(configuration, 0, 2);
				}

				IChromosome sampleChromosome = new Chromosome(configuration,
						sampleGenes);
				configuration.setSampleChromosome(sampleChromosome);

				// Finally, we need to tell the Configuration object how many
				// Chromosomes we want in our population. The more Chromosomes,
				// the larger number of potential solutions (which is good for
				// finding the answer), but the longer it will take to evolve
				// the population (which could be seen as bad).
				configuration.setPopulationSize(popSize);
				PlayerFitnessFunction fitnessFunction = new PlayerFitnessFunction();
				configuration.setFitnessFunction(fitnessFunction);
				if (population == null) {
					try {
						Document doc = XMLManager.readFile(new File(
								"Chromosome.xml"));
						population = XMLManager.getGenotypeFromDocument(
								configuration, doc);
					} catch (UnsupportedRepresentationException uex) {
						// JGAP codebase might have changed between two
						// consecutive
						// runs.
						// --------------------------------------------------------------
						System.out
								.println("Previous population has a different configuration");
						population = Genotype
								.randomInitialGenotype(configuration);
					} catch (FileNotFoundException fex) {
						System.out.println("Previous population not found");
						population = Genotype
								.randomInitialGenotype(configuration);
					} catch (Exception e) {
						population = Genotype
								.randomInitialGenotype(configuration);
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void evolution() throws Exception {
		if(evolve){
		for (IChromosome c : population.getPopulation().getChromosomes()) {
			c.setFitnessValueDirectly(score[(popSize - 1) - currentChromosome--]);
		}
		List<IChromosome> pop;
		BufferedWriter logger = new BufferedWriter(new FileWriter(
				"evolutionLog.txt"));
		logger.write("Size\tMin\tMax\tAvg\n");

		DataTreeBuilder builder = DataTreeBuilder.getInstance();
		IDataCreators doc2 = builder.representGenotypeAsDocument(population);
		// create XML document from generated tree
		XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
		Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
		XMLManager.writeFile(xmlDoc, new File("Chromosome.xml"));
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		System.out.println("The best solution has a fitness value of "
				+ bestSolutionSoFar.getFitnessValue());
		BufferedWriter writer = new BufferedWriter(new FileWriter("genome"));
		for (int i = 0; i < 10000; i++) {
			writer.write(bestSolutionSoFar.getGene(i).getAllele().toString());
			if (i < 9999) {
				writer.write("\t");
			}
		}
		writer.write("\n");
		writer.flush();
		writer.close();
		currentChromosome = (popSize - 1);
		population.evolve();
		}
	}

	public void setEvolve(boolean evolve){
		this.evolve=evolve;
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
		if (p2 != null && headx == p2.head().getToX()
				&& heady == p2.head().getToY())
			return true;
		// Test if the player has crashed into a tail
		for (Segment s : logicalSegments)
			if (s.onSegment(headx, heady))
				return true;
		if (p2 != null)
			for (Segment s : p2.getLogicalSegments())
				if (s.onSegment(headx, heady))
					return true;
		// Test if the player has collided with a bullet
		Point p = new Point(headx, heady);
		for (Bullet b : bullets)
			if (p.distance(b.getCenter()) <= Constants.BULLET_RADIUS)
				return true;
		if (p2 != null)
			for (Bullet b : p2.getBullets())
				if (p.distance(b.getCenter()) <= Constants.BULLET_RADIUS)
					return true;
		return false;
	}

	/**
	 * Creates a new segment for the player. Tests bounds of the playing field.
	 * 
	 */

	public int shot = 100;
	public ISegment move(int maxLength) {
//		shot--;
//		if(shot==0){
//			shot=100;
//			requestShot();
//		}
		Chromosome chromo = (Chromosome) population.getPopulation()
				.getChromosomes().get(currentChromosome);
		int posX = head().getToX();
		int posY = head().getToY();
		double[] d = world.getTrackObstaclSensors(posX, posY, getColor());
		int count = 0;
		String s = "";
		for (int i = 0; i < d.length; i++) {
			double value = d[i]+1;
			if (value == 0) {
				count++;
			}
			if (value > 9) {
				value = 9;
			}
			if(value==1 && i==0 && direction==Direction.LEFT){
				value--;
			}
			else if(value==1 && i==1 && direction==Direction.RIGHT){
				value--;
			}
			else if(value==1 && i==2 && direction==Direction.UP){
				value--;
			}
			else if(value==1 && i==3 && direction==Direction.DOWN){
				value--;
			}
			s += (int) value;
		}
		if (count == 2) {
			System.out.println();
		}
		int key = Integer.valueOf(s);


		int value = Integer
				.parseInt(chromo.getGene(key).getAllele().toString());

//		if(key==8092 || key==838 || key==839){
//			value=2;
//		}
//		if(key==9690 || key==9082){
//			value=1;
//		}
//		if(key==1901){
//			value=1;
//		}
//		if(key==3029 || key==9059){
//			value=1;
//		}
		int c=0;
		while(c<3){
		if(direction==Direction.UP && s.subSequence(0, 1).equals("1") && value==2){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.UP && s.subSequence(1, 2).equals("1") && value==0){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.UP && s.subSequence(3, 4).equals("1") && value==1){
			value+=2;
			value%=3;
		}
		if(direction==Direction.DOWN && s.subSequence(0, 1).equals("1") && value==0){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.DOWN && s.subSequence(1, 2).equals("1") && value==2){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.DOWN && s.subSequence(2, 3).equals("1") && value==1){
			value+=2;
			value%=3;
		}
		if(direction==Direction.RIGHT && s.subSequence(0, 1).equals("1") && value==1){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.RIGHT && s.subSequence(2, 3).equals("1") && value==2){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.RIGHT && s.subSequence(3, 4).equals("1") && value==0){
			value+=2;
			value%=3;
		}
		if(direction==Direction.LEFT && s.subSequence(1, 2).equals("1") && value==1){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.LEFT && s.subSequence(2, 3).equals("1") && value==0){
			value+=2;
			value%=3;
		}
		else if(direction==Direction.LEFT && s.subSequence(3, 4).equals("1") && value==2){
			value+=2;
			value%=3;
		}
		c++;
	}
		
		
		switch (direction) {
		case UP:
			if (value == 0) {
				posX = posX - 1;
				direction = Direction.LEFT;
			} else if (value == 1) {
				posY = posY - 1;
				direction = Direction.UP;
			} else if (value == 2) {
				posX = posX + 1;
				direction = Direction.RIGHT;
			}
			break;
		case DOWN:
			if (value == 0) {
				posX = posX + 1;
				direction = Direction.RIGHT;
			} else if (value == 1) {
				posY = posY + 1;
				direction = Direction.DOWN;
			} else if (value == 2) {
				posX = posX - 1;
				direction = Direction.LEFT;
			}
			break;
		case LEFT:
			if (value == 0) {
				posY = posY + 1;
				direction = Direction.DOWN;
			} else if (value == 1) {
				posX = posX - 1;
				direction = Direction.LEFT;
			} else if (value == 2) {
				posY = posY - 1;
				direction = Direction.UP;
			}
			break;
		case RIGHT:
			if (value == 0) {
				posY = posY - 1;
				direction = Direction.UP;
			} else if (value == 1) {
				posX = posX + 1;
				direction = Direction.RIGHT;
			} else if (value == 2) {
				posY = posY + 1;
				direction = Direction.DOWN;
			}
			break;
		}
		System.out.print(key + " " + posX + " " + posY + " " + value + " "
				+ currentChromosome + " " +s.subSequence(0, 1));
		System.out.println();

		//
		// switch (this.direction) {
		// case UP:
		// if (!world.isObstacle(posX, posY - 1)) {
		// posY--;
		// }
		// else{
		// if(Integer.parseInt(chromo.getGene(0).getAllele().toString())==0){
		// direction=Direction.LEFT;
		// posX--;
		// }
		// if(Integer.parseInt(chromo.getGene(0).getAllele().toString())==1){
		// direction=Direction.RIGHT;
		// posX++;
		// }
		// }
		// break;
		// case DOWN:
		// if (!world.isObstacle(posX, posY + 1)) {
		// posY++;
		// } else {
		// direction = Direction.RIGHT;
		// posX++;
		// }
		// break;
		// case LEFT:
		// if (!world.isObstacle(posX - 1, posY)) {
		// posX--;
		// } else {
		// direction = Direction.DOWN;
		// posY++;
		// }
		// break;
		// case RIGHT:
		// if (!world.isObstacle(posX + 1, posY)) {
		// posX++;
		// } else {
		// direction = Direction.UP;
		// posY--;
		// }
		// break;
		// default:
		// return null;
		// }

		Segment seg = new Segment(head().getToX(), head().getToY(), posX, posY);
		segments.addLast(seg);
		Segment retVal = null;

		getCut += 1;
		if (getCut > 2) {
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
		if (isP1) {
			first = new Segment(x, 0, x, 1);

		} else {
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

	public class PlayerFitnessFunction extends FitnessFunction {

		private static final long serialVersionUID = 1L;

		protected double evaluate(IChromosome arg0) {
			return 0; // score[4 - currentChromosome--];
		}
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}
