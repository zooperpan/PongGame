package pong.client.gui.pong;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
//import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;


public class Ball extends MovingObject {

	private static final long serialVersionUID	= 1L;
	public static final int STARTING_VELOCITY	= 10;
	private boolean tickTock					= false;
	private boolean transparent					= false;
	private ImageIcon image 					= new ImageIcon(this.getClass().getResource("/pong/client/gui/images/bombAnim.gif"));
	
	public Ball (int radious, Color color, String name) {
		super(radious, radious, color, name);
	}
	
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		if (tickTock) {
			Image img = image.getImage();
			g.drawImage(img, 0, 0, null);
			
//			g2.setColor(Color.BLACK);
//			Ellipse2D e2d = new Ellipse2D.Double(0,  this.getWidth() / 6, 2 * this.getWidth() / 3,  2 * this.getHeight() / 3);
//			Rectangle2D r2d = new Rectangle2D.Double(2 + getWidth() / 8, 1 + this.getHeight() / 8, (2 * getWidth() / 5) - 2, getHeight() / 6);
//			g2.fill(r2d);
//			g2.fill(e2d);
//			g2.setColor(Color.RED);
//			r2d = new Rectangle2D.Double(3 * getWidth() / 8, 0, 3, 4);
//			g2.fill(r2d);
		} else if (transparent) {
			g2.setColor(this.getColor());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
			Ellipse2D e2d = new Ellipse2D.Double(0,  this.getWidth() / 6, 2 * this.getWidth() / 3,  2 * this.getHeight() / 3);
			g2.fill(e2d);
		} else {
			g2.setColor(this.getColor());
			Ellipse2D e2d = new Ellipse2D.Double(0,  this.getWidth() / 6, 2 * this.getWidth() / 3,  2 * this.getHeight() / 3);
			g2.fill(e2d);
		}
	}
	
	public void setAngle (int angle) {
		if (angle == 90 || angle == 270) {
			angle = 0;
		}
		super.setAngle(angle);
	}
	
	public void reset () {
		super.reset();
		tickTock = false;
		transparent = false;
		setVelocity(STARTING_VELOCITY);
	}
	
	public void setTickTock (boolean tickTock) {
		this.tickTock = tickTock;
	}
	
	public void setTransparent (boolean transparent) {
		this.transparent = transparent;
	}
	
	public boolean isTickTock () {
		return tickTock;
	}
}
