# MyBatis 核心知识

> 本文梳理 MyBatis 在实际项目中最常用的四个核心知识点：分页查询、动态 SQL 更新、自增主键回填、批量插入。

---

## 一、分页查询（PageHelper）

### 1. 基本用法

**入参**：DTO

```java
@GetMapping("/page")
public Result<PageResult> page(EmployeePageQueryDTO dto) {
    // ...
}
```

**出参**：`Result<PageResult>`

```java
@Data
public class PageResult {
    private long total;        // 总记录数
    private List records;      // 当前页数据
}
```

### 2. PageHelper 做了什么

```text
① 自动执行 count 查询  →  得到 total
② 自动拼接 limit       →  查当前页数据
③ 封装成 Page 对象（继承 ArrayList，多了 total 等属性）
```

**核心代码**：

```java
// 1. 开启分页
PageHelper.startPage(dto.getPage(), dto.getPageSize());

// 2. 执行查询（PageHelper 会自动拦截并改写 SQL）
List<Employee> list = employeeMapper.list(dto);

// 3. 包装结果
Page<Employee> page = (Page<Employee>) list;
PageResult result = new PageResult(page.getTotal(), page.getResult());
```

### 3. 关键：total 是什么

> **total = 总记录数（所有页加起来），不是当前页条数。**

| 数据 | 来源 | 含义 |
|------|------|------|
| `total` | `count` 查询 | 满足条件的**所有记录数** |
| `records` | 数据查询 | **当前页**的记录列表 |

**示例**：

```text
数据库共 100 条记录，每页 10 条，查第 2 页：
total   = 100        （不是 10）
records = 第 11~20 条
```

### 4. PageHelper 使用注意

| 注意点 | 说明 |
|--------|------|
| **startPage 必须紧邻查询** | 中间不能插入其他查询，否则分页会作用到错误的 SQL |
| **只对第一条 SQL 生效** | `startPage` 后第一条查询才会被分页 |
| **返回类型** | 查询结果实际是 `Page` 类型，可强转获取 total |
| **依赖引入** | `pagehelper-spring-boot-starter` |

---

## 二、动态 SQL 更新

### 1. 核心思想

需要一个**实体对象**承载“要改哪些字段”。

### 2. 例子：启用禁用功能

```java
// 传 Employee 实体，而不是单独的 status 和 id
Employee employee = new Employee();
employee.setStatus(status);
employee.setId(id);
employeeMapper.update(employee);
```

**配合 `<set>` + `<if>`**：

```xml
<update id="update">
    update employee
    <set>
        <if test="status != null">status = #{status},</if>
        <if test="name != null">name = #{name},</if>
        <if test="updateTime != null">update_time = #{updateTime},</if>
    </set>
    where id = #{id}
</update>
```

### 3. 标签作用

| 标签 | 作用 |
|------|------|
| `<if>` | 只更新非 null 字段 |
| `<set>` | 自动加 `SET`，去掉多余逗号 |
| `<where>` | 自动加 `WHERE`，去掉开头多余的 `AND` / `OR` |

### 4. 为什么用实体而不是多个参数

| 方式 | 问题 |
|------|------|
| `update(status, id)` | 参数固定，扩展性差 |
| `update(Employee)` | 灵活，新增字段无需改方法签名 |

> **判断标准**：更新字段不固定时，用实体承载。

---

## 三、useGeneratedKeys（自增主键回填）

### 1. 基本用法

```xml
<insert id="insert" useGeneratedKeys="true" keyProperty="id">
    insert into dish (name, price, ...)
    values (#{name}, #{price}, ...)
</insert>
```

### 2. 作用

> 插入后，把**数据库生成的自增 id** 回填到 Java 对象。

### 3. 关键理解

```text
id 自增是数据库的行为，不是 Java 对象的属性
        ↓
Java 对象的 id 初始是 null
        ↓
插入后靠 useGeneratedKeys 回填
        ↓
copyProperties 只“搬运”值，不“生成” id
```

**示例**：

```java
Dish dish = new Dish();
dish.setName("宫保鸡丁");
// dish.getId() == null

dishMapper.insert(dish);
// 插入后，dish.getId() 已被回填为数据库生成的 id

// 用回填的 id 关联其他表
Long dishId = dish.getId();
```

