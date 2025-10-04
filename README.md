# 🔥 Multithreading & Concurrency - LỘ TRÌNH THỰCH HÀNH 3 THÁNG

**🎯 Mục tiêu**: Từ Junior → Senior Developer, làm chủ **multithreading & concurrency** thông qua thực hành!

---

## 📚 **THÁNG 1 - Thread & Synchronization Cơ Bản**

### 🔸 **Tuần 1 - Làm Quen Thread**
- **Kiến thức**: Thread lifecycle, Thread vs Runnable, Thread methods (start, join, sleep, yield)
- **Thực hành**: 
  - Tạo 10 threads, mỗi thread in số 1-100
  - So sánh hiệu năng Thread vs Runnable
- **🎮 Mini Project**: **Race Simulator** (mô phỏng đua xe)
  - Nhiều threads = nhiều xe đua
  - Random movement, track progress real-time
  - **Learning**: Thread management, timing, interrupt handling

### 🔸 **Tuần 2 - Synchronization**
- **Kiến thức**: Race condition, synchronized keyword, ReentrantLock
- **Thực hành**:
  - BankAccount với deposit/withdraw
  - Demo race condition khi không sync
  - Fix bằng synchronized và ReentrantLock
- **🎮 Mini Project**: **ATM Simulator**
  - Multiple users cùng rút tiền từ 1 account
  - **Learning**: Thread safety, lock management

### 🔸 **Tuần 3 - Deadlock & Lock**
- **Kiến thức**: Deadlock detection/prevention, volatile keyword, lock ordering
- **Thực hành**:
  - Viết ví dụ gây deadlock
  - Fix deadlock bằng tryLock, timeout
  - Demo volatile vs non-volatile counter
- **🎮 Mini Project**: **Dining Philosophers Problem**
  - Classic deadlock scenario
  - **Learning**: Resource contention, deadlock debugging

### 🔸 **Tuần 3.5 - Debugging & Profiling** ⚡
- **Kiến thức**: VisualVM, Thread dump analysis, Memory profiling
- **🎮 Mini Project**: **Thread Pool Monitor Dashboard**
  - Real-time thread monitoring
  - **Learning**: Production debugging skills

---

## 🚀 **THÁNG 2 - Java Concurrency Utilities**

### 🔸 **Tuần 4 - Executor & Thread Pool**
- **Kiến thức**: ExecutorService, Thread pool types, Future/Callable
- **Thực hành**:
  - `newFixedThreadPool`, `newCachedThreadPool`, `newScheduledThreadPool`
  - Future.get() blocking
- **🎮 Mini Project**: **Image Downloader**
  - Download 10 ảnh song song từ URLs
  - **Learning**: Task parallelization, resource management

### 🔸 **Tuần 5 - Concurrent Collections**
- **Kiến thức**: ConcurrentHashMap, BlockingQueue, CopyOnWriteArrayList
- **Thực hành**:
  - Producer-Consumer pattern với BlockingQueue
  - Performance comparison vs synchronized collections
- **🎮 Mini Project**: **Log Processor**
  - 1 thread đọc log files
  - Nhiều threads xử lý & lưu database
  - **Learning**: Work distribution, bounded queues

### 🔸 **Tuần 6 - Synchronizers**
- **Kiến thức**: CountDownLatch, CyclicBarrier, Semaphore
- **Thực hành**:
  - Coordinate multiple threads completion
  - Rate limiting với Semaphore
- **🎮 Mini Project**: **Multi-player Game Start**
  - Players phải ready tất cả
  - Barrier synchronization để bắt đầu race
  - **Learning**: Complex coordination patterns

### 🔸 **Tuần 6.5 - Performance & Benchmarking** 🔥
- **Kiến thức**: JMH (Java Microbenchmark Harness)
- **🎮 Mini Project**: **Performance Testing Suite**
  - Comparative analysis tools
  - **Learning**: Production performance optimization

---

## 🎯 **THÁNG 3 - Hiểu Sâu & Ứng Dụng Nâng Cao**

### 🔸 **Tuần 7 - Atomic & Lock-Free**
- **Kiến thức**: AtomicInteger, CAS (Compare-And-Swap), lock-free algorithms
- **Thực hành**:
  - Thread-safe counter bằng AtomicInteger
  - Compare synchronized vs Atomic performance
