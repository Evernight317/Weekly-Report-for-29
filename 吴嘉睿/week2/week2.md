# 学习周报：Agent 编排引擎与系统整合部署

**周期**：2026年7月13日 - 2026年7月18日
**主题**：LangGraph 智能体编排、MCP 多传输层迁移、知识库管理前端落地、Docker 容器化部署

---

## 一、本周学习概述

本周承接上周 MCP 服务器开发与 RAG 原型系统的基础，将学习重心从单点技术验证转向系统整合优化。核心进展包括四大方向：**MCP 服务器从 Stdio 模式迁移至 HTTP/SSE 模式并补全 Resources 与 Prompts 能力**、**基于 LangGraph 实现具备规划-执行-反思闭环的 Agent 编排引擎**、**前端知识库管理页面的功能落地与后端鉴权接入**、**完成 Docker Compose 多服务编排与基础监控体系搭建**。通过本周实践，Agent 网站从可运行的 Demo 演进为可部署的最小可用系统。

---

## 二、核心学习内容

### 2.1 MCP 服务器进阶：多传输层与全能力覆盖

#### 2.1.1 Resources 资源实现

Resources 是 MCP 协议中为 Agent 提供只读数据上下文的核心机制。本周实现了两类典型资源：

**文件系统资源**：
```python
from mcp.server.fastmcp import FastMCP

mcp = FastMCP(name="knowledge-server", version="1.0.0")

@mcp.resource("docs://{filename}")
def read_document(filename: str) -> str:
    """读取知识库中的文档内容，以 URI 模板形式暴露给 Agent"""
    filepath = f"./knowledge_base/{filename}"
    with open(filepath, "r", encoding="utf-8") as f:
        return f.read()
```

**数据库查询资源**：
```python
@mcp.resource("db://table/{table_name}")
def query_table_schema(table_name: str) -> str:
    """将数据库表结构作为资源暴露，Agent 可动态感知数据模型"""
    conn = get_db_connection()
    cursor = conn.execute(f"PRAGMA table_info({table_name})")
    columns = cursor.fetchall()
    return json.dumps([{"name": c[1], "type": c[2]} for c in columns])
```

**关键设计要点**：
- 资源 URI 采用 RESTful 风格模板化设计，支持路径参数动态绑定
- 资源返回为纯文本或 JSON，由 Agent 自行解析和利用
- 资源与工具的职责分离：资源只读、用于上下文注入；工具可写、用于执行操作

#### 2.1.2 Prompts 提示词模板

Prompts 为 Agent 提供预定义的指令框架，确保特定任务执行的一致性与最佳实践遵循：

```python
@mcp.prompt()
def knowledge_qa_prompt(question: str) -> str:
    """知识问答任务的标准化提示词模板"""
    return f"""你是一个专业的知识库助手。请按照以下步骤回答问题：

问题：{question}

步骤：
1. 首先使用 search_knowledge 工具检索相关文档
2. 如果检索结果不足，尝试用不同关键词重新检索
3. 综合多轮检索结果，给出准确、有依据的回答
4. 在回答末尾标注信息来源
"""
```

**Prompts 的应用场景**：
- **任务模板化**：将高频 Agent 任务（QA、摘要、分析）封装为标准 Prompt
- **质量管控**：通过预定义步骤约束 Agent 行为，防止跳过关键流程
- **用户可发现**：客户端 UI 可列出服务器注册的 Prompts，供用户选择激活

#### 2.1.3 Stdio → HTTP/SSE 传输层迁移

**迁移动机**：Stdio 模式仅支持本地单客户端，无法满足多用户 Web 应用场景。HTTP/SSE 模式允许远程多客户端并发连接。

**迁移实现**：
```python
# 原 Stdio 模式
if __name__ == "__main__":
    mcp.run()  # 默认 stdio

# 迁移至 SSE 模式
if __name__ == "__main__":
    mcp.run(transport="sse", host="0.0.0.0", port=8001)
```

**架构变更对比**：

| 维度 | Stdio 模式 | HTTP/SSE 模式 |
|------|-----------|---------------|
| 通信方式 | 子进程 stdin/stdout | HTTP POST + SSE 事件流 |
| 并发能力 | 单客户端 | 多客户端并发 |
| 部署方式 | 与 Client 同机部署 | 独立服务，网络可达即可 |
| 适用场景 | 本地开发、桌面应用 | Web 应用、生产环境 |
| 生命周期 | 随 Client 启停 | 独立常驻进程 |

