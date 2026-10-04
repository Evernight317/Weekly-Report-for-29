# 周报（第 9 周）

**提交人：** 宋旭涛

**报告周期：** 2026.09.28 - 2026.10.04

---

## 一、本周学习概述

本周围绕持久层框架 MyBatis-Plus 展开学习与实践，系统掌握了通用 CRUD、条件构造器、分页插件、公共字段自动填充、逻辑删除、乐观锁等核心功能，并在苍穹外卖项目中用 MyBatis-Plus 重构了原有手写的单表增删改查代码，体会到"减少样板代码"对开发效率的实质提升。同时，从 MyBatis-Plus 反推到 MyBatis 的底层原理，重点研究了 Mapper 接口动态代理机制与 `#{}` / `${}` 的区别，并系统复习了 Spring IoC / AOP、事务失效场景与 JWT 等高频面试考点。

---

## 二、核心学习内容

### 2.1 MyBatis-Plus 的定位
- **是什么**：MyBatis 的增强工具，**只做增强不做改变**，引入后不影响原有 MyBatis 功能，可平滑替换。
- **解决什么问题**：单表增删改查的样板代码重复度极高，MyBatis-Plus 用一套通用接口覆盖了这部分工作。
- **边界**：复杂多表关联查询、动态 SQL 仍建议写 XML 或注解，不要为了用而用。

### 2.2 通用 CRUD：`BaseMapper`
- Mapper 接口只需继承 `BaseMapper<T>`，**无需编写任何 XML** 即可获得基础能力。
- 常用方法：`insert`、`deleteById`、`updateById`、`selectById`、`selectList`、`selectCount`、`selectPage`。
- 相比原 MyBatis：省去了每个单表方法都要写一条 `<select>` 的工作量。

### 2.3 Service 层封装：`IService` / `ServiceImpl`
- `IService<T>` 提供 `save`、`saveBatch`、`removeById`、`updateById`、`getById`、`list`、`page` 等方法。
- 实现类继承 `ServiceImpl<Mapper, Entity>` 即可，无需重复定义这些方法。
- **与苍穹外卖手写方案的对比**：项目中原先是手写 Service 接口 + 实现类 + XML，MyBatis-Plus 把这部分通用逻辑框架化了。

### 2.4 条件构造器
- 提供 `QueryWrapper`、`UpdateWrapper`、`LambdaQueryWrapper`、`LambdaUpdateWrapper`。
- **推荐使用 Lambda 版本**：基于方法引用（如 `Dish::getCategoryId`），字段名写错在**编译期**就会暴露；字符串版本写错只能在运行时才发现。
- **常用方法**：`eq`、`ne`、`gt`、`ge`、`lt`、`like`、`in`、`isNull`、`orderByDesc`，以及 `and` / `or` 的嵌套组合。
- **部分字段查询**：可用 `select` 指定返回列，避免 `select *` 带来的额外开销。

### 2.5 分页插件
- 需注册 `MybatisPlusInterceptor` 并向其中添加 `PaginationInnerInterceptor`。
- **关键点**：不注册插件时，`Page` 查询**不会真正分页**，会退化成查全表（极其危险的坑）。
- **与 PageHelper 的对比**：PageHelper 基于拦截器改写 SQL，使用更简单；MyBatis-Plus 的分页与自身 `Page` 对象、`IPage` 体系绑定更紧密。

### 2.6 公共字段自动填充
- **用法**：实体字段上加 `@TableField(fill = FieldFill.INSERT)` 或 `FieldFill.INSERT_UPDATE`，再实现 `MetaObjectHandler` 接口。
- **与苍穹外卖 AOP 方案的对比**：
  1. **苍穹外卖方案**：自定义注解 `@AutoFill` + `OperationType` 枚举 + AOP 环绕通知 + 反射赋值，能区分 insert / update 并区分不同业务操作，灵活但需要自己写反射逻辑。
  2. **MyBatis-Plus 方案**：框架内置，配置简单，触发点固定为 insert / update 两类。
- **结论**：通用场景用框架方案，需要按业务类型细分填充策略时，AOP 方案更灵活。

### 2.7 主键策略与逻辑删除
- **主键策略**：`@TableId(type = ...)`，可选 `AUTO`（数据库自增）、`ASSIGN_ID`（雪花算法生成分布式 ID）、`ASSIGN_UUID`。
- **逻辑删除**：字段加 `@TableLogic` 并配置 `logic-delete-field` 后，删除操作会变成 `UPDATE ... SET deleted = 1`，查询会自动追加 `AND deleted = 0` 条件。
- **优点**：数据可追溯、可恢复；**代价**：表会持续膨胀，且与唯一索引存在冲突风险。

### 2.8 乐观锁与防全表更新
- **乐观锁插件**：`OptimisticLockerInnerInterceptor` + 实体字段 `@Version`。更新时 SQL 变为 `UPDATE ... SET version = version + 1 WHERE id = ? AND version = ?`，版本不匹配则更新失败，用于防止并发覆盖。
- **防全表更新删除**：`BlockAttackInnerInterceptor` 可拦截没有 `where` 条件的 `update` / `delete`，避免误操作清库。

