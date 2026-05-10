package com.example.counseling.service;

import com.example.counseling.common.BusinessException;
import com.example.counseling.dto.AppointmentRequest;
import com.example.counseling.dto.ReviewRequest;
import com.example.counseling.mapper.AppointmentMapper;
import com.example.counseling.mapper.ServiceInfoMapper;
import com.example.counseling.mapper.TeacherMapper;
import com.example.counseling.mapper.UserMapper;
import com.example.counseling.model.Appointment;
import com.example.counseling.model.ServiceInfo;
import com.example.counseling.model.Teacher;
import com.example.counseling.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentMapper appointmentMapper;
    private final UserMapper userMapper;
    private final TeacherMapper teacherMapper;
    private final ServiceInfoMapper serviceInfoMapper;

    public AppointmentService(AppointmentMapper appointmentMapper, UserMapper userMapper, TeacherMapper teacherMapper,
                              ServiceInfoMapper serviceInfoMapper) {
        this.appointmentMapper = appointmentMapper;
        this.userMapper = userMapper;
        this.teacherMapper = teacherMapper;
        this.serviceInfoMapper = serviceInfoMapper;
    }

    public Appointment submit(AppointmentRequest request) {
        User user = userMapper.findById(request.getUserId());
        Teacher teacher = teacherMapper.findById(request.getTeacherId());
        if (user == null) throw new BusinessException("用户不存在");
        if (teacher == null) throw new BusinessException("心理老师不存在");
        if (!"APPROVED".equals(teacher.getStatus())) throw new BusinessException("心理老师尚未审核通过");
        ServiceInfo serviceInfo = serviceInfoMapper.findByTeacherId(teacher.getId());
        Appointment appointment = new Appointment();
        appointment.setAppointmentNo("YY" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        appointment.setTeacherId(teacher.getId());
        appointment.setJobNo(teacher.getJobNo());
        appointment.setTeacherName(teacher.getName());
        appointment.setUserId(user.getId());
        appointment.setStudentNo(user.getStudentNo());
        appointment.setStudentName(user.getName());
        appointment.setFee(serviceInfo == null ? "免费" : serviceInfo.getFee());
        appointment.setWorkingTime(serviceInfo == null ? "" : serviceInfo.getWorkingTime());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setNote(request.getNote());
        appointment.setStatus("PENDING");
        appointment.setIsPaid("UNPAID");
        appointmentMapper.insert(appointment);
        return appointmentMapper.findById(appointment.getId());
    }

    public Appointment review(Long id, ReviewRequest request) {
        Appointment appointment = appointmentMapper.findById(id);
        if (appointment == null) throw new BusinessException("预约不存在");
        appointment.setStatus(request.getStatus());
        appointment.setReviewReply(request.getReviewReply());
        appointmentMapper.review(appointment);
        return appointmentMapper.findById(id);
    }

    public List<Appointment> list(String role, Long ownerId) {
        if ("USER".equals(role)) return appointmentMapper.findByUser(ownerId);
        if ("TEACHER".equals(role)) return appointmentMapper.findByTeacher(ownerId);
        return appointmentMapper.findAll();
    }
}
