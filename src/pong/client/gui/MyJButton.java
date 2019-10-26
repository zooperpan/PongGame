package pong.client.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class MyJButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	private BufferedImage imageIcon;
	
	public MyJButton (BufferedImage imageIcon) {
		super();
		setPreferredSize(new Dimension(imageIcon.getWidth() + 34, imageIcon.getHeight()));
		this.imageIcon = imageIcon;
		setOpaque(false);
	}
	
	public void paint(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g.create();
    	
    	Rectangle2D rect;
    	g2.setColor(Color.BLACK);
    	rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    	g2.fill(rect);
    	
    	if (!isEnabled()) g2.setColor(new Color(225, 225, 225));
    	else g2.setColor(new Color(51, 51, 51));
    	rect = new Rectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2);
    	g2.fill(rect);
    	
    	if (imageIcon != null) {
        	if (!isEnabled()) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2));
        	g2.drawImage(imageIcon, 
        			(getWidth() / 2) - (imageIcon.getWidth() / 2), 
        			(getHeight() / 2) - (imageIcon.getHeight() / 2), null);
    	}
    	g2.dispose();
	}
}
