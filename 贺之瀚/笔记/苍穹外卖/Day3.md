# Day-03

## 公共字段自动填充

四个字段；

统一处理就需要AOP切面统一处理；

为Mapper方法加入注解：

1、自定义注解AutoFill，

2、自定义切面类AutoFillAspect，通过反射赋值？

3、加AutoFill注解

Enum标注操作类型

execution: `@annotation(com.sky.annotation.AutoFill)` 筛选有AutoFill的注解；

## 新增菜品

分类管理；

菜品表

 分类id：逻辑外键

口味表

菜品id逻辑外键

### 文件上传

commoncontroller

阿里云OSS不配置；

**引用** ：防止泄露；在主配置文件中使用引用，并且通过切换环境的方式实现快速切换配置组；

返回请求路径用于回显图片；

菜品的数据封装成DTO接收JSON；

### Debug

Mapper中数据库连不上：无论XML还是.java先把表名打出来，然后报错选择连接即可；也注意检查是否注入mysql

`useGeneratedKeys="true" ` 返回生成的主键值

## 查询菜品

有一个字段是菜品分类名；故涉及到多表查询；

## 删除菜品

删除菜品数量不确定（动态sql

起售中菜品不能删除；

被套餐关联菜品 不能删除；

关联口味数据也要删除；

事务？

请求方式：DELETE

查询参数 

**`!=null` 防空指针！**

## 修改菜品

根据id查询菜品：

路径参数，GET

修改菜品：

请求参数，PUT
