# JWT 与 Session 完全指南

> JWT 和 Session 是两种最常见的身份认证方案。本文从原理、结构、工作流程、安全、实战到选型，系统梳理两者的完整知识体系。

---

## 一、JWT（JSON Web Token）

### 1.1 什么是 JWT

JWT（JSON Web Token）是一种开放标准（RFC 7519），用于在各方之间以 JSON 对象的形式安全地传输信息。它常被用于身份认证和信息交换。

**核心特点**：

- **自包含**：Token 本身携带用户信息，服务端无需存储
- **无状态**：服务端不保存会话状态，适合分布式系统
- **可验证**：通过签名机制防止篡改

### 1.2 JWT 的结构

JWT 由三部分组成，用 `.` 分隔：

```text
xxxxx.yyyyy.zzzzz

Header.Payload.Signature
```

| 部分 | 内容 | 说明 |
|------|------|------|
| **Header** | 令牌用了什么算法 | 声明签名算法（如 HS256、RS256）和类型 |
| **Payload** | 用户姓名、id、令牌失效时间 `exp` | 存放实际传递的数据（Claims） |
| **Signature** | 签名 | 防止篡改 |

> 每一部分的信息都会用 **Base64URL** 编码。

**示例**：

```text
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### 1.3 签名原理与算法

#### 签名公式

```text
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

#### 常见算法对比

| 算法 | 类型 | 密钥 | 特点 |
|------|------|------|------|
| **HS256** | 对称加密 | 同一个密钥签名和验证 | 简单快速，但密钥需双方共享 |
| **RS256** | 非对称加密 | 私钥签名，公钥验证 | 更安全，适合分布式，公钥可公开 |
| **ES256** | 非对称加密 | 椭圆曲线 | 密钥更短，性能更好 |

**选型建议**：

- 单体应用、内部服务：HS256 足够
- 多服务、第三方对接：RS256 更合适（公钥可公开，私钥自己保留）

### 1.4 Payload 的 Claim 类型

Payload 中的字段称为 **Claim**，分为三种：

| 类型 | 说明 | 示例 |
|------|------|------|
| **Registered Claims** | 预定义的标准字段 | `exp`、`iat`、`nbf`、`iss`、`sub`、`aud`、`jti` |
| **Public Claims** | 公开自定义字段 | `name`、`role`、`email` |
| **Private Claims** | 私有自定义字段 | 业务特定字段，如 `tenantId` |

**常见 Registered Claims**：

| 字段 | 全称 | 说明 |
|------|------|------|
| `exp` | Expiration Time | 过期时间（Unix 时间戳） |
| `iat` | Issued At | 签发时间 |
| `nbf` | Not Before | 生效时间 |
| `iss` | Issuer | 签发者 |
| `sub` | Subject | 主题（通常是用户 ID） |
| `aud` | Audience | 接收方 |
| `jti` | JWT ID | 唯一标识，用于防重放 |

> ⚠️ **重要**：Payload 只是 Base64URL 编码，**不是加密**！任何人都能解码查看内容，**绝不能存放密码等敏感信息**。

### 1.5 JWT 的工作流程

```text
① 用户登录，服务器验证成功
        ↓
② 服务器生成 JWT 返回给客户端
        ↓
③ 客户端保存（通常 localStorage / cookie）
        ↓
④ 之后每次请求在 Authorization: Bearer <token> 中携带
        ↓
⑤ 服务器验证签名和过期时间，提取用户信息
```

### 1.6 JWT 的存储位置与安全

| 存储位置 | 优点 | 缺点 | 风险 |
|----------|------|------|------|
| **localStorage** | 简单、跨域友好 | 可被 JS 读取 | **XSS 攻击**可窃取 Token |
| **sessionStorage** | 同上，关闭标签页即清除 | 同上 | 同上 |
| **HttpOnly Cookie** | JS 无法读取 | 需处理 CSRF | **CSRF 攻击** |
| **内存（变量）** | 最安全 | 刷新页面丢失 | 需配合 Refresh Token |

**最佳实践**：

- **Access Token** 存内存或 HttpOnly Cookie
- **Refresh Token** 存 HttpOnly + Secure + SameSite Cookie
- 始终使用 **HTTPS**

### 1.7 Access Token + Refresh Token

单一 Token 的问题：过期时间短则频繁登录，过期时间长则安全风险大。

**双 Token 机制**：

```text
登录成功
    ↓
返回 Access Token（短，如 15 分钟） + Refresh Token（长，如 7 天）
    ↓
Access Token 过期 → 用 Refresh Token 换新的 Access Token
    ↓
Refresh Token 也过期 → 重新登录
```

| Token | 有效期 | 存储 | 用途 |
|-------|--------|------|------|
| Access Token | 短（15min ~ 1h） | 内存 / Cookie | 每次请求携带 |
| Refresh Token | 长（7d ~ 30d） | HttpOnly Cookie | 换取新 Access Token |

