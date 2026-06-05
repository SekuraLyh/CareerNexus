package com.cn.mapper;


import com.cn.entity.JobSeeker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JobSeekerMapper {

    @Select("select * from users where username = #{username}")
    JobSeeker getByUsername(String username);

}
