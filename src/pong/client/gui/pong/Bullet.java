package pong.client.gui.pong;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class Bullet extends MovingObject {


	private static final long serialVersionUID 	= 1L;
	public static final int STARTING_VELOCITY	= 16;
	private boolean isActive = false;

	public Bullet (int height, int width, Color color) {
		super(height, width, color, "Bullet");
		setVelocity(STARTING_VELOCITY);
	}
	
	public void paint (Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		if (isActive) {
			g2.setColor(this.getColor());
		} else {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
		}
		Rectangle2D e2d = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		g2.fill(e2d);
	}
	
	public void setActive (boolean isActive) {
		this.isActive = isActive;
	}
	
	public void reset () {
		isActive = false;
		setVelocity(STARTING_VELOCITY);
		this.repaint();
	}

	public boolean isActive() {
		return isActive;
	}
}
