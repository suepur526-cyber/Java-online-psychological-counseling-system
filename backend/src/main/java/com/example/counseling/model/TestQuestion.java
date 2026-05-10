package com.example.counseling.model;

public class TestQuestion {
    private Long id;
    private Long testId;
    private String questionName;
    private String optionsJson;
    private Long score;
    private String answer;
    private String analysis;
    private String type;
    private Long sequenceNo;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }
    public String getQuestionName() { return questionName; }
    public void setQuestionName(String questionName) { this.questionName = questionName; }
    public String getOptionsJson() { return optionsJson; }
    public void setOptionsJson(String optionsJson) { this.optionsJson = optionsJson; }
    public Long getScore() { return score; }
    public void setScore(Long score) { this.score = score; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Long sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
