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
import model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException; 
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() {
        // Initialize studentDAO (Task 2.1)
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        String action = request.getParameter("action");

        // If action is null, set to "list" (Task 2.1)
        if (action == null) action = "list";

        try {
            // Route to appropriate method based on action (Task 2.1 & 2.2)
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteStudent(request, response);
                    break;
                case "list":
                default:
                    listStudents(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Route to appropriate method based on action (Task 2.2)
        try {
            switch (action) {
                case "insert":
                    insertStudent(request, response);
                    break;
                case "update":
                    updateStudent(request, response);
                    break;
                default:
                    listStudents(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    // --- CRUD and LIST Methods ---

    /**
     * Handles listing, searching, filtering, and sorting of students (Tasks 2.1, 5.2, 7.2).
     */
    private void listStudents(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException, SQLException {
        
        // Get search/filter/sort parameters (Task 5.2 & 7.2)
        String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
        String major = request.getParameter("major") != null ? request.getParameter("major") : "All Majors";
        String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "id";
        String order = request.getParameter("order") != null ? request.getParameter("order") : "asc";

        // Get list of students from DAO
        List<Student> students = studentDAO.searchStudents(keyword, major, sortBy, order);
        
        // Set as request attributes (Task 2.1)
        request.setAttribute("students", students);
        
        // Set search/filter/sort attributes to maintain state in the view (Task 7.2)
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedMajor", major);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);

        // Forward to student-list.jsp (Task 2.1)
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-list.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Displays the form for adding a new student (Task 2.2).
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Just forwards to the form view
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Displays the form for editing an existing student (Task 2.2).
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Student existingStudent = studentDAO.getStudentById(id);
            if (existingStudent != null) {
                request.setAttribute("student", existingStudent);
            }
        } catch (NumberFormatException e) {
            // Handle error if ID is missing or invalid
            response.sendRedirect("student?action=list&error=Invalid student ID.");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Performs server-side validation (Exercise 6).
     * @return A map of field names to error messages. Empty if validation passes.
     */
    private Map<String, String> validateStudent(HttpServletRequest request, boolean isUpdate) {
        Map<String, String> errors = new HashMap<>();
        
        // Get parameters
        String studentCode = request.getParameter("studentCode").trim();
        String fullName = request.getParameter("fullName").trim();
        String email = request.getParameter("email").trim();
        String major = request.getParameter("major");
        
        int id = 0;
        if (isUpdate) {
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                errors.put("global", "Invalid student ID for update operation.");
            }
        }

        // 1. Full Name Validation (Required, length)
        if (fullName.isEmpty()) {
            errors.put("fullName", "Full Name is required.");
        } else if (fullName.length() < 5) {
            errors.put("fullName", "Full Name must be at least 5 characters.");
        }

        // 2. Email Validation (Required, format, uniqueness)
        if (email.isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,4}$")) {
            errors.put("email", "Invalid email format.");
        } else if (studentDAO.emailExists(email, id)) {
            errors.put("email", "This email is already registered.");
        }

        // 3. Student Code Validation (Required, uniqueness)
        if (!isUpdate) { // Only validate code for INSERT, as it's readonly for UPDATE
            if (studentCode.isEmpty()) {
                errors.put("studentCode", "Student Code is required.");
            } else if (studentDAO.studentCodeExists(studentCode, 0)) {
                errors.put("studentCode", "This Student Code already exists.");
            }
        }
        
        // 4. Major Validation (Required)
        if (major == null || major.isEmpty() || major.equals("Select Major")) {
            errors.put("major", "Major selection is required.");
        }

        return errors;
    }

    /**
     * Inserts a new student (Task 2.2, 4.2, 6.2).
     */
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException {
        
        Map<String, String> errors = validateStudent(request, false);

        if (!errors.isEmpty()) {
            // Validation failed: return to form with errors and pre-filled data
            request.setAttribute("errors", errors);
            // Re-create temporary student object for pre-filling fields
            Student student = new Student(
                request.getParameter("studentCode"),
                request.getParameter("fullName"),
                request.getParameter("email"),
                request.getParameter("major")
            );
            request.setAttribute("student", student);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validation passed
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);
        boolean success = studentDAO.addStudent(newStudent);
        
        String message = success ? "Student added successfully!" : "Failed to add student due to a database error.";
        String type = success ? "message" : "error";
        
        // Redirect to list page with message
        response.sendRedirect("student?action=list&" + type + "=" + message);
    }
    
    /**
     * Updates an existing student (Task 2.2, 4.2, 6.2).
     */
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException {

        Map<String, String> errors = validateStudent(request, true);

        if (!errors.isEmpty()) {
            // Validation failed: return to form with errors and pre-filled data
            request.setAttribute("errors", errors);
            // Re-create temporary student object for pre-filling fields
            try {
                 Student student = new Student(
                    Integer.parseInt(request.getParameter("id")),
                    request.getParameter("studentCode"), // Readonly, but needed for form re-fill
                    request.getParameter("fullName"),
                    request.getParameter("email"),
                    request.getParameter("major"),
                    null // createdAt is not set here
                );
                request.setAttribute("student", student);
            } catch (NumberFormatException e) {
                // If ID is bad, fall back to list with a global error
                response.sendRedirect("student?action=list&error=Update failed: Invalid ID provided.");
                return;
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Validation passed
        int id = Integer.parseInt(request.getParameter("id"));
        // Student code is not updatable, so we only fetch it for the Model object creation
        String studentCode = request.getParameter("studentCode"); 
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        
        // Note: The parameterized constructor used here will ignore the studentCode for the SQL UPDATE
        Student studentToUpdate = new Student(id, studentCode, fullName, email, major, null); 
        
        boolean success = studentDAO.updateStudent(studentToUpdate);
        
        String message = success ? "Student ID " + id + " updated successfully!" : "Failed to update student.";
        String type = success ? "message" : "error";
        
        // Redirect to list page with message
        response.sendRedirect("student?action=list&" + type + "=" + message);
    }

    /**
     * Deletes a student record (Task 2.2, 4.2).
     */
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = studentDAO.deleteStudent(id);

            String message = success ? "Student ID " + id + " deleted successfully!" : "Failed to delete student.";
            String type = success ? "message" : "error";

            // Redirect to list page with message
            response.sendRedirect("student?action=list&" + type + "=" + message);
        } catch (NumberFormatException e) {
            response.sendRedirect("student?action=list&error=Invalid student ID for deletion.");
        }
    }
}