package pong.client.motion;

import java.util.ArrayList;

import pong.client.game.GameManager;
import pong.client.gui.GamePanel;
import pong.client.gui.pong.MovingObject;

public class MotionUpdater {

	private ArrayList<MotionManager> motionManagers	= new ArrayList<MotionManager>();
	private GameManager gm							= null;
	private GamePanel gamePanel						= null;

	public MotionUpdater (GameManager gm, GamePanel gamePanel) {
		this.gm = gm;
		this.gamePanel = gamePanel;
	}

	public void update () {
		if (motionManagers.size() > 0) {
			for (int pos = 0; pos < motionManagers.size(); pos++) {
				MotionManager motionManager = motionManagers.get(pos);
				if (motionManager.getBounceManager() != null) {
					motionManager.getBounceManager().checkBounce(motionManager.getMovingObject());
				}
				motionManager.update();
			}
			int result;
			if (gm != null) {
				gm.checkResult();
				if ((result = gm.checkResult()) != GameManager.CONTINUE) {
					gamePanel.setWinner(result);
					for (int pos = 0; pos < motionManagers.size(); pos++) {
						MotionManager motionManager = motionManagers.get(pos);
						motionManager.reset();
					}
				}
			}
		}
	}
	
	public void addMotionManager (MotionManager motionManager) {
		motionManagers.add(motionManager);
	}
	
	public void removeMotionManager (MotionManager motionManager) {
		motionManagers.remove(motionManager);
	}
	
	public MovingObject getMovingObject (String name) {
		for (int i = 0; i < motionManagers.size(); i++) {
			if (motionManagers.get(i).getMovingObject().getName().equals(name)) {
				return motionManagers.get(i).getMovingObject();
			}
		}
		return null;
	}
}
