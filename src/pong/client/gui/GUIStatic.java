package pong.client.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class GUIStatic {

	public static void setDialogCenterLocation (JDialog dialog) {
		Dimension dlgSize = dialog.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    	if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
    	dialog.setLocation((screenSize.width - dlgSize.width) / 2,
    			(screenSize.height - dlgSize.height) / 2);
	}
	
	public static Image rotate (BufferedImage bsrc, int angle) {
		
		double radians = Math.toRadians(360 - angle);
	    double sin = Math.abs(Math.sin(radians)), cos = Math.abs(Math.cos(radians));
	    int w = bsrc.getWidth(), h = bsrc.getHeight();
	    int neww = (int)Math.floor((w * cos) + (h * sin)), newh = (int)Math.floor((h * cos) + (w * sin));
	    GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	    BufferedImage bdest = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	    Graphics2D g = bdest.createGraphics();
	    
	    g.translate((neww - w) / 2, (newh - h) / 2);
	    g.rotate(radians, w / 2, h / 2);
	    g.drawRenderedImage(bsrc, null);
	    g.dispose();

	    return  (new ImageIcon(bdest).getImage());
	}
}
