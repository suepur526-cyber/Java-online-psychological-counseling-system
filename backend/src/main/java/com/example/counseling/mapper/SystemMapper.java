package com.example.counseling.mapper;

import com.example.counseling.model.SystemConfig;
import com.example.counseling.model.SystemInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SystemMapper {
    @Select("select * from system_infos order by id desc")
    List<SystemInfo> findInfos();

    @Select("select * from system_infos where id=#{id}")
    SystemInfo findInfo(Long id);

    @Update("update system_infos set title=#{title}, subtitle=#{subtitle}, content=#{content}, picture1=#{picture1}, picture2=#{picture2}, picture3=#{picture3} where id=#{id}")
    int updateInfo(SystemInfo info);

    @Select("select * from system_configs order by id desc")
    List<SystemConfig> findConfigs();

    @Update("update system_configs set config_value=#{configValue}, remark=#{remark} where id=#{id}")
    int updateConfig(SystemConfig config);
}
