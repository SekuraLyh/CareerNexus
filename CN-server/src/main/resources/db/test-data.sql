-- =============================================================================
-- CareerNexus 求职网站 - 测试数据脚本
-- 用途: 为管理员仪表盘提供测试数据
-- 说明: 执行前请确保已运行 career-nexus-schema.sql 创建表结构
-- =============================================================================

USE CareerNexus;

-- =============================================================================
-- 1. 插入测试用户数据 (users)
-- =============================================================================

-- 管理员账号（已有，跳过）
-- INSERT INTO users (username, password, user_type, email, status) VALUES
--     ('admin', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ADMIN', 'admin@careernexus.com', 'ACTIVE');

-- 求职者用户 (10个)
INSERT INTO users (username, password, user_type, email, phone, status) VALUES
    ('zhangsan', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'zhangsan@example.com', '13800138001', 'ACTIVE'),
    ('lisi', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'lisi@example.com', '13800138002', 'ACTIVE'),
    ('wangwu', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'wangwu@example.com', '13800138003', 'ACTIVE'),
    ('zhaoliu', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'zhaoliu@example.com', '13800138004', 'ACTIVE'),
    ('sunqi', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'sunqi@example.com', '13800138005', 'ACTIVE'),
    ('zhouba', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'zhouba@example.com', '13800138006', 'ACTIVE'),
    ('wujiu', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'wujiu@example.com', '13800138007', 'ACTIVE'),
    ('zhengshi', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'zhengshi@example.com', '13800138008', 'ACTIVE'),
    ('qianshiyi', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'qianshiyi@example.com', '13800138009', 'ACTIVE'),
    ('chenshier', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'chenshier@example.com', '13800138010', 'ACTIVE');

-- 企业用户 (5个)
INSERT INTO users (username, password, user_type, email, phone, status) VALUES
    ('tech_company', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ENTERPRISE', 'hr@techcompany.com', '13900139001', 'ACTIVE'),
    ('finance_company', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ENTERPRISE', 'hr@financecompany.com', '13900139002', 'ACTIVE'),
    ('edu_company', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ENTERPRISE', 'hr@educompany.com', '13900139003', 'ACTIVE'),
    ('medical_company', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ENTERPRISE', 'hr@medicalcompany.com', '13900139004', 'ACTIVE'),
    ('retail_company', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'ENTERPRISE', 'hr@retailcompany.com', '13900139005', 'ACTIVE');

-- 已注销的用户 (2个，用于测试统计排除逻辑)
INSERT INTO users (username, password, user_type, email, phone, status) VALUES
    ('deleted_user1', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'deleted1@example.com', '13800138099', 'INACTIVE'),
    ('deleted_user2', '$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K', 'JOB_SEEKER', 'deleted2@example.com', '13800138098', 'INACTIVE');

-- =============================================================================
-- 2. 插入求职者档案数据 (job_seeker_profiles)
-- =============================================================================

INSERT INTO job_seeker_profiles (user_id, real_name, gender, birth_date, major, education, school, graduation_year, work_experience, expected_salary, skills, self_description) VALUES
    (2, '张三', 'MALE', '1995-03-15', '计算机科学与技术', 'BACHELOR', '北京大学', 2017, 6, 25000, 'Java,Spring,MySQL,Redis', '资深Java开发工程师，擅长分布式系统设计'),
    (3, '李四', 'FEMALE', '1997-07-20', '软件工程', 'BACHELOR', '清华大学', 2019, 4, 20000, 'Python,Django,PostgreSQL', '全栈开发工程师，热爱开源'),
    (4, '王五', 'MALE', '1993-11-08', '电子信息工程', 'MASTER', '上海交通大学', 2018, 5, 28000, 'C++,Linux,嵌入式开发', '嵌入式系统专家'),
    (5, '赵六', 'FEMALE', '1998-01-25', '数据科学', 'MASTER', '复旦大学', 2020, 3, 22000, 'Python,TensorFlow,PyTorch', '机器学习工程师，专注NLP领域'),
    (6, '孙七', 'MALE', '1996-05-12', '网络工程', 'BACHELOR', '浙江大学', 2018, 5, 23000, 'Go,Kubernetes,Docker', '云原生架构师'),
    (7, '周八', 'FEMALE', '1999-09-30', '信息安全', 'BACHELOR', '南京大学', 2021, 2, 18000, '网络安全,渗透测试', '安全工程师'),
    (8, '吴九', 'MALE', '1994-12-18', '人工智能', 'PHD', '中国科学院', 2019, 4, 35000, 'AI,深度学习,计算机视觉', 'AI研究员'),
    (9, '郑十', 'FEMALE', '2000-02-14', '数字媒体技术', 'BACHELOR', '武汉大学', 2022, 1, 15000, 'Unity,C#,游戏开发', '游戏开发工程师'),
    (10, '钱十一', 'MALE', '1992-08-05', '自动化', 'MASTER', '哈尔滨工业大学', 2017, 6, 26000, 'PLC,SCADA,工业自动化', '工业自动化专家'),
    (11, '陈十二', 'FEMALE', '1996-04-22', '通信工程', 'BACHELOR', '华中科技大学', 2018, 5, 21000, '5G,通信协议', '通信工程师');

-- =============================================================================
-- 3. 插入企业档案数据 (enterprise_profiles)
-- =============================================================================

INSERT INTO enterprise_profiles (user_id, company_name, industry, company_size, company_description, contact_person, contact_phone, address, website) VALUES
    (12, '腾讯科技有限公司', '互联网', '10000+', '全球领先的互联网科技公司', '张HR', '0755-88888888', '深圳市南山区科技园', 'https://www.tencent.com'),
    (13, '阿里巴巴集团', '电子商务', '10000+', '全球最大的电子商务平台', '李HR', '0571-88888888', '杭州市西湖区', 'https://www.alibaba.com'),
    (14, '华为技术有限公司', '通信技术', '10000+', '全球领先的ICT解决方案提供商', '王HR', '0755-28888888', '深圳市龙岗区', 'https://www.huawei.com'),
    (15, '字节跳动', '互联网', '5000-10000', '全球领先的科技创新公司', '赵HR', '010-88888888', '北京市海淀区', 'https://www.bytedance.com'),
    (16, '美团点评', '本地生活', '5000-10000', '中国领先的生活服务电子商务平台', '孙HR', '010-68888888', '北京市朝阳区', 'https://www.meituan.com');

-- =============================================================================
-- 4. 插入职位发布数据 (job_postings)
-- =============================================================================

-- 开放中的职位 (15个)
INSERT INTO job_postings (enterprise_user_id, title, description, required_major, min_experience, max_experience, min_salary, max_salary, location, status) VALUES
    (12, '高级Java开发工程师', '负责核心业务系统开发，参与架构设计', '计算机科学与技术', 3, 8, 25000, 40000, '深圳', 'OPEN'),
    (12, '前端开发工程师', '负责Web前端开发，优化用户体验', '软件工程', 2, 5, 20000, 35000, '深圳', 'OPEN'),
    (12, '产品经理', '负责产品规划和需求分析', '不限', 3, 7, 22000, 38000, '深圳', 'OPEN'),
    (13, '算法工程师', '负责推荐算法研发和优化', '计算机科学', 2, 6, 30000, 50000, '杭州', 'OPEN'),
    (13, '数据分析师', '负责业务数据分析和可视化', '统计学', 1, 4, 18000, 30000, '杭州', 'OPEN'),
    (13, '运维工程师', '负责服务器运维和监控', '网络工程', 2, 5, 20000, 32000, '杭州', 'OPEN'),
    (14, '5G通信工程师', '负责5G技术研发', '通信工程', 3, 8, 28000, 45000, '深圳', 'OPEN'),
    (14, '嵌入式开发工程师', '负责嵌入式系统开发', '电子信息工程', 2, 6, 25000, 40000, '深圳', 'OPEN'),
    (15, 'AI研究员', '负责人工智能算法研究', '人工智能', 3, 10, 35000, 60000, '北京', 'OPEN'),
    (15, '后端开发工程师', '负责后端服务开发', '计算机科学', 2, 5, 22000, 38000, '北京', 'OPEN'),
    (15, 'UI设计师', '负责产品界面设计', '设计类', 1, 4, 18000, 30000, '北京', 'OPEN'),
    (16, 'Android开发工程师', '负责移动端应用开发', '软件工程', 2, 5, 20000, 35000, '北京', 'OPEN'),
    (16, 'iOS开发工程师', '负责iOS应用开发', '软件工程', 2, 5, 20000, 35000, '北京', 'OPEN'),
    (16, '测试工程师', '负责软件质量保证', '计算机科学', 1, 4, 15000, 25000, '北京', 'OPEN'),
    (16, 'DevOps工程师', '负责CI/CD流程建设', '计算机科学', 2, 6, 22000, 38000, '北京', 'OPEN');

-- 已关闭的职位 (3个，用于测试统计排除逻辑)
INSERT INTO job_postings (enterprise_user_id, title, description, required_major, min_experience, max_experience, min_salary, max_salary, location, status) VALUES
    (12, '实习生-Java开发', '面向在校学生的实习岗位', '计算机相关专业', 0, 0, 5000, 8000, '深圳', 'CLOSED'),
    (13, '实习生-产品运营', '产品运营实习岗位', '不限', 0, 0, 4000, 6000, '杭州', 'CLOSED'),
    (14, '实习生-硬件测试', '硬件测试实习岗位', '电子工程', 0, 0, 4500, 7000, '深圳', 'CLOSED');

-- =============================================================================
-- 5. 插入行业分析报告数据 (industry_reports)
-- =============================================================================

INSERT INTO industry_reports (title, industry, period_start, period_end, summary, content) VALUES
    ('2024年Q1互联网行业人才趋势报告', '互联网', '2024-01-01', '2024-03-31', '本季度互联网行业招聘需求持续增长，AI相关岗位薪资涨幅明显', '# 2024年Q1互联网行业人才趋势报告\n\n## 概述\n本季度互联网行业整体招聘需求环比增长15%...'),
    ('2024年Q1金融行业薪酬分析报告', '金融', '2024-01-01', '2024-03-31', '金融行业平均薪资稳中有升，量化交易岗位需求旺盛', '# 2024年Q1金融行业薪酬分析报告\n\n## 市场概况\n金融行业薪资水平持续保持高位...'),
    ('2024年Q1制造业数字化转型报告', '制造业', '2024-01-01', '2024-03-31', '制造业数字化人才缺口扩大，智能制造相关岗位增长迅速', '# 2024年Q1制造业数字化转型报告\n\n## 行业背景\n随着工业4.0推进...'),
    ('2024年Q1医疗健康行业就业报告', '医疗健康', '2024-01-01', '2024-03-31', '医疗健康行业人才需求稳定，远程医疗相关岗位兴起', '# 2024年Q1医疗健康行业就业报告\n\n## 行业现状\n后疫情时代医疗健康行业持续发展...'),
    ('2024年Q1教育行业人才流动报告', '教育', '2024-01-01', '2024-03-31', '在线教育平台人才需求回暖，职业教育领域增长显著', '# 2024年Q1教育行业人才流动报告\n\n## 市场分析\n教育行业经历调整后逐步恢复...'),
    ('2024年Q2互联网行业人才趋势报告', '互联网', '2024-04-01', '2024-06-30', '二季度互联网行业招聘热度不减，大模型相关岗位成为新热点', '# 2024年Q2互联网行业人才趋势报告\n\n## 季度总结\n大模型技术爆发带动相关岗位需求...'),
    ('2024年Q2新能源行业就业报告', '新能源', '2024-04-01', '2024-06-30', '新能源行业人才竞争加剧，电池技术和储能领域需求旺盛', '# 2024年Q2新能源行业就业报告\n\n## 行业发展\n双碳目标推动下新能源行业快速发展...'),
    ('2024年Q2零售业数字化转型报告', '零售', '2024-04-01', '2024-06-30', '新零售模式推动零售行业人才结构升级', '# 2024年Q2零售业数字化转型报告\n\n## 转型趋势\n线上线下融合成为主流...');

-- =============================================================================
-- 6. 插入论坛帖子数据 (forum_posts)
-- =============================================================================

-- 正常帖子 (20个)
INSERT INTO forum_posts (category_id, user_id, title, content, like_count, comment_count, is_deleted) VALUES
    (1, 2, '分享我的BAT面试经验', '最近拿到了某大厂的offer，分享一下面试经验和准备过程...', 156, 23, 0),
    (1, 3, '应届生如何准备春招？', '作为应届生，想请教各位大佬如何高效准备春招...', 89, 15, 0),
    (1, 4, '工作3年，谈谈我的职业发展心得', '从初级工程师到高级工程师，这3年我学到了什么...', 234, 45, 0),
    (2, 5, 'Spring Boot最佳实践分享', '总结了项目中用到的一些Spring Boot最佳实践...', 178, 32, 0),
    (2, 6, '微服务架构设计思路', '聊聊微服务拆分的原则和注意事项...', 267, 56, 0),
    (2, 7, 'Docker容器化部署实战', '分享Docker在项目中的实际应用经验...', 145, 28, 0),
    (2, 8, 'MySQL性能优化技巧', '总结了一些MySQL优化的实用技巧...', 312, 67, 0),
    (2, 9, 'Redis缓存策略详解', '深入探讨Redis的使用场景和缓存策略...', 198, 41, 0),
    (3, 10, '2024年IT行业就业形势分析', '根据最新数据，分析一下当前IT行业的就业情况...', 423, 89, 0),
    (3, 11, '程序员35岁危机真的存在吗？', '探讨一下程序员职业发展中的年龄问题...', 567, 123, 0),
    (3, 2, '远程办公的利与弊', '经历了几年远程办公，谈谈个人感受...', 234, 56, 0),
    (4, 3, '如何平衡工作和生活？', '工作压力大，想请教大家如何平衡...', 189, 45, 0),
    (4, 4, '程序员的副业探索', '分享一些适合程序员的副业方向...', 345, 78, 0),
    (4, 5, '职场沟通技巧总结', '在职场中，良好的沟通能力很重要...', 156, 34, 0),
    (4, 6, '如何提升工作效率？', '分享一些提高工作效率的方法和工具...', 278, 52, 0),
    (5, 7, '求助：如何选择第一份工作？', '拿到几个offer，不知道该如何选择...', 98, 23, 0),
    (5, 8, '转行做程序员来得及吗？', '目前从事其他行业，想转行做开发...', 167, 45, 0),
    (5, 9, '考研还是直接工作？', '大三学生，面临选择困难...', 234, 67, 0),
    (5, 10, '简历怎么写才能吸引HR？', '投了很多简历都没回应，求指导...', 189, 38, 0),
    (5, 11, '面试时被问到不会的问题怎么办？', '分享一些应对技巧...', 145, 29, 0);

-- 已删除的帖子 (3个，用于测试统计排除逻辑)
INSERT INTO forum_posts (category_id, user_id, title, content, like_count, comment_count, is_deleted) VALUES
    (1, 2, '广告帖-培训课程推广', '这是一个广告...', 0, 0, 1),
    (2, 3, '违规内容', '违规内容已被删除', 0, 0, 1),
    (4, 4, 'spam内容', '垃圾信息', 0, 0, 1);

-- =============================================================================
-- 7. 插入职位订阅数据 (subscriptions) - 可选
-- =============================================================================

INSERT INTO subscriptions (user_id, keywords, major, min_salary, work_experience, location, enabled) VALUES
    (2, 'Java,Spring', '计算机科学与技术', 20000, 3, '深圳', 1),
    (3, 'Python,数据分析', '数据科学', 18000, 2, '杭州', 1),
    (4, '嵌入式,C++', '电子信息工程', 25000, 3, '深圳', 1),
    (5, 'AI,机器学习', '人工智能', 30000, 2, '北京', 1),
    (6, 'Go,云原生', '网络工程', 22000, 3, '北京', 1);

-- =============================================================================
-- 测试数据统计验证
-- =============================================================================

-- 验证统计数据（应该与仪表盘返回的数据一致）
SELECT 
    (SELECT COUNT(*) FROM job_postings WHERE status = 'OPEN') AS jobCount,
    (SELECT COUNT(*) FROM industry_reports) AS industryCount,
    (SELECT COUNT(*) FROM forum_posts WHERE is_deleted = 0) AS forumCount,
    (SELECT COUNT(*) FROM users WHERE status = 'ACTIVE') AS userCount;

-- 预期结果:
-- jobCount: 15 (开放中的职位)
-- industryCount: 8 (行业报告)
-- forumCount: 20 (未删除的帖子)
-- userCount: 17 (10个求职者 + 5个企业 + 1个管理员 + 1个已存在的admin，排除2个已注销的)
