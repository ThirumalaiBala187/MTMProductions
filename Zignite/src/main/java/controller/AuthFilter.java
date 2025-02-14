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


        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.html"); 
            return;
        }

        String role = (String) session.getAttribute("role");


        String requestURI = req.getRequestURI();
        if (requestURI.contains("/admin") && !"admin".equals(role)) {
            res.getWriter().write("Access Denied: Admins only");
            return;
        }
        if (requestURI.contains("/user") && !"user".equals(role) && !"admin".equals(role)) {
            res.getWriter().write("Access Denied: Users only");
            return;
        }


        chain.doFilter(request, response);
    }

    public void destroy() {
       
    }
}
