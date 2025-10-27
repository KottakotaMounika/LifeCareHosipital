package controller;

import dao.DoctorDAO;
import model.Doctor;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private DoctorDAO doctorDAO = new DoctorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("currentUser");
        if(!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.html");
            return;
        }

        List<Doctor> doctors = doctorDAO.getAllDoctors();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Admin Dashboard</title>");
        out.println("<style>");
        out.println("body {font-family: Arial, sans-serif; background: #f5f9ff;}");
        out.println(".container {max-width: 900px; margin: 30px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,80,0.1);}");
        out.println("h2 {color: #007BFF; text-align: center;}");
        out.println("table {width: 100%; border-collapse: collapse; margin-top: 20px;}");
        out.println("th, td {border: 1px solid #ccc; padding: 12px; text-align: left;}");
        out.println("th {background: #007BFF; color: white;}");
        out.println("input, select {padding: 8px; margin: 5px 0; border-radius: 6px; border: 1px solid #ccc;}");
        out.println("input[type='submit'] {background: #007BFF; color: white; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer;}");
        out.println("input[type='submit']:hover {background: #0056b3;}");
        out.println(".delete-btn {background: #FF4D4D; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer;}");
        out.println(".delete-btn:hover {background: #cc0000;}");
        out.println(".logout {display: inline-block; margin-top: 20px; padding: 10px 25px; background: #007BFF; color: white; border-radius: 6px; text-decoration: none;}");
        out.println(".logout:hover {background: #0056b3;}");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h2>Admin Dashboard</h2>");

        // Add Doctor Form
        out.println("<h3>Add Doctor</h3>");
        out.println("<form action='doctor' method='post'>");
        out.println("<input type='hidden' name='action' value='add'>");
        out.println("Name: <input type='text' name='name' required>");
        out.println("Specialization: <input type='text' name='specialization' required>");
        out.println("Contact: <input type='text' name='contact'>");
        out.println("<input type='submit' value='Add Doctor'>");
        out.println("</form>");

        // Doctor Table
        out.println("<h3>All Doctors</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Name</th><th>Specialization</th><th>Contact</th><th>Action</th></tr>");
        for(Doctor d : doctors) {
            out.println("<tr>");
            out.println("<td>" + d.getDoctorId() + "</td>");
            out.println("<td>" + d.getName() + "</td>");
            out.println("<td>" + d.getSpecialization() + "</td>");
            out.println("<td>" + d.getContactNumber() + "</td>");
            out.println("<td>");
            out.println("<form action='doctor' method='post' style='display:inline;'>");
            out.println("<input type='hidden' name='action' value='delete'>");
            out.println("<input type='hidden' name='doctorId' value='" + d.getDoctorId() + "'>");
            out.println("<input type='submit' value='Delete' class='delete-btn'>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("<a href='logout' class='logout'>Logout</a>");
        out.println("</div></body></html>");
    }
}
