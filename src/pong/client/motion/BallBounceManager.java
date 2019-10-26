package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public class BallBounceManager extends BounceManager {

	public BallBounceManager(int top, int bottom, int right, int left) {
		super(top, bottom, right, left);
	}
	
	public void checkBounce (MovingObject mo) {
		if (mo.getAngle() >= 0 && mo.getAngle() < 90) {
			if (mo.getYPos() - ((mo.getHeight() / 2) /*+ mo.getVelocity()*/) <= getTop()) {
				mo.setAngle(360 - mo.getAngle());
			}
		}
		else if (mo.getAngle() >= 90 && mo.getAngle() < 180) {
			if (mo.getYPos() - ((mo.getHeight() / 2) /*+ mo.getVelocity()*/) <= getTop()) {
				mo.setAngle(360 - mo.getAngle());
			}
		}
		else if (mo.getAngle() >= 180 && mo.getAngle() < 270) {
			if (mo.getYPos() + (mo.getHeight() / 2) /*+ mo.getVelocity()*/ >= getBottom()) {
				mo.setAngle(360 - mo.getAngle());
			}
		}
		else if (mo.getAngle() >= 270 && mo.getAngle() < 360) {
			if (mo.getYPos() + (mo.getHeight() / 2) /*+ mo.getVelocity()*/ >= getBottom()) {
				mo.setAngle(360 - mo.getAngle());
			}
		}
	}
}
