package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public class BarBounceManager extends BounceManager {

	public BarBounceManager(int top, int bottom, int right, int left) {
		super(top, bottom, right, left);
	}

	@Override
	public void checkBounce(MovingObject mo) {
		if (mo.getAngle() == 90 && mo.getVelocity() > 0 && mo.getYPos() - ((mo.getHeight() / 2) + 4) <= getTop()) {
			mo.setVelocity(0);
		} else if (mo.getAngle() == 270 && mo.getVelocity() > 0 && mo.getYPos() + (mo.getHeight() / 2) + 4 >= getBottom()) {
			mo.setVelocity(0);
		}
	}
}
