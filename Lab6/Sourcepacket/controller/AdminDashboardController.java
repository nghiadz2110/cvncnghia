/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author NGHIA
 */
import dao.StudentDAO;
import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller xử lý trang Dashboard dành cho Admin.
 * Được ánh xạ tới /admin/dashboard, theo yêu cầu của LoginController.
 */
@WebServlet("/admin_dashboard")
public class AdminDashboardController extends HttpServlet {
    
    private StudentDAO studentDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        // Khởi tạo các DAO cần thiết
        studentDAO = new StudentDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        // Kiểm tra phòng ngừa: Đảm bảo user đã đăng nhập và là Admin.
        // AdminFilter đã làm việc này, nhưng kiểm tra lại là cần thiết.
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 1. Lấy dữ liệu thống kê từ DAO (Giả định các method này đã được thêm vào DAO)
            
            // Từ StudentDAO (Cần thêm method này vào StudentDAO)
            int totalStudents = studentDAO.countStudents(); 
            // int studentsByMajor = studentDAO.countStudentsByMajor("CNTT"); 
            
            // Từ UserDAO (Cần thêm method này vào UserDAO)
            int totalActiveUsers = userDAO.countActiveUsers(); 
            // int totalAdmins = userDAO.countAdmins();
            
            // 2. Đặt dữ liệu vào request
            request.setAttribute("totalStudents", totalStudents);
            request.setAttribute("totalActiveUsers", totalActiveUsers);
            // request.setAttribute("studentsByMajor", studentsByMajor);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading dashboard statistics: " + e.getMessage());
            
            // Nếu DAO chưa có method, sử dụng dữ liệu giả (Hardcode)
            request.setAttribute("totalStudents", 0); 
            request.setAttribute("totalActiveUsers", 0);
        }
        request.getRequestDispatcher("/views/admin_dashboard.jsp").forward(request, response);
    }
}