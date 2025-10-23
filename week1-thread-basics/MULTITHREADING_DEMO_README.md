# 🚀 Multi-threading Demo Projects

## 📋 Tổng quan

Đây là các project demo để minh họa **khi nào multi-threading thực sự hiệu quả** hơn single-threading.

## 🎯 Mục đích

Sau khi chạy Race Simulator và thấy multi-threading chậm hơn, các demo này sẽ cho thấy:
- **Khi nào** multi-threading nhanh hơn
- **Tại sao** Race Simulator chậm hơn
- **Cách** thiết kế multi-threading hiệu quả

## 📁 Các Project Demo

### 1. 🔢 PrimeCalculatorDemo.java
**Task**: Tìm tất cả số nguyên tố từ 2 đến 1,000,000

**Tại sao hiệu quả**:
- ✅ **CPU-intensive**: Tính toán phức tạp (kiểm tra số nguyên tố)
- ✅ **Independent work**: Mỗi số có thể kiểm tra độc lập
- ✅ **No I/O**: Không có delay, sleep, hay chờ đợi
- ✅ **Large dataset**: 1 triệu số cần xử lý

**Kết quả mong đợi**: Multi-threading nhanh hơn 2-4 lần

### 2. 📊 DataProcessorDemo.java
**Task**: Tính tổng bình phương của 10 triệu số ngẫu nhiên

**Tại sao hiệu quả**:
- ✅ **CPU-intensive**: Phép nhân và cộng liên tục
- ✅ **Parallelizable**: Có thể chia nhỏ dữ liệu
- ✅ **No dependencies**: Không phụ thuộc giữa các phần
- ✅ **Large dataset**: 10 triệu phần tử

**Kết quả mong đợi**: Multi-threading nhanh hơn 2-4 lần

## 🏁 Race Simulator vs Demo Projects

| Aspect | Race Simulator | Demo Projects |
|--------|----------------|---------------|
| **Task Type** | I/O-bound (sleep) | CPU-bound (tính toán) |
| **Delay** | 50-150ms mỗi bước | Không có delay |
| **Workload** | Đơn giản (position++) | Phức tạp (isPrime, math) |
| **Dataset** | Nhỏ (100 units) | Lớn (1M-10M items) |
| **Result** | Multi-thread chậm hơn | Multi-thread nhanh hơn |

## 🚀 Cách chạy

### Chạy Prime Calculator:
```bash
cd src/main/java/projects
javac PrimeCalculatorDemo.java
java PrimeCalculatorDemo
```

### Chạy Data Processor:
```bash
cd src/main/java/projects
javac DataProcessorDemo.java
java DataProcessorDemo
```

## 📊 Kết quả mong đợi

### Prime Calculator:
```
Single-threaded: ~2000-3000 ms
Multi-threaded:  ~800-1200 ms
Speedup: 2.5-3.5x
```

### Data Processor:
```
Single-threaded: ~500-800 ms
Multi-threaded:  ~200-300 ms
Speedup: 2.0-3.0x
```

## 🎓 Bài học quan trọng

### ✅ Multi-threading hiệu quả khi:
- **CPU-intensive tasks** (tính toán phức tạp)
- **Independent work** (không phụ thuộc lẫn nhau)
- **Large datasets** (nhiều dữ liệu cần xử lý)
- **No I/O blocking** (không chờ đợi)

### ❌ Multi-threading không hiệu quả khi:
- **I/O-bound tasks** (chờ network, file, sleep)
- **Simple calculations** (phép tính đơn giản)
- **Small datasets** (ít dữ liệu)
- **Sequential dependencies** (phụ thuộc tuần tự)

## 🔧 Tối ưu hóa

### Để có speedup tốt nhất:
1. **Chọn số thread phù hợp**: Thường = số CPU cores
2. **Chia công việc đều**: Mỗi thread xử lý ~cùng lượng work
3. **Tránh synchronization overhead**: Dùng ít locks/atomics
4. **Profile trước khi optimize**: Đo lường thực tế

## 🎯 Kết luận

Race Simulator chậm hơn vì:
- Task quá đơn giản (position++)
- Có delay (Thread.sleep)
- Overhead tạo thread > benefit

Demo projects nhanh hơn vì:
- Task phức tạp (tính toán)
- Không có delay
- Benefit > overhead

**→ Multi-threading không phải lúc nào cũng nhanh hơn!**
