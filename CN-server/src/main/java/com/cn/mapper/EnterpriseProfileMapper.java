package com.cn.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseProfileMapper {

    /**
     * 插入公司名称
     *
     * @param userId      用户ID
     * @param companyName 公司名称
     */
    @Insert("insert into enterprise_profiles (user_id, company_name) values (#{userId}, #{companyName})")
    void insertCompanyName(Long userId, String companyName);

}
