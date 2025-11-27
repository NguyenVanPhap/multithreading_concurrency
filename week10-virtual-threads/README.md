# Week 10 - Virtual Threads

## 📚 Kiến Thức Cần Học
- Virtual Threads là gì (Java 19+)
- Platform threads vs Virtual threads
- Creating virtual threads
- Executors.newVirtualThreadPerTaskExecutor()
- Thread.ofVirtual()
- Use cases và benefits
- Migration từ platform threads
- Performance characteristics

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `VirtualThreadDemo` → `VirtualThreadExecutorDemo` → `PerformanceDemo`
2. **Thực hành với Projects**: High-concurrency scenarios
3. **So sánh**: Virtual threads vs Platform threads

## 🎯 Bài Tập Tuần 10

### Exercise 1: Virtual Thread Basics
- [ ] Creating virtual threads
- [ ] Thread.ofVirtual()
- [ ] Basic operations

### Exercise 2: Virtual Thread Executor
- [ ] Executors.newVirtualThreadPerTaskExecutor()
- [ ] Submit tasks
- [ ] Performance testing

### Exercise 3: Performance Comparison
- [ ] Virtual threads vs Platform threads
- [ ] High-concurrency scenarios
- [ ] I/O-bound vs CPU-bound

## 🏆 Mini Projects

### 1. High-Concurrency Server
**Mô tả**: Server với virtual threads
- Handle many concurrent connections
- I/O-bound operations
- Performance testing

### 2. Async Task Processor
**Mô tả**: Process many async tasks
- Virtual thread executor
- Task management
- Statistics

## 📁 File Structure
```
week10-virtual-threads/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── VirtualThreadDemo.java
    │   │   ├── VirtualThreadExecutorDemo.java
    │   │   └── PerformanceDemo.java
    │   └── projects/
    │       ├── HighConcurrencyServer.java
    │       └── AsyncTaskProcessor.java
    └── test/java/
```

## 💡 Key Concepts

### 1. Creating Virtual Threads
```java
Thread virtualThread = Thread.ofVirtual().start(() -> {
    // task
});

ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
```

### 2. Benefits
- Lightweight (millions of threads)
- Better for I/O-bound tasks
- No thread pool needed

## 🚨 Common Pitfalls
1. **CPU-bound tasks**: Virtual threads không tốt hơn
2. **ThreadLocal**: Có thể expensive với virtual threads
3. **Pinning**: Platform thread blocking

## 🎯 Tips
- Virtual threads: I/O-bound, high-concurrency
- Platform threads: CPU-bound, limited concurrency
- Migration: Dễ dàng với Executors.newVirtualThreadPerTaskExecutor()

