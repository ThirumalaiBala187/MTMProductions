import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import assignment010.Connectionclass;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
        
        JSONObject jsonResponse = new JSONObject();
        if (isValidUser) {
            HttpSession session = request.getSession(true); 
            session.setAttribute("user", email);
            String sessionId = session.getId();
            Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
            sessionCookie.setPath("/"); 
            sessionCookie.setMaxAge(60 * 60 * 12); 
            response.addCookie(sessionCookie);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
//            response.sendRedirect("https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#");
        } 
        else {
            jsonResponse.put("success", false);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
		doGet(request, response);
		
	}
	
	
}
