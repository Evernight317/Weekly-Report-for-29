# SpringBoot

## 一、配置优先级

总共5种配置方式，3种配置文件+java系统属性+命令行参数；

其中*命令行参数>java系统属性>3种配置文件（.properties>.yml>yaml）* ；

---

三种格式配置文件：`application.properties` `application.yml` `application.yaml`

优先级：properties>yml>yaml；

springboot支持多种格式配置文件，但一般统一使用一种格式的配置（yml是主流）

其他属性配置方式

- **Java系统属性** ：`-Dserver.port=9000` ，IDEA中在VM Option
- **命令行参数** ：`--server.port=10010` ，IDEA 在 program arguments
- *命令行参数* 优先级更高 ；

IDEA外设置

比如，执行maven打包指令package 为jar包；执行java指令运行jar包时，就可以设置了；

（Springboot项目进行打包时，需要引入插件spring-boot-maven-plugin（基于官网骨架创建项目，会自动添加该插件））

- 在`java`后跟指令设置java系统属性；
- 在最后设置命令行参数；
- `java [options] -jar <jar文件> [args...]`

## 二、Bean管理

### 1.获取bean

主动获取bean对象

- 根据bean的name获取bean：`Object getBean(String name)`
- 根据类型获取bean：`<T> T getBean(Class<T> requiredType)`
- 根据name获取bean（带类型转换）：`<T> T getBean(String name, class<T> requiredType)`

```Java
// springboot获取IOC容器直接将IOC容器注入即可？
// 注入IOC容器对象就能调用了？
private ApplicationContext applicationContext;
```

默认情况下，bean是**单例** 的，一个容器中只能有一个bean对象；

- 默认情况下，Spring项目启动时，会把bean都创建好放在IOC容器中；但这个是针对默认的单例非延迟加载的bean而言，实际还会受到作用域和延迟初始化的影响；

### 2.bean作用域

spring支持五种作用域，后三种在web环境生效；

### 第三方bean

## 三、SpringBoot原理
