package com.example.counseling.controller;

import com.example.counseling.common.ApiResponse;
import com.example.counseling.dto.ConsultationRequest;
import com.example.counseling.dto.ReplyRequest;
import com.example.counseling.model.Consultation;
import com.example.counseling.model.ConsultationReply;
import com.example.counseling.service.ConsultationService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {
    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping
    public ApiResponse<List<Consultation>> list(@RequestParam(defaultValue = "ADMIN") String role,
                                                @RequestParam(required = false) Long ownerId) {
        return ApiResponse.ok(consultationService.list(role, ownerId));
    }

    @PostMapping
    public ApiResponse<Consultation> submit(@Valid @RequestBody ConsultationRequest request) {
        return ApiResponse.ok("咨询提交成功", consultationService.submit(request));
    }

    @PostMapping("/{id}/reply")
    public ApiResponse<ConsultationReply> reply(@PathVariable Long id, @Valid @RequestBody ReplyRequest request) {
        return ApiResponse.ok("咨询回复成功", consultationService.reply(id, request));
    }

    @GetMapping("/replies")
    public ApiResponse<List<ConsultationReply>> replies(@RequestParam(required = false) Long userId) {
        return ApiResponse.ok(consultationService.replies(userId));
    }
}
