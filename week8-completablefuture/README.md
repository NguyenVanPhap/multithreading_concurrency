# Week 8 - CompletableFuture

## 📚 Kiến Thức Cần Học
- CompletableFuture là gì và tại sao cần dùng
- Creating CompletableFuture
- Chaining operations: thenApply, thenCompose, thenCombine
- Combining multiple futures: allOf, anyOf
- Exception handling: handle, exceptionally
- Async operations
- Timeout và cancellation
- Best practices

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `CompletableFutureDemo` → `ChainingDemo` → `CombiningDemo`
2. **Thực hành với Projects**: Async API calls, Pipeline processing
3. **So sánh**: CompletableFuture vs Future vs Callback

## 🎯 Bài Tập Tuần 8

### Exercise 1: CompletableFuture Basics
- [ ] Creating CompletableFuture
- [ ] Getting results
- [ ] Async operations

### Exercise 2: Chaining Operations
- [ ] thenApply, thenAccept, thenRun
- [ ] thenCompose (flatMap)
- [ ] thenCombine

### Exercise 3: Combining Futures
- [ ] allOf
- [ ] anyOf
- [ ] Exception handling

## 🏆 Mini Projects

### 1. Async API Client
**Mô tả**: Simulate async API calls
- Multiple API calls
- Combine results
- Error handling

### 2. Pipeline Processor
**Mô tả**: Data processing pipeline
- Sequential stages
- Parallel processing
- Result aggregation

## 📁 File Structure
```
week8-completablefuture/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── CompletableFutureDemo.java
    │   │   ├── ChainingDemo.java
    │   │   └── CombiningDemo.java
    │   └── projects/
    │       ├── AsyncAPIClient.java
    │       └── PipelineProcessor.java
    └── test/java/
```

## 💡 Key Concepts

### 1. Creating CompletableFuture
```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Result");
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {});
```

### 2. Chaining
```java
future.thenApply(s -> s.toUpperCase())
      .thenAccept(System.out::println)
      .thenRun(() -> System.out.println("Done"));
```

### 3. Combining
```java
CompletableFuture.allOf(f1, f2, f3).join();
CompletableFuture.anyOf(f1, f2, f3).join();
```

## 🚨 Common Pitfalls
1. **Blocking on get()**: Dùng join() hoặc chaining
2. **Not handling exceptions**: Dùng handle() hoặc exceptionally()
3. **Wrong executor**: Default uses ForkJoinPool

## 🎯 Tips
- Use supplyAsync/runAsync cho async operations
- Chain operations thay vì blocking
- Handle exceptions properly
- Use custom executor khi cần

