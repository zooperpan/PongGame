package pong.com.command;

public class UserEnteredCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private String opponentName;
	
	public UserEnteredCommand (String opponentName) {
		super(Command.USER_ENTERED);
		this.opponentName = opponentName;
	}

	public String getOpponentName() {
		return opponentName;
	}
}
