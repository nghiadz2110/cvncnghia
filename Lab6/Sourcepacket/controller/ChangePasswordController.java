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
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/change_password")
public class ChangePasswordController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // AuthFilter ensures user is logged in.
        request.getRequestDispatcher("/views/change_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        // 1. Basic Validation
        if (newPassword == null || newPassword.length() < 8) {
            request.setAttribute("error", "New password must be at least 8 characters long.");
        } else if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirmation do not match.");
        } else {
            // 2. Verify current password
            if (BCrypt.checkpw(currentPassword, user.getPassword())) {
                // 3. Hash the new password
                String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                // 4. Update in Database
                if (userDAO.updatePassword(user.getId(), newHashedPassword)) {
                    // Update the User object in the session with the new hash (security/consistency)
                    user.setPassword(newHashedPassword);
                    request.getSession().setAttribute("user", user); 

                    request.setAttribute("message", "Password changed successfully!");
                } else {
                    request.setAttribute("error", "Error: Could not update password in the database.");
                }
            } else {
                request.setAttribute("error", "Incorrect current password.");
            }
        }
        
        // Forward back to the change password page with a message
        request.getRequestDispatcher("/views/change_password.jsp").forward(request, response);
    }
}