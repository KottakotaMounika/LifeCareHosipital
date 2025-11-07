package controller;

import dao.DoctorDAO;
import model.Doctor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/doctor", "/editDoctor", "/deleteDoctor"})
public class DoctorServlet extends HttpServlet {
    private DoctorDAO doctorDAO = new DoctorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Handle deleteDoctor
        if ("/deleteDoctor".equals(path)) {
            int id = Integer.parseInt(request.getParameter("id"));
            doctorDAO.deleteDoctor(id);
            response.sendRedirect("adminDashboard");
        }

        // Handle editDoctor
        else if ("/editDoctor".equals(path)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Doctor doctor = doctorDAO.getDoctorById(id);
            request.setAttribute("doctor", doctor);
            request.getRequestDispatcher("edit_doctor.jsp").forward(request, response);
        }

        // Default — show all doctors
        else {
            List<Doctor> list = doctorDAO.getAllDoctors();
            request.setAttribute("doctors", list);
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Add new doctor
        if ("add".equalsIgnoreCase(action)) {
            Doctor d = new Doctor();
            d.setName(request.getParameter("name"));
            d.setSpecialization(request.getParameter("specialization"));
            d.setContactNumber(request.getParameter("contact"));
            doctorDAO.addDoctor(d);
        }

        // Update doctor info
        else if ("update".equalsIgnoreCase(action)) {
            Doctor d = new Doctor();
            d.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
            d.setName(request.getParameter("name"));
            d.setSpecialization(request.getParameter("specialization"));
            d.setContactNumber(request.getParameter("contact"));
            doctorDAO.updateDoctor(d);
        }

        // Delete doctor
        else if ("delete".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("doctorId"));
            doctorDAO.deleteDoctor(id);
        }

        response.sendRedirect("adminDashboard");
    }
}
