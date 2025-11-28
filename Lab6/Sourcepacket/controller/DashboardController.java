/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author NGHIA
 */


import dao.StudentDAO; // Giả sử StudentDAO đã tồn tại
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
 
    private StudentDAO studentDAO; // (1) ĐÃ BỎ COMMENT VÀ KHAI BÁO
 
    @Override
    public void init() {
        studentDAO = new StudentDAO(); // (2) ĐÃ BỎ COMMENT VÀ KHỞI TẠO
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
 
        // Lấy User từ session (đã được AuthFilter kiểm tra)
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
 
        if (user == null) {
            response.sendRedirect("login"); // Lỗi hoặc session hết hạn
            return;
        }
        
        int totalStudents = 0;
        try {
            totalStudents = studentDAO.countStudents(); 
        } catch (Exception e) {
            System.err.println("Error fetching total students: " + e.getMessage());
            request.setAttribute("error", "Cannot load statistics: Database error.");
        }
        
        // Set attributes
        request.setAttribute("totalStudents", totalStudents);
        
        // Forward to dashboard.jsp
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}