/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author NGHIA
 */
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserDAO {
 
    // Thay đổi thông tin kết nối phù hợp với máy của bạn
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Mkokdz123@";
 
    private static final String SQL_AUTHENTICATE = 
        "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
 
    private static final String SQL_UPDATE_LAST_LOGIN = 
        "UPDATE users SET last_login = NOW() WHERE id = ?";
 
    private static final String SQL_GET_BY_ID = 
        "SELECT * FROM users WHERE id = ?";
 
    // Implement getConnection()
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
 
    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("full_name"),
            rs.getString("role"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("last_login")
        );
    }
    
    // Implement updateLastLogin()
    private void updateLastLogin(int userId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_LAST_LOGIN)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace(); // Log error
        }
    }
    
    // Implement authenticate()
    public User authenticate(String username, String password) {
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_AUTHENTICATE)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Map ResultSet to User object
                    user = mapResultSetToUser(rs);
                    
                    // 2. Use BCrypt.checkpw() to verify password
                    if (BCrypt.checkpw(password, user.getPassword())) {
                        // 3. If valid, update last login and return user
                        updateLastLogin(user.getId());
                        return user;
                    } 
                    // 4. If password doesn't match, user remains null (fail authentication)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Implement getUserById()
    public User getUserById(int id) {
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_GET_BY_ID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}