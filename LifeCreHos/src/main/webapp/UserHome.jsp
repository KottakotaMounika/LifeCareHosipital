
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, dao.AppointmentDAO, dao.OrderDAO, model.Appointment, model.Order, model.User" %>

<%
    // Use the built-in JSP session object instead of declaring a new one
    session = request.getSession(false);

    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    int userId = (Integer) session.getAttribute("userId");
    String username = session.getAttribute("username") != null ? session.getAttribute("username").toString() : "";
    String name = session.getAttribute("name") != null ? session.getAttribute("name").toString() : "";
    String email = session.getAttribute("email") != null ? session.getAttribute("email").toString() : "";

    AppointmentDAO appointmentDAO = new AppointmentDAO();
    List<Appointment> appointments = appointmentDAO.getAppointmentsByUserId(userId);

    OrderDAO orderDAO = new OrderDAO();
    List<Order> orders = orderDAO.getOrdersByUser(userId);

    String msg = (String) session.getAttribute("msg");
    if (msg != null) {
        session.removeAttribute("msg");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - LifeCare Hospital</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        :root{
            --blue:#007BFF;
            --navy:#004aad;
            --muted:#6b7280;
            --bg:#f4f7fb;
            --card:#ffffff;
        }
        body { margin:0; font-family: "Poppins", Arial, sans-serif; background: var(--bg); color:#222; }
        .wrap { display:flex; min-height:100vh; }
        /* sidebar */
        .sidebar {
            width:240px; background: linear-gradient(180deg,var(--blue),var(--navy)); color:white;
            padding:25px 18px; box-shadow: 0 6px 20px rgba(0,0,0,0.08);
            position:fixed; height:100vh;
        }
        .brand { font-size:20px; font-weight:700; letter-spacing:0.4px; margin-bottom:18px; }
        .profile { padding:12px; background: rgba(255,255,255,0.06); border-radius:10px; margin-bottom:18px; }
        .profile p { margin:4px 0; color:#e6f0ff; }
        .nav a { display:block; color:#eaf3ff; padding:10px 12px; text-decoration:none; border-radius:8px; margin-bottom:8px; }
        .nav a:hover { background: rgba(255,255,255,0.06); }
        .logout-btn { display:inline-block; margin-top:12px; padding:8px 12px; background:#ff5b5b; color:white; border-radius:8px; text-decoration:none; }

        /* main */
        .main { margin-left:260px; padding:28px; width:100%; box-sizing:border-box; }
        .header { display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
        .welcome { font-size:20px; font-weight:700; color:#0b2545; }
        .quick-actions { display:flex; gap:12px; flex-wrap:wrap; }
        .action-btn {
            background:var(--card); border-radius:10px; padding:12px 16px; box-shadow:0 6px 18px rgba(14,30,37,0.06);
            display:inline-flex; gap:10px; align-items:center; text-decoration:none; color:var(--navy);
        }
        .cards { display:grid; grid-template-columns: repeat(auto-fit,minmax(220px,1fr)); gap:14px; margin-top:16px; }
        .card { background:var(--card); padding:18px; border-radius:12px; box-shadow:0 6px 18px rgba(14,30,37,0.04); }
        .card h3 { margin:0 0 8px 0; color:var(--navy); }
        .card p { margin:0; font-size:20px; font-weight:700; }

        /* tables */
        .panel { margin-top:18px; display:grid; grid-template-columns: 1fr; gap:14px; }
        table { width:100%; border-collapse:collapse; background:var(--card); border-radius:10px; overflow:hidden; box-shadow:0 6px 18px rgba(14,30,37,0.04); }
        th, td { padding:12px 14px; text-align:left; border-bottom:1px solid #eef2f6; }
        th { background: linear-gradient(90deg,var(--blue),var(--navy)); color:white; }
        tr:hover td { background:#fbfdff; }

        .pill { display:inline-block; padding:6px 10px; border-radius:999px; font-weight:600; font-size:13px; }
        .pill-upcoming { background:#eaf4ff; color:var(--blue); }
        .pill-completed { background:#e8fff0; color:#0da36a; }
        .btn-small { padding:6px 10px; border-radius:7px; text-decoration:none; font-weight:600; background:var(--blue); color:white; }

        .muted { color:var(--muted); }

        /* responsiveness */
        @media (max-width:800px) {
            .sidebar { position:relative; width:100%; height:auto; display:block; }
            .main { margin-left:0; padding:16px; }
        }
    </style>
</head>
<body>
<div class="wrap">
    <aside class="sidebar">
        <div class="brand">LifeCare Hospital</div>
        <div class="profile">
            <p style="font-weight:700;"><%= name != null && !name.isEmpty() ? name : username %></p>
            <p class="muted"><%= email %></p>
        </div>

        <nav class="nav">
            <a href="#dashboard">Dashboard</a>
            <a href="#appointments">My Appointments</a>
            <a href="#orders">My Orders</a>
            <a href="appointment.jsp" class="">Book Appointment</a>
            <a href="medicalshop.html">Medical Shop</a>
            <a href="webpage.html">Home</a>
        </nav>

        <a href="logout" class="logout-btn">Logout</a>
    </aside>

    <main class="main">
        <div class="header">
            <div class="welcome">Welcome back, <%= name != null && !name.isEmpty() ? name : username %> 👋</div>
            <div class="quick-actions">
                <a class="action-btn" href="appointment.jsp">Book Appointment</a>
                <a class="action-btn" href="medicalshop.html">Order Medicine</a>
                
            </div>
        </div>

        <% if (msg != null) { %>
            <div style="padding:12px;background:#e9ffef;border:1px solid #d4f6df;border-radius:8px;margin-bottom:12px;"><%= msg %></div>
        <% } %>

        <div class="cards">
            <div class="card">
                <h3>Total Appointments</h3>
                <p><%= appointments != null ? appointments.size() : 0 %></p>
            </div>
            <div class="card">
                <h3>Total Orders</h3>
                <p><%= orders != null ? orders.size() : 0 %></p>
            </div>
            <div class="card">
                <h3>Upcoming</h3>
                <p>
                    <%
                        // find next upcoming appointment date (simple)
                        String upcoming = "None";
                        if (appointments != null && !appointments.isEmpty()) {
                            java.util.Date now = new java.util.Date();
                            java.util.Date nextDate = null;
                            for (Appointment a : appointments) {
                                if (a.getAppointmentDate() != null) {
                                    java.util.Date d = new java.util.Date(a.getAppointmentDate().getTime());
                                    if (d.after(now) && (nextDate == null || d.before(nextDate))) {
                                        nextDate = d;
                                        upcoming = a.getDepartment() + " on " + a.getAppointmentDate();
                                    }
                                }
                            }
                        }
                        out.print(upcoming);
                    %>
                </p>
            </div>
            <div class="card">
                <h3>Profile</h3>
                <p><%= username %></p>
            </div>
        </div>

        <!-- Appointments panel -->
        <div id="appointments" class="panel">
            <div class="card">
                <h3>My Appointments</h3>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Department</th>
                            <th>Date</th>
                            <th>Doctor</th>
                            <th>Contact</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (appointments == null || appointments.isEmpty()) {
                        %>
                        <tr><td colspan="6" style="text-align:center;padding:18px;">You have no appointments yet.</td></tr>
                        <%
                            } else {
                                for (Appointment a : appointments) {
                        %>
                        <tr>
                            <td><%= a.getAppointmentId() %></td>
                            <td><%= a.getDepartment() %></td>
                            <td><%= a.getAppointmentDate() != null ? a.getAppointmentDate() : "-" %></td>
                            <td><%= a.getDoctorName() != null ? a.getDoctorName() : "Not Assigned" %></td>
                            <td><%= a.getMobile() %></td>
                            <td>
                                <a class="btn-small"  href="editappointment.jsp?appointmentId=<%= a.getAppointmentId() %>">Edit</a>
                                <a class="btn-small" style="background:#ff6b6b;" href="AppointmentController?action=delete&appointmentId=<%= a.getAppointmentId() %>" onclick="return confirm('Delete appointment?');">Delete</a>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Orders panel -->
        <div id="orders" class="panel">
            <div class="card">
                <h3>Order History</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Medicine</th>
                            <th>Qty</th>
                            <th>Price</th>
                            <th>Date</th>
                            <th>Status</th>
                             <th>Action</th>
                            
                            
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (orders == null || orders.isEmpty()) {
                        %>
                        <tr><td colspan="6" style="text-align:center;padding:18px;">No orders yet.</td></tr>
                        <%
                            } else {
                                for (Order o : orders) {
                        %>
                        <tr>
                            <td><%= o.getOrderId() %></td>
                            <td><%= o.getMedicineName() %></td>
                            <td><%= o.getQuantity() %></td>
                            <td>₹<%= o.getPrice() %></td>
                            <td><%= o.getOrderDate() != null ? o.getOrderDate() : "-" %></td>
                            <td><%= o.getStatus() != null ? o.getStatus() : "Pending" %></td>
                             <td>
                <a class="btn-small" style="background:#ff6b6b;"
                   href="order?action=delete&orderId=<%= o.getOrderId() %>"
                   onclick="return confirm('Are you sure you want to delete this order?');">
                   Delete
                </a>
            </td>
                            
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

    </main>
</div>
</body>
</html>
