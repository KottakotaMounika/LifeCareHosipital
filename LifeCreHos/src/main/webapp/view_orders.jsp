<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User, java.util.List, model.Order, dao.OrderDAO" %>
<%
    User user = (User) session.getAttribute("currentUser");
    if(user == null) {
        response.sendRedirect("login.html");
        return;
    }

    OrderDAO dao = new OrderDAO();
    List<Order> orders = dao.getOrdersByUser(user.getUsername());
%>
<!DOCTYPE html>
<html>
<head>
    <title>Your Orders</title>
    <style>
        body { font-family: Arial; background:#f5f7fa; padding:20px; }
        h2 { text-align:center; color:#003366; }
        table { width:80%; margin:auto; border-collapse: collapse; background:white; box-shadow:0 4px 10px rgba(0,0,0,0.1); border-radius:10px; overflow:hidden; }
        th, td { padding:12px; text-align:center; border-bottom:1px solid #ddd; }
        th { background:#0066ff; color:white; }
        tr:hover { background:#f1f1f1; }
        .center { text-align:center; margin-top:20px; }
    </style>
</head>
<body>
    <h2>Your Orders</h2>
    <%
        if(orders != null && !orders.isEmpty()) {
    %>
    <table>
        <tr>
            <th>Order ID</th>
            <th>Medicine Name</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Status</th>
            <th>Order Date</th>
        </tr>
        <%
            for(Order o : orders) {
        %>
        <tr>
            <td><%=o.getOrderId()%></td>
            <td><%=o.getMedicineName()%></td>
            <td><%=o.getQuantity()%></td>
            <td>₹<%=o.getPrice()%></td>
            <td><%=o.getStatus()%></td>
            <td><%=o.getOrderDate()%></td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        } else {
    %>
    <p class="center">No orders found.</p>
    <%
        }
    %>
    <div class="center">
        <a href="UserHome.jsp">Back to Home</a>
        <a href="medicalshop.html">Go to Medical Shop</a>
    </div>
</body>
</html>
