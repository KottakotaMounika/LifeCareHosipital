<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String errorMsg = (String) request.getAttribute("errorMsg");
    String successMsg = (String) request.getAttribute("successMsg");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Make an Appointment</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');

        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f5f7fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0px 8px 25px rgba(0, 0, 0, 0.1);
            display: flex;
            gap: 30px;
            max-width: 900px;
            align-items: center;
        }

        img {
            width: 380px;
            height: auto;
            border-radius: 18px;
            object-fit: cover;
        }

        .form-container {
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        h2 {
            margin: 0 0 12px 0;
            color: #003366;
            font-size: 26px;
            font-weight: 600;
        }

        p {
            margin: 0 0 25px 0;
            color: #666;
            font-size: 15px;
        }

        label {
            font-size: 14px;
            font-weight: 500;
            color: #003366;
            margin-bottom: 6px;
        }

        select,
        input {
            padding: 14px;
            border: 1px solid #cdd4e0;
            border-radius: 25px;
            width: 100%;
            font-size: 15px;
            background-color: #f3f6fa;
            outline: none;
            transition: 0.3s;
            margin-bottom: 18px; /* 👈 this adds space between boxes */
        }

        select:focus,
        input:focus {
            border-color: #0066ff;
            background-color: #fff;
            box-shadow: 0 0 6px rgba(0, 102, 255, 0.3);
        }

        .btn {
            background-color: #0066ff;
            color: white;
            padding: 14px;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            margin-top: 5px;
            transition: 0.3s;
        }

        .btn:hover {
            background-color: #004ecc;
            transform: scale(1.03);
        }

        a {
            color: #5a2ca0;
            text-decoration: none;
            font-weight: 500;
            margin-top: 10px;
            display: inline-block;
        }

        a:hover {
            text-decoration: underline;
        }

        .error {
            color: red;
            font-weight: bold;
        }

        .success {
            color: green;
            font-weight: bold;
        }

        @media (max-width: 850px) {
            .container {
                flex-direction: column;
                text-align: center;
                max-width: 95%;
            }

            img {
                width: 100%;
            }

            .form-container {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <img src="images/mask.jpeg" alt="Doctor" />
        <div class="form-container">
            <h2>Make an appointment</h2>
            <p>We have some of the best specialists from around the world</p>

            <% if (errorMsg != null) { %>
                <p class="error"><%= errorMsg %></p>
            <% } %>

            <% if (successMsg != null) { %>
                <p class="success"><%= successMsg %></p>
            <% } %>

            <form action="AppointmentController" method="post">
    <label for="department">Department</label>
    <select name="department" id="department" required>
        <option value="">--Select Department--</option>
        <option value="Cardiology">Cardiology</option>
        <option value="Neurology">Neurology</option>
        <option value="Orthopedics">Orthopedics</option>
        <option value="General Medicine">General Medicine</option>
        <option value="Pediatrics">Pediatrics</option>
    </select>

    <input type="text" name="patientName" placeholder="Your name" required>
    <input type="email" name="email" placeholder="Your Email" required>
    <input type="tel" name="mobile" placeholder="Mobile number (10 digits)" pattern="[0-9]{10}" required>
    <input type="date" name="appointmentDate" required>
    <button type="submit" class="btn">Make an appointment</button>
</form>
            

            <a href="view_appointments.jsp">View All Appointments</a>
        </div>
    </div>
</body>
</html>
