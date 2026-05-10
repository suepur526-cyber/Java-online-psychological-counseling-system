package com.example.counseling.controller;

import com.example.counseling.common.ApiResponse;
import com.example.counseling.model.*;
import com.example.counseling.service.ManagementService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ManagementController {
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        return ApiResponse.ok(managementService.users());
    }

    @PutMapping("/users/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return ApiResponse.ok(managementService.updateUser(user));
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<Void> updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        managementService.updateUserStatus(id, status);
        return ApiResponse.ok("用户状态已更新", null);
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        managementService.deleteUser(id);
        return ApiResponse.ok("用户已删除", null);
    }

    @GetMapping("/teachers")
    public ApiResponse<List<Teacher>> teachers(@RequestParam(defaultValue = "false") boolean approvedOnly) {
        return ApiResponse.ok(approvedOnly ? managementService.approvedTeachers() : managementService.teachers());
    }

    @PutMapping("/teachers/{id}")
    public ApiResponse<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        return ApiResponse.ok(managementService.updateTeacher(teacher));
    }

    @PutMapping("/teachers/{id}/status")
    public ApiResponse<Void> updateTeacherStatus(@PathVariable Long id, @RequestParam String status) {
        managementService.updateTeacherStatus(id, status);
        return ApiResponse.ok("心理老师状态已更新", null);
    }

    @GetMapping("/services")
    public ApiResponse<List<ServiceInfo>> services() {
        return ApiResponse.ok(managementService.services());
    }

    @GetMapping("/announcements")
    public ApiResponse<List<Announcement>> announcements(@RequestParam(defaultValue = "false") boolean publicOnly) {
        return ApiResponse.ok(managementService.announcements(publicOnly));
    }

    @GetMapping("/announcements/{id}")
    public ApiResponse<Announcement> announcement(@PathVariable Long id) {
        return ApiResponse.ok(managementService.announcement(id));
    }

    @PostMapping("/announcements")
    public ApiResponse<Announcement> saveAnnouncement(@RequestBody Announcement announcement) {
        return ApiResponse.ok("公告保存成功", managementService.saveAnnouncement(announcement));
    }

    @PutMapping("/announcements/{id}")
    public ApiResponse<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        return ApiResponse.ok("公告更新成功", managementService.saveAnnouncement(announcement));
    }

    @DeleteMapping("/announcements/{id}")
    public ApiResponse<Void> deleteAnnouncement(@PathVariable Long id) {
        managementService.deleteAnnouncement(id);
        return ApiResponse.ok("公告已删除", null);
    }

    @GetMapping("/system/infos")
    public ApiResponse<List<SystemInfo>> systemInfos() {
        return ApiResponse.ok(managementService.systemInfos());
    }

    @PutMapping("/system/infos/{id}")
    public ApiResponse<SystemInfo> updateSystemInfo(@PathVariable Long id, @RequestBody SystemInfo info) {
        info.setId(id);
        return ApiResponse.ok(managementService.updateSystemInfo(info));
    }

    @GetMapping("/system/configs")
    public ApiResponse<List<SystemConfig>> configs() {
        return ApiResponse.ok(managementService.configs());
    }

    @PutMapping("/system/configs/{id}")
    public ApiResponse<Void> updateConfig(@PathVariable Long id, @RequestBody SystemConfig config) {
        config.setId(id);
        managementService.updateConfig(config);
        return ApiResponse.ok("系统配置已更新", null);
    }
}
