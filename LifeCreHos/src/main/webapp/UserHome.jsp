<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*, java.util.*, dao.OrderDAO, model.Order" %>
<%
    // Check session
    javax.servlet.http.HttpSession sess = request.getSession(false);
    if(sess == null || sess.getAttribute("userId") == null){
        response.sendRedirect("login.html");
        return;
    }
    int userId = (Integer) sess.getAttribute("userId");
    String username = (sess.getAttribute("username") != null) ? sess.getAttribute("username").toString() : "User";

    // Fetch orders
    OrderDAO orderDAO = new OrderDAO();
    List<Order> orders = orderDAO.getOrdersByUser(userId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Home - Lifecare</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f7fa; padding: 20px; }
        h2 { color: #003366; text-align: center; }
        table { width: 80%; margin: 20px auto; border-collapse: collapse; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0px 4px 10px rgba(0,0,0,0.1); }
        th, td { padding: 12px; border-bottom: 1px solid #ddd; text-align: center; }
        th { background-color: #0066ff; color: white; }
        tr:hover { background-color: #f1f1f1; }
        .btn { display: block; width: 200px; margin: 20px auto; padding: 12px; background-color: #0066ff; color: white; text-align: center; border-radius: 20px; text-decoration: none; transition: background 0.3s; }
        .btn:hover { background-color: #004db3; }
        .empty { text-align:center; padding:20px; color:#666; }

        /* Logout button style */
        .logout {
            display: block;
            width: 150px;
            margin: 30px auto;
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            text-align: center;
            border-radius: 25px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s, transform 0.2s;
        }
        .logout:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }
    </style>
</head>
<body>

<h2>Welcome, <%= username %></h2>

<a href="medicalshop.html" class="btn">Go to Medical Shop</a>

<h2>Your Order History</h2>

<table>
    <tr>
        <th>Order ID</th>
        <th>Medicine Name</th>
        <th>Quantity</th>
        <th>Price</th>
        <th>Order Date</th>
        <th>Status</th>
    </tr>
    <%
        if(orders.isEmpty()){
    %>
    <tr>
        <td class="empty" colspan="6">You have no orders yet.</td>
    </tr>
    <%
        } else {
            for(Order o : orders){
    %>
    <tr>
        <td><%= o.getOrderId() %></td>
        <td><%= o.getMedicineName() %></td>
        <td><%= o.getQuantity() %></td>
        <td>₹<%= o.getPrice() %></td>
        <td><%= o.getOrderDate() != null ? o.getOrderDate() : "" %></td>
        <td><%= o.getStatus() != null ? o.getStatus() : "Pending" %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<a href="logout" class="logout">Logout</a>

</body>
</html>
