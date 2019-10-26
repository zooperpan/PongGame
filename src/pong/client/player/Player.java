package pong.client.player;

import pong.client.ability.Ability;
import pong.client.ability.AbilityFactory;
import pong.client.gui.GamePanel;
import pong.client.gui.pong.Bar;
import pong.com.command.AbilityCommand;

public class Player {

	private boolean master;
	private boolean remote;
	private Ability rightAbility;
	private Ability leftAbility;
	private Bar bar;
	
	public Player (Bar bar, GamePanel gamePanel, boolean master, boolean remote, String abilities[]) {
		this.bar = bar;
		this.master = master;
		this.remote = remote;
		this.leftAbility = AbilityFactory.getAbility(gamePanel, abilities[0], master, remote, Ability.LEFT_KEY);
		this.rightAbility = AbilityFactory.getAbility(gamePanel, abilities[1], master, remote, Ability.RIGHT_KEY);
	}
	
	public void useLeftAbility () {
		
		if (leftAbility != null) {
			if (bar.getHeight() > 0) {
				leftAbility.use();
			}
		}
	}
	
	public void useRightAbility () {
		if (rightAbility != null) {
			if (bar.getHeight() > 0) {
				rightAbility.use();
			}
		}
	}
	
	public void setAbilities (GamePanel gamePanel, String abilities[]) {
		this.leftAbility = AbilityFactory.getAbility(gamePanel, abilities[0], master, remote, Ability.LEFT_KEY);
		this.rightAbility = AbilityFactory.getAbility(gamePanel, abilities[1], master, remote, Ability.RIGHT_KEY);
	}
	
	public void handleAbilityCommand (AbilityCommand command) {
		if (command.getRegisteredKey() == Ability.LEFT_KEY) {
			leftAbility.handleAbilityCommand(command);
		} else {
			rightAbility.handleAbilityCommand(command);
		}
	}
	
	public void stopLeftAbility () {
		if (leftAbility != null) leftAbility.stop(false);
	}
	
	public void stopRightAbility () {
		if (rightAbility != null) rightAbility.stop(false);
	}
	
	public boolean isMaster () {
		return master;
	}
	
	public boolean isRemote () {
		return remote;
	}
	
	public void kill () {
		if (rightAbility != null) rightAbility.stop(true);
		if (leftAbility != null) leftAbility.stop(true);
	}
}
