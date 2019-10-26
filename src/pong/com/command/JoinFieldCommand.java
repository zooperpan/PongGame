package pong.com.command;

public class JoinFieldCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private String fieldName;
	private String userName;
	
	public JoinFieldCommand(String fieldName, String userName) {
		super(Command.JOIN_FIELD);
		this.fieldName = fieldName;
		this.userName = userName;
	}
	
	public String getFieldName () {
		return fieldName;
	}
	
	public String getUserName () {
		return userName;
	}
}