### 2.9 MyBatis-Plus 的常见坑
1. **`updateById` 只更新非 null 字段**：想把字段置为 `null` 时必须用 `LambdaUpdateWrapper.set()` 显式赋值。
2. **`MetaObjectHandler` 在 `UpdateWrapper` 场景不生效**：自动填充依赖实体对象，用 Wrapper 更新时不会触发。
3. **逻辑删除与唯一索引冲突**：已逻辑删除的记录仍在表中，新增同名数据会撞唯一索引。
4. **`selectOne` 查到多条会抛异常**：使用前应确认查询结果的唯一性。
5. **分页插件未注册**：表现为"分页失效、返回全表"，属于最容易踩且最危险的坑。

### 2.10 面试八股复习：MyBatis / Spring / 项目
- **MyBatis 的 Mapper 接口没有实现类，为什么能直接调用？**
  通过 **JDK 动态代理**在**运行时**生成代理对象，代理的 `invoke` 方法用「**接口全限定名 + 方法名**」定位到对应的 `MappedStatement` 并执行。要点是"运行时动态代理"，而不是编译期生成实现类，也不是 Spring Boot 的功劳。
- **`#{}` 与 `${}` 的区别**：
  - `#{}` 会被编译为 PreparedStatement 的占位符 `?`，参数通过 `setXXX` 传入，**能防止 SQL 注入**。
  - `${}` 是字符串直接拼接，**存在注入风险**；仅在需要动态拼接表名、排序字段等无法用占位符的场景使用。
- **Spring IoC / DI 的价值**：由容器统一管理对象的创建与依赖装配，实现解耦、便于替换实现、便于单元测试时注入 mock。
- **`@Transactional` 失效的常见场景**：方法非 `public`；同类内部自调用（绕过代理）；异常被 `catch` 后未抛出；抛出受检异常但未配置 `rollbackFor`；目标对象未被 Spring 管理。
- **Spring MVC 的全局异常处理机制**：`@RestControllerAdvice` + `@ExceptionHandler`，由 `HandlerExceptionResolver` 链中的 `ExceptionHandlerExceptionResolver` 负责解析。**注意它属于 MVC 的异常解析机制，不是 AOP 代理**。
- **ThreadLocal 在项目中的应用与风险**：`BaseContext` 用于在拦截器与 Service 之间传递当前用户 id，必须在请求结束后调用 `remove()`，否则线程池复用会造成数据串号与内存泄漏。
- **JWT 的组成与注意事项**：由 Header、Payload、Signature 三部分组成；**Payload 只是 Base64 编码而非加密**，不能存放敏感信息；JWT 无状态、无需查库，但**无法主动失效**，需要配合 Redis 黑名单实现登出。

---

## 三、本周实践进展

1. 在苍穹外卖项目中引入 MyBatis-Plus，将原有手写的单表 CRUD 替换为通用接口。
2. 使用 `LambdaQueryWrapper` 重构菜品、套餐等模块的条件查询，替换字符串形式的字段名。
3. 注册 `MybatisPlusInterceptor` 与分页插件，统一分页返回结构。
4. 实现 `MetaObjectHandler`，完成创建时间 / 更新时间的自动填充。
5. 整理 MyBatis 与 Spring 面试题笔记，完成自测并针对错题做了二次订正。

---

## 四、问题与解决方案

| 问题 | 解决方案 |
| --- | --- |
| 分页查询返回了全表数据 | 未注册 `PaginationInnerInterceptor`，MyBatis-Plus 的分页依赖该插件改写 SQL，注册后恢复正常 |
| 想把某字段更新为 null，调用 `updateById` 无效 | `updateById` 只更新非 null 字段，改用 `LambdaUpdateWrapper.set()` 显式赋值 |
| 使用 UpdateWrapper 更新时，公共字段自动填充不生效 | `MetaObjectHandler` 依赖实体对象触发，改用实体更新的方式，或在 Wrapper 中手动赋值 |
| 逻辑删除后新增同名数据报唯一键冲突 | 逻辑删除的记录仍保留在表中，需调整唯一索引设计（如与 deleted 字段组成联合唯一索引） |
| 接口返回多条记录时 `selectOne` 抛异常 | 确认业务上是否唯一；确实可能多条时应改用 `list` 并取首条，或补充更精确的查询条件 |
| 排序字段无法用 `#{}` 传入 | 排序字段属于 SQL 结构而非参数，只能使用 `${}`，但必须在服务端做白名单校验以规避注入风险 |

---

## 五、下周学习计划

1. 继续完善项目功能，补充统一的响应结构与全局异常处理。
2. 学习 Spring 事务的传播机制与 AOP 底层实现原理。
3. 系统复习 JVM 内存结构与并发编程相关面试考点。

---

## 六、总结与思考

本周用 MyBatis-Plus 重构持久层的过程，让我第一次直观感受到"框架替开发者做了多少重复劳动"。原来自查一个单表查询要写接口、实现、XML 三层，现在继承一个 `BaseMapper` 就解决了。但更有价值的是反过来看——**MyBatis-Plus 之所以能"只做增强不做改变"，是因为 MyBatis 本身的 Mapper 动态代理机制留出了扩展空间**：接口没有实现类却能调用，靠的是运行时用「接口全限定名 + 方法名」定位 SQL。理解了这一层，就不会再把框架当成黑盒。

这一周印象最深的坑是"分页插件不注册导致查全表"。它提醒我，很多框架能力是**需要显式装配的**，默认配置并不等于安全配置；同理，`updateById` 不更新 null 字段、逻辑删除与唯一索引冲突这类问题，也只有真正在项目里踩过才会记住。

接下来准备把项目的响应结构与异常处理统一起来，让接口对外更规范。
