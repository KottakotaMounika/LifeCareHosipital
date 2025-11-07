<!DOCTYPE html>
<html>
<head>
    <title>Add Doctor</title>
</head>
<body>
    <h2>Add New Doctor</h2>
    <form action="doctor" method="post">
        <input type="hidden" name="action" value="add">
        <label>Name:</label>
        <input type="text" name="name" required><br><br>

        <label>Specialization:</label>
        <input type="text" name="specialization" required><br><br>

        <label>Contact:</label>
        <input type="text" name="contact" required><br><br>

        <button type="submit">Add Doctor</button>
    </form>
</body>
</html>
