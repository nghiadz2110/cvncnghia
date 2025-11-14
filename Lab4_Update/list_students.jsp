<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <%-- ADDED viewport for mobile responsiveness --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1 { color: #333; }
        .message {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin-bottom: 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        
        .search-form {
            margin-bottom: 20px;
        }
        .search-form input[type="text"] {
            padding: 10px;
            width: 300px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        .search-form button {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .search-form a {
            display: inline-block;
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-left: 5px;
            font-size: 14px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
        }
        th {
            background-color: #007bff;
            color: white;
            padding: 12px;
            text-align: left;
        }
        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        tr:hover { background-color: #f8f9fa; }
        .action-link {
            color: #007bff;
            text-decoration: none;
            margin-right: 10px;
        }
        .delete-link { color: #dc3545; }
        
        .pagination {
            margin-top: 20px;
        }
        .pagination a, .pagination strong {
            display: inline-block;
            padding: 8px 12px;
            margin: 0 2px;
            border-radius: 4px;
            text-decoration: none;
            border: 1px solid #ddd;
            background-color: #fff;
            color: #007bff;
        }
        .pagination strong {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .pagination a:hover {
            background-color: #f0f0f0;
        }
        .table-responsive {
            overflow-x: auto;
        }
        
        @media (max-width: 768px) {
            table {
                font-size: 12px;
            }
            th, td {
                padding: 5px;
            }
        }
        
    </style>
</head>
<body>
    <h1>📚 Student Management System</h1>
    
    <%
        String keyword = request.getParameter("keyword");
        int currentPage = 1;
        int recordsPerPage = 10;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        int offset = (currentPage - 1) * recordsPerPage;
        int totalRecords = 0;
        int totalPages = 1;
    %>
    
    <div class="search-form">
        <form action="list_students.jsp" method="GET">
            <input type="text" name="keyword" placeholder="Search by name or code..."
                   value="<%= (keyword != null) ? keyword : "" %>">
            <button type="submit">Search</button>
            <a href="list_students.jsp">Clear</a>
        </form>
    </div>
    
    <%-- MODIFIED: Added icons (Requirement 7.2a) --%>
    <% if (request.getParameter("message") != null) { %>
        <div class="message success">
            ✓ <%= request.getParameter("message") %>
        </div>
    <% } %>
    
    <% if (request.getParameter("error") != null) { %>
        <div class="message error">
            × <%= request.getParameter("error") %>
        </div>
    <% } %>
    
    <a href="add_student.jsp" class="btn">➕ Add New Student</a>
    
    <%-- NEW: Added responsive wrapper (Requirement 7.2c) --%>
    <div class="table-responsive">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Student Code</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Major</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
<%
    Connection conn = null;
    PreparedStatement pstmt = null; 
    ResultSet rs = null;
    PreparedStatement pstmtCount = null;
    ResultSet rsCount = null;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/student_management",
            "root",
            "Mkokdz123@"
        );
        
        String countSql;
        String searchKeyword = "%" + (keyword != null ? keyword : "") + "%";
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            countSql = "SELECT COUNT(*) FROM students WHERE full_name LIKE ? OR student_code LIKE ?";
            pstmtCount = conn.prepareStatement(countSql);
            pstmtCount.setString(1, searchKeyword);
            pstmtCount.setString(2, searchKeyword);
        } else {
            countSql = "SELECT COUNT(*) FROM students";
            pstmtCount = conn.prepareStatement(countSql);
        }
        
        rsCount = pstmtCount.executeQuery();
        if (rsCount.next()) {
            totalRecords = rsCount.getInt(1);
        }
        totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        
        String sql;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql = "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        } else {
            sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";
        }
        
        pstmt = conn.prepareStatement(sql);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setInt(3, recordsPerPage);
            pstmt.setInt(4, offset);
        } else {
            pstmt.setInt(1, recordsPerPage);
            pstmt.setInt(2, offset);
        }
        
        rs = pstmt.executeQuery(); 
        
        boolean found = false;
        while (rs.next()) {
            found = true;
            int id = rs.getInt("id");
            String studentCode = rs.getString("student_code");
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            String major = rs.getString("major");
            Timestamp createdAt = rs.getTimestamp("created_at");
%>
            <tr>
                <td><%= id %></td>
                <td><%= studentCode %></td>
                <td><%= fullName %></td>
                <td><%= email != null ? email : "N/A" %></td>
                <td><%= major != null ? major : "N/A" %></td>
                <td><%= createdAt %></td>
                <td>
                    <a href="edit_student.jsp?id=<%= id %>" class="action-link">✏️ Edit</a>
                    <a href="delete_student.jsp?id=<%= id %>" 
                       class="action-link delete-link"
                       onclick="return confirm('Are you sure?')">🗑️ Delete</a>
                </td>
            </tr>
<%
        }
        
        if (!found) {
            out.println("<tr><td colspan='7' style='text-align:center;'>No students found.</td></tr>");
        }
        
    } catch (ClassNotFoundException e) {
        out.println("<tr><td colspan='7'>Error: JDBC Driver not found!</td></tr>");
        e.printStackTrace();
    } catch (SQLException e) {
        out.println("<tr><td colspan='7'>Database Error: " + e.getMessage() + "</td></tr>");
        e.printStackTrace();
    } finally {
        try {
            if (rsCount != null) rsCount.close();
            if (pstmtCount != null) pstmtCount.close();
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close(); 
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
            </tbody>
        </table>
    </div>

    <div class="pagination">
    <%
        String searchParam = "";
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchParam = "&keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8");
        }
    
        if (totalPages > 1) {
            if (currentPage > 1) {
    %>
                <a href="list_students.jsp?page=<%= currentPage - 1 %><%= searchParam %>">Previous</a>
    <%
            }
            for (int i = 1; i <= totalPages; i++) {
                if (i == currentPage) {
    %>
                    <strong><%= i %></strong>
    <%
                } else {
    %>
                    <a href="list_students.jsp?page=<%= i %><%= searchParam %>"><%= i %></a>
    <%
                }
            }
            if (currentPage < totalPages) {
    %>
                <a href="list_students.jsp?page=<%= currentPage + 1 %><%= searchParam %>">Next</a>
    <%
            }
        }
    %>
    </div>
    <script>
        setTimeout(function() {
            var messages = document.querySelectorAll('.message');
            messages.forEach(function(msg) {
                msg.style.display = 'none';
            });
        }, 3000); // 3000 milliseconds = 3 seconds
    </script>
    
</body>
</html>