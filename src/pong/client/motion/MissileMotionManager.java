package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public class MissileMotionManager extends MotionManager {

	
	public MissileMotionManager (MovingObject movingObject) {
		super(movingObject, null);
	}
	
	private void updatePosition() {
		MovingObject mo = getMovingObject();
		int x = (int) (mo.getVelocity() * Math.cos(Math.toRadians(mo.getAngle())));
		int y = (int) -(mo.getVelocity() * Math.sin(Math.toRadians(mo.getAngle())));
		mo.updatePosition(mo.getXPos() + x, mo.getYPos() + y);
	}
	
	@Override
	public void reset() {
		getMovingObject().reset();
	}

	@Override
	public void update() {
		updatePosition();
	}
}
