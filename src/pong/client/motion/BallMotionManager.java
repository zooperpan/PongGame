package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public class BallMotionManager extends MotionManager {

	private static final int TIMEOUT = 300;
	private int count = TIMEOUT;
	
	public BallMotionManager (MovingObject movingObject, BallBounceManager ballBounceManager) {
		super(movingObject, ballBounceManager);
	}
	
	private void updatePosition() {
		MovingObject mo = getMovingObject();
		int x = (int) (mo.getVelocity() * Math.cos(Math.toRadians(mo.getAngle())));
		int y = (int) -(mo.getVelocity() * Math.sin(Math.toRadians(mo.getAngle())));
		mo.updatePosition(mo.getXPos() + x, mo.getYPos() + y);
	}
	
	private void updateVelocity() {
		MovingObject mo = getMovingObject();
		mo.setVelocity(mo.getVelocity() + 1);
	}

	@Override
	public void reset() {
		getMovingObject().reset();
	}

	@Override
	public void update() {
		updatePosition();
		count--;
		if (count == 0) {
			updateVelocity();
			count = TIMEOUT;
		}
	}
}
