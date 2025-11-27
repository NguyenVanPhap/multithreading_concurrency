# Learning Guide - Week 4: Executor và ThreadPool

## 🎯 Mục Tiêu Học Tập

Sau khi hoàn thành tuần này, bạn sẽ:
- Hiểu được Executor Framework và tại sao cần dùng
- Biết cách sử dụng các loại thread pools khác nhau
- Nắm vững Future và Callable
- Sử dụng được CompletionService
- Implement được graceful shutdown
- Tune thread pool size dựa trên workload

## 📖 Lý Thuyết Cần Nắm

### 1. Tại sao cần Executor Framework?

**Vấn đề với Thread.start():**
- Khó quản lý nhiều threads
- Overhead tạo/destroy threads
- Không có thread reuse
- Không có queue management
- Khó handle exceptions

**Giải pháp - Executor Framework:**
- Thread pool management
- Task queuing
- Thread reuse
- Better exception handling
- Lifecycle management

### 2. Executor Hierarchy

```
Executor (interface)
  └── ExecutorService (interface)
       └── ScheduledExecutorService (interface)
            └── ThreadPoolExecutor (class)
                 └── ScheduledThreadPoolExecutor (class)
```

### 3. Thread Pool Types

#### a) FixedThreadPool
- **Cấu hình**: Fixed threads, unbounded queue
- **Use case**: CPU-bound tasks, predictable workload
- **Pros**: Stable, predictable
- **Cons**: May queue too many tasks

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
```

#### b) CachedThreadPool
- **Cấu hình**: 0 to Integer.MAX_VALUE threads, SynchronousQueue
- **Use case**: Short-lived, I/O-bound tasks
- **Pros**: Auto-scaling, no queue
- **Cons**: May create too many threads

```java
ExecutorService executor = Executors.newCachedThreadPool();
```

#### c) SingleThreadExecutor
- **Cấu hình**: 1 thread, unbounded queue
- **Use case**: Sequential processing, task ordering
- **Pros**: Guaranteed ordering
- **Cons**: No parallelism

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

#### d) ScheduledThreadPool
- **Cấu hình**: Fixed threads, DelayedWorkQueue
- **Use case**: Scheduled/recurring tasks
- **Pros**: Scheduling capabilities
- **Cons**: Limited to scheduled tasks

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
```

### 4. Future và Callable

**Runnable vs Callable:**
- Runnable: void run(), no return value, no exception
- Callable: V call() throws Exception, return value, can throw exception

**Future:**
- Represents result of async computation
- Methods: get(), get(timeout), isDone(), cancel(), isCancelled()

```java
Future<String> future = executor.submit(() -> "Result");
String result = future.get(); // Blocking
```

### 5. CompletionService

**Vấn đề với Future:**
- get() returns in submission order, not completion order
- Hard to process results as they complete

**Giải pháp - CompletionService:**
- Get results in completion order
- take() blocks until next completed task

```java
CompletionService<String> cs = new ExecutorCompletionService<>(executor);
cs.submit(task1);
cs.submit(task2);
Future<String> first = cs.take(); // First completed
```

## 🏃 Hướng Dẫn Thực Hành

### Bước 1: ExecutorDemo.java

**Mục tiêu**: Hiểu Executor Framework cơ bản

1. **testBasicExecutor()**:
   - TODO: Tạo Executor với FixedThreadPool
   - TODO: Execute tasks với executor.execute()
   - TODO: Shutdown executor
   - So sánh với Thread.start()

2. **testExecutorServiceRunnable()**:
   - TODO: Submit Runnable tasks
   - TODO: Get Future và wait for completion
   - Quan sát thread reuse

3. **testExecutorServiceCallable()**:
   - TODO: Submit Callable tasks
   - TODO: Get results với Future.get()
   - TODO: Handle exceptions

4. **testInvokeAll()**:
   - TODO: Submit multiple Callables
   - TODO: Wait for all to complete
   - Process results

5. **testInvokeAny()**:
   - TODO: Submit multiple Callables
   - TODO: Get first completed result
   - Other tasks are cancelled

**Tips**:
- Luôn shutdown ExecutorService
- Use awaitTermination() for graceful shutdown
- Handle InterruptedException properly

### Bước 2: ThreadPoolDemo.java

**Mục tiêu**: So sánh các loại thread pools

1. **testFixedThreadPool()**:
   - TODO: Tạo FixedThreadPool
   - TODO: Submit tasks
   - Quan sát behavior

2. **testCachedThreadPool()**:
   - TODO: Tạo CachedThreadPool
   - TODO: Submit tasks
   - Quan sát thread creation

3. **testSingleThreadExecutor()**:
   - TODO: Tạo SingleThreadExecutor
   - TODO: Submit tasks
   - Quan sát sequential execution

4. **testCustomThreadPool()**:
   - TODO: Tạo custom ThreadPoolExecutor
   - TODO: Custom ThreadFactory
   - TODO: Monitor pool metrics

5. **testRejectedExecutionHandler()**:
   - TODO: Tạo pool với bounded queue
   - TODO: Custom RejectedExecutionHandler
   - TODO: Submit more tasks than capacity

