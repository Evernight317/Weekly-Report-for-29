-- DDL-数据库以及数据表的操作语法

-- 库操作
/*
show datebase;
select datebase();
create datebase [if not exists] 数据库名 [default charset] [collate 排序规则]
drop datebase [if exists] 数据库名
use 数据库名
*/

-- 表操作-建立
/*
create table 表名( 字段名 字段类型 [约束] comment 注释) comment 表注释
show tables
desc 表名：查询表结构
show create table 表名：查询指定表的建表结构
drop table [if exists] 表名：删除表
truncate table 表名：删除表名并且重新创建该表
*/
-- 表操作-修改
/*
添加字段：alter table 表名 add 字段名 类型 [约束] [comment 注释]
修改字段数据类型：alter table 表名 modify 字段名 新数据类型
修改字段名和字段类型：alter table 表名 change 旧字段名 新字段名 类型 [约束] [comment 注释]
删除字段：alter table 表名 drop 字段名
修改表名：alter table 表名 rename to 新表名
*/


-- DML-数据操作语法
/*
添加数据：insert into 表名 [(字段……)] values (值)
修改数据：update 表名 set 字段名=值 [where 条件]
删除数据：delete from 表名 [where 条件]
*/


-- DQL单表查询
/*
select [distinct(去重查询)]字段名 (as 别名) 
from table
where 过滤前条件
group by 分组查询
having 过滤后查询
order by 排序 1. asc 升序 2. desc 降序 
limit 起始页，每页展示数
*/
-- 执行顺序
/*
form -> where -> group by -> select -> having -> distinct -> order by -> limit
*/
-- 关于where过滤条件
/*
比较运算符
逻辑运算符
范围查询：between and 
集合查询：in, not in
模糊查询：like _：任意单个字符，%：任意多个字符
正则表达式查询：regexp
*/
-- 聚合函数
/*
count([distinct(去重计数)] 字段)
max
min
avg
sum
*/
select*from emp;
-- 查询所有员工的姓名、性别、城市
select name, age, address from emp;
-- 查询有哪些城市（去重）
select distinct address from emp;
-- 查询年龄大于40岁的员工
select name from emp where age > 40;
-- 查询年龄在 30-40 岁之间的女性员工
select name from emp where age between 20 and 40 && gender = '女';
-- 查询城市为"北京"或"上海"的员工
select name, address from emp where address = '北京' || address = '上海';
-- 查询姓名中包含"三"的员工
select name from emp where name like '%三%';
-- 查询所有员工，按年龄从大到小排序
select name, age from emp order by age desc;
-- 查询所有员工，先按城市升序，再按年龄降序
select name, address, age from emp order by address, age desc;
-- 查询第2页的数据（每页显示3条）
select * from emp limit 1, 3;
-- 统计每个城市的员工人数
select address, count(*) from emp group by address;
-- 统计每个城市的平均年龄和人数
select address, avg(age), count(*)  from emp group by address;
-- 统计每个城市和性别的组合人数
select address, gender, count(*) from emp group by address, gender;
-- 统计各城市的人数，只显示人数大于2的城市
select address, count(*) as c from emp group by address having c > 2;
-- 只显示人数大于等于2的城市
select address, count(*) as c from emp group by address having c >= 2;
-- 统计不同城市中，不同性别的人数分布
select address, gender, count(*) from emp group by address, gender;
-- 统计每一年入职的人数
select year(time), count(*) from emp group by year(time) order by year(time);


-- DCL-数据库控制语言
-- 用来管理数据库用户、控制数据库的访问权限
/*
管理用户
use mysql
select * from user
创建用户
create user '用户名@主机名' identified by '密码'
修改用户密码
alter user '用户名@主机名' identified with mysql_native_password by ’新密码'
删除用户
drop user '用户名@主机名'
*/
/*
查询权限：
show grants for '用户名@主机名'
授予权限
grant 权限列表 on 数据库名 表名 to '用户名@主机名'
撤销权限
revoke 权限列表 on 数据库 表名 from '用户名@主机名'
*/
