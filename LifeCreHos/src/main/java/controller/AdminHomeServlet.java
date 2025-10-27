package controller;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.User;

@WebServlet("/adminHome")
public class AdminHomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            if("admin".equals(user.getRole())) {
                response.setContentType("text/html");
                response.getWriter().println("<h2>Welcome Admin, " + user.getName() + "!</h2>");
                response.getWriter().println("<a href='logout'>Logout</a>");
            } else {
                response.sendRedirect("login.html");
            }
        } else {
            response.sendRedirect("login.html");
        }
    }
}
