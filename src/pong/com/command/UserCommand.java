package pong.com.command;

import java.io.Serializable;

public class UserCommand implements Serializable {
	
	private static final long serialVersionUID = 395867276L;
	private Command command;
	
	public UserCommand (Command command) {
		this.command = command;
	}
	
	public Command getCommand () {
		return command;
	}
}
