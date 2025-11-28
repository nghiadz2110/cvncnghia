<%-- 
    Document   : login
    Created on : 28 thg 11, 2025, 21:33:13
    Author     : NGHIA
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
 <meta charset="UTF-8">
 <title>Đăng nhập | Student Management System</title>
 <style>
 /* Basic CSS cho form đăng nhập */
 body { font-family: sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
 .login-container { background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); width: 100%; max-width: 350px; text-align: center; }
 .login-header h1 { color: #333; margin-bottom: 5px; }
 .login-header p { color: #777; margin-bottom: 20px; }
 .form-group { margin-bottom: 15px; text-align: left; }
 .form-group label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
 .form-group input[type="text"], .form-group input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; box-sizing: border-box; transition: border-color 0.3s; }
 .form-group input:focus { border-color: #007bff; outline: none; box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25); }
 .btn-submit { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin-top: 10px; transition: background-color 0.3s; }
 .btn-submit:hover { background-color: #0056b3; }
 .alert { padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: left; font-weight: bold; }
 .alert-error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
 .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
 .demo-credentials { margin-top: 25px; padding-top: 15px; border-top: 1px solid #eee; text-align: left; font-size: 0.9em; color: #666; }
 .demo-credentials strong { color: #333; }
 </style>
</head>
<body>
 <div class="login-container">
 <div class="login-header">
 <h1>🔐 Login</h1>
 <p>Student Management System</p>
 </div>
 
 <c:if test="${not empty error}">
 <div class="alert alert-error">
 ❌ ${error}
 </div>
 </c:if>
 
 <c:if test="${not empty param.message}">
 <div class="alert alert-success">
 ✅ ${param.message}
 </div>
 </c:if>
 
 <form action="login" method="post">
 <div class="form-group">
 <label for="username">Username:</label>
 <input type="text" id="username" name="username" required
                value="${not empty username ? username : ''}" 
                placeholder="Type you username">
 </div>
 <div class="form-group">
 <label for="password">Password:</label>
 <input type="password" id="password" name="password" required
                placeholder="Type your Password">
 </div>
 <button type="submit" class="btn-submit">Login</button>
 </form>
 </div>
</body>
</html>
