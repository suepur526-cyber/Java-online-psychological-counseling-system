package com.example.counseling.service;

import com.example.counseling.common.BusinessException;
import com.example.counseling.dto.*;
import com.example.counseling.mapper.TestMapper;
import com.example.counseling.mapper.UserMapper;
import com.example.counseling.model.PsychologicalTest;
import com.example.counseling.model.TestQuestion;
import com.example.counseling.model.TestRecord;
import com.example.counseling.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TestService {
    private final TestMapper testMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public TestService(TestMapper testMapper, UserMapper userMapper, ObjectMapper objectMapper) {
        this.testMapper = testMapper;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    public List<PsychologicalTest> tests(boolean activeOnly) {
        return activeOnly ? testMapper.findActiveTests() : testMapper.findTests();
    }

    public TestDetailResponse detail(Long id) {
        PsychologicalTest test = testMapper.findTest(id);
        if (test == null) {
            throw new BusinessException("心理测试不存在");
        }
        return new TestDetailResponse(test, testMapper.findQuestions(id));
    }

    public PsychologicalTest saveTest(PsychologicalTest test) {
        if (test.getStatus() == null || test.getStatus().isEmpty()) {
            test.setStatus("ACTIVE");
        }
        if (test.getId() == null) {
            testMapper.insertTest(test);
        } else {
            testMapper.updateTest(test);
        }
        return testMapper.findTest(test.getId());
    }

    public TestQuestion saveQuestion(TestQuestion question) {
        if (question.getType() == null || question.getType().isEmpty()) {
            question.setType("SINGLE");
        }
        if (question.getSequenceNo() == null) {
            question.setSequenceNo(100L);
        }
        if (question.getId() == null) {
            testMapper.insertQuestion(question);
        } else {
            testMapper.updateQuestion(question);
        }
        return testMapper.findQuestion(question.getId());
    }

    public void deleteQuestion(Long id) {
        testMapper.deleteQuestion(id);
    }

    public TestSubmitResponse submit(Long testId, TestSubmitRequest request) {
        User user = userMapper.findById(request.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        PsychologicalTest test = testMapper.findTest(testId);
        if (test == null) {
            throw new BusinessException("心理测试不存在");
        }
        List<TestQuestion> questions = testMapper.findQuestions(testId);
        long total = 0;
        for (TestQuestion question : questions) {
            String selected = request.getAnswers().get(question.getId());
            total += optionScore(question.getOptionsJson(), selected);
        }
        TestRecord record = new TestRecord();
        record.setUserId(user.getId());
        record.setUsername(user.getName());
        record.setTestId(test.getId());
        record.setTestName(test.getName());
        record.setTotalScore(total);
        record.setResultLevel(level(total, questions.size()));
        try {
            record.setAnswersJson(objectMapper.writeValueAsString(request.getAnswers()));
        } catch (JsonProcessingException exception) {
            throw new BusinessException("答案保存失败");
        }
        testMapper.insertRecord(record);
        return new TestSubmitResponse(record.getId(), record.getTotalScore(), record.getResultLevel());
    }

    public List<TestRecord> records(Long userId) {
        return userId == null ? testMapper.findRecords() : testMapper.findRecordsByUser(userId);
    }

    @SuppressWarnings("unchecked")
    private long optionScore(String optionsJson, String selected) {
        if (selected == null) {
            return 0;
        }
        try {
            List<Map<String, Object>> options = objectMapper.readValue(optionsJson, List.class);
            for (Map<String, Object> option : options) {
                if (selected.equals(String.valueOf(option.get("value")))) {
                    Object score = option.get("score");
                    return Long.parseLong(String.valueOf(score));
                }
            }
        } catch (Exception exception) {
            throw new BusinessException("试题选项格式错误");
        }
        return 0;
    }

    private String level(long score, int questionCount) {
        long high = questionCount * 3L;
        if (score <= high * 0.45) {
            return "状态良好";
        }
        if (score <= high * 0.7) {
            return "轻度压力";
        }
        return "建议预约咨询";
    }
}
