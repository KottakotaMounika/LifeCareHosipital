<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dao.AppointmentDAO, model.Appointment" %>
<%
    String emailParam = request.getParameter("email");
    String dateParam = request.getParameter("date");

    AppointmentDAO dao = new AppointmentDAO();
    List<Appointment> allAppointments = dao.getAllAppointments();

    Appointment latest = null;
    if(allAppointments != null && !allAppointments.isEmpty()) {
        for(Appointment a : allAppointments) {
            if(a.getEmail().equals(emailParam) && a.getAppointmentDate().toString().equals(dateParam)) {
                latest = a;
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Appointment Confirmed</title>
<style>
    body { font-family: Arial; background-color: #f5f7fa; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .container { background: white; padding: 30px; border-radius: 15px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 500px; text-align: center; }
    h2 { color: #28a745; }
    p { margin: 10px 0; font-size: 16px; }
    .btn { display: inline-block; margin-top: 20px; padding: 12px 25px; background-color: #0066ff; color: white; border-radius: 25px; text-decoration: none; font-weight: bold; }
    .btn:hover { background-color: #0052cc; }
</style>
</head>
<body>
<div class="container">
<%
    if(latest != null){
%>
    <h2>Appointment Confirmed!</h2>
    <p><strong>Patient Name:</strong> <%= latest.getPatientName() %></p>
    <p><strong>Department:</strong> <%= latest.getDepartment() %></p>
    <p><strong>Appointment Date:</strong> <%= latest.getAppointmentDate() %></p>
    <p><strong>Assigned Doctor:</strong> <%= latest.getDoctorName() != null ? latest.getDoctorName() : "Not Assigned" %></p>
<%
    } else {
%>
    <h2>No Appointment Found!</h2>
<%
    }
%>
    <a href="appointment.jsp" class="btn">Book Another Appointment</a>
    <a href="view_appointments.jsp" class="btn">View All Appointments</a>
</div>
</body>
</html>
