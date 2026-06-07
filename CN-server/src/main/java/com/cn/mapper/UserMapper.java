package com.cn.mapper;

import com.cn.entity.UserAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from users where username = #{username}")
    UserAccount getByUsername(String username);

    @Select("select * from users where email = #{email}")
    UserAccount getByEmail(String email);

    @Select("select * from users where id = #{id}")
    UserAccount getById(Long id);

    @Insert("insert into users (username, password, user_type, email, phone, status) " +
            "values (#{username}, #{password}, #{userType}, #{email}, #{phone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserAccount userAccount);

    /**
     * 更新密码（专用方法）
     * 也可以使用 AdminMapper.updateUser() 进行通用更新
     */
    @org.apache.ibatis.annotations.Update("update users set password = #{password} where id = #{id}")
    void updatePassword(@org.apache.ibatis.annotations.Param("id") Long id, @org.apache.ibatis.annotations.Param("password") String password);

    /**
     * 更新状态（专用方法）
     * 也可以使用 AdminMapper.updateUser() 进行通用更新
     */
    @org.apache.ibatis.annotations.Update("update users set status = #{status} where id = #{id}")
    void updateStatus(@org.apache.ibatis.annotations.Param("id") Long id, @org.apache.ibatis.annotations.Param("status") String status);
}
