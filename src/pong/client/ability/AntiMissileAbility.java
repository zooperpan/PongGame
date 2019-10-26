package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.client.gui.pong.Laser;
import pong.client.gui.pong.Missile;
import pong.com.command.AbilityCommand;

public class AntiMissileAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME			= 60;
	private AntiMissileThread antiMissileThread	= null;
	private Missile target						= null;
	private Laser laser							= null;
	
	public AntiMissileAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}
	
	@Override
	protected void startAbility () {
		
		if (isMaster()) {
			// Master uses left bar
			if ((target = (Missile)gamePanel.getMovingObject("MissileL")) != null) {
				antiMissileThread = new AntiMissileThread(gamePanel.getLeftBar(), AntiMissileThread.RIGHT_SIDE);
			}
			
		} else {
			// Slave uses right bar
			if ((target = (Missile)gamePanel.getMovingObject("MissileR")) != null) {
				antiMissileThread = new AntiMissileThread(gamePanel.getRightBar(), AntiMissileThread.LEFT_SIDE);
			}
		}
		gamePanel.addAbilityThread(this);
	}

	@Override
	public boolean run () {
		if (antiMissileThread != null) {
			return antiMissileThread.run();
		} else return true;
	}

	@Override
	public void stop () {
		stopAbility();
	}

	@Override
	protected void stopAbility () {
		if (antiMissileThread != null) {
			antiMissileThread.kill();
			antiMissileThread = null;
			
		}
		if (target != null) {
			target = null;
		}
		if (laser != null) {
			gamePanel.removeMovingObject(laser);
			gamePanel.repaint();
			laser = null;
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}
	
	private class AntiMissileThread {
		
		public static final int	LEFT_SIDE	= 0;
		public static final int	RIGHT_SIDE	= 1;
		public static final int COUNT		= 30;
		private int counter					= 0;
		private Bar source;
		
		public AntiMissileThread (Bar source, int comingFrom) {
			this.source = source;
			source.addActiveAbility("decoy");
			
			int width, height;
			
			width = Math.abs(target.getXPos() - source.getXPos());
			height = Math.abs(target.getYPos() - source.getYPos());
			if (height < 8) height = 8;
			laser = new Laser(width, height, "Laser");
			
			if (comingFrom == RIGHT_SIDE) {
				if (target.getYPos() < source.getYPos()) {
					// Missile is higher
					laser.setStartingPosition(source.getXPos() + (source.getWidth() / 2) + (width / 2), 
												source.getYPos() - (height / 2));
					laser.setPoints(0, height, width, 0);
				} else {
					// Missile is lower
					laser.setStartingPosition(source.getXPos() + (source.getWidth() / 2) + (width / 2), 
												source.getYPos() + (height / 2));
					laser.setPoints(0, 0, width, height);
				}
			} else {
				if (target.getYPos() < source.getYPos()) {
					// Missile is higher
					laser.setStartingPosition(target.getXPos() + (width / 2), 
												source.getYPos() - (height / 2));
					laser.setPoints(0, 0, width, height);
				} else {
					// Missile is lower
					laser.setStartingPosition(target.getXPos() + (width / 2), 
												source.getYPos() + (height / 2));
					laser.setPoints(0, height, width, 0);
				}
			}
			gamePanel.addMovingObject(laser);
			gamePanel.repaint();
		}
		
		public boolean run () {
			if (counter++ < COUNT) {
				laser.repaint();
				return false;
			}
			return true;
		}
		
		public void kill () {
			source.removeActiveAbility("decoy");
		}
	}
}
