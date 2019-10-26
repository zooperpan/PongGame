package pong.server;

public class UDPPort {

	private int port = 0;
	private boolean inUse = false;
	
	public UDPPort (int port) {
		this.port = port;
	}
	
	public int getPort () {
		return port;
	}
	
	public boolean isInUse () {
		return inUse;
	}
	
	public void setInUse (boolean inUse) {
		this.inUse = inUse;
	}
}
