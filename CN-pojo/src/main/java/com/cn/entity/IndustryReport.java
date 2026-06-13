package com.cn.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IndustryReport {
    private Long id;
    private String title;
    private String industry;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String summary;
    private String content;
    private LocalDateTime generatedAt;
    private LocalDateTime createdAt;
}
