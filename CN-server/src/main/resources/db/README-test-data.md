# 测试数据使用说明

## 📋 文件说明

- **test-data.sql**: 测试数据脚本，包含管理员仪表盘所需的各类测试数据
- **career-nexus-schema.sql**: 数据库表结构脚本（需先执行）

## 🚀 使用步骤

### 1. 确保数据库表已创建

首先执行表结构脚本（如果尚未执行）：

```bash
mysql -u root -p < career-nexus-schema.sql
```

### 2. 导入测试数据

```bash
mysql -u root -p < test-data.sql
```

或者在 MySQL 客户端中执行：

```sql
USE CareerNexus;
SOURCE F:/Develop/CareerNexusApplication/Backend/CareerNexus/CN-server/src/main/resources/db/test-data.sql;
```

### 3. 验证数据

执行脚本末尾的验证查询，应该得到以下结果：

```
+----------+---------------+------------+-----------+
| jobCount | industryCount | forumCount | userCount |
+----------+---------------+------------+-----------+
|       15 |             8 |         20 |        17 |
+----------+---------------+------------+-----------+
```

## 📊 测试数据概览

### 用户数据 (users)
- ✅ 10个求职者用户 (ACTIVE)
- ✅ 5个企业用户 (ACTIVE)
- ✅ 1个管理员用户 (ACTIVE, 已在 schema 中创建)
- ✅ 2个已注销用户 (INACTIVE) - 用于测试统计排除逻辑
- **总计**: 17个活跃用户

### 求职者档案 (job_seeker_profiles)
- ✅ 10个求职者档案，涵盖不同专业、学历、工作经验
- 包括：计算机、软件工程、电子信息、数据科学、网络工程等

### 企业档案 (enterprise_profiles)
- ✅ 5个知名企业：腾讯、阿里、华为、字节跳动、美团
- 涵盖互联网、电子商务、通信技术等行业

### 职位发布 (job_postings)
- ✅ 15个开放职位 (status='OPEN')
- ✅ 3个已关闭职位 (status='CLOSED') - 用于测试统计排除逻辑
- 薪资范围：5,000 - 60,000 元/月
- 地点：深圳、杭州、北京

### 行业分析报告 (industry_reports)
- ✅ 8份行业报告
- 涵盖：互联网、金融、制造业、医疗健康、教育、新能源、零售
- 时间跨度：2024年Q1-Q2

### 论坛帖子 (forum_posts)
- ✅ 20个正常帖子 (is_deleted=0)
- ✅ 3个已删除帖子 (is_deleted=1) - 用于测试统计排除逻辑
- 分类：求职经验、技术交流、行业资讯、职场生活、问答求助

### 职位订阅 (subscriptions)
- ✅ 5个职位订阅，用于后续功能测试

## 🔐 登录信息

所有测试用户的密码均为：`admin123` (bcrypt加密后的值为 `$2a$10$tEX0ontUusB6OWUHOygBd.bxVlTrjOvkhRAS811.nwe6BNmiMbS4K`)

### 管理员账号
- 用户名: `admin`
- 密码: `admin123`

### 求职者账号示例
- 用户名: `zhangsan`, `lisi`, `wangwu` 等
- 密码: `admin123`

### 企业账号示例
- 用户名: `tech_company`, `finance_company` 等
- 密码: `admin123`

## 🧪 测试仪表盘接口

启动项目后，访问：

```
GET http://localhost:8080/api/admin/dashboard
```

预期返回：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "jobCount": 15,
    "industryCount": 8,
    "forumCount": 20,
    "userCount": 17
  }
}
```

## ⚠️ 注意事项

1. **执行顺序**: 必须先执行 `career-nexus-schema.sql` 创建表结构，再执行 `test-data.sql` 插入数据
2. **外键约束**: 测试数据遵循外键约束，user_id 必须对应已存在的用户
3. **唯一约束**: 用户名和邮箱具有唯一性，重复执行会报错
4. **数据清理**: 如需重新导入，可先清空数据：
   ```sql
   SET FOREIGN_KEY_CHECKS = 0;
   TRUNCATE TABLE notifications;
   TRUNCATE TABLE subscriptions;
   TRUNCATE TABLE forum_likes;
   TRUNCATE TABLE forum_comments;
   TRUNCATE TABLE forum_posts;
   TRUNCATE TABLE demand_distributions;
   TRUNCATE TABLE salary_trends;
   TRUNCATE TABLE industry_reports;
   TRUNCATE TABLE job_postings;
   TRUNCATE TABLE enterprise_profiles;
   TRUNCATE TABLE job_seeker_profiles;
   TRUNCATE TABLE users;
   SET FOREIGN_KEY_CHECKS = 1;
   ```

## 📝 自定义测试数据

如需调整测试数据数量，修改对应的 INSERT 语句即可。注意保持：
- 外键关联的正确性
- 唯一约束不冲突
- 枚举值符合定义

---

**生成时间**: 2026-06-06  
**适用版本**: CareerNexus v1.0