**SSE 事件流协议**：服务端通过 `text/event-stream` 持续推送工具调用进度、Resources 变更通知等事件，Client 端使用 EventSource API 接收。

---

### 2.2 LangGraph Agent 编排引擎

#### 2.2.1 LangGraph 核心概念

LangGraph 是 LangChain 生态中专门用于构建有状态、多角色 Agent 的图编排框架。其核心抽象为**状态图（StateGraph）**，将 Agent 的推理-行动循环建模为节点与边的有向图。

**三大核心元素**：
- **State（状态）**：贯穿整个 Agent 执行周期的共享数据结构，通常为 TypedDict，包含消息历史、中间结果、上下文信息
- **Nodes（节点）**：执行具体逻辑的函数单元，可以是 LLM 调用、工具执行、条件判断
- **Edges（边）**：定义节点间的流转关系，支持普通边（固定路由）和条件边（动态路由）

#### 2.2.2 规划-执行-反思闭环实现

本周实现了基于 LangGraph 的三阶段 Agent 闭环架构：

```
         ┌──────────────────────────────────┐
         │                                  │
         ▼                                  │
    ┌─────────┐    ┌──────────┐    ┌───────────┐
    │ Planner │───▶│ Executor │───▶│ Reflector │
    │ 规划节点  │    │ 执行节点   │    │ 反思节点   │
    └─────────┘    └──────────┘    └───────────┘
                                             │
                                    信息充足？  │
                                        │      │
                                       否     是
                                        │      │
                                        └──────┘
                                          ▼
                                    输出最终答案
```

**代码实现概要**：
```python
from langgraph.graph import StateGraph, END
from typing import TypedDict, Annotated
import operator

class AgentState(TypedDict):
    messages: Annotated[list, operator.add]
    plan: list[str]
    current_step: int
    knowledge_results: list[str]
    final_answer: str

def planner_node(state: AgentState) -> AgentState:
    """分析用户问题，生成分步执行计划"""
    llm = ChatOpenAI(model="gpt-4o")
    response = llm.invoke([
        SystemMessage(content="将用户问题分解为可执行的步骤列表"),
        HumanMessage(content=state["messages"][-1].content)
    ])
    state["plan"] = parse_steps(response.content)
    state["current_step"] = 0
    return state

def executor_node(state: AgentState) -> AgentState:
    """执行当前步骤，调用工具获取信息"""
    step = state["plan"][state["current_step"]]
    result = execute_tool_call(step)  # 调用 MCP 工具或 RAG 检索
    state["knowledge_results"].append(result)
    state["current_step"] += 1
    return state

def reflector_node(state: AgentState) -> AgentState:
    """评估当前信息是否充足，决定继续执行还是输出答案"""
    llm = ChatOpenAI(model="gpt-4o")
    response = llm.invoke([
        SystemMessage(content="判断当前收集的信息是否足以回答用户问题"),
        HumanMessage(content=json.dumps(state["knowledge_results"]))
    ])
    if "信息充足" in response.content:
        state["final_answer"] = generate_final_answer(state)
    return state

def should_continue(state: AgentState) -> str:
    """条件边：判断是否继续循环"""
    if state["final_answer"]:
        return "end"
    if state["current_step"] < len(state["plan"]):
        return "execute"
    return "end"

# 构建图
graph = StateGraph(AgentState)
graph.add_node("planner", planner_node)
graph.add_node("executor", executor_node)
graph.add_node("reflector", reflector_node)

graph.set_entry_point("planner")
graph.add_edge("planner", "executor")
graph.add_edge("executor", "reflector")
graph.add_conditional_edges("reflector", should_continue, {
    "execute": "executor",
    "end": END
})

app = graph.compile()
```

#### 2.2.3 图执行的可观测性

LangGraph 内置了执行追踪能力，通过 `checkpointer` 机制记录每一步的状态快照：
- **断点恢复**：执行中断后可从中断处继续
- **执行追溯**：完整记录节点的输入/输出，便于调试与审计
- **流式输出**：通过 `astream()` 方法将每个节点的输出实时推送到前端

---

### 2.3 前端知识库管理与后端鉴权

#### 2.3.1 知识库管理页面

基于 Vue3 + Element Plus 实现了知识库管理的前端功能模块：

