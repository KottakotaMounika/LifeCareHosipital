package dao;

import java.sql.*;
import java.util.*;
import model.Admin;
import model.Appointment;
import util.DBConnection;

public class AdminDAO {

    // ✅ Check admin login credentials
    public boolean checkLogin(String username, String password) {
        boolean status = false;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM admin WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // ✅ Get all appointments (patients)
    public List<Appointment> getAllPatients() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a " +
                     "LEFT JOIN doctor_details d ON a.doctor_id = d.doctor_id ORDER BY a.appointment_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setDepartment(rs.getString("department"));
                a.setPatientName(rs.getString("patient_name"));
                a.setEmail(rs.getString("email"));
                a.setMobile(rs.getString("mobile"));
                a.setAppointmentDate(rs.getDate("appointment_date"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setDoctorName(rs.getString("doctor_name"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Delete patient (appointment)
    public boolean deletePatient(int appointmentId) {
        String sql = "DELETE FROM appointment_details WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, appointmentId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Add new appointment
    public boolean addPatient(Appointment a) {
        String sql = "INSERT INTO appointment_details " +
                     "(appointment_id, department, patient_name, email, mobile, appointment_date, doctor_id) " +
                     "VALUES (appointment_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, a.getDepartment());
            pst.setString(2, a.getPatientName());
            pst.setString(3, a.getEmail());
            pst.setString(4, a.getMobile());
            pst.setDate(5, a.getAppointmentDate());
            pst.setInt(6, a.getDoctorId());
            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Dashboard Statistics Methods
    public int getTotalDoctors() {
        return getCount("SELECT COUNT(*) FROM doctor_details");
    }

    // ✅ Updated logic — count unique patients by name & email
 // ✅ Count only users with role='user'
    public int getTotalUsers() {
        return getCount("SELECT COUNT(*) FROM users WHERE role = 'user'");
    }

    // ✅ Count distinct patients (by name & email)
    public int getTotalPatients() {
        String sql = "SELECT COUNT(DISTINCT patient_name || '-' || email) FROM appointment_details";
        return getCount(sql);
    }


    public int getTotalAppointments() {
        return getCount("SELECT COUNT(*) FROM appointment_details");
    }

    private int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET department=?, patient_name=?, email=?, mobile=?, appointment_date=?, doctor_id=? WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getDepartment());
            ps.setString(2, a.getPatientName());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getMobile());
            ps.setDate(5, a.getAppointmentDate());
            ps.setInt(6, a.getDoctorId());
            ps.setInt(7, a.getAppointmentId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    // ✅ Appointments per Doctor (for chart)
    public Map<String, Integer> getAppointmentsByDoctor() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT d.doctor_name, COUNT(a.appointment_id) AS total " +
                     "FROM appointment_details a " +
                     "LEFT JOIN doctor_details d ON a.doctor_id = d.doctor_id " +
                     "GROUP BY d.doctor_name ORDER BY total DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("doctor_name"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // ✅ Appointments per Department (for chart)
    public Map<String, Integer> getAppointmentsByDepartment() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT department, COUNT(*) AS total FROM appointment_details GROUP BY department ORDER BY total DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("department"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
