<%@ page import="dao.AppointmentDAO,model.Appointment" %>
<%
    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
    AppointmentDAO appointmentDAO = new AppointmentDAO();
    Appointment a = appointmentDAO.getAppointmentById(appointmentId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Appointment</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(to right, #eef2f3, #8e9eab);
            padding: 50px;
        }
        form {
            background: #fff;
            padding: 25px;
            border-radius: 15px;
            width: 420px;
            margin: auto;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            color: #444;
        }
        input {
            width: 100%;
            padding: 10px;
            margin: 8px 0 15px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 15px;
        }
        input[type="submit"] {
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: bold;
            border-radius: 8px;
        }
        input[type="submit"]:hover {
            background: #0056b3;
            transition: 0.3s;
        }
        a {
            display: block;
            text-align: center;
            margin-top: 10px;
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <form action="AppointmentController" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">

        <h2>Edit Appointment</h2>

        <label>Department:</label>
        <input type="text" name="department" value="<%= a.getDepartment() %>" required>

        <label>Patient Name:</label>
        <input type="text" name="patientName" value="<%= a.getPatientName() %>" required>

        <label>Email:</label>
        <input type="email" name="email" value="<%= a.getEmail() %>" required>

        <label>Mobile:</label>
        <input type="text" name="mobile" value="<%= a.getMobile() %>" required>

        <label>Date:</label>
        <input type="date" name="appointmentDate" value="<%= a.getAppointmentDate() %>" required>

        <input type="hidden" name="doctorId" value="<%= a.getDoctorId() %>">

        <input type="submit" value="Update Appointment">
    </form>

</body>
</html>
