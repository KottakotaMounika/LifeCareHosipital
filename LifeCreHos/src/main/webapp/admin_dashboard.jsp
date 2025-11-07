<%@ page import="java.util.*,dao.AdminDAO,dao.DoctorDAO,model.Doctor,model.Appointment,model.User" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || sess.getAttribute("currentUser") == null) {
        response.sendRedirect("login.html");
        return;
    }

    String adminName = "";
    Object cu = sess.getAttribute("currentUser");
    if (cu instanceof User) {
        adminName = ((User) cu).getUsername();
    } else {
        adminName = cu.toString();
    }

    AdminDAO adminDAO = new AdminDAO();
    DoctorDAO doctorDAO = new DoctorDAO();

    int totalDoctors = doctorDAO.getTotalDoctors();
    int totalPatients = adminDAO.getTotalPatients();
    int totalUsers = adminDAO.getTotalUsers();
    int totalAppointments = adminDAO.getTotalAppointments();

    Map<String,Integer> deptData = adminDAO.getAppointmentsByDepartment();
    Map<String,Integer> docData = adminDAO.getAppointmentsByDoctor();

    List<Doctor> doctorList = doctorDAO.getAllDoctors();
    List<Appointment> appointmentList = adminDAO.getAllPatients();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard - Lifecare Hospital</title>
