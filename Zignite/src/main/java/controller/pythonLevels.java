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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import model.DAO.Database;


/**
 * Servlet implementation class pythonLevels
 */

public class pythonLevels extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public pythonLevels() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    JSONObject jsonResponse = new JSONObject();

	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	         PrintWriter out = response.getWriter()) {
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }

	        JSONObject jsonRequest = new JSONObject(sb.toString());
	        int levelsCompleted = jsonRequest.optInt("level", 0);
	        int courseId = jsonRequest.optInt("courseId", 1);

	
	        Cookie[] cookies = request.getCookies();
	        String email = null;
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("user")) {
	                    email = cookie.getValue();
	                }
	            }
	        }

	        if (email == null || email.isEmpty()) {
	            jsonResponse.put("status", "error");
	            jsonResponse.put("message", "User email not found in cookies.");
	            out.print(jsonResponse.toString());
	            out.flush();
	            return;
	        }

	        updateCoursedata(email, courseId);

	   
	        HttpSession session = request.getSession(true);
	        JSONObject details = getUserDetails(email);
	        session.setAttribute("details", details);

	    
	        Cookie detailsCookie = new Cookie("DETAILS",
	                Base64.getEncoder().encodeToString(details.toString().getBytes(StandardCharsets.UTF_8)));
	        detailsCookie.setPath("/");
	        detailsCookie.setMaxAge(60 * 60 * 24);
	        response.addCookie(detailsCookie);

	  
	        jsonResponse.put("status", "success");
	        jsonResponse.put("message", "Level " + levelsCompleted + " recorded successfully.");
	        out.print(jsonResponse.toString());
	        out.flush();
	    } catch (Exception e) {
	        jsonResponse.put("status", "error");
	        jsonResponse.put("message", "Invalid request: " + e.getMessage());
	        response.getWriter().write(jsonResponse.toString());
	    }
	}

	private void updateCoursedata(String email, int course_id) throws SQLException {
		
		   try (Connection cn = Database.getConnection()) {
	            String sql = " UPDATE User_Progress SET Levels_Completed = Levels_Completed + 1,XP = XP + 100 WHERE Course_Id = ? AND User_Id = (SELECT User_Id FROM Users WHERE Email = ?)";
	            try (PreparedStatement st = cn.prepareStatement(sql)) {
	            	st.setInt(1, course_id);
	                st.setString(2, email);
	                st.executeUpdate();
	                }
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
	                    
	                    coursesArray.put(courseObj);
	                }
	            }
	        }
	    }

	    userDetails.put("courses", coursesArray);
	    return userDetails;
	}


}
