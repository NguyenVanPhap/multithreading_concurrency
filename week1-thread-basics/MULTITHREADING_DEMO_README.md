# 🚀 Multi-threading Demo Projects - Tuần 1

## 📋 Tổng quan

Đây là các demo project minh họa **khi nào multi-threading thực sự hiệu quả** hơn single-threading trong Week 1 - Thread Basics.

## 🎯 Mục đích

Sau khi học về Thread lifecycle và basic synchronization, các demo này sẽ cho thấy:
- **Khi nào** multi-threading nhanh hơn
- **Tại sao** cần phân tích workload trước khi dùng multi-threading
- **Cách** thiết kế multi-threading hiệu quả
- **Pitfalls** thường gặp (closure capture, floating point precision)

## 📁 Các Demo Projects

### 1. 📊 DataProcessorDemo.java
**Mục đích**: So sánh hiệu suất xử lý dữ liệu lớn giữa single-thread và multi-thread

**Task**: 
- Tính tổng bình phương của 100 triệu số ngẫu nhiên
- Tìm min/max của bình phương
- So sánh thời gian thực thi

**Các chế độ**:
1. Single-threaded Processing
2. Multi-threaded Processing (8 threads)
3. Performance Comparison

**Kết quả thực tế**:
```
🖥️  CPU Info:
   - Available cores: 8
   - Threads used: 8

Single-threaded: 98 ms
Multi-threaded:  45 ms
Speedup: 2.18x ✅

Multi-threading nhanh hơn 2.2 lần!
```

**Tại sao hiệu quả**:
- ✅ **CPU-intensive**: Phép nhân và cộng liên tục
- ✅ **Parallelizable**: Chia nhỏ dữ liệu thành chunks
- ✅ **No dependencies**: Không phụ thuộc giữa các phần
- ✅ **Large dataset**: 100 triệu phần tử
- ✅ **Số threads khớp với CPU cores**: 8 threads = 8 cores

**Concepts học được**:
- ExecutorService và Future
- Chia dữ liệu thành chunks
- Closure capture bugs và cách fix (final variables)
- Floating point precision trong parallel computing
- CPU info với `Runtime.getRuntime().availableProcessors()`

### 2. 🏁 RaceSimulator.java
**Mục đích**: Demo multi-threading với I/O-bound operations (Thread.sleep)

**Task**:
- Nhiều threads = nhiều xe đua
- Random movement với delay
- Real-time progress tracking

**Learning Points**:
- Thread lifecycle và state transitions
- Thread.sleep() cho I/O simulation
- Chờ đợi threads hoàn thành (join)
- Multi-threading KHÔNG hiệu quả với simple tasks + delays

## 📊 Khi nào Multi-threading Hiệu quả?

### ✅ Hiệu quả (Data Processor):
| Aspect | Details |
|--------|---------|
| **Task Type** | CPU-bound (tính toán phức tạp) |
| **Delay** | Không có delay |
| **Workload** | Phức tạp (phép toán) |
| **Dataset** | Lớn (100M items) |
| **Threads** | Khớp với CPU cores |
| **Result** | Multi-thread nhanh hơn 2.2x |

### ❌ Không hiệu quả (Race Simulator):
| Aspect | Details |
|--------|---------|
| **Task Type** | I/O-bound (có sleep) |
| **Delay** | 50-150ms mỗi bước |
| **Workload** | Đơn giản (position++) |
| **Dataset** | Nhỏ (100 units) |
| **Result** | Multi-thread chậm hơn |

## 🚀 Cách chạy

### Chạy Data Processor:
```bash
cd week1-thread-basics
mvn compile
mvn exec:java -Dexec.mainClass="projects.DataProcessorDemo"
# Hoặc với Maven:
java -cp target/classes projects.DataProcessorDemo test  # Tự động chạy performance comparison
```

### Chạy Race Simulator:
```bash
cd week1-thread-basics
mvn exec:java -Dexec.mainClass="projects.RaceSimulator"
```

## 🎓 Bài học quan trọng

### ✅ Multi-threading hiệu quả khi:
1. **CPU-intensive tasks** (tính toán phức tạp, không I/O)
2. **Independent work** (không phụ thuộc lẫn nhau)
3. **Large datasets** (nhiều dữ liệu cần xử lý)
4. **Threads ≈ CPU cores** (tối ưu resource)
5. **No blocking I/O** (không có sleep, network, file I/O)

### ❌ Multi-threading không hiệu quả khi:
1. **I/O-bound tasks** (chờ network, file, sleep)
2. **Simple calculations** (phép tính đơn giản như ++)
3. **Small datasets** (ít dữ liệu, overhead > benefit)
4. **Sequential dependencies** (phụ thuộc tuần tự)
5. **Too many threads** (nhiều hơn CPU cores)

### 🐛 Common Pitfalls học được:

#### 1. **Closure Capture Bug**
```java
// ❌ SAI - Biến trong loop có thể bị capture sai
for (int i = 0; i < THREAD_COUNT; i++) {
    int start = i * chunkSize;
    executor.submit(() -> {
        // start có thể bị capture sai!
        for (int j = start; j < end; j++) { ... }
    });
}

// ✅ ĐÚNG - Copy vào final
for (int i = 0; i < THREAD_COUNT; i++) {
    final int chunkIndex = i;
    final int start = chunkIndex * chunkSize;
    executor.submit(() -> {
        // Mỗi lambda có bản copy riêng
        for (int j = start; j < end; j++) { ... }
    });
}
```

#### 2. **Floating Point Precision**
```java
// Single-thread: cộng tuần tự
sum += value * value;

// Multi-thread: cộng theo thứ tự khác nhau
totalSum += result1 + result2 + result3 + result4;

// → Kết quả có thể khác nhau do floating point precision!
// Solution: Accept small differences (< 1.0) hoặc dùng BigDecimal
```

## 🔧 Tối ưu hóa

### Checklist để có speedup tốt nhất:
- [ ] **Số thread = CPU cores**: `Runtime.getRuntime().availableProcessors()`
- [ ] **Chia công việc đều**: Mỗi thread xử lý ~cùng lượng work
- [ ] **Tránh synchronization overhead**: Dùng ít locks/atomics
- [ ] **Profile thực tế**: Đo lường trước và sau optimization
- [ ] **Test với dataset lớn**: Ít nhất 10M+ items

## 🎯 Kết luận

**Race Simulator chậm hơn vì**:
- Task quá đơn giản (position++)
- Có delay (Thread.sleep)
- Overhead tạo thread > benefit

**Data Processor nhanh hơn vì**:
- Task phức tạp (tính toán)
- Không có delay
- Benefit > overhead
- Số threads tối ưu

**→ Multi-threading không phải lúc nào cũng nhanh hơn!**
**→ Phải phân tích workload trước khi quyết định dùng multi-threading.**

## 📚 Resources

- [Java ExecutorService Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)
- [Understanding Java Memory Model](https://www.baeldung.com/java-memory-model)
- [Floating Point Precision](https://www.geeksforgeeks.org/floating-point-precision-in-java/)
