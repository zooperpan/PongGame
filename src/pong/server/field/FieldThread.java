package pong.server.field;

import pong.com.command.Command;
import pong.com.command.UserCommand;
import pong.server.user.UserServerThread;

public class FieldThread extends Thread {

	private static final int REFRESH_TIME		= 30;
	private static final UserCommand command	= new UserCommand(Command.MOTION_UPDATE);
	private FieldHandler fieldHandler			= null;
	private boolean isAlive						= true;
	private boolean user1First					= true;
	private UserServerThread user1				= null;
	private UserServerThread user2				= null;
	
	public FieldThread (FieldHandler fieldHandler, UserServerThread user1, UserServerThread user2) {
		super();
		this.fieldHandler = fieldHandler;
		this.user1 = user1;
		this.user2 = user2;
	}
	
	public void run () {
		while (isAlive) {
			try {
	    		Thread.sleep(REFRESH_TIME);
	    		if (isAlive) {
	    			if (user1First) {
	    				fieldHandler.sendUDPCommand(user1, command);
			    		fieldHandler.sendUDPCommand(user2, command);
			    		user1First = false;
	    			} else {
	    				fieldHandler.sendUDPCommand(user2, command);
			    		fieldHandler.sendUDPCommand(user1, command);
			    		user1First = true;
	    			}
	    			
	    		}
			} catch ( InterruptedException ex) {}
		}
	}
	
	public void kill () {
		isAlive = false;
	}
}
