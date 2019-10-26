package pong.client.conn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingWorker;

import pong.client.gui.ClientFrame;
import pong.com.FieldCharacteristics;
import pong.com.command.ActiveFieldsCommand;
import pong.com.command.Command;
import pong.com.command.GetActiveFieldsCommand;
import pong.com.command.UDPPortCommand;
import pong.com.command.UserCommand;

public class ConnectionHandler {

	private static final String IP_ADDRESS			= "127.0.0.1";
	private static final int PORT_NO				= 39990;
	private Socket socket 							= null;
	private String userName;
	private ObjectOutputStream objectOutputStream 	= null;
	private ObjectInputStream objectInputStream		= null;
	private ClientFrame frame						= null;
	private MessageHandler messageHandler 			= null;
	private DatagramSocket udpSocket				= null;
	private int udpPort								= 0;
	
	public ConnectionHandler (ClientFrame frame, String userName) {
		this.frame = frame;
		this.userName = userName;
	}
	
	public void connect () {
		EstablishConnectionTask task = new EstablishConnectionTask();
		task.execute();
	}
	
	public void sendCommand (UserCommand command) {
		try {
			objectOutputStream.writeObject(command);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendUDPCommand (UserCommand command) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(command);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] data = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			packet.setAddress(InetAddress.getByName(IP_ADDRESS));
	        packet.setPort(udpPort);
			udpSocket.send(packet);
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UserCommand readCommand () throws IOException, ClassNotFoundException {
		UserCommand command = null;
		Object o;
		if ((o = objectInputStream.readObject()) instanceof UserCommand) {
			command = (UserCommand)o;
		}
		return command;
	}
	
	public UserCommand readUDPCommand () {
		Object o;
		
		int length = 6400;
		byte buf[] = new byte[length];
		
		DatagramPacket packet = new DatagramPacket(buf, length);
		try {
			udpSocket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getData().length);
		
		ObjectInputStream oos;
		try {
			oos = new ObjectInputStream(bais);
			o = oos.readObject();
			if (o instanceof UserCommand) {
				oos.close();
				bais.close();
				return ((UserCommand)o);
			}
			else {
				oos.close();
				bais.close();
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void startCommunication () {
		CommunicationTask comTask = new CommunicationTask(this);
		comTask.execute();
	}
	
	public void stopCommunication () {
		if (messageHandler != null) {
			try {
				// Sends USER_EXITED command and says to the handler to stop receiving
				messageHandler.stop();
				// Wait for the BYE command from the server
				messageHandler.join();
				// Method disconnect() will be called when the CommunicationTask has finished
			} catch (IOException e) {}
		} else {
			sendUDPCommand(new UserCommand(Command.USER_EXITED));
			// Do not wait for the BYE command, just disconnect
			disconnect();
		}
	}
	
	public void disconnect () {
		try {
			if (udpSocket != null) udpSocket.close();
			objectOutputStream.close();
			objectOutputStream.close();
			socket.close();
		} catch (IOException e) {}
	}
	
	public void setReceiver (MessageReceiver receiver) {
		messageHandler.setReceiver(receiver);
	}
	
	private class EstablishConnectionTask extends SwingWorker<Void, Void> {

		private FieldCharacteristics fields[];
		private boolean success;
		
		protected Void doInBackground () throws Exception {
			try {
				// Connect to the server (if not already connected)
				if (socket == null) {
					if ((success = establishConnection())) {
						// Send the GET_ACTIVE_FIELDS command (1st message to the server)
						sendCommand(new GetActiveFieldsCommand(userName));
						// We wait for 2 responses, the active fields and the UDP port
						getActiveFields(true);
						getUDPPort();
					}
				} else {
					// We are already connected, just request the active fields
					sendUDPCommand(new GetActiveFieldsCommand(userName));
					success = true;
					// We wait for the active fields through the UDP socket
					getActiveFields(false);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public void done () {
			frame.connectionEstablished(success, fields);
		}
		
		private boolean establishConnection () throws IOException, ClassNotFoundException {
			boolean success = false;
			socket = new Socket(IP_ADDRESS, PORT_NO);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			success = true;
			return success;
		}
		
		/**
		 * Waits until the active fields command is received from the server. Sets the received
		 * active fields in the <code>fields</code> global variable.
		 * @param useTCP - true if the command should come through the TCP socket, false
		 * if it should come through the UDP socket
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void getActiveFields (boolean useTCP) throws IOException, ClassNotFoundException {
			UserCommand command;
			if (useTCP) {
				// Wait for the response from the server
				command = readCommand();
				if (command instanceof ActiveFieldsCommand) {
					fields = ((ActiveFieldsCommand)command).getFields();
				}
			} else {
				command = readUDPCommand();
				if (command instanceof ActiveFieldsCommand) {
					fields = ((ActiveFieldsCommand)command).getFields();
				}
			}
		}
		
		/**
		 * Waits until the UDP port command is received from the server. Creates a new UPD
		 * socket with the received port number plus 1.
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void getUDPPort () throws IOException, ClassNotFoundException {
			// Wait for the response from the server
			UserCommand command = readCommand();
			if (command instanceof UDPPortCommand) {
				udpPort = ((UDPPortCommand)command).getPort();
				udpSocket = new DatagramSocket(udpPort + 1);
			}
		}
	}
	
	private class CommunicationTask extends SwingWorker<Void, Void> {
		
		private ConnectionHandler ch;
		
		public CommunicationTask (ConnectionHandler ch) {
			this.ch = ch;
		}
		
		protected Void doInBackground () {
			messageHandler = new MessageHandler(ch, frame);
			// The Task blocks here until the connection is closed
			messageHandler.start();
			return null;
		}
		
		public void done () {
			disconnect();
		}
	}
}
