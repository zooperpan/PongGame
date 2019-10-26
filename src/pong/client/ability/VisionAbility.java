package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.com.command.AbilityCommand;

public class VisionAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 20;
	private VisionThread visionThread	= null;
	
	
	public VisionAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}
	
	
	@Override
	public void startAbility () {
		if (isMaster()) {
			// Master uses left bar
			visionThread = new VisionThread(gamePanel.getLeftBar());
			gamePanel.addAbilityThread(this);
		} else {
			// Slave uses right bar
			visionThread = new VisionThread(gamePanel.getRightBar());
			gamePanel.addAbilityThread(this);
		}
	}
	
	@Override
	public boolean run () {
		if (visionThread != null) {
			return visionThread.run();
		} else return true;
	}
	
	@Override
	public void stop () {
		stopAbility();
	}
	
	@Override
	public void stopAbility () {
		if (visionThread != null) {
			if (isMaster()) {
				// Master uses left bar
				gamePanel.getLeftBar().removeActiveAbility("vision");
			} else {
				// Slave uses right bar
				gamePanel.getRightBar().removeActiveAbility("vision");
			}
			visionThread = null;
		}
	}
	
	@Override
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}
	private class VisionThread {
		
		private static final int COUNT	= 3;
		private int couner				= 0;
		
		public VisionThread (Bar bar) {
			bar.addActiveAbility("vision");
		}
		
		public boolean run () {
			if (couner++ < COUNT) return false;
			else return true;
		}
	}
}
