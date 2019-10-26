package pong.client.conn;

import java.io.IOException;

import pong.com.command.Command;
import pong.com.command.UserCommand;

public class MessageHandler {

	private ConnectionHandler parent;
	private boolean keepReceiving = false;
	private boolean hanldeExited = false;
	private MessageReceiver receiver;
	
	public MessageHandler (ConnectionHandler parent, MessageReceiver receiver) {
		this.parent = parent;
		this.receiver = receiver;
	}
	
	// Main method that handles the incoming messages
	public void start () {
		
		keepReceiving = true;
		try {
			while (keepReceiving) {
				// Task blocks in the read method
				UserCommand userCommand = read();
				if (userCommand == null) break;
				receiver.receive(userCommand);
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {}
		keepReceiving = false;
		hanldeExited = true;
	}
	
	private void send (UserCommand command) {
		parent.sendUDPCommand(command);
	}
	
	private UserCommand read () throws IOException, ClassNotFoundException {
		//return parent.readCommand();
		return parent.readUDPCommand();
	}
	
	public void setReceiver (MessageReceiver receiver) {
		this.receiver = receiver;
	}
	
	public void stop () throws IOException {
		// The user has exited the game, send a notification to the server
		send(new UserCommand(Command.USER_EXITED));
		keepReceiving = false;
	}
	
	/**
	 * Actually waits until the BYE command is received
	 */
	public void join () {
		// Make sure that we are not trying to receive anything
		if (!keepReceiving) {
			// Wait for flag to get true
			while (!hanldeExited) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
		}
	}
}
