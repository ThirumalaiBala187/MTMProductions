package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthFilter implements Filter {

    private static final String[] EXCLUDED_PATHS = {
        "signin.html", "signup.html", "index.html",
        "/LoginServlet", "/LogoutServlet", "/SignupServlet"
    };

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String requestURI = req.getRequestURI();

        // 🛑 Prevent caching so logout works properly
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");

        // ✅ Allow public pages & static resources
        if (isExcluded(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 🚨 Block unauthorized users
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("Unauthorized access, redirecting to signin.html...");
            res.sendRedirect("signin.html");
            return;
        }

        // ✅ Allow request to continue
        chain.doFilter(request, response);
    }

    public void destroy() {}

    // 📌 Function to check if the request should be excluded from authentication
    private boolean isExcluded(String requestURI) {
        for (String path : EXCLUDED_PATHS) {
            if (requestURI.endsWith(path) || requestURI.contains(path)) {
                return true;
            }
        }
        // Allow static files (CSS, JS, images)
        return requestURI.matches(".*\\.(css|js|png|jpg|jpeg|gif)$");
    }
}
