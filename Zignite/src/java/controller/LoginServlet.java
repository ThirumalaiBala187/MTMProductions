
import model.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

import assignment010.Connectionclass;

import java.io.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
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
        
        boolean isValidUser = validateUser(email, password);
        JSONObject jsonResponse = new JSONObject();
        if (isValidUser) {
            HttpSession session = request.getSession(true); 
            session.setAttribute("user", email);
            String sessionId = session.getId();
            Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
            sessionCookie.setPath("/"); 
            sessionCookie.setMaxAge(60 * 60 * 24); 
            response.addCookie(sessionCookie);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
//            out.flush();
//            response.sendRedirect("https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#");
        } else {
            jsonResponse.put("success", false);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
    try {
		String Query = "insert into Hotelbooking (cname,rno,check_in_date,check_out_date,total_price) values(?,?,?,?,?);";
		PreparedStatement stmt = Connectionclass.getConnection().prepareStatement(Query);
		
		stmt.setString(1,c.c_name);
		stmt.setInt(2, c.bid);
		stmt.setString(3,c.check_in_date+"");
		stmt.setString(4, c.check_out_date+"");
		stmt.setInt(5, c.total_price);
		
		int resultSet = stmt.executeUpdate();
		
	} catch (SQLException e) {
		System.out.println("mysql syntax error");
		e.printStackTrace();
	}
}