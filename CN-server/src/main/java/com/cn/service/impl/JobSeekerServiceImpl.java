package com.cn.service.impl;

import com.cn.DTO.JobSeekerLoginDTO;
import com.cn.VO.JobSeekerLoginVO;
import com.cn.VO.JobSeekerLoginVO.UserInfo;
import com.cn.entity.JobSeeker;
import com.cn.exception.LoginFailedException;
import com.cn.mapper.JobSeekerMapper;
import com.cn.properties.JwtProperties;
import com.cn.service.JobSeekerService;
import com.cn.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerMapper jobSeekerMapper;

    @Autowired
    private JwtProperties jwtProperties;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public JobSeekerLoginVO login(JobSeekerLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        JobSeeker jobSeeker = jobSeekerMapper.getByUsername(username);

        if (jobSeeker == null || !passwordEncoder.matches(password, jobSeeker.getPassword())) {
            throw new LoginFailedException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", jobSeeker.getId());
        claims.put("username", jobSeeker.getUsername());
        claims.put("userType", jobSeeker.getUserType());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        UserInfo userInfo = new UserInfo(
                jobSeeker.getId(),
                jobSeeker.getUsername(),
                jobSeeker.getUserType(),
                jobSeeker.getEmail(),
                jobSeeker.getPhone(),
                jobSeeker.getStatus(),
                jobSeeker.getCreatedAt()
        );

        log.info("用户登录成功: {}", username);
        return new JobSeekerLoginVO(token, userInfo);
    }
}
