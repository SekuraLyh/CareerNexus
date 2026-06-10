package com.cn.mapper;

import com.cn.entity.JobPosting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobsMapper {

    List<JobPosting> selectJosByConditions(String major, String keyword, String minSalary, String location);

    /**
     * 模糊搜索职位（支持更多筛选条件）
     */
    List<JobPosting> searchJobs(
            String keyword,
            String major,
            Integer minExperience,
            Integer maxExperience,
            Integer minSalary,
            Integer maxSalary,
            String location
    );

}
