# AOP

spring框架核心之一；Aspect Oriented Programming（*面向切面编程 、面向方面编程*），其实就是*面向特定方法编程* ；

例：统计每个业务方法的执行耗时。当有多个方法时，分开统计较为繁琐；

定义一个模板方法：1、获取方法运行开始时间；2、 运行原始方法；3、 获取方法运行结束时间，计算执行耗时；

**动态代理** 是面向切面编程最主流的实现。而SpringAOP是Spring框架的高级技术，旨在管理bean对象的过程中，通过底层的动态代理机制，对特定的方法进行编程；

---

## 入门程序

- 导入依赖
- 编写AOP

```XML
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

- 交给IOC容器管理，加上`@Component`注解；
- 声明AOP类：加上`@Aspect`注解；
- 通过`@Around("execution(* com.itheima.service.*.*(..))")`声明针对的原始方法，格式为`包名.*.*()`第一个`*`代表类名/接口名，第二个代表方法名；变更需求；
- `JoinPoint` 会封装原始方法的基本信息，`JoinPoint.getSignature`可以获取方法名；

```Java
@Component
@Aspect
public class TimeAspect {
  // 声明针对的原始方法
	@Around("execution(* com.itheima.service.*.*(..))")
	public Object recordTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
	long begin = System.currentTimeMillis();
    // 调用原始方法运行
	Object object = proceedingJoinPoint.proceed();
	long end = System.currentTimeMillis();
	log.info(proceedingJoinPoint.getSignature()+"执行耗时:{}ms"，end - begin);
	return object;
}
```

优势：代码无侵入；减少重复代码；开发维护方便；

## 概念

- **连接点** ：JoinPoint，可以被AOP控制的方法（暗含方法执行时的相关信息）；
- **通知** ： Advice，指哪些重复的逻辑，也就是共性功能（最终体现为一个方法）；
- **切入点** ：PointCut，匹配连接点的条件，通知仅会在切入点方法执行时被应用；
- 切入点表达式：`execution`中的语句；
- **切面** ：Aspect，描述通知与切入点的对应关系（*通知+切入点*）（切面描述的是什么样的原始方法在什么条件下会执行什么操作）；
- 切面类：被@Aspect注解的类；
- **目标对象** ：Target，通知所应用的对象；

实际运行时，会生成并注入**代理对象** ；

## 二、AOP进阶

### 1.通知类型

（根据字面意思理解

| `@Around`                                        | `@Before`                                  | `@After`                                                                 | `@AfterReturning`                                                | `@AfterThrowing`                         |
| -------------------------------------------------- | -------------------------------------------- | -------------------------------------------------------------------------- | ------------------------------------------------------------------ | ------------------------------------------ |
| 环绕通知                                           | 前置通知                                     | 后置通知<br />（也称为最终通知）                                           | 返回后通知                                                         | 异常后通知                                 |
| 此注解标注的通知方法<br />在目标方法前、后都被执行 | 此注解标注的通知方法<br />在目标方法前被执行 | 此注解标注的通知方法<br />在目标方法后被执行，<br />无论是否有异常都会执行 | 此注解标注的通知方法<br />在目标方法后被执行，<br />有异常不会执行 | 此注解标注的通知方法在<br />发生异常后执行 |

- 出现异常的话，@Around的方法后执行的内容也不会执行；
- `@Around`环绕通知需要**手动调用** `ProceedingJoinPoint.proceed()`来让原始方法执行，其他通知不需要考虑目标方法执行；
- `@Around`环绕通知方法的返回值，*必须指定为`Object`*，来接收原始方法的返回值；例如对于业务层方法，处理完之后会返回Result，如果通知不返回值的话相当于处理完之后没有返回值了；
- 重复的切入点表达式：可以使用抽取的思想；
- 如果方法为`public`，那么**其他类** 也可以使用这个抽取出的切入点表达式；

```Java
// 切入点表达式的抽取
// pointcut即切点
@Pointcut("切入点表达式");
private void pt();
// 在切入点表达式中直接输入pt()即可
```

#### @Pointcut

是将公共的切点表达式抽取出来，需要用到时引用该切点表达式即可；

- private：仅能在当前切面类中引用该表达式；
- public：在其他外部的切面类中也可以引用该表达式；

### 2.通知顺序(理解？)

当有多个切面的切入点（不同切面类）都匹配到了目标方法，目标方法运行时，多个通知方法都会被执行；

~（整体类似于压栈）~

~顺序和**类名字母排序** 有关系，越小的先执行（进栈），（后执行的先出栈，即）~

- 目标方法前的通知方法：字母排名**靠前** 的先执行；
- 目标方法后的通知方法：字母排名**靠后** 的后执行；

#### spring提供的顺序控制方式

`@Order`注解加在切面类上来控制顺序；

- 目标方法前的通知方法：数字小的**先** 执行；
- 目标方法后的通知方法：数字小的**后** 执行；

### 3.切入点表达式

描述切入点方法的表达式，主要用来决定项目中的哪些方法需要加入通知；

| `execution()`       | `@annotation` |
| --------------------- | --------------- |
| 根据方法的签名来匹配  | 根据注解匹配    |

#### @execution

主要根据方法的返回值、包名、类名、方法名、方法参数等信息来匹配

`execution(访问修饰符? 返回值 包名.类名.? 方法名(方法参数) throws 异常?)`

```Java
execution(访问修饰符? 返回值 包名.类名.? 方法名(方法参数) throws 异常？)
```

- 其中带`?`的表示可以省略的部分

  - 访问修饰符：可省略，如public、protected；
  - 包名.类名：可省略；（一般不建议
  - throws异常：可省略（注意是方法上声明抛出的异常，不是实际抛出的异常）

| 通配符 |                                                                                                                  |
| ------ | ---------------------------------------------------------------------------------------------------------------- |
| `*`  | 单个独立的任意符号，可以通配任意返回值、包名、类名、方法名、任意类型的一个参数，也可以通配包、类、方法名的一部分 |
| `..` | 多个连续的任意符号，可以配任意层级的**包** ，或任意类型、任意个数的**参数** ；                            |

```Java
execution(* com.*.service.*.update*(*));
// 权限修饰符省略，返回值任意，com目录下任意名为service
// 的二级目录的任意类/接口
// update开头的任意方法，且只有一个形参，异常省略

