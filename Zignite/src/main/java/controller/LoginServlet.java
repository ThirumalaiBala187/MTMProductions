package controller;
import model.DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonResponse = new JSONObject();
        response.setContentType("application/json");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
             PrintWriter out = response.getWriter()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonRequest = new JSONObject(sb.toString());
            String email = jsonRequest.optString("email", "").trim();
            String password = jsonRequest.optString("password", "").trim();

            if (email.isEmpty() || password.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Email and password are required.");
                out.print(jsonResponse.toString());
                return;
            }

            String role = validateUser(email, password);
            boolean isValidUser = role != null;

            if (isValidUser) {
                String userName = getUserName(email);
                if (request.getSession(false) != null) {
                    request.getSession().invalidate();
                }
                HttpSession session = request.getSession(true);
                session.setAttribute("user", email);
                session.setAttribute("role", role);

                JSONObject details = getUserDetails(email);
                session.setAttribute("details", details);

                Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
                Cookie detailsCookie = new Cookie("DETAILS",
                        Base64.getEncoder().encodeToString(details.toString().getBytes(StandardCharsets.UTF_8)));
                Cookie nameCookie = new Cookie("name",
                        Base64.getEncoder().encodeToString(userName.getBytes(StandardCharsets.UTF_8)));

                sessionCookie.setPath("/");
                sessionCookie.setMaxAge(60 * 60 * 24);
                nameCookie.setPath("/");
                nameCookie.setMaxAge(60 * 60 * 24);
                detailsCookie.setPath("/");
                detailsCookie.setMaxAge(60 * 60 * 24);

                response.addCookie(sessionCookie);
                response.addCookie(detailsCookie);
                response.addCookie(nameCookie);

                jsonResponse.put("success", true);
                jsonResponse.put("role", role);
                out.print(jsonResponse.toString());

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid credentials.");
                out.print(jsonResponse.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Internal server error.");
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
            }
        }
    }

    private String validateUser(String email, String password) throws SQLException {
        try (Connection cn = Database.getConnection()) {
            String sql = "SELECT Password, Salt, Role_Id FROM Users WHERE Email = ?";
            try (PreparedStatement st = cn.prepareStatement(sql)) {
                st.setString(1, email);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("Password");
                        String storedSalt = rs.getString("Salt");
                        String role = rs.getString("Role_Id");

                   
                        String hashedInputPassword = hashPasswordWithSalt(password, storedSalt);

                        if (storedHash.equals(hashedInputPassword)) {
                            return role; 
                        }
                    }
                }
            }
        }
        return null;
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
}


