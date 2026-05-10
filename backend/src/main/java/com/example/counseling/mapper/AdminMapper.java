package com.example.counseling.mapper;

import com.example.counseling.model.Admin;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {
    @Select("select * from admins where username = #{username}")
    Admin findByUsername(String username);
}
