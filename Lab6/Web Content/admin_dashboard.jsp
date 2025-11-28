<%-- 
    Document   : admin_dashboard
    Created on : 28 thg 11, 2025, 23:56:04
    Author     : NGHIA
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f6; }
        .container { max-width: 1200px; margin: 50px auto; padding: 20px; }
        .card { 
            background: white; 
            padding: 20px; 
            border-radius: 8px; 
            box-shadow: 0 4px 6px rgba(0,0,0,0.1); 
            margin-bottom: 20px;
        }
        .header { text-align: center; margin-bottom: 30px; }
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }
        .stat-box { 
            text-align: center; 
            padding: 25px; 
            border-radius: 6px; 
            color: white; 
            font-size: 1.2em;
        }
        .stat-box h2 { font-size: 2.5em; margin: 0; }
        .stat-box p { margin: 5px 0 0; font-weight: 300; }
        .stat-students { background-color: #007bff; }
        .stat-users { background-color: #28a745; }
        .nav-links { margin-top: 20px; text-align: center; }
        .nav-links a { margin: 0 10px; text-decoration: none; color: #007bff; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Admin Dashboard</h1>
            <p>Xin chào, ${adminFullName}. This is your management page.</p>
            
            <c:if test="${not empty error}">
                <p style="color: red; border: 1px solid red; padding: 10px;">Lỗi: ${error}</p>
            </c:if>
        </div>

        <div class="stats-grid">
            <div class="stat-box stat-students">
                <h2>${totalStudents}</h2>
                <p>Total students</p>
            </div>

            <div class="stat-box stat-users">
                <h2>${totalActiveUsers}</h2>
                <p>Total users working</p>
            </div>
            
            </div>

        <div class="card">
            <h2>Chức năng Quản trị</h2>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/student">Manage student (CRUD)</a>
                <a href="${pageContext.request.contextPath}/admin/user_management">Manage User (Chưa triển khai)</a>
                <a href="${pageContext.request.contextPath}/user/change_password">Change password</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>

    </div>
</body>
</html>