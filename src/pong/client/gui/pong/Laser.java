package pong.client.gui.pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Laser extends MovingObject {

	private static final long serialVersionUID = 1L;
	private int x1, y1, x2, y2;
	
	public Laser (int width, int height, String name) {
		super(width, height, Color.WHITE, name);
	}
	
	public void setPoints (int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(4.0f));
		g2.drawLine(x1, y1, x2, y2);
	}
}
