package pong.client.gui.pong;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;

public class PongObject extends JComponent {

	private static final long serialVersionUID = 1L;
	private int height, width;
	private int xPos, yPos;
	private Color color;
	
	public PongObject (int width, int height, Color color) {
		
		super();
		this.setColor(color);
		this.setHeight(height);
		this.setWidth(width);
		this.setPreferredSize(new Dimension(width, height));
		this.setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
	}
	
	public void setXPos (int x) {
		this.xPos = x;
		this.setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
	}
	
	public void setYPos (int y) {
		this.yPos = y;
		this.setBounds(getXPos() - (getWidth() / 2), getYPos() - (getHeight() / 2), getWidth(), getHeight());
	}
	
	public int getXPos () {
		return xPos;
	}
	
	public int getYPos () {
		return yPos;
	}
	
	public void setHeight (int height) {
		this.height = height;
	}
	
	public int getHeight () {
		return height;
	}
	
	public void setWidth (int width) {
		this.width = width;
	}
	
	public int getWidth () {
		return width;
	}
	
	public void setColor (Color color) {
		this.color = color;
	}
	
	public Color getColor () {
		return color;
	}
}
