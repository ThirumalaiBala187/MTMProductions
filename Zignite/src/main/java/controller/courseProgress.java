package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import model.DAO.Database;

public class courseProgress extends HttpServlet{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
try(		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		 PrintWriter out = response.getWriter() ){

	     StringBuilder sb = new StringBuilder();
	     String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	     JSONObject jsonRequest = new JSONObject(sb.toString());
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
	   
		 
//	     int userid = 0;
//	     if (email != "") {
//	    	 try {
//	    		 String query = "Select User_Id from Users where Email = ? ;";
//	 			PreparedStatement stmt = (PreparedStatement) Database.getConnection().prepareStatement(query);
//	 			stmt.setString(1, email);
//	 			ResultSet result = ((java.sql.Statement) stmt).executeQuery(query);
//	 			userid = result.getInt(1);  
//	    	 }
//	    	 catch (SQLException e) {
//	 			e.printStackTrace();
//	 		}
//	     }
	     initializeUserCourse(email,courseId);
	     HttpSession session = request.getSession(false);
	     JSONObject jsonResponse = new JSONObject();
	     response.setContentType("application/json");
	     JSONObject details = getUserDetails(email);
         session.setAttribute("details", details);
         Cookie detailsCookie = new Cookie("DETAILS",
                 Base64.getEncoder().encodeToString(details.toString().getBytes(StandardCharsets.UTF_8)));
         detailsCookie.setPath("/");
         detailsCookie.setMaxAge(60 * 60 * 24);
         response.addCookie(detailsCookie);
         jsonResponse.put("success", true);
       
         out.print(jsonResponse.toString());
	}
   catch(Exception e) {
	System.out.println(e.getLocalizedMessage());
    }
	}
	private static JSONObject getUserDetails(String email) throws SQLException {
	    JSONObject userDetails = new JSONObject();
	    JSONArray coursesArray = new JSONArray();

	    try (Connection cn = Database.getConnection()) {
	        String sql = "SELECT c.Course_Name, cl.LevelName, usp.XP, usp.Levels_Completed " +
	                     "FROM Users us " +
	                     "INNER JOIN User_Progress usp ON us.User_Id = usp.User_Id " +
	                     "INNER JOIN Course c ON c.Course_Id = usp.Course_Id " +
	                     "LEFT JOIN Course_Levels cl ON c.Course_Id = cl.Course_Id AND usp.Levels_Completed + 1 = cl.Level_Id " +
	                     "WHERE us.Email = ?";
	        
	        try (PreparedStatement st = cn.prepareStatement(sql)) {
	            st.setString(1, email);
	            try (ResultSet rs = st.executeQuery()) {
	                while (rs.next()) {
	                    JSONObject courseObj = new JSONObject();
	                    courseObj.put("course_name", rs.getString("Course_Name"));
	                    courseObj.put("xp", rs.getInt("XP"));
//	                    courseObj.put("streakcount", rs.getInt("StreakCount"));
	                    courseObj.put("levels_completed", rs.getInt("Levels_Completed"));
	                    
	                    String levelName = rs.getString("LevelName");
	                    if (levelName != null) {
	                        courseObj.put("level_name", levelName);
	                    }
	                    else {
	                    	 courseObj.put("level_name", "Completed !");
	                    }
	                    
	                    coursesArray.put(courseObj);
	                }
	            }
	        }
	    }

	    userDetails.put("courses", coursesArray);
	    return userDetails;
	}
	private static void initializeUserCourse(String email, int courseId) {
	    int userId = 0;
      System.out.println("initializing!!");
	    String selectQuery = "SELECT User_Id FROM Users WHERE Email = ?";
	    String insertQuery = "INSERT INTO User_Progress (Levels_Completed, XP, User_Id, Course_Id) VALUES (0, 0, ?, ?)";

	    try (Connection con = Database.getConnection();
	         PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {

	        selectStmt.setString(1, email);
	        try (ResultSet result = selectStmt.executeQuery()) {
	            if (result.next()) {
	                userId = result.getInt(1);
	            }
	        }

	        System.out.println("User ID: " + userId);

	        if (userId != 0) {
	        	System.out.println("userID Check validates");
	            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
	                insertStmt.setInt(1, userId);
	                insertStmt.setInt(2, courseId);
	                insertStmt.executeUpdate();
	                System.out.println("User course initialized successfully.");
	            }
	        } else {
	            System.out.println("Email does not exist.");
	        }

	    } catch (SQLException e) {
	        System.err.println("SQL Error: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


}
