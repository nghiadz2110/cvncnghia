/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author NGHIA
 */
import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StudentDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Mkokdz123";
    private static final String SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String INSERT_STUDENT = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STUDENT = "UPDATE students SET full_name = ?, email = ?, major = ? WHERE id = ?";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String CHECK_CODE_EXISTS = "SELECT COUNT(*) FROM students WHERE student_code = ? AND id != ?";
    private static final String CHECK_EMAIL_EXISTS = "SELECT COUNT(*) FROM students WHERE email = ? AND id != ?";

    /**
     * Establishes a connection to the database.
     */
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    public List<Student> searchStudents(String keyword, String major, String sortBy, String order) {
        List<Student> students = new ArrayList<>();
        
        // Base SQL query
        StringBuilder sql = new StringBuilder("SELECT * FROM students WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        // 1. Search filter (Exercise 5)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (student_code LIKE ? OR full_name LIKE ? OR email LIKE ?)");
            String likeKeyword = "%" + keyword.trim() + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
        }

        // 2. Major filter (Exercise 7)
        if (major != null && !major.isEmpty() && !major.equals("All Majors")) {
            sql.append(" AND major = ?");
            params.add(major);
        }
        
        // 3. Sorting (Exercise 7)
        String validatedSortBy = switch (sortBy) {
            case "id" -> "id";
            case "studentCode" -> "student_code";
            case "fullName" -> "full_name";
            case "email" -> "email";
            case "major" -> "major";
            default -> "id"; // Default sort
        };
        
        String validatedOrder = order.equalsIgnoreCase("desc") ? "DESC" : "ASC";
        
        sql.append(" ORDER BY ").append(validatedSortBy).append(" ").append(validatedOrder);

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            
            // Set parameters for PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    public Student getStudentById(int id) {
        Student student = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                student = mapRowToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }
    public boolean addStudent(Student student) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT)) {
            
            preparedStatement.setString(1, student.getStudentCode());
            preparedStatement.setString(2, student.getFullName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getMajor());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateStudent(Student student) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT)) {
            
            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getMajor());
            preparedStatement.setInt(4, student.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteStudent(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT)) {
            
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean studentCodeExists(String studentCode, int currentId) {
        return checkExistence(CHECK_CODE_EXISTS, studentCode, currentId);
    }
    public boolean emailExists(String email, int currentId) {
        return checkExistence(CHECK_EMAIL_EXISTS, email, currentId);
    }
    private boolean checkExistence(String sql, String value, int currentId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, currentId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("student_code"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("major"),
            rs.getTimestamp("created_at")
        );
    }
}