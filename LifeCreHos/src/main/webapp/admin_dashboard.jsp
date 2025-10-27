<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Doctor" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Lifecare Hospital</title>
    <style>
        body {
            background: #f5f9ff;
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,80,0.1);
        }
        h2 {
            color: #007BFF;
            text-align: center;
            margin-bottom: 30px;
        }
        h3 {
            color: #007BFF;
            margin-bottom: 15px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 12px;
            text-align: left;
        }
        th {
            background: #007BFF;
            color: white;
        }
        form input, form select {
            padding: 8px;
            margin: 5px 0;
            border-radius: 6px;
            border: 1px solid #ccc;
        }
        form input[type="submit"] {
            background: #007BFF;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 6px;
            padding: 10px 20px;
            margin-top: 10px;
            font-size: 16px;
        }
        form input[type="submit"]:hover {
            background: #0056b3;
        }
        .delete-btn {
            padding: 6px 12px;
            background: #FF4D4D;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            border: none;
            cursor: pointer;
        }
        .delete-btn:hover {
            background: #cc0000;
        }
        .logout {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 25px;
            background: #007BFF;
            color: white;
            border-radius: 6px;
            text-decoration: none;
        }
        .logout:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Admin Dashboard</h2>

        <!-- Add Doctor Form -->
        <h3>Add Doctor</h3>
        <form action="doctor" method="post">
            <input type="hidden" name="action" value="add">
            Name: <input type="text" name="name" required>
            Specialization: <input type="text" name="specialization" required>
            Contact Number: <input type="text" name="contact">
            <input type="submit" value="Add Doctor">
        </form>

        <!-- Doctors Table -->
        <h3>All Doctors</h3>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Specialization</th>
                <th>Contact</th>
                <th>Action</th>
            </tr>
            <%
                List<Doctor> doctors = (List<Doctor>)request.getAttribute("doctorList");
                if(doctors != null) {
                    for(Doctor d : doctors) {
            %>
            <tr>
                <td><%= d.getDoctorId() %></td>
                <td><%= d.getName() %></td>
                <td><%= d.getSpecialization() %></td>
                <td><%= d.getContactNumber() %></td>
                <td>
                    <form action="doctor" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="doctorId" value="<%= d.getDoctorId() %>">
                        <input type="submit" value="Delete" class="delete-btn">
                    </form>
                </td>
            </tr>
            <%      }
                }
            %>
        </table>

        <a href="logout" class="logout">Logout</a>
    </div>
</body>
</html>