**核心页面**：

| 页面 | 功能 |
|------|------|
| 文档上传页 | 拖拽上传 PDF/Markdown/TXT，自动触发后端解析与向量化流水线 |
| 分块预览页 | 可视化展示文档分块结果，支持手动调整 chunk 边界与合并 |
| 索引状态面板 | 实时展示向量库文档数、分块数、索引大小，支持重建索引操作 |
| 检索测试页 | 提供检索调试工具，输入 Query 查看 Top-K 召回结果与相似度分数 |

**关键交互设计**：
- 上传进度条采用 SSE 推送，实时反馈文档解析→分块→向量化各阶段进度
- 分块预览使用双栏布局：左侧原文、右侧分块列表，支持点击定位原文位置
- 检索测试展示结果卡片，含相似度分数进度条、匹配片段高亮、来源文档链接

#### 2.3.2 后端鉴权接入

基于 FastAPI 中间件实现了 JWT 鉴权体系：

```python
from fastapi import FastAPI, Depends, HTTPException
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

security = HTTPBearer()

def verify_token(credentials: HTTPAuthorizationCredentials = Depends(security)):
    token = credentials.credentials
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
        return payload["user_id"]
    except jwt.ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token 已过期")
    except jwt.InvalidTokenError:
        raise HTTPException(status_code=401, detail="无效 Token")

app = FastAPI()

@app.get("/api/knowledge/documents")
async def list_documents(user_id: str = Depends(verify_token)):
    # 按用户 ID 隔离数据
    return get_user_documents(user_id)
```

**权限模型**：
- 采用 RBAC（基于角色的访问控制），区分 `admin`、`user`、`viewer` 三种角色
- API 路由通过装饰器声明所需权限等级，中间件统一校验
- 会话 Token 有效期 24 小时，支持 refresh token 自动续期

---

### 2.4 Docker Compose 容器化部署与监控

#### 2.4.1 多服务编排

编写 Docker Compose 配置，实现一键部署全部服务组件：

```yaml
version: "3.8"
services:
  agent-backend:
    build: ./agent_backend
    ports:
      - "8000:8000"
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - DATABASE_URL=postgresql://user:pass@postgres:5432/agent
    depends_on:
      - postgres
      - chroma

  mcp-server:
    build: ./mcp_server
    ports:
      - "8001:8001"
    volumes:
      - ./knowledge_base:/app/knowledge_base
    command: ["python", "server.py", "--transport", "sse", "--port", "8001"]

  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    depends_on:
      - agent-backend

  chroma:
    image: chromadb/chroma:latest
    ports:
      - "8002:8000"
    volumes:
      - chroma_data:/chroma/chroma

  postgres:
    image: postgres:16
    environment:
      POSTGRES_USER: agent
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: agent
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  chroma_data:
  postgres_data:
```

**服务依赖拓扑**：

```
frontend:3000 ──▶ agent-backend:8000 ──▶ mcp-server:8001
                       │                      │
                       ▼                      ▼
                  postgres:5432          chroma:8002
```

#### 2.4.2 监控与日志体系

**日志收集**：采用 Loki + Promtail 收集各容器日志，Grafana 统一可视化查询

**关键监控指标**：
| 指标 | 来源 | 告警阈值 |
|------|------|---------|
| API 请求延迟 P99 | FastAPI middleware | > 2000ms |
| LLM 调用失败率 | Agent 核心层埋点 | > 5% |
| 向量检索延迟 P95 | Chroma 查询日志 | > 500ms |
| 容器内存使用率 | Docker stats | > 80% |
| Token 消耗速率 | LLM 调用计数 | 单日 > 100万 |

**健康检查**：每个服务暴露 `/health` 端点，Docker Compose 通过 `healthcheck` 指令定期探活，异常时自动重启。

---

## 三、本周实践进展

### 3.1 已完成事项

1. **MCP 服务器全能力覆盖**：实现 Resources（文件系统 + 数据库表结构）、Prompts（知识问答模板）、完整的 Stdio→SSE 迁移，支持多客户端并发连接
2. **LangGraph Agent 编排**：完成 Planner-Executor-Reflector 三节点闭环，支持断点恢复与执行追溯，接入 MCP 工具与 RAG 检索形成完整 Agent 能力链
3. **知识库管理前端**：实现文档上传（拖拽 + 进度追踪）、分块预览（双栏可视化）、索引状态监控三个核心页面
4. **后端鉴权体系**：接入 JWT + RBAC 权限模型，API 路由级权限控制，用户数据隔离
5. **Docker Compose 部署**：5 个服务容器的编排配置，健康检查与自动重启，Loki + Grafana 日志监控

