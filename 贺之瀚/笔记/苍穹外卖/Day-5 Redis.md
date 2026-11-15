# Redis

Redis是一个基于**内存** 的**key-value** 结构数据库；

存在内存上，mysql是存在硬盘上；

- 基于内存存储，读写性能高；
- 适合存储热点数据（热点商品、资讯、新闻）；
- 默认端口：6379

键盘上方向键在cmd中输入上一条指令

```Shell
# 启动redis
redis-server.exe redis.windows.conf
# 启动cli [主机ip] [端口号] [密码]
redis-cli.exe [-h ] [-p ] [-a]
```

- redis没有用户概念，仅需要密码；
- redis客户端；

---

## 一、数据类型

key默认**字符串**类型，value5种类型；

### 1.字符串string

### 2.哈希hash

也叫散列，类似于Java中的HashMap结构；

`value`中分为两个：`field` `value`

类似的，可以存对象；

### 3.列表

按照插入顺序排序（有序？，可以有重复元素，类似于Java中的LinkedList；

### 4.集合

**无序**集合，没有重复元素，类似于Java中的HashSet；

### 5.有序集合

集合中每个元素关联一个分数(score)，根据分数**升序**排序，没有重复元素；

例如排行榜；

## 二、Redis常用操作命令-字符串

sql：语句和类型无关；

Redis：语句和value类型有关；

---

|                             |                                                             |                                                 |
| --------------------------- | ----------------------------------------------------------- | ----------------------------------------------- |
| `SET key value`           | 设置指定key的值                                             |                                                 |
| `GET key `               | 获取指定key的值                                             |                                                 |
| `SETEX key seconds value` | 设置指定key的值，并将key的过期时间设为seconds**秒**； | 常见应用：验证码                                |
| `SETNX key value`         | 只有在key**不存在**时设置key的值                      | 应用：分布式锁；<br /> 返回值1：成功，0：失败； |

客户端可以查看TTL（Time to Live） -2为过期，-1为没有设置时间

- set时，如果key已经存在，则会**覆盖**原先的value；

## 三、哈希

一个**string**类型的field和value的映射表，hash特别适合用于存储对象，常用命令；

相当于key对应了一张哈希表；

- 注意：是value部分是哈希表，*field和value都是string*；
- **哈希表字段**指的就是**field**；

|                          |                                             |
| ------------------------ | ------------------------------------------- |
| `HSET key field value` | 将key对应的哈希表中的字段field的值设为value |
| `HGET key field`       | 获取存储在哈希表中指定字段的值              |
| `HDEL key field`       | 删除存储在哈希表中的指定字段                |
| `HKEYS key`            | 获取哈希表中所有字段                        |
| `HVALS key`            | 获取哈希表中所有值                          |

## 四、列表

- 列表中所有元素都是**字符串** ，即简单字符串列表；

|                               |                                                    |                                   |
| ----------------------------- | -------------------------------------------------- | --------------------------------- |
| `LPUSH key value1 [value2]` | 将一个或多个值插入到列表头部                       | 此处是**Left** 即从左侧插入 |
| `LRANGE key start stop`     | 获取列表指定范围内的元素                           |                                   |
| `RPOP key`                  | 移除并**获取返回**列表**最后一个**元素 |                                   |
| `LLEN key`                  | 获取列表长度                                       |                                   |

- 可以将列表理解成一个特殊的队列，*左侧（列表头部）是队尾，右侧（列表尾部）是队头* ，或者说左进右出；
- 返回列表**所有**元素`LRANGE key 0 -1` ；

## 五、集合

- value是string类型的**无序**集合，成员唯一，不允许重复数据；
- 注意删除是SREM(remove)

|                                |                                  |
| ------------------------------ | -------------------------------- |
| `SADD key member1 [member2]` | 向集合添加一个或多个成员         |
| `SMEMBERS key`               | 返回集合中的所有成员             |
| `SCARD key`                  | 获取集合的成员数                 |
| `SINTER key1 [key2]`         | 返回给定所有集合的**交集** |
| `SUNION key1 [key2]`         | 返回所有给定集合的**并集** |
| `SREM key member1 [member2]` | 删除集合中一个或多个成员         |

- 插入时，成员已存在的话，不会执行插入操作；
- 

## 六、有序集合

- string类型元素集合，不允许重复；
- 每个元素都会关联一个**double类型的分数**，用来**排序**；

Z->Zset

ZREM(remove)

(Z increment by)

