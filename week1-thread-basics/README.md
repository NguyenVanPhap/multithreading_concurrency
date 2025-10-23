# Week 1 - Thread Basics

## 📚 Kiến Thức Cần Học
- Thread lifecycle (NEW → RUNNABLE → BLOCKED → WAITING → TIMED_WAITING → TERMINATED)
- Thread vs Runnable interface
- Thread methods: start(), join(), sleep(), yield(), interrupt()
- Basic synchronization concepts

## 🎯 Bài Tập Tuần 1

### Exercise 1: Thread Creation
- Tạo 10 threads, mỗi thread in ra số từ 1-100
- So sánh Thread vs Runnable
- Thực hành join(), sleep(), yield()

### Exercise 2: Race Condition Demo
- Tạo class Counter với method increment()
- Viết nhiều thread cùng increment counter
- Quan sát kết quả không đồng nhất (race condition)

### 🏆 Mini Project: Race Simulator

**Mô tả**: Mô phỏng cuộc đua xe/đua ốc sên
- Mỗi thread = 1 xe/ốc sên
- Tiến ngẫu nhiên theo tốc độ khác nhau  
- In ra progress real-time
- Kết quả cuối: ai thắng?

**Features**:
- [ ] Thread creation và management
- [ ] Random movement simulation
- [ ] Real-time progress tracking
- [ ] Winner determination
- [ ] Performance comparison (1 vs N threads)

## 📁 File Structure
```
week1-thread-basics/
├── README.md
├── src/
│   ├── main/java/
│   │   ├── exercises/
│   │   │   ├── ThreadCreationDemo.java
│   │   │   ├── RaceConditionDemo.java
│   │   │   └── ThreadMethodsDemo.java
│   │   └── projects/
│   │       └── RaceSimulator.java
│   └── test/java/
│       └── RaceSimulatorTest.java
└── pom.xml
```

## 🚀 Cách Chạy
```bash
cd week1-thread-basics
mvn compile exec:java -Dexec.mainClass="exercises.ThreadCreationDemo"
mvn compile exec:java -Dexec.mainClass="projects.RaceSimulator"
```

## ⚠️ Tại Sao Race Simulator Multi-thread Chậm Hơn Single-thread?

### 🔍 **Phân tích kết quả Performance Comparison:**

Khi chạy Performance Comparison trong Race Simulator, bạn sẽ thấy:
```
Multi-threaded time: 38324 ms
Single-threaded time: 1 ms
Speedup: 0.00x
```

### 🎯 **Nguyên nhân chính:**

#### 1. **Thread.sleep() quá dài**
```java
// Trong Racer.race() - dòng 471
Thread.sleep(random.nextLong(50,150)); // 50-150ms mỗi bước!
```
- Mỗi thread phải chờ 50-150ms sau mỗi bước
- 10 threads × 100ms × 25 bước = **25,000ms+** thời gian chờ
- Đây là **I/O-bound task**, không phải CPU-bound

#### 2. **Single-thread cũng có delay tương tự**
```java
// Trong simulateSequentialRace() - dòng 256
Thread.sleep(random.nextLong(50, 150)); // Cùng delay như multi-thread
```
- Single-thread cũng có Thread.sleep() để so sánh công bằng
- Nhưng vẫn chậm hơn vì phải xử lý tuần tự

#### 3. **Thread overhead lớn hơn benefit**
- **Thread creation cost**: Tạo 10 threads tốn thời gian
- **Context switching**: Chuyển đổi giữa threads
- **Memory overhead**: Mỗi thread cần stack riêng
- **Synchronization cost**: AtomicBoolean, AtomicInteger
- **Display overhead**: displayRaceTrack() được gọi mỗi 100ms

#### 4. **Sequential vs Parallel processing**
- **Single-thread**: Xử lý từng racer một cách tuần tự
- **Multi-thread**: Xử lý tất cả racers song song
- **Nhưng**: Với I/O-bound tasks, parallel không mang lại lợi ích

### 📊 **So sánh Task Types:**

| Aspect | Race Simulator | Multi-threading hiệu quả |
|--------|----------------|---------------------------|
| **Task Type** | I/O-bound (sleep) | CPU-bound (tính toán) |
| **Workload** | Đơn giản (position++) | Phức tạp (isPrime, math) |
| **Delay** | 50-150ms mỗi bước (cả 2 version) | Không có delay |
| **Dataset** | Nhỏ (100 units) | Lớn (1M-10M items) |
| **Dependencies** | Independent | Independent |
| **Processing** | Sequential vs Parallel | Parallel hiệu quả |

### 🎓 **Bài học quan trọng:**

#### ✅ **Multi-threading hiệu quả khi:**
- **CPU-intensive tasks** (tính toán phức tạp)
- **Independent work** (không phụ thuộc lẫn nhau)
- **Large datasets** (nhiều dữ liệu cần xử lý)
- **No I/O blocking** (không chờ đợi)

#### ❌ **Multi-threading không hiệu quả khi:**
- **I/O-bound tasks** (chờ network, file, sleep)
- **Simple calculations** (phép tính đơn giản)
- **Small datasets** (ít dữ liệu)
- **Thread overhead > benefit**

### 🚀 **Xem Demo Multi-threading Hiệu Quả:**

Chạy các demo trong thư mục `src/main/java/projects/`:
- `PrimeCalculatorDemo.java` - Tính số nguyên tố
- `DataProcessorDemo.java` - Xử lý dữ liệu lớn

Những demo này sẽ cho thấy multi-threading **nhanh hơn 2-4 lần** vì:
- Task phức tạp (CPU-intensive)
- Không có delay
- Benefit > overhead

## 💡 Tips
- Dùng Thread.sleep() để simulate movement delay
- Random sử dụng java.util.Random hoặc ThreadLocalRandom
- Thread.join() để đợi tất cả threads hoàn thành
- Thread.interrupt() để dừng thread gracefully
- **Quan trọng**: Multi-threading không phải lúc nào cũng nhanh hơn!
