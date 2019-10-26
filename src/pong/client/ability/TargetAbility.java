package pong.client.ability;

import java.awt.Color;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.Explosion;
import pong.client.gui.pong.Missile;
import pong.client.motion.MissileMotionManager;
import pong.com.command.AbilityCommand;

public class TargetAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 60;
	private TargetThread targetThread	= null;
	private Explosion explosion 		= null;
	private Missile missile				= null;
	private MissileMotionManager mmm	= null;
	
	public TargetAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}

	@Override
	protected void startAbility () {
		if (isMaster()) {
			// Master uses left bar
			targetThread = new TargetThread(gamePanel.getLeftBar(), gamePanel.getRightBar(), TargetThread.RIGHT_SIDE);
		} else {
			// Slave uses right bar
			targetThread = new TargetThread(gamePanel.getRightBar(), gamePanel.getLeftBar(), TargetThread.LEFT_SIDE);
		}
		gamePanel.addAbilityThread(this);
	}

	@Override
	public boolean run () {
		if (targetThread != null) {
			return targetThread.run();
		} else return true;
	}

	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	protected void stopAbility () {
		if (targetThread != null) {
			targetThread = null;
			if (explosion != null) {
				gamePanel.removeMovingObject(explosion);
				explosion = null;
				gamePanel.repaint();
			}
			if (mmm != null) {
				gamePanel.removeMovingObject(missile);
				gamePanel.removeMotionManager(mmm);
				mmm = null;
				missile = null;
				gamePanel.repaint();
			}
		}

	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}

	private class TargetThread {

		private static final int EXP_FRAMES	= 30;
		private static final int ANGLE 		= 5;
		public static final int	LEFT_SIDE	= 0;
		public static final int	RIGHT_SIDE	= 1;
		private Bar target					= null;
		private boolean createExplosion		= false;
		private int counter 				= 0;
		private int side;

		public TargetThread (Bar launcher, Bar target, int side) {
			this.target = target;
			this.side = side;
			
			if (side == RIGHT_SIDE) {
				missile = new Missile(40, 40, Color.WHITE, "MissileR");
				missile.setStartingPosition(launcher.getXPos() + (launcher.getWidth() / 2) + (missile.getWidth() / 2), 
						launcher.getYPos());
				missile.setAngle(0);
			} else {
				missile = new Missile(40, 40, Color.WHITE, "MissileL");
				missile.setStartingPosition(launcher.getXPos() - ((launcher.getWidth() / 2) + (missile.getWidth() / 2)), 
						launcher.getYPos());
				missile.setAngle(180);
			}
			mmm = new MissileMotionManager(missile);
			gamePanel.addMotionManager(mmm);
			gamePanel.addMovingObject(missile);
		}

		public boolean run () {

			if (createExplosion) {
				if (counter++ == 0) {
					if (!target.hasAbility("decoy")) {
						target.setWidth(0);
						target.setHeight(0);
					}
					missile.repaint();
					explosion = new Explosion(142, 200, Color.WHITE);
					explosion.setStartingPosition(missile.getXPos(), missile.getYPos());
					gamePanel.addMovingObject(explosion);
				}
				if (counter < EXP_FRAMES) {
					explosion.repaint();
					return false;
				} else return true;
			}

			if (target.hasAbility("decoy")) {
				createExplosion = true;
				missile.setTransparent(true);
				missile.setVelocity(0);
				return false;
			}

			int angle = missile.getAngle();
			double A, B, C;
			int D;
			
			if (side == RIGHT_SIDE) {
				// Target is at the right side
				// Check if we reached the right target
				if (missile.getXPos() + (missile.getWidth() / 2) >= target.getXPos() - (target.getWidth() / 2)) {
					// Check if we hit the target
					if (((missile.getYPos() > target.getYPos() - (target.getHeight() / 2)) && 
							(missile.getYPos() < target.getYPos() + (target.getHeight() / 2))) ||
							((missile.getYPos() + (missile.getHeight() / 2) > target.getYPos() - (target.getHeight() / 2)) && 
							(missile.getYPos() < target.getYPos())) ||
							((missile.getYPos() - (missile.getHeight() / 2) < target.getYPos() + (target.getHeight() / 2)) &&
							(missile.getYPos() > target.getYPos()))) {
						createExplosion = true;
						missile.setTransparent(true);
						missile.setVelocity(0);
						target.setVelocity(0);
						return false;
					} else return true;
				} else if ((angle < 90) || (angle > 270)) {
					A = target.getXPos() - missile.getXPos();
					B = target.getYPos() - missile.getYPos();
					C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
					D = (int) Math.toDegrees(Math.asin(B / C));

					if (B <= 0) {
						// Ball is lower
						D = -D;
						if (angle > 270)
							missile.setAngle(angle + ANGLE);
						else if (D < angle)
							missile.setAngle(angle - ANGLE);
						else if (D > angle)
							missile.setAngle(angle + ANGLE);
					} else {
						// Ball is higher
						if (angle >= 0 && angle < 90)
							missile.setAngle(angle - ANGLE);
						else if (D < 360 - angle)
							missile.setAngle(angle + ANGLE);
						else if (D > 360 - angle)
							missile.setAngle(angle - ANGLE);
					}
					return false;
				} else return true;
			} else {
				// Target is at the left side
				if (missile.getXPos() - ((missile.getWidth() / 2) + 10) <= target.getXPos() + (target.getWidth() / 2)) {
					// Check if we hit the target
					if (((missile.getYPos() > target.getYPos() - (target.getHeight() / 2)) && 
							(missile.getYPos() < target.getYPos() + (target.getHeight() / 2))) ||
							((missile.getYPos() + (missile.getHeight() / 2) > target.getYPos() - (target.getHeight() / 2)) && 
							(missile.getYPos() < target.getYPos())) ||
							((missile.getYPos() - (missile.getHeight() / 2) < target.getYPos() + (target.getHeight() / 2)) &&
							(missile.getYPos() > target.getYPos()))) {
						createExplosion = true;
						missile.setTransparent(true);
						missile.setVelocity(0);
						target.setVelocity(0);
						return false;
					} else return true;
				} else if ((angle > 90) && (angle < 270)) {
					A = target.getXPos() - missile.getXPos();
					B = target.getYPos() - missile.getYPos();
					C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
					D = (int) Math.toDegrees(Math.asin(B / C));
					if (B <= 0) {
						// Ball is lower
						D = -D;
						if (angle > 180)
							missile.setAngle(angle - ANGLE);
						else if (D < (180 - angle))
							missile.setAngle(angle + ANGLE);
						else if (D > (180 - angle))
							missile.setAngle(angle - ANGLE);
					} else {
						// Ball is higher
						if (angle < 180)
							missile.setAngle(angle + ANGLE);
						else if (D < (angle - 180))
							missile.setAngle(angle - ANGLE);
						else if (D > (angle - 180))
							missile.setAngle(angle + ANGLE);
					}
					return false;
				} else return true;
			}
		}
	}
}
