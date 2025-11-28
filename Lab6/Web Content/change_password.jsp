<%-- 
    Document   : change_password
    Created on : 28 thg 11, 2025, 23:19:28
    Author     : NGHIA
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Change Password</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #e9ecef; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
        .container { 
            width: 100%; 
            max-width: 420px; 
            background: white; 
            padding: 30px; 
            border-radius: 10px; 
            box-shadow: 0 4px 20px rgba(0,0,0,0.1); 
        }
        h2 { text-align: center; margin-bottom: 30px; color: #007bff; }
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; margin-bottom: 6px; font-weight: 600; color: #495057; }
        .form-control { 
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ced4da; 
            border-radius: 5px; 
            box-sizing: border-box;
            font-size: 1em;
        }
        .message-box { padding: 12px; border-radius: 5px; margin-bottom: 20px; font-weight: 500; text-align: center; }
        .message-success { background-color: #d4edda; color: #155724; border-color: #c3e6cb; }
        .message-error { background-color: #f8d7da; color: #721c24; border-color: #f5c6cb; }
        .btn-submit { 
            width: 100%; 
            padding: 12px; 
            background-color: #007bff; 
            color: white; 
            border: none; 
            border-radius: 5px; 
            cursor: pointer; 
            font-size: 1.1em;
            transition: background-color 0.3s;
        }
        .btn-submit:hover { background-color: #0056b3; }
        .back-link { display: block; text-align: center; margin-top: 20px; color: #6c757d; text-decoration: none; font-size: 0.9em; }
        .back-link:hover { color: #007bff; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Change Password</h2>

        <c:if test="${not empty message}">
            <div class="message-box message-success">
                <c:out value="${message}"/>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="message-box message-error">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/user/change_password" method="POST">
            
            <div class="form-group">
                <label for="current_password">Current password:</label>
                <input type="password" id="current_password" name="current_password" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="new_password">New password (at least 8 characters):</label>
                <input type="password" id="new_password" name="new_password" class="form-control" required minlength="8">
            </div>

            <div class="form-group">
                <label for="confirm_password">Verify new password:</label>
                <input type="password" id="confirm_password" name="confirm_password" class="form-control" required minlength="8">
            </div>

            <button type="submit" class="btn-submit">Save</button>
        </form>

        <c:choose>
            <c:when test="${sessionScope.user.isAdmin()}">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="back-link">← Back to Dashboard Admin</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/dashboard" class="back-link">← back to Dashboard</a>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>