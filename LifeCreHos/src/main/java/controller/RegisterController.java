package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;
import model.User;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Check if passwords match
        if(!password.equals(confirmPassword)) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Registration Failed</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f9ff; }");
            out.println(".container { max-width: 400px; margin: 100px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,80,0.1); text-align: center; }");
            out.println("h3 { color: #FF4D4D; margin-bottom: 20px; }");
            out.println("a { display: inline-block; padding: 10px 20px; background: #007BFF; color: white; text-decoration: none; border-radius: 6px; }");
            out.println("a:hover { background: #0056b3; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h3>Passwords do not match. Try Again!</h3>");
            out.println("<a href='register.html'>Go Back</a>");
            out.println("</div></body></html>");
            return; // STOP execution here
        }

        // 2. Create user object
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

     // 3. Register user
        boolean registered = userDAO.registerUser(user);

        if (registered) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Registration Success</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f9ff; }");
            out.println(".container { max-width: 400px; margin: 100px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,80,0.1); text-align: center; }");
            out.println("h3 { color: #28a745; margin-bottom: 20px; }"); // green for success
            out.println("a { display: inline-block; padding: 10px 20px; background: #007BFF; color: white; text-decoration: none; border-radius: 6px; }");
            out.println("a:hover { background: #0056b3; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h3>Registered successfully! <a href='login.html'>Go to Login</a></h3>");
            out.println("</div></body></html>");
        } else {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Registration Failed</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #f5f9ff; }");
            out.println(".container { max-width: 400px; margin: 100px auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,80,0.1); text-align: center; }");
            out.println("h3 { color: #FF4D4D; margin-bottom: 20px; }"); // red for error
            out.println("a { display: inline-block; padding: 10px 20px; background: #007BFF; color: white; text-decoration: none; border-radius: 6px; }");
            out.println("a:hover { background: #0056b3; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h3>User already exists or error! <a href='register.html'>Try again</a></h3>");
            out.println("</div></body></html>");
        }

    }
}
