<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dao.AppointmentDAO, model.Appointment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Appointments</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f5f7fa; margin:0; padding:0; }
        h2 { text-align:center; color:#003366; margin-top:30px; }
        table {
            width:90%; margin:30px auto; border-collapse: collapse;
            background:white; border-radius:10px; overflow:hidden;
            box-shadow:0px 4px 10px rgba(0,0,0,0.1);
        }
        th, td { padding:15px; border-bottom:1px solid #ddd; text-align:center; }
        th { background-color:#0066ff; color:white; }
        tr:hover { background-color:#f1f1f1; }
        .btn { background-color:#0066ff; color:white; border:none; padding:10px 15px; border-radius:20px; cursor:pointer; margin:10px; }
        .btn:hover { background-color:#004db3; }
        .center { text-align:center; }
        .doctor-assigned { color:#28a745; font-weight:bold; }
        .doctor-not-assigned { color:#ff4d4d; font-weight:bold; }
    </style>
</head>
<body>
    <h2>All Appointments</h2>

    <%
        AppointmentDAO dao = new AppointmentDAO();
        List<Appointment> list = dao.getAllAppointments();
        if (list != null && !list.isEmpty()) {
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Department</th>
            <th>Patient Name</th>
            <th>Email</th>
            <th>Mobile</th>
            <th>Appointment Date</th>
            <th>Doctor Name</th>
        </tr>
        <%
            for (Appointment a : list) {
        %>
        <tr>
            <td><%= a.getAppointmentId() %></td>
            <td><%= a.getDepartment() %></td>
            <td><%= a.getPatientName() %></td>
            <td><%= a.getEmail() %></td>
            <td><%= a.getMobile() %></td>
            <td><%= a.getAppointmentDate() %></td>
            <td class="<%= (a.getDoctorName() != null && !a.getDoctorName().isEmpty()) ? "doctor-assigned" : "doctor-not-assigned" %>">
                <%= (a.getDoctorName() != null && !a.getDoctorName().isEmpty()) ? a.getDoctorName() : "Not Assigned" %>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        } else {
    %>
        <p class="center">No appointments found.</p>
    <%
        }
    %>

    <div class="center">
        <button class="btn" onclick="window.location.href='appointment.jsp'">Book New Appointment</button>
    </div>
</body>
</html>
