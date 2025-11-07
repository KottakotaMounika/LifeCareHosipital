package controller;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AdminDAO;
import dao.AppointmentDAO;
import model.Appointment;
import model.User;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        AdminDAO adminDAO = new AdminDAO();

        try {
            switch (action) {

                // ✅ 1. Admin login
                case "login":
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    boolean valid = adminDAO.checkLogin(username, password);

                    if (valid) {
                        HttpSession session = request.getSession();
                        // store username properly for JSP
                        User adminUser = new User();
                        adminUser.setUsername(username);
                        adminUser.setRole("admin");
                        session.setAttribute("currentUser", adminUser);

                        response.sendRedirect("adminDashboard");
                    } else {
                        request.setAttribute("errorMsg", "Invalid admin username or password!");
                        request.getRequestDispatcher("login.html").forward(request, response);
                    }
                    break;

                // ✅ 2. Delete appointment
                case "delete":
                    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                    AppointmentDAO deleteDAO = new AppointmentDAO();
                    deleteDAO.deleteAppointment(appointmentId);
                    response.sendRedirect("adminDashboard");
                    break;

                // ✅ 3. Add appointment
                case "add":
                    String department = request.getParameter("department");
                    String patientName = request.getParameter("patient_name");
                    String email = request.getParameter("email");
                    String mobile = request.getParameter("mobile");
                    Date appointmentDate = Date.valueOf(request.getParameter("appointment_date"));
                    int doctorId = Integer.parseInt(request.getParameter("doctor_id"));

                    Appointment newAppt = new Appointment();
                    newAppt.setDepartment(department);
                    newAppt.setPatientName(patientName);
                    newAppt.setEmail(email);
                    newAppt.setMobile(mobile);
                    newAppt.setAppointmentDate(appointmentDate);
                    newAppt.setDoctorId(doctorId);

                    AppointmentDAO addDAO = new AppointmentDAO();
                    addDAO.insertAppointment(newAppt);
                    response.sendRedirect("adminDashboard");
                    break;

                // ✅ 4. Update appointment
                case "updateAppointment":
                    HttpSession sess = request.getSession(false);
                    if (sess == null || sess.getAttribute("currentUser") == null) {
                        response.sendRedirect("login.html");
                        return;
                    }

                    int id = Integer.parseInt(request.getParameter("appointmentId"));
                    String dept = request.getParameter("department");
                    String patient = request.getParameter("patientName");
                    String mail = request.getParameter("email");
                    String phone = request.getParameter("mobile");
                    Date date = Date.valueOf(request.getParameter("appointmentDate"));

                    Appointment updated = new Appointment();
                    updated.setAppointmentId(id);
                    updated.setDepartment(dept);
                    updated.setPatientName(patient);
                    updated.setEmail(mail);
                    updated.setMobile(phone);
                    updated.setAppointmentDate(date);

                    AppointmentDAO appDAO = new AppointmentDAO();
                    boolean success = appDAO.updateAppointment(updated);

                    if (success)
                        response.sendRedirect("adminDashboard");
                    else
                        response.getWriter().println("❌ Update failed!");
                    break;

                default:
                    response.sendRedirect("login.html");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "Internal server error!");
            request.getRequestDispatcher("login.html").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.invalidate();
            response.sendRedirect("login.html");
        } else {
            response.getWriter().println("AdminController is working!");
        }
    }
}
