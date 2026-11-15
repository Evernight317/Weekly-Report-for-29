import sqlite3
import uuid

import gradio as gr
from langgraph.checkpoint.sqlite import SqliteSaver

from app.supervisor import build_supervisor
_conn = sqlite3.connect("checkpoints.sqlite", check_same_thread=False)
_app = build_supervisor(checkpointer=SqliteSaver(_conn))

def respond(user_msg: str, history: list, thread_id: str):
    """一轮对话：返回 (清空输入框, 新的历史)"""
    if not user_msg.strip():
        return "", history

    cfg = {"configurable": {"thread_id": thread_id}, "recursion_limit": 12}
    result = _app.invoke({"messages": [("human", user_msg)]}, cfg)
    answer = result["messages"][-1].content

    # Gradio 6 的 Chatbot 只接受 messages 格式：[{"role": ..., "content": ...}]
    return "", history + [
        {"role": "user", "content": user_msg},
        {"role": "assistant", "content": answer},
    ]

def new_chat():
    """新会话：清空界面 + 换一个全新的 thread_id"""
    return [], str(uuid.uuid4())

with gr.Blocks(title="苍穹外卖 AI 助手") as demo:
    gr.Markdown(
        "## 苍穹外卖 AI 助手\n"
        "可以点餐、查订单、取消/催单，也能解答平台规则（退款、配送、发票、投诉）。"
    )

    # 每个浏览器会话一个随机 thread_id —— 两个人同时打开不会串话
    thread_state = gr.State(str(uuid.uuid4()))
    chatbot = gr.Chatbot(height=520)
    msg_box = gr.Textbox(
        placeholder="说说你想吃什么，或者问平台规则…",
        show_label=False,
    )
    with gr.Row():
        send_btn = gr.Button("发送", variant="primary")
        new_btn = gr.Button("新会话")

    # inputs 顺序必须和 respond 的形参顺序一致
    send_btn.click(respond, [msg_box, chatbot, thread_state], [msg_box, chatbot])
    msg_box.submit(respond, [msg_box, chatbot, thread_state], [msg_box, chatbot])
    new_btn.click(new_chat, None, [chatbot, thread_state])

if __name__ == "__main__":
    # queue(1)：把请求串行化，避免两个人同时说话时 sqlite 报 database is locked。
    # 这是我唯一建议"提前防御"的地方 —— 它爆的时机恰好是别人点开你链接的那一刻。
    demo.queue(default_concurrency_limit=1).launch(
        server_name="127.0.0.1", server_port=7860
    )