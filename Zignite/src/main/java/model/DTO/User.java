package model.DTO;

public class User{
	

	int UserId;
	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	String userName;
	String email;
	int role_id;
	
	public User(int userId, String userName, String email, int role_id) {
		UserId = userId;
		this.userName = userName;
		this.email = email;
		this.role_id = 2;
	}
	
	
}