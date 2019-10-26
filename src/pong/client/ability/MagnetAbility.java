package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Ball;
import pong.client.gui.pong.MovingObject;
import pong.com.command.AbilityCommand;

public class MagnetAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 20;
	private MagnetThread magnetThread	= null;

	public MagnetAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}

	@Override
	protected void startAbility () {
		if (isMaster()) {
			// Master uses left bar
			magnetThread = new MagnetThread(gamePanel.getLeftBar(), MagnetThread.LEFT_SIDE);
		} else {
			// Slave uses right bar
			magnetThread = new MagnetThread(gamePanel.getRightBar(), MagnetThread.RIGHT_SIDE);
		}
		gamePanel.addAbilityThread(this);
	}
	
	@Override
	public boolean run () {
		if (magnetThread != null) {
			return magnetThread.run();
		} else return true;
	}
	
	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	protected void stopAbility () {
		if (magnetThread != null) {
			magnetThread = null;
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}

	private class MagnetThread {

		public static final int	LEFT_SIDE	= 0;
		public static final int	RIGHT_SIDE	= 1;
		private static final int ANGLE 		= 5;
		private MovingObject target;
		private int side;

		public MagnetThread (MovingObject target, int side) {
			this.target = target;
			this.side = side;
		}

		public boolean run () {
			Ball ball = gamePanel.getBall();
			int angle = ball.getAngle();
			double A, B, C;
			int D;

			if (side == RIGHT_SIDE) {
				// Target is at the right side
				if ((angle < 90) || (angle > 270)) {
					angle = ball.getAngle();

					A = target.getXPos() - ball.getXPos();
					B = target.getYPos() - ball.getYPos();
					C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
					D = (int) Math.toDegrees(Math.asin(B / C));

					if (B <= 0) {
						// Ball is lower
						D = -D;
						if (angle > 270)
							ball.setAngle(angle + ANGLE);
						else if (D < angle)
							ball.setAngle(angle - ANGLE);
						else if (D > angle)
							ball.setAngle(angle + ANGLE);
					} else {
						// Ball is higher
						if (angle >= 0 && angle < 90)
							ball.setAngle(angle - ANGLE);
						else if (D < 360 - angle)
							ball.setAngle(angle + ANGLE);
						else if (D > 360 - angle)
							ball.setAngle(angle - ANGLE);
					}
					return false;
				} else return true;
			} else {
				// Target is at the left side
				if ((angle > 90) && (angle < 270)) {
					angle = ball.getAngle();

					A = target.getXPos() - ball.getXPos();
					B = target.getYPos() - ball.getYPos();
					C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
					D = (int) Math.toDegrees(Math.asin(B / C));
					if (B <= 0) {
						// Ball is lower
						D = -D;
						if (angle > 180)
							ball.setAngle(angle - ANGLE);
						else if (D < (180 - angle))
							ball.setAngle(angle + ANGLE);
						else if (D > (180 - angle))
							ball.setAngle(angle - ANGLE);
					} else {
						// Ball is higher
						if (angle < 180)
							ball.setAngle(angle + ANGLE);
						else if (D < (angle - 180))
							ball.setAngle(angle - ANGLE);
						else if (D > (angle - 180))
							ball.setAngle(angle + ANGLE);
					}
					return false;
				} else return true;
			}
		}
	}
}
