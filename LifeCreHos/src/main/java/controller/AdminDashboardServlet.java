package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AdminDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import model.Appointment;
import model.Doctor;
import model.User;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AdminDAO adminDAO;
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
        doctorDAO = new DoctorDAO();
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // ✅ Check session
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User admin = (User) session.getAttribute("currentUser");
        if (admin == null || !"admin".equalsIgnoreCase(admin.getRole())) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // ✅ Load dashboard details
            int totalDoctors = doctorDAO.getTotalDoctors();
            int totalAppointments = adminDAO.getTotalAppointments();
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            List<Appointment> appointments = appointmentDAO.getAllAppointments();

            // ✅ Set attributes
            request.setAttribute("adminName", admin.getUsername());
            request.setAttribute("totalDoctors", totalDoctors);
            request.setAttribute("totalAppointments", totalAppointments);
            request.setAttribute("doctors", doctors);
            request.setAttribute("appointments", appointments);

            // ✅ Forward to JSP
            RequestDispatcher rd = request.getRequestDispatcher("admin_dashboard.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Error loading admin dashboard: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("login.html");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
