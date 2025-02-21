package controller;

import java.io.BufferedReader;	
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import model.DAO.Database;

public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignupServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	     StringBuilder sb = new StringBuilder();
	     String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	     JSONObject jsonRequest = new JSONObject(sb.toString());

    	  String email = jsonRequest.getString("email");
          String password = jsonRequest.getString("password");
          String firstName = jsonRequest.getString("firstName");
          String lastName = jsonRequest.getString("lastName");
          String dob = jsonRequest.getString("dob");

          boolean validation = validateUser(email, password, firstName);
          JSONObject jsonResponse = new JSONObject();

          if (validation) {
        	  try {
        	  HttpSession session = request.getSession(true);
        	  String sessionId = session.getId();
        	  session.setAttribute("user", email);
        	  Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
              Cookie sessionCookie1 = new Cookie("user", email);
              session.setAttribute("role", 2);
              sessionCookie.setPath("/"); 
              sessionCookie.setMaxAge(60 * 60 * 24); 
              sessionCookie1.setPath("/"); 
              sessionCookie1.setMaxAge(60 * 60 * 24); 
              ////////////
//              JSONObject details = getUserDetails(email);
//              session.setAttribute("details", details);
              String userName;
			
//				userName = getUserName(email);
			              
//              Cookie detailsCookie = new Cookie("DETAILS",
//                      Base64.getEncoder().encodeToString(details.toString().getBytes(StandardCharsets.UTF_8)));
              Cookie nameCookie = new Cookie("name",
                      Base64.getEncoder().encodeToString((firstName+" "+lastName).getBytes(StandardCharsets.UTF_8)));
              sessionCookie.setPath("/");
              sessionCookie.setMaxAge(60 * 60 * 24);
              nameCookie.setPath("/");
              nameCookie.setMaxAge(60 * 60 * 24);
//              detailsCookie.setPath("/");
//              detailsCookie.setMaxAge(60 * 60 * 24);
//              
//              response.addCookie(detailsCookie);
              response.addCookie(nameCookie);
              
        
              ///////////
              response.setContentType("application/json");
              response.addCookie(sessionCookie);
              response.addCookie(sessionCookie1);
              jsonResponse.put("success", true);
              PrintWriter out = response.getWriter();
              out.write(jsonResponse.toString());

              addUserToDB(email, password, firstName, lastName, dob);
              initializeUserCourse(email);
        	  }
             catch(Exception e) {
            	  System.out.println(e.getMessage());
              }
          }
        	  else {
              jsonResponse.put("failed", false);
              PrintWriter out = response.getWriter();
              out.print(jsonResponse.toString());
              out.flush();
          }
       
          }
        
	

	private static void initializeUserCourse(String email) {
		int user_id = 0;
		try {
			Connection con = Database.getConnection();
			String query = "SELECT User_Id FROM Users WHERE Email = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, email);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				user_id = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("SQL error in retrieving user ID");
			e.printStackTrace();
		}
		System.out.println(user_id);
		if (user_id != 0) {
			try {
				for(int i=1;i<=2;i++) {
				Connection con = Database.getConnection();
				String query = "INSERT INTO User_Progress (Levels_Completed, StreakCount, XP, User_Id, Course_Id) VALUES (0, 0, 0, ?, ?)";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setInt(1, user_id);
				stmt.setInt(2, i);;
				stmt.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("Email does not exist");
		}
	}

	private static void addUserToDB(String email, String password, String firstName, String lastName, String dob) {
		try {
			System.out.println("nammadhan");
			Connection con = Database.getConnection();
			String query = "INSERT INTO Users (Name, Email, Password, Date_Of_Birth, SignUpTime, Role_Id, Salt) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(query);
			String salt = generateSalt();
			String hashedPassword = hashPasswordWithSalt(password, salt);

			stmt.setString(1, firstName + " " + lastName);
			stmt.setString(2, email);
			stmt.setString(3, hashedPassword);
			stmt.setDate(4, Date.valueOf(dob));
			stmt.setDate(5, Date.valueOf(LocalDate.now()));
			stmt.setInt(6, 2);
			stmt.setString(7, salt);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean validateUser(String email, String password, String firstName) {
		System.out.println("nammadhan1");
		boolean isValid = email.contains("@gmail.com") && password.length() > 8 && !firstName.isEmpty();
		if (isValid) {
			try {
				System.out.println("nammadhan2");
				Connection con = Database.getConnection();
				String query = "SELECT Email FROM Users WHERE Email = ?";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, email);
				ResultSet result = stmt.executeQuery();
				if (result.next()) {
					isValid = false; 
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isValid;
	}

		private static String generateSalt() {
	    SecureRandom random = new SecureRandom();
	    byte[] salt = new byte[16];
	    random.nextBytes(salt);
	    return Base64.getEncoder().encodeToString(salt);
	}


	private static String hashPasswordWithSalt(String password, String salt) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(salt.getBytes());  
	        byte[] hashedBytes = md.digest(password.getBytes());
	        return Base64.getEncoder().encodeToString(hashedBytes);
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Error hashing password", e);
	    }
	}
    private JSONObject getUserDetails(String email) throws SQLException {
        JSONObject userDetails = new JSONObject();
        JSONArray coursesArray = new JSONArray();

        try (Connection cn = Database.getConnection()) {
            String sql = "SELECT c.Course_Name, cl.LevelName, usp.XP, usp.StreakCount, usp.Levels_Completed " +
                    "FROM Users us " +
                    "INNER JOIN User_Progress usp ON us.User_Id = usp.User_Id " +
                    "INNER JOIN Course c ON c.Course_Id = usp.Course_Id " +
                    "INNER JOIN Course_Levels cl ON c.Course_Id = cl.Course_Id AND usp.Levels_Completed + 1 = cl.Level_Id " +
                    "WHERE us.Email = ?";
            try (PreparedStatement st = cn.prepareStatement(sql)) {
                st.setString(1, email);
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        JSONObject courseObj = new JSONObject();
                        courseObj.put("course_name", rs.getString("Course_Name"));
                        courseObj.put("level_name", rs.getString("LevelName"));
                        courseObj.put("xp", rs.getInt("XP"));
                        courseObj.put("streakcount", rs.getInt("StreakCount"));
                        courseObj.put("levels_completed", rs.getInt("Levels_Completed"));
                        coursesArray.put(courseObj);
                    }
                }
            }
        }

        userDetails.put("courses", coursesArray);
        return userDetails;
    }
    private String getUserName(String email) throws SQLException {
        try (Connection cn = Database.getConnection()) {
            String sql = "SELECT Name FROM Users WHERE Email = ?";
            try (PreparedStatement st = cn.prepareStatement(sql)) {
                st.setString(1, email);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("Name");
                    }
                }
            }
        }
        return null;
    }

}

	
