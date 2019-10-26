package pong.server.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import pong.com.ActiveField;
import pong.com.command.ActiveFieldsCommand;
import pong.com.command.Command;
import pong.com.command.CreateFieldCommand;
import pong.com.command.GetActiveFieldsCommand;
import pong.com.command.JoinFieldCommand;
import pong.com.command.UDPPortCommand;
import pong.com.command.UserCommand;
import pong.com.command.UserEnteredCommand;
import pong.server.PongServer;
import pong.server.UDPPort;
import pong.server.field.FieldHandler;

public class UserServerThread extends Thread {

	private Socket socket		 					= null;
	private ObjectOutputStream objectOutputStream 	= null;
	private ObjectInputStream objectInputStream		= null;
	private boolean threadAlive						= true;
	private PongServer parent						= null;
	private boolean connectionAccepted				= false;
	private boolean connectionClosed				= false;
	private String userName;
	private FieldHandler fieldHandler				= null;
	private UDPPort udpPort							= null;
	private InetAddress userAddress					= null;
	private DatagramSocket udpSocket 				= null;

	public UserServerThread (UDPPort udpPort, Socket socket, PongServer parent) {
		super();
		this.udpPort = udpPort;
		this.socket = socket;
		this.parent = parent;
		userAddress = socket.getInetAddress();
		try {
			udpSocket = new DatagramSocket(udpPort.getPort());
		} catch (SocketException e) {
			parent.addText(e.getMessage() + "\r\n");
		}
	}

	public void run () {

		Object o;
		// Handles all the commands that are coming from the served user
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());

			// This is a new thread that waits 5sec for a connection to be established
			// before closing the connection.
			CheckConnection checkConnection = new CheckConnection(this);
			checkConnection.start();

			// The very first command that the user must send is the GET_ACTIVE_FILEDS.
			// If this command is not received within 5 seconds, the connection will close.
			if (!waitFirstCommand()) {
				// Wrong or no response from user
				closeConnection();
				return;
			}

			// Since the GET_ACTIVE_FILEDS is received, send them with a ACTIVE_FILEDS command
			sendActiveFields(true);
			// Also send the UDP port
			sendUDPPort();