- **🎮 Mini Project**: **Counter Benchmark**
  - So sánh 3 approaches: normal int, synchronized, atomic
  - Stress test với high concurrency
  - **Learning**: Performance trade-offs

### 🔸 **Tuần 8 - CompletableFuture**
- **Kiến thức**: Asynchronous programming, combining futures
- **Thực hành**:
  - API call async với supplyAsync()
  - Chain futures với thenCombine, allOf
- **🎮 Mini Project**: **Crypto Price Fetcher**
  - Fetch giá BTC/ETH từ multiple exchanges song song
  - Aggregate results từ nhiều sources
  - **Learning**: Modern async patterns

### 🔸 **Tuần 9 - Parallelism & ForkJoin**
- **Kiến thức**: ForkJoinPool, parallelStream(), work-stealing
- **Thực hành**:
  - Calculate Fibonacci với recursive parallelism
  - Process large datasets với parallelStream
- **🎮 Mini Project**: **Word Frequency Counter**
  - Count word occurrences trong large text files
  - Divide-and-conquer với ForkJoinPool
  - **Learning**: CPU-intensive parallel processing

### 🔸 **Tuần 10 - Virtual Threads (Java 21+)**
- **Kiến thức**: Project Loom, Virtual vs Platform threads
- **Thực hành**:
  - Create millions of virtual threads
  - Memory footprint comparison
- **🎮 Mini Project**: **Chat Server**
  - 1 virtual thread per user connection
  - Real-time messaging với high concurrency
  - **Learning**: Modern concurrency model

### 🔸 **Tuần 11 - Real-time System**
- **Kiến thức**: Event-driven architecture, reactive programming
- **🎮 Mini Project**: **Stock Trading Simulator**
  - Multiple users đặt buy/sell orders (thread-safe)
  - OrderBook thread xử lý order matching
  - Real-time trade execution & history
  - **Learning**: Complex concurrent business logic

### 🔸 **Tuần 11.5 - Production Readiness** 🚀
- **Kiến thức**: Spring Boot + @Async, error handling, circuit breakers
- **🎮 Mini Project**: **REST API with Async Processing**
  - Database transaction handling
  - **Learning**: Enterprise patterns

### 🔸 **Tuần 12 - Final Project**
- **🎮 Capstone Project**: **Mini Web Crawler**
  - **Input**: List of URLs
  - **Features**:
    - Multi-threaded web scraping
    - BlockingQueue task management
    - Concurrent database writes
    - Progress tracking & error handling
    - **Deliverables**: Complete web application với documentation

---

## 🏆 **MỤC TIÊU SAU 3 THÁNG**

| Skill Level | Achievement |
|-------------|-------------|
| ✅ **Thread Management** | Tạo, quản lý, terminate threads confidently |
| ✅ **Synchronization** | Hiểu sâu synchronized, locks, atomic operations |
| ✅ **Concurrency Tools** | Thành thạo `java.util.concurrent.*` package |
| ✅ **Performance** | Debug, profile, optimize concurrent applications |
| ✅ **Modern Patterns** | CompletableFuture, Virtual Threads, Reactive |
| ✅ **Production Ready** | Error handling, monitoring, resilience |
| ✅ **Architecture** | Thiết kế concurrent systems từ đầu |
| ✅ **Portfolio** | Có 12+ projects để showcase trong CV/interviews |

---

## 🛠️ **SETUP REQUIREMENTS**

### **Core Tools**
- **Java 17+** (khuyến nghị **Java 21** để có Virtual Threads)
- **IDE**: IntelliJ IDEA (Professional) hoặc Eclipse
- **Build Tool**: Maven hoặc Gradle
- **Version Control**: Git + GitHub

### **Monitoring & Profiling Tools**
- **VisualVM**: Thread monitoring, memory profiling
- **JProfiler**: Advanced profiling (trial version OK)
- **JMeter**: Load testing (cho performance benchmarks)
- **Java Mission Control (JMC)**: Built-in với JDK

### **Optional Enhancement Tools**
- **Spring Boot**: Cho production-ready projects
- **Docker**: Containerization
- **JMH (Java Microbenchmark Harness)**: Micro-benchmarking

---

## 📖 **KIẾN THỨC NỀN TẢNG**

