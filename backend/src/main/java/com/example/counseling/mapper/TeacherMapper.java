package com.example.counseling.mapper;

import com.example.counseling.model.Teacher;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface TeacherMapper {
    @Select("select * from teachers order by id desc")
    List<Teacher> findAll();

    @Select("select * from teachers where status = 'APPROVED' order by id desc")
    List<Teacher> findApproved();

    @Select("select * from teachers where id=#{id}")
    Teacher findById(Long id);

    @Select("select * from teachers where job_no=#{jobNo}")
    Teacher findByJobNo(String jobNo);

    @Insert("insert into teachers(job_no,password,name,gender,email,phone,specialty,qualification,introduction,avatar,status) values(#{jobNo},#{password},#{name},#{gender},#{email},#{phone},#{specialty},#{qualification},#{introduction},'', 'PENDING')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Teacher teacher);

    @Update("update teachers set name=#{name}, gender=#{gender}, email=#{email}, phone=#{phone}, specialty=#{specialty}, qualification=#{qualification}, introduction=#{introduction}, status=#{status} where id=#{id}")
    int update(Teacher teacher);

    @Update("update teachers set status=#{status} where id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("delete from teachers where id=#{id}")
    int delete(Long id);
}
