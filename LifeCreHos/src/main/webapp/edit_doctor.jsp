<%@ page import="dao.DoctorDAO, model.Doctor" %>
<%
    int doctorId = Integer.parseInt(request.getParameter("doctorId"));
    DoctorDAO dao = new DoctorDAO();
    Doctor doctor = dao.getDoctorById(doctorId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Doctor</title>
    <style>
        body { font-family: Arial; background: #f0f4ff; padding: 40px; }
        form { background: white; padding: 25px; border-radius: 10px; width: 400px; margin: auto; box-shadow: 0 3px 10px rgba(0,0,0,0.2); }
        input[type="text"], input[type="submit"] { width: 100%; padding: 10px; margin: 10px 0; border-radius: 6px; border: 1px solid #ccc; }
        input[type="submit"] { background: #007bff; color: white; cursor: pointer; }
        input[type="submit"]:hover { background: #0056b3; }
    </style>
</head>
<body>
    <h2 align="center">Edit Doctor</h2>
    <form action="doctor" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">

        Name: <input type="text" name="name" value="<%= doctor.getName() %>" required>
        Specialization: <input type="text" name="specialization" value="<%= doctor.getSpecialization() %>" required>
        Contact: <input type="text" name="contact" value="<%= doctor.getContactNumber() %>" required>

        <input type="submit" value="Update Doctor">
    </form>
</body>
</html>