execution(* com.itheima..DeptService.*(..))
// 权限修饰符省略，返回值任意，com/itheima目录下
// 所有名为DeptService的类/接口的所有方法
```

- 通配符可以使用`||` `&&` `!` 进行连接；
- 所有业务方法名在*命名时尽量规范* ，方便切入点表达式快速匹配。如：查询类方法都是find开头，更新类方法都是update开头；
- 描述切入点方法通常 **基于接口描述** ，而不是直接描述实现类，增强拓展性
- 在满足业务需要的前提下，尽量*缩小切入点的匹配范围* 。如包名匹配尽量不使用`..`使用`*`匹配单个包；

#### @annotation

execution不适合无规则的情况；

切入点表达式，用于匹配标识有特定注解的方法；

需要自定义一个注解，然后在要切入的方法上加上对应注解

`@annotation(注解全类名)`

```Java
@Retention(RetentionPolicy,RUNTIME) // 注解生效时机
@Target(ElementType.METHOD) // 注解作用位置
public @interface MyLog {
}

// 在需要切入的方法上加上自定义注解
@MyLog
public void example(){
}

// 修改切入点注解
@Pointcut("@annotation(com.itheima.aop.MyLog)")
private void pt() {}
```

### 4.连接点

在Spring中用`JoinPoint`抽象了连接点，用它可以获得*方法执行时的相关信息*，如目标类名、方法名、方法参数等。

- 对于`@Around`通知，获取连接点信息只能使用`ProceedingJoinPoint` ；
- 对于其他四种通知，获取连接点信息只能使用`org.aspectj.lang.JoinPoint`，它是`ProceedingJoinPoint`的父类型；

- 如果要获取方法信息，Around要加上形参`(ProceedingJoinPoint proceedingJoinPoint)`，其他四种加上形参`(JoinPoint joinPoint)`

```Java
// 获取类名
String className = joinPoint.getTarget().getClass().getName();
// 获取方法名
String methodName = joinPoint.getSignature().getName();
// 获取运行时传入参数
Object[] args = joinPoint.getArgs();
// 放行目标方法执行
// 这里需要接受目连接点的返回值
Object result = joinPoint.proceed();
// 获取目标方法运行的返回值
Object result = joinPoint.proceed();
```

- 前置通知拿不到返回值；

## AOP案例-操作日志记录

将crud全部记录：操作人、操作时间、执行方法的全类名、执行方法名、方法运行时参数、返回值、方法执行时长；

方法很多，故AOP实现；

- 因为要记录运行时长，所以用`@Around` ；
- crud方法名无规律，故用`@annotation` ；
