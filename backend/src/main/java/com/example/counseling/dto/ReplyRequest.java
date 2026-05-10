package com.example.counseling.dto;

import javax.validation.constraints.NotBlank;

public class ReplyRequest {
    @NotBlank(message = "回复内容不能为空")
    private String replyContent;

    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }
}
