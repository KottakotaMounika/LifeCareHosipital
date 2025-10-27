<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ffe6e6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .box {
            background: white;
            padding: 40px;
            border-radius: 15px;
            text-align: center;
            box-shadow: 0px 4px 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #cc0000;
        }
        .btn {
            background-color: #0066ff;
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            border: none;
            cursor: pointer;
            margin-top: 20px;
        }
        .btn:hover {
            background-color: #0052cc;
        }
    </style>
</head>
<body>
    <div class="box">
        <h2>
            <%
                String errorType = (String) request.getAttribute("errorType");
                if("duplicate".equals(errorType)){
                    out.print("❌ Duplicate Appointment!");
                } else {
                    out.print("Something went wrong!");
                }
            %>
        </h2>
        <p>
            <%
                if("duplicate".equals(errorType)){
                    out.print("You already have an appointment in this department on this date.");
                } else {
                    out.print("Please try again later.");
                }
            %>
        </p>
        <button class="btn" onclick="window.location.href='appointment.html'">Back to Appointment</button>
    </div>
</body>
</html>
