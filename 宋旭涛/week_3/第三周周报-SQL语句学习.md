# 学习周报：SQL 语句学习与实践
周期：2026年7月20日 - 2026年7月26日
主题：SQL 语句学习

## 一、本周学习概述
本周集中学习了关系型数据库中最常用的 SQL 语句，涵盖数据定义（DDL）、数据操作（DML）、数据查询（DQL）和数据控制（DCL）四大类别，并通过实际建表、增删改查及复杂查询练习，掌握了 SQL 的核心语法与常见应用场景。

## 二、核心学习内容
### 2.1 数据定义语言（DDL）
库操作：CREATE DATABASE、DROP DATABASE、USE。

表操作：CREATE TABLE（含字段类型、约束如 PRIMARY KEY、FOREIGN KEY、NOT NULL、UNIQUE）、ALTER TABLE（添加/修改/删除列）、DROP TABLE、TRUNCATE TABLE。

索引：CREATE INDEX、DROP INDEX，理解索引对查询性能的影响。

### 2.2 数据操作语言（DML）
插入数据：INSERT INTO ... VALUES，支持单条或多条插入。

更新数据：UPDATE ... SET ... WHERE，注意 WHERE 条件避免全表更新。

删除数据：DELETE FROM ... WHERE 与 TRUNCATE 的区别（删除 vs 重置）。

### 2.3 数据查询语言（DQL）—— 重点
基础查询：SELECT、DISTINCT、WHERE（比较运算符、逻辑运算符、IN、BETWEEN、LIKE 模糊匹配）、ORDER BY、LIMIT 分页。

聚合函数：COUNT、SUM、AVG、MAX、MIN，结合 GROUP BY 分组，使用 HAVING 过滤分组。

多表连接：INNER JOIN、LEFT JOIN、RIGHT JOIN、FULL JOIN，以及自连接。

子查询：WHERE 子句中的标量子查询、FROM 中的派生表、EXISTS 相关子查询。

集合操作：UNION、UNION ALL、INTERSECT、EXCEPT。

### 2.4 数据控制语言（DCL）
权限管理：GRANT、REVOKE，了解常见权限（SELECT、INSERT、UPDATE、DELETE、ALL）。

## 三、本周实践进展
### 3.1 已完成事项
在 MySQL 中创建了 student、course、score 三张表，并插入模拟数据。

编写了 20+ 条查询语句，涵盖单表查询、多表连接、分组统计、子查询和窗口函数。

练习了事务控制（BEGIN、COMMIT、ROLLBACK），理解 ACID 特性。

### 3.2 关键代码示例
```sql
-- 多表连接查询：查询学生姓名、课程名称及成绩
SELECT s.name, c.course_name, sc.score
FROM student s
JOIN score sc ON s.id = sc.student_id
JOIN course c ON sc.course_id = c.id
WHERE sc.score >= 60
ORDER BY sc.score DESC;

-- 窗口函数：按课程分组，对学生成绩排名
SELECT student_id, course_id, score,
       RANK() OVER (PARTITION BY course_id ORDER BY score DESC) AS rank_in_course
FROM score;
```
## 四、问题与解决方案
### 4.1 左连接与内连接结果混淆
问题：使用 LEFT JOIN 时未注意右表无匹配记录会返回 NULL，导致统计结果偏差。

解决：明确业务需求，若需保留左表全部记录则使用 LEFT JOIN，否则使用 INNER JOIN，并在 SELECT 中用 COALESCE 处理空值。

### 4.2 GROUP BY 与 HAVING 的误用
问题：在 HAVING 中使用未在 SELECT 中出现的非聚合字段，导致语法错误。

解决：牢记 HAVING 仅用于过滤分组后的聚合结果，非聚合条件应放在 WHERE 中。

### 4.3 索引失效导致查询缓慢
问题：在 WHERE 中对索引列使用函数或隐式类型转换，导致索引失效。

解决：避免在索引列上使用函数，确保数据类型匹配，使用 EXPLAIN 分析执行计划。

## 五、下周学习计划
SQL 进阶：学习存储过程、触发器、视图，理解事务隔离级别。


实战项目：将 SQL 与 Spring Boot 结合，使用 MyBatis 操作数据库，完成带复杂查询的接口开发。

性能调优：学习慢查询日志分析、索引优化策略。

## 六、总结与思考
本周系统梳理了 SQL 的核心语法，从建表到复杂查询都有了实操经验。SQL 作为后端开发的必备技能，其灵活性和表达力远超预期。通过大量练习，我意识到编写高效 SQL 的关键不仅在于语法正确，更在于对数据逻辑和性能的理解。后续将把 SQL 与编程语言结合，在实际项目中不断打磨查询优化能力，为数据驱动的应用开发打好基础。