Week2 
一、学习笔记/记录
 
本周学习Java基础入门与MySQL数据库，目标完成Java连接MySQL实现增删改查基础功能，核心知识点记录如下：
 
1. MySQL数据库SQL基础
 
1. SQL分为四大类别
2. SQL通用书写规范
- 语句分单行/多行编写，必须以分号 ; 结尾；关键字建议大写，方便区分阅读
- 注释分为单行 -- 注释 、 # MySQL专属注释 ，多行 /* 注释内容 */ 
3. DDL数据库操作指令
-  SHOW DATABASES;  查询本机所有数据库
-  CREATE DATABASE 库名;  创建数据库，搭配 IF NOT EXISTS 避免重复创建报错
-  DROP DATABASE IF EXISTS 库名;  删除数据库
-  USE 数据库名;  切换、使用指定数据库
4. DDL数据表操作指令
-  SHOW TABLES;  查看当前库内所有数据表
-  DESC 表名;  查看表字段结构
-  CREATE TABLE 表名(字段1 类型 注释,字段2 类型 注释);  创建数据表，最后一个字段末尾不能加逗号，可给表、字段添加注释标注含义
- 实操示例：创建 tb_user 用户表，包含id编号、name姓名、age年龄、gender性别字段
 
2. Java后端基础
 
 
二、遇到难题解决方法
 
问题：不清楚Java和MySQL怎么建立连接，不知道用什么工具打通代码与数据库
解决方法：查阅课程任务，了解需要JDBC连接驱动，先学完MySQL基础语法，再跟着教程配置，尝试连接代码。

 
三、刷题记录
 
SQL基础实操题1：创建用户数据库与用户表
 
需求：新建数据库 week2_db ，库内创建 tb_student 学生表，包含id、姓名、年龄、班级字段。
参考代码：
 
sql
  
-- 创建数据库
CREATE DATABASE IF NOT EXISTS week2_db DEFAULT CHARSET utf8mb4;
-- 切换数据库
USE week2_db;
-- 创建学生表
CREATE TABLE tb_student(
    id INT COMMENT '学生编号',
    name VARCHAR(50) COMMENT '学生姓名',
    age INT COMMENT '学生年龄',
    class VARCHAR(30) COMMENT '所在班级'
) COMMENT '学生信息表';
-- 查看数据表
SHOW TABLES;
 
 
SQL基础实操题2：查看数据表结构
 
需求：查询tb_student表内所有字段与字段类型
参考代码：
 
sql
  
DESC tb_student;
 
 
四、本周学习总结
 
本周从HTML网页结构学习切换，初步了解数据库存储数据的逻辑，掌握MySQL基础建库、建表语法，学习DML、DQL增删改查语句，再结合Java代码完成本次week2
 
