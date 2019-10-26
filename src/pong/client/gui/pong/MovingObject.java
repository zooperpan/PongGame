package pong.client.gui.pong;

import java.awt.Color;


public class MovingObject extends PongObject {

	private static final long serialVersionUID = 1L;
	private int velocity = 0;
	private int angle = 0;
	private int startingX, startingY;
	private String name;

	public MovingObject (int width, int height, Color color, String name) {
		super(width, height, color);
		this.name = name;
	}

	public void updatePosition (int x, int y) {
		this.setXPos(x);
		this.setYPos(y);
	}
	
	public void setStartingPosition (int x, int y) {
		startingX = x;
		this.setXPos(x);
		startingY = y;
		this.setYPos(y);
	}
	
	public void setVelocity (int velocity) {
		this.velocity = velocity;
	}
	
	public int getVelocity () {
		return velocity;
	}
	
	public void setAngle (int angle) {
		if (angle >= 360) this.angle = angle - 360;
		else if (angle < 0) this.angle = 360 + angle;
		else this.angle = angle;
	}
	
	public int getAngle () {
		return angle;
	}

	public void reset() {
		this.setXPos(startingX);
		this.setYPos(startingY);
	}
	
	public String getName () {
		return name;
	}
}
