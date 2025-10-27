<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f7fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-box {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            width: 350px;
        }
        h2 { text-align: center; color: #003366; }
        input {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 25px;
            border: 1px solid #d1d1d1;
            font-size: 14px;
            outline: none;
        }
        .btn {
            width: 100%;
            padding: 12px;
            background-color: #0066ff;
            color: white;
            border: none;
            border-radius: 25px;
            font-size: 16px;
            cursor: pointer;
        }
        .btn:hover { background-color: #0052cc; }
        .error { color: red; text-align: center; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="login-box">
        <h2>Admin Login</h2>
        <form action="AdminController" method="post">
            <input type="hidden" name="action" value="login">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit" class="btn">Login</button>
        </form>
        <%
            String errorMsg = (String) request.getAttribute("errorMsg");
            if(errorMsg != null) {
        %>
            <p class="error"><%= errorMsg %></p>
        <%
            }
        %>
    </div>
</body>
</html>