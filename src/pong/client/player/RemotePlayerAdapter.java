package pong.client.player;

import java.awt.event.KeyEvent;

import pong.client.gui.pong.Bar;
import pong.client.gui.pong.MovingObject;
import pong.com.command.KeyReleasedCommand;

public class RemotePlayerAdapter {

	private MovingObject movingObject 	= null;
	private boolean upPressed 			= false;
	private boolean downPressed			= false;
	private Player player 				= null;
	private static final int VELOCITY 	= Bar.STARTING_VELOCITY;
	
	public RemotePlayerAdapter (MovingObject movingObject, Player player) {
		this.movingObject = movingObject;
		this.player = player;
	}
	
	public void keyPressed (int ke) {
		if (ke == KeyEvent.VK_UP) {
			upPressed = true;
			movingObject.setVelocity(VELOCITY);
			movingObject.setAngle(90);
		} else if (ke == KeyEvent.VK_DOWN) {
			downPressed = true;
			movingObject.setVelocity(VELOCITY);
			movingObject.setAngle(270);
		} else if (ke == KeyEvent.VK_RIGHT) {
			player.useRightAbility();
		} else if (ke == KeyEvent.VK_LEFT) {
			player.useLeftAbility();
		}
	}
	
	public void keyReleased (int ke, KeyReleasedCommand command) {
		if (ke == KeyEvent.VK_UP) {
			if (downPressed) {
				movingObject.setVelocity(VELOCITY);
				movingObject.setAngle(270);
			}
			else movingObject.setVelocity(0);
			upPressed = false;
		} else if (ke == KeyEvent.VK_DOWN) {
			if (upPressed) {
				movingObject.setVelocity(VELOCITY);
				movingObject.setAngle(90);
			}
			else movingObject.setVelocity(0);
			downPressed = false;
		}
		movingObject.setYPos(command.getYPos());
	}
}
