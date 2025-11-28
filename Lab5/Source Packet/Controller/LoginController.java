/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author NGHIA
 */
import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
 
    private UserDAO userDAO;
 
    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
 
        // 1. Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Already logged in, redirect to dashboard
            response.sendRedirect("dashboard");
            return;
        }
 
        // 2. Forward to login.jsp
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
 
        // 1. Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
 
        // 2. Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Username and password are required!");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
 
        // 3. Authenticate user
        User user = userDAO.authenticate(username, password);
 
        if (user != null) {
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole()); // Dễ dàng cho việc kiểm tra role
            
            session.setMaxInactiveInterval(30 * 60); 
            
            // e. Redirect based on role
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard"); // Ví dụ: trang admin riêng
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard"); // Trang user/chung
            }
 
        } else {
            // Authentication failed
            request.setAttribute("error", "Username and password are wrong");
            // Giữ lại username để tiện cho người dùng
            request.setAttribute("username", username); 
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
