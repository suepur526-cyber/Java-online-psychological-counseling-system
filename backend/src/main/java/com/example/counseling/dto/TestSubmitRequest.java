package com.example.counseling.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class TestSubmitRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotEmpty(message = "答案不能为空")
    private Map<Long, String> answers;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}
