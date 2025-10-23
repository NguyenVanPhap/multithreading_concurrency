# Week 1 - Thread Basics

## 📚 Kiến Thức Cần Học
- Thread lifecycle (NEW → RUNNABLE → BLOCKED → WAITING → TIMED_WAITING → TERMINATED)
- Thread vs Runnable interface
- Thread methods: start(), join(), sleep(), yield(), interrupt()
- Basic synchronization concepts
- When to use multi-threading vs single-threading
- Performance comparison and measurement

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `ThreadCreationDemo` → `RaceConditionDemo` → `ThreadMethodsDemo`
2. **Chạy Race Simulator**: Hiểu thread management và tại sao multi-thread có thể chậm hơn
3. **Chạy Performance Demos**: `PrimeCalculatorDemo` và `DataProcessorDemo` để thấy multi-threading hiệu quả
4. **So sánh kết quả**: Hiểu khi nào multi-threading thực sự có lợi

## 🎯 Bài Tập Tuần 1

### Exercise 1: Thread Creation
- Tạo 10 threads, mỗi thread in ra số từ 1-100
- So sánh Thread vs Runnable
- Thực hành join(), sleep(), yield()

### Exercise 2: Race Condition Demo
- Tạo class Counter với method increment()
- Viết nhiều thread cùng increment counter
- Quan sát kết quả không đồng nhất (race condition)

### 🏆 Mini Projects

#### 1. Race Simulator
**Mô tả**: Mô phỏng cuộc đua xe/đua ốc sên
- Mỗi thread = 1 xe/ốc sên
- Tiến ngẫu nhiên theo tốc độ khác nhau  
- In ra progress real-time
- Kết quả cuối: ai thắng?

**Features**:
- [x] Thread creation và management
- [x] Random movement simulation
- [x] Real-time progress tracking
- [x] Winner determination
- [x] Performance comparison (1 vs N threads)

#### 2. Prime Calculator Demo
**Mô tả**: So sánh hiệu suất tính số nguyên tố
- Single-thread vs Multi-thread
- Tìm số nguyên tố từ 2 đến 1,000,000
- CPU-intensive task

**Features**:
- [x] Performance comparison
- [x] CPU-intensive workload
- [x] Speedup calculation

#### 3. Data Processor Demo
**Mô tả**: So sánh xử lý dữ liệu lớn
- Single-thread vs Multi-thread
- Tính tổng bình phương 10 triệu số
- Parallel processing

**Features**:
- [x] Large dataset processing
- [x] Performance comparison
- [x] Parallel data processing

## 📁 File Structure
```
week1-thread-basics/
├── README.md
├── MULTITHREADING_DEMO_README.md
├── LEARNING_GUIDE.md
├── src/
│   ├── main/java/
│   │   ├── exercises/
│   │   │   ├── ThreadCreationDemo.java
│   │   │   ├── RaceConditionDemo.java
│   │   │   └── ThreadMethodsDemo.java
│   │   └── projects/
│   │       ├── RaceSimulator.java
│   │       ├── PrimeCalculatorDemo.java
│   │       └── DataProcessorDemo.java
│   └── test/java/
│       └── RaceSimulatorTest.java
├── pom.xml
└── run_demo.bat
```

## 🚀 Cách Chạy

### Sử dụng Maven:
```bash
cd week1-thread-basics

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.ThreadCreationDemo"
mvn compile exec:java -Dexec.mainClass="exercises.RaceConditionDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ThreadMethodsDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.RaceSimulator"
mvn compile exec:java -Dexec.mainClass="projects.PrimeCalculatorDemo"
mvn compile exec:java -Dexec.mainClass="projects.DataProcessorDemo"
```

### Sử dụng batch file (Windows):
```bash
cd week1-thread-basics
run_demo.bat
```

### Chạy trực tiếp với Java:
```bash
cd week1-thread-basics/src/main/java

# Compile
javac exercises/*.java projects/*.java

# Run
java exercises.ThreadCreationDemo
java projects.RaceSimulator
java projects.PrimeCalculatorDemo
java projects.DataProcessorDemo
```


## 📊 So Sánh Các Demo Projects

| Demo | Task Type | Dataset Size | Delay | Expected Speedup | Lý do |
|------|-----------|--------------|-------|------------------|-------|
| **Race Simulator** | I/O-bound | Nhỏ (100 units) | 50-150ms | 0.5-1.0x (chậm hơn) | Thread.sleep overhead |
| **Prime Calculator** | CPU-bound | Lớn (1M numbers) | Không | 2.5-3.5x | Tính toán phức tạp |
| **Data Processor** | CPU-bound | Rất lớn (10M items) | Không | 2.0-3.0x | Parallel processing |

### 🎯 **Kết luận quan trọng:**
- **Race Simulator**: Demo về thread management, không phải performance
- **Prime Calculator & Data Processor**: Demo về multi-threading hiệu quả
- **Multi-threading không phải lúc nào cũng nhanh hơn!**

## 🧭 Khi nào dùng Single-thread vs Multi-thread

- **Dùng Single-thread khi:**
  - Tác vụ nhỏ, ngắn; overhead tạo/quản lý thread không đáng so với lợi ích
  - I/O-bound ít chờ đợi hoặc đã có thư viện async/non-blocking
  - Logic phụ thuộc tuần tự, cần tính quyết định theo thứ tự; ưu tiên đơn giản/dễ debug
  - Môi trường tài nguyên hạn chế (mobile/embedded), tránh oversubscription

- **Dùng Multi-thread khi:**
  - Công việc **CPU-intensive** có thể chia nhỏ độc lập (no shared state) 
  - Dữ liệu lớn cần xử lý song song; mỗi phần có thể tính độc lập
  - Muốn tận dụng nhiều CPU cores (thường chọn số threads ≈ số cores)
  - I/O-bound có nhiều tác vụ chờ đợi độc lập (thread pool che giấu độ trễ)

- **Lưu ý quan trọng:**
  - Tránh tạo quá nhiều threads (oversubscription) → context switching tăng, hiệu năng giảm
  - Chia việc đều giữa threads; giảm contention/synchronization, tránh shared mutable state
  - Luôn đo lường trước/sau (profiling) và tối ưu dựa trên số liệu thực tế

## 💡 Tips
- Dùng Thread.sleep() để simulate movement delay
- Random sử dụng java.util.Random hoặc ThreadLocalRandom
- Thread.join() để đợi tất cả threads hoàn thành
- Thread.interrupt() để dừng thread gracefully
- **Quan trọng**: Multi-threading không phải lúc nào cũng nhanh hơn!


