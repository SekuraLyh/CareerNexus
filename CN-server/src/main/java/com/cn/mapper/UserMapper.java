package com.cn.mapper;

import com.cn.entity.UserAccont;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from users where username = #{username}")
    UserAccont getByUsername(String username);

    @Select("select * from users where email = #{email}")
    UserAccont getByEmail(String email);

    @Insert("insert into users (username, password, user_type, email, phone, status) " +
            "values (#{username}, #{password}, #{userType}, #{email}, #{phone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserAccont jobSeeker);
}