### 1.8 JWT 注销与黑名单

**问题**：JWT 无状态，服务端无法主动失效已签发的 Token。

**解决方案**：

| 方案 | 做法 | 优缺点 |
|------|------|--------|
| **短过期时间** | Access Token 设很短 | 简单，但用户体验差 |
| **黑名单** | 注销时把 Token 加入 Redis 黑名单 | 有效，但引入了状态 |
| **版本号** | 用户表存 `tokenVersion`，JWT 带版本号 | 改密码时版本 +1，旧 Token 全部失效 |
| **白名单** | 只允许特定 Token | 安全但失去无状态优势 |

### 1.9 JWT 安全风险

| 风险 | 说明 | 防护 |
|------|------|------|
| **alg: none 攻击** | 攻击者把算法改为 `none`，绕过签名 | 服务端强制指定算法 |
| **密钥泄露** | HS256 密钥泄露可伪造任意 Token | 使用强密钥、RS256、定期轮换 |
| **算法混淆** | RS256 公钥被当 HS256 密钥用 | 明确指定算法，不信任 Header |
| **Token 泄露** | XSS 窃取 localStorage | HttpOnly Cookie、CSP |
| **重放攻击** | 截获 Token 重复使用 | `jti` + 黑名单、HTTPS |

---

## 二、Session

### 2.1 什么是 Session

Session 是服务器端保存用户状态的机制。服务器为每个用户创建一个 Session 对象，存在内存 / 数据库 / Redis 中，并生成一个 Session ID 返回给客户端（通常存在 Cookie 里）。

**核心特点**：

- **有状态**：服务端保存会话数据
- **易注销**：删除服务端 Session 即可
- **依赖 Cookie**：通常靠 Cookie 传递 Session ID

### 2.2 Session 工作流程

```text
① 用户登录成功
        ↓
② 服务器创建 Session，存到服务端存储
        ↓
③ 返回 Set-Cookie: sessionid=xxx
        ↓
④ 浏览器后续请求自动带上 Cookie
        ↓
⑤ 服务器用 Session ID 查找对应 Session 数据
```

### 2.3 Session 存储方式

| 存储方式 | 优点 | 缺点 | 适用场景 |
|----------|------|------|----------|
| **内存** | 快、简单 | 重启丢失、无法集群 | 单机开发 |
| **数据库** | 持久化、可查询 | 慢、压力大 | 小规模 |
| **Redis** | 快、支持集群、可设 TTL | 需额外维护 | **生产环境首选** |
| **文件** | 简单 | 慢、不适合集群 | 极少使用 |

### 2.4 Session 过期与滑动续期

| 机制 | 说明 |
|------|------|
| **绝对过期** | 创建后固定时间过期，无论是否活跃 |
| **滑动过期** | 每次访问重置过期时间，活跃用户不掉线 |
| **空闲过期** | 超过一定时间无访问则过期 |

**Redis 实现**：使用 `EXPIRE` 命令，每次访问 `EXPIRE` 续期。

### 2.5 集群 Session 共享

多台服务器时，用户请求可能落到不同机器，需要共享 Session：

| 方案 | 做法 | 优缺点 |
|------|------|--------|
| **Session 复制** | 各服务器互相同步 | 网络开销大，不推荐 |
| **粘性会话** | 负载均衡把同一用户固定到同一台 | 简单，但故障时丢失 |
| **Redis 集中存储** | 所有服务器读写同一个 Redis | **推荐方案** |
| **Token（JWT）** | 改用无状态方案 | 彻底解决，但失去易注销特性 |

### 2.6 Cookie 安全属性

| 属性 | 作用 | 建议 |
|------|------|------|
| `HttpOnly` | JS 无法读取 Cookie | **必开**，防 XSS |
| `Secure` | 仅 HTTPS 传输 | **必开** |
| `SameSite=Strict` | 完全禁止跨站携带 | 最安全，但影响体验 |
| `SameSite=Lax` | 顶级导航允许携带 | **推荐默认** |
| `SameSite=None` | 允许跨站，需配 `Secure` | 跨域场景 |
| `Domain` | 指定生效域名 | 按需设置 |
| `Path` | 指定生效路径 | 按需设置 |
| `Max-Age` / `Expires` | 过期时间 | 按需设置 |

### 2.7 CSRF 攻击与防护

**CSRF（跨站请求伪造）**：攻击者诱导用户在其他网站发起已认证的请求。

**防护措施**：

| 措施 | 说明 |
|------|------|
| **CSRF Token** | 表单/请求头携带随机 Token，服务端校验 |
| **SameSite Cookie** | 限制跨站携带 Cookie |
| **验证 Referer / Origin** | 检查请求来源 |
| **关键操作二次验证** | 如转账需密码 |

