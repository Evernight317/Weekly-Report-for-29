# 学习周报：AIC Grounding 工作流重构与 InternVL 支持

**周期**：2026年8月3日 - 2026年8月8日  
**主题**：统一推理框架、Prompt Profile、LocateAnything 与 InternVL backend、批处理与测试完善

---

## 一、本周学习概述

本周在上周环境与任务分析的基础上，集中完成 AIC 多模态目标定位代码的工程化重构。主要成果是建立 `grounding_workflow/` 单目录工作流，将数据分组、prompt 生成、推理缓存、batch 调度、OOM 拆分、断点续跑、raw 审计、bbox 解析和提交校验统一到共享 runner 中；不同模型只通过 backend 适配层接入。

本周先后支持了 Qwen、LocateAnything 和 InternVL3.5-38B-HF，并将 prompt 逻辑从 backend 中抽离为全局 Prompt Profile。这样既能针对不同模型选择默认提示词，又能保持推理主流程稳定，便于后续实验和复现。

---

## 二、核心学习内容

### 2.1 单目录工作流重构

本周将实现收敛到 `grounding_workflow/`，保留一个清晰的推理入口：

```bash
cd grounding_workflow
cp server.env.example server.env
bash run_server.sh check
bash run_server.sh smoke
bash run_server.sh full
```

新的工作流中，`src/inference.py` 是统一入口，负责读取配置并调用共享 runner。runner 负责处理所有与模型无关的流程：

- 读取数据集与查询文件
- 按 image group 分组，减少重复图像处理
- 生成并缓存 prompt
- 批量调用 backend
- 捕获 OOM 并降低 batch size 重试
- 写入 raw audit 记录
- 解析模型输出中的 bbox
- 合并预测并校验 submission

这种结构避免了为每个模型复制一套推理脚本，也降低了后续新增模型时的维护成本。

---

### 2.2 Backend 接口与模型适配

#### 2.2.1 Qwen backend

Qwen backend 继续作为基础视觉语言模型推理路径，主要用于复用已有 Qwen3-VL 经验，并验证统一 runner 的模型无关性。backend 只消费 runner 已经生成的 prompt，不再拼接模型指令。

#### 2.2.2 LocateAnything backend

LocateAnything 接入时重点保留官方 runtime 的特点，同时把模型差异限制在 backend 内部。它依赖 `opencv-python-headless`、`decord` 和 `lmdb`，并需要 Transformers 4.x，避免官方远程模型代码与 Transformers 5.x attention API 不兼容。

双卡运行方面，确认 LocateAnything 不支持在单个官方 runtime 内自动模型并行。因此当配置 `GPU_IDS=0,1` 时，脚本会启动两个独立 worker，每个 worker 绑定一张 GPU，并按图像组分片。worker 内如果遇到 OOM，会自动降低后续 batch size。

#### 2.2.3 InternVL backend

本周新增 `internvl` backend，支持 InternVL3.5-38B-HF。它默认使用 Transformers 的 `device_map=auto`，让单进程看到的多张 GPU 共同承载模型。因此 InternVL 不使用 LocateAnything 的多进程分片方式。

InternVL backend 还加入了同图视觉特征缓存，缓存大小由 `INTERNVL_IMAGE_CACHE_SIZE` 控制。如果 checkpoint 暴露 `get_image_features()`，则优先复用视觉特征；如果接口不兼容，则回退到标准 processor 路径，保证功能可运行。

---

### 2.3 Prompt Profile 与批量 Prompt Provider

本周最重要的结构调整之一，是将 prompt 生成从 backend 中抽离到 `src/grounding/prompts.py`。默认 profile 由 backend 选择，也可以通过全局配置覆盖：

```text
qwen_json | locateanything | internvl_json_union | internvl_native_box | internvl_minimal
```

新的 Prompt Profile 机制解决了两个问题：

1. **模型差异可配置**：不同模型可以使用更适合自身输出习惯的提示词。
2. **主流程不随实验变化**：prompt 实验不会改动 backend 和 runner。

对于复杂 prompt 逻辑，支持通过 `--prompt_provider module:function` 注入自定义函数。若函数标记 `function.__prompt_batch__ = True`，它会一次收到完整的 `tuple[PromptRequest, ...]`，返回 `query_id -> prompt`，避免随推理 batch 重复调用。

---

### 2.4 输出解析、断点与审计

为提高全量推理的稳定性，本周补齐了以下通用能力：

