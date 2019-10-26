package pong.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pong.client.ability.AbilityThread;
import pong.client.conn.ConnectionHandler;
import pong.client.conn.MessageReceiver;
import pong.client.game.GameManager;
import pong.client.game.PongGameManager;
import pong.client.gui.pong.Ball;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.MovingObject;
import pong.client.gui.pong.PlayField;
import pong.client.motion.BallBounceManager;
import pong.client.motion.BallMotionManager;
import pong.client.motion.BarBounceManager;
import pong.client.motion.BarMotionManager;
import pong.client.motion.MotionManager;
import pong.client.motion.MotionUpdater;
import pong.client.player.Player;
import pong.client.player.PlayerKeyAdapter;
import pong.client.player.RemotePlayerAdapter;
import pong.com.command.AbilityCommand;
import pong.com.command.BallAngleCommand;
import pong.com.command.Command;
import pong.com.command.KeyReleasedCommand;
import pong.com.command.UserCommand;

public class GamePanel extends JPanel implements MessageReceiver {


	private static final long serialVersionUID 		= 1L;
	private ClientFrame parent						= null;
	private ConnectionHandler connectionHandler		= null;
	private boolean master							= true;
	private boolean gameStarted						= false;
	private PlayField playField						= null;
	private Ball ball								= null;
	private Bar leftBar								= null;
	private Bar rightBar							= null;
	private Player leftPlayer						= null;
	private Player rightPlayer						= null;
	private MotionUpdater motionUpdater				= null;
	private RemotePlayerAdapter remoteAdapter		= null;
	private int player1Score						= 0;
	private int player2Score						= 0;
	private JPanel centerPanel						= null;
	private String userName							= null;
	private String opponentName						= null;
	private JLabel userLabel						= null;
	private JLabel opponentLabel					= null;
	private JLabel leftAbilityLabel					= null;
	private JLabel rightAbilityLabel				= null;
	private ArrayList<AbilityThread> abilThreads	= null;
	
	public GamePanel (ClientFrame parent, ConnectionHandler connectionHandler, boolean master, String userName, String opponentName) {
		super(new BorderLayout());
		this.parent = parent;
		this.connectionHandler = connectionHandler;
		this.master = master;
		this.userName = userName;
		this.opponentName = opponentName;
		abilThreads = new ArrayList<AbilityThread>();
		jbInit();
	}
	
