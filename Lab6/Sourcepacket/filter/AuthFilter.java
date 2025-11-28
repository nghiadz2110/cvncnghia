/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

/**
 *
 * @author NGHIA
 */
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    // Public URLs that DO NOT require authentication
    private static final List<String> PUBLIC_URL_PREFIXES = Arrays.asList(
        "/login", 
        "/logout", 
        "/resources/", // CSS/JS/Images
        "/views/login.jsp" 
    );
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String path = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        // 1. Check if the requested path is a public resource
        boolean isPublicResource = false;
        for (String urlPrefix : PUBLIC_URL_PREFIXES) {
            if (path.startsWith(urlPrefix)) {
                isPublicResource = true;
                break;
            }
        }
        
        // 2. If it's public OR the user is logged in, allow access
        if (isPublicResource || user != null) {
            chain.doFilter(request, response);
            return;
        }
 
        // 3. If it's a protected path AND the user is not logged in, redirect to login
        res.sendRedirect(req.getContextPath() + "/login?error=You must log in to access this page.");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}