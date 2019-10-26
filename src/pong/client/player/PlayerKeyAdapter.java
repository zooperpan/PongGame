package pong.client.player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.MovingObject;
import pong.com.command.Command;
import pong.com.command.KeyReleasedCommand;
import pong.com.command.UserCommand;

public class PlayerKeyAdapter extends KeyAdapter {

	private GamePanel gamePanel 		= null;
	private MovingObject movingObject 	= null;
	private boolean upPressed 			= false;
	private boolean downPressed			= false;
	private Player player 				= null;
	private static final int VELOCITY 	= Bar.STARTING_VELOCITY;
	
	public PlayerKeyAdapter (GamePanel gamePanel, MovingObject movingObject, Player player) {
		this.gamePanel = gamePanel;
		this.movingObject = movingObject;
		this.player = player;
	}
	
	public void keyPressed (KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = true;
			movingObject.setVelocity(VELOCITY);
			movingObject.setAngle(90);
			gamePanel.sendCommand(new UserCommand(Command.UP_PRESSED));
		} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = true;
			movingObject.setVelocity(VELOCITY);
			movingObject.setAngle(270);
			gamePanel.sendCommand(new UserCommand(Command.DOWN_PRESSED));
		} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
			gamePanel.sendCommand(new UserCommand(Command.RIGHT_PRESSED));
			player.useRightAbility();
		} else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
			gamePanel.sendCommand(new UserCommand(Command.LEFT_PRESSED));
			player.useLeftAbility();
		}
	}
	
	public void keyReleased (KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_UP) {
			if (downPressed) {
				movingObject.setVelocity(VELOCITY);
				movingObject.setAngle(270);
			}
			else movingObject.setVelocity(0);
			upPressed = false;
			gamePanel.sendCommand(new KeyReleasedCommand(Command.UP_RELEASED, movingObject.getYPos()));
		} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			if (upPressed) {
				movingObject.setVelocity(VELOCITY);
				movingObject.setAngle(90);
			}
			else movingObject.setVelocity(0);
			downPressed = false;
			gamePanel.sendCommand(new KeyReleasedCommand(Command.DOWN_RELEASED, movingObject.getYPos()));
		}
	}
}
