# Week 13 - ExecutorService + CompletableFuture (Advanced)

## 📚 Mô Tả

Project nâng cao kết hợp **ExecutorService** và **CompletableFuture** để xây dựng hệ thống xử lý bất đồng bộ phức tạp.

## 🎯 Mục Tiêu Học Tập

- Kết hợp ExecutorService với CompletableFuture
- Xử lý async pipeline phức tạp
- Error handling và retry mechanisms
- Performance optimization với custom thread pools
- Real-world async service orchestration

## 🏆 Projects

### 1. Async Service Orchestrator (Khó)
**Mô tả**: Hệ thống điều phối nhiều service bất đồng bộ
- Gọi nhiều microservices song song
- Kết hợp kết quả từ nhiều nguồn
- Retry logic với exponential backoff
- Circuit breaker pattern
- Timeout handling

**Features**:
- [ ] Custom ExecutorService cho different service types
- [ ] CompletableFuture chains với thenCombine, allOf
- [ ] Retry mechanism với exponential backoff
- [ ] Circuit breaker để tránh cascade failures
- [ ] Timeout cho từng service call
- [ ] Error aggregation và reporting
- [ ] Performance metrics tracking

### 2. Data Pipeline Processor (Khó)
**Mô tả**: Pipeline xử lý dữ liệu nhiều giai đoạn
- Stage 1: Data fetching (parallel)
- Stage 2: Data transformation (parallel với dependencies)
- Stage 3: Data validation
- Stage 4: Data aggregation
- Stage 5: Result storage

**Features**:
- [ ] Multi-stage pipeline với CompletableFuture
- [ ] Parallel processing trong mỗi stage
- [ ] Dependency management giữa stages
- [ ] Error recovery và partial results
- [ ] Progress tracking
- [ ] Resource cleanup

## 📁 File Structure
```
week13-executor-completablefuture/
├── README.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── ExecutorCompletableFutureDemo.java
    │   │   └── PipelineDemo.java
    │   └── projects/
    │       ├── AsyncServiceOrchestrator.java
    │       └── DataPipelineProcessor.java
    └── test/java/
```

## 🚀 Cách Chạy

```bash
cd week13-executor-completablefuture

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorCompletableFutureDemo"
mvn compile exec:java -Dexec.mainClass="exercises.PipelineDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.AsyncServiceOrchestrator"
mvn compile exec:java -Dexec.mainClass="projects.DataPipelineProcessor"
```

## 💡 Key Concepts

### 1. Custom ExecutorService với CompletableFuture
```java
ExecutorService ioExecutor = Executors.newFixedThreadPool(10);
ExecutorService cpuExecutor = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);

CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchData(), ioExecutor)
    .thenApplyAsync(data -> processData(data), cpuExecutor);
```

### 2. Service Orchestration
```java
CompletableFuture<Result1> f1 = CompletableFuture
    .supplyAsync(() -> callService1(), executor);
CompletableFuture<Result2> f2 = CompletableFuture
    .supplyAsync(() -> callService2(), executor);

CompletableFuture<CombinedResult> combined = f1.thenCombine(
    f2, (r1, r2) -> combine(r1, r2)
);
```

### 3. Error Handling với Retry
```java
CompletableFuture<String> withRetry = CompletableFuture
    .supplyAsync(() -> riskyOperation())
    .handle((result, throwable) -> {
        if (throwable != null) {
            return retryOperation();
        }
        return result;
    });
```

## 🚨 Common Pitfalls
1. **Blocking on get()**: Dùng join() hoặc chaining
2. **Wrong executor**: Chọn executor phù hợp với task type
3. **Not handling exceptions**: Luôn handle exceptions trong chains
4. **Resource leaks**: Shutdown executor properly
5. **Timeout issues**: Set timeout cho mọi async operation

