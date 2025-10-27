<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dao.DoctorDAO, model.Doctor" %>
<%
    String errorMsg = (String) request.getAttribute("errorMsg");
    DoctorDAO doctorDAO = new DoctorDAO();
    List<Doctor> doctors = doctorDAO.getAllDoctors();
    Set<String> uniqueDepartments = new HashSet<>();
    for(Doctor d : doctors){ uniqueDepartments.add(d.getSpecialization()); }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Appointment</title>
    <style>
        body { font-family: Arial; background: #f0f0f0; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
        .container { background: #fff; padding: 40px; border-radius: 15px; box-shadow: 0px 4px 10px rgba(0,0,0,0.1); width: 400px; }
        h2 { text-align: center; color: #333; }
        input, select { width: 100%; padding: 10px; margin: 10px 0; border-radius: 8px; border: 1px solid #ccc; }
        .btn { background-color: #28a745; color: white; padding: 10px; width: 100%; border: none; border-radius: 8px; cursor: pointer; }
        .btn:hover { background-color: #218838; }
        .error { color: red; text-align: center; }
        input[readonly] { background-color: #e9ecef; }
    </style>
    <script>
        var deptDoctorMap = {};
        <% 
            Set<String> addedDept = new HashSet<>();
            for(Doctor d : doctors){
                if(!addedDept.contains(d.getSpecialization())){
                    addedDept.add(d.getSpecialization());
                    String dept = d.getSpecialization().replace("\"","\\\"");
                    String doctor = d.getName().replace("\"","\\\"");
        %>
            deptDoctorMap["<%=dept%>"] = "<%=doctor%>";
        <% 
                }
            } 
        %>

        function updateDoctor() {
            var dept = document.getElementById("department").value;
            document.getElementById("doctor_name").value = deptDoctorMap[dept] || "Not Assigned";
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Book Appointment</h2>
        <% if(errorMsg != null) { %>
            <p class="error"><%= errorMsg %></p>
        <% } %>
        <form action="AppointmentController" method="post">
            <input type="text" name="patientName" placeholder="Patient Name" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="text" name="mobile" placeholder="Mobile Number" required>
            
            <label for="department">Department</label>
            <select name="department" id="department" onchange="updateDoctor()" required>
                <option value="">Select Department</option>
                <% for(String dept : uniqueDepartments){ %>
                    <option value="<%=dept%>"><%=dept%></option>
                <% } %>
            </select>

            <input type="text" id="doctor_name" name="doctorName" placeholder="Assigned Doctor" readonly>
            <input type="date" name="appointmentDate" required>

            <button type="submit" class="btn">Book Appointment</button>
        </form>
    </div>
</body>
</html>
