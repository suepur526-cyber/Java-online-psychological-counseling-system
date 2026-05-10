package com.example.counseling.model;

public class ServiceInfo {
    private Long id;
    private Long teacherId;
    private String jobNo;
    private String teacherName;
    private String gender;
    private String photo;
    private String fee;
    private String workingTime;
    private String teachingYears;
    private String introduction;
    private String honor;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getJobNo() { return jobNo; }
    public void setJobNo(String jobNo) { this.jobNo = jobNo; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public String getFee() { return fee; }
    public void setFee(String fee) { this.fee = fee; }
    public String getWorkingTime() { return workingTime; }
    public void setWorkingTime(String workingTime) { this.workingTime = workingTime; }
    public String getTeachingYears() { return teachingYears; }
    public void setTeachingYears(String teachingYears) { this.teachingYears = teachingYears; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getHonor() { return honor; }
    public void setHonor(String honor) { this.honor = honor; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
