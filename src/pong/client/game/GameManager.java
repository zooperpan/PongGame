package pong.client.game;

public abstract class GameManager {

	public static final int CONTINUE		= 0;
	public static final int PALYER1_WINS	= 1;
	public static final int PALYER2_WINS	= 2;
	public static final int EXPLOSION		= 3;
	
	public GameManager () {
		
	}
	
	public abstract int checkResult ();
}
