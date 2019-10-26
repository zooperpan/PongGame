package pong.com.command;

import pong.com.FieldCharacteristics;

public class ActiveFieldsCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private FieldCharacteristics fields[];
	
	public ActiveFieldsCommand(FieldCharacteristics fields[]) {
		super(Command.ACTIVE_FIELDS);
		this.fields = fields;
	}
	
	public FieldCharacteristics[] getFields() {
		return fields;
	}
}
