"""检索层诊断：打印分数，不经过大模型。用法：python scripts/search_test.py"""
import sys
from pathlib import Path

# 脚本在 scripts/ 下运行时，sys.path[0] 是 scripts/，找不到 app 包，
# 所以要把项目根插进来。另一种做法是 python -m scripts.search_test，但需要 __init__.py。
sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from app.rag import retrieve

CASES = [
    "退款多久到账",
    "什么情况下不能催单",
    "发票怎么开",
    "少送了菜怎么办",
    "超时有补偿吗",
    "今天天气怎么样",
    "你是谁",
    "帮我写个冒泡排序",
]

for q in CASES:
    print(f"\n=== {q} ===")
    # min_score=0.0：诊断要看完整排名，不受阈值影响
    hits = retrieve(q, k=4, min_score=0.0)
    for h in hits:
        flag = "" if h["score"] >= 0.55 else "  ←低于阈值"
        print(f"  {h['score']:.3f}  [{h['id']:>3}]  {h['text'][:46]}{flag}")