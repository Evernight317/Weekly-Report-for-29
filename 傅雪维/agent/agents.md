# AI Agent 学习笔记 

## 0. 一句话理解 Agent

**AI Agent = 以 LLM 为推理核心的自主软件程序。**

它能感知环境、收集信息、自主决策并执行任务，具备：

- 自主性
- 交互能力
- 学习能力
- 多模态能力

更工程化地看：

> **Agent = LLM 推理核心 + Memory + Plan + Tools + Action，通过 ReAct 循环与环境交互。**

---

## 1. Agent 四大核心模块

| 模块 | 作用 | 说明 |
|---|---|---|
| Memory 记忆 | 存储历史信息、对话、观测结果 | 分为短时记忆 / 长时记忆 |
| Plan 规划 | 拆解目标，制定分步执行计划 | 决定“先做什么、再做什么” |
| Tools 工具 | 调用外部能力 | 如搜索、数据库、pandas 等，弥补 LLM 知识与能力局限 |
| Action 行动 | 执行动作，获取环境反馈 | 让 Agent 从“想”走向“做” |

---

## 2. 基础能力：提示工程 Prompt Engineering

**定义：**  
通过优化 Prompt、调整生成策略，提升大模型输出质量的工程方法。

**关键原则：**

- 描述清晰
- 需求具体
- 减少模型猜测
- 持续调优提示词，直到获得预期结果

**定位：**  
提示工程是构建 Agent 的基础能力。Agent 的规划、工具调用、反思等环节，都依赖良好的 Prompt 控制。

---

## 3. Agent 运行范式：从 CoT 到 ReAct

### 3.1 三类范式对比

| 范式 | 特点 | 局限 |
|---|---|---|
| Reason Only | 仅内部推理，如 CoT 思维链 | 不与外部环境交互 |
| Act Only | 直接执行动作，如 WebGPT | 缺少显式推理 |
| ReAct | 推理 + 行动交替循环 | Agent 最主流框架 |

### 3.2 ReAct 核心循环

**ReAct = Reason + Act，思考 + 行动。**

流程：

```text
Reasoning 推理
   ↓
Action 行动
   ↓
Environment 环境
   ↓
Observation 观测反馈
   ↓
继续 Reasoning 推理
   ↓
循环迭代，直到完成任务
```

特点：

- 模型先思考
- 再执行动作
- 接收环境反馈
- 根据反馈继续推理和行动

这是当前 Agent 最主流的运行框架。

---

## 4. 工程实现：用 Function Calling 构建 Action Agent

以 **NL2SQL** 为例：用户用自然语言提问，Agent 生成 SQL 并查询数据库。

### 4.1 基础流程

1. 构建消息列表  
   - `system`：系统角色指令  
   - `user`：用户问题

2. 请求大模型  
   模型判断是否需要调用工具。

3. 解析模型返回的 `function_call`  
   执行对应工具，例如生成 SQL 并查询数据库。

4. 将工具执行结果放回消息队列  
   继续交给大模型处理。

5. 循环执行  
   直到模型输出最终答案。

### 4.2 伪代码结构

```python
messages = [
    {"role": "system", "content": "你是 SQL 助手..."},
    {"role": "user", "content": "查询..."}
]

while True:
    response = llm(messages, tools)

    if response.function_call:
        result = execute_tool(response.function_call)

        messages.append(response)
        messages.append({
            "role": "tool",
            "content": result
        })
    else:
        return response.content
```

核心思想：

> **LLM 负责决策，工具负责执行，消息队列负责保存上下文，循环负责推进任务。**

---

## 5. 案例：Jarvis Agent

Jarvis Agent 通过结构化 JSON 输出，实现 **思考 - 行动分离**。

### 5.1 输出结构示例

```json
{
  "thought": "我需要先搜索相关信息，再决定下一步",
  "command": "search",
  "args": {
    "query": "..."
  }
}
```

### 5.2 工作流程

```text
Agent 内部生成 thought 思考 / 自我反思
        ↓
选择 command 命令
        ↓
调用工具执行
        ↓
获取观测结果
        ↓
进入下一轮 thought
        ↓
直到任务完成，使用 finish 输出最终结果
```

### 5.3 支持命令

| 命令 | 作用 |
|---|---|
| `search` | 搜索 |
| `write_file` | 写文件 |
| `read_file` | 读文件 |
| `finish` | 完成任务并输出最终结果 |

关键点：

- 用 JSON 结构化输出，让“思考”和“行动”分离
- Agent 先思考、反思，再选择命令
- 工具执行后继续循环
- 任务完成时用 `finish` 终止并输出结果

---

## 6. 总结：Agent 知识主线

```text
LLM 推理核心
   ↓
Prompt Engineering 控制模型行为
   ↓
Memory / Plan / Tools / Action 扩展能力
   ↓
ReAct 循环：Reason → Act → Observation
   ↓
Function Calling / JSON 结构化输出落地
   ↓
NL2SQL、Jarvis 等 Agent 案例
```

一句话记忆：

> **LLM 是大脑，Prompt 是接口，Memory 是上下文，Plan 是策略，Tools 是手脚，ReAct 是工作流，Action 是结果。**
