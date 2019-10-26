package pong.client.ability;

public interface AbilityThread {

	/**
	 * This method should be called when the ability must update the objects that it handles.
	 * @return true if the ability should be stopped, false otherwise.
	 */
	public boolean run ();
	
	/**
	 * Stops the ability. Any objects that are handled by the ability, will not be updated anymore.
	 */
	public void stop();
}
