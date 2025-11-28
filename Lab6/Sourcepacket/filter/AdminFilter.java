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

// This filter applies to all paths starting with /admin/
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        // AuthFilter should ensure the session is valid, but we check role here
        User user = (session != null) ? (User) session.getAttribute("user") : null;
 
        // 1. If user is null (session expired/invalid), redirect to login
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=Session expired. Please log in again.");
            return;
        }
        
        // 2. Check the user's role
        if (user.isAdmin()) {
            // If Admin, allow access
            chain.doFilter(request, response);
        } else {
            // If not Admin, redirect to the general dashboard with an error
            res.sendRedirect(req.getContextPath() + "/dashboard?error=Access Denied. (Admin only)");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}