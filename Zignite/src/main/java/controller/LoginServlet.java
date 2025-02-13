package controller;


import model.DAO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;


public class LoginServlet extends HttpServlet {
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        JSONObject jsonRequest = new JSONObject(sb.toString());
	        try {
	            String email = jsonRequest.getString("email");
	            String password = jsonRequest.getString("password");
	            boolean isValidUser = validateUser(email, password);
	            JSONObject jsonResponse = new JSONObject();
	            
	            if (isValidUser) {
	                HttpSession session = request.getSession(true);
	                session.setAttribute("user", email);
	                JSONObject details = userDetails(email);
	                session.setAttribute("details", details);
	                
	                String sessionId = session.getId();
	                Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
	                
	               
	                String encodedDetails = Base64.getEncoder().encodeToString(details.toString().getBytes(StandardCharsets.UTF_8));
	                Cookie detailsCookie = new Cookie("DETAILS", encodedDetails);
	                
	                sessionCookie.setPath("/");
	                sessionCookie.setMaxAge(60 * 60 * 24);
	                
	                detailsCookie.setPath("/");
	                detailsCookie.setMaxAge(60 * 60 * 24);
	                
	                response.setContentType("application/json");
	                response.addCookie(sessionCookie);
	                response.addCookie(detailsCookie);
	                System.out.println(encodedDetails);
	                jsonResponse.put("success", true);
	                PrintWriter out = response.getWriter();
	                out.write(jsonResponse.toString());
	            } else {
	                jsonResponse.put("success", false);
	                PrintWriter out = response.getWriter();
	                out.print(jsonResponse.toString());
	                out.flush();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private boolean validateUser(String email, String password) throws SQLException {
	        try (Connection cn = Database.getConnection()) {
	            String sql = "SELECT User_Id, Role_Id FROM Users WHERE Email = ? AND Password = ?";
	            PreparedStatement st = cn.prepareStatement(sql);
	            st.setString(1, email);
	            st.setString(2, password);
	            ResultSet rs = st.executeQuery();
	            return rs.next();
	        }
	    }

	    private JSONObject userDetails(String email) throws SQLException {
	        try (Connection cn = Database.getConnection()) {
	            JSONArray jsonArray = new JSONArray();
	            String sql = "SELECT c.Course_Name, usp.XP, usp.StreakCount, usp.Levels_Completed " +
	                         "FROM Users us " +
	                         "INNER JOIN User_Progress usp ON us.User_Id = usp.User_Id " +
	                         "INNER JOIN Course c ON c.Course_Id = usp.Course_Id " +
	                         "WHERE us.Email = ?";
	            PreparedStatement st = cn.prepareStatement(sql);
	            st.setString(1, email);
	            ResultSet rs = st.executeQuery();
	            while (rs.next()) {
	                JSONObject newObj = new JSONObject();
	                newObj.put("course_name", rs.getString(1));
	                newObj.put("xp", rs.getString(2));
	                newObj.put("streakcount", rs.getString(3));
	                newObj.put("levels_completed", rs.getString(4));
	                jsonArray.put(newObj);
	            }
	            JSONObject userDetails = new JSONObject();
	            userDetails.put("courses", jsonArray);
	            return userDetails;
	        }
	    }
	}



