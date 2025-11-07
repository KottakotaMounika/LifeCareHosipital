package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import model.User;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("LOGIN ATTEMPT -> username: " + username + " | password: " + password + " | role: " + role);

        User user = null;
        try {
            user = userDAO.validateUser(username, password, role);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("validateUser returned -> " + (user == null ? "null" : ("id=" + user.getId() + ", role=" + user.getRole())));

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            System.out.println("Session set: userId=" + user.getId() + " username=" + user.getUsername() + " role=" + user.getRole());

            if ("admin".equalsIgnoreCase(user.getRole())) {
                System.out.println("Redirecting to admin_dashboard.jsp");
                response.sendRedirect("adminDashboard");
            } else {
                System.out.println("Redirecting to user_home.jsp");
                response.sendRedirect("UserHome.jsp");
            }
        }else {
            System.out.println("Login failed -> redirecting back to login.html");
            response.sendRedirect("login.html?error=" + java.net.URLEncoder.encode("Invalid username, password, or role!", "UTF-8"));
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.html");
    }
}
