package com.example.counseling.controller;

import com.example.counseling.common.ApiResponse;
import com.example.counseling.dto.*;
import com.example.counseling.model.PsychologicalTest;
import com.example.counseling.model.TestQuestion;
import com.example.counseling.model.TestRecord;
import com.example.counseling.service.TestService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ApiResponse<List<PsychologicalTest>> tests(@RequestParam(defaultValue = "false") boolean activeOnly) {
        return ApiResponse.ok(testService.tests(activeOnly));
    }

    @GetMapping("/{id}")
    public ApiResponse<TestDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(testService.detail(id));
    }

    @PostMapping
    public ApiResponse<PsychologicalTest> saveTest(@RequestBody PsychologicalTest test) {
        return ApiResponse.ok("心理测试保存成功", testService.saveTest(test));
    }

    @PutMapping("/{id}")
    public ApiResponse<PsychologicalTest> updateTest(@PathVariable Long id, @RequestBody PsychologicalTest test) {
        test.setId(id);
        return ApiResponse.ok("心理测试更新成功", testService.saveTest(test));
    }

    @PostMapping("/{id}/questions")
    public ApiResponse<TestQuestion> saveQuestion(@PathVariable Long id, @RequestBody TestQuestion question) {
        question.setTestId(id);
        return ApiResponse.ok("试题保存成功", testService.saveQuestion(question));
    }

    @PutMapping("/questions/{id}")
    public ApiResponse<TestQuestion> updateQuestion(@PathVariable Long id, @RequestBody TestQuestion question) {
        question.setId(id);
        return ApiResponse.ok("试题更新成功", testService.saveQuestion(question));
    }

    @DeleteMapping("/questions/{id}")
    public ApiResponse<Void> deleteQuestion(@PathVariable Long id) {
        testService.deleteQuestion(id);
        return ApiResponse.ok("试题已删除", null);
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<TestSubmitResponse> submit(@PathVariable Long id, @Valid @RequestBody TestSubmitRequest request) {
        return ApiResponse.ok("测试提交成功", testService.submit(id, request));
    }

    @GetMapping("/records")
    public ApiResponse<List<TestRecord>> records(@RequestParam(required = false) Long userId) {
        return ApiResponse.ok(testService.records(userId));
    }
}
