package pong.client.gui.pong;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import pong.client.gui.GUIStatic;

public class Missile extends MovingObject {

	private static final long serialVersionUID 	= 1L;
	public static final int STARTING_VELOCITY	= 20;
	private BufferedImage bufferedImage;
	private boolean transparent					= false;
	
	public Missile (int width, int height, Color color, String name) {
		super(width, height, color, name);
		try {
			bufferedImage = ImageIO.read(this.getClass().getResource("/pong/client/gui/images/missile.png"));
		} catch (MalformedURLException e) {
		} catch (IOException e) {}
		setVelocity(STARTING_VELOCITY);
	}
	
	public void paint (Graphics g) {
	
		if (transparent) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
			Rectangle2D e2d = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			g2.fill(e2d);
		} else {
			Image img = GUIStatic.rotate(bufferedImage, getAngle());
			g.drawImage(img, 0, 0, null);
		}
	}
	
	public void setAngle (int angle) {
		if (!(angle == 90 || angle == 270)) {
			super.setAngle(angle);
		}
	}
	
	public void reset () {
		super.reset();
		transparent = false;
		setVelocity(STARTING_VELOCITY);
	}
	
	public void setTransparent (boolean transparent) {
		this.transparent = transparent;
	}
}
