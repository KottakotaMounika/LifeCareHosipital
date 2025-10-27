package controller;

import dao.DoctorDAO;
import model.Doctor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {
    private DoctorDAO doctorDAO = new DoctorDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if("add".equalsIgnoreCase(action)) {
            Doctor d = new Doctor();
            d.setName(request.getParameter("name"));
            d.setSpecialization(request.getParameter("specialization"));
            d.setContactNumber(request.getParameter("contact"));
            doctorDAO.addDoctor(d);
        } else if("delete".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("doctorId"));
            doctorDAO.deleteDoctor(id);
        }

        response.sendRedirect("adminDashboard");
    }
}
