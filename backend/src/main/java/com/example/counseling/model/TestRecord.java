package com.example.counseling.model;

public class TestRecord {
    private Long id;
    private Long userId;
    private String username;
    private Long testId;
    private String testName;
    private Long totalScore;
    private String resultLevel;
    private String answersJson;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public Long getTotalScore() { return totalScore; }
    public void setTotalScore(Long totalScore) { this.totalScore = totalScore; }
    public String getResultLevel() { return resultLevel; }
    public void setResultLevel(String resultLevel) { this.resultLevel = resultLevel; }
    public String getAnswersJson() { return answersJson; }
    public void setAnswersJson(String answersJson) { this.answersJson = answersJson; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
