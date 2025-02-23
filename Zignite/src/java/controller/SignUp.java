import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Servlet implementation class SignUp
 */

public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
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
        boolean check = false;
        
        if (email.contains("@gmail.com") && firstName.length() > 0 && password.length() > 6) {
        	check = true;
        }
        
        if (check) {
        try {
			String Query = "insert into Users (Name,Email,Password,Date_Of_Birth,SignUpTime,Role_Id) values(?,?,?,?,?,?);";
			PreparedStatement stmt = Database.getConnection().prepareStatement(Query);
			
			stmt.setString(1,email);
			stmt.setString(2, password);
			stmt.setString(3,firstName);
			stmt.setString(4, lastName);
			stmt.setDate(5,Date.valueOf(dob));
			
			int resultSet = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("mysql syntax error");
			e.printStackTrace();
		}
        
        JSONObject jsonResponse = new JSONObject();
        HttpSession session = request.getSession(true); 
        session.setAttribute("user", email);
        String sessionId = session.getId();
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setPath("/"); 
        sessionCookie.setMaxAge(60 * 60 * 12); 
        response.addCookie(sessionCookie);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        response.sendRedirect("https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#");    
        
        }
        else {
            jsonResponse.put("success", false);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
        }
		doGet(request, response);
	}
	
}

?