# day3

@AutoFill\(value = OperationType\.UPDATE\)

void update\(Category category\)

//joinpoint\-\>signature\-\>methsignature\-\>autofil;\(annotation\)\-\>value

//一整个是joinpoint     @AutoFill\(value = OperationType\.UPDATE\)

//    void update\(Category category\)

//signature是 @AutoFill\(value = OperationType\.UPDATE\)

//获取到当前被拦截的方法上的数据库操作类型

//方法签名对象

//joinpoint\-\>signature\-\>methsignature\-\>autofil;\(annotation\)\-\>value

//一整个是joinpoint     @AutoFill\(value = OperationType\.UPDATE\)

//    void update\(Category category\)

//signature是 @AutoFill\(value = OperationType\.UPDATE\)

//获取到当前被拦截的方法上的数据库操作类型

//方法签名对象//*todo*
public void autoFillPointCut\(\)\{\}

*/\*\**
* \* 前置通知，在通知中进行公共字段的赋值*
* \*/*
//执行前mapper
@Before\("autoFillPointCut\(\)"\)   void update\(Category category\)

挑一个比较难的来写个一个demo

@Bean

@ConditionalOnMissingBeanpackage com\.sky\.config;

import com\.sky\.properties\.AliOssProperties;
import com\.sky\.utils\.AliOssUtil;
import lombok\.extern\.slf4j\.Slf4j;
import org\.springframework\.boot\.autoconfigure\.condition\.ConditionalOnMissingBean;
import org\.springframework\.context\.annotation\.Bean;
import org\.springframework\.context\.annotation\.Configuration;

*/\*\**
* \* 配置类，用于创建AliOssUtil对象*
* \*/*
@Configuration
@Slf4j
public class OssConfiguration \{

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil\(AliOssProperties aliOssProperties\)\{
        *log*\.info\("开始创建阿里云文件上传工具类对象：\{\}",aliOssProperties\);
        return new AliOssUtil\(aliOssProperties\.getEndpoint\(\),
                aliOssProperties\.getAccessKeyId\(\),
                aliOssProperties\.getAccessKeySecret\(\),
                aliOssProperties\.getBucketName\(\)\);
    \}
\}package com\.sky\.utils;

import com\.aliyun\.oss\.ClientException;
import com\.aliyun\.oss\.OSS;
import com\.aliyun\.oss\.OSSClientBuilder;
import com\.aliyun\.oss\.OSSException;
import lombok\.AllArgsConstructor;
import lombok\.Data;
import lombok\.extern\.slf4j\.Slf4j;
import java\.io\.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil \{

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    */\*\**
*     \* 文件上传*
*     \**
*     \* @param bytes*
*     \* @param objectName*
*     \* @return*
*     \*/*
*    *public String upload\(byte\[\] bytes, String objectName\) \{

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder\(\)\.build\(endpoint, accessKeyId, accessKeySecret\);

        try \{
            // 创建PutObject请求。
            ossClient\.putObject\(bucketName, objectName, new ByteArrayInputStream\(bytes\)\);
        \} catch \(OSSException oe\) \{
            System\.*out*\.println\("Caught an OSSException, which means your request made it to OSS, "
                    \+ "but was rejected with an error response for some reason\."\);
            System\.*out*\.println\("Error Message:" \+ oe\.getErrorMessage\(\)\);
            System\.*out*\.println\("Error Code:" \+ oe\.getErrorCode\(\)\);
            System\.*out*\.println\("Request ID:" \+ oe\.getRequestId\(\)\);
            System\.*out*\.println\("Host ID:" \+ oe\.getHostId\(\)\);
        \} catch \(ClientException ce\) \{
            System\.*out*\.println\("Caught an ClientException, which means the client encountered "
                    \+ "a serious internal problem while trying to communicate with OSS, "
                    \+ "such as not being able to access the network\."\);
            System\.*out*\.println\("Error Message:" \+ ce\.getMessage\(\)\);
        \} finally \{
            if \(ossClient \!= null\) \{
                ossClient\.shutdown\(\);
            \}
        \}

        //文件访问路径规则 https://BucketName\.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder\("https://"\);
        stringBuilder
                \.append\(bucketName\)
                \.append\("\."\)
                \.append\(endpoint\)
                \.append\("/"\)
                \.append\(objectName\);

        *log*\.info\("文件上传到:\{\}", stringBuilder\.toString\(\)\);

        return stringBuilder\.toString\(\);
    \}
\}

![image\.png](图片和附件/image%209.png)

![image\.png](图片和附件/image%2013.png)

![image\.png](图片和附件/image%207.png)

![image\.png](图片和附件/image%2011.png)

![image\.png](图片和附件/image%201.png)

![image\.png](图片和附件/image%2010.png)

![image\.png](图片和附件/image%202.png)

![image\.png](图片和附件/image.png)

## \<insert id="insert" useGeneratedKeys="true" keyProperty="id"\>//dish中的id变成了菜里面的dishis 但是这个dishflavour中也有自己的id

//dish中的id变成了菜里面的dishis 但是这个dishflavour中也有自己的id*/\*\**
* \* 新增菜品和对应的口味*
* \**
* \* @param dishDTO*
* \*/*
@Transactional
public void saveWithFlavor\(DishDTO dishDTO\) \{

    Dish dish = new Dish\(\);

    BeanUtils\.*copyProperties*\(dishDTO, dish\);

    //向菜品表插入1条数据
    dishMapper\.insert\(dish\);

    //获取insert语句生成的主键值
    Long dishId = dish\.getId\(\);

    List\<DishFlavor\> flavors = dishDTO\.getFlavors\(\);
    if \(flavors \!= null \&\& flavors\.size\(\) \> 0\) \{
        flavors\.forEach\(dishFlavor \-\> \{
            dishFlavor\.setDishId\(dishId\);
        \}\);
        //向口味表插入n条数据
        dishFlavorMapper\.insertBatch\(flavors\);
    \}
\}

New  公共字段自动填充

？补充看一下javaweb中的aop注解

？mp也又可以看看

技术方面

![image\.png](图片和附件/image%205.png)

aop拦截mapper\-》注解标识那些需要拦截\-》枚举operationtype\-》反射（ 注入）

解决的问题

![image\.png](图片和附件/image%2017.png)

Aop 通知很多 前置通知

获得所有的对象 ，规定把要用到的放在第一个；

？具体去了解去理解内部的东西

![image\.png](图片和附件/image%2018.png)

```Java
package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

*/***
* * 自定义切面，实现公共字段自动填充处理逻辑*
* */*
@Aspect
@Component
@Slf4j
//切入点阻拦什么-通知when-获得数据-反射注入
public class AutoFillAspect {

    */***
