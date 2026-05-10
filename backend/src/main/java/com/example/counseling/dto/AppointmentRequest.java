package com.example.counseling.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AppointmentRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "老师ID不能为空")
    private Long teacherId;
    @NotBlank(message = "预约时间不能为空")
    private String appointmentTime;
    private String note;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
