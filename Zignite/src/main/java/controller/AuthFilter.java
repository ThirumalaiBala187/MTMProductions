package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthFilter implements Filter {

    private static final String[] EXCLUDED_PATHS = {
        "signin.html", "/LoginServlet", "signup.html", "/LogoutServlet","index.html","entrance.html",
        ".css", ".js", ".png", ".jpg", ".jpeg", ".gif","/SignupServlet"
    };

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String requestURI = req.getRequestURI();


        for (String path : EXCLUDED_PATHS) {
            if (requestURI.contains(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

//        System.out.println(session);
//
        if (session == null || session.getAttribute("user") == null) {
        	res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            System.out.println("Redirecting to index.html...");
            res.sendRedirect("signin.html");
            return;
        }

 
        /*
        String role = (String) session.getAttribute("role");
        if (requestURI.contains("/Admin") && !"Admin".equals(role)) {
            res.getWriter().write("Access Denied: Admins only");
            return;
        }
        if (requestURI.contains("/Customer") && !"Customer".equals(role) && !"Admin".equals(role)) {
            res.getWriter().write("Access Denied: Users only");
            return;
        }
        */

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}