*     * 切入点*
*     */*
*    *@Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    //*todo*
*    *public void autoFillPointCut(){}

    */***
*     * 前置通知，在通知中进行公共字段的赋值*
*     */*
*    *//执行前mapper
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        *log*.info("开始进行公共字段自动填充...");
//joinpoint->signature->methsignature->autofil;(annotation)->value
        //一整个是joinpoint     @AutoFill(value = OperationType.UPDATE)
        //    void update(Category category)
        //signature 身份凭证  @AutoFill(value = OperationType.UPDATE)+
       // void update(Category category)
        //获取到当前被拦截的方法上的数据库操作类型
        //方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获得方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获得数据库操作类
        OperationType operationType = autoFill.value();

        //获取到当前被拦截的方法的参数--实体对象 all
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
//默认人第一个
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.*now*();
        Long currentId = BaseContext.*getCurrentId*();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.*INSERT*){
            //为4个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_CREATE_TIME*, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_CREATE_USER*, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_UPDATE_TIME*, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_UPDATE_USER*, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.*UPDATE*){
            //为2个公共字段赋值
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_UPDATE_TIME*, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.*SET_UPDATE_USER*, Long.class);

                //通过反射为对象属性赋值
                //注入
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
```

```Java
package com.sky.annotation;

import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

*/***
* * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理*
* */*
//目标加载什么上面
@Target(ElementType.*METHOD*)
//保留
@Retention(RetentionPolicy.*RUNTIME*)
public @interface AutoFill {
    //数据库操作类型：UPDATE INSERT
    OperationType value();
}
```

## 新增菜品

### 文件上传

yml\-》congfigurationproperties\-》config

？之前jjavaweb的方法是如何实现的；

？yml分为 dev和就是软编码

配置属性类\-yml中用配置\-小驼峰命名自动对应转化·\-调用工具类 aliossutil

多环境（就是不同情况下有不同的配置文件）

![image\.png](图片和附件/image%2019.png)

1. 新增菜品

2. 分类的

逻辑外键

![image\.png](图片和附件/image%2014.png)


    @Bean   *// 2\. 告诉 Spring：下面这个方法的返回值，要注册进容器作为 Bean*    @ConditionalOnMissingBean  *// 3\. 如果容器里还没有 AliOssUtil，我才创建；有了我就不管了*

![image\.png](图片和附件/image%204.png)

package com\.sky\.config;



import com\.sky\.properties\.AliOssProperties;

import com\.sky\.utils\.AliOssUtil;

import org\.springframework\.boot\.autoconfigure\.condition\.ConditionalOnMissingBean;

import org\.springframework\.context\.annotation\.Bean;

import org\.springframework\.context\.annotation\.Configuration;



@Configuration

public class OssConfiguration \{

@Bean

@ConditionalOnMissingBean

public AliOssUtil aliOssUtil\(AliOssProperties aliOssProperties\) \{

return new AliOssUtil\(\)

\};

\}

在主要的yml不配置在子的dev/pro配置    主要的用$\{\}来表示；类似与全类名

![image\.png](图片和附件/image%2016.png)

没有连接上redis？先注释掉缓存之后就是看看到底是如何运行的；

事务：@transactional

@enabletansactionalmanagement

//获取insert语句生成的主键值

Long dishId = dish\.getId\(\);

//dish中的id变成了菜里面的dishis 但是这个dishflavour中也有自己的id

//向菜品表插入1条数据

dishMapper\.insert\(dish\);



//获取insert语句生成的主键值

Long dishId = dish\.getId\(\);

//dish中的id变成了菜里面的dishis 但是这个dishflavour中也有自己的id

List\<DishFlavor\> flavors = dishDTO\.getFlavors\(\);

if \(flavors \!= null \&\& flavors\.size\(\) \> 0\) \{

flavors\.forEach\(dishFlavor \-\> \{

dishFlavor\.setDishId\(dishId\);

\}\);

//向口味表插入n条数据

dishFlavorMapper\.insertBatch\(flavors\);

\}

keyproperty应该是填实体类中的属性而不是数据库中的字段；

\<insert id="insert" useGeneratedKeys="true" keyProperty="id"\>

？写一下sql的语句 多表得会；多表这里就是自己还是没有理解的很好；理解一下；

\<select id="pageQuery" resultType="com\.sky\.vo\.DishVO"\>

select d\.\* , c\.name as categoryName from dish d left outer join category c on d\.category\_id = c\.id

\<where\>

\<if test="name \!= null"\>

and d\.name like concat\('%',\#\{name\},'%'\)

\</if\>

\<if test="categoryId \!= null"\>

and d\.category\_id = \#\{categoryId\}

\</if\>

\<if test="status \!= null"\>

and d\.status = \#\{status\}

\</if\>

\</where\>

order by d\.create\_time desc

\</select\>

![image\.png](图片和附件/image%2015.png)

## 菜品分页查询

Foreach 

![image\.png](图片和附件/image%208.png)

## 删除

单个和批量

启售

![image\.png](图片和附件/image%2021.png)

setmeal\-》两者关系表

？被套餐关联的不可以删除是为什么；

setmeal\-dish（有setmeal\_id 和 dish\_id\);

![image\.png](图片和附件/image%2012.png)

```Java
@Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品？？
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.*ENABLE*) {
                //当前菜品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.*DISH_ON_SALE*);
            }
        }
