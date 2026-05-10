package com.example.counseling.service;

import com.example.counseling.common.BusinessException;
import com.example.counseling.dto.ConsultationRequest;
import com.example.counseling.dto.ReplyRequest;
import com.example.counseling.mapper.ConsultationMapper;
import com.example.counseling.mapper.TeacherMapper;
import com.example.counseling.mapper.UserMapper;
import com.example.counseling.model.Consultation;
import com.example.counseling.model.ConsultationReply;
import com.example.counseling.model.Teacher;
import com.example.counseling.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultationService {
    private final ConsultationMapper consultationMapper;
    private final UserMapper userMapper;
    private final TeacherMapper teacherMapper;

    public ConsultationService(ConsultationMapper consultationMapper, UserMapper userMapper, TeacherMapper teacherMapper) {
        this.consultationMapper = consultationMapper;
        this.userMapper = userMapper;
        this.teacherMapper = teacherMapper;
    }

    public Consultation submit(ConsultationRequest request) {
        User user = userMapper.findById(request.getUserId());
        Teacher teacher = teacherMapper.findById(request.getTeacherId());
        if (user == null) throw new BusinessException("用户不存在");
        if (teacher == null) throw new BusinessException("心理老师不存在");
        Consultation consultation = new Consultation();
        consultation.setTeacherId(teacher.getId());
        consultation.setJobNo(teacher.getJobNo());
        consultation.setTeacherName(teacher.getName());
        consultation.setUserId(user.getId());
        consultation.setStudentNo(user.getStudentNo());
        consultation.setStudentName(user.getName());
        consultation.setContent(request.getContent());
        consultation.setStatus("PENDING");
        consultation.setConsultationDate(LocalDate.now().toString());
        consultationMapper.insert(consultation);
        return consultationMapper.findById(consultation.getId());
    }

    public ConsultationReply reply(Long id, ReplyRequest request) {
        Consultation consultation = consultationMapper.findById(id);
        if (consultation == null) throw new BusinessException("咨询不存在");
        ConsultationReply reply = new ConsultationReply();
        reply.setConsultationId(consultation.getId());
        reply.setTeacherId(consultation.getTeacherId());
        reply.setJobNo(consultation.getJobNo());
        reply.setTeacherName(consultation.getTeacherName());
        reply.setUserId(consultation.getUserId());
        reply.setStudentNo(consultation.getStudentNo());
        reply.setStudentName(consultation.getStudentName());
        reply.setConsultationContent(consultation.getContent());
        reply.setReplyContent(request.getReplyContent());
        reply.setReplyDate(LocalDate.now().toString());
        consultationMapper.insertReply(reply);
        consultationMapper.updateStatus(id, "REPLIED");
        return reply;
    }

    public List<Consultation> list(String role, Long ownerId) {
        if ("USER".equals(role)) return consultationMapper.findByUser(ownerId);
        if ("TEACHER".equals(role)) return consultationMapper.findByTeacher(ownerId);
        return consultationMapper.findAll();
    }

    public List<ConsultationReply> replies(Long userId) {
        return userId == null ? consultationMapper.findReplies() : consultationMapper.findRepliesByUser(userId);
    }
}
