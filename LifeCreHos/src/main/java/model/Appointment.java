package model;

import java.sql.Date;

public class Appointment {
    private int appointmentId;
    private String department;
    private String patientName;
    private String email;
    private String mobile;
    private Date appointmentDate;
    private int doctorId;
    private String doctorName;
    private int userId;

    public Appointment() {}
    public Appointment(String department, String patientName, String email, String mobile, Date appointmentDate) {
        this.department = department;
        this.patientName = patientName;
        this.email = email;
        this.mobile = mobile;
        this.appointmentDate = appointmentDate;
    }

    // Getters & Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    
}
