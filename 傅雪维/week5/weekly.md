# Gin 框架学习记录

> 目标：掌握 Gin 基础路由、静态文件服务、安全防护与生产配置

---

## 模块 A – Gin 基础与路由

### 1. 环境准备
- 创建项目目录 `my-go-http`，初始化模块：
  ```bash
  mkdir gin-learning && cd gin-learning
  go mod init gin-learning
  ```
- 安装 Gin：
  ```bash
  go get -u github.com/gin-gonic/gin
  ```

### 2. 编写 `main.go` – 基本路由
创建 `main.go`，实现三个接口：
- `/ping` – 健康检查（GET）
- `/user/:id` – 路径参数（GET）
- `/search` – 查询参数（GET）

```go
package main

import (
	"net/http"
	"github.com/gin-gonic/gin"
)

func main() {
	// 使用默认引擎（带 Logger 和 Recovery）
	r := gin.Default()

	// 1. 健康检查
	r.GET("/ping", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"message": "pong",
		})
	})

	// 2. 路径参数
	r.GET("/user/:id", func(c *gin.Context) {
		id := c.Param("id")
		c.JSON(http.StatusOK, gin.H{
			"user_id": id,
			"name":    "示例用户",
		})
	})

	// 3. 查询参数
	r.GET("/search", func(c *gin.Context) {
		q := c.Query("q")
		if q == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "缺少 q 参数"})
			return
		}
		c.JSON(http.StatusOK, gin.H{
			"query": q,
			"results": []string{"结果1", "结果2"},
		})
	})

	// 启动服务
	r.Run(":8080")
}
```

### 3. 测试结果（Postman）
| 接口 | 请求 | 响应 |
|------|------|------|
| `GET /ping` | 无参数 | `{"message":"pong"}` |
| `GET /user/123` | 路径参数 id=123 | `{"user_id":"123","name":"示例用户"}` |
| `GET /search?q=gin` | 查询参数 q=gin | `{"query":"gin","results":["结果1","结果2"]}` |
| `GET /search` | 无 q | 400 `{"error":"缺少 q 参数"}` |

### 4. 遇到问题与解决
- **问题**：尝试用 `c.BindJSON` 解析 GET 请求体导致报错。  
  **解决**：分清 `ShouldBindJSON` 只用于 POST/PUT 等带 JSON 体的请求，GET 应使用 Query 或 Param。

- **补充**：区分 `gin.Default()` 和 `gin.New()`，默认引擎包含日志和恢复中间件，适合开发；裸引擎需手动添加。

---

## 模块 B – 静态文件与页面渲染

### 1. 静态站点搭建
- 创建 `public/` 目录，放入 `index.html`、`style.css`、`app.js` 和一张图片。
- 在 `main.go` 中注册静态路由（**注意顺序**）：

```go
// 必须先注册 API 路由，再注册静态路由
r.GET("/ping", ...)           // API
r.GET("/user/:id", ...)
r.GET("/search", ...)

// 静态文件服务 – 根路径映射到 ./public
r.Static("/", "./public")
```

- 访问 `http://localhost:8080/` 自动显示 `index.html`，CSS/JS 正常加载。

### 2. API 路由分组
将已有 API 移至 `/api` 分组，避免与静态路由冲突：

```go
api := r.Group("/api")
{
	api.GET("/ping", pingHandler)
	api.GET("/user/:id", userHandler)
	api.GET("/search", searchHandler)
}
```

现在静态路由仍然为 `/`，API 前缀为 `/api`，结构清晰。

### 3. 模板渲染（额外练习）
- 创建 `templates/` 目录，放入 `index.tmpl`。
- 加载模板并渲染动态数据：

```go
r.LoadHTMLGlob("templates/*")
r.GET("/hello", func(c *gin.Context) {
	c.HTML(http.StatusOK, "index.tmpl", gin.H{
		"title": "Gin 模板",
		"name":  "世界",
	})
})
```

### 4. 遇到问题与解决
- **问题**：访问 `/ping` 返回 404，被静态文件处理器拦截。  
  **解决**：将 `r.Static("/", "./public")` 移到所有 API 路由之后，确保精确路由优先匹配。

- **问题**：`r.Static` 无法自定义 `index.html` 以外的默认文件。  
  **解决**：使用 `r.StaticFile("/", "./public/index.html")` 单独指定首页。

---

## 模块 C – 安全防护与生产配置

### 1. 路径遍历漏洞复现与防御
- **初始代码**（危险）：
  ```go
  r.Static("/static", "./public")
  ```
  攻击者请求 `/static/../../../etc/passwd` 可能读取系统文件。

- **防御措施**：
  1. 将静态目录转为**绝对路径**：
     ```go
     absPath, _ := filepath.Abs("./public")
     r.Static("/static", absPath)
     ```
  2. 添加中间件拦截包含 `..` 的请求：
     ```go
     func safePathMiddleware() gin.HandlerFunc {
         return func(c *gin.Context) {
             if strings.Contains(c.Request.URL.Path, "..") {
                 c.AbortWithStatusJSON(403, gin.H{"error": "非法路径"})
                 return
             }
             c.Next()
         }
     }
     // 应用到静态路由组
     staticGroup := r.Group("/static", safePathMiddleware())
     staticGroup.Static("/", absPath)
     ```
  3. （可选）禁用符号链接：`http.Dir` 默认会跟随符号链接，若业务不需要，可在中间件中额外校验。

- **测试**：请求 `/static/../../../etc/passwd` 返回 403，安全生效。

### 2. 生产配置
- 自定义 `http.Server`，设置超时和头部限制：

```go
srv := &http.Server{
    Addr:           ":8080",
    Handler:        r,
    ReadTimeout:    5 * time.Second,
    WriteTimeout:   10 * time.Second,
    MaxHeaderBytes: 1 << 20, // 1MB
}
srv.ListenAndServe()
```

- 开启生产模式（隐藏调试信息）：
  ```go
  gin.SetMode(gin.ReleaseMode)
  ```

### 3. 最终 `main.go` 结构（核心部分）
```go
func main() {
    gin.SetMode(gin.ReleaseMode)
    r := gin.New()
    r.Use(gin.Logger(), gin.Recovery()) // 保留必要中间件

    // API 路由（先注册）
    api := r.Group("/api")
    // ... 定义路由

    // 静态路由（后注册，带安全中间件）
    abs, _ := filepath.Abs("./public")
    r.Use(safePathMiddleware())
    r.Static("/", abs)

    srv := &http.Server{...}
    srv.ListenAndServe()
}
```

### 4. 问题与解决
- **问题**：仅使用 `filepath.Abs` 后，攻击者仍能通过 `../` 穿越，因为 `http.Dir` 只做简单清理。  
  **解决**：必须结合中间件显式检查 `..`，双重保障。

- **问题**：生产模式开启后，错误返回不再显示堆栈，但日志仍会记录，便于排查。

---

## 📝 下周学习计划（模块 D、E、F）

| 模块 | 学习内容 | 目标成果 |
|------|---------|---------|
| **D – 中间件深入** | 自定义日志格式、鉴权中间件（JWT）、跨域处理（CORS） | 实现可复用的鉴权中间件，保护 `/api/admin` 路由 |
| **E – 参数绑定与验证** | 使用 `binding` 标签（`required`、`min`、`email` 等），统一错误处理 | 创建用户注册接口，自动校验请求体，返回友好错误信息 |
| **F – 数据库集成** | GORM + MySQL 连接，模型定义，CRUD 操作 | 实现完整的图书管理 API（增删改查） |
