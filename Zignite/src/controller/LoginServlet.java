
import model.Database;

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

public class LoginServlet extends HttpServlet{
protected void	doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
	String email=request.getParameter("email");
	String password=request.getParameter("password");
	System.out.print(false);
	try(Connection cn=Database.getConnection()){
		String sql="select user_id from users where username=? and password=?";
		PreparedStatement st=cn.prepareStatement(sql);
		st.setString(1, email);
		st.setString(2, password);
		ResultSet rs=st.executeQuery();
		if(rs.next()) {
			int user_id=rs.getInt(1);
			
			HttpSession session=request.getSession();
			session.setAttribute("user_id", user_id);
			session.setAttribute("email",email);
			
			Cookie cookie=new Cookie("email",email);
			cookie.setMaxAge(30*60);
			response.addCookie(cookie);
			
			response.sendRedirect("https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#");
		response.getWriter().write();
		}
		else {
			response.sendRedirect("");
				response.getWriter().write();
		}
	}
	catch(Exception e ) {
		response.sendRedirect("");
	}
}
}