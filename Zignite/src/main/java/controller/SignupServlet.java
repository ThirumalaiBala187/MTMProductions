package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import model.DAO.Database;


public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
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
	    boolean validation = validation(email, password, firstName);
	    JSONObject jsonResponse = new JSONObject();

	    if (validation) {
	        HttpSession session = request.getSession(true);
	        String sessionId = session.getId();
	   
	        session.setAttribute("user", email);
	        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
	        Cookie sessionCookie1 = new Cookie("email", email);
	        sessionCookie.setPath("/");
	        sessionCookie.setMaxAge(60 * 60 * 24); 
	        sessionCookie1.setPath("/");
	        sessionCookie1.setMaxAge(60 * 60 * 24); 
	 
	        response.setContentType("application/json");
	        response.addCookie(sessionCookie);
	        response.addCookie(sessionCookie1);
	        jsonResponse.put("success", true);
	
	        PrintWriter out = response.getWriter();
	        SignupServlet.add_in_db(email, password, firstName, lastName, dob);
	        SignupServlet.course_Intialization(email);
	        out.write(jsonResponse.toString());
	        out.flush();
	    } else {
	        jsonResponse.put("failed", false);
	        PrintWriter out = response.getWriter();
	        out.print(jsonResponse.toString());
	        out.flush();
	    }
	}
	private static void course_Intialization(String email) {
		int user_id = 0;
		try {
			String query = "select User_Id,Email from Users;";
			PreparedStatement stmt = (PreparedStatement) Database.getConnection().prepareStatement(query);
			ResultSet result = ((java.sql.Statement) stmt).executeQuery(query);
			while (result.next()) {
				int id = result.getInt(1);
				String email1 = result.getString(2);
					if (email1 == email) {
						user_id = id;
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("mysql syntax error");
				e.printStackTrace();
			}
		if (user_id != 0) {
			try {
				Connection con=Database.getConnection();
				String query = "insert into User_Progress (Levels_Completed,StreakCount,XP,User_Id,Course_Id) values(0,0,0,1,1);";
				PreparedStatement stmt = con.prepareStatement(query);
				
				stmt.setInt(1, 0);
				stmt.setInt(2, 0);
				stmt.setInt(3, 0);
				stmt.setInt(4, user_id);
				stmt.setInt(5, 1);
				
				int resultSet = stmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("email is not exsist");
		}
		
	}

	private static void add_in_db(String email, String password, String firstName, String lastName, String dob) {
		try {
			
			Connection con=Database.getConnection();
			String query = "insert into Users(Name,Email,Password,Date_Of_Birth,SignUpTime,Role_Id) values(?,?,?,?,?,?);";
			PreparedStatement stmt = con.prepareStatement(query);
			System.out.print("inga");
			stmt.setString(1, firstName+" "+lastName);
			stmt.setString(2, email);
			stmt.setString(3, password);
			stmt.setDate(4, Date.valueOf(dob));
			stmt.setDate(5, Date.valueOf(LocalDate.now()));
			stmt.setInt(6, 2);
			
			int resultSet = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean validation(String email, String password, String firstName) {
	    boolean check = false;
	    if (email.contains("@gmail.com") && password.length() > 8 && !firstName.isEmpty()) {
	        check = true;
	        try {
	            String query = "SELECT Email FROM Users;";
	            PreparedStatement stmt = Database.getConnection().prepareStatement(query);
	            ResultSet result = stmt.executeQuery();
	            while (result.next()) {
	                String email1 = result.getString(1);
	                if (email.equals(email1)) {
	                    check = false;
	                    break;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return check;
	}
}
	
	
	