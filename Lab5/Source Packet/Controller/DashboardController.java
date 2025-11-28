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
 
    // private StudentDAO studentDAO; // Khai báo
 
    @Override
    public void init() {
        // studentDAO = new StudentDAO(); // Khởi tạo
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
        
        // TODO: Get statistics (ví dụ: total students)
        // int totalStudents = studentDAO.countStudents();
        int totalStudents = 150; // Dữ liệu giả định
        
        // Set attributes
        request.setAttribute("totalStudents", totalStudents);
        
        // Forward to dashboard.jsp
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}