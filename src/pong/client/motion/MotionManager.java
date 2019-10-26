package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public abstract class MotionManager {

	private MovingObject movingObject;
	private BounceManager bounceManager;
	
	public MotionManager (MovingObject movingObject, BounceManager bounceManager) {
		this.movingObject = movingObject;
		this.bounceManager = bounceManager;
	}
	
	public abstract void update ();
	
	public MovingObject getMovingObject () {
		return movingObject;
	}
	
	public BounceManager getBounceManager () {
		return bounceManager;
	}

	public abstract void reset();
}