			// Start the standard communication with the user
			while ((o = readUDPCommand()) != null) {
				UserCommand userCommand = (UserCommand)o;
				switch (userCommand.getCommand()) {
				case GET_ACTIVE_FIELDS:
				{
					sendActiveFields(false);
					break;
				}
				case CREATE_FIELD:
				{
					// A new field must be created, get its name from the command
					String fieldName = ((CreateFieldCommand)userCommand).getFieldName();
					// Create a FieldHandler with the created ActiveField
					fieldHandler = new FieldHandler(new ActiveField(fieldName));
					// Add the actual user
					fieldHandler.addUser(this);
					// Add it in the parent's list
					parent.handleFieldHandler(true, fieldHandler);
					break;
				}
				case JOIN_FIELD:
				{
					// Get the name of the user that just joined
					String userName = ((JoinFieldCommand)userCommand).getUserName();
					// Get the field's name to be joined
					String fieldName = ((JoinFieldCommand)userCommand).getFieldName();
					// Find the FieldHandler that handles the specific field
					fieldHandler = parent.getFieldHandler(fieldName);
					if (fieldHandler != null) {
						// The FieldHandler has been found, add the actual user
						fieldHandler.addUser(this);
						// Notify the other user with a USER_ENTERED command
						fieldHandler.sendUDPCommand(this, new UserEnteredCommand(userName));
					}
					break;
				}
				case USER_EXITED:
					// We come here when the served user wants to exit the application
					// Check if the user has created or joined a field
					if (fieldHandler != null) {
						// Send a BYE command back to the served user
						sendBye();
						// Stop the field handler
						fieldHandler.stop();
						// Notify the other user with a USER_REMOVED command
						fieldHandler.sendUDPCommand(this, new UserCommand(Command.USER_REMOVED));
					}
					// Close the connection. This actually removes this thread from the thread list
					// as well as from the FieldHandler. If the FieldHandler gets empty, then this is
					// also removed from the FieldHandler list.
					closeConnection();
					break;
				case BALL_ANGLE:
					// Send the incoming BALL_ANGLE command to the other user 
					fieldHandler.sendUDPCommand(this, userCommand);
					// Wait a few milliseconds and start the field thread
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					// The server will start sending MOTION_UPDATE commands
					fieldHandler.start();
					break;
				case PAUSE_GAME:
					// Pause the game by stop sending MOTION_UPDATE commands
					if (fieldHandler != null) {
						// Stop the field handler
						fieldHandler.stop();
					}
					break;
				default:
					// All other commands are send to the other user
					fieldHandler.sendUDPCommand(this, userCommand);
					break;
				}
			}

		} catch (IOException e) {
			//e.printStackTrace();
			closeConnection();
		}
		threadAlive = false;
	}

	/**
	 * Waits for the very 1st command from the user. It should be a GET_ACTIVE_FILEDS.
	 * If this command is received, the connection is accepted.
	 * @return true if the connection has been accepted, false otherwise.
	 */
	private boolean waitFirstCommand () throws IOException {
		Object o;
		boolean result = false;

		try {
			o = objectInputStream.readObject();
			if (o instanceof UserCommand) {
				UserCommand userCommand = (UserCommand)o;
				switch (userCommand.getCommand()) {
				case GET_ACTIVE_FIELDS:
					// Correct command from user, accept the connection
					connectionAccepted = true;
					// Get the user name
					userName = ((GetActiveFieldsCommand)userCommand).getUserName();
					result = true;
					break;
				default:
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			parent.addText(e.getMessage() + "\r\n");
		}

		return result;
	}

	/**
	 * Sends the active fields command to the served user.
	 * @param useTCP - true if the command should be send through the TCP socket.
	 * If false the command will be send through the UDP socket.
	 */
	private void sendActiveFields (boolean useTCP) throws IOException {
		ActiveFieldsCommand command = new ActiveFieldsCommand(parent.getFieldCharacteristics());
		if (useTCP) {
			objectOutputStream.writeObject(command);
			objectOutputStream.flush();
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(command);
			byte[] data = baos.toByteArray();
			DatagramPacket packet = new DatagramPacket(data, data.length);
			sendUDPPacket(packet);
		}
	}

	/**
	 * Sends the UDP port to the served user.
	 */
	private void sendUDPPort () throws IOException {
		UDPPortCommand command = new UDPPortCommand(udpPort.getPort());
		objectOutputStream.writeObject(command);
		objectOutputStream.flush();
	}
	
	/**
	 * Sends a BYE command to the served user.
	 * @throws IOException
	 */
	private void sendBye () throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(new UserCommand(Command.BYE));
		byte[] data = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		sendUDPPacket(packet);
	}

	/**
	 * Sends the specific user command to the served user. This is actually a command
	 * that came from the opponent user. It may also be used when the server goes down.
	 * @param userCommand - The command to send to the served user.
	 */
	public void sendCommand (UserCommand userCommand) {
		try {
			objectOutputStream.writeObject(userCommand);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			parent.addText("sendCommand: " + e.getMessage() + "\r\n");
		}
	}

	/**
	 * This method blocks until it reads a user command from the UDP socket.
	 * @return - the user command that was read.
	 * @throws IOException
	 */
	public UserCommand readUDPCommand () throws IOException {
		Object o;
		int length = 6400;
		byte buf[] = new byte[length];

		DatagramPacket packet = new DatagramPacket(buf, length);
		udpSocket.receive(packet);

		ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getData().length);

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			o = ois.readObject();
			if (o instanceof UserCommand) {
				ois.close();
				bais.close();
				return ((UserCommand)o);
			}
			else {
				ois.close();
				bais.close();
				return null;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			parent.addText("readUDPCommand: " + e.getMessage() + "\r\n");
		}
		return null;
	}

	/**
	 * Sends the specific packet to the served user.
	 * @param packet - The packet to send to the served user.
	 */
	public void sendUDPPacket (DatagramPacket packet) {

		packet.setAddress(userAddress);
		packet.setPort(udpPort.getPort() + 1);
		try {
			udpSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			parent.addText(e.getMessage() + "\r\n");
		}
	}

	/**
	 * This method returns the status of the connection.
	 * @return true if the connection has been established, false otherwise.
	 */
	public boolean getConnectionStatus () {
		return connectionAccepted;
	}

	/**
	 * Returns the status of the thread.
	 * @return true if the thread is active, false otherwise.
	 */
	public boolean isThreadAlive () {
		return threadAlive;
	}

	public void closeConnection () {
		if (connectionClosed) return;
		connectionClosed = true;
		try {
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
			udpSocket.close();
			udpPort.setInUse(false);
		} catch (IOException e) {}
		threadAlive = false;
		parent.handleUserThread(false, this);
	}

	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	public String getUserName () {
		return userName;
	}
}
