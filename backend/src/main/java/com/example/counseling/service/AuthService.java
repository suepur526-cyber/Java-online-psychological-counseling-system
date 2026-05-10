package com.example.counseling.service;

import com.example.counseling.common.BusinessException;
import com.example.counseling.dto.*;
import com.example.counseling.mapper.AdminMapper;
import com.example.counseling.mapper.TeacherMapper;
import com.example.counseling.mapper.UserMapper;
import com.example.counseling.model.Admin;
import com.example.counseling.model.Teacher;
import com.example.counseling.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final TeacherMapper teacherMapper;

    public AuthService(AdminMapper adminMapper, UserMapper userMapper, TeacherMapper teacherMapper) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
        this.teacherMapper = teacherMapper;
    }

    public LoginResponse login(LoginRequest request) {
        switch (request.getRole()) {
            case "ADMIN":
                Admin admin = adminMapper.findByUsername(request.getAccount());
                if (admin == null || !admin.getPassword().equals(request.getPassword())) {
                    throw new BusinessException("管理员账号或密码错误");
                }
                return new LoginResponse(admin.getId(), admin.getUsername(), admin.getName(), "ADMIN", "ACTIVE");
            case "USER":
                User user = userMapper.findByStudentNo(request.getAccount());
                if (user == null || !user.getPassword().equals(request.getPassword())) {
                    throw new BusinessException("用户账号或密码错误");
                }
                if (!"ACTIVE".equals(user.getStatus())) {
                    throw new BusinessException("用户账号已被禁用");
                }
                return new LoginResponse(user.getId(), user.getStudentNo(), user.getName(), "USER", user.getStatus());
            case "TEACHER":
                Teacher teacher = teacherMapper.findByJobNo(request.getAccount());
                if (teacher == null || !teacher.getPassword().equals(request.getPassword())) {
                    throw new BusinessException("心理老师账号或密码错误");
                }
                if (!"APPROVED".equals(teacher.getStatus())) {
                    throw new BusinessException("心理老师账号尚未审核通过");
                }
                return new LoginResponse(teacher.getId(), teacher.getJobNo(), teacher.getName(), "TEACHER", teacher.getStatus());
            default:
                throw new BusinessException("角色类型不正确");
        }
    }

    public User registerUser(RegisterUserRequest request) {
        if (userMapper.findByStudentNo(request.getStudentNo()) != null) {
            throw new BusinessException("该学号已注册");
        }
        User user = new User();
        user.setStudentNo(request.getStudentNo());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        userMapper.insert(user);
        return userMapper.findById(user.getId());
    }

    public Teacher registerTeacher(RegisterTeacherRequest request) {
        if (teacherMapper.findByJobNo(request.getJobNo()) != null) {
            throw new BusinessException("该工号已注册");
        }
        Teacher teacher = new Teacher();
        teacher.setJobNo(request.getJobNo());
        teacher.setPassword(request.getPassword());
        teacher.setName(request.getName());
        teacher.setGender(request.getGender());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setSpecialty(request.getSpecialty());
        teacher.setQualification(request.getQualification());
        teacher.setIntroduction(request.getIntroduction());
        teacherMapper.insert(teacher);
        return teacherMapper.findById(teacher.getId());
    }
}
