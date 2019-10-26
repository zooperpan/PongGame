package pong.client.gui.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Explosion extends MovingObject {

	private static final long serialVersionUID = 1L;
	private ImageIcon image = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/explosion.gif"));
	private Image img;
	
	public Explosion (int width, int height, Color color) {
		super(width, height, color, "Explosion");
		img = image.getImage();
	}
	
	public void paint (Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}
