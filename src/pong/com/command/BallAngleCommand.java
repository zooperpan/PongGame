package pong.com.command;

public class BallAngleCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private int angle;
	
	public BallAngleCommand (int angle) {
		super(Command.BALL_ANGLE);
		this.angle = angle;
	}
	
	public int getAngle () {
		return angle;
	}
}
