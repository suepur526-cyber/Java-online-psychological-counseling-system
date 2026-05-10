package com.example.counseling.dto;

import javax.validation.constraints.NotBlank;

public class ReviewRequest {
    @NotBlank(message = "审核状态不能为空")
    private String status;
    private String reviewReply;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReviewReply() { return reviewReply; }
    public void setReviewReply(String reviewReply) { this.reviewReply = reviewReply; }
}
