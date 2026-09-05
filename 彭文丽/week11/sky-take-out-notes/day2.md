# day2

## 员工管理

### 新增

需求分析设计

![image\.png](图片和附件/image%205.png)

![image\.png](图片和附件/image%203.png)

![image\.png](图片和附件/image%2010.png)

![image\.png](图片和附件/image%209.png)

Beanutils（对象属性拷贝）

digestutils

不要硬编码 用一个常量来维护 状态常量类 启用和禁用  

![image\.png](图片和附件/image%2013.png)

为什么pojo都要实现serializable

标识： 

序列化：对象\-》字节序列（存文件发网络请求）

反序列化：字节序列\-》对象（恢复可用）

很多中间件默认java原生序列化，否则报错

localdatetime\.now是因为就是public static

后期改为当前用户的id

？如何让自己写注释的sql有提示  alt\+enter

配置：camel

![image\.png](图片和附件/image%201.png)

？有一个jwt令牌

？Map\<String,Object\> claims=new HashMap\<\>\(\)面向接口编程

？配置属性类

- bug：

2026\-07\-31 16:18:22\.673 ERROR 21060 \-\-\- \[nio\-8080\-exec\-2\] o\.a\.c\.c\.C\.\[\.\[\.\[/\]\.\[dispatcherServlet\]    : Servlet\.service\(\) for servlet \[dispatcherServlet\] in context with path \[\] threw exception \[Request processing failed; nested exception is io\.jsonwebtoken\.security\.WeakKeyException: The signing key's size is 48 bits which is not secure enough for the HS256 algorithm\.  The JWT JWA Specification \(RFC 7518, Section 3\.2\) states that keys used with HS256 MUST have a size \>= 256 bits \(the key size must be greater than or equal to the hash output size\)\.  Consider using the io\.jsonwebtoken\.security\.Keys class's 'secretKeyFor\(SignatureAlgorithm\.HS256\)' method to create a key guaranteed to be secure enough for HS256\.  See https://tools\.ietf\.org/html/rfc7518\#section\-3\.2 for more information\.\] with root cause

你的 `JwtUtil.createJWT` 里用的 `secretKey` 字符串长度太短，只有 6 个字节（48 bit），而 HS256 算法强制要求 ≥ 256 bit（32 字节）在配置用改一下secretkey

统一的一个全局的token

1. 直接先注释掉interceptor

2. 配置一个全局的token注意timemillis（过期时间）

![image\.png](图片和附件/image.png)

```Java
//lombok builder
EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.*builder*()
        .id(employee.getId())
        .userName(employee.getUsername())
        .name(employee.getName())
        .token(token)
        .build();
```



异常处理

处理抛出有重复username的人

![image\.png](图片和附件/image%2011.png)

每一次请求是一个线程 是一个存储空间  

BaseContexts \(threadlocal\)

why在common\-》因为差不多是类似与工具类，常量

![image\.png](图片和附件/image%2018.png)

![image\.png](图片和附件/image%207.png)

![image\.png](图片和附件/image%202.png)

![image\.png](图片和附件/image%2015.png)

![image\.png](图片和附件/image%2012.png)

？没有抗冻aop拦截器 有什么用 就是 autofill

接口文档里面有一个必须和非必须传入就是其实是用

@notnull和@valid来实现的；and 全局异常处理器 baseexception（why 用baseexception

？employeeservice   接口

### 分页

?为什么就是有的就是record为0 total为2

![image\.png](图片和附件/image%206.png)



![image\.png](图片和附件/image%2016.png)

Query ？通过地址栏？来传参

？是不是可以深入了解一下pagehelper

？必须 notnull   valid

Why 用pagehelper 

1. 原来不用 查询两个  一个total 一个分页的结果

```Java
<!-- 查总数 -->
<select id="countByCondition" resultType="long">
    select count(*) from employee 
    <where> ... 各种条件 ... </where>
</select>

<!-- 查分页数据（必须手动拼接 limit） -->
<select id="selectByConditionWithLimit" resultType="Employee">
    select * from employee 
    <where> ... 各种条件 ... </where>
    order by create_time desc 
    limit #{offset}, #{pageSize}  <!-- 这里必须手动传偏移量 -->
</select>
```

![image\.png](图片和附件/image%204.png)

运行的逻辑方式：分页的sql不用写limit，pagehelper会拦截之后加上/sql拦截器

page第一页

pagesize每一页多少个

total：所有满足where条件的 业务方面 pagehelper属于是如何展示

resutl/records：对应要展示的数据

![image\.png](图片和附件/image%2019.png)

#### 一张图看清全过程（超直观）

底层还是threadlocal

假设数据库有 101 条姓张的员工，你想看第 2 页（每页 10 条）：

#### 状态码

为什么这里records没有数据

\{

"code": 1,

"msg": null,

"data": \{

"total": 2,

"records": \[\]

\}

\}

Cannot resolve symbol 'name'你：这个没有影响可以正常运行

![image\.png](图片和附件/image%2017.png)

推荐第二种方式在

#### new 格式处理

？具体理解第二种方法

```Java

    */***
*     * 扩展Spring MVC框架的消息转化器*
*     * @param converters*
*     */*
*    *protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        *log*.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转化器加入容器中
        converters.add(0,converter);
    }
}
*/***
* * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象*
* * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]*
* * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]*
* */*
//消息转化器
    //在server-config-webmvnconfiguration
    //对象转化器JacksonobjectMapper
//默认是由消息转化器  加上下标可以提到前面
```

返回result中有data值就是要指定泛型；

如果是没有那就是Result\<void\>;

mapper\.xml什么时候要加上parametertype

可以不写全类名\-》有配置还是写比较好



### 启用禁用



### 编辑

## 分类管理

一个比较基础的功能，是后面很多的基础

跟前面的差不多就是操作的表不同；

![image\.png](图片和附件/image%2014.png)

唯一加了一个unique约束

![image\.png](图片和附件/image%208.png)

？可以自己敲一遍

