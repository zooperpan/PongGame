package pong.client.ability;

import java.util.ArrayList;

public interface AbilityFeature {

	public ArrayList<String> getActiveAbilities();
	
	public void addActiveAbility(String ability);
	
	public void removeActiveAbility(String ability);
	
	public boolean hasAbility (String ability);
}
