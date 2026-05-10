package com.example.counseling.mapper;

import com.example.counseling.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface UserMapper {
    @Select("select * from users order by id desc")
    List<User> findAll();

    @Select("select * from users where id = #{id}")
    User findById(Long id);

    @Select("select * from users where student_no = #{studentNo}")
    User findByStudentNo(String studentNo);

    @Insert("insert into users(student_no,password,name,gender,email,phone,avatar,status) values(#{studentNo},#{password},#{name},#{gender},#{email},#{phone},'', 'ACTIVE')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("update users set name=#{name}, gender=#{gender}, email=#{email}, phone=#{phone}, status=#{status} where id=#{id}")
    int update(User user);

    @Update("update users set status=#{status} where id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("delete from users where id=#{id}")
    int delete(Long id);
}
