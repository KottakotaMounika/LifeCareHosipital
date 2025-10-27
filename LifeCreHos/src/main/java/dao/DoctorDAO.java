package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;
import util.DBConnection;

public class DoctorDAO {

    // List all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY doctor_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setContactNumber(rs.getString("contact_number"));
                doctors.add(d);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Add a doctor
    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors(name, specialization, contact_number) VALUES(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getContactNumber());
            int i = ps.executeUpdate();
            return i > 0;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a doctor by ID
    public boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            int i = ps.executeUpdate();
            return i > 0;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
