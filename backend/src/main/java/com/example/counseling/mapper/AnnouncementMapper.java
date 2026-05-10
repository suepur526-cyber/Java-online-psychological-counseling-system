package com.example.counseling.mapper;

import com.example.counseling.model.Announcement;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface AnnouncementMapper {
    @Select("select * from announcements order by id desc")
    List<Announcement> findAll();

    @Select("select * from announcements where status='PUBLISHED' order by id desc")
    List<Announcement> findPublished();

    @Select("select * from announcements where id=#{id}")
    Announcement findById(Long id);

    @Insert("insert into announcements(title,introduction,picture,content,status) values(#{title},#{introduction},#{picture},#{content},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Announcement announcement);

    @Update("update announcements set title=#{title}, introduction=#{introduction}, picture=#{picture}, content=#{content}, status=#{status} where id=#{id}")
    int update(Announcement announcement);

    @Delete("delete from announcements where id=#{id}")
    int delete(Long id);
}
