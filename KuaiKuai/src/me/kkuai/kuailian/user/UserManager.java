package me.kkuai.kuailian.user;

public class UserManager {
	
	private final static UserManager instance = new UserManager();
	private static UserInfo currentUser;

	private UserManager() {
		
	}
	
	public static UserManager getInstance() {
		return instance;
	}
	
	public UserInfo getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(UserInfo currentUser) {
		UserManager.currentUser = currentUser;
	}
}
