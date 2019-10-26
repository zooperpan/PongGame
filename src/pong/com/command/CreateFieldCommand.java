package pong.com.command;

public class CreateFieldCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	
	private String fieldName;
	
	public CreateFieldCommand (String fieldName) {
		super(Command.CREATE_FIELD);
		this.fieldName = fieldName;
	}
	
	public String getFieldName () {
		return fieldName;
	}
}
