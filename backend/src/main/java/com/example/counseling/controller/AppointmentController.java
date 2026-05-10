package com.example.counseling.controller;

import com.example.counseling.common.ApiResponse;
import com.example.counseling.dto.AppointmentRequest;
import com.example.counseling.dto.ReviewRequest;
import com.example.counseling.model.Appointment;
import com.example.counseling.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ApiResponse<List<Appointment>> list(@RequestParam(defaultValue = "ADMIN") String role,
                                               @RequestParam(required = false) Long ownerId) {
        return ApiResponse.ok(appointmentService.list(role, ownerId));
    }

    @PostMapping
    public ApiResponse<Appointment> submit(@Valid @RequestBody AppointmentRequest request) {
        return ApiResponse.ok("预约提交成功", appointmentService.submit(request));
    }

    @PutMapping("/{id}/review")
    public ApiResponse<Appointment> review(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.ok("预约审核成功", appointmentService.review(id, request));
    }
}
