package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.Appointment;
import util.DBConnection;

public class AdminDAO {

    // Check admin login credentials
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT COUNT(*) FROM admin_users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all patients/daughters (appointments)
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

    // Delete patient by appointment_id
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

    // Add new patient/daughter
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
            pst.setInt(6, a.getDoctorId()); // Doctor must be selected beforehand
            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
