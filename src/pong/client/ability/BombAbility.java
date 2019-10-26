package pong.client.ability;

import java.awt.Color;

import pong.client.game.GameManager;
import pong.client.gui.GamePanel;
import pong.client.gui.pong.Ball;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.Explosion;
import pong.com.command.AbilityCommand;

public class BombAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 45;
	private BombThread bombThread		= null;
	private Explosion explosion 		= null;
	
	public BombAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}

	@Override
	protected void startAbility () {
		bombThread = new BombThread(gamePanel.getBall(), gamePanel.getRightBar(), gamePanel.getLeftBar());
		gamePanel.addAbilityThread(this);
	}

	@Override
	public boolean run () {
		if (bombThread != null) {
			return bombThread.run();
		} else return true;
	}

	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	protected void stopAbility () {
		if (bombThread != null) {
			if (bombThread.getWinner() != GameManager.CONTINUE) gamePanel.getBall().reset();
			bombThread = null;
			if (explosion != null) {
				gamePanel.removeMovingObject(explosion);
				explosion = null;
				gamePanel.repaint();
			}
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}

	private class BombThread {

		private static final int EXP_FRAMES	= 30;
		private Bar rightTarget;
		private Bar leftTarget;
		private Ball ball;
		private int counter 				= 0;
		private boolean createExplosion		= false;
		private int winner					= GameManager.CONTINUE;
		
		
		public BombThread (Ball ball, Bar rightTarget, Bar leftTarget) {
			this.ball = ball;
			this.rightTarget = rightTarget;
			this.leftTarget = leftTarget;
			ball.setTickTock(true);
		}
		
		public int getWinner () {
			return winner;
		}

		public boolean run () {

			if (createExplosion) {
				if (counter++ == 0) {
					ball.repaint();
					explosion = new Explosion(142, 200, Color.WHITE);
					explosion.setStartingPosition(ball.getXPos(), ball.getYPos());
					gamePanel.addMovingObject(explosion);
				}
				if (counter < EXP_FRAMES) {
					explosion.repaint();
					return false;
				} else {
					if (winner != GameManager.CONTINUE) {
						gamePanel.setWinner(winner);
					}
					return true;
				}
			}
			
			if (ball.getAngle() < 90 || ball.getAngle() > 270) {
				// Check if we reached the right target
				if (ball.getXPos() + (ball.getWidth() / 2) + 15 >= rightTarget.getXPos() - (rightTarget.getWidth() / 2)) {
					// Check if we hit the target
					if ((ball.getYPos() > rightTarget.getYPos() - (rightTarget.getHeight() / 2)) && 
							(ball.getYPos() < rightTarget.getYPos() + (rightTarget.getHeight() / 2))) {
						createExplosion = true;
						ball.setTickTock(false);
						if (!rightTarget.hasAbility("antibomb")) {
							ball.setTransparent(true);
							ball.setVelocity(0);
							rightTarget.setVelocity(0);
							rightTarget.setWidth(0);
							rightTarget.setHeight(0);
							rightTarget.repaint();
							winner = GameManager.PALYER1_WINS;
							return false;
						} else return false;
					} else return true;
				}
			} else if (ball.getAngle() > 90 && ball.getAngle() < 270) {
				// Check if we reached the left target
				if (ball.getXPos() - ((ball.getWidth() / 2) + 15) <= leftTarget.getXPos() + (leftTarget.getWidth() / 2)) {
					// Check if we hit the target
					if ((ball.getYPos() > leftTarget.getYPos() - (leftTarget.getHeight() / 2)) && 
							(ball.getYPos() < leftTarget.getYPos() + (leftTarget.getHeight() / 2))) {
						createExplosion = true;
						ball.setTickTock(false);
						if (!leftTarget.hasAbility("antibomb")) {
							ball.setTransparent(true);
							ball.setVelocity(0);
							leftTarget.setVelocity(0);
							leftTarget.setWidth(0);
							leftTarget.setHeight(0);
							leftTarget.repaint();
							winner = GameManager.PALYER2_WINS;
							return false;
						} else return false;
					} else return true;
				}
			}
			return false;
		}
	}
}
