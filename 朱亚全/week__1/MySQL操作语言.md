# SQL 语法全面总结

---

## 1. DDL（数据定义语言）

### 1.1 数据库操作

```sql
-- 查看所有数据库
SHOW DATABASES;

-- 查看当前使用的数据库
SELECT DATABASE();

-- 创建数据库
CREATE DATABASE [IF NOT EXISTS] 数据库名 
    [DEFAULT CHARSET 字符集] 
    [COLLATE 排序规则];

-- 删除数据库
DROP DATABASE [IF EXISTS] 数据库名;

-- 切换/使用数据库
USE 数据库名;
```

### 1.2 数据表操作 - 创建

```sql
-- 创建表
CREATE TABLE 表名 (
    字段名 字段类型 [约束] COMMENT '注释'
) COMMENT '表注释';

-- 查看当前数据库所有表
SHOW TABLES;

-- 查询表结构
DESC 表名;

-- 查询指定表的建表语句
SHOW CREATE TABLE 表名;

-- 删除表
DROP TABLE [IF EXISTS] 表名;

-- 删除表并重新创建（清空数据）
TRUNCATE TABLE 表名;
```

### 1.3 数据表操作 - 修改

```sql
-- 添加字段
ALTER TABLE 表名 ADD 字段名 类型 [约束] [COMMENT '注释'];

-- 修改字段数据类型
ALTER TABLE 表名 MODIFY 字段名 新数据类型;

-- 修改字段名和字段类型
ALTER TABLE 表名 CHANGE 旧字段名 新字段名 类型 [约束] [COMMENT '注释'];

-- 删除字段
ALTER TABLE 表名 DROP 字段名;

-- 修改表名
ALTER TABLE 表名 RENAME TO 新表名;
```

---

## 2. DML（数据操作语言）

```sql
-- 添加数据
INSERT INTO 表名 [(字段1, 字段2, ...)] VALUES (值1, 值2, ...);

-- 修改数据
UPDATE 表名 SET 字段名 = 值 [WHERE 条件];

-- 删除数据
DELETE FROM 表名 [WHERE 条件];
```

---

## 3. DQL（数据查询语言）

### 3.1 基本查询语法

```sql
SELECT [DISTINCT] 字段名 [AS 别名]
FROM 表名
[WHERE 条件]              -- 分组前过滤
[GROUP BY 分组字段]       -- 分组
[HAVING 条件]             -- 分组后过滤
[ORDER BY 排序字段 [ASC|DESC]]  -- 排序：ASC升序（默认）/ DESC降序
[LIMIT 起始索引, 每页条数];      -- 分页
```

### 3.2 执行顺序

```
FROM → WHERE → GROUP BY → SELECT → HAVING → DISTINCT → ORDER BY → LIMIT
```

### 3.3 WHERE 条件过滤

| 类型 | 操作符/关键字 | 说明 |
|------|--------------|------|
| 比较运算符 | `=`, `>`, `<`, `>=`, `<=`, `<>`/`!=` | 比较大小 |
| 逻辑运算符 | `AND`/`&&`, `OR`/`||`, `NOT`/`!` | 组合多个条件 |
| 范围查询 | `BETWEEN ... AND ...` | 在某个范围内 |
| 集合查询 | `IN`, `NOT IN` | 在/不在集合中 |
| 模糊查询 | `LIKE` | `_` 匹配单个字符，`%` 匹配任意多个字符 |
| 正则表达式 | `REGEXP` | 正则匹配 |

### 3.4 聚合函数

| 函数 | 说明 |
|------|------|
| `COUNT([DISTINCT] 字段)` | 统计行数 |
| `MAX(字段)` | 最大值 |
| `MIN(字段)` | 最小值 |
| `AVG(字段)` | 平均值 |
| `SUM(字段)` | 求和 |

---

## 4. 示例查询

```sql
-- 查询所有数据
SELECT * FROM emp;

-- 查询所有员工的姓名、年龄、城市
SELECT name, age, address FROM emp;

-- 查询有哪些城市（去重）
SELECT DISTINCT address FROM emp;

-- 查询年龄大于40岁的员工
SELECT name FROM emp WHERE age > 40;

-- 查询年龄在 20-40 岁之间的女性员工
SELECT name FROM emp WHERE age BETWEEN 20 AND 40 AND gender = '女';

-- 查询城市为"北京"或"上海"的员工
SELECT name, address FROM emp WHERE address = '北京' OR address = '上海';

-- 查询姓名中包含"三"的员工
SELECT name FROM emp WHERE name LIKE '%三%';

-- 查询所有员工，按年龄从大到小排序
SELECT name, age FROM emp ORDER BY age DESC;

-- 查询所有员工，先按城市升序，再按年龄降序
SELECT name, address, age FROM emp ORDER BY address, age DESC;

-- 查询第2页的数据（每页3条，起始索引从0开始）
SELECT * FROM emp LIMIT 3, 3;   -- 跳过3条，取3条（第2页）

-- 统计每个城市的员工人数
SELECT address, COUNT(*) FROM emp GROUP BY address;

-- 统计每个城市的平均年龄和人数
SELECT address, AVG(age), COUNT(*) FROM emp GROUP BY address;

-- 统计每个城市和性别的组合人数
SELECT address, gender, COUNT(*) FROM emp GROUP BY address, gender;

-- 统计各城市的人数，只显示人数大于2的城市
SELECT address, COUNT(*) AS c FROM emp GROUP BY address HAVING c > 2;

-- 统计不同城市中，不同性别的人数分布
SELECT address, gender, COUNT(*) FROM emp GROUP BY address, gender;

-- 统计每一年入职的人数
SELECT YEAR(time), COUNT(*) FROM emp GROUP BY YEAR(time) ORDER BY YEAR(time);
```

---

## 5. DCL（数据控制语言）

用于管理数据库用户和控制访问权限。

### 5.1 用户管理

```sql
-- 切换到 mysql 数据库
USE mysql;

-- 查询所有用户
SELECT * FROM user;

-- 创建用户
CREATE USER '用户名'@'主机名' IDENTIFIED BY '密码';

-- 修改用户密码
ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '新密码';

-- 删除用户
DROP USER '用户名'@'主机名';
```

### 5.2 权限管理

```sql
-- 查询用户权限
SHOW GRANTS FOR '用户名'@'主机名';

-- 授予权限
GRANT 权限列表 ON 数据库名.表名 TO '用户名'@'主机名';

-- 撤销权限
REVOKE 权限列表 ON 数据库名.表名 FROM '用户名'@'主机名';
```

---
