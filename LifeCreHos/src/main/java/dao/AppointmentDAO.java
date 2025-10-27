package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;
import util.DBConnection;

public class AppointmentDAO {

    public String insertAppointment(Appointment a) {
        String checkSql = "SELECT COUNT(*) FROM appointment_details WHERE patient_name=? AND department=? AND email=? AND mobile=? AND appointment_date=?";
        String doctorSql = "SELECT doctor_id, doctor_name FROM doctor_details WHERE specialization=? AND ROWNUM = 1";
        String insertSql = "INSERT INTO appointment_details (appointment_id, department, patient_name, email, mobile, appointment_date, doctor_id) "
                         + "VALUES (appointment_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Duplicate check
            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setString(1, a.getPatientName());
                checkPst.setString(2, a.getDepartment());
                checkPst.setString(3, a.getEmail());
                checkPst.setString(4, a.getMobile());
                checkPst.setDate(5, a.getAppointmentDate());
                ResultSet rs = checkPst.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) return "duplicate";
            }

            // Assign doctor
            int doctorId = 0;
            String doctorName = "";
            try (PreparedStatement pstDoc = conn.prepareStatement(doctorSql)) {
                pstDoc.setString(1, a.getDepartment());
                ResultSet rsDoc = pstDoc.executeQuery();
                if (rsDoc.next()) {
                    doctorId = rsDoc.getInt("doctor_id");
                    doctorName = rsDoc.getString("doctor_name");
                } else {
                    return "noDoctor";
                }
            }

            // Insert appointment
            try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                pst.setString(1, a.getDepartment());
                pst.setString(2, a.getPatientName());
                pst.setString(3, a.getEmail());
                pst.setString(4, a.getMobile());
                pst.setDate(5, a.getAppointmentDate());
                pst.setInt(6, doctorId);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    a.setDoctorId(doctorId);
                    a.setDoctorName(doctorName);
                    return "success";
                } else return "error";
            } catch (SQLException e) {
                if (e.getErrorCode() == 2289) { // ORA-02289: sequence does not exist
                    return "seqError";
                }
                e.printStackTrace();
                return "error";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.doctor_name FROM appointment_details a LEFT JOIN doctor_details d ON a.doctor_id=d.doctor_id ORDER BY a.appointment_id";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()) {

            while(rs.next()) {
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

        } catch(SQLException e) { e.printStackTrace(); }
        return list;
    }
}
