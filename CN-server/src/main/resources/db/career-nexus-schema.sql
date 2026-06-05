-- =============================================================================
-- CareerNexus 求职网站 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4_unicode_ci
-- 引擎: InnoDB
-- =============================================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS CareerNexus
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE CareerNexus;

-- =============================================================================
-- 1. 用户表 (users)
-- 系统管理模块 — 所有用户类型的统一认证表
-- 软删除: status='INACTIVE' 表示已注销账号
-- =============================================================================
CREATE TABLE users (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
    username        VARCHAR(20)     NOT NULL                 COMMENT '用户名，3-20字符',
    password   VARCHAR(255)    NOT NULL                 COMMENT '密码',
    user_type       ENUM('JOB_SEEKER', 'ENTERPRISE', 'ADMIN')
                                    NOT NULL                 COMMENT '用户类型：求职者/企业/管理员',
    email           VARCHAR(100)    NOT NULL                 COMMENT '邮箱',
    phone           VARCHAR(20)     DEFAULT NULL             COMMENT '手机号',
    status          ENUM('ACTIVE', 'INACTIVE')
                                    NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态：激活/停用',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    INDEX idx_user_type_status (user_type, status),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================================================
-- 2. 求职者档案表 (job_seeker_profiles)
-- 档案管理模块 — 与 users 表 1:1 关联
-- 搜索核心表：支持按专业、学历、工作经验、技能进行模糊搜索
-- =============================================================================
CREATE TABLE job_seeker_profiles (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '档案ID',
    user_id         BIGINT          NOT NULL                 COMMENT '关联用户ID',
    real_name       VARCHAR(50)     DEFAULT NULL             COMMENT '真实姓名',
    gender          ENUM('MALE', 'FEMALE')
                                    DEFAULT NULL             COMMENT '性别',
    birth_date      DATE            DEFAULT NULL             COMMENT '出生日期',
    major           VARCHAR(100)    DEFAULT NULL             COMMENT '专业',
    education       ENUM('HIGH_SCHOOL', 'ASSOCIATE', 'BACHELOR', 'MASTER', 'PHD')
                                    DEFAULT NULL             COMMENT '最高学历',
    school          VARCHAR(100)    DEFAULT NULL             COMMENT '毕业院校',
    graduation_year INT             DEFAULT NULL             COMMENT '毕业年份',
    work_experience INT             DEFAULT 0                COMMENT '工作年限（年）',
    expected_salary INT             DEFAULT NULL             COMMENT '期望月薪（元）',
    skills          TEXT            DEFAULT NULL             COMMENT '技能标签，逗号分隔，如"Java,Spring,MySQL"',
    self_description TEXT           DEFAULT NULL             COMMENT '自我描述',
    resume_url      VARCHAR(500)    DEFAULT NULL             COMMENT '简历文件路径',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_major (major),
    INDEX idx_education (education),
    INDEX idx_work_experience (work_experience),
    INDEX idx_expected_salary (expected_salary),
    INDEX idx_graduation_year (graduation_year),
    FULLTEXT INDEX ft_major_skills (major, skills) WITH PARSER ngram,
    CONSTRAINT fk_seeker_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者档案表';

-- =============================================================================
-- 3. 企业档案表 (enterprise_profiles)
-- 档案管理模块 — 与 users 表 1:1 关联
-- =============================================================================
CREATE TABLE enterprise_profiles (
    id                  BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '档案ID',
    user_id             BIGINT          NOT NULL                 COMMENT '关联用户ID',
    company_name        VARCHAR(100)    NOT NULL                 COMMENT '企业名称',
    industry            VARCHAR(100)    DEFAULT NULL             COMMENT '所属行业',
    company_size        VARCHAR(50)     DEFAULT NULL             COMMENT '企业规模',
    company_description TEXT            DEFAULT NULL             COMMENT '企业描述',
    contact_person      VARCHAR(50)     DEFAULT NULL             COMMENT '联系人',
    contact_phone       VARCHAR(20)     DEFAULT NULL             COMMENT '联系电话',
    address             VARCHAR(200)    DEFAULT NULL             COMMENT '企业地址',
    website             VARCHAR(200)    DEFAULT NULL             COMMENT '企业官网',
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_company_name (company_name),
    INDEX idx_industry (industry),
    CONSTRAINT fk_enterprise_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业档案表';

-- =============================================================================
-- 4. 职位发布表 (job_postings)
-- 职位管理 + 信息查询模块的核心表
-- 软删除: status='CLOSED' 表示职位已关闭/下架
-- 搜索策略: FULLTEXT(title,description) 处理关键词搜索
--           B-tree索引处理薪资、经验、地点等范围过滤
-- =============================================================================
CREATE TABLE job_postings (
    id                  BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '职位ID',
    enterprise_user_id  BIGINT          NOT NULL                 COMMENT '发布企业用户ID',
    title               VARCHAR(200)    NOT NULL                 COMMENT '职位名称',
    description         TEXT            NOT NULL                 COMMENT '职位描述',
    required_major      VARCHAR(100)    DEFAULT NULL             COMMENT '专业要求',
    min_experience      INT             DEFAULT NULL             COMMENT '最低工作年限要求',
    max_experience      INT             DEFAULT NULL             COMMENT '最高工作年限要求',
    min_salary          INT             DEFAULT NULL             COMMENT '最低薪资（月薪/元）',
    max_salary          INT             DEFAULT NULL             COMMENT '最高薪资（月薪/元）',
    location            VARCHAR(100)    DEFAULT NULL             COMMENT '工作地点',
    status              ENUM('OPEN', 'CLOSED')
                                        NOT NULL DEFAULT 'OPEN'  COMMENT '职位状态：开放/关闭',
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    updated_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_enterprise_user (enterprise_user_id),
    INDEX idx_status (status),
    INDEX idx_required_major (required_major),
    INDEX idx_location (location),
    INDEX idx_min_salary (min_salary),
    INDEX idx_max_salary (max_salary),
    INDEX idx_min_experience (min_experience),
    INDEX idx_max_experience (max_experience),
    INDEX idx_created_at (created_at),
    FULLTEXT INDEX ft_title_desc (title, description) WITH PARSER ngram,
    CONSTRAINT fk_job_enterprise FOREIGN KEY (enterprise_user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位发布表';

-- =============================================================================
-- 5. 职位订阅表 (subscriptions)
-- 信息互动模块 — 用户可创建多个订阅条件，系统自动匹配职位
-- =============================================================================
CREATE TABLE subscriptions (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '订阅ID',
    user_id         BIGINT          NOT NULL                 COMMENT '订阅用户ID',
    keywords        VARCHAR(500)    DEFAULT NULL             COMMENT '关键词，逗号分隔',
    major           VARCHAR(100)    DEFAULT NULL             COMMENT '专业筛选',
    min_salary      INT             DEFAULT NULL             COMMENT '最低薪资筛选',
    work_experience INT             DEFAULT NULL             COMMENT '工作年限筛选',
    location        VARCHAR(100)    DEFAULT NULL             COMMENT '工作地点筛选',
    enabled         TINYINT(1)      NOT NULL DEFAULT 1       COMMENT '是否启用：1启用 0禁用',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_enabled (enabled),
    CONSTRAINT fk_sub_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位订阅表';

-- =============================================================================
-- 6. 职位匹配通知表 (notifications)
-- 信息互动模块 — 存储订阅匹配结果的历史快照
-- job_title/company_name 冗余存储，防止源数据变更后历史通知信息丢失
-- =============================================================================
CREATE TABLE notifications (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '通知ID',
    subscription_id BIGINT          NOT NULL                 COMMENT '关联订阅ID',
    user_id         BIGINT          NOT NULL                 COMMENT '接收通知的用户ID',
    job_id          BIGINT          NOT NULL                 COMMENT '匹配的职位ID',
    job_title       VARCHAR(200)    NOT NULL                 COMMENT '职位名称快照',
    company_name    VARCHAR(100)    NOT NULL                 COMMENT '企业名称快照',
    matched_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '匹配时间',
    is_read         TINYINT(1)      NOT NULL DEFAULT 0       COMMENT '是否已读：1已读 0未读',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_user_read (user_id, is_read),
    INDEX idx_subscription (subscription_id),
    INDEX idx_matched_at (matched_at),
    CONSTRAINT fk_notif_sub FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_notif_job FOREIGN KEY (job_id) REFERENCES job_postings(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位匹配通知表';

-- =============================================================================
-- 7. 行业分析报告表 (industry_reports)
-- 行业动态模块 — 系统生成的行业分析报告
-- content 字段存储 Markdown 格式的完整报告内容
-- =============================================================================
CREATE TABLE industry_reports (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '报告ID',
    title           VARCHAR(200)    NOT NULL                 COMMENT '报告标题',
    industry        VARCHAR(100)    NOT NULL                 COMMENT '所属行业',
    period_start    DATE            NOT NULL                 COMMENT '统计周期起始日期',
    period_end      DATE            NOT NULL                 COMMENT '统计周期结束日期',
    summary         TEXT            DEFAULT NULL             COMMENT '报告摘要',
    content         MEDIUMTEXT      DEFAULT NULL             COMMENT '报告正文（Markdown格式）',
    generated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_industry (industry),
    INDEX idx_period (period_start, period_end),
    INDEX idx_generated_at (generated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='行业分析报告表';

-- =============================================================================
-- 8. 薪资趋势数据表 (salary_trends)
-- 行业动态模块 — 报告的子表，存储薪资变化趋势数据
-- =============================================================================
CREATE TABLE salary_trends (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '记录ID',
    report_id       BIGINT          NOT NULL                 COMMENT '关联报告ID',
    period          VARCHAR(50)     NOT NULL                 COMMENT '统计周期标识，如"2024-Q1"',
    avg_salary      INT             NOT NULL                 COMMENT '平均薪资（月薪/元）',
    job_count       INT             NOT NULL DEFAULT 0       COMMENT '该周期职位数量',
    PRIMARY KEY (id),
    INDEX idx_report_id (report_id),
    INDEX idx_period (period),
    CONSTRAINT fk_trend_report FOREIGN KEY (report_id) REFERENCES industry_reports(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='薪资趋势数据表';

-- =============================================================================
-- 9. 需求分布数据表 (demand_distributions)
-- 行业动态模块 — 报告的子表，存储需求分布数据
-- 可按专业(MAJOR)、地点(LOCATION)、经验(EXPERIENCE)等维度分布
-- =============================================================================
CREATE TABLE demand_distributions (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '记录ID',
    report_id       BIGINT          NOT NULL                 COMMENT '关联报告ID',
    dimension_type  ENUM('MAJOR', 'LOCATION', 'EXPERIENCE')
                                    NOT NULL                 COMMENT '分布维度：专业/地点/经验',
    label           VARCHAR(100)    NOT NULL                 COMMENT '分布标签，如"计算机科学"',
    count           INT             NOT NULL DEFAULT 0       COMMENT '职位数量',
    percentage      DECIMAL(5,2)    NOT NULL DEFAULT 0.00    COMMENT '占比百分比（0.00-100.00）',
    PRIMARY KEY (id),
    INDEX idx_report_id (report_id),
    INDEX idx_dimension_label (dimension_type, label),
    CONSTRAINT fk_demand_report FOREIGN KEY (report_id) REFERENCES industry_reports(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求分布数据表';

-- =============================================================================
-- 10. 论坛分类表 (forum_categories)
-- 求职论坛模块 — 帖子分类（如"求职经验"、"技术交流"、"行业资讯"等）
-- =============================================================================
CREATE TABLE forum_categories (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '分类ID',
    name            VARCHAR(50)     NOT NULL                 COMMENT '分类名称',
    description     VARCHAR(200)    DEFAULT NULL             COMMENT '分类描述',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛分类表';

-- =============================================================================
-- 11. 论坛帖子表 (forum_posts)
-- 求职论坛模块 — 核心内容表
-- 软删除: is_deleted=1 表示已删除
-- like_count/comment_count 为冗余计数器，由应用层维护
-- =============================================================================
CREATE TABLE forum_posts (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '帖子ID',
    category_id     BIGINT          NOT NULL                 COMMENT '所属分类ID',
    user_id         BIGINT          NOT NULL                 COMMENT '发帖用户ID',
    title           VARCHAR(200)    NOT NULL                 COMMENT '帖子标题',
    content         TEXT            NOT NULL                 COMMENT '帖子内容',
    like_count      INT             NOT NULL DEFAULT 0       COMMENT '点赞数（冗余计数）',
    comment_count   INT             NOT NULL DEFAULT 0       COMMENT '评论数（冗余计数）',
    is_deleted      TINYINT(1)      NOT NULL DEFAULT 0       COMMENT '软删除标记：1已删除 0正常',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_category (category_id),
    INDEX idx_user (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_popular (like_count, comment_count),
    FULLTEXT INDEX ft_title_content (title, content) WITH PARSER ngram,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES forum_categories(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛帖子表';

-- =============================================================================
-- 12. 论坛评论表 (forum_comments)
-- 求职论坛模块 — 帖子下的评论
-- 软删除: is_deleted=1 表示已删除
-- 仅支持一级评论，不支持嵌套回复（符合API spec的扁平评论模型）
-- =============================================================================
CREATE TABLE forum_comments (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '评论ID',
    post_id         BIGINT          NOT NULL                 COMMENT '所属帖子ID',
    user_id         BIGINT          NOT NULL                 COMMENT '评论用户ID',
    content         TEXT            NOT NULL                 COMMENT '评论内容',
    is_deleted      TINYINT(1)      NOT NULL DEFAULT 0       COMMENT '软删除标记：1已删除 0正常',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (id),
    INDEX idx_post (post_id),
    INDEX idx_user (user_id),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES forum_posts(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛评论表';

-- =============================================================================
-- 13. 论坛点赞记录表 (forum_likes)
-- 求职论坛模块 — 帖子点赞记录
-- 唯一约束(post_id, user_id)保证同一用户不能重复点赞
-- 点赞时需同步更新 forum_posts.like_count
-- =============================================================================
CREATE TABLE forum_likes (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '点赞记录ID',
    post_id         BIGINT          NOT NULL                 COMMENT '被点赞帖子ID',
    user_id         BIGINT          NOT NULL                 COMMENT '点赞用户ID',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_post (post_id),
    INDEX idx_user (user_id),
    CONSTRAINT fk_like_post FOREIGN KEY (post_id) REFERENCES forum_posts(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛点赞记录表';

-- =============================================================================
-- 初始化数据 — 论坛默认分类
-- =============================================================================
INSERT INTO forum_categories (name, description) VALUES
    ('求职经验', '分享求职经历、面试技巧与职场心得'),
    ('技术交流', '讨论编程技术、项目经验与技术趋势'),
    ('行业资讯', '关注各行业动态、政策变化与就业形势'),
    ('职场生活', '交流职场日常、人际关系与职业发展规划'),
    ('问答求助', '求职相关问题答疑解惑');

-- =============================================================================
-- 初始化数据 — 管理员账号
-- 密码: admin123 (bcrypt加密，实际部署时请替换为真实hash)
-- =============================================================================
INSERT INTO users (username, password, user_type, email, status) VALUES
    ('admin', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ADMIN', 'admin@careernexus.com', 'ACTIVE');
