# Web 全栈开发学习路线与核心知识梳理

---

## 一、学习路线总览
```mermaid
flowchart LR
    A[📦 Maven] --> B[⚙️ SpringBoot + SpringMVC] --> C[🗄️ MySQL] --> D[🔗 JDBC / MyBatis]
    D --> E[🧪 Web 案例实战] --> F[🔐 Cookie · Session · 令牌]
    F --> G[🛡️ Filter / Interceptor<br>（令牌统一拦截校准）] --> H[✂️ AOP] --> I[🧠 SpringBoot 原理]
```

---

## 二、Web 开发架构

### 前后端分离架构
```mermaid
flowchart LR
    A[浏览器] --> B[前端服务器] --> C[后端服务器] --> D[数据库服务器]
```
![Web前后端分离架构](./img/1.png)

- **前端代码**：通过浏览器的解析和渲染转换成可视化的网页
- **开发模式**：前后端分离开发

---

## 三、Web 标准

Web 标准由三部分组成：

| 组成部分 | 作用 |
|---------|------|
| **HTML** | 页面结构 |
| **CSS** | 页面样式 |
| **JavaScript** | 页面交互 |

---
