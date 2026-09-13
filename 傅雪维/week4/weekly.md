# Go并发编程周报：并发爬虫改造实践

## 一、学习重点
本周基于经典并发爬虫问题，将串行递归爬虫改造为**并行、去重**的高效爬虫，重点实践了：
- Channel任务队列与工作池模式
- sync.Mutex保护共享访问记录
- goroutine生命周期管理（WaitGroup + close）

## 二、串行代码问题
原代码使用深度递归，逐个抓取URL，存在：
- 串行执行，无法利用多核
- 重复抓取相同URL（如`/pkg/`被多次引用）
- 无退出控制，可能阻塞

## 三、并发改造方案

### 3.1 核心设计
- **任务队列**：缓冲Channel存储待抓取URL及深度
- **Worker池**：多个goroutine并发消费任务
- **去重集合**：用Mutex保护的map记录已访问URL

### 3.2 关键代码片段
```go
type SafeVisited struct {
    mu   sync.Mutex
    data map[string]bool
}

func (v *SafeVisited) Mark(url string) bool {
    v.mu.Lock()
    defer v.mu.Unlock()
    if v.data[url] { return false }
    v.data[url] = true
    return true
}

// worker逻辑
for task := range tasks {
    if task.Depth > maxDepth || !visited.Mark(task.URL) {
        continue
    }
    body, urls, _ := fetcher.Fetch(task.URL)
    // 打印结果，并将子URL入队（depth+1）
    for _, u := range urls {
        tasks <- CrawlTask{u, task.Depth+1}
    }
}
```

### 3.3 并发原语对比
| 组件       | 作用                          |
|------------|-------------------------------|
| Channel    | 任务传递与同步                |
| Mutex      | 保护共享visited map           |
| WaitGroup  | 等待所有worker完成            |

## 四、实践心得
- **去重前置**：在Fetch前标记，减少无效请求，本例中实际抓取次数从约10次降至4次。
- **缓冲队列**：缓解生产消费速度差，提高吞吐。
- **关闭策略**：主goroutine关闭tasks，worker自动退出，配合WaitGroup优雅终止。

通过本次改造，深入理解了Go“通过通信共享内存”的并发哲学，Channel与Mutex各司其职，实现了清晰高效的并发爬虫。

---

> **总结**：任务队列+工作池模式是处理并发IO任务的通用方案，结合去重和深度控制，可轻松扩展至真实爬虫场景。未来可引入Context超时和结果收集Channel进一步优化。
