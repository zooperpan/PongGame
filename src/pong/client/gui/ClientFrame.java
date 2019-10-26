package pong.client.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import pong.client.conn.ConnectionHandler;
import pong.client.conn.MessageReceiver;
import pong.client.gui.dialog.AbilityDialog;
import pong.client.gui.dialog.FieldsDialog;
import pong.client.gui.dialog.UserNameDialog;
import pong.com.FieldCharacteristics;
import pong.com.command.AbilitiesCommand;
import pong.com.command.Command;
import pong.com.command.UserCommand;
import pong.com.command.UserEnteredCommand;

public class ClientFrame extends JFrame implements ActionListener, MessageReceiver {


	private static final long serialVersionUID 	= 1L;
	private boolean connected 					= false;
	private boolean gamePaused					= false;
	private boolean master 						= true;
	private boolean opponentReady 				= false;
	private boolean userReady 					= false;
	private GamePanel gamePanel 				= null;
	private ConnectionHandler connectionHandler	= null;
	private JMenuItem connectMenuItem			= null;
	private JLabel statusLabel					= null;
	private String abilities[]					= null;
	private String remoteAbilities[]			= null;
	private String userName						= null;
	private String opponentName					= null;
	
	public ClientFrame () {
		try {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing (WindowEvent evt) {
					exitApplication();
				}
			});
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit () throws Exception {
		//this.setResizable(false);
		
		// Create the menu bar
		setJMenuBar(createMenuBar());
		
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setOpaque(true);
		contentPane.setLayout(new BorderLayout());
		JToolBar statusBar = new JToolBar();
		statusLabel = new JLabel("Welcome to the ultimate pong game!!!");
		statusBar.add(statusLabel);
		contentPane.add(statusBar, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates the menu bar of the frame.
	 * @return the menu bar of the frame.
	 */
	private JMenuBar createMenuBar () {
		JMenuBar menuBar = new JMenuBar();

		// Create the File menu
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(menu);

		// Create the Import menu item
		connectMenuItem = new JMenuItem("Connect");
		connectMenuItem.setIcon(new ImageIcon(this.getClass().getResource("/pong/client/gui/images/connect.png")));
		connectMenuItem.setActionCommand("connect");
		connectMenuItem.addActionListener(this);
		menu.add(connectMenuItem);
		
		// Create the Exit menu item
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.setIcon(new ImageIcon(this.getClass().getResource("/pong/client/gui/images/exit24.png")));
		menuItem.setActionCommand("exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		return menuBar;
	}
	
	private void exitApplication () {
		// Stop the game (if played)
		if (gamePanel != null) {
			gamePanel.stopGame();
		}
		// Stop the communication (if connected)
		if (connectionHandler != null) connectionHandler.stopCommunication();
		// Consider adding a small sleep here...
    	System.exit(0);
	}

	public void actionPerformed (ActionEvent e) {
		if ("exit".equals(e.getActionCommand())) {
			exitApplication();
		} else if ("connect".equals(e.getActionCommand())) {
			connectMenuItem.setEnabled(false);
			if (connected) {
				connectionHandler.connect();
			} else {
				UserNameDialog und = new UserNameDialog();
				// Show the "Set User Name" dialog
				GUIStatic.setDialogCenterLocation(und);
				und.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				und.setVisible(true);
				if (!und.getUserName().equals("")) {
					userName = und.getUserName();
					statusLabel.setText("Connecting to server...");
					connectionHandler = new ConnectionHandler(this, und.getUserName());
					connectionHandler.connect();
				} else {
					connectMenuItem.setEnabled(true);
				}
			}
		}
	}
	
	public void connectionEstablished (boolean success, FieldCharacteristics fields[]) {
		if (success) {
			connected = true;
			statusLabel.setText("Connected");
			FieldsDialog fd = new FieldsDialog(connectionHandler, fields, userName);
			// Show the "Fields" dialog
			GUIStatic.setDialogCenterLocation(fd);
			fd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			fd.setVisible(true);
			if (!fd.isConnected()) {
				statusLabel.setText("Select \"Connect\" to create or join a game!");
				connectMenuItem.setEnabled(true);
			} else {
				// Get the action by the user
				if (fd.getCreateORjoin()) {
					statusLabel.setText("Waiting for a user to join the game...");
					master = true;
					connectionHandler.startCommunication();
				} else {
					opponentName = fd.getOpponentName();
					statusLabel.setText("Opponent name: " + opponentName + ". Let the game begin...");
					master = false;
					connectionHandler.startCommunication();
					AbilityDialog ad = new AbilityDialog();
					GUIStatic.setDialogCenterLocation(ad);
					ad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
					ad.setVisible(true);
					abilities = ad.getAbilities();
					userReady = true;
					connectionHandler.sendUDPCommand(new AbilitiesCommand(abilities));
					connectionHandler.sendUDPCommand(new UserCommand(Command.USER_READY));
					if (opponentReady) {
						startGame();
					}
				}
			}
		} else {
			statusLabel.setText("Could not connect to server!");
			connectMenuItem.setEnabled(true);
			connectionHandler = null;
		}
	}
	
	private void startGame () {
		if (gamePaused) {
			connectionHandler.setReceiver(gamePanel);
			if (master) gamePanel.startGame();
		} else {
			gamePanel = new GamePanel(this, connectionHandler, master, userName, opponentName);
			connectionHandler.setReceiver(gamePanel);
			JPanel contentPane = (JPanel) getContentPane();
			contentPane.add(gamePanel, BorderLayout.CENTER);
			contentPane.validate();
			contentPane.repaint();
			if (master) gamePanel.startGame();
		}
	}
	
	/**
	 * This method is called from the MessageHandler task. So we need to return
	 * back to it as soon as possible. This means that a new task has to started
	 * in order to show the "Select Abilities" dialog.
	 * @param opponentName - the name of the opponent that joined the game.
	 */
	public void opponentJoined (String opponentName) {
		this.opponentName = opponentName;
		statusLabel.setText("Opponent name: " + opponentName + ". Let the game begin...");
		OpponentJoinedTask opt = new OpponentJoinedTask();
		opt.execute();
	}

	/**
	 * Called when the opponent has exited the game.
	 */
	public void opponentExited () {
		if (gamePanel != null) {
			JPanel contentPane = (JPanel) getContentPane();
			contentPane.remove(gamePanel);
			contentPane.validate();
			contentPane.repaint();
			gamePanel = null;
			connectionHandler.setReceiver(this);
		}
		statusLabel.setText("Waiting for a user to join the game...");
		master 			= true;
		opponentReady 	= false;
		userReady 		= false;
		abilities 		= null;
		remoteAbilities = null;
	}

	public void opponentReady() {
		opponentReady = true;
		if (userReady) {
			startGame();
		}
	}
	
	public void reChooseAbilities() {
		gamePaused = true;
		userReady = false;
		opponentReady = false;
		connectionHandler.setReceiver(this);
		OpponentJoinedTask opt = new OpponentJoinedTask();
		opt.execute();
	}
	
	public boolean isGamePaused () {
		return gamePaused;
	}
	
	public void setGamePaused (boolean gamePaused) {
		this.gamePaused = gamePaused;
	}
	
	public void setRemoteAbilities (String[] remoteAbilities) {
		this.remoteAbilities = remoteAbilities;
	}
	
	public String[] getRemoteAbilities () {
		return remoteAbilities;
	}
	
	public String[] getAbilities () {
		return abilities;
	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	private class OpponentJoinedTask extends SwingWorker<Void, Void> {

		protected Void doInBackground() throws Exception {
			AbilityDialog ad = new AbilityDialog();
			GUIStatic.setDialogCenterLocation(ad);
			ad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			ad.setVisible(true);
			abilities = ad.getAbilities();
			return null;
		}
		
		public void done () {
			userReady = true;
			connectionHandler.sendUDPCommand(new AbilitiesCommand(abilities));
			connectionHandler.sendUDPCommand(new UserCommand(Command.USER_READY));
			if (opponentReady) {
				startGame();
			}
		}
	}

	@Override
	public void receive (UserCommand command) {
		switch (command.getCommand()) {
		case USER_ENTERED:
			// A new opponent has joined the game
			opponentJoined(((UserEnteredCommand)command).getOpponentName());
			break;
		case USER_REMOVED:
			// The opponent has exited the game
			opponentExited();
			break;
		case USER_READY:
			// The opponent is ready to play
			opponentReady();
			break;
		case ABILITIES:
			// The opponent has selected his abilities
			setRemoteAbilities(((AbilitiesCommand)command).getAbilities());
			break;
		default:
			break;
		}
	}
}
