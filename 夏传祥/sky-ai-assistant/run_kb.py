from langgraph.checkpoint.sqlite import SqliteSaver

from app.graph import build_graph
from app.prompts import KB_AGENT_PROMPT
from app.tools_kb import search_rules


def main():
    print("=" * 50)
    print("苍穹外卖 AI 客服助手（输入 exit 退出）")
    print("=" * 50)

    # 和订单 Agent 共用同一个 sqlite 文件，但 thread_id 不同 → 两套记忆互不干扰
    config = {"configurable": {"thread_id": "kb-1"}}

    with SqliteSaver.from_conn_string("checkpoints.sqlite") as saver:
        app = build_graph(checkpointer=saver, tools=[search_rules], prompt=KB_AGENT_PROMPT)
        while True:
            user_input = input("\n你: ").strip()
            if not user_input:
                continue
            if user_input.lower() in ("exit", "quit", "q"):
                print("再见！")
                break
            result = app.invoke({"messages": [("human", user_input)]}, config)
            print("\n客服:", result["messages"][-1].content)


if __name__ == "__main__":
    main()