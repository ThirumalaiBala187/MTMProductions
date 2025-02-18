package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthFilter implements Filter {
    
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String requestURI = req.getRequestURI();

        if (requestURI.endsWith("index.html") || 
            requestURI.contains("/LoginServlet") || 
            requestURI.contains("/SignUp") || 
            requestURI.contains("/LogoutServlet") || 
            requestURI.endsWith(".css") || 
            requestURI.endsWith(".js") || 
            requestURI.endsWith(".png") || 
            requestURI.endsWith(".jpg") || 
            requestURI.endsWith(".jpeg") || 
            requestURI.endsWith(".gif")) {
            chain.doFilter(request, response);
            return;
        }

 
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("Redirecting to index.html...");
            res.sendRedirect("index.html");
            return;
        }

  
//        String role = (String) session.getAttribute("role");
//        if (requestURI.contains("/Admin") && !"Admin".equals(role)) {
//            res.getWriter().write("Access Denied: Admins only");
//            return;
//        }
//        if (requestURI.contains("/Customer") && !"Customer".equals(role) && !"Admin".equals(role)) {
//            res.getWriter().write("Access Denied: Users only");
//            return;
//        }

        chain.doFilter(request, response);
    }

    public void destroy() {
       
    }
}

