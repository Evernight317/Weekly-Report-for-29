import json
import re
import sys
import time
from pathlib import Path

from langchain_community.document_loaders import UnstructuredMarkdownLoader
from langchain_core.documents import Document
from langchain_ollama import OllamaEmbeddings

# 用 __file__ 推出项目根
BASE = Path(__file__).resolve().parent.parent
CORPUS = BASE / "knowledge" / "苍穹外卖平台规则.md"
INDEX = BASE / "data" / "index.json"

# 章节标题：只认 "一、" "二、" 这种，排除文档大标题「苍穹外卖平台规则」
SECTION_RE = re.compile(r"^[一二三四五六七八九十]+、")
# 条款编号，如 "3.2"
CLAUSE_RE = re.compile(r"^(\d+\.\d+)\s+(.+)$")
# 关键：unstructured 把正文拼成了一行（换行变空格），
# 所以只能靠「编号 + 空格」这个特征在文本内部找切点。
# (?=...) 是前瞻，只匹配位置、不消耗字符，切完每段仍带自己的编号。
CLAUSE_SPLIT = re.compile(r"(?=\d+\.\d+\s)")

def split_to_clauses() -> list[Document]:
    """18 个元素 → 44 条条款 Document。

        元素顺序就是文档顺序，依赖它来累积 section，不能排序。
        """
    loader = UnstructuredMarkdownLoader(
        file_path=str(CORPUS),
        mode="elements",  # 按标题/段落等元素切分（相比 single 更细）
    )
    docs = loader.load()

    chunks: list[Document] = []
    section = ""  # 当前章节名，遇到章节标题时更新
    dropped = 0  # 被丢弃的片段数，用来发现"静默丢数据"
    for doc in docs:
        text = doc.page_content.strip()
        category = doc.metadata.get("category")

        # 标题：只接受 "X、xxx" 形式的章节标题
        # 第一个 Title 是文档大标题，不能当章节名
        if category == "Title":
            if SECTION_RE.match(text):
                section = text
            continue

        # 门禁：还没进入任何章节之前的内容（文件头的说明块）一律跳过。
        # 不靠"恰好切不出来"来过滤，而是显式判断，意图更清楚。
        if not section:
            continue

        # 正文元素可能是 NarrativeText，也可能被 unstructured 判成
        # UncategorizedText（七、八章就是这样），所以只排除 Title，不白名单
        for part in CLAUSE_SPLIT.split(text):
            part = part.strip()
            if not part:  # 前瞻切分的第一个片段通常是空的
                continue
            m = CLAUSE_RE.match(part)
            if not m:
                dropped += 1  # 例如文件头里"（如 3.2）"这类碎片
                continue
            cid, content = m.group(1), m.group(2)
            chunks.append(Document(
                # 拼上章节名：3.8 说"按第五章处理"，脱离章节语义会变弱；
                # 同时章节名本身就是引用出处的一部分。
                page_content=f"{section} / {cid} {content}",
                metadata={"id": cid, "section": section},
            ))
    print(f"元素数: {len(docs)}  条款数: {len(chunks)}  丢弃片段: {dropped}")
    return chunks

def build_index(chunks: list[Document]) -> None:
    """批量向量化 → 归一化 → 写 data/index.json"""
    embeddings = OllamaEmbeddings(model="bge-m3", base_url="http://localhost:11434")

    texts = [c.page_content for c in chunks]
    t0 = time.time()
    # embed_documents 一次发全部：实测比逐条快约 19 倍
    vectors = embeddings.embed_documents(texts)
    cost = time.time() - t0

    records = []
    for c, v in zip(chunks, vectors):
        # 归一化后，检索时「点积 == 余弦」，省掉每次查询的两次开方
        norm = sum(x * x for x in v) ** 0.5 or 1.0
        records.append({
            "id": c.metadata["id"],
            "section": c.metadata["section"],
            "text": c.page_content,
            "vec": [x / norm for x in v],
        })

    INDEX.parent.mkdir(parents=True, exist_ok=True)
    INDEX.write_text(json.dumps(records, ensure_ascii=False), encoding="utf-8")
    print(f"维度: {len(vectors[0])}  耗时: {cost:.1f}s  "
          f"写入 {INDEX.relative_to(BASE)}（{INDEX.stat().st_size / 1024:.0f} KB）")

def main():
    chunks = split_to_clauses()

    # 抽查前三条，确认章节名和编号都对
    for c in chunks[:3]:
        print(f"  {c.metadata['id']:>4}  {c.page_content[:60]}")

    if "--chunks-only" in sys.argv:
            return

    build_index(chunks)

if __name__ == "__main__":
    main()


