package com.example.counseling.mapper;

import com.example.counseling.model.ServiceInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ServiceInfoMapper {
    @Select("select * from services order by id desc")
    List<ServiceInfo> findAll();

    @Select("select * from services where teacher_id=#{teacherId}")
    ServiceInfo findByTeacherId(Long teacherId);

    @Insert("insert into services(teacher_id,job_no,teacher_name,gender,photo,fee,working_time,teaching_years,introduction,honor) values(#{teacherId},#{jobNo},#{teacherName},#{gender},#{photo},#{fee},#{workingTime},#{teachingYears},#{introduction},#{honor})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ServiceInfo serviceInfo);
}
