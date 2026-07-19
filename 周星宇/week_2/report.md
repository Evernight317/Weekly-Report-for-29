# HTML, CSS \& 数据库 学习周报 (第二周)

**报告周期**：2026年07月13日 —— 2026年07月19日  
**学习人**：学员同学  
**当前阶段**：前端核心布局进阶 与 后端数据库(MySQL)基础入门

本周学习时间投入

|日期|学习时长 (小时)|核心学习内容|状态评估|
|-|:-:|-|:-:|
|07月13日 (周一)|3.0|CSS 盒模型（标准盒模型与怪异盒模型）深度解析|良好|
|07月14日 (周二)|3.5|Flexbox 弹性盒子布局与一维页面构建| 极佳|
|07月15日 (周三)|3.0|HTML5 表单高级验证（H5自带属性与正则表达式简介）|良好|
|07月16日 (周四)|4.0|数据库概念入门、MySQL 安装与 SQL 基础 (DDL)|概念较多|
|07月17日 (周五)|3.5|SQL 核心增删改查 (DML/DQL) 实战演练|良好|
|07月18日 (周六)|4.0|综合融会贯通：设计“用户注册表单”对应的后台数据库表|极佳|
|07月19日 (周日)|2.0|梳理错题、规范 SQL 与 CSS 代码、撰写本周周报|🟢 良好|
|**总计**|**23.0**|**跨越前端与数据库，顺利迈出全栈第一步**|**总体：优秀**|

\---

## 核心知识点梳理与总结

### 一、 前端知识略微延伸 (CSS进阶与表单扩展)

基于第一周学过的 HTML 基础表单和 CSS 引入/基础选择器，本周向网页布局核心和前端数据校验进行了延伸：

#### 1\. CSS 盒模型 (Box Model)

* **标准盒模型** (`box-sizing: content-box`): 给盒子加 `padding` 或 `border` 会撑大盒子。
* **怪异盒模型** (`box-sizing: border-box`): 设定的 `width` 即为盒子最终总宽度，内边距和边框会自动向内挤压。*在写复杂表单和布局时，强烈推荐使用 `border-box`。*

#### 2\. Flexbox 弹性盒子布局

取代了传统的浮动布局，专门用于解决表单对齐、导航栏分布等一维排版问题：

* `display: flex;` 开启弹性上下文。
* `justify-content: center / space-between;` 轻松控制子元素的水平对齐与间距。
* `align-items: center;` 完美解决表单提示文字与输入框的**垂直居中**难题。

#### 3\. HTML5 表单前端验证

结合上周学的 `<input>`，延伸学习了不用写 JS 就能实现的表单验证：

* `required`: 不能为空。
* `pattern="^\[a-zA-Z0-9]{6,12}$"`: 用正则表达式直接限制输入框（例如限制密码为6-12位数字或字母）。

\---

### 二、 数据库基础知识 (MySQL)

为了配合前端表单收集的数据落地存储，本周全新开启了关系型数据库的学习。

#### 1\. 核心概念

* **数据库 (Database)**、**表 (Table)**、**行 (Row/记录)**、**列 (Column/字段)**。
* **主键 (Primary Key)**: 唯一标识一条记录，通常设置为自增（`AUTO\_INCREMENT`）。

#### 2\. SQL 语句实战 (CRUD)

* **创建表 (Create Table)**：

&#x20;   ```sql
    CREATE TABLE users (
        id INT PRIMARY KEY AUTO\_INCREMENT,
        username VARCHAR(50) NOT NULL,
        password VARCHAR(100) NOT NULL,
        email VARCHAR(50),
        reg\_time TIMESTAMP DEFAULT CURRENT\_TIMESTAMP
    );
    ```

* **增 (Insert)**：对应前端用户注册提交的数据。

&#x20;   ```sql
    INSERT INTO users (username, password, email) VALUES ('张三', '123456', 'zhangsan@qq.com');
    ```

* **删 (Delete)**：

&#x20;   ```sql
    DELETE FROM users WHERE id = 1;
    ```

* **改 (Update)**：对应用户修改资料。

&#x20;   ```sql
    UPDATE users SET password = 'newpassword' WHERE username = '张三';
    ```

* **查 (Select)**：对应登录时检验身份。

&#x20;   ```sql
    SELECT \* FROM users WHERE username = '张三';
    ```

\---

## 🔗 前后端协同与逻辑贯通（本周最大收获）

本周将两部分内容进行了逻辑串联，理清了用户数据在网页上的流转闭环：

1. **用户在前台**通过 HTML+CSS 渲染出的精美表单输入数据。
2. **HTML5 验证**在第一关拦截掉格式错误的非合规数据。
3. 表单通过路径提交到后台，最终由后端转化为一条 `INSERT INTO users ...` 的 **SQL 语句** 插入到 MySQL 数据库中，完成数据的永久保存。

\---

## 遇到的难题与解决办法

1. **问题：前端表单控件宽度在手机端溢出**

   * *原因*：在标准盒模型下给输入框设置了 `width: 100%`，又加了 `padding` 导致超出了父容器。
   * *解决*：为所有的表单输入标签加上 `box-sizing: border-box;` 成功修复。
2. **问题：执行 SQL 插入中文时报错 `Incorrect string value`**

   * *原因*：数据库或表默认创建成了 `latin1` 字符集，不支持中文。
   * *解决*：建表时显式指定字符集为 `utf8mb4`：`CREATE TABLE ... CHARSET=utf8mb4;`。

\---

## 下周学习计划

* \[ ] **CSS 响应式与媒体查询**：让表单和布局完美适配手机、平板与电脑各种屏幕。
* \[ ] **SQL 多表查询 (Join)**：学习如何设计用户表与用户权限表、订单表的多表关联查询。
* \[ ] **初识动态后端语言 (如 Node.js 或 PHP)**：编写最简单的服务器代码，真正把 HTML 表单和 MySQL 数据库连接起来。

