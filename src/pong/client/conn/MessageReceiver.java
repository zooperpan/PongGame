package pong.client.conn;

import pong.com.command.UserCommand;

public interface MessageReceiver {

	public void receive (UserCommand command);
}
