package pong.com.command;

public class AbilitiesCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private String [] abilities;
	
	public AbilitiesCommand (String [] abilities) {
		super(Command.ABILITIES);
		this.abilities = abilities;
	}
	
	public String[] getAbilities () {
		return abilities;
	}
}
