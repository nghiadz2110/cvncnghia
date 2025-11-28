<%-- 
    Document   : dashboard
    Created on : 28 thg 11, 2025, 21:33:20
    Author     : NGHIA
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="user" value="${sessionScope.user}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        /* CSS cơ bản cho dashboard */
        body { font-family: sans-serif; margin: 0; background-color: #f4f7f6; }
        .navbar { background-color: #343a40; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .navbar a { color: white; text-decoration: none; margin-left: 20px; }
        .navbar a:hover { text-decoration: underline; }
        .container { padding: 30px; }
        .welcome-message { margin-bottom: 20px; font-size: 1.5em; color: #333; }
        .stats-cards { display: flex; gap: 20px; margin-bottom: 30px; }
        .card { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); flex: 1; text-align: center; }
        .card h3 { margin-top: 0; color: #007bff; }
        .card p { font-size: 2em; font-weight: bold; margin: 5px 0 0; }
        .quick-actions button { background-color: #28a745; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin-right: 10px; transition: background-color 0.3s; }
        .quick-actions button:hover { background-color: #1e7e34; }
    </style>
</head>
<body>
    <div class="navbar">
        <h2>Student Management System</h2>
        <div>
            <span>Hello, <strong><c:out value="${user.fullName}"/></strong> (<c:out value="${user.role}"/>)</span>
            <a href="${pageContext.request.contextPath}/logout">Log out</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-message">
            Welcome back, ${user.fullName}!
        </div>

        <div class="stats-cards">
            <div class="card">
                <h3>Total student</h3>
                <p><c:out value="${totalStudents}"/></p>
            </div>
            </div>

        <div class="quick-actions">
            <h4>Button:</h4>
            <c:if test="${user.isAdmin()}">
                <button onclick="location.href='student/add'">+ Add new student</button>
                <button onclick="location.href='admin/manage_users'">User management</button>
            </c:if>
            <button onclick="location.href='student/list'">List of student</button>
            <button onclick="location.href='user/change_password'">Change password</button>
        </div>
        
    </div>
</body>
</html>