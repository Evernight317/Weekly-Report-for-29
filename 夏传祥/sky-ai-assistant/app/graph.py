"""
苍穹外卖 AI 点餐助手 —— LangGraph 图定义

图结构（LangGraph 标准 Agent 循环）：

    START → agent(大模型决策) → should_continue?
                ↑                   ├─ 需要调工具 → tools(执行工具) ─┐
                └───────────────────────────────────────────────────┘
                                    └─ 不需要 → END


本文件只负责"图"，命令行交互在根目录 run.py。
"""
import os
from typing import Annotated, List, TypedDict

from langchain.chat_models import init_chat_model
from langchain_core.messages import AIMessage, SystemMessage, ToolMessage
from langgraph.graph import END, START, StateGraph
from langgraph.graph.message import add_messages

from app.prompts import ORDER_AGENT_PROMPT
from app.tools_order import ALL_TOOLS


# ========== 1. 定义状态 ==========
class AgentState(TypedDict):
    messages: Annotated[List, add_messages]


# ========== 2. 定义大模型并绑定工具 ==========
llm = init_chat_model(
    model="deepseek-flash",
    model_provider="openai",
    api_key=os.getenv("DEEPSEEK_API_KEY"),
    base_url=os.getenv("DEEPSEEK_url"),
)

def build_graph(checkpointer=None, tools=None, prompt=None):
    """组装图。
    tools / prompt 不传就用订单 Agent 的默认值，传了就用传的（客服 Agent 靠这个复用）。
    """
    tools = ALL_TOOLS if tools is None else tools
    system_prompt = ORDER_AGENT_PROMPT if prompt is None else prompt

    # 把"绑定工具"和"节点定义"都挪进函数内部：
    # 不同 Agent 的工具体不同，节点必须闭包住自己那份，不能共用模块级变量
    llm_with_tools = llm.bind_tools(tools)
    tools_by_name = {t.name: t for t in tools}

    def agent_node(state: AgentState):
        messages = [SystemMessage(content=system_prompt)] + state["messages"]
        return {"messages": [llm_with_tools.invoke(messages)]}

    def tools_node(state: AgentState):
        last = state["messages"][-1]
        results = []
        for call in last.tool_calls:
            tool = tools_by_name.get(call["name"])
            if tool is None:
                content = f"未知工具: {call['name']}"
            else:
                print(f"  [调用工具] {call['name']}({call['args']})")
                content = tool.invoke(call["args"])
            results.append(ToolMessage(content=str(content), tool_call_id=call["id"]))
        return {"messages": results}

    def should_continue(state: AgentState):
        return "tools" if state["messages"][-1].tool_calls else END

    graph = StateGraph(AgentState)
    graph.add_node("agent", agent_node)
    graph.add_node("tools", tools_node)
    graph.add_edge(START, "agent")
    graph.add_conditional_edges("agent", should_continue, ["tools", END])
    graph.add_edge("tools", "agent")
    app=graph.compile(checkpointer=checkpointer)
    return app
