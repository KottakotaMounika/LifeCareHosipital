<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dao.AppointmentDAO, model.Appointment" %>
<%
    javax.servlet.http.HttpSession sess = request.getSession(false);
    if (sess == null || sess.getAttribute("username") == null) {
        response.sendRedirect("login.html");
        return;
    }
    String username = sess.getAttribute("username").toString();
    String email = sess.getAttribute("email") != null ? sess.getAttribute("email").toString() : "";

    AppointmentDAO dao = new AppointmentDAO();
    int userId = (Integer) session.getAttribute("userId");
    List<Appointment> list = dao.getAppointmentsByUserId(userId);

%>
<!DOCTYPE html>
<html>
<head>
    <title>My Appointments - Lifecare</title>
    <style>
        body { font-family: Arial; background:#f5f7fa; margin:0; padding:0; }
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
    </style>
</head>
<body>
    <h2>My Appointments</h2>
    <%
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
            <td><%= a.getDoctorName() != null ? a.getDoctorName() : "Not Assigned" %></td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        } else {
    %>
        <p class="center">You don’t have any appointments yet.</p>
    <%
        }
    %>
    <div class="center">
        <button class="btn" onclick="window.location.href='appointment.jsp'">Book New Appointment</button>
        <button class="btn" onclick="window.location.href='UserHome.jsp'">Back to Home</button>
    </div>
</body>
</html>