	private void jbInit () {
		add(createCenterPanel(), BorderLayout.CENTER);
		add(createSouthPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel createCenterPanel () {
		centerPanel = new JPanel(null);
		
		// Create the play field
		playField = new PlayField(784, 504, Color.BLACK);
		playField.setXPos(playField.getWidth() / 2);
		playField.setYPos(playField.getHeight() / 2);
		centerPanel.add(playField);
		
		// Create the ball
		ball = new Ball (30, Color.RED, "Ball");
		ball.setStartingPosition(playField.getWidth() / 2, playField.getHeight() / 2);
		ball.setVelocity(Ball.STARTING_VELOCITY);
		MotionManager ballMM = new BallMotionManager(ball, new BallBounceManager(0, playField.getHeight(), playField.getWidth(), 0));
		centerPanel.add(ball);
		
		// Create the bars and players (the master always plays left side)
		leftBar = new Bar(10, 80, Color.BLUE, "BarL");
		rightBar = new Bar(10, 80, Color.BLUE, "BarR");
		leftBar.setStartingPosition(leftBar.getWidth() / 2, playField.getHeight() / 2);
		rightBar.setStartingPosition(playField.getWidth() - (rightBar.getWidth() / 2), playField.getHeight() / 2);
		if (master) {
			// Active user is at left side
			leftPlayer = new Player(leftBar, this, true, false, parent.getAbilities());
			rightPlayer = new Player(rightBar, this, false, true, parent.getRemoteAbilities());
			this.addKeyListener(new PlayerKeyAdapter(this, leftBar, leftPlayer));
			remoteAdapter = new RemotePlayerAdapter(rightBar, rightPlayer);
		} else {
			// Active user is at right side
			rightPlayer = new Player(rightBar, this, false, false, parent.getAbilities());
			leftPlayer = new Player(leftBar, this, true, true, parent.getRemoteAbilities());
			this.addKeyListener(new PlayerKeyAdapter(this, rightBar, rightPlayer));
			remoteAdapter = new RemotePlayerAdapter(leftBar, leftPlayer);
		}
		MotionManager leftBarMM = new BarMotionManager(leftBar, new BarBounceManager(0, playField.getHeight(), playField.getWidth(), 0));
		MotionManager rightBarMM = new BarMotionManager(rightBar, new BarBounceManager(0, playField.getHeight(), playField.getWidth(), 0));
		centerPanel.add(leftBar);
		centerPanel.add(rightBar);
		
		PongGameManager pongGM = new PongGameManager(ball, rightBar, leftBar);
		// Create the motion thread
		motionUpdater = new MotionUpdater(pongGM, this);
		motionUpdater.addMotionManager(ballMM);
		motionUpdater.addMotionManager(leftBarMM);
		motionUpdater.addMotionManager(rightBarMM);
		
		return centerPanel;
	}
	
	private JPanel createSouthPanel () {
		JPanel southPanel = new JPanel(new BorderLayout());
		
		userLabel = new JLabel(userName + ": 0");
		opponentLabel = new JLabel(opponentName + ": 0");
		userLabel.setForeground(Color.BLUE);
		opponentLabel.setForeground(Color.BLUE);
		userLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
		opponentLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
		
		JPanel gridPanel = new JPanel(new GridBagLayout());
		leftAbilityLabel = new JLabel();
		rightAbilityLabel = new JLabel();
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + parent.getAbilities()[0] + "S.png"));
		leftAbilityLabel.setIcon(icon);
		icon = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + parent.getAbilities()[1] + "S.png"));
		rightAbilityLabel.setIcon(icon);
		leftAbilityLabel.setAlignmentX(CENTER_ALIGNMENT);
		rightAbilityLabel.setAlignmentX(CENTER_ALIGNMENT);
		gridPanel.add(leftAbilityLabel, new GridBagConstraints (0, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		gridPanel.add(rightAbilityLabel, new GridBagConstraints (1, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		southPanel.add(gridPanel, BorderLayout.CENTER);
		
		if (master) {
			southPanel.add(userLabel, BorderLayout.WEST);
			southPanel.add(opponentLabel, BorderLayout.EAST);
		} else {
			southPanel.add(userLabel, BorderLayout.EAST);
			southPanel.add(opponentLabel, BorderLayout.WEST);
		}
		return southPanel;
	}
	
	public void startGame () {
		leftBar.reset();
		rightBar.reset();
		
		if (parent.isGamePaused()) {
			if (master) {
				// Active user is at left side
				leftPlayer.setAbilities(this, parent.getAbilities());
				rightPlayer.setAbilities(this, parent.getRemoteAbilities());
			} else {
				// Active user is at right side
				rightPlayer.setAbilities(this, parent.getAbilities());
				leftPlayer.setAbilities(this, parent.getRemoteAbilities());
			}
			ImageIcon icon = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + parent.getAbilities()[0] + "S.png"));
			leftAbilityLabel.setIcon(icon);
			icon = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + parent.getAbilities()[1] + "S.png"));
			rightAbilityLabel.setIcon(icon);
			parent.setGamePaused(false);
		}
		
		if (master) {
			int angle = (int) (Math.random() * 360);
			if (angle > 45  && angle <= 90) angle = 45;
			else if (angle > 90  && angle <= 135) angle = 135;
			else if (angle > 225  && angle <= 270) angle = 225;
			else if (angle > 270  && angle <= 315) angle = 315;
			connectionHandler.sendUDPCommand(new BallAngleCommand(angle));
			ball.setAngle(angle);
		}
		if (!gameStarted) {
			this.requestFocusInWindow();
			gameStarted = true;
		}
	}
	
	public void stopGame () {
		gameStarted = false;
		if (leftPlayer != null) leftPlayer.kill();
		if (rightPlayer != null) rightPlayer.kill();
	}
	
	public void pauseGame () {
		gameStarted = false;
		if (leftPlayer != null) leftPlayer.kill();
		if (rightPlayer != null) rightPlayer.kill();
		sendCommand(new UserCommand(Command.PAUSE_GAME));
	}
	
	
	public void setWinner (int winner) {
		if (winner == GameManager.PALYER1_WINS) {
			player1Score++;
			if (master) {
				userLabel.setText(userName + ": " + Integer.toString(player1Score));
			} else {
				opponentLabel.setText(opponentName + ": " + Integer.toString(player1Score));
			}
		} else if (winner == GameManager.PALYER2_WINS) {
			player2Score++;
			if (master) {
				opponentLabel.setText(opponentName + ": " + Integer.toString(player2Score));
			} else {
				userLabel.setText(userName + ": " + Integer.toString(player2Score));
			}
		}
		// Stop all abilities
		leftPlayer.stopLeftAbility();
		leftPlayer.stopRightAbility();
		rightPlayer.stopLeftAbility();
		rightPlayer.stopRightAbility();
		// Remove all the ability threads from the list
		while (abilThreads.size() > 0) abilThreads.remove(0);
		// Every 5 goals, users should choose new abilities
		if ((player1Score + player2Score) % 5 == 0) {
			pauseGame();
			parent.reChooseAbilities();
		} else {
			// Start a new round
			startGame();
		}
	}
	
	public void setLeftAbilityEnabled (boolean enabled) {
		leftAbilityLabel.setEnabled(enabled);
	}
	
	public void setRightAbilityEnabled (boolean enabled) {
		rightAbilityLabel.setEnabled(enabled);
	}
	
	public void addMovingObject (MovingObject movingObject) {
		centerPanel.add(movingObject);
	}
	
	public void removeMovingObject (MovingObject movingObject) {
		centerPanel.remove(movingObject);
	}
	
	public void addMotionManager (MotionManager motionManager) {
		motionUpdater.addMotionManager(motionManager);
	}
	
	public void removeMotionManager (MotionManager motionManager) {
		motionUpdater.removeMotionManager(motionManager);
	}
	
	public MovingObject getMovingObject (String name) {
		return motionUpdater.getMovingObject(name);
	}
	
	public int getPlayFieldWidth () {
		return playField.getWidth();
	}
	
	public int getPlayFieldHeight () {
		return playField.getHeight();
	}
	
	/**
	 * Sends a command through the connection handler to the other user.
	 * @param command - the command to be sent.
	 */
	public void sendCommand (UserCommand command) {
		connectionHandler.sendUDPCommand(command);
	}

	@Override
	public void receive (UserCommand command) {
		switch (command.getCommand()) {
		case BALL_ANGLE:
			// The BALL_ANGLE command is send by the master
			ball.reset();
			ball.setAngle(((BallAngleCommand)command).getAngle());
			startGame();
			break;
		case USER_REMOVED:
			// The opponent has exited the game
			stopGame();
			parent.opponentExited();
			break;
		case UP_PRESSED:
			remoteAdapter.keyPressed(KeyEvent.VK_UP);
			break;
		case UP_RELEASED:
			remoteAdapter.keyReleased(KeyEvent.VK_UP, (KeyReleasedCommand) command);
			break;
		case DOWN_PRESSED:
			remoteAdapter.keyPressed(KeyEvent.VK_DOWN);
			break;
		case DOWN_RELEASED:
			remoteAdapter.keyReleased(KeyEvent.VK_DOWN, (KeyReleasedCommand) command);
			break;
		case RIGHT_PRESSED:
			remoteAdapter.keyPressed(KeyEvent.VK_RIGHT);
			break;
		case LEFT_PRESSED:
			remoteAdapter.keyPressed(KeyEvent.VK_LEFT);
			break;
		case MOTION_UPDATE:
			motionUpdater.update();
			if (abilThreads.size() > 0) {
				for (int i = 0; i < abilThreads.size(); i++) {
					if (abilThreads.get(i).run()) {
						// The setWinner() method may have been called, which removes all the abilities
						if (abilThreads.size() > 0) {
							// Ability must be stopped and removed from the list
							abilThreads.get(i).stop();
							abilThreads.remove(i);
							i--;
						}
					}
				}
			}
			break;
		case ABILITY:
			// Find the remote player
			if (leftPlayer.isRemote()) {
				leftPlayer.handleAbilityCommand((AbilityCommand)command);
			} else {
				rightPlayer.handleAbilityCommand((AbilityCommand)command);
			}
		default:
			break;
		}
	}
	
	public Ball getBall () {
		return ball;
	}
	
	public Bar getLeftBar () {
		return leftBar;
	}
	
	public Bar getRightBar () {
		return rightBar;
	}
	
	public void addAbilityThread (AbilityThread t) {
		abilThreads.add(t);
	}
}
