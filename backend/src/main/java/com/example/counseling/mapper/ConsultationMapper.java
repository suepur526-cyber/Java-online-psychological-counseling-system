package com.example.counseling.mapper;

import com.example.counseling.model.Consultation;
import com.example.counseling.model.ConsultationReply;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ConsultationMapper {
    @Select("select * from consultations order by id desc")
    List<Consultation> findAll();

    @Select("select * from consultations where user_id=#{userId} order by id desc")
    List<Consultation> findByUser(Long userId);

    @Select("select * from consultations where teacher_id=#{teacherId} order by id desc")
    List<Consultation> findByTeacher(Long teacherId);

    @Select("select * from consultations where id=#{id}")
    Consultation findById(Long id);

    @Insert("insert into consultations(teacher_id,job_no,teacher_name,user_id,student_no,student_name,content,status,consultation_date) values(#{teacherId},#{jobNo},#{teacherName},#{userId},#{studentNo},#{studentName},#{content},#{status},#{consultationDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Consultation consultation);

    @Update("update consultations set status=#{status} where id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Insert("insert into consultation_replies(consultation_id,teacher_id,job_no,teacher_name,user_id,student_no,student_name,consultation_content,reply_content,reply_date) values(#{consultationId},#{teacherId},#{jobNo},#{teacherName},#{userId},#{studentNo},#{studentName},#{consultationContent},#{replyContent},#{replyDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertReply(ConsultationReply reply);

    @Select("select * from consultation_replies order by id desc")
    List<ConsultationReply> findReplies();

    @Select("select * from consultation_replies where user_id=#{userId} order by id desc")
    List<ConsultationReply> findRepliesByUser(Long userId);
}
