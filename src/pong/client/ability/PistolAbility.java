package pong.client.ability;

import java.awt.Color;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.Bullet;
import pong.com.command.AbilityCommand;

public class PistolAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 10;
	private PistolThread pistolThread	= null;
	private Bullet bullet				= null;
	
	public PistolAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}
	
	@Override
	protected void startAbility () {
		bullet = new Bullet(10, 5, Color.BLACK);
		if (isMaster()) {
			// Master uses left bar
			bullet.setStartingPosition(gamePanel.getLeftBar().getXPos(), gamePanel.getLeftBar().getYPos());
			bullet.setActive(true);
			// Bullet goes to the right
			pistolThread = new PistolThread(gamePanel.getRightBar(), PistolThread.RIGHT_SIDE);
		} else {
			// Slave uses right bar
			bullet.setStartingPosition(gamePanel.getRightBar().getXPos(), gamePanel.getRightBar().getYPos());
			bullet.setActive(true);
			// Bullet goes to the left
			pistolThread = new PistolThread(gamePanel.getLeftBar(), PistolThread.LEFT_SIDE);
		}
		gamePanel.addMovingObject(bullet);
		gamePanel.addAbilityThread(this);
	}
	
	@Override
	public boolean run () {
		if (pistolThread != null) {
			return pistolThread.run();
		} else return true;
	}
	
	@Override
	public void stop () {
		stopAbility();
	}
	
	@Override
	protected void stopAbility () {
		if (pistolThread != null) {
			pistolThread = null;
			bullet.setActive(false);
			bullet.repaint();
			gamePanel.removeMovingObject(bullet);
			bullet = null;
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}
	
	private class PistolThread {
		
		public static final int	LEFT_SIDE	= 0;
		public static final int	RIGHT_SIDE	= 1;
		private Bar target;
		private int side;
		
		public PistolThread (Bar target, int side) {
			this.target = target;
			this.side = side;
		}
		
		public boolean run () {
			if (bullet.isActive()) {
				if (side == RIGHT_SIDE) {
					// Target is at the right side
					// Check if we reached the target
					if (bullet.getXPos() + bullet.getVelocity() >= target.getXPos() - (target.getWidth() / 2)) {
						// Check if we hit the target
						if ((bullet.getYPos() > target.getYPos() - (target.getHeight() / 2)) && 
								(bullet.getYPos() < target.getYPos() + (target.getHeight() / 2))) {
							target.setWidth(0);
							target.setHeight(0);
							target.repaint();
							return true;
						} else return true;
					} else {
						bullet.setXPos(bullet.getXPos() + bullet.getVelocity());
						return false;
					}
				} else {
					// Target is at the left side
					// Check if we reached the target
					if (bullet.getXPos() - bullet.getVelocity() <= target.getXPos() + (target.getWidth() / 2)) {
						// Check if we hit the target
						if ((bullet.getYPos() > target.getYPos() - (target.getHeight() / 2)) && 
								(bullet.getYPos() < target.getYPos() + (target.getHeight() / 2))) {
							target.setWidth(0);
							target.setHeight(0);
							target.repaint();
							return true;
						} else return true;
					} else {
						bullet.setXPos(bullet.getXPos() - bullet.getVelocity());
						return false;
					}
				}
			} else return true;
		}
	}
}
