# Week 15 - BlockingQueue + ThreadPool (Advanced)

## 📚 Mô Tả

Project nâng cao kết hợp **BlockingQueue** và **ThreadPool** để xây dựng hệ thống xử lý task queue với producer-consumer pattern.

## 🎯 Mục Tiêu Học Tập

- Kết hợp BlockingQueue với ThreadPoolExecutor
- Producer-Consumer pattern nâng cao
- Task prioritization
- Backpressure handling
- Graceful shutdown
- Performance optimization

## 🏆 Projects

### 1. Task Queue Processor (Khó)
**Mô tả**: Hệ thống xử lý task queue với thread pool
- Multiple producers và consumers
- Task prioritization
- Backpressure với bounded queue
- Dynamic thread pool sizing
- Task retry mechanism
- Statistics tracking

**Features**:
- [ ] BlockingQueue cho task queue
- [ ] ThreadPoolExecutor cho processing
- [ ] Priority queue cho task ordering
- [ ] Backpressure handling
- [ ] Dynamic thread pool adjustment
- [ ] Task retry với exponential backoff
- [ ] Graceful shutdown
- [ ] Performance metrics

### 2. Event-Driven Message Broker (Khó)
**Mô tả**: Message broker với event-driven architecture
- Multiple producers
- Topic-based routing
- Consumer groups
- Message persistence
- Delivery guarantees

**Features**:
- [ ] BlockingQueue cho mỗi topic
- [ ] ThreadPoolExecutor cho message processing
- [ ] Topic routing
- [ ] Consumer group management
- [ ] Message acknowledgment
- [ ] At-least-once delivery

## 📁 File Structure
```
week15-blockingqueue-threadpool/
├── README.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── BlockingQueueThreadPoolDemo.java
    │   │   └── ProducerConsumerDemo.java
    │   └── projects/
    │       ├── TaskQueueProcessor.java
    │       └── EventDrivenMessageBroker.java
    └── test/java/
```

## 🚀 Cách Chạy

```bash
cd week15-blockingqueue-threadpool

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.BlockingQueueThreadPoolDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ProducerConsumerDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.TaskQueueProcessor"
mvn compile exec:java -Dexec.mainClass="projects.EventDrivenMessageBroker"
```

## 💡 Key Concepts

### 1. BlockingQueue với ThreadPoolExecutor
```java
BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5, 10, 60L, TimeUnit.SECONDS, queue
);
```

### 2. Producer-Consumer Pattern
```java
// Producer
queue.put(task);

// Consumer
Task task = queue.take();
process(task);
```

### 3. Priority Queue
```java
BlockingQueue<Task> queue = new PriorityBlockingQueue<>(
    100, Comparator.comparing(Task::getPriority)
);
```

## 🚨 Common Pitfalls
1. **Unbounded queue**: Có thể gây OutOfMemoryError
2. **Not handling InterruptedException**: Phải handle properly
3. **Deadlock**: Producer và consumer blocking nhau
4. **Not shutting down**: Threads không terminate
5. **Queue full**: Không handle RejectedExecutionException

