package controller;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import util.DBConnection;

@WebServlet("/order")
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        // Comma-separated strings from JS
        String medicineNames = request.getParameter("medicineNames");
        String quantities = request.getParameter("quantities");
        String prices = request.getParameter("prices");

        String[] meds = medicineNames.split(",");
        String[] qtys = quantities.split(",");
        String[] prs = prices.split(",");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO user_orders(user_id, medicine_name, quantity, price, status) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < meds.length; i++) {
                    ps.setInt(1, userId);
                    ps.setString(2, meds[i]);
                    ps.setInt(3, Integer.parseInt(qtys[i]));
                    ps.setDouble(4, Double.parseDouble(prs[i]));
                    ps.setString(5, "Paid"); // Set default status
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error");
        }
    }
}
