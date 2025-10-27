package controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AppointmentDAO;
import model.Appointment;

@WebServlet("/AppointmentController")
public class AppointmentController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String department = request.getParameter("department");
            String patientName = request.getParameter("patientName");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            String appointmentDateStr = request.getParameter("appointmentDate");

            Date appointmentDate = null;
            try {
                appointmentDate = Date.valueOf(appointmentDateStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMsg", "Invalid date format.");
                request.getRequestDispatcher("appointment.jsp").forward(request, response);
                return;
            }

            Appointment appt = new Appointment();
            appt.setDepartment(department);
            appt.setPatientName(patientName);
            appt.setEmail(email);
            appt.setMobile(mobile);
            appt.setAppointmentDate(appointmentDate);

            AppointmentDAO dao = new AppointmentDAO();
            String result = dao.insertAppointment(appt);

            switch (result) {
                case "success":
                    request.setAttribute("successMsg", "Appointment booked successfully! Doctor: " + appt.getDoctorName());
                    break;
                case "duplicate":
                    request.setAttribute("errorMsg", "Duplicate appointment! You already booked for this department and date.");
                    break;
                case "noDoctor":
                    request.setAttribute("errorMsg", "No doctor available for the selected department.");
                    break;
                case "seqError":
                    request.setAttribute("errorMsg", "Appointment sequence missing. Please create appointment_seq in DB.");
                    break;
                default:
                    request.setAttribute("errorMsg", "Error booking appointment. Please check your data or database.");
                    break;
            }

            RequestDispatcher rd = request.getRequestDispatcher("appointment.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Internal server error. Please try again later.");
            request.getRequestDispatcher("appointment.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("AppointmentController is working!");
    }
}
