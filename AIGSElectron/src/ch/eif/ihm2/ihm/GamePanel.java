package ch.eif.ihm2.ihm;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import ch.eif.ihm2.cst.Constants;
import ch.eif.ihm2.model.IBullet;
import ch.eif.ihm2.model.IModelOperations;
import ch.eif.ihm2.model.ISegment;

/**
 * GamePanel - Contains the lines and bullets of each Player
 * @author Stefan Aebischer
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements PropertyChangeListener {

	private BufferedImage bgBuff, fgBuff,wpBuff;
	private Graphics2D bg2d,fg2d,wp2d;
	private Color c1 = Color.RED; //color of player 1, should be set if the menu disapears
	private Color c2 = Color.BLUE; //color of player 2
	private  int wwidth = Constants.WORLD_WIDTH*Constants.CELL_WIDTH;
	private int wheight = Constants.WORLD_HEIGHT*Constants.CELL_WIDTH;
	
	public GamePanel(IModelOperations model){
		super();
		init();
		model.addGamePropertyChangeListener(this);
	}
	/**
	 * setups the gamepanel screen (image buffers)
	 */
	private void init(){
		 bgBuff = new BufferedImage(Constants.CELL_WIDTH*Constants.WORLD_WIDTH,Constants.CELL_WIDTH*Constants.WORLD_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		 fgBuff = new BufferedImage(Constants.CELL_WIDTH*Constants.WORLD_WIDTH,Constants.CELL_WIDTH*Constants.WORLD_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		 wpBuff = new BufferedImage(Constants.CELL_WIDTH*Constants.WORLD_WIDTH,Constants.CELL_WIDTH*Constants.WORLD_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		 bg2d = bgBuff.createGraphics();
		 fg2d = fgBuff.createGraphics();
		 wp2d = wpBuff.createGraphics();
		 drawBackground();
		 
		 //testSegments(); //Remove me
		 //testBullets();
	}
	
	
	public void reset(){
		init();
	}
	public void setColorP1(Color c){
		c1 = c;
	}
	public void setColorP2(Color c){
		c2 = c;
	}
	
	
	/*private void testSegments(){
		Segment s1 = new Segment();
		s1.setFromX(0); s1.setFromY(50);
		s1.setToX(20); s1.setToY(50);
		
		Segment s2 = new Segment();
		s2.setFromX(Constants.WORLD_WIDTH); s2.setFromY(40);
		s2.setToX(40); s2.setToY(40);
		
		drawSegment(s1, Color.BLUE);
		//removeSegment(s1);
		//drawSegment(s1, Color.BLUE);
		drawSegment(s2, Color.RED);
	}*/
	
	/*private void testBullets(){
		Bullet b = new Bullet();
		b.setX(30);
		b.setY(10);
		drawShot(b, Color.BLUE);
		
		Bullet b2 = new Bullet();
		b2.setX(80);
		b2.setY(60);
		drawShot(b2, Color.RED);
	}*/
	
	/**
	 * prepares the background grid
	 */
	private void drawBackground(){
		/* GRID */
		int cellsize = Constants.CELL_WIDTH;
		Rectangle2D.Float r = new Rectangle2D.Float(0, 0, wwidth, wheight);
		bg2d.setPaint(Color.BLACK);
		bg2d.fill(r);
		bg2d.setPaint(new Color(0,102,0,100));
		bg2d.setStroke(new BasicStroke (8.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F));
		bg2d.draw(r);
		bg2d.setStroke(new BasicStroke(1));
		//bg2d.setPaint(new Color(7,48,0));
		Line2D l = new Line2D.Double(0, 0, 0, 0);
		for(int i = 0; i<Constants.WORLD_WIDTH;i++){
			l.setLine(i*cellsize, 0, i*cellsize, wheight);
			bg2d.draw(l);
		}
		for(int i = 0; i<Constants.WORLD_HEIGHT;i++){
			l.setLine(0, i*cellsize, wwidth, i*cellsize);
			bg2d.draw(l);
		}
	}
	

	/**
	 * @param iBullet - bullet to draw
	 * @param color - color of the bullet
	 */
	private void drawShot(IBullet iBullet, Color color) {
		int radius = Constants.BULLET_RADIUS*Constants.CELL_WIDTH;
		int radius2x = radius*2;
		int cellsize = Constants.CELL_WIDTH;
		Ellipse2D.Float point = new Ellipse2D.Float((iBullet.getX()*cellsize)-radius,(iBullet.getY()*cellsize)-radius,radius2x,radius2x);
		wp2d.setColor(color);
		wp2d.setStroke(new BasicStroke(0)); //ToDo
		wp2d.fill(point);
		//wp2d.draw(point);
	}

	/**
	 * will remove a specific segment of the player
	 * @param iSegment segment to remove
	 */
	private void removeSegment(ISegment iSegment) {
		int cellsize = Constants.CELL_WIDTH;
		fg2d.setStroke(new BasicStroke(2));
		fg2d.setComposite(AlphaComposite.Clear); //REMOVE the line
		Line2D l = new Line2D.Double(iSegment.getFromX()*cellsize, iSegment.getFromY()*cellsize, iSegment.getToX()*cellsize, iSegment.getToY()*cellsize);
		fg2d.setColor(Color.BLACK);
		fg2d.draw(l);
	}

	/**
	 * will draw a new segment on the grid
	 * @param iSegment - segment to draw
	 * @param color - color of the segment (player color)
	 */
	private void drawSegment(ISegment iSegment, Color color) {
		int cellsize = Constants.CELL_WIDTH;
		fg2d.setStroke(new BasicStroke(2));
		fg2d.setComposite(AlphaComposite.SrcOver); //ToDo CHeck 
		Line2D l = new Line2D.Double(iSegment.getFromX()*cellsize, iSegment.getFromY()*cellsize, iSegment.getToX()*cellsize, iSegment.getToY()*cellsize);
		fg2d.setColor(color);
		fg2d.draw(l);
	}

	/**
	 * repaints the gamepanel (wow, three layers!)
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		double height = (double) getHeight();
		double width = (double)getWidth();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		int x = (int)(width/2-(wwidth/2));
		int y = (int)(height/2-(wheight/2));
		g2d.drawImage(bgBuff,x,y,null);
		g2d.drawImage(fgBuff,x,y,null);
		g2d.drawImage(wpBuff,x,y,null);
	}
	
	/**
	 * called by the model when a game property has changed its value, which means that we have to repaint our screen!
	 */
	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		//System.out.println("property changed: "+ name);
		if(name.equals("add")){ //new segment
			ISegment[] seg = (ISegment[]) e.getNewValue();
			if(seg[0]!=null)drawSegment(seg[0], c1); //player 1
			if(seg[1]!=null)drawSegment(seg[1], c2); //player 2
		}
		else if(name.equals("remove")){ //tail of the players disapeard
			ISegment[] seg = (ISegment[]) e.getNewValue();
			if(seg[0]!=null)removeSegment(seg[0]);
			if(seg[1]!=null)removeSegment(seg[1]);
		}
		else if(name.equals("shot")){ //oh new bullet on the screen
			IBullet[] bullet = (IBullet[]) e.getNewValue();	
			if(bullet[0]!=null)drawShot(bullet[0], c1);
			if(bullet[1]!=null)drawShot(bullet[1], c2);
		}
		repaint();
	}

}
