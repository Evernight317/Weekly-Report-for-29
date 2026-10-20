# Mybatis

通过java操作数据库；

MyBatis是一款优秀的**持久层（dao数据访问层）** 框架，用于简化*JDBC* 的开发

---

## 一、入门

dataGrip操作数据库步骤：sql语句发给sql服务器并执行，然后服务器将结果返回给GUI，Mybatis替换GUI就是原理；底层原理想通

1、准备工作(创建springboot工程、数据库表user、实体类User)

2、引入Mybatis的相关依赖，配置Mybatis(数据库连接信息）

3、编写SQL语句(通过注解/XML定义sql语句)

数据库四要素：驱动类全类名、数据库连接URL、访问数据库的账密；

### 2.配置sql提示

注入语言；

在idea中连接数据库；

### 3.JDBC

使用Java语言操作关系型数据库的一套API；

sun公司官方定义的一套操作所有关系型数据库的规范，即接口；各个数据库厂商去实现这套接口，提供数据库驱动jar包；我们可以使用这套接口(JDBC）编程，真正执行的代码是驱动jar包中的实现类；

硬编码：将驱动类、数据库url账密等写入java程序；

数据库连接池

mybatis操作数据库，简化为配置`application.porperties`和编写`mapper`接口；

### 4.数据库连接池

数据库连接池是个**容器** ，负责分配、管理数据库连接（Connection）；

它允许应用程序重复使用一个现有的数据库连接，而不是再重新建立一个Connect连接对象，通过资源复用的方式显著提升了系统响应速度；

释放空闲时间超过最大空闲时间的连接，来避免因为没有释放连接而引起的数据库连接遗漏

标准接口：DataSource

官方的数据库连接池接口，由第三方实现，功能为获取连接，核心都是实现`Connection getConnection() throws SQLException` 方法；

Springboot默认为hikari；还有Druid德鲁伊连接池；

### lombok

Lombok是一个java类库，能通过**注解** 的形式自动生成构造器 getter/setter、equals、hashcode、toString等方法，并可以自动化生成日志变量；lombok是在编译时去掉注解并自动生成相应代码，使用前，要添加lombok依赖，在idea使用时需要安装lombok插件；

| 注解                        |                                                                    |
| --------------------------- | ------------------------------------------------------------------ |
| `@Getter`/`@Setter`     | 为所有成员提供get/set方法                                          |
| `@ToString`               | 自动重写toString方法                                               |
| `@EqualsAndHashCode`      | 自动重写`equals`方法和`hashCode`方法                           |
| *`@Data`*               | `@Getter`+`@Setter`+`@ToString`+<br />`@EqualsAndHashCode` |
| *`@NoArgsConstructor`*  | 无参构造器                                                         |
| *`@AllArgsConstructor`* | 除静态成员外的有参构造器                                           |

## 二、基础操作-基于注解配置mysql语句

### 环境准备

准备数据库表 emp
创建一个新的springboot工程，选择引入对应的起步依赖（mybatis、
application.properties中引入数据库连接信息
创建对应的实体类Emp（实体类属性采用*驼峰命名*）
准备Mapper接口EmpMapper

### 1.删除

Mybatis中的参数占位符 `#()`，直接填具体值的话相当于该方法不是动态了；

**接口方法** ：

```Java
@Delete("delete from emp where id = #{id}")
public void delete(Integer id);
```

方法的返回值意义：*此次操作影响的数据条数* ；

### 配置mybatis日志

`mybatislog`日志关键字

### 预编译sql语句

性能更高，安全性更高（防止sql注入

**sql注入** ：通过操作输入的数据来修改事先定义好的SQL语句，以达到执行代码对服务器进行攻击的方法；比如对于拼接的方式，可以通过`'`等破坏字符串的语句（即破坏原有逻辑）；

| 参数占位符 | `#{}`                                                                                      | `${}`                                   |
| ---------- | -------------------------------------------------------------------------------------------- | ----------------------------------------- |
|            | 执行sql时，将其**替换** 为`?`，自动设置参数值（可以认为是将`?`直接替换为对应参数） | 直接将参数拼接在sql语句中，存在注入问题； |
| 使用时机   | 参数传递                                                                                     | 对表名、列名进行动态设置（不常用）        |

### 2.新增

因为形参可能很多，所以考虑封装成一个对象，而且员工对象在Emp中已经封装好了，所以直接调用即可；

**接口方法** ：

```Java
@Insert("insert into emp(username,name...)"
       +"values(#{username},#{name}...)")
public void insert (Emp emp);
```

### 主键返回

在数据添加成功后，需要获取插入数据库数据的主键；

默认情况下，基础方法拿不到主键返回值；

所需注解`Options` ；

```Java
// 获取返回主键值
// useGeneratedKeys表示是否获取返回主键
// keyProperty指定的是返回主键在emp中的封装成员名
@Options(keyProperty="id",useGeneratedKeys=true)
```

### 3.更新

接口方法

```Java
@Update("update emp set username= #{}, name = #{}...")
public void update(Emp emp);
```

### 4.查询-根据id

查询是具有**返回值** 的，根据内容可以封装成List集合或者单个对象；

接口方法

```Java
@Select("select * from emp where id=#{id}")
public Emp getById(Integer id);
```

### 数据封装

实体类属性名和数据库表查询返回的字段名一致，mybatis会自动封装；

如果实体类属性名和数据库表查询返回的字段名**不一致** ，不能自动封装；

#### 给字段起别名

别名要与实体类属性名一致；

#### 通过注解手动结果映射

繁琐，所以不常用

`@Results` 、 `@Result`注解可以指定字段封装的属性；

直接在方法上加上注解即可；

```Java
// @Results(Result[] values default{})
// 即内容是一个Result数组
// column是数据库表中字段，property是目标对象属性
@Results({
  	@Result(column = "dept_id", property = "deptId"),
  	@Result(column = "create_time", property = "createTime"),
  	@Result(column = "update_time", property = "updateTime")
})
@Select("select * from emp where id=#{id}")
public Emp getById(Integer id);
```

#### *Mybatis驼峰命名映射开关

严格要求数据库中命名为`a_column` java属性名为`aColumn`，否则无法完成映射；

`application.properties`中配置；

关键词camel

```Properties
mybatis.configuration.map-underscore-to-camel-case=true
```

### 5.条件查询

接口方法

- 使用模糊匹配的话，相当于条件成为了一个字符串（带有`''`），但是占位符`?` 不允许出现在其中，而使用`'%${name}%'`的话又存在注入风险。这里推荐的方案是使用mysql的`concat`**字符串连接函数** ：`concat('%',#{name},'%')` ；
- 这里由于参数和Emp对象无法对应，所以不使用对象封装，直接传参了，能封装的话还是尽量封装；

```Java
// 这里由于和Emp对不上，所以直接传参了
@Select("select * from emp where name like concat('%',#{name},'%') and gender = #{gender} and" +
       "entrydate between #{begin} and #{end} order by update_time desc")
public List<Emp> list(String name, short gender, LocalDate begin, LocalDate end);
```

- 在springboot的1.x版本/单独使用mybatis时，由于不会保留方法形参名称，所以需要再每个形参前额外加上`@Param("sql语句字段名")`来建立映射；

## 三、XML映射文件-基于XML配置mysql语句

### 全类名

包名+类名的完整路径，相当于文件的绝对路径；

### 规范

- XML映射文件的名称与Mapper接口名称一致，并且将XML映射文件和Mapper接口放置在相同包下（**同包同名** ，即在`resource`下再创建一个同名的包）；一般是一个接口对应一份配置文件
- XML映射文件的namespace属性为Mapper接口**全限定名** 一致（即模块下的带有所有包的完整路径名/*引用名*？）；
- XML映射文件中sql语句的id与Mapper接口中的方法名一致，并保持*返回类型一致 （全类名）*；
- XML的约束去mybatis官方文档获取；

mybatis本质还是调用sql语句执行， 由于方法和sql是分开的，遵循上述规则能够使调用方法的时候定位到对应的sql语句；

```XML
<!-- 以条件查询为例 -->
<select id="方法名" resultType="返回值类型全类名">
  	<!-- sql语句 -->
</select>
```

### IDEA插件-MybatisX

### 注解/XML映射语句

| 注解                | XML         |
| ------------------- | ----------- |
| 更偏向于简单sql语句 | 复杂sql语句 |

## 四、动态sql

随着用户的输入或外部条件的变化而变化的SQL语句；

在XML中使用标签即可；

动态sql中，排查语法错误注意关键字*near*；

---

### 1.`<if>`

用于判断条件是否成立。使用*test属性*进行条件判断，如果条件为true，则拼接SQL语句；

例如

```XML
<if test="name != null">
  name like concat('%',#{name},'%')
</if>
```

- `and`关键字，当前面都不成立，就会and开头导致语法错误，都不成立也可能多出来个where；解决方法是使用*where* 标签。`where` 会根据条件自动判断是否生成where，并且可以自动去除多余and；

### 2.`<foreach>`