<style>
    :root {
        --blue:#007BFF;
        --navy:#004aad;
        --bg:#f4f7fb;
        --card:#ffffff;
    }
    body {
        margin:0;
        font-family:"Poppins",Arial,sans-serif;
        background:var(--bg);
        color:#222;
    }
    .wrap { display:flex; min-height:100vh; }

    /* Sidebar */
    .sidebar {
        width:240px;
        background:linear-gradient(180deg,var(--blue),var(--navy));
        color:white;
        padding:25px 18px;
        box-shadow:0 6px 20px rgba(0,0,0,0.08);
        position:fixed;
        height:100vh;
    }
    .brand { font-size:20px;font-weight:700;margin-bottom:25px;text-align:center; }
    .nav a {
        display:block;
        color:#eaf3ff;
        padding:10px 12px;
        text-decoration:none;
        border-radius:8px;
        margin-bottom:8px;
    }
    .nav a:hover { background:rgba(255,255,255,0.06); }
    .logout-btn {
        display:inline-block;
        margin-top:12px;
        padding:8px 12px;
        background:#ff5b5b;
        color:white;
        border-radius:8px;
        text-decoration:none;
    }
  
    .logout-btnp:hover { background:#d93636; }

    /* Top Header */
   
  
    .logout-btn-top {
        background:#ff4d4d;
        color:white;
        border:none;
        padding:8px 15px;
        border-radius:6px;
        font-weight:500;
        text-decoration:none;
    }
    .logout-btn-top:hover { background:#d93636; }

    /* Main content */
    .main {
        margin-left:260px;
        padding:0px 40px 40px 40px;
        box-sizing:border-box;
    }
    h1,h2 { color:var(--navy); }
    .cards {
        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(220px,1fr));
        gap:20px;
        margin:25px 0;
    }
    .card {
        background:var(--card);
        border-radius:12px;
        box-shadow:0 6px 18px rgba(14,30,37,0.04);
        padding:20px;
        text-align:center;
    }
    .card h3 { color:var(--navy);font-size:17px;margin-bottom:8px; }
    .card p { font-size:22px;font-weight:700; }

    section {
        background:white;
        border-radius:12px;
        box-shadow:0 6px 18px rgba(14,30,37,0.04);
        padding:25px;
        margin-bottom:35px;
    }

    table {
        width:100%;
        border-collapse:collapse;
        margin-top:15px;
    }
    th,td {
        padding:12px 14px;
        text-align:center;
        border-bottom:1px solid #eef2f6;
    }
    th {
        background:linear-gradient(90deg,var(--blue),var(--navy));
        color:white;
    }
    tr:hover td { background:#fbfdff; }
    .edit-btn, .delete-btn {
        border:none;
        border-radius:6px;
        padding:6px 10px;
        color:white;
        text-decoration:none;
        font-weight:500;
    }
    .edit-btn { background:#007bff; }
    
    .edit-btn:hover { background:#005ed2; }
   
    
   
    

    input[type="text"], select {
        padding:8px;
        border:1px solid #ccc;
        border-radius:8px;
        margin-right:10px;
    }
    input[type="submit"] {
        background:var(--blue);
        color:white;
        border:none;
        border-radius:6px;
        padding:8px 14px;
        cursor:pointer;
    }
</style>
</head>
<body>
<div class="wrap">
    <aside class="sidebar">
        <div class="brand">Lifecare Admin</div>
        <nav class="nav">
            <a href="#dashboard">Dashboard</a>
            <a href="#reports">Reports</a>
            <a href="#doctors">Doctors</a>
            <a href="#patients">Appointments</a>
            <a href="webpage.html">Home</a>
        </nav>
        <a href="AdminController?action=logout" class="logout-btn">Logout</a>
    </aside>



    <main class="main">
        <section id="dashboard">
            <h1>Dashboard Overview</h1>
            <div class="cards">
                <div class="card"><h3>Total Doctors</h3><p><%= totalDoctors %></p></div>
                <div class="card"><h3>Total Patients</h3><p><%= totalPatients %></p></div>
                <div class="card"><h3>Total Users</h3><p><%= totalUsers %></p></div>
                <div class="card"><h3>Total Appointments</h3><p><%= totalAppointments %></p></div>
            </div>
        </section>

        <section id="reports">
    <h2>Reports Overview</h2>
    <div id="deptChart" style="width: 400px; height: 300px; display: inline-block;"></div>
    <div id="docChart" style="width: 400px; height: 300px; display: inline-block;"></div>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawCharts);

        function drawCharts() {
            // Department Data
            var deptData = google.visualization.arrayToDataTable([
                ['Department', 'Appointments Count'],
                <% for(String key : deptData.keySet()){ %>
                    ['<%= key %>', <%= deptData.get(key) %>],
                <% } %>
            ]);

            var deptOptions = {
                title: 'Appointments by Department',
                pieHole: 0.4,
                chartArea: { width: '90%', height: '80%' },
                legend: { position: 'bottom' }
            };

            var deptChart = new google.visualization.PieChart(document.getElementById('deptChart'));
            deptChart.draw(deptData, deptOptions);

            // Doctor Data
            var docData = google.visualization.arrayToDataTable([
                ['Doctor', 'Appointments Count'],
                <% for(String key : docData.keySet()){ %>
                    ['<%= key %>', <%= docData.get(key) %>],
                <% } %>
            ]);

            var docOptions = {
                title: 'Appointments by Doctor',
                pieHole: 0.4,
                chartArea: { width: '90%', height: '80%' },
                legend: { position: 'bottom' }
            };

            var docChart = new google.visualization.PieChart(document.getElementById('docChart'));
            docChart.draw(docData, docOptions);
        }
    </script>
</section>
        

        <section id="doctors">
            <h2>Manage Doctors</h2>
            <form action="doctor" method="post">
                <input type="hidden" name="action" value="add">
                <input type="text" name="name" placeholder="Name" required>
                <input type="text" name="specialization" placeholder="Specialization" required>
                <input type="text" name="contact" placeholder="Contact">
                <input type="submit" value="Add Doctor">
            </form>
            <table>
                <tr><th>ID</th><th>Name</th><th>Specialization</th><th>Contact</th><th>Actions</th></tr>
                <% for(Doctor d : doctorList){ %>
                <tr>
                    <td><%= d.getDoctorId() %></td>
                    <td><%= d.getName() %></td>
                    <td><%= d.getSpecialization() %></td>
                    <td><%= d.getContactNumber() %></td>
                    <td>
                        <a href="edit_doctor.jsp?doctorId=<%= d.getDoctorId() %>" class="edit-btn">Edit</a>
                        <form action="doctor" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="doctorId" value="<%= d.getDoctorId() %>">
                            <input type="submit" value="Delete" style="background:#ff0000; color:white; border:none; padding:10px 10px; border-radius:5px; cursor:pointer;" />
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </section>

        <section id="patients">
            <h2>All Appointments</h2>
            <table>
                <tr>
                    <th>ID</th><th>Department</th><th>Patient</th><th>Email</th><th>Mobile</th><th>Date</th><th>Doctor</th><th>Action</th>
                </tr>
                <% for(Appointment a : appointmentList){ %>
                <tr>
                    <td><%= a.getAppointmentId() %></td>
                    <td><%= a.getDepartment() %></td>
                    <td><%= a.getPatientName() %></td>
                    <td><%= a.getEmail() %></td>
                    <td><%= a.getMobile() %></td>
                    <td><%= a.getAppointmentDate() %></td>
                    <td><%= a.getDoctorName() %></td>
                    <td>
                        <form action="AdminController" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">
                            <input type="submit" value="Delete" style="background:#ff0000; color:white; border:none; padding:10px 10px; border-radius:5px; cursor:pointer;" />
                            
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </section>
    </main>
</div>
</body>
</html>
