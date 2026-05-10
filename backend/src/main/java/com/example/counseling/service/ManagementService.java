package com.example.counseling.service;

import com.example.counseling.common.BusinessException;
import com.example.counseling.mapper.*;
import com.example.counseling.model.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ManagementService {
    private final UserMapper userMapper;
    private final TeacherMapper teacherMapper;
    private final AnnouncementMapper announcementMapper;
    private final ServiceInfoMapper serviceInfoMapper;
    private final SystemMapper systemMapper;

    public ManagementService(UserMapper userMapper, TeacherMapper teacherMapper, AnnouncementMapper announcementMapper,
                             ServiceInfoMapper serviceInfoMapper, SystemMapper systemMapper) {
        this.userMapper = userMapper;
        this.teacherMapper = teacherMapper;
        this.announcementMapper = announcementMapper;
        this.serviceInfoMapper = serviceInfoMapper;
        this.systemMapper = systemMapper;
    }

    public List<User> users() { return userMapper.findAll(); }

    public User updateUser(User user) {
        require(userMapper.findById(user.getId()) != null, "用户不存在");
        userMapper.update(user);
        return userMapper.findById(user.getId());
    }

    public void updateUserStatus(Long id, String status) {
        require(userMapper.updateStatus(id, status) > 0, "用户不存在");
    }

    public void deleteUser(Long id) {
        require(userMapper.delete(id) > 0, "用户不存在");
    }

    public List<Teacher> teachers() { return teacherMapper.findAll(); }

    public List<Teacher> approvedTeachers() { return teacherMapper.findApproved(); }

    public Teacher updateTeacher(Teacher teacher) {
        require(teacherMapper.findById(teacher.getId()) != null, "心理老师不存在");
        teacherMapper.update(teacher);
        return teacherMapper.findById(teacher.getId());
    }

    public void updateTeacherStatus(Long id, String status) {
        require(teacherMapper.updateStatus(id, status) > 0, "心理老师不存在");
    }

    public List<ServiceInfo> services() { return serviceInfoMapper.findAll(); }

    public List<Announcement> announcements(boolean publicOnly) {
        return publicOnly ? announcementMapper.findPublished() : announcementMapper.findAll();
    }

    public Announcement announcement(Long id) {
        Announcement announcement = announcementMapper.findById(id);
        require(announcement != null, "公告不存在");
        return announcement;
    }

    public Announcement saveAnnouncement(Announcement announcement) {
        if (announcement.getStatus() == null || announcement.getStatus().isEmpty()) {
            announcement.setStatus("PUBLISHED");
        }
        if (announcement.getId() == null) {
            announcementMapper.insert(announcement);
        } else {
            require(announcementMapper.update(announcement) > 0, "公告不存在");
        }
        return announcementMapper.findById(announcement.getId());
    }

    public void deleteAnnouncement(Long id) {
        require(announcementMapper.delete(id) > 0, "公告不存在");
    }

    public List<SystemInfo> systemInfos() { return systemMapper.findInfos(); }

    public SystemInfo updateSystemInfo(SystemInfo info) {
        require(systemMapper.updateInfo(info) > 0, "系统信息不存在");
        return systemMapper.findInfo(info.getId());
    }

    public List<SystemConfig> configs() { return systemMapper.findConfigs(); }

    public void updateConfig(SystemConfig config) {
        require(systemMapper.updateConfig(config) > 0, "系统配置不存在");
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }
}
