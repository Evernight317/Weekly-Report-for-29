# 公共字段自动填充（AOP 实战）

> 在项目中，像 `create_time`、`update_time`、`create_user`、`update_user` 这类公共字段，几乎每张表都有。如果每次插入/更新都手动 set，代码重复且容易遗漏。用 **AOP + 自定义注解 + 反射** 可以自动完成填充。

---

## 一、三件套

### 1. 枚举（OperationType）

```java
public enum OperationType {
    UPDATE,
    INSERT
}
```

**要点**：

- 枚举是**引用类型**，不是基本类型
- 本质是**特殊的类**，继承 `java.lang.Enum`
- 用来区分当前是插入还是更新操作，决定填充哪些字段

---

### 2. 自定义注解（@AutoFill）

```java
@Target(ElementType.METHOD)              // 用在哪
@Retention(RetentionPolicy.RUNTIME)      // 保留到运行时
public @interface AutoFill {
    OperationType value();               // 注解属性
}
```

| 元注解 | 作用 |
|--------|------|
| `@Target` | 注解能用在哪些地方（方法、类、字段等） |
| `@Retention` | 注解保留到什么阶段 |
| `@Retention(RUNTIME)` | **AOP 必须用 RUNTIME**，否则运行时读不到 |
| 注解属性 `value()` | 给读注解的切面传参数（这里是 INSERT / UPDATE） |

**@Retention 的三种取值**：

| 取值 | 保留阶段 | 能否反射读取 |
|------|----------|:------------:|
| `SOURCE` | 仅源码 | ❌ |
| `CLASS` | 编译到 class 文件 | ❌ |
| `RUNTIME` | 运行期 | ✅ |

---

### 3. AOP 切面

```java
@Before("execution(* com.sky.mapper.*.*(..)) && @annotation(autoFill)")
public void autoFill(JoinPoint joinPoint, AutoFill autoFill) { ... }
```

**切点表达式拆解**：

```text
execution(* com.sky.mapper.*.*(..)) && @annotation(autoFill)
     ↑              ↑    ↑  ↑         ↑
    任意返回值   包名  任意类 任意方法  且方法上有 @AutoFill 注解
                       任意参数
```

---

## 二、核心概念

| 概念 | 含义 |
|------|------|
| **切点（Pointcut）** | 定义“拦哪些方法” |
| **通知（Advice）** | 定义“什么时候执行什么逻辑” |
| **连接点（JoinPoint）** | 被拦截的方法调用本身 |
| **@Before / @After / @Around** | 通知注解，指定执行时机 |

### 通知类型对比

| 注解 | 执行时机 | 能否阻止原方法 | 能否拿到返回值 |
|------|----------|:--------------:|:--------------:|
| `@Before` | 目标方法执行前 | ❌ | ❌ |
| `@After` | 目标方法执行后（无论异常） | ❌ | ❌ |
| `@AfterReturning` | 目标方法正常返回后 | ❌ | ✅ |
| `@AfterThrowing` | 目标方法抛异常后 | ❌ | ❌ |
| `@Around` | 包裹目标方法 | ✅ | ✅ |

### 关键理解

- **连接点是方法被拦截时 Spring 自动创建的**
- 在通知方法参数里写 `JoinPoint`，Spring 自动注入
- `@Around` 用 `ProceedingJoinPoint`，因为需要 `proceed()` 手动放行

```java
@Around("...")
public Object around(ProceedingJoinPoint pjp) throws Throwable {
    // 前置逻辑
    Object result = pjp.proceed();  // 放行，执行目标方法
    // 后置逻辑
    return result;
}
```

---

## 三、反射

### 1. 两步走

```java
// 第一步：找方法
Method setCreateTime = clazz.getDeclaredMethod("setCreateTime", LocalDateTime.class);

// 第二步：调用
setCreateTime.invoke(entity, now);
```

| 步骤 | 方法 | 说明 |
|------|------|------|
| 找方法 | `getDeclaredMethod("方法名", 参数类型.class)` | 返回 Method 对象 |
| 调用 | `method.invoke(对象, 参数值)` | 执行该方法 |

### 2. 为什么需要参数类型

> **Java 支持方法重载，光有方法名不唯一。**

```java
public void setStatus(Integer status) { }
public void setStatus(String status) { }
```

