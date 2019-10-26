package pong.client.ability;

import java.awt.Color;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Ball;
import pong.client.motion.BallBounceManager;
import pong.client.motion.BallMotionManager;
import pong.com.command.AbilityCommand;
import pong.com.command.TripleAbilityCommand;

public class TripleAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 20;
	private TripleThread tripleThread	= null;
	private Ball ball1 					= null;
	private Ball ball2					= null;
	private Ball ball3					= null;
	private BallMotionManager bmm1		= null;
	private BallMotionManager bmm2		= null;
	private BallMotionManager bmm3		= null;
	private int activeBalls				= 0;
	
	public TripleAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}
	
	@Override
	public void startAbility () {
		tripleThread = new TripleThread();
		gamePanel.addAbilityThread(this);
	}
	
	@Override
	public boolean run () {
		if (tripleThread != null) {
			return tripleThread.run();
		} else return true;
	}
	
	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	public void stopAbility () {
		if (tripleThread != null) {
			tripleThread = null;
		}
		if (bmm1 != null) {
			gamePanel.removeMotionManager(bmm1);
			gamePanel.removeMovingObject(ball1);
			ball1 = null;
			bmm1 = null;
		}
		if (bmm2 != null) {
			gamePanel.removeMotionManager(bmm2);
			gamePanel.removeMovingObject(ball2);
			ball1 = null;
			bmm1 = null;
		}
		if (bmm3 != null) {
			gamePanel.removeMotionManager(bmm3);
			gamePanel.removeMovingObject(ball3);
			ball1 = null;
			bmm1 = null;
		}
		gamePanel.repaint();
		activeBalls = 0;
	}
	
	@Override
	public void handleAbilityCommand (AbilityCommand command) {
		TripleAbilityCommand tCommand = (TripleAbilityCommand)command;
		gamePanel.getBall().setAngle(tCommand.getBallAngle());
		ball1.setAngle(tCommand.getBall1Angle());
		ball2.setAngle(tCommand.getBall2Angle());
		ball3.setAngle(tCommand.getBall3Angle());
	}
	
	private class TripleThread {
		
		public TripleThread () {
			
			Ball ball = gamePanel.getBall();
			
			if (!isRemoteUser()) {
				ball1 = new Ball(30, Color.RED, "PantomBall1");
				ball2 = new Ball(30, Color.RED, "PantomBall2");
				ball3 = new Ball(30, Color.RED, "PantomBall3");
				ball1.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball2.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball3.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball1.setVelocity(ball.getVelocity());
				ball2.setVelocity(ball.getVelocity());
				ball3.setVelocity(ball.getVelocity());
				bmm1 = new BallMotionManager(ball1, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				bmm2 = new BallMotionManager(ball2, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				bmm3 = new BallMotionManager(ball3, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				
				gamePanel.addMotionManager(bmm1);
				gamePanel.addMotionManager(bmm2);
				gamePanel.addMotionManager(bmm3);
				gamePanel.addMovingObject(ball1);
				gamePanel.addMovingObject(ball2);
				gamePanel.addMovingObject(ball3);
				
				int rand = (int) (Math.random() * 4);
				if (ball.getAngle() < 90 || ball.getAngle() > 270) {
					// Ball goes to the right
					switch (rand) {
					case 0: 
						ball.setAngle(30);
						ball1.setAngle(330);
						ball2.setAngle(55);
						ball3.setAngle(305);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 30, 330, 55, 305));
						break;
					case 1:
						ball.setAngle(330);
						ball1.setAngle(30);
						ball2.setAngle(55);
						ball3.setAngle(305);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 330, 30, 55, 305));
						break;
					case 2:
						ball.setAngle(55);
						ball1.setAngle(30);
						ball2.setAngle(330);
						ball3.setAngle(305);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 55, 30, 330, 305));
						break;
					case 3:
						ball.setAngle(305);
						ball1.setAngle(30);
						ball2.setAngle(55);
						ball3.setAngle(330);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 305, 30, 55, 330));
						break;
					default:
						ball.setAngle(305);
						ball1.setAngle(30);
						ball2.setAngle(55);
						ball3.setAngle(330);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 305, 30, 55, 330));
						break;
					}
				} else {
					// Ball goes to the left
					switch (rand) {
					case 0: 
						ball.setAngle(210);
						ball1.setAngle(150);
						ball2.setAngle(235);
						ball3.setAngle(125);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 210, 150, 235, 125));
						break;
					case 1:
						ball.setAngle(150);
						ball1.setAngle(210);
						ball2.setAngle(235);
						ball3.setAngle(125);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 150, 210, 235, 125));
						break;
					case 2:
						ball.setAngle(235);
						ball1.setAngle(150);
						ball2.setAngle(210);
						ball3.setAngle(125);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 235, 150, 210, 125));
						break;
					case 3:
						ball.setAngle(125);
						ball1.setAngle(150);
						ball2.setAngle(235);
						ball3.setAngle(210);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 125, 150, 235, 210));
						break;
					default:
						ball.setAngle(125);
						ball1.setAngle(150);
						ball2.setAngle(235);
						ball3.setAngle(210);
						gamePanel.sendCommand(new TripleAbilityCommand(getRegisteredKey(), 125, 150, 235, 210));
						break;
					}
				}
			} else {
				// This is the remote user
				ball1 = new Ball(30, Color.RED, "PantomBall1");
				ball2 = new Ball(30, Color.RED, "PantomBall2");
				ball3 = new Ball(30, Color.RED, "PantomBall3");
				ball1.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball2.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball3.setStartingPosition(ball.getXPos(), ball.getYPos());
				ball1.setVelocity(ball.getVelocity());
				ball2.setVelocity(ball.getVelocity());
				ball3.setVelocity(ball.getVelocity());
				ball1.setAngle(ball.getAngle());
				ball2.setAngle(ball.getAngle());
				ball3.setAngle(ball.getAngle());
				bmm1 = new BallMotionManager(ball1, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				bmm2 = new BallMotionManager(ball2, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				bmm3 = new BallMotionManager(ball3, new BallBounceManager(0, gamePanel.getPlayFieldHeight(), gamePanel.getPlayFieldWidth(), 0));
				gamePanel.addMotionManager(bmm1);
				gamePanel.addMotionManager(bmm2);
				gamePanel.addMotionManager(bmm3);
				gamePanel.addMovingObject(ball1);
				gamePanel.addMovingObject(ball2);
				gamePanel.addMovingObject(ball3);
			}
			activeBalls = 3;
		}
		
		public boolean run () {
			
			if (gamePanel.getRightBar().hasAbility("vision") || gamePanel.getLeftBar().hasAbility("vision")) {
				ball1.setColor(Color.GREEN);
				ball2.setColor(Color.GREEN);
				ball3.setColor(Color.GREEN);
			}
			
			if (activeBalls > 0) {
				if (ball1 != null) {
					if (checkBallLimits(bmm1)) {
						bmm1 = null;
						ball1 = null;
						activeBalls--;
						gamePanel.repaint();
					}
				}
				if (ball2 != null) {
					if (checkBallLimits(bmm2)) {
						bmm2 = null;
						ball2 = null;
						activeBalls--;
						gamePanel.repaint();
					}
				}
				if (ball3 != null) {
					if (checkBallLimits(bmm3)) {
						bmm3 = null;
						ball3 = null;
						activeBalls--;
						gamePanel.repaint();
					}
				}
				if (activeBalls == 0) return true;
				return false;
			} else return true;
		}
		
		private boolean checkBallLimits (BallMotionManager bmm) {
			Ball ball = (Ball)bmm.getMovingObject();
			BallBounceManager bbm = (BallBounceManager) bmm.getBounceManager();
			
			if (ball.getAngle() < 90 || ball.getAngle() > 270) {
				// Ball goes to the right
				if (ball.getXPos() + (ball.getWidth() / 2) - 4 >= bbm.getRight()) {
					// Ball has reached right limits
					gamePanel.removeMotionManager(bmm);
					gamePanel.removeMovingObject(ball);
					return true;
				}
			} else {
				// Ball goes to the left
				if (ball.getXPos() - (ball.getWidth() / 2) + 4 <= bbm.getLeft()) {
					// Ball has reached left limits
					gamePanel.removeMotionManager(bmm);
					gamePanel.removeMovingObject(ball);
					return true;
				}
			}
			return false;
		}
	}
}
