# 后端开发进阶与身份认证 学习周报

**学习周期**：2026年08月17日 —— 2026年08月23日（第 2 周）  
**学习目标**：掌握用户身份认证机制（JWT）、数据库多表关联设计与中间件鉴权，完成带登录防护的业务 API  
**学习状态**：已按计划顺利完成  

---

## 📅 一周学习时间与进度安排

| 日期 | 学习主题 | 核心学习内容 | 学习时长 | 状态 |
| :--- | :--- | :--- | :---: | :---: |
| **Mon (周一)** | 密码安全与哈希加密 | 了解明文密码存储风险，学习 `bcrypt` 算法及盐值（Salt）哈希加密与校验 | 2.5h | 🟢 完成 |
| **Tue (周二)** | Token 认证机制 (JWT) | 理解 Session/Cookie vs JWT 异同，学习 JWT 的 Header/Payload/Signature 结构及签发原理 | 3.0h | 🟢 完成 |
| **Wed (周三)** | 自定义鉴权中间件 | 编写 `authMiddleware` 校验请求头中的 `Authorization: Bearer <token>`，实现受保护路由隔离 | 3.5h | 🔥 突破 |
| **Thu (周四)** | 数据库多表设计与 Join | 设计用户表与文章/订单表的一对多（1:N）外键关联，编写 `JOIN` 复合查询 SQL | 3.5h | 🟢 完成 |
| **Fri (周五)** | 业务接口二次升级 | 实现“用户登录”、“发布文章”、“获取我的文章列表”等强关联业务 API | 3.5h | 🟢 完成 |
| **Sat (周六)** | 全流程接口测试与压测 | 在 Postman 设置环境变量（自动保存 Token），模拟多用户登录及非法请求拦截 | 3.5h | 🟢 完成 |
| **Sun (周日)** | 错题回顾与周报撰写 | 梳理 JWT 过期处理、统一错误返回格式，总结两周后端学习路径 | 2.0h | 🟢 完成 |
| **总计** | -- | **深化安全与数据关联，实现真实业务逻辑** | **21.5h** | **优秀** |

---

## 📝 核心知识点梳理

### 1. 用户密码安全存储（Bcrypt）
*   **原则**：数据库**绝不能**存明文密码！
*   **盐值哈希（Salted Hash）**：使用 `bcrypt.hash(password, saltRounds)` 进行不可逆加密，即使相同密码每次生成的密文也不同，有效抵御彩虹表攻击。

### 2. JWT (JSON Web Token) 身份认证
*   **无状态认证**：服务端无需保存 Session 记录，有利于集群扩展。
*   **Token 组成**：`Header.Payload.Signature`
    *   **Payload**：存放非敏感用户信息（如 `userId`, `role`）及过期时间 `exp`。
*   **鉴权中间件实现逻辑**：
    ```javascript
    const jwt = require('jsonwebtoken');

    function authMiddleware(req, res, next) {
        const authHeader = req.headers['authorization'];
        const token = authHeader && authHeader.split(' ')[1]; // 提取 Bearer <token>

        if (!token) {
            return res.status(401).json({ success: false, message: '未提供访问令牌，拒绝访问' });
        }

        jwt.verify(token, process.env.JWT_SECRET || 'secret_key', (err, user) => {
            if (err) {
                return res.status(403).json({ success: false, message: 'Token 无效或已过期' });
            }
            req.user = user; // 将解码后的用户信息挂载到请求对象上
            next(); // 放行进入下一个路由处理器
        });
    }
    ```

### 3. 数据库多表关联与查询
*   **外键关联**：在文章表（`articles`）中增加 `user_id` 关联用户表 `users(id)`。
*   **多表联合查询（SQL）**：
    ```sql
    SELECT a.id, a.title, a.content, u.username AS author
    FROM articles a
    JOIN users u ON a.user_id = u.id
    WHERE a.user_id = 1;
    ```

---

## 💻 本周实战成果：带认证与权限的“个人文章 API”

在上一周纯 CRUD 的基础上，本周将服务升级为**具备真实生产逻辑的后端系统**：

1.  `POST /api/auth/register` —— 用户注册（密码经过 bcrypt 加密后入库）。
2.  `POST /api/auth/login` —— 用户登录（验证密码成功后签发 JWT Token）。
3.  `POST /api/articles` *(需 Auth)* —— 依赖鉴权中间件，自动获取当前登录用户的 ID 并创建文章。
4.  `GET /api/articles/my` *(需 Auth)* —— 获取当前登录用户发表的所有文章。

---

## ❓ 遇到的问题与解决方案

1.  **问题：Postman 每次调用受保护接口都要手动复制粘贴 Token，效率低下**  
    *   *解决*：在 Postman 的登录接口 `Tests` 标签中编写脚本：`pm.environment.set("token", pm.response.json().token);`，并在 Header 中统一设置为 `Bearer {{token}}` 实现自动化测试。

2.  **问题：Token 过期后前端页面直接卡死或报错**  
    *   *解决*：在后端统一返回 `401 Unauthorized` 状态码与明确的 `code: "TOKEN_EXPIRED"` 标识，告知前端需要引导用户重新登录。

---

## 🚀 下一步学习计划

- [ ] **全局错误处理与日志记录**：编写统一兜底 error-handling 中间件，引入 `winston` 或 `morgan` 记录服务端请求日志。
- [ ] **项目部署与线上运行**：学习 Docker 容器化基础，尝试将后端服务与数据库部署到云服务器环境。
