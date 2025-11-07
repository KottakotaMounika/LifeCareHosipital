package controller;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AppointmentDAO;
import model.Appointment;

@WebServlet("/AppointmentController")
public class AppointmentController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.html");
            return;
        }

        boolean isAdmin = session.getAttribute("adminId") != null;
        Integer userId = (Integer) session.getAttribute("userId");

        String action = request.getParameter("action");
        AppointmentDAO dao = new AppointmentDAO();

        if ("edit".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("appointmentId");
            try {
                int appointmentId = Integer.parseInt(idStr);
                Appointment appt;

                if (isAdmin) {
                    appt = dao.getAppointmentById(appointmentId);
                } else {
                    appt = dao.getAppointmentById(appointmentId, userId);
                }

                if (appt != null) {
                    request.setAttribute("appointment", appt);
                    request.getRequestDispatcher("edit_appointment.jsp").forward(request, response);
                } else {
                    session.setAttribute("msg", "Appointment not found or unauthorized access.");
                    response.sendRedirect(isAdmin ? "adminDashboard" : "UserHome.jsp");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("msg", "Invalid appointment ID.");
                response.sendRedirect(isAdmin ? "adminDashboard" : "UserHome.jsp");
            }
            return;
        }

        if ("delete".equalsIgnoreCase(action)) {
            try {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                boolean deleted = dao.deleteAppointment(appointmentId);
                session.setAttribute("msg", deleted ? "Deleted successfully." : "Failed to delete.");
                response.sendRedirect(isAdmin ? "adminDashboard" : "UserHome.jsp");
            } catch (Exception e) {
                session.setAttribute("msg", "Error deleting appointment.");
                response.sendRedirect(isAdmin ? "adminDashboard" : "UserHome.jsp");
            }
            return;
        }

        response.sendRedirect(isAdmin ? "adminDashboard" : "UserHome.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("update".equalsIgnoreCase(action)) {
            handleUpdate(request, response);
        } else {
            handleBooking(request, response);
        }
    }

    // ✅ Handle Booking (User only)
    private void handleBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("login.html");
                return;
            }

            int userId = (Integer) session.getAttribute("userId");
            String department = request.getParameter("department");
            String patientName = request.getParameter("patientName");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));

            Appointment a = new Appointment();
            a.setDepartment(department);
            a.setPatientName(patientName);
            a.setEmail(email);
            a.setMobile(mobile);
            a.setAppointmentDate(appointmentDate);
            a.setUserId(userId);

            AppointmentDAO dao = new AppointmentDAO();
            String result = dao.insertAppointment(a);

            if ("success".equals(result)) {
                request.setAttribute("successMsg", "Appointment booked successfully!");
            } else if ("duplicate".equals(result)) {
                request.setAttribute("errorMsg", "Appointment already exists!");
            } else if ("noDoctor".equals(result)) {
                request.setAttribute("errorMsg", "No doctor available for the selected department!");
            } else {
                request.setAttribute("errorMsg", "Error booking appointment. Please try again!");
            }

            request.getRequestDispatcher("appointment.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Internal error occurred!");
            request.getRequestDispatcher("appointment.jsp").forward(request, response);
        }
    }

    // ✅ Handle Update (Fixed session + redirect logic)
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Don’t recreate session — just use existing
            HttpSession session = request.getSession(false);

            // If session missing, show message instead of redirecting to login
            if (session == null) {
                request.setAttribute("errorMsg", "Session expired. Please log in again.");
                request.getRequestDispatcher("edit_appointment.jsp").forward(request, response);
                return;
            }

            boolean isAdmin = session.getAttribute("adminId") != null;
            Integer userId = (Integer) session.getAttribute("userId");

            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String department = request.getParameter("department");
            String patientName = request.getParameter("patientName");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));

            Appointment a = new Appointment();
            a.setAppointmentId(appointmentId);
            a.setDepartment(department);
            a.setPatientName(patientName);
            a.setEmail(email);
            a.setMobile(mobile);
            a.setAppointmentDate(appointmentDate);
            if (userId != null) a.setUserId(userId);

            AppointmentDAO dao = new AppointmentDAO();
            boolean ok = dao.updateAppointment(a);

            // ✅ Perfect redirect
            session.setAttribute("msg", ok ? "Updated successfully!" : "Failed to update!");
            if (isAdmin) {
                response.sendRedirect("adminDashboard");
            } else {
                response.sendRedirect("UserHome.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Internal error while updating.");
            request.getRequestDispatcher("edit_appointment.jsp").forward(request, response);
        }
    }
}
