package pong.client.ability;

import pong.client.gui.GamePanel;

public class AbilityFactory {

	public static Ability getAbility (GamePanel gamePanel, String abilitiyName, boolean master, boolean remoteUser, int registeredKey) {
		
		if (abilitiyName.equals("fat")) {
			return new FatAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("magnet")) {
			return new MagnetAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("pistol")) {
			return new PistolAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("bomb")) {
			return new BombAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("antibomb")) {
			return new AntiBombAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("target")) {
			return new TargetAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("decoy")) {
			return new AntiMissileAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("triple")) {
			return new TripleAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else if (abilitiyName.equals("vision")) {
			return new VisionAbility(gamePanel, abilitiyName, master, remoteUser, registeredKey);
		} else return null;
	}
}
