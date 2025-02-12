
import model.DAO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        String email = jsonRequest.getString("email");
        String password = jsonRequest.getString("password");
        boolean isValidUser = validateUser(email, password);
        JSONObject jsonResponse = new JSONObject();
        if (isValidUser) {
        	
            HttpSession session = request.getSession(true); 
            session.setAttribute("user", email);
            JSONObject details=userDetails(email);
            session.setAttribute("details", details);
            String sessionId = session.getId();
            Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
            Cookie sessionCookie1 = new Cookie("DETAILS",details.toString());
            sessionCookie.setPath("/"); 
            sessionCookie.setMaxAge(60 * 60 * 24); 
            response.setContentType("application/json");
            response.addCookie(sessionCookie);
            response.addCookie(sessionCookie1);
            PrintWriter out = response.getWriter();
            out.write(jsonResponse.toString());
//            out.flush();
//            response.sendRedirect("https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#");
        } else {
            jsonResponse.put("success", false);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
    private boolean validateUser(String email, String password) {
    	try(Connection cn=Database.getConnection()){
    		String sql="select User_Id,Role_Id from Users where Email=? and Password=?";
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
    private JSONObject userDetails(String email) {
    	try(Connection cn=Database.getConnection()){
    		JSONArray jsonArray=new JSONArray();
    		String sql="select c.Course_Name,usp.XP,usp.StreakCount,usp.Levels_Completed from Users us inner join User_Progress usp on us.User_Id=usp.User_Id inner join Course c on c.Course_Id=usp.Course_Id where us.Email=?";
    		PreparedStatement st=cn.prepareStatement(sql);
    		st.setString(1, email);
    		ResultSet rs=st.executeQuery();
    		while(rs.next()) {
    		JSONObject newObj=new JSONObject();
    			newObj.put("course_name",rs.getString(1));
    			newObj.put("xp",rs.getString(2));
    			newObj.put("streakcount",rs.getString(3));
    			newObj.put("levels_completed",rs.getString(4));
    			jsonArray.put(newObj);
    		}
    		JSONObject userDetails=new JSONObject();
    		userDetails.put("Courses", jsonArray);
    		return userDetails;
    }
}
    

}

 
