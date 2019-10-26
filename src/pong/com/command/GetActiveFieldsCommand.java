package pong.com.command;

public class GetActiveFieldsCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private String userName;
	
	public GetActiveFieldsCommand (String userName) {
		super(Command.GET_ACTIVE_FIELDS);
		this.userName = userName;
	}
	
	public String getUserName () {
		return userName;
	}
}
