from langchain_core.tools import tool

from app.graph import build_graph
from app.prompts import (KB_AGENT_PROMPT, ORDER_AGENT_PROMPT,
                         SUBAGENT_SUFFIX, SUPERVISOR_PROMPT)
from app.tools_kb import search_rules
from app.tools_order import ALL_TOOLS

_order_app = build_graph(tools=ALL_TOOLS, prompt=ORDER_AGENT_PROMPT + SUBAGENT_SUFFIX)
_kb_app = build_graph(tools=[search_rules], prompt=KB_AGENT_PROMPT + SUBAGENT_SUFFIX)

def _run(app, question: str) -> str:
    result = app.invoke({"messages": [("human", question)]})
    return result["messages"][-1].content

@tool
def ask_order_agent(question: str) -> str:
    """处理订单相关的一切事务：查询历史订单与订单状态、下单、取消订单、
        催单、再来一单。

        用户问到具体订单、要求下单/取消/催单/重复下单时调用。
        question 要写成完整的一句话，把用户的原话和必要上下文都包含进去，
        例如「用户想知道订单 13 现在是什么状态」。

        注意：写操作（下单、取消）必须先在主管层向用户确认，不要替用户做决定。"""
    return _run(_order_app, question)

@tool
def ask_kb_agent(question: str) -> str:
    """查询平台规则：退款与取消规则、配送时效、催单规则、食品安全处理、
      发票、会员优惠券、投诉与客服时效。

      用户询问政策、规定、时限、"能不能/多久/怎么办"这类规则性问题时调用。
      不涉及具体订单状态的查询走这里。

      question 写成完整问句，例如「已接单的订单能否取消」。"""
    return _run(_kb_app, question)

def build_supervisor(checkpointer=None):
    return build_graph(
        checkpointer=checkpointer,
        tools=[ask_order_agent, ask_kb_agent],
        prompt=SUPERVISOR_PROMPT,
    )