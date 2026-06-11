package com.cn.mapper;

import com.cn.VO.SubscriptionsVO;
import com.cn.entity.JobPosting;
import com.cn.entity.Subscriptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubscriptionsMapper {
    List<SubscriptionsVO> getSubscriptionsByUserId(Long userId);

    Subscriptions selectById(Long id);

    void insert(Subscriptions subscription);

    void update(Subscriptions subscription);

    void deleteById(Long id);

    void updateEnabled(@Param("id") Long id, @Param("enabled") Boolean enabled);

    List<JobPosting> matchJobs(@Param("keywords") List<String> keywords,
                               @Param("major") String major,
                               @Param("minSalary") Integer minSalary,
                               @Param("workExperience") Integer workExperience,
                               @Param("location") String location);
}
