package com.example.counseling.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ConsultationRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "老师ID不能为空")
    private Long teacherId;
    @NotBlank(message = "咨询内容不能为空")
    private String content;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
