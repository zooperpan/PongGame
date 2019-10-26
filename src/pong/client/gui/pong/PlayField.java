package pong.client.gui.pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PlayField extends PongObject {

	private static final long serialVersionUID = 1L;
	
	public PlayField (int height, int width, Color color) {
		super(height, width, color);
	}

	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(this.getColor());
		g2.setStroke(new BasicStroke(4.0f));
		Rectangle2D r2d = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		g2.draw(r2d);
	}
}
