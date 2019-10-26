package pong.com.command;

public class TripleAbilityCommand extends AbilityCommand {

	private static final long serialVersionUID = 395867276L;
	private int ballAngle;
	private int ball1Angle;
	private int ball2Angle;
	private int ball3Angle;
	
	public TripleAbilityCommand(int registeredKey, int ballAngle, int ball1Angle, int ball2Angle, int ball3Angle) {
		super(registeredKey);
		this.ballAngle = ballAngle;
		this.ball1Angle = ball1Angle;
		this.ball2Angle = ball2Angle;
		this.ball3Angle = ball3Angle;
	}

	public int getBallAngle() {
		return ballAngle;
	}
	
	public int getBall1Angle() {
		return ball1Angle;
	}
	
	public int getBall2Angle() {
		return ball2Angle;
	}
	
	public int getBall3Angle() {
		return ball3Angle;
	}
}
