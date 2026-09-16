# Spring MVC 基础

> Spring MVC 是 Spring 框架中用于构建 Web 应用的核心模块。

---

## 一、核心组件：DispatcherServlet

**DispatcherServlet（前端控制器）** 是 Spring MVC 的总调度中心：

```text
统一收请求 → 找 Controller → 调方法 → 处理返回值 → 返回响应
```

- 它让整个 Web 层解耦、可扩展
- 是 Spring MVC 能工作的核心

### 与 HttpServletRequest / HttpServletResponse 的关系

- DispatcherServlet 是一个 Servlet
- 它接收 `HttpServletRequest` 和 `HttpServletResponse` 作为方法参数
- 并在整个 Spring MVC 流程中传递、使用它们
- 三者是 **使用关系**，不是组成关系

| 对象 | 角色 |
|------|------|
| HttpServletRequest | 请求对象 |
| HttpServletResponse | 响应对象 |
| DispatcherServlet | 调度器（使用上面两者） |

---

## 二、Controller

- Controller 是处理 HTTP 请求的入口

### @RestController

分为两个部分：

1. `@Controller`：标记这个类是控制类
2. `@ResponseBody`：自动返回 JSON 响应体

### 请求映射注解

| 注解 | 匹配的 HTTP 方法 |
|------|----------------|
| `@RequestMapping` | 所有 HTTP 方法（GET、POST 等）都匹配 |
| `@GetMapping` | 只有 GET 方法 |
| `@PostMapping` | 只有 POST 方法 |

---

## 三、Spring Boot 处理 HTTP 请求的匹配逻辑

```text
浏览器请求 /xxx
    ↓
① 先找 Controller 里有没有匹配的 @RequestMapping / @GetMapping
    ↓
找到了 → 执行 Controller 方法，返回响应（流程结束）
    ↓
没找到 → 继续往下
    ↓
② 去静态资源目录里找（static/、public/ 等）
    ↓
找到了 → 返回文件（HTML、JS、CSS、图片等）
    ↓
还没找到 → 返回 404
```

**优先级**：Controller 映射 > 静态资源映射