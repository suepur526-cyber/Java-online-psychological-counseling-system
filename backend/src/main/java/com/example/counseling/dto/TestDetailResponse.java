package com.example.counseling.dto;

import com.example.counseling.model.PsychologicalTest;
import com.example.counseling.model.TestQuestion;
import java.util.List;

public class TestDetailResponse {
    private PsychologicalTest test;
    private List<TestQuestion> questions;

    public TestDetailResponse(PsychologicalTest test, List<TestQuestion> questions) {
        this.test = test;
        this.questions = questions;
    }

    public PsychologicalTest getTest() { return test; }
    public List<TestQuestion> getQuestions() { return questions; }
}