只写 `getDeclaredMethod("setStatus")` 无法确定是哪一个，必须指定参数类型：

```java
clazz.getDeclaredMethod("setStatus", Integer.class);
```

### 3. 反射能获取什么

反射能获取：**字段、方法、构造器、注解、类信息**等，不只是 setter。

| 用途 | 方法 |
|------|------|
| 获取字段 | `getDeclaredField("name")` |
| 获取方法 | `getDeclaredMethod("setName", String.class)` |
| 获取构造器 | `getDeclaredConstructor(...)` |
| 获取注解 | `getAnnotation(AutoFill.class)` |
| 获取类信息 | `getName()`、`getSimpleName()` |

---

## 四、完整实战代码

### 1. 枚举

```java
package com.sky.enumeration;

public enum OperationType {
    UPDATE,
    INSERT
}
```

### 2. 自定义注解

```java
package com.sky.annotation;

import com.sky.enumeration.OperationType;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
```

### 3. 切面

```java
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

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切点：拦截 mapper 包下所有方法，且方法上有 @AutoFill 注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知：在目标方法执行前，自动填充公共字段
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 1. 获取被拦截方法上的注解，得到操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 2. 获取被拦截方法的参数（实体对象）
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 3. 准备填充的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4. 根据操作类型，通过反射填充对应字段
        try {
            if (operationType == OperationType.INSERT) {
                Method setCreateTime = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } else if (operationType == OperationType.UPDATE) {
                Method setUpdateTime = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }
        } catch (Exception e) {
            log.error("公共字段自动填充失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
```

### 4. 常量类

```java
package com.sky.constant;

public class AutoFillConstant {
    public static final String SET_CREATE_TIME = "setCreateTime";
    public static final String SET_CREATE_USER = "setCreateUser";
    public static final String SET_UPDATE_TIME = "setUpdateTime";
    public static final String SET_UPDATE_USER = "setUpdateUser";
}
```

### 5. 在 Mapper 上使用

```java
@Mapper
public interface EmployeeMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);
}
```

---

## 五、执行流程图

```text
调用 employeeMapper.insert(employee)
        ↓
AOP 拦截（切点匹配到 @AutoFill）
        ↓
@Before 通知执行 autoFill()
        ↓
① 读注解 → 得到 OperationType.INSERT
② 拿参数 → entity = employee
③ 准备数据 → now, currentId
④ 反射调用 setter → 填充 createTime / createUser / updateTime / updateUser
        ↓
放行，执行原 insert 方法
        ↓
SQL 执行，公共字段已填好
```

---

## 六、知识点小结

| 知识点 | 核心内容 |
|--------|----------|
| **枚举** | 引用类型，特殊类，继承 `java.lang.Enum` |
| **@Target** | 注解用在哪（METHOD / TYPE / FIELD 等） |
| **@Retention** | 注解保留阶段，AOP 必须 `RUNTIME` |
| **注解属性** | 给切面传参数 |
| **切点** | 定义拦哪些方法 |
| **通知** | 定义什么时候执行什么逻辑 |
| **连接点** | 被拦截的方法调用，Spring 自动创建 |
| **JoinPoint** | 写在通知参数里，Spring 自动注入 |
| **ProceedingJoinPoint** | `@Around` 专用，需 `proceed()` 放行 |
| **反射两步** | `getDeclaredMethod` 找方法 + `invoke` 调用 |
| **为什么传参数类型** | Java 方法重载，光有方法名不唯一 |

---

## 七、常见面试点

| 问题 | 答案要点 |
|------|----------|
| 为什么要用 AOP 自动填充？ | 公共字段重复 set，代码冗余，AOP 统一处理 |
| @Retention 为什么要 RUNTIME？ | 只有 RUNTIME 才能在运行时通过反射读到注解 |
| JoinPoint 和 ProceedingJoinPoint 区别？ | JoinPoint 用于 @Before/@After；ProceedingJoinPoint 用于 @Around，可 proceed() |
| 反射为什么需要参数类型？ | Java 支持重载，方法名不唯一，需参数类型定位 |
| 切点表达式 `execution(* com.sky.mapper.*.*(..))` 含义？ | mapper 包下任意类、任意方法、任意参数、任意返回值 |
| `&& @annotation(autoFill)` 作用？ | 只拦截标注了 @AutoFill 的方法 |