package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.DAO.Database;

public class UserProgress {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	     StringBuilder sb = new StringBuilder();
	     String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	     JSONObject jsonRequest = new JSONObject(sb.toString());
	     
	     int LevelCompleted = jsonRequest.getInt("LevelsCompleted");
         int streakCount = jsonRequest.getInt("streakcount");
         int xp = jsonRequest.getInt("xp");
         int courseId = jsonRequest.getInt("courseId");
         
	     String email = "";
	     
	     Cookie[] cookies = request.getCookies();
	     if (cookies != null) {
	         for (Cookie cookie : cookies) {
	             if (cookie.getName().equals("user")) { 
	            	 email = cookie.getValue();
	             }
	         }
	     }
	     
	     int userid = 0;
	     if (email != "") {
	    	 try {
	    		 String query = "Select User_Id from Users where Email = ? ;";
	 			PreparedStatement stmt = (PreparedStatement) Database.getConnection().prepareStatement(query);
	 			stmt.setString(1, email);
	 			ResultSet result = ((java.sql.Statement) stmt).executeQuery(query);
	 			userid = result.getInt(1);  
	    	 }
	    	 catch (SQLException e) {
	 			e.printStackTrace();
	 		}
	     }
	     UserProgress.addInDb(LevelCompleted,streakCount,xp,courseId,userid);
	}
//	     int courseId = 0;
//	     try {
//    		 String query = "select Course_ID from Course_Levels where Level_Id = ?;";
// 			PreparedStatement stmt = (PreparedStatement) Database.getConnection().prepareStatement(query);
// 			stmt.setInt(1, LevelCompleted);
// 			ResultSet result = ((java.sql.Statement) stmt).executeQuery(query);
// 			courseId = result.getInt(1);  
//    	 }
//    	 catch (SQLException e) {
// 			e.printStackTrace();
// 		}
	     
	    
	     
	private static void addInDb(int levelCompleted, int streakCount, int xp, int courseId,int userid) {
		int courseid = 0;
		try {
			Connection con=Database.getConnection();
			String query = "Select Course_Id from Users where User_Id = ? ;";
			PreparedStatement stmt = (PreparedStatement) Database.getConnection().prepareStatement(query);
 			stmt.setInt(1, userid);
 			ResultSet result = ((java.sql.Statement) stmt).executeQuery(query);
 			if (result.next()) {
 				courseid = result.getInt(1); 
 			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (userid != 0 && courseid != 0) {
			try {
				Connection con=Database.getConnection();
				String query = "update User_Progress set Levels_Completed = ? , StreakCount = ?,XP = XP + ? where User_Id = ?;";
				PreparedStatement stmt = con.prepareStatement(query);
				
				stmt.setInt(1, levelCompleted);
				stmt.setInt(2, streakCount);
				stmt.setInt(3, xp);
				stmt.setInt(4, userid);
				
				int resultSet = stmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    else {
	    	try {
				Connection con=Database.getConnection();
				String query = "insert into User_Progress (Levels_Completed,StreakCount,XP,User_Id,Course_Id) Values(?,?,?,?,?);";
				PreparedStatement stmt = con.prepareStatement(query);
				
				stmt.setInt(1, levelCompleted);
				stmt.setInt(2, streakCount);
				stmt.setInt(3, xp);
				stmt.setInt(4, userid);
				stmt.setInt(5, courseId);
				
				int resultSet = stmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}
}
