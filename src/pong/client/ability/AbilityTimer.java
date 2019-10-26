package pong.client.ability;

public class AbilityTimer extends Thread {

	private Ability ability;
	private int time;
	private boolean isRunning = true;
	
	public AbilityTimer (int time, Ability ability) {
		super();
		this.ability = ability;
		this.time = time;
	}
	
	public void run () {
		int counter = time;
		try {
			while (isRunning && (counter-- > 0)) Thread.sleep(1000);
		} catch (InterruptedException e) {}
		ability.setReusable();
	}
	
	public void kill () {
		isRunning = false;
		this.interrupt();
	}
}
