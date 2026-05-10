package com.example.counseling.dto;

public class TestSubmitResponse {
    private Long recordId;
    private Long totalScore;
    private String resultLevel;

    public TestSubmitResponse(Long recordId, Long totalScore, String resultLevel) {
        this.recordId = recordId;
        this.totalScore = totalScore;
        this.resultLevel = resultLevel;
    }

    public Long getRecordId() { return recordId; }
    public Long getTotalScore() { return totalScore; }
    public String getResultLevel() { return resultLevel; }
}
