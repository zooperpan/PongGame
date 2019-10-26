package pong.com;

import java.io.Serializable;

public class FieldCharacteristics implements Serializable {

	private static final long serialVersionUID = 395867276L;
	private boolean opened;
	private String name;
	private String user1Name;
	private String user2Name;
	
	public FieldCharacteristics (boolean opened, String name, String user1Name, String user2Name) {
		this.setOpened(opened);
		this.setName(name);
		this.setUser1Name(user1Name);
		this.setUser2Name(user2Name);
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}

	public String getUser1Name() {
		return user1Name;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	public String getUser2Name() {
		return user2Name;
	}
}
