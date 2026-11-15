"""
苍穹外卖 AI 点餐助手 —— 命令行入口

运行：python run.py    （输入 exit 退出）
"""

from langgraph.checkpoint.sqlite import SqliteSaver

from app.graph import build_graph


def main():
    print("=" * 50)
    print("苍穹外卖 AI 点餐助手（输入 exit 退出）")
    print("=" * 50)
    config = {"configurable": {"thread_id": "user-4"}}
    with SqliteSaver.from_conn_string("checkpoints.sqlite") as saver:
        app = build_graph(checkpointer=saver)
        while True:
            user_input = input("\n你: ").strip()
            if not user_input:
                continue
            if user_input.lower() in ("exit", "quit", "q"):
                print("再见！")
                break
            result = app.invoke({"messages": [("human", user_input)]}, config)
            print("\n助手:", result["messages"][-1].content)



if __name__ == "__main__":
    main()
