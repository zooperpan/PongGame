package pong.client.motion;

import pong.client.gui.pong.MovingObject;

public abstract class BounceManager {

	private int top, bottom, right, left;
	
	public BounceManager (int top, int bottom, int right, int left) {
		this.setTop(top);
		this.setBottom(bottom);
		this.setRight(right);
		this.setLeft(left);
	}

	public abstract void checkBounce (MovingObject mo);
	
	public void setTop (int top) {
		this.top = top;
	}

	public int getTop () {
		return top;
	}

	public void setBottom (int bottom) {
		this.bottom = bottom;
	}

	public int getBottom () {
		return bottom;
	}

	public void setRight (int right) {
		this.right = right;
	}

	public int getRight () {
		return right;
	}

	public void setLeft (int left) {
		this.left = left;
	}

	public int getLeft () {
		return left;
	}
	
}
