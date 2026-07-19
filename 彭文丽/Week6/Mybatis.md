# Mybatis
---
## 前提引入
**jdbc**
api接口
！[](./Images/屏幕截图%202026-06-08%20111600.png)
![](./Images/屏幕截图%202026-06-08%20111609.png)
## when 
持久层（mapper/dao）框架，简化jdbc6/9/2026, 10:38:17 AM
CSDN一键发布任务开始！

6/9/2026, 10:38:18 AM
错误信息：Failed to launch the browser process! TROUBLESHOOTING: https://github.com/puppeteer/puppeteer/blob/main/docs/troubleshooting.md ,{}

## 入门
1. pre数据
2. 创建springboot引入mybatis+mysqldriver依赖+lombok
3. 在application.properties连接数据库
！[](./Images/屏幕截图%202026-06-08%20111609.png)
4. 在mapper接口写sql语句
！[](./Images/屏幕截图%202026-06-08%20192717.png)
## 数据库连接池
！[](./Images/屏幕截图%202026-06-08%20193937.png)
springboot默认是hikari
！[](./Images/屏幕截图%202026-06-08%20195216.png)
## lomok
！[](./Images/屏幕截图%202026-06-08%20195216%20-%20副本.png)
## 基础操作
![alt text](image-2.png)
![alt text](image-3.png)
- delete
![alt text](image-1.png)
- insert
字段名和类中属性名
![alt text](image-4.png)
![alt text](image-5.png)
- update
![alt text](image-6.png)
- select
在application.properties加
![alt text](image-7.png)
![alt text](image-8.png)
- xml
比较复杂的时候
创建用.
![alt text](image-9.png)
### 动态sql
<if><where>select
![alt text](image-10.png)
<if><set>update
![alt text](image-11.png)
![alt text](image-12.png)
![alt text](image-13.png)
![alt text](image-14.png)