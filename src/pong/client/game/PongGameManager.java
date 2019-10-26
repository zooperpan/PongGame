package pong.client.game;

import pong.client.gui.pong.Ball;
import pong.client.gui.pong.Bar;

public class PongGameManager extends GameManager {

	private int BALL_MARGIN;
	private int ANGLE_MARGIN;
	private Ball ball;
	private Bar leftBar;
	private Bar rightBar;
	
	public PongGameManager (Ball ball, Bar rightBar, Bar leftBar) {
		super();
		BALL_MARGIN = ball.getHeight() / 2;
		ANGLE_MARGIN = BALL_MARGIN / 2;
		this.ball = ball;
		this.leftBar = leftBar;
		this.rightBar = rightBar;
	}
	
	public int checkResult() {
		
		int result;
		result = checkBallPlayerBounce();
		
		return result;
	}
	
	public int checkBallPlayerBounce () {
		
		if (ball.getAngle() < 90 || ball.getAngle() > 270) {
			// Ball goes to the right
			if (ball.getXPos() + (ball.getWidth() / 2) - 4 >= rightBar.getXPos() - (rightBar.getWidth() / 2)) {
				// Ball has reached player 2
				if ((rightBar.getHeight() == 0) || ((ball.getYPos() + BALL_MARGIN < rightBar.getYPos() - (rightBar.getHeight() / 2)) || 
						(ball.getYPos() - BALL_MARGIN >= rightBar.getYPos() + (rightBar.getHeight() / 2)))) {
					return PALYER1_WINS;
				} else {
					if (ball.getAngle() >= 0 && ball.getAngle() < 180) {
						// Ball goes up
						if (ball.getYPos() + BALL_MARGIN - (rightBar.getYPos() - (rightBar.getHeight() / 2)) < ANGLE_MARGIN) {
							// High hit edge
							ball.setAngle((180 - ball.getAngle()) - (2 * (90 - ball.getAngle()) / 3));
						} else if (ball.getYPos() + BALL_MARGIN - (rightBar.getYPos() - (rightBar.getHeight() / 2)) < 2 * ANGLE_MARGIN) {
							// High hit medium
							ball.setAngle((180 - ball.getAngle()) - ((90 - ball.getAngle()) / 3));
						} else if ((rightBar.getYPos() + (rightBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) < ANGLE_MARGIN)) {
							// Low hit edge
							ball.setAngle(180 + ball.getAngle());
						} else if ((rightBar.getYPos() + (rightBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) <  2 * ANGLE_MARGIN)) {
							// Low hit medium
							ball.setAngle(180 - ball.getAngle() + (ball.getAngle() / 2));
						} else {
							// Medium hit
							ball.setAngle(180 - ball.getAngle());
						}
					} else if (ball.getAngle() >= 180 && ball.getAngle() < 360) {
						// Ball goes down
						if ((rightBar.getYPos() + (rightBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) < ANGLE_MARGIN)) {
							// Low hit edge
							ball.setAngle((540 - ball.getAngle()) + (2 * (ball.getAngle() - 270) / 3));
						} else if ((rightBar.getYPos() + (rightBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) <  2 * ANGLE_MARGIN)) {
							// Low hit medium
							ball.setAngle((540 - ball.getAngle()) + ((ball.getAngle() - 270) / 3));
						} else if (ball.getYPos() + BALL_MARGIN - (rightBar.getYPos() - (rightBar.getHeight() / 2)) < ANGLE_MARGIN) {
							// High hit edge
							ball.setAngle(ball.getAngle() - 180);
						} else if (ball.getYPos() + BALL_MARGIN - (rightBar.getYPos() - (rightBar.getHeight() / 2)) < 2 * ANGLE_MARGIN) {
							// High hit medium
							ball.setAngle(360 - (ball.getAngle() / 2));
						} else {
							// Medium hit
							ball.setAngle(540 - ball.getAngle());
						}
					}
				}
			}
		} else {
			// Ball goes to the left
			if (ball.getXPos() - ((ball.getWidth() / 2) + 4/*ball.getVelocity()*/) < leftBar.getXPos() + (leftBar.getWidth() / 2)) {
				// Ball has reached player 1
				if ((leftBar.getHeight() == 0) || ((ball.getYPos() + BALL_MARGIN < leftBar.getYPos() - (leftBar.getHeight() / 2)) || 
						(ball.getYPos() - BALL_MARGIN >= leftBar.getYPos() + (leftBar.getHeight() / 2)))) {
					return PALYER2_WINS;
				} else {
					if (ball.getAngle() >= 0 && ball.getAngle() < 180) {
						// Ball goes up
						if (ball.getYPos() + BALL_MARGIN - (leftBar.getYPos() - (leftBar.getHeight() / 2)) < ANGLE_MARGIN) {
							// High hit edge
							ball.setAngle((180 - ball.getAngle()) + (2 * (180 - ball.getAngle()) / 3));
						} else if (ball.getYPos() + BALL_MARGIN - (leftBar.getYPos() - (leftBar.getHeight() / 2)) < 2 * ANGLE_MARGIN) {
							// High hit medium
							ball.setAngle((180 - ball.getAngle()) + ((180 - ball.getAngle()) / 3));
						} else if ((leftBar.getYPos() + (leftBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) < ANGLE_MARGIN)) {
							// Low hit edge
							ball.setAngle(180 + ball.getAngle());
						} else if ((leftBar.getYPos() + (leftBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) <  2 * ANGLE_MARGIN)) {
							// Low hit medium
							ball.setAngle(180 - ball.getAngle() - ((180 - ball.getAngle()) / 2));
						} else {
							ball.setAngle(180 - ball.getAngle());
						}
					} else if (ball.getAngle() >= 180 && ball.getAngle() < 360) {
						// Ball goes down
						if ((leftBar.getYPos() + (leftBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) < ANGLE_MARGIN)) {
							// Low hit edge
							ball.setAngle(660 - (5 * ball.getAngle() / 3));
						} else if ((leftBar.getYPos() + (leftBar.getHeight() / 2) - (ball.getYPos() - BALL_MARGIN) <  2 * ANGLE_MARGIN)) {
							// Low hit medium
							ball.setAngle(600 - (4 * ball.getAngle() / 3));
						} else if (ball.getYPos() + BALL_MARGIN - (leftBar.getYPos() - (leftBar.getHeight() / 2)) < ANGLE_MARGIN) {
							// High hit edge
							ball.setAngle(ball.getAngle() - 180);
						} else if (ball.getYPos() + BALL_MARGIN - (leftBar.getYPos() - (leftBar.getHeight() / 2)) < 2 * ANGLE_MARGIN) {
							// High hit medium
							ball.setAngle((540 - ball.getAngle()) + ((ball.getAngle() / 2) - 90));
						} else {
							// Medium hit
							ball.setAngle(540 - ball.getAngle());
						}
					}
				}
			}
		}
		return CONTINUE;
	}
}