**Tips**:
- FixedThreadPool: Stable, predictable
- CachedThreadPool: Good for short-lived tasks
- Custom ThreadPoolExecutor: Full control

### Bước 3: ScheduledExecutorDemo.java

**Mục tiêu**: Scheduled và recurring tasks

1. **testScheduleWithDelay()**:
   - TODO: Schedule task với delay
   - TODO: Schedule Callable
   - TODO: Get result

2. **testScheduleAtFixedRate()**:
   - TODO: Schedule với fixed rate
   - Quan sát execution timing
   - Rate không phụ thuộc execution time

3. **testScheduleWithFixedDelay()**:
   - TODO: Schedule với fixed delay
   - Quan sát delay sau completion
   - Delay phụ thuộc execution time

4. **testCancelScheduledTasks()**:
   - TODO: Cancel scheduled tasks
   - TODO: Check cancellation status

**Tips**:
- Fixed rate: Next execution starts at fixed interval
- Fixed delay: Next execution starts after delay from completion
- Always cancel scheduled tasks when done

### Bước 4: WebServer.java

**Mục tiêu**: Áp dụng vào thực tế

1. **WebServer class**:
   - TODO: Tạo ThreadPoolExecutor
   - TODO: Custom RejectedExecutionHandler
   - TODO: Handle requests

2. **Request processing**:
   - TODO: Simulate different request types
   - TODO: Track statistics
   - TODO: Monitor pool

3. **Graceful shutdown**:
   - TODO: Shutdown executor
   - TODO: Wait for tasks
   - TODO: Force shutdown if needed

**Tips**:
- Use bounded queue để tránh OOM
- Monitor active threads và queue size
- Implement graceful shutdown

### Bước 5: TaskProcessor.java

**Mục tiêu**: Batch processing với ExecutorService

1. **processTasks()**:
   - TODO: Submit Callables
   - TODO: Collect results với Future
   - TODO: Handle exceptions

2. **processTasksWithCompletionService()**:
   - TODO: Use CompletionService
   - TODO: Get results as they complete
   - Quan sát completion order

3. **processTasksWithTimeout()**:
   - TODO: Process với timeout
   - TODO: Cancel timed-out tasks
   - Handle TimeoutException

**Tips**:
- CompletionService: Get results in completion order
- Timeout: Prevent tasks from running too long
- Always handle exceptions

## 🐛 Common Mistakes

1. **Forgetting to shutdown**:
   ```java
   // ❌ BAD
   ExecutorService executor = Executors.newFixedThreadPool(5);
   executor.submit(task);
   // Forgot to shutdown!
   
   // ✅ GOOD
   ExecutorService executor = Executors.newFixedThreadPool(5);
   try {
       executor.submit(task);
   } finally {
       executor.shutdown();
   }
   ```

2. **Blocking on Future.get()**:
   ```java
   // ❌ BAD: May cause deadlock
   Future<String> f1 = executor.submit(task1);
   Future<String> f2 = executor.submit(task2);
   String r1 = f1.get(); // Blocks
   String r2 = f2.get(); // Blocks
   
   // ✅ GOOD: Use CompletionService
   CompletionService<String> cs = new ExecutorCompletionService<>(executor);
   cs.submit(task1);
   cs.submit(task2);
   Future<String> first = cs.take(); // First completed
   ```

3. **Wrong pool size**:
   ```java
   // ❌ BAD: Too small
   ExecutorService executor = Executors.newFixedThreadPool(1);
   
   // ❌ BAD: Too large
   ExecutorService executor = Executors.newFixedThreadPool(1000);
   
   // ✅ GOOD: Based on workload
   int poolSize = Runtime.getRuntime().availableProcessors();
   ExecutorService executor = Executors.newFixedThreadPool(poolSize);
   ```

## 📊 Thread Pool Sizing

### CPU-bound Tasks
```java
int poolSize = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(poolSize);
```

### I/O-bound Tasks
```java
int poolSize = Runtime.getRuntime().availableProcessors() * 
               (1 + (wait time / compute time));
ExecutorService executor = Executors.newFixedThreadPool(poolSize);
```

### Mixed Workload
- Profile và tune dựa trên metrics
- Monitor active threads, queue size, throughput

## 🎓 Bài Tập Thêm

1. **Implement custom ThreadFactory**: Custom thread names, priorities
2. **Implement custom RejectedExecutionHandler**: Log, retry, fallback
3. **Monitor thread pool metrics**: Active threads, queue size, throughput
4. **Implement task prioritization**: PriorityBlockingQueue
5. **Implement task batching**: Group similar tasks

## ✅ Checklist Hoàn Thành

- [ ] Hiểu được Executor Framework
- [ ] Sử dụng được các loại thread pools
- [ ] Nắm vững Future và Callable
- [ ] Sử dụng được CompletionService
- [ ] Implement graceful shutdown
- [ ] Tune thread pool size
- [ ] Handle exceptions properly
- [ ] Implement custom ThreadPoolExecutor
- [ ] Hoàn thành WebServer project
- [ ] Hoàn thành TaskProcessor project

Chúc bạn học tốt! 🚀

