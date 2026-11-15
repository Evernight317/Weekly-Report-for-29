from langchain_core.tools import tool

from app.rag import retrieve

@tool
def search_rules(query: str) -> str:
    """查询苍穹外卖平台规则，涵盖退款与取消、配送时效、催单、食品安全、
    发票、会员优惠券、投诉与客服时效。

    用户询问平台的规则、规定、政策、时限、能否退款、怎么开发票等问题时调用。
    只能依据本工具返回的条款回答，不要用常识补充。
    query 用用户关心的关键词，例如「退款时间」「能否取消订单」「发票」。

    返回若干条带编号的条款，回答时请标注条款编号，如 [3.2]。"""
    hits = retrieve(query)

    if not hits:
        # 关键：明确说"没找到"，而不是返回空字符串。
        # 空串会让模型倾向于自己编，这是 RAG 幻觉的主要来源之一。
        return "知识库中没有找到与该问题相关的规则条款。"

    # 只给编号和正文，不给相似度分数：模型看不懂分数，只会干扰措辞
    lines = [f"[{h['id']}] {h['text']}" for h in hits]
    return "命中规则：\n" + "\n".join(lines)