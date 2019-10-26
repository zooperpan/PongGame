package pong.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JTextArea;

import pong.com.FieldCharacteristics;
import pong.server.field.FieldHandler;
import pong.server.user.UserServerThread;


public class PongServer extends Thread {

	/* Final fields */
	private static final int SERVER_PORT_NO			= 39990;
	private static final UDPPort udpPortPool []		= {
		new UDPPort(SERVER_PORT_NO + 1),	new UDPPort(SERVER_PORT_NO + 3),
		new UDPPort(SERVER_PORT_NO + 5),	new UDPPort(SERVER_PORT_NO + 7),
		new UDPPort(SERVER_PORT_NO + 9),	new UDPPort(SERVER_PORT_NO + 11), 
		new UDPPort(SERVER_PORT_NO + 13),	new UDPPort(SERVER_PORT_NO + 15)
	};
	
	/* Variables */
	private ServerSocket serverSocket				= null;
	private JTextArea serverTextArea				= null;
	private boolean listening 						= false;
	private LinkedList<UserServerThread> threads	= new LinkedList<UserServerThread>();
	private LinkedList<FieldHandler> fieldHandlers	= new LinkedList<FieldHandler>();
	
	public PongServer (JTextArea serverTextArea) {
		super("Server");
		this.serverTextArea = serverTextArea;
		addText("Update Server is starting...\n");
		try {
			InitializeServer();
        } catch (Exception error) {
        	addText("Server could not be started: " + error.getMessage() + "\r\n");
        }
	}
	
	private void InitializeServer () throws Exception {
		// Create socket
    	serverSocket = new ServerSocket(SERVER_PORT_NO);
    	listening = true;
	}
	
	public void run() {
		boolean connectionAccepted;
		
		if (listening) {
			serverTextArea.append("Listening for connections...\n");
		}
		while (listening) {
			try {
				// Wait for a connection
				Socket socket = serverSocket.accept();
				connectionAccepted = false;
				for (int i = 0; i < udpPortPool.length; i++) {
					if (!udpPortPool[i].isInUse()) {
						udpPortPool[i].setInUse(true);
						UserServerThread ust = new UserServerThread(udpPortPool[i], socket, this);
						serverTextArea.append("New user connected!\n");
						ust.start();
						handleUserThread(true, ust);
						connectionAccepted = true;
						break;
					}
				}
				if (!connectionAccepted) socket.close();
			} catch (IOException e) {
				serverTextArea.append("Error while waiting for connections!\n");
			}
		}
		serverTextArea.append("Pong Server stopped.\n");
	}
	
	public synchronized void handleUserThread (boolean add, UserServerThread ust) {
		if (add) {
			threads.add(ust);
		} else {
			serverTextArea.append("User disconnected!\n");
			// Remove the thread from the list
			threads.remove(ust);
			// Remove it also from the FieldHandler
			FieldHandler fH;
			// Check if the user created or joined a filed
			if ((fH = ust.getFieldHandler()) != null) {
				fH.removeUser(ust);
				// If no more users are connected in the FieldHandler, remove it from the list
				if (!fH.isOpened()) {
					handleFieldHandler(false, fH);
				}
			}
		}
	}
	
	public synchronized void handleFieldHandler (boolean add, FieldHandler fieldHandler) {
		if (add) {
			fieldHandlers.add(fieldHandler);
		} else {
			fieldHandlers.remove(fieldHandler);
		}
	}
	
	public synchronized FieldCharacteristics[] getFieldCharacteristics () {
		if (fieldHandlers.size() == 0) {
			return null;
		}
		FieldCharacteristics result[] = new FieldCharacteristics[fieldHandlers.size()];
		
		for (int i = 0; i < fieldHandlers.size(); i++) {
			FieldHandler fH = fieldHandlers.get(i);
			result[i] = new FieldCharacteristics(fH.isOpened(), fH.getFieldName(), 
					fH.getUser1Name(), fH.getUser2Name());
		}
		return result;
	}
	
	/**
	 * Returns the FieldHandler that handles an ActiveField with the specific name.
	 * @param fieldName - The name of the ActiveField.
	 * @return the FieldHandler that handles an ActiveField with the specific name.
	 */
	public synchronized FieldHandler getFieldHandler (String fieldName) {
		for (int i = 0; i < fieldHandlers.size(); i++) {
			if (fieldHandlers.get(i).getFieldName().equals(fieldName))
				return fieldHandlers.get(i);
		}
		return null;
	}
	
	/**
	 * Stops the server from listening for new connections.
	 */
	public void stopListening () {
		if (listening) {
			listening = false;
			try {
				// Stop all User Server Threads
				try {
					for (int i = 0; i < threads.size(); i++) {
						if (threads.get(i).isThreadAlive()) {
							threads.get(i).closeConnection();
							threads.get(i).join();
						}
					}
				} catch (InterruptedException e) {
					serverTextArea.append("Error stopping User Server Threads!\n");
				}
				serverSocket.close();
			} catch (IOException e) {
				serverTextArea.append("Error closing server socket!\n");
			}
		}
	}
	
	public void addText (String text) {
		serverTextArea.append(text);
	}
	
	public boolean isListening () {
		return listening;
	}
}
