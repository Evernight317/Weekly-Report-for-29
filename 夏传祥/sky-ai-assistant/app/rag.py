"""
检索层：加载索引 + 向量检索
scripts/search_test.py 和 app/tools_kb.py 都调用这里的 retrieve()，实现只有一份。
"""
import json
from pathlib import Path

from langchain_ollama import OllamaEmbeddings

BASE = Path(__file__).resolve().parent.parent
INDEX = BASE / "data" / "index.json"

MIN_SCORE = 0.55   # 低于此分视为没找到：宁可拒答，也不喂错规则
MAX_K = 4          # 上限；实测发票类问题需要第 4 条才带出 6.1

_embeddings = None
_recs = None


def _ensure_loaded():
    """懒加载：import 时不读 985KB 索引、不构造模型对象，第一次检索才加载。"""
    global _embeddings, _recs
    if _recs is None:
        _recs = json.loads(INDEX.read_text(encoding="utf-8"))
    if _embeddings is None:
        _embeddings = OllamaEmbeddings(model="bge-m3", base_url="http://localhost:11434")
    return _recs, _embeddings


def retrieve(query: str, k: int = MAX_K, min_score: float = MIN_SCORE) -> list[dict]:
    """返回命中条款（按分数降序、已过滤阈值）。
    每条含 score / id / section / text。

    min_score 默认 0.55；诊断时传 0.0 可看完整排名。
    注意：这个参数只给 Python 调用方用，不会暴露给模型。
    """
    recs, embeddings = _ensure_loaded()

    q = embeddings.embed_query(query)              # 查询用 embed_query，入库用 embed_documents
    nq = sum(x * x for x in q) ** 0.5 or 1.0
    q = [x / nq for x in q]                        # 归一化后点积才是余弦，分数才可比较

    scored = [
        {
            "score": sum(a * b for a, b in zip(q, r["vec"])),
            "id": r["id"],
            "section": r["section"],
            "text": r["text"],
        }
        for r in recs
    ]
    scored.sort(key=lambda d: d["score"], reverse=True)

    return [d for d in scored[:k] if d["score"] >= min_score]