package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.MovingObject;
import pong.com.command.AbilityCommand;

public class FatAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 25;
	private FatThread fatThread			= null;

	public FatAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}

	@Override
	public void startAbility () {
		if (isMaster()) {
			// Master uses left bar
			fatThread = new FatThread(gamePanel.getLeftBar(), FatThread.LEFT_SIDE);
		} else {
			// Slave uses right bar
			fatThread = new FatThread(gamePanel.getRightBar(), FatThread.RIGHT_SIDE);
		}
		gamePanel.addAbilityThread(this);
	}
	
	@Override
	public boolean run () {
		if (fatThread != null) {
			return fatThread.run();
		} else return true;
	}
	
	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	public void stopAbility () {
		if (fatThread != null) {
			fatThread = null;
			if (isMaster()) {
				if (gamePanel.getLeftBar().getHeight() != 0) {
					gamePanel.getLeftBar().setHeight(80);
					gamePanel.getLeftBar().setWidth(10);
				}
			} else {
				if (gamePanel.getRightBar().getHeight() != 0) {
					gamePanel.getRightBar().setHeight(80);
					gamePanel.getRightBar().setWidth(10);
				}
			}
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}

	private class FatThread {

		public static final int	LEFT_SIDE	= 0;
		public static final int	RIGHT_SIDE	= 1;

		private int side					= RIGHT_SIDE;
		private MovingObject movingObject 	= null;
		private int count					= 0;

		public FatThread (MovingObject movingObject, int side) {
			this.side = side;
			this.movingObject = movingObject;
		}

		public boolean run () {
			if (movingObject.getHeight() == 0) return true;
			if (count < 50) {
				if (side == RIGHT_SIDE) {
					movingObject.setHeight(movingObject.getHeight() + 4);
					movingObject.setWidth( movingObject.getWidth() + 4);
					movingObject.setXPos(movingObject.getXPos() - 2);
				} else {
					movingObject.setHeight(movingObject.getHeight() + 4);
					movingObject.setWidth( movingObject.getWidth() + 4);
					movingObject.setXPos(movingObject.getXPos() + 2);
				}
				count++;
				return false;
			} else if (count < 100) {
				if (side == RIGHT_SIDE) {
					movingObject.setHeight(movingObject.getHeight() - 4);
					movingObject.setWidth(movingObject.getWidth() - 4);
					movingObject.setXPos(movingObject.getXPos() + 2);
				} else {
					movingObject.setHeight(movingObject.getHeight() - 4);
					movingObject.setWidth( movingObject.getWidth() - 4);
					movingObject.setXPos(movingObject.getXPos() - 2);
				}
				count++;
				return false;
			} else return true;
		}
	}
}
