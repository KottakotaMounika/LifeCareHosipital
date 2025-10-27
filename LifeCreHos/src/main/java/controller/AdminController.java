package controller;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AdminDAO;
import model.Appointment;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        AdminDAO dao = new AdminDAO();

        try {
            switch(action) {

                // 1️⃣ Admin login
                case "login":
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    boolean valid = dao.checkLogin(username, password);

                    if(valid) {
                        HttpSession session = request.getSession();
                        session.setAttribute("adminUser", username);
                        response.sendRedirect("admin_dashboard.jsp");
                    } else {
                        request.setAttribute("errorMsg", "Invalid admin username or password!");
                        request.getRequestDispatcher("admin_login.jsp").forward(request, response);
                    }
                    break;

                // 2️⃣ Delete patient
                case "delete":
                    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                    dao.deletePatient(appointmentId);
                    response.sendRedirect("admin_dashboard.jsp");
                    break;

                // 3️⃣ Add patient
                case "add":
                    String department = request.getParameter("department");
                    String patientName = request.getParameter("patient_name");
                    String email = request.getParameter("email");
                    String mobile = request.getParameter("mobile");
                    Date appointmentDate = Date.valueOf(request.getParameter("appointment_date"));
                    int doctorId = Integer.parseInt(request.getParameter("doctor_id"));

                    Appointment appt = new Appointment();
                    appt.setDepartment(department);
                    appt.setPatientName(patientName);
                    appt.setEmail(email);
                    appt.setMobile(mobile);
                    appt.setAppointmentDate(appointmentDate);
                    appt.setDoctorId(doctorId);

                    dao.addPatient(appt);
                    response.sendRedirect("admin_dashboard.jsp");
                    break;

                default:
                    response.sendRedirect("admin_login.jsp");
                    break;
            }

        } catch(Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Internal server error!");
            request.getRequestDispatcher("admin_login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if(session != null) session.invalidate();
            response.sendRedirect("admin_login.jsp");
        } else {
            response.getWriter().println("AdminController is working!");
        }
    }
}
