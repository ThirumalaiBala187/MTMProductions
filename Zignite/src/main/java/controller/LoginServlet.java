
import model.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

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
        String email = jsonRequest.getString("email");
        String password = jsonRequest.getString("password");
        boolean isValidUser = validateUser(email, password);
        JSONObject jsonResponse = new JSONObject();
        if (isValidUser) {
            HttpSession session = request.getSession(true); 
            session.setAttribute("user", email);
            String sessionId = session.getId();
            Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
            sessionCookie.setPath("/"); 
            sessionCookie.setMaxAge(60 * 60 * 24); 
            response.setContentType("application/json");
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
    private boolean validateUser(String email, String password){
    	try(Connection cn=Database.getConnection()){
    		String sql="select User_Id, from Users where Name=? and Password=?";
    		PreparedStatement st=cn.prepareStatement(sql);
    		st.setString(1, email);
    		st.setString(2, password);
    		ResultSet rs=st.executeQuery();
    		if(rs.next()) { 
            return true;
    		}
    		else {
    			return false;
    		}
    }
}

}

 