### **📚 Sách Phải Đọc**
1. **[Java Concurrency in Practice - Brian Goetz](https://jcip.net/)**
   - *The Bible* của Java Concurrency
   - Deep dive vào JMM (Java Memory Model)

2. **[Effective Java - Joshua Bloch](https://www.amazon.com/Effective-Java-3rd-Joshua-Bloch/dp/0134685997)**
   - Chapter 11: Concurrency

### **🎥 Video Resources**
- [Java Concurrency Tutorial - Jakob Jenkov](https://jenkov.com/tutorials/java-concurrency/index.html)
- [Java Memory Model - Aleksey Shipilёv](https://shipilev.net/blog/2014/safe-public-construction/)

### **🌐 Online References**
- [Oracle Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Baeldung Concurrency Articles](https://www.baeldung.com/java-concurrency)
- [Java Concurrent Collections](https://www.baeldung.com/java-concurrent-collections)

---

## 🚀 **QUICK START**

### **Setup Project**
```bash
# Clone repository
git clone <your-repo-url>
cd multithreading_concurrency

# Setup Java 21 (tuỳ chọn)
# Download từ: https://jdk.java.net/21/

# Verify Java version
java --version
```

### **Running Projects**
```bash
# Chạy bất kỳ project nào
cd week1-thread-basics
mvn clean compile
mvn exec:java -Dexec.mainClass="projects.RaceSimulator"

# Hoặc build và chạy
mvn package
java -jar target/week1-thread-basics-1.0.0.jar
```

### **Running Tests**
```bash
# Chạy all tests trong project
mvn test

# Chạy specific test class
mvn test -Dtest=RaceSimulatorTest

# Generate test coverage report
mvn test jacoco:report
```

---

## 📈 **PROGRESS TRACKING**

### **Mỗi Tuần Checklist**
- [ ] Hoàn thành exercises cơ bản
- [ ] Viết code cho mini project
- [ ] Viết unit tests (ít nhất 80% coverage)
- [ ] Profile performance và analyze results
- [ ] Document learnings & challenges
- [ ] Commit code với detailed commit messages

### **Monthly Reviews**
- **End of Month 1**: Dashboard với race condition demons
- **End of Month 2**: Concurrent utilities showcase
- **End of Month 3**: Full portfolio website với live demos

---

## 💡 **LEARNING TIPS**

### **🔍 Debugging Tips**
1. **VisualVM**: Monitor thread states real-time
2. **Thread Dumps**: `jstack <pid>` khi gặp deadlock
3. **JConsole**: Memory leaks detection
4. **YourKit**: Advanced profiling (trial available)

### **⚡ Performance Tips**
1. **Measure First**: Always benchmark before optimizing
2. **Contention Hotspots**: Identify thread competition areas
3. **Lock Granularity**: Fine-grained locks vs coarse-grained
4. **Lock-free Alternatives**: Where possible, use atomics

### **🚨 Common Pitfalls**
- ❌ **forgetting** to handle `InterruptedException`
- ❌ **ignoring** memory visibility với volatile
- ❌ **over-synchronizing** everything
- ❌ **not testing** under load
- ❌ **ignoring** deadlock prevention

---

## 🏆 **CAREER IMPACT**

### **Skills Transferable**
- **System Design**: Scalable concurrent architectures
- **Performance**: High-throughput applications
- **Cloud Computing**: Microservices concurrency patterns
- **Real-time Systems**: Trading platforms, gaming servers
- **Big Data**: Parallel processing frameworks

### **Interview Advantages**
- ✅ **Technical Depth**: Explain JMM confidently
- **Problem Solving**: Debug complex concurrent bugs
- **Architecture**: Design multi-threaded systems from scratch
- **Portfolio**: 12+ real projects to showcase

---

## 🤝 **COLLABORATION**

### **Community**
- **Reddit**: r/java, r/programming
- **Stack Overflow**: Tag questions với `java-threading`
- **GitHub**: Find open-source concurrency projects

### **Mentorship**
- Sẽ có **weekly code reviews** (optional)
- **Slack/Discord community** cho hỗ trợ
- **Pair programming sessions** cho complex projects

---

💡 **REMEMBER**: *"Practice makes perfect!"* Commit code mỗi ngày, track progress, và đừng ngại ask questions!
