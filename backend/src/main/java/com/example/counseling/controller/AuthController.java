package com.example.counseling.controller;

import com.example.counseling.common.ApiResponse;
import com.example.counseling.dto.*;
import com.example.counseling.model.Teacher;
import com.example.counseling.model.User;
import com.example.counseling.service.AuthService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("登录成功", authService.login(request));
    }

    @PostMapping("/register/user")
    public ApiResponse<User> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return ApiResponse.ok("用户注册成功", authService.registerUser(request));
    }

    @PostMapping("/register/teacher")
    public ApiResponse<Teacher> registerTeacher(@Valid @RequestBody RegisterTeacherRequest request) {
        return ApiResponse.ok("老师注册成功，请等待管理员审核", authService.registerTeacher(request));
    }
}