//为什么这里不是用for循环看看到底某一个id有没有setmealids；
        //判断当前菜品是否能够删除---是否被套餐关联了？？
        //少一点与数据库的交互
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.*DISH_BE_RELATED_BY_SETMEAL*);
        }
//为什么这里不直接批量删除两个都是可以吗；
//这里改成批量的比较好
        //删除菜品表中的菜品数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishId(id);
        }
    }
```

## 修改

什么时候用new\+beanutil\-》就是有两个对象之间的时候；

builder一个是对象一个是参数

```Java
public void updateWithFlavor(DishDTO dishDTO) {
    Dish dish = new Dish();
    BeanUtils.*copyProperties*(dishDTO, dish);}
    public void startOrStop(Integer status, Long id) {
    Dish dish = Dish.*builder*()
            .id(id)
            .status(status)
            .build();
    dishMapper.update(dish);}
```

![image\.png](图片和附件/image%206.png)

```Java
insert into dish_flavor (dish_id, name, value) VALUES
<foreach collection="flavors" item="df" separator=",">
    (#{df.dishId},#{df.name},#{df.value})
</foreach>
```

```Java
flavors.forEach(dishFlavor -> {
    //有可能是新增出来的
    dishFlavor.setDishId(dishId);
});
for(DishFlavor flavor : flavors){
    flavor.setDishId(dishId);
}
```

就是其实是细节

Null /size\>0

String null/ ""

![image\.png](图片和附件/image%2020.png)



![image\.png](图片和附件/image%203.png)



