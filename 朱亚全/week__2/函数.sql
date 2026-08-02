-- 函数
-- 字符串函数
/*
concat: 合并字符串
lower: 转小写
upper：转大写
填充函数：
lpad(column_name, n, pad)
rpad(column_name, n, pad)
trim：去除空格
substring(str, start,n)：截取字符串,从1开始
*/
select * from emp;

update emp set id = lpad(id, 6, 2);

-- 数值函数
/*
ceil：向上取整
floor：向下取整
mod：取模
rand：0~1  
round(double，n)：n为小数位数
*/

select lpad(round(rand()*1000000, 0), 6, '0');
select lpad(200, 5, '-');

-- 时间函数
/*
curdate:返回当前日期
curtime：放回当前时间
now：返回当前日期和时间
year(date),month(date),day(date)
date_add(date, interval expr type):date增加expr个type类型后的时间
date_sub:date减法
datediff(date1, date2):date1-date2
*/
select month(now());
select date_add(now(), interval 2 day);
select datediff('2025-1-1', '2026-2-1');

select name, datediff(curdate(), time) as 'days' from emp order by days desc;

-- 流程函数
/*
if(value,t,f):相当于三元运算符
ifnull(value1,value2):如果value1是null则返回value2,反之返回value1
case 字段 when then else end：等值判断
case when then else end：范围/复杂条件
*/
select if(false, 'ok', 'Error');
select ifNull('ok', 'Error');
select ifNull(null, 1);
-- 查询emp表中员工姓名和工作地址（北京/上海 是一线城市，其他是二线城市）
select 
  name,
  (case address when '北京' then '一线城市' when '上海' then '一线城市' else '二线城市' end) as '工作城市'
from emp;

-- 创建一个新的学生成绩表:字段名 数据类型 comment注释
create table stu (
  name varchar(20) comment '姓名',
  class char(4) comment '班级',
  gender char(1) comment '性别',
  chinese tinyint unsigned comment '语文成绩',
  math tinyint unsigned comment '数学成绩',
  english tinyint unsigned comment '英语成绩',
  physics tinyint unsigned comment '体育成绩',
  chemistry tinyint unsigned comment'化学成绩',
  biology tinyint unsigned comment '生物成绩'
) comment '学生成绩表';
insert into stu (name, class, gender, chinese, math, english, physics, chemistry, biology) values
('张三', '高一1班', '男', 85.5, 92.0, 78.5, 88.0, 91.5, 84.0),
('李四', '高一1班', '女', 92.0, 76.5, 89.0, 93.5, 77.0, 90.5),
('王五', '高一2班', '男', 68.0, 95.0, 72.5, 65.0, 88.0, 70.0),
('赵六', '高一2班', '女', 79.0, 88.5, 94.0, 72.0, 83.5, 91.0),
('孙七', '高一1班', '男', 96.0, 81.0, 87.5, 79.0, 94.0, 82.5),
('周八', '高一3班', '女', 73.5, 67.0, 81.0, 85.5, 69.0, 78.0),
('吴九', '高一2班', '男', 45.0, 52.0, 38.0, 40.0, 55.0, 48.0),  
('郑十', '高一3班', '男', 88.0, 91.0, 86.5, 90.0, 87.0, 89.5);

select * from stu;
select 
  name as '姓名', 
  case gender
      when '男' then 1
      else 0 end as '性别代号',
  case 
      when chinese>=85 then '优秀'
      when chinese>=60 then '及格'
      else '不及格'
  end as '语文',
  case 
      when math>=90 then '优秀'
      when math>=60 then '及格'
      else '不合格'
  end as '数学'
from stu;
