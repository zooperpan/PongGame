package pong.client.ability;

import pong.client.gui.GamePanel;
import pong.com.command.AbilityCommand;

public abstract class Ability {

	public static final int LEFT_KEY	= 0;
	public static final int RIGHT_KEY	= 1;
	
	private String name					= null;
	private int time					= 0;
	private boolean canReuse		 	= true;
	private boolean master				= true;
	private boolean remoteUser			= false;
	private AbilityTimer abilityTimer	= null;
	private int registeredKey			= LEFT_KEY;
	protected GamePanel gamePanel		= null;
	
	
	public Ability (GamePanel gamePanel, String name, int time, boolean master, boolean remoteUser, int registeredKey) {
		this.gamePanel = gamePanel;
		this.name = name;
		this.time = time;
		this.master = master;
		this.remoteUser = remoteUser;
		this.registeredKey = registeredKey;
	}
	
	private void setAbilityEnabled (boolean enabled) {
		if (!remoteUser) {
			switch (registeredKey) {
			case LEFT_KEY:
				gamePanel.setLeftAbilityEnabled(enabled);
				break;
			case RIGHT_KEY:
				gamePanel.setRightAbilityEnabled(enabled);
				break;
			default:
				break;
			}
		}
	}
	
	public String getName () {
		return name;
	}
	
	public void use () {
		if (canReuse) {
			setAbilityEnabled(false);
			canReuse = false;
			abilityTimer = new AbilityTimer(time, this);
			abilityTimer.start();
			startAbility();
		}
	}
	
	public void stop (boolean stopTimer) {
		if (stopTimer) {
			canReuse = true;
			if (abilityTimer != null) {
				abilityTimer.kill();
				try {
					abilityTimer.join();
				} catch (InterruptedException e) {}
				abilityTimer = null;
			}
		}
		stopAbility();
	}
	
	public boolean canUse () {
		return canReuse;
	}
	
	public void setReusable () {
		abilityTimer = null;
		canReuse = true;
		setAbilityEnabled(true);
	}
	
	public boolean isMaster () {
		return master;
	}
	
	public boolean isRemoteUser () {
		return remoteUser;
	}
	
	public int getRegisteredKey () {
		return registeredKey;
	}
	
	protected abstract void startAbility ();
	
	protected abstract void stopAbility ();
	
	public abstract void handleAbilityCommand (AbilityCommand command);
}
