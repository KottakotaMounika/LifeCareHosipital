package dao;

import java.sql.*;
import java.util.*;
import model.Appointment;
import util.DBConnection;

public class AppointmentDAO {

    // ✅ 1. Insert appointment
    public String insertAppointment(Appointment a) {
        String checkSql = "SELECT COUNT(*) FROM appointment_details WHERE patient_name=? AND department=? AND email=? AND mobile=? AND appointment_date=?";
        String doctorSql = "SELECT doctor_id, doctor_name FROM doctor_details WHERE specialization=? AND ROWNUM = 1";
        String insertSql = "INSERT INTO appointment_details (appointment_id, department, patient_name, email, mobile, appointment_date, doctor_id, user_id) "
                + "VALUES (appointment_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Duplicate check
            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setString(1, a.getPatientName());
                checkPst.setString(2, a.getDepartment());
                checkPst.setString(3, a.getEmail());
                checkPst.setString(4, a.getMobile());
                checkPst.setDate(5, a.getAppointmentDate());
                try (ResultSet rs = checkPst.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return "duplicate";
                }
            }

            // Assign doctor
            int doctorId = 0;
            String doctorName = "";
            try (PreparedStatement pstDoc = conn.prepareStatement(doctorSql)) {
                pstDoc.setString(1, a.getDepartment());
                try (ResultSet rsDoc = pstDoc.executeQuery()) {
                    if (rsDoc.next()) {
                        doctorId = rsDoc.getInt("doctor_id");
                        doctorName = rsDoc.getString("doctor_name");
                    } else {
                        return "noDoctor";
                    }
                }
            }

            // Insert
            try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                pst.setString(1, a.getDepartment());
                pst.setString(2, a.getPatientName());
                pst.setString(3, a.getEmail());
                pst.setString(4, a.getMobile());
                pst.setDate(5, a.getAppointmentDate());
                pst.setInt(6, doctorId);
                pst.setInt(7, a.getUserId());

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    a.setDoctorId(doctorId);
                    a.setDoctorName(doctorName);
                    return "success";
                } else {
                    return "error";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    // ✅ 2. Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a LEFT JOIN doctor_details d ON a.doctor_id=d.doctor_id ORDER BY a.appointment_id";
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
                a.setUserId(rs.getInt("user_id"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ 3. Get appointments by userId
    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a LEFT JOIN doctor_details d ON a.doctor_id=d.doctor_id WHERE a.user_id=? ORDER BY a.appointment_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
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
                    a.setUserId(rs.getInt("user_id"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ 4. Get single appointment (for user)
    public Appointment getAppointmentById(int appointmentId, int userId) {
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a LEFT JOIN doctor_details d ON a.doctor_id=d.doctor_id WHERE a.appointment_id=? AND a.user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, appointmentId);
            pst.setInt(2, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentId(rs.getInt("appointment_id"));
                    a.setDepartment(rs.getString("department"));
                    a.setPatientName(rs.getString("patient_name"));
                    a.setEmail(rs.getString("email"));
                    a.setMobile(rs.getString("mobile"));
                    a.setAppointmentDate(rs.getDate("appointment_date"));
                    a.setDoctorId(rs.getInt("doctor_id"));
                    a.setDoctorName(rs.getString("doctor_name"));
                    a.setUserId(rs.getInt("user_id"));
                    return a;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ 5. Get single appointment (for admin)
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a LEFT JOIN doctor_details d ON a.doctor_id=d.doctor_id WHERE a.appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, appointmentId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentId(rs.getInt("appointment_id"));
                    a.setDepartment(rs.getString("department"));
                    a.setPatientName(rs.getString("patient_name"));
                    a.setEmail(rs.getString("email"));
                    a.setMobile(rs.getString("mobile"));
                    a.setAppointmentDate(rs.getDate("appointment_date"));
                    a.setDoctorId(rs.getInt("doctor_id"));
                    a.setDoctorName(rs.getString("doctor_name"));
                    a.setUserId(rs.getInt("user_id"));
                    return a;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ 6. Update
    public boolean updateAppointment(Appointment a) {
        String sql = "UPDATE appointment_details SET department=?, patient_name=?, email=?, mobile=?, appointment_date=? WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, a.getDepartment());
            pst.setString(2, a.getPatientName());
            pst.setString(3, a.getEmail());
            pst.setString(4, a.getMobile());
            pst.setDate(5, a.getAppointmentDate());
            pst.setInt(6, a.getAppointmentId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 7. Delete
    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointment_details WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, appointmentId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
