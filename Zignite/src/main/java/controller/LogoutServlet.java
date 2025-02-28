package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
        	System.out.println("logging out!");
            session.invalidate();
        }

            Cookie sessionCookie1 = new Cookie("JSESSIONID", "");
            sessionCookie1.setMaxAge(0);
            sessionCookie1.setPath("/Zignite"); 
            response.addCookie(sessionCookie1);

            Cookie sessionCookie2 = new Cookie("JSESSIONID", "");
            sessionCookie2.setMaxAge(0);
            sessionCookie2.setPath("/"); 
            response.addCookie(sessionCookie2);

            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            response.addCookie(sessionCookie1);
            response.addCookie(sessionCookie2);
      
        response.getWriter().write("Logged out successfully");
//        response.sendRedirect("/index.html");
    }
}