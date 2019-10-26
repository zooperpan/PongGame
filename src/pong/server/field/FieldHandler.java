package pong.server.field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import pong.com.ActiveField;
import pong.com.command.UserCommand;
import pong.server.user.UserServerThread;

public class FieldHandler {

	private ActiveField field;
	private UserServerThread user1	= null;;
	private UserServerThread user2	= null;
	private FieldThread fieldThread	= null;
	
	
	public FieldHandler (ActiveField field) {
		this.field = field;
	}
	
	public String getFieldName () {
		return field.getName();
	}
	
	public boolean isOpened () {
		return field.isOpened();
	}
	
	public String getUser1Name () {
		return field.getUser1Name();
	}
	
	public String getUser2Name () {
		return field.getUser2Name();
	}
	
	public synchronized void addUser (UserServerThread user) {
		if (user1 == null) {
			user1 = user;
			field.addUser(user.getUserName());
		} else if (user2 == null) {
			user2 = user;
			field.addUser(user.getUserName());
		}
	}
	
	public synchronized void removeUser (UserServerThread user) {
		if (user1 == null) {
			// Should never come in here
			return;
		} else if (user1 == user) {
			// The 1st user has been removed
			field.removeUser(user.getUserName());
			user1 = user2;
			user2 = null;
		} else {
			// The 2nd user has been removed
			field.removeUser(user.getUserName());
			user2 = null;
		}
	}
	
	/**
	 * Sends a command from one user to the other.
	 * @param user - The user sending the command.
	 * @param command - The command to be sent.
	 */
	public synchronized void sendCommand (UserServerThread user, UserCommand command) {
		if ((user == user1) && (user2 != null)) {
			user2.sendCommand(command);
		} else if ((user == user2) && (user1 != null)) {
			user1.sendCommand(command);
		}
	}
	
	/**
	 * Sends a command from one user to the other through the UDP socket.
	 * @param user - The user sending the command.
	 * @param command - The command to be sent.
	 */
	public synchronized void sendUDPCommand (UserServerThread user, UserCommand command) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(command);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] data = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		if ((user == user1) && (user2 != null)) {
			user2.sendUDPPacket(packet);
		} else if ((user == user2) && (user1 != null)) {
			user1.sendUDPPacket(packet);
		}
	}
	
	public void start () {
		if (fieldThread == null) {
			fieldThread = new FieldThread(this, user1, user2);
			fieldThread.start();
		}
	}
	
	public void stop () {
		try {
			if (fieldThread != null) {
				fieldThread.kill();
				fieldThread.join();
				fieldThread = null;
			}
		} catch (InterruptedException e) {}
	}
}
