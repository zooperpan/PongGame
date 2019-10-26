package pong.com.command;

public class AbilityCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private int registeredKey;
	
	public AbilityCommand (int registeredKey) {
		super(Command.ABILITY);
		this.registeredKey = registeredKey;
	}

	public int getRegisteredKey() {
		return registeredKey;
	}
}
