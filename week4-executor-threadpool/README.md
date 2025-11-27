# Week 4 - Executor và ThreadPool

## 📚 Kiến Thức Cần Học
- Executor Framework là gì và tại sao cần dùng
- Executor, ExecutorService, ScheduledExecutorService interfaces
- ThreadPoolExecutor và các loại thread pools
- FixedThreadPool, CachedThreadPool, SingleThreadExecutor
- ScheduledThreadPoolExecutor
- Future và Callable
- CompletionService và ExecutorCompletionService
- Shutdown và graceful shutdown
- RejectedExecutionHandler
- Thread pool sizing và tuning

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `ExecutorDemo` → `ThreadPoolDemo` → `ScheduledExecutorDemo`
2. **Thực hành với Projects**: Hiểu cách dùng Executor trong thực tế
3. **So sánh performance**: Different thread pool types
4. **Practice**: Implement web server và task processor với thread pools

## 🎯 Bài Tập Tuần 4

### Exercise 1: Executor Demo
- [ ] Sử dụng Executor interface
- [ ] So sánh Executor vs Thread.start()
- [ ] Implement ExecutorService
- [ ] Submit tasks và get results

### Exercise 2: ThreadPool Demo
- [ ] Tạo FixedThreadPool
- [ ] Tạo CachedThreadPool
- [ ] Tạo SingleThreadExecutor
- [ ] So sánh performance và behavior
- [ ] Custom ThreadPoolExecutor

### Exercise 3: ScheduledExecutor Demo
- [ ] Schedule tasks với delay
- [ ] Schedule tasks với fixed rate
- [ ] Schedule tasks với fixed delay
- [ ] Cancel scheduled tasks

## 🏆 Mini Projects

### 1. Web Server Simulator
**Mô tả**: Mô phỏng web server với thread pool
- Handle multiple concurrent requests
- Thread pool management
- Request queuing
- Graceful shutdown

**Features**:
- [ ] WebServer class với ThreadPoolExecutor
- [ ] Handle HTTP requests (simulated)
- [ ] Request queue management
- [ ] Statistics tracking
- [ ] Graceful shutdown
- [ ] RejectedExecutionHandler

**API Requirements**:
```java
public class WebServer {
    public WebServer(int poolSize, int queueCapacity);
    public void start();
    public void handleRequest(Request request);
    public void shutdown();
    public ServerStats getStats();
}
```

### 2. Task Processor
**Mô tả**: Batch processing với ExecutorService
- Process multiple tasks concurrently
- Future và Callable
- CompletionService
- Error handling

**Features**:
- [ ] TaskProcessor với ExecutorService
- [ ] Submit Callable tasks
- [ ] Collect results với Future
- [ ] Use CompletionService
- [ ] Handle exceptions
- [ ] Progress tracking

**API Requirements**:
```java
public class TaskProcessor {
    public TaskProcessor(int poolSize);
    public <T> List<Future<T>> submitTasks(List<Callable<T>> tasks);
    public <T> List<T> processTasks(List<Callable<T>> tasks) throws InterruptedException;
    public void shutdown();
}
```

## 📁 File Structure
```
week4-executor-threadpool/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── ExecutorDemo.java
    │   │   ├── ThreadPoolDemo.java
    │   │   └── ScheduledExecutorDemo.java
    │   └── projects/
    │       ├── WebServer.java
    │       └── TaskProcessor.java
    └── test/java/
        └── ExecutorDemoTest.java
```

## 🚀 Cách Chạy

### Sử dụng Maven:
```bash
cd week4-executor-threadpool

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ThreadPoolDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ScheduledExecutorDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.WebServer"
mvn compile exec:java -Dexec.mainClass="projects.TaskProcessor"
```

### Sử dụng batch file (Windows):
```bash
cd week4-executor-threadpool
run_demo.bat
```

## 💡 Key Concepts

### 1. Executor Framework
```java
// Basic Executor
Executor executor = Executors.newFixedThreadPool(5);
executor.execute(() -> System.out.println("Task"));

// ExecutorService
ExecutorService executor = Executors.newFixedThreadPool(5);
Future<String> future = executor.submit(() -> "Result");
String result = future.get();
executor.shutdown();
```

### 2. Thread Pool Types
```java
// Fixed Thread Pool
ExecutorService fixed = Executors.newFixedThreadPool(10);

// Cached Thread Pool
ExecutorService cached = Executors.newCachedThreadPool();

// Single Thread Executor
ExecutorService single = Executors.newSingleThreadExecutor();

// Scheduled Thread Pool
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);
```

### 3. Future và Callable
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
Callable<String> task = () -> {
    Thread.sleep(1000);
    return "Result";
};

Future<String> future = executor.submit(task);
String result = future.get(5, TimeUnit.SECONDS);
```

### 4. CompletionService
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

for (int i = 0; i < 10; i++) {
    completionService.submit(() -> "Task " + i);
}

for (int i = 0; i < 10; i++) {
    Future<String> future = completionService.take();
    String result = future.get();
}
```

## 🚨 Common Pitfalls
1. **Forgetting to shutdown**: Luôn shutdown ExecutorService
2. **Wrong pool size**: Quá nhỏ → chậm, quá lớn → waste resources
3. **Blocking on Future.get()**: Có thể gây deadlock
4. **Not handling exceptions**: Callable có thể throw exception
5. **Unbounded queue**: Có thể gây OutOfMemoryError
6. **Not using CompletionService**: Khó lấy results theo completion order

## 🎯 Tips
- Dùng ExecutorService thay vì tự quản lý threads
- FixedThreadPool cho CPU-bound tasks
- CachedThreadPool cho I/O-bound tasks với short-lived tasks
- Luôn shutdown ExecutorService gracefully
- Sử dụng CompletionService khi cần results theo completion order
- Monitor thread pool metrics (active threads, queue size)
- Tune pool size dựa trên workload characteristics

## 📊 Thread Pool Sizing

| Task Type | Pool Size | Reason |
|-----------|-----------|--------|
| **CPU-bound** | CPU cores | Maximize CPU utilization |
| **I/O-bound** | CPU cores × (1 + wait time / compute time) | Overlap I/O waits |
| **Mixed** | Tune based on profiling | Balance CPU and I/O |

## 🔧 Custom ThreadPoolExecutor
```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5,                          // corePoolSize
    10,                         // maximumPoolSize
    60L,                        // keepAliveTime
    TimeUnit.SECONDS,           // unit
    new LinkedBlockingQueue<>(), // workQueue
    new ThreadFactory() { ... }, // threadFactory
    new RejectedExecutionHandler() { ... } // handler
);
```

