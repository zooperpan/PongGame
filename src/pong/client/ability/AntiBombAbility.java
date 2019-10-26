package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.com.command.AbilityCommand;

public class AntiBombAbility extends Ability implements AbilityThread {

	private static final int REUSE_TIME	= 40;
	private AntiBombThread abThread		= null;
	private boolean started 			= false;
	
	public AntiBombAbility (GamePanel gamePanel, String name, boolean master, boolean remoteUser, int registeredKey) {
		super(gamePanel, name, REUSE_TIME, master, remoteUser, registeredKey);
	}

	@Override
	protected void startAbility () {
		if (isMaster()) {
			abThread = new AntiBombThread(gamePanel.getLeftBar());
		} else {
			abThread = new AntiBombThread(gamePanel.getRightBar());
		}
		gamePanel.addAbilityThread(this);
	}
	
	@Override
	public boolean run () {
		if (abThread != null) {
			if (!started)  {
				started = true;
				abThread.start();
				return false;
			} else if (abThread.isAlive()) return false;
		}
		return true;
	}

	@Override
	public void stop () {
		stopAbility();
	}
	
	@Override
	protected void stopAbility () {
		if (abThread != null) {
			abThread.kill();
			try {
				abThread.join();
			} catch (InterruptedException e) {}
			abThread = null;
			started = false;
		}
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		return;
	}
	
	private class AntiBombThread extends Thread {
		
		private static final int COOLDOWN = 50;
		private Bar userBar;
		private boolean keepRunning	= false;
		
		public AntiBombThread (Bar userBar) {
			super("antibombThread");
			this.userBar = userBar;
		}
		
		public void run () {
			keepRunning = true;
			userBar.addActiveAbility("antibomb");
			userBar.repaint();
			for (int i = 0; (i < COOLDOWN) && keepRunning; i++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			keepRunning = false;
			userBar.removeActiveAbility("antibomb");
			userBar.repaint();
		}
		
		public void kill () {
			keepRunning = false;
			this.interrupt();
		}
	}
}
