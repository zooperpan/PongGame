package pong.com;

public class ActiveField {

	private String name;
	private String user1Name = null;
	private String user2Name = null;
	private boolean opened;
	private int numOfUsers = 0;
	
	public ActiveField (String name) {
		opened = true;
		this.name = name;
	}
	
	public void addUser (String userName) {
		if (opened) {
			if (numOfUsers == 0) {
				user1Name = userName;
				numOfUsers = 1;
			} else {
				user2Name = userName;
				numOfUsers = 2;
				opened = false;
			}
		}
	}
	
	public void removeUser (String userName) {
		if (opened) {
			// Only one user connected
			user1Name = null;
			numOfUsers = 0;
			opened = false;
		} else {
			if (userName.equals(user1Name)) {
				// 1st user removed
				user1Name = user2Name;
				user2Name = null;
				numOfUsers = 1;
			} else if (userName.equals(user2Name)) {
				// 2nd user removed
				user2Name = null;
				numOfUsers = 1;
			}
			opened = true;
		}
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

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isOpened() {
		return opened;
	}
	
	public String getName () {
		return name;
	}
}
