package model.DTO;

public class User{
	
	public User(int userId, String userName, String email, int role_id) {
		UserId = userId;
		this.userName = userName;
		this.email = email;
		this.role_id = 2;
	}
	
	int UserId;
	String userName;
	String email;
	int role_id;
	
	
	
}