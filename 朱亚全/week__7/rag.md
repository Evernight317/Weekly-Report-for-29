# RAG 初学入门

> RAG（Retrieval-Augmented Generation，检索增强生成）是一种让大语言模型（LLM）能够基于外部知识库回答问题的方法。它通过“先查资料，再回答”的方式，解决了 LLM 知识过时、缺乏私有数据、容易产生幻觉等问题。

---

## 一、为什么需要 RAG？

大语言模型虽然强大，但存在两个核心问题：

1. **知识有截止日期**：训练数据之后发生的事情，模型不知道。
2. **会产生幻觉**：当模型不知道答案时，可能自信地编造错误信息。

RAG 的解决思路很简单：**在回答问题前，先从外部知识库中检索相关信息，把这些信息连同问题一起交给 LLM，让它“看着资料回答”**[citation:1]。

---

## 二、RAG 的核心流程

RAG 系统分为两个阶段：**索引构建（离线）** 和 **检索生成（在线）**。

### 阶段一：索引构建（Indexing）

这个阶段的目标是把你的文档转换成可被快速检索的格式。

```text
加载文档 → 切分（Split）→ 向量化（Embed）→ 存入向量数据库
```

| 步骤 | 做什么 | 说明 |
|------|--------|------|
| **加载（Load）** | 读取文档 | 支持 PDF、TXT、Markdown、HTML、数据库记录等[citation:1] |
| **切分（Split）** | 把长文档切成小块（Chunk） | 因为 LLM 有 Token 长度限制，且整篇文档可能包含无关内容[citation:1][citation:4] |
| **向量化（Embed）** | 将文本块转为向量 | 使用嵌入模型（Embedding Model），语义相近的文本向量距离更近[citation:1] |
| **存储（Store）** | 存入向量数据库 | 如 FAISS、ChromaDB、Milvus 等，支持高效的相似度搜索[citation:1] |

### 阶段二：检索生成（Retrieval & Generation）

这个阶段是用户提问时的实时过程。

```text
用户提问 → 查询向量化 → 向量检索 → 构建增强 Prompt → LLM 生成回答
```

1. **用户提问**：用户输入一个问题。
2. **查询向量化**：用同一个嵌入模型把问题转为向量。
3. **检索（Retrieve）**：在向量数据库中搜索最相似的 N 个文本块（Top-K）[citation:1]。
4. **增强（Augment）**：把检索到的文本块（上下文）和用户问题组合成一个新的 Prompt[citation:2]。
5. **生成（Generate）**：把增强后的 Prompt 发给 LLM，让它基于资料生成回答[citation:1]。

---

## 三、核心概念速查

| 概念 | 说明 |
|------|------|
| **Chunk（文本块）** | 文档切分后的小片段，是检索的基本单位[citation:4] |
| **Embedding（嵌入）** | 把文本映射成高维数字向量的技术，语义相近的文本向量距离更近[citation:3] |
| **向量数据库** | 专门存储和检索向量的数据库，支持相似度搜索[citation:1] |
| **Top-K** | 检索时返回最相似的 K 个结果，K 需要根据场景调整[citation:4] |
| **上下文窗口** | LLM 一次能处理的最大 Token 数量，检索结果不能超过这个限制[citation:8] |

---

## 四、关键设计决策

### 1. 切分策略（Chunking Strategy）

切分方式直接影响检索质量[citation:8]：

| 策略 | 做法 | 适用场景 |
|------|------|----------|
| **固定大小** | 按固定字符数或 Token 数切分 | 内容结构不明确时 |
| **语义切分** | 按段落、标题、句子边界切分 | 结构化文档（法律条款、API 文档） |
| **重叠切分** | 相邻块之间保留部分重叠文本 | 概念可能跨越块边界时 |

### 2. 检索模式

最基础的是 **Top-K 向量检索**。但对于复杂问题，还有更高级的模式[citation:4]：

- **上下文窗口检索**：不仅检索命中的块，还把相邻块也带进来，提供更完整的上下文[citation:8]。
- **混合检索**：结合向量检索（语义）和关键词检索（BM25，精确匹配），用 RRF 等算法融合结果[citation:14]。
- **重排序（Reranking）**：先检索较多候选，再用专门的重排模型对候选进行二次打分，提高精度[citation:11]。

---

## 五、Python 快速上手示例

以下是一个使用 LangChain 构建迷你 RAG 的简化流程[citation:1]：

```python
# 1. 加载文档
from langchain_community.document_loaders import TextLoader
loader = TextLoader("knowledge.txt")
documents = loader.load()

# 2. 切分
from langchain.text_splitter import CharacterTextSplitter
splitter = CharacterTextSplitter(chunk_size=500, chunk_overlap=50)
chunks = splitter.split_documents(documents)

# 3. 向量化并存储
from langchain_openai import OpenAIEmbeddings
from langchain_community.vectorstores import Chroma
embeddings = OpenAIEmbeddings()
vectorstore = Chroma.from_documents(chunks, embeddings)

# 4. 检索
query = "你的问题"
retrieved = vectorstore.similarity_search(query, k=3)

# 5. 构建 Prompt 并生成
from langchain_openai import ChatOpenAI
llm = ChatOpenAI()
context = "\n\n".join([doc.page_content for doc in retrieved])
prompt = f"根据以下资料回答问题：\n{context}\n\n问题：{query}"
answer = llm.invoke(prompt)
```

---

## 六、常见问题与优化方向

| 问题 | 可能原因 | 优化方向 |
|------|----------|----------|
| 检索不到相关内容 | 切分不合理、嵌入模型不匹配 | 调整切分策略、更换嵌入模型 |
| 回答不准确 | 检索结果不相关、Prompt 设计差 | 引入重排序、优化 Prompt 模板 |
| 回答不完整 | Top-K 太小、上下文窗口限制 | 增加 K 值、使用上下文窗口检索 |
| 专有名词检索失败 | 纯向量检索对精确匹配不敏感 | 引入混合检索（BM25 + 向量）[citation:14] |

---

## 七、RAG 的适用场景

RAG 特别适合以下场景[citation:2][citation:12]：

- **企业知识库**：员工查询内部政策、流程文档
- **客服系统**：基于产品文档回答用户问题
- **技术文档问答**：开发者查询 API 文档
- **法律/医疗辅助**：基于专业文献回答问题

**核心判断标准**：数据频繁变化、需要引用来源、涉及私有知识时，RAG 比微调更合适。

---

## 八、小结

| 知识点 | 核心内容 |
|--------|----------|
| RAG 定义 | 检索 + 生成的混合架构，让 LLM 基于外部知识回答 |
| 核心流程 | 索引构建（加载→切分→向量化→存储） + 检索生成（查询→检索→增强→生成） |
| 关键组件 | 嵌入模型、向量数据库、LLM |
| 切分策略 | 固定大小、语义切分、重叠切分 |
| 检索优化 | 上下文窗口、混合检索、重排序 |
| 适用场景 | 私有知识、频繁更新、需要引用的问答场景 |