|                                              |                                             |                    |
| -------------------------------------------- | ------------------------------------------- | ------------------ |
| `ZADD key score1 member1 [score2 member2]` | 向有序集合添加一个或多个成员                |                    |
| `ZRANGE key start stop [WITHSCORES]`       | 通过索引区间返回有序集合中指定区间内的成员  | 可选择是否带上分数 |
| `ZINCRBY key increment member`             | 有序集合中对指定成员的分数加上增量increment |                    |
| `ZREM key member [member...]`              | 移除有序集合中的一个或多个成员              |                    |

- 默认升序？
- 全部元素`ZRANGE key 0 -1`

## 七、通用命令

- 都是对key操作的；

|                        |                                    |  |
| ---------------------- | ---------------------------------- | - |
| `KEYS pattern`       | 查找所有符合给定模式(pattern)的key |  |
| `EXISTS key`         | 检查给定key是否存在                |  |
| `TYPE key`           | 返回key所储存的值的类型            |  |
| `DEL key1 [key2]...` | 该命令用于在key存在是删除key       |  |

```SQL
-- 所有key
keys *
-- 所有set开头的key
keys set*
```

## 八、Java操作Redis

### Redis Java客户端

- Jedis/Lettuce/Spring Data Redis（Spring一部分）

### Spring Data Redis

- maven导入
- 配置Redis数据源
- 编写配置类，创建RedisTemplate对象
- 通过RedisTemplate对象操作；

Redis启动时默认创建16个DB，默认使用db0，也可以手动指定；

- 同样，主配置文件最好使用**引用**；Spring是通过命名约定确定实际加载的application.yml；
- RedisTemplate提供了五种接口，通过五种接口操作对应数据，但是需要先得到相应的#{value}Operations对象

```Java
ValueOperations
    valueOperations = redisTemplate.opsForValue();
HashOperations
	shashOperations = redisTemplate.opsForHash();
ListOperations
	listOperations = redisTemplate.opsForList();
SetOperations
	ssetOperations = redisTemplate.opsForSet();
ZSetOperations
	zSetOperations = redisTemplate.opsForZSet();
```

配置类中的序列化器

```Java
// com/sky/config/RedisConfiguration.java
// 设置key和value的序列化器
redisTemplate.setKeySerializer(new StringRedisSerializer());
redisTemplate.setValueSerializer(new StringRedisSerializer());
```

### string

```Java
//        set get setex setnx
ValueOperations
	valueOperations = redisTemplate.opsForValue();
valueOperations.set("hometown","绍兴");
//        set key value
redisTemplate.opsForValue().set("city","北京");
//        get key
String city = (String) redisTemplate.opsForValue().get("city");
System.out.println(city);
//        setex key seconds value
//        此处分别为(key,value,timeout,timeUnit)
//        timeout即为过期时间，timeUnit则是指时间单位，是一个枚举类
redisTemplate.opsForValue().set("code","123456",
            3, TimeUnit.MINUTES);
//        setnx key value
redisTemplate.opsForValue().setIfAbsent("lock","1");
redisTemplate.opsForValue().setIfAbsent("lock","2");
```

- TimeUnit 是一个枚举类，用来；
- get的返回值为**Objcet** ；
- Redis中的string和java中有区别：redis可以接收java中任意类型的对象，并且**序列化**成string，相应的get得到的值也就是**任意对象** ；
- 通过对key和value设置**序列化器**，可以使其在Desktop显示时不产生乱码，但这个只是对显示有影响，对于存储无影响（最后几位是正常的，前面多点乱码）；

### hash

```Java
//        hset hget hkeys hvals
HashOperations hashOperations = redisTemplate.opsForHash();

//        hset key field value
hashOperations.put("100","name","hzh");
hashOperations.put("100","length","20cm");
//        hget key field
String name = (String) hashOperations.get("100","name");
System.out.println(name);
//        hkeys key 这里返回的是Set集合 应该是因为hash的唯一性
Set keys = hashOperations.keys("100");
System.out.println(keys);
//        hvals key
List values = hashOperations.values("100");
System.out.println(values);
//        hkeys key field
hashOperations.delete("100","length");
```

### 列表

```Java
//        lpush lrange lpop llen
        ListOperations listOperations = redisTemplate.opsForList();

//        lpush keys value1 [value2]
        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","b","c","d");

//        lrange key start stop
//        同样，0 -1表示全部
//        listOperations.range("mylist",0,-1);
        List mylist = listOperations.range("mylist", 0, -1);
        System.out.println(mylist);

//        lpop key
        String mylist1 = (String) listOperations.rightPop("mylist");
        System.out.println(mylist1);

//        llen key
        Long size = listOperations.size("mylist");
        System.out.println(size);
```

### 集合

### 有序集合