---

## 三、Base64 与 Base64URL

### 3.1 Base64 编码原理

Base64 是一种把二进制数据编码成文本的方法，用 64 个可打印字符表示数据。

```text
A-Z (26个) + a-z (26个) + 0-9 (10个) + "+" + "/" = 64 个字符
再加上填充符 =
```

**编码过程**：

```text
原始: "Man"

字节: 01001101 01100001 01101110

6bit: 010011 010110 000101 101110

索引:   19     22     5      46

字符:   T      W      F      u

结果: "TWFu"
```

### 3.2 Base64URL 的差异

标准 Base64 有两个字符在 URL 里是特殊/不安全的：

| 标准 Base64 | Base64URL |
|-------------|-----------|
| `+` | `-` |
| `/` | `_` |
| `=` | 去掉 |

### 3.3 为什么要用 Base64URL

Base64 解决的不是“保密”，而是“让任意数据都能安全地变成纯文本、在 URL / Header / JSON 里无损传输”。

> **加密和编码是两件完全不同的事。**
> - **编码**：可逆，无密钥，目的是传输
> - **加密**：可逆，有密钥，目的是保密

---

## 四、JWT vs Session 全面对比

| 维度 | JWT | Session |
|------|-----|---------|
| **状态** | 无状态 | 有状态 |
| **存储位置** | 客户端 | 服务端 |
| **服务器压力** | 小（不存储） | 大（需存储） |
| **扩展性** | 好，多台服务器无需共享 | 差，需共享 Session |
| **跨域支持** | 好 | 差（Cookie 跨域受限） |
| **注销** | 难，需黑名单等机制 | 容易，删除即可 |
| **性能** | 每次需验签 | 每次需查存储 |
| **安全性** | 依赖密钥管理 | 依赖 Session ID 保密 |
| **Token 大小** | 较大（含 Payload） | 小（仅 ID） |
| **CSRF 风险** | 低（不依赖 Cookie） | 高（依赖 Cookie） |
| **XSS 风险** | 高（若存 localStorage） | 低（HttpOnly Cookie） |
| **适用场景** | 分布式、无状态 API | 传统 Web、需即时注销 |

---

## 五、如何选型

### 5.1 选 JWT 的场景

- 前后端分离、移动端 App
- 微服务架构、多服务间认证
- 跨域 API
- 需要无状态、易水平扩展
- 第三方开放平台（OAuth 2.0）

### 5.2 选 Session 的场景

- 传统服务端渲染 Web
- 需要即时注销（如后台管理系统）
- 单机或小规模集群
- 对安全性要求极高，不希望 Token 暴露在客户端

### 5.3 混合方案

实际项目中常见混合使用：

- **JWT 做认证** + **Redis 存黑名单/Refresh Token**
- **Session 做 Web** + **JWT 做 API**

---

## 六、实战示例

### 6.1 JWT 生成与校验（Java）

```java
// 生成
public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId().toString())
        .claim("name", user.getName())
        .claim("role", user.getRole())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
}

// 校验
public Claims parseToken(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET)
        .parseClaimsJws(token)
        .getBody();
}
```

### 6.2 Session 配置（Spring Boot + Redis）

```yaml
spring:
  session:
    store-type: redis
    timeout: 1800s
  redis:
    host: localhost
    port: 6379
```

```java
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
    // 自动启用 Redis Session
}
```

### 6.3 双 Token 刷新流程

```java
@PostMapping("/refresh")
public Result refresh(@CookieValue("refreshToken") String refreshToken) {
    // 1. 校验 Refresh Token
    Claims claims = parseToken(refreshToken);
    // 2. 生成新的 Access Token
    String newAccess = generateAccessToken(claims.getSubject());
    // 3. 返回
    return Result.ok(newAccess);
}
```

---

## 七、小结

| 知识点 | 核心内容 |
|--------|----------|
| JWT 结构 | Header.Payload.Signature，Base64URL 编码 |
| 签名原理 | HMACSHA256(base64(header) + "." + base64(payload), secret) |
| 算法选型 | HS256 对称、RS256 非对称 |
| JWT 流程 | 登录 → 生成 Token → 客户端保存 → 请求携带 |
| 双 Token | Access Token（短）+ Refresh Token（长） |
| JWT 注销 | 黑名单、版本号、短过期 |
| Session | 服务端保存状态，靠 Session ID（Cookie）识别 |
| Session 共享 | Redis 集中存储是推荐方案 |
| Cookie 安全 | HttpOnly + Secure + SameSite |
| Base64 | 编码方式，非加密 |
| 核心区别 | JWT 无状态、难删除、易扩展；Session 有状态、易删除、需共享 |
| 选型 | 分布式 API 用 JWT，传统 Web 用 Session，可混合 |