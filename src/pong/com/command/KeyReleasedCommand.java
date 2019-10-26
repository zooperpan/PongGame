package pong.com.command;

public class KeyReleasedCommand extends UserCommand {
	
	private static final long serialVersionUID = 395867276L;
	private int yPos;
	
	public KeyReleasedCommand (Command command, int yPos) {
		super(command);
		this.yPos = yPos;
	}
	
	public int getYPos () {
		return yPos;
	}

}
