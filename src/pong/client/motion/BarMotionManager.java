package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public class BarMotionManager extends MotionManager {

	public BarMotionManager (MovingObject movingObject, BarBounceManager barBounceManager) {
		super(movingObject, barBounceManager);
	}

	@Override
	public void update () {
		
		MovingObject mo = getMovingObject();
		if (mo.getVelocity() > 0) {
			if (mo.getAngle() == 90) {
				mo.updatePosition(mo.getXPos(), mo.getYPos() - mo.getVelocity());
			} else if (mo.getAngle() == 270) {
				mo.updatePosition(mo.getXPos(), mo.getYPos() + mo.getVelocity());
			}
		}
	}

	@Override
	public void reset () {
		getMovingObject().reset();
	}
}
