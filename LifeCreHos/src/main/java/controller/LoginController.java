package controller;
import java.io.IOException;
import java.io.PrintWriter;

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

        User user = userDAO.validateUser(username, password, role);
        if(user != null) {
            HttpSession session = request.getSession();
           session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            
            if("admin".equalsIgnoreCase(user.getRole())) {
            	response.sendRedirect("adminDashboard");
 // THIS CALLS THE SERVLET
            } else {
                response.sendRedirect("UserHome.jsp");
            }

        } else {
            // Login failed
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Login Failed</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f9ff; }");
            out.println(".container { max-width: 400px; margin: 100px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,80,0.1); text-align: center; }");
            out.println("h3 { color: #FF4D4D; margin-bottom: 20px; }");
            out.println("a { display: inline-block; padding: 10px 20px; background: #007BFF; color: white; text-decoration: none; border-radius: 6px; }");
            out.println("a:hover { background: #0056b3; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h3>Invalid username, password, or role.</h3>");
            out.println("<a href='login.html'>Try Again</a>");
            out.println("</div></body></html>");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.html");
    }
}
