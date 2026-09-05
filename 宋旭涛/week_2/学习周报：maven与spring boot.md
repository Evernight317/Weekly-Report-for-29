# 学习周报：Maven 项目管理与 Spring Boot 入门实践
**周期**：2026年7月6日 - 2026年7月11日
**主题**：Maven 项目构建与管理、Spring Boot 快速上手、RESTful API 开发与集成测试

## 一、本周学习概述
本周围绕 Java 后端开发的基础设施展开系统性学习，核心覆盖两大模块：Maven 项目管理工具和 Spring Boot 框架入门。通过理论学习结合动手实践，完成了从项目骨架搭建、依赖管理、多模块构建，到基于 Spring Boot 开发第一个 RESTful 接口的全链路验证，初步掌握了企业级 Java 后端开发的标准流程与最佳实践。

## 二、核心学习内容
### 2.1 Maven 项目构建与管理
#### 2.1.1 Maven 核心原理
Maven 是 Apache 旗下的项目构建与依赖管理工具，基于 项目对象模型（POM） 理念，通过 pom.xml 声明项目结构、依赖关系与构建生命周期。

三大核心组件：

坐标（GAV）：groupId、artifactId、version 唯一标识一个构件，用于依赖管理。

仓库（Repository）：本地仓库（~/.m2/repository）缓存下载的依赖，中央仓库（Maven Central）提供公共构件，私服（Nexus/Artifactory）用于企业内部共享。

生命周期（Lifecycle）：包含 clean、default（核心，含 compile、test、package、install、deploy 等阶段）、site 三套生命周期，按顺序执行。

依赖管理特性：

传递性依赖：A 依赖 B，B 依赖 C，则 A 自动引入 C（除非排除）。

依赖范围：compile（默认，全生命周期有效）、provided（编译测试有效，运行时由容器提供）、runtime（运行时有效）、test（仅测试有效）。

冲突解决：最短路径优先 + 第一声明优先，可通过 dependencyManagement 统一版本。

#### 2.1.2 Maven 多模块项目搭建
实践场景：构建一个包含 common（通用工具）、api（接口定义）、service（业务实现）的多模块项目。

父模块 POM 关键配置：

```xml
<!-- 父 pom.xml -->
<packaging>pom</packaging>

<modules>
    <module>common</module>
    <module>api</module>
    <module>service</module>
</modules>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>3.2.5</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
子模块继承父模块，可单独配置特有依赖，实现模块化开发与独立构建。

#### 2.1.3 常用插件与扩展
插件	作用
maven-compiler-plugin	指定 JDK 版本（如 17）
maven-surefire-plugin	执行单元测试，可配置跳过测试
maven-assembly-plugin	打包可执行 JAR（含所有依赖）
spring-boot-maven-plugin	打包 Spring Boot 应用，支持 repackage 和 run
### 2.2 Spring Boot 框架入门
#### 2.2.1 Spring Boot 核心特性
Spring Boot 基于 Spring 框架，通过自动配置与起步依赖（Starter）大幅简化了 Spring 应用的初始搭建与开发过程。

自动配置：根据 classpath 中的 jar 包自动配置 Bean（如 DataSourceAutoConfiguration 检测到 H2 驱动则自动创建数据源）。

起步依赖：聚合常用依赖，如 spring-boot-starter-web 包含 Tomcat、Spring MVC、Jackson 等。

嵌入式容器：内置 Tomcat/Jetty/Undertow，可直接运行 JAR 包，无需部署 WAR。


2.2.2 快速搭建 RESTful API
项目初始化：使用 Spring Initializr（或 IDEA 内置）生成基础项目，选择依赖 Spring Web、Lombok、Spring Boot DevTools。

控制器示例：

```java
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("查询用户 id: {}", id);
        // 模拟业务逻辑
        User user = new User(id, "张三", "zhangsan@example.com");
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("创建用户: {}", user);
        // 实际保存操作...
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
```
配置文件（application.yml）：

```yaml
server:
  port: 8080

spring:
  application:
    name: demo-service

logging:
  level:
    com.example: DEBUG
```
#### 2.2.3 单元测试与集成测试
单元测试：使用 @SpringBootTest 加载上下文，结合 @MockBean 模拟外部依赖。

集成测试：利用 TestRestTemplate 或 WebTestClient 发送 HTTP 请求验证接口功能。

测试切片：@WebMvcTest 仅加载 Web 层，@DataJpaTest 仅加载 JPA 层，提升测试速度。

示例测试代码：
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetUser() {
        User user = restTemplate.getForObject("http://localhost:" + port + "/api/users/1", User.class);
        assertThat(user.getName()).isEqualTo("张三");
    }
}
```


## 三、本周实践进展
### 3.1 已完成事项
Maven 多模块项目搭建：成功创建包含 common、api、service 三模块的项目结构，实现模块间依赖继承与独立打包。

Spring Boot 入门 Demo：基于 Spring Initializr 生成项目，开发了用户管理相关的 CRUD 接口，并完成 Postman 联调测试。


### 3.2 关键代码产出
pom.xml（父模块及子模块）：多模块 Maven 配置。

UserController.java：RESTful 接口实现。

UserService.java 及实现类：业务逻辑层。

GlobalExceptionHandler.java：统一异常处理，返回规范错误格式。

测试类：UserControllerTest、UserServiceTest。

## 四、问题与解决方案
### 4.1 Maven 依赖冲突导致启动失败
问题：引入 spring-boot-starter-web 和 spring-boot-starter-data-jpa 后，项目启动报 ClassNotFoundException，原因是一些传递性依赖版本不兼容。
解决：在父 POM 中使用 dependencyManagement 统一管理 Spring Boot 版本（通过 spring-boot-dependencies BOM 导入），避免显式指定子版本，利用 Maven 的依赖仲裁机制解决冲突。

### 4.2 测试时数据库连接未生效
问题：集成测试时因未配置数据库连接信息导致 DataSource 初始化失败。
解决：使用 @AutoConfigureTestDatabase(replace = Replace.ANY) 替换为嵌入式 H2 数据源，或使用 @TestPropertySource 指定测试专用配置。

### 4.3 项目热部署不生效
问题：修改代码后需手动重启应用，开发效率低。
解决：添加 spring-boot-devtools 依赖，并在 IDEA 中开启自动编译（Build -> Build Project），DevTools 会自动检测变更并重启应用（或使用 JRebel 等更高级方案）。

## 五、下周学习计划
Spring Boot 进阶：深入学习数据访问层（Spring Data JPA + MySQL），实现完整的增删改查；集成 Swagger/Knife4j 生成 API 文档。

Maven 私服搭建：使用 Nexus 搭建本地仓库，学习构件发布与私服配置。

安全与鉴权：引入 Spring Security，实现基于 JWT 的身份认证与权限控制。

项目整合实践：将现有的 Spring Boot 工程与前端 Vue 项目联调，完成一个简单的员工信息管理系统原型。

## 六、总结与思考
本周的学习为我打下了扎实的 Java 后端开发基础。Maven 解决了依赖管理混乱和构建流程标准化问题，而 Spring Boot 则通过自动化配置极大地提升了开发效率。两者结合，使得从零搭建一个可运行的后端服务变得轻量而高效。

后续学习将聚焦于数据持久化、接口安全、性能优化等实战能力，并逐步引入微服务架构（如 Spring Cloud）的相关组件。本周的实践让我深刻体会到"约定优于配置"带来的便利，但也意识到自动化配置背后的原理仍需深入理解，以便在出现问题时能快速定位和解决。