### 4. 什么时候需要

| 场景 | 需要吗 | 原因 |
|------|:------:|------|
| 员工新增 | ❌ | 单表插入，不需要 id |
| 菜品新增 | ✅ | 要拿 id 关联口味表 |
| 订单新增 | ✅ | 要拿 id 关联订单明细 |
| 日志记录 | ❌ | 单表插入，不需要 id |

> **判断标准**：插入后要不要拿 id 去关联其他表。

### 5. 属性说明

| 属性 | 作用 |
|------|------|
| `useGeneratedKeys="true"` | 开启主键回填 |
| `keyProperty="id"` | 指定回填到 Java 对象的哪个属性 |
| `keyColumn="id"` | 指定数据库中的主键列（一般可省略） |

---

## 四、批量插入（`<foreach>`）

### 1. 基本用法

```xml
<insert id="insertBatch">
    insert into dish_flavor (dish_id, name, value)
    values
    <foreach collection="flavors" item="df" separator=",">
        (#{df.dishId}, #{df.name}, #{df.value})
    </foreach>
</insert>
```

**生成的 SQL**：

```sql
insert into dish_flavor (dish_id, name, value)
values
(1, '辣度', '中辣'),
(1, '忌口', '不要葱'),
(1, '甜度', '少糖')
```

### 2. `<foreach>` 属性

| 属性 | 作用 |
|------|------|
| `collection` | 要遍历的集合 |
| `item` | 每次遍历的元素 |
| `separator` | 每轮之间加的分隔符（**最后一轮不加**） |
| `open` | 循环前加的字符 |
| `close` | 循环后加的字符 |
| `index` | 遍历索引（List 中是下标，Map 中是 key） |

### 3. 常见用法对比

**批量插入（VALUES 形式）**：

```xml
<foreach collection="flavors" item="df" separator=",">
    (#{df.dishId}, #{df.name}, #{df.value})
</foreach>
```

**IN 查询**：

```xml
<select id="listByIds" resultType="Dish">
    select * from dish
    where id in
    <foreach collection="ids" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
</select>
```

### 4. 关键注意点

| 注意点 | 说明 |
|--------|------|
| **IN 里只写 `#{id}`** | 不要写 `id = #{id}`，否则语法错误 |
| **separator 最后一轮不加** | MyBatis 自动处理，无需手动判断 |
| **collection 名称** | 方法参数用 `@Param` 指定，或直接用 `list` / `array` |
| **批量插入性能** | 比循环单条插入快很多，但 SQL 过长需分批 |

### 5. 方法参数示例

```java
// 方式一：@Param 指定名称
void insertBatch(@Param("flavors") List<DishFlavor> flavors);

// 方式二：直接用 list
void insertBatch(List<DishFlavor> flavors);
// XML 中 collection="list"
```

---

## 五、核心知识点总结

| 知识点 | 核心内容 | 关键点 |
|--------|----------|--------|
| **PageHelper** | 自动 count + limit | `total` 是总记录数，不是当前页条数 |
| **动态 SQL 更新** | `<set>` + `<if>` | 用实体承载非 null 字段，只更新需要改的 |
| **useGeneratedKeys** | 自增主键回填 | 插入后要拿 id 关联其他表时才需要 |
| **批量插入** | `<foreach>` | IN 查询只写 `#{id}`，不写 `id = #{id}` |

---

## 六、常见面试点

| 问题 | 答案要点 |
|------|----------|
| PageHelper 原理？ | 拦截 SQL，自动执行 count，改写为 limit 查询 |
| total 和 records 区别？ | total 是总数，records 是当前页数据 |
| `<set>` 和 `<where>` 作用？ | 自动处理 SET / WHERE 及多余逗号、AND |
| 为什么用实体做更新入参？ | 字段不固定，扩展性好，配合 `<if>` 动态拼接 |
| useGeneratedKeys 什么时候用？ | 插入后需要拿 id 关联其他表时 |
| `<foreach>` 的 separator 作用？ | 每轮之间加分隔符，最后一轮不加 |
| 批量插入和循环插入区别？ | 批量一条 SQL，循环 N 条 SQL，性能差很多 |