### 3.2 关键代码产出

- `mcp_server/`：完整的多能力 MCP 服务器，含 Resources、Tools、Prompts 三大模块及 SSE 传输层
- `agent_graph.py`：LangGraph 三阶段编排图定义，含状态管理、条件路由、checkpointer 持久化
- `frontend/src/views/KnowledgeBase/`：知识库管理页面组件（Upload、ChunkPreview、IndexStatus）
- `backend/auth.py`：JWT 鉴权中间件与 RBAC 权限装饰器
- `docker-compose.yml`：完整多服务编排配置
- `prometheus.yml` + `loki-config.yml`：监控与日志采集配置

---

## 四、问题与解决方案

### 4.1 LangGraph 状态膨胀导致内存溢出
**问题**：多轮 Agent 循环中，State 内 `messages` 列表不断追加，对话超过 20 轮后内存占用超过 2GB，导致容器 OOM。
**解决**：引入滑动窗口策略，State 中仅保留最近 10 轮的消息；早期消息压缩为摘要文本存入 `context_summary` 字段，兼顾上下文连贯性与内存控制。

### 4.2 SSE 传输层断连重试机制缺失
**问题**：MCP 服务器迁移到 SSE 模式后，网络波动导致客户端与服务端连接中断，Agent 处于永久等待状态。
**解决**：客户端实现指数退避重连策略（初始 1s，最大 30s），服务端发送心跳事件（每 15s），客户端超时 30s 未收到心跳即触发重连。

### 4.3 知识库上传大文件超时
**问题**：上传超过 50MB 的 PDF 文件时，后端同步处理（解析→分块→向量化）耗时超过 3 分钟，前端请求超时。
**解决**：改为异步任务队列模式——上传接口立即返回 `task_id`，后端用 Celery 异步处理，前端通过 SSE 订阅任务进度；同时将分块并发度从 1 提升到 4，处理耗时降低 60%。

### 4.4 Docker 容器间网络延迟
**问题**：Agent 后端调用 Chroma 向量检索时，跨容器 DNS 解析偶发超时（>1s），影响用户体验。
**解决**：使用 Docker 自定义 Bridge 网络替代默认网络，并配置 `network_mode: host` 的替代方案——在 Compose 中使用静态 IP 分配与 `/etc/hosts` 注入，将 DNS 解析延迟从偶发 1s 降至稳定的 <5ms。

---

## 五、下周学习计划

1. **Agent 多工具协同与调度优化**：实现工具调用的并行执行与结果聚合，引入优先级队列管理工具调用顺序，降低端到端响应延迟
2. **评估与测试体系**：搭建 Agent 回答质量评估框架（基于 LLM-as-Judge 模式），编写 RAG 检索准确率的自动化测试用例
3. **前端体验优化**：实现对话历史搜索、工具调用链路可视化（LangSmith 风格的时间轴展示）、深色模式支持
4. **CI/CD 流水线**：配置 GitHub Actions 自动化构建、测试与部署，集成 E2E 测试（Playwright），实现合并到 main 分支后自动部署到测试环境
5. **安全加固**：API Rate Limiting、SQL 注入防护审计、敏感信息脱敏（日志中的 API Key 与用户数据）

---

## 六、总结与思考

本周是项目从"能跑"到"能用"的关键转折点。LangGraph 的引入让 Agent 从简单的链式调用升级为具备自主规划与反思能力的状态机，MCP 的多传输层迁移打通了从本地开发到 Web 部署的最后一道技术障碍。Docker 容器化的完成为团队协作与后续迭代提供了标准化的运行环境。

在实践过程中，最深刻的体会是**系统整合的成本远高于单点技术验证**。MCP 协议、LangGraph 编排、RAG 检索三项技术在各自领域内都已相对成熟，但将它们纳入统一的 Agent 运行框架时，接口契约设计、错误传播路径、并发状态一致性等问题会集中暴露。未来学习中，除了继续深入各项技术的进阶用法，更需关注系统级的非功能性需求——可观测性、容错性、性能基线——这些才是决定一个 Agent 系统能否在生产环境长期稳定运行的基石。
