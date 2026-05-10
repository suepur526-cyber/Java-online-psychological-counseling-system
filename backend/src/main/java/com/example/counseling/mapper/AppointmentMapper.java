package com.example.counseling.mapper;

import com.example.counseling.model.Appointment;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface AppointmentMapper {
    @Select("select * from appointments order by id desc")
    List<Appointment> findAll();

    @Select("select * from appointments where user_id=#{userId} order by id desc")
    List<Appointment> findByUser(Long userId);

    @Select("select * from appointments where teacher_id=#{teacherId} order by id desc")
    List<Appointment> findByTeacher(Long teacherId);

    @Select("select * from appointments where id=#{id}")
    Appointment findById(Long id);

    @Insert("insert into appointments(appointment_no,teacher_id,job_no,teacher_name,user_id,student_no,student_name,fee,working_time,appointment_time,note,status,review_reply,is_paid) values(#{appointmentNo},#{teacherId},#{jobNo},#{teacherName},#{userId},#{studentNo},#{studentName},#{fee},#{workingTime},#{appointmentTime},#{note},#{status},#{reviewReply},#{isPaid})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Appointment appointment);

    @Update("update appointments set status=#{status}, review_reply=#{reviewReply} where id=#{id}")
    int review(Appointment appointment);
}
