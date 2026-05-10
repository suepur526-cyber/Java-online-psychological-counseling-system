package com.example.counseling.model;

public class Appointment {
    private Long id;
    private String appointmentNo;
    private Long teacherId;
    private String jobNo;
    private String teacherName;
    private Long userId;
    private String studentNo;
    private String studentName;
    private String fee;
    private String workingTime;
    private String appointmentTime;
    private String note;
    private String status;
    private String reviewReply;
    private String isPaid;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppointmentNo() { return appointmentNo; }
    public void setAppointmentNo(String appointmentNo) { this.appointmentNo = appointmentNo; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getJobNo() { return jobNo; }
    public void setJobNo(String jobNo) { this.jobNo = jobNo; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getFee() { return fee; }
    public void setFee(String fee) { this.fee = fee; }
    public String getWorkingTime() { return workingTime; }
    public void setWorkingTime(String workingTime) { this.workingTime = workingTime; }
    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReviewReply() { return reviewReply; }
    public void setReviewReply(String reviewReply) { this.reviewReply = reviewReply; }
    public String getIsPaid() { return isPaid; }
    public void setIsPaid(String isPaid) { this.isPaid = isPaid; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
