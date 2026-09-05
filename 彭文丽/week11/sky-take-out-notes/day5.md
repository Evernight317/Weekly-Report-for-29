# day5

## redis一些基础知识

再redis的视频专门看过了，so我跳过了；

![image\.png](图片和附件/image%204.png)

jdis：用的就i是原来的命令

![image\.png](图片和附件/image%202.png)

![image\.png](图片和附件/image%203.png)

默认是16个database（从0\-15）； 

默认1个即0

test路径无法注入；

```Java
redisTemplate.opsForValue().set("code","1234",3, TimeUnit.*MINUTES*);
//记得有单位
```

redistemplate没有注入成功；

包的路径

bug：为什么  依赖注入不了吗；

```Java
Map<String, String> map = new HashMap<>() {{
    put("name", "小明");
    put("age", "20");
    put("city", "北京");
}};
// 最多10个键值对的快速写法
//这个是之后不用修改
Map<String, String> map = Map.of(
    "name", "小明",
    "age", "20",
    "city", "北京"
);
Map<String, String> map = new HashMap<>();
map.put("name", "小明");      // 存
map.get("name");             // 取 → "小明"
map.containsKey("name");     // 判断键是否存在
map.remove("name");          // 删除
map.keySet();                // 获取所有键
map.values();                // 获取所有值有没有简化写法
```

hash：key\-hashkey\(field\)\-value

timeunit\-》单位

value可以是任意类型就是都会序列化为string

```Java
//这里得到的是hash中的key和valu传入的是redis中的key
//为什么一个是list一个是set（hashkey是唯一的）
Set keys = hashOperations.keys("100");
System.*out*.println(keys);

List values = hashOperations.values("100");
System.*out*.println(values);
```

```Java
//代表的是到结尾最后一个
List mylist = listOperations.range("mylist", 0, -1)
```

血泪：就是必须完成一个大概的功能之后git 来版本控制要不自己一个一个revert是非常之累；（他只是那几个修改的文件）

## bug1 springboottest

**Redistemplate null**

看文章之后就是直接解决了

ai了很久不对 dp数据库是2025

大概是因为就是websocket之类的东西就是再test地时候是不会运行的；

但是是排除了这个就是再springboot上加了一个东西就是改了一下springboottest具体注解内容就可以了；

\[[博客](https://blog.csdn.net/Uzumaki_Naruto12/article/details/144853595)\]\(https://blog\.csdn\.net/Uzumaki\_Naruto12/article/details/144853595\)

**连接不上redis**

卡了很久

Why

1. 黑马给的yml中是localhost 可能是直接再linux虚拟机上录的视频or用了一个映射

2. 没有知道整个东西是如何运行的；or用ai来消除自己的脑测or类似与知识幻觉

```Java
redis:
//这里不是localhost
  host: 192.168.167.128
  port: 6379
  password: 18825796768Pwl
  database: 10
```

**序列化**

传入的object 其实是可以是任意的类型 因为回序列化为string

图形化的客户端value是java序列化之后结果

为了让图形化界面的key更加直观好看 要自己设置string类型的序列化器，不要用默认的

其实也是可以设置一下value的序列化器让这个更加直观

### 店铺营业状态

1. why用redis

![image\.png](图片和附件/image.png)

![image\.png](图片和附件/image%201.png)

就是用这个逻辑1\. 存储数据2， 营业状态这一个字段 01 \-》key与value\-》redis嘛是这个推理逻辑

```Java
package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String *KEY *= "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    */***
*     * 设置店铺的营业状态*
*     * @param status*
*     * @return*
*     */*
*    *@PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status){
        *log*.info("设置店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(*KEY*,status);
        return Result.*success*();
    }

    */***
*     * 获取店铺的营业状态*
*     * @return*
*     */*
*    *@GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(*KEY*);
        *log*.info("获取到店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        return Result.*success*(status);
    }
}
```

Why 就是这个没有把key弄成一个constant常量

Test  for hash 这里有一点问题；？

secretkey太短了

其实redistemplate业务的东西应该是在service层写（除了filter也需要用到设置的redis数据）

还有video中public final static应该在common中用新建一个constant的类；

bean的名字起冲突 \-》默认是用类名来区分 \-》在restcontroller加入value

**swagger技巧**

加上groupname\-扫描不同的包\-》分组



