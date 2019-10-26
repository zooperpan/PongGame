package pong.server.user;



public class CheckConnection implements Runnable {

	private Thread t 				= null;
	private UserServerThread ust 	= null;
	private boolean isPongGame 		= false;
	
	public CheckConnection (UserServerThread ust) {
		this.ust = ust;
		t = new Thread(this);
	}
	
	/**
	 * Main execution method.
	 */
	public void run() {
		
		for (int i = 0; i < 100; i++) {
			try {
	    		Thread.sleep(50);
	    	} catch ( InterruptedException ex) {}
	    	if (ust.getConnectionStatus()) {
	    		// If the connection has been established, do not close the 
	    		// connection.
	    		isPongGame = true;
	    		break;
	    	}
		}
		if (!isPongGame) {
			// If the connection has not been established within 5sec, kill 
			// the UserServerThread.
			ust.closeConnection();
		}
	}
	
	/**
	 * This method causes the thread to begin execution.
	 */
	void start () {
		t.start();
	}
}
