package com.cn;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CNApplication {
    public static void main(String[] args) {
        SpringApplication.run(CNApplication.class, args);
        log.info("========== CareerNexus 启动成功 ==========");
    }
}
