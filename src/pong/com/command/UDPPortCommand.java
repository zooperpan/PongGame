package pong.com.command;

public class UDPPortCommand extends UserCommand {

	private static final long serialVersionUID = 395867276L;
	private int port;
	
	public UDPPortCommand (int port) {
		super(Command.UDP_PORT);
		this.port = port;
	}
	
	public int getPort () {
		return port;
	}
}