- **bbox 解析兼容**：统一支持严格 JSON 坐标和 InternVL native `<box>` 输出。
- **断点续跑**：已完成 query 不重复推理，失败后可继续。
- **raw audit**：保存模型原始输出、prompt profile、状态字段和解析结果，便于复盘。
- **提交校验**：最终 `predictions.json` 只保留原 query 字段并新增合法 `bbox`。
- **OOM 拆分**：批处理失败时降低 batch size，减少一次异常导致整轮失败的概率。

这些能力使推理工作流从“能跑一次”提升到“能复现、能审计、能恢复”。

---

## 三、本周实践进展

### 3.1 已完成事项

1. **统一入口重构**：确认 `src/inference.py` 为唯一推理入口，脚本统一调用共享 runner。
2. **共享 runner 完善**：实现数据分组、prompt 缓存、batch 调度、断点、raw 审计、bbox 解析和提交校验。
3. **LocateAnything 接入**：完成依赖约束、双卡 worker 分片和 OOM 自动降 batch。
4. **InternVL 接入**：新增 InternVL3.5-38B-HF backend，兼容 JSON 与 `<box>` 输出。
5. **Prompt Profile 抽象**：支持全局 `PROMPT_PROFILE`/`--prompt_profile` 和 batch prompt provider。
6. **测试补充**：新增 grounding tests，覆盖 prompt、bbox、runner 与 backend 关键行为。

### 3.2 关键代码产出

- `grounding_workflow/src/inference.py`：统一推理入口
- `grounding_workflow/src/grounding/runner.py`：共享推理调度与恢复逻辑
- `grounding_workflow/src/grounding/prompts.py`：Prompt Profile 与 Prompt Provider
- `grounding_workflow/src/grounding/backends/qwen.py`：Qwen backend
- `grounding_workflow/src/grounding/backends/locateanything.py`：LocateAnything backend
- `grounding_workflow/src/grounding/backends/internvl.py`：InternVL backend
- `grounding_workflow/tests/test_grounding.py`：核心单元测试

---

## 四、问题与解决方案

### 4.1 Prompt 逻辑散落在各 backend 中

**问题**：早期 prompt 由各模型 backend 自己拼接，导致实验提示词时容易影响模型加载和推理流程。

**解决**：建立全局 Prompt Profile，统一在 `prompts.py` 中生成最终模型输入；backend 只消费 prompt，不再拼接模型指令。

### 4.2 LocateAnything 双卡运行方式与 InternVL 不一致

**问题**：LocateAnything 更适合多进程按图像组分片，而 InternVL 需要单进程多 GPU `device_map=auto`。如果共用同一种双卡策略，会导致资源利用或模型加载出错。

**解决**：将多 GPU 策略放到 backend 和启动脚本层区分：LocateAnything 使用多个 worker；InternVL 使用单进程自动设备映射。

### 4.3 InternVL 输出格式不稳定

**问题**：InternVL 既可能按 JSON 输出，也可能返回 native `<box>` 格式，直接解析容易失败。

**解决**：扩展共享 bbox parser，同时兼容严格 JSON 和 `<box>`；raw audit 保留原始响应，便于定位解析失败原因。

### 4.4 全量推理中断后重复成本高

**问题**：大模型推理时间长，如果中断后从头开始，会浪费大量 GPU 时间。

**解决**：加入断点文件和 query 级状态记录，已完成结果直接复用；batch OOM 后自动降低 batch size 并继续处理。

---

## 五、后续安排

1. 使用统一工作流分别运行 Qwen、LocateAnything 和 InternVL 的 smoke/full 实验，比较结果稳定性。
2. 根据 raw audit 统计失败样本，继续优化 bbox 解析和 prompt profile。
3. 扩充 query-aware prompt 的多目标消歧规则，但保持每条 query 独立，避免引用 prior prediction。
4. 完善可视化检查流程，将预测结果与图像叠加，辅助人工审查错误样本。
5. 在确认最优 backend 后整理最终提交包和运行说明。

---

## 六、总结与思考

本周完成了 AIC 代码从临时实验脚本到可维护工作流的关键转变。统一 runner 让数据、缓存、断点、审计和校验逻辑只实现一次；backend 抽象让 Qwen、LocateAnything 和 InternVL 可以在同一套流程中比较；Prompt Profile 则把提示词实验从模型代码中解耦出来。

这次重构的主要价值不只是支持了更多模型，而是提高了实验复现能力。多模态定位任务的误差来源很多，只有保留 raw 输出、prompt profile、解析状态和提交校验，才能在结果异常时快速判断问题来自模型理解、提示词、解析逻辑还是工程运行状态。后续工作将基于这套统一流程继续做模型对比和提交优化。
