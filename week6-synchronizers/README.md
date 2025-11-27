# Week 6 - Synchronizers

## 📚 Kiến Thức Cần Học
- CountDownLatch - đợi một hoặc nhiều threads hoàn thành
- CyclicBarrier - đồng bộ hóa threads tại một điểm
- Semaphore - kiểm soát số lượng threads truy cập tài nguyên
- Phaser - flexible barrier với phases
- Exchanger - trao đổi dữ liệu giữa 2 threads
- Khi nào dùng synchronizer nào
- So sánh các synchronizers

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `CountDownLatchDemo` → `CyclicBarrierDemo` → `SemaphoreDemo` → `PhaserDemo`
2. **Thực hành với Projects**: Hiểu cách dùng synchronizers trong thực tế
3. **So sánh**: Khi nào dùng synchronizer nào

## 🎯 Bài Tập Tuần 6

### Exercise 1: CountDownLatch Demo
- [ ] Đợi nhiều threads hoàn thành
- [ ] One-time synchronization
- [ ] Use cases

### Exercise 2: CyclicBarrier Demo
- [ ] Đồng bộ threads tại barrier
- [ ] Reusable barrier
- [ ] Barrier action

### Exercise 3: Semaphore Demo
- [ ] Giới hạn số threads truy cập
- [ ] Fair vs unfair semaphore
- [ ] Acquire và release

### Exercise 4: Phaser Demo
- [ ] Multi-phase synchronization
- [ ] Dynamic parties
- [ ] Phase advancement

## 🏆 Mini Projects

### 1. Parallel Processing với Synchronizers
**Mô tả**: Xử lý song song với các synchronizers
- Phân chia công việc
- Đồng bộ hóa kết quả
- Collect results

### 2. Resource Pool với Semaphore
**Mô tả**: Quản lý pool tài nguyên với Semaphore
- Limited resources
- Acquire/release mechanism
- Statistics

## 📁 File Structure
```
week6-synchronizers/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── CountDownLatchDemo.java
    │   │   ├── CyclicBarrierDemo.java
    │   │   ├── SemaphoreDemo.java
    │   │   └── PhaserDemo.java
    │   └── projects/
    │       ├── ParallelProcessor.java
    │       └── ResourcePool.java
    └── test/java/
```

## 🚀 Cách Chạy

```bash
cd week6-synchronizers
mvn compile exec:java -Dexec.mainClass="exercises.CountDownLatchDemo"
```

## 💡 Key Concepts

### 1. CountDownLatch
```java
CountDownLatch latch = new CountDownLatch(3);
// Threads count down
latch.countDown();
// Main thread waits
latch.await();
```

### 2. CyclicBarrier
```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All arrived"));
// Threads wait at barrier
barrier.await();
```

### 3. Semaphore
```java
Semaphore semaphore = new Semaphore(5);
semaphore.acquire();
// Use resource
semaphore.release();
```

## 🚨 Common Pitfalls
1. **CountDownLatch không reusable**: Phải tạo mới mỗi lần
2. **CyclicBarrier reset**: Có thể reuse
3. **Semaphore không release**: Memory leak
4. **Wrong synchronizer choice**: Hiểu use case trước khi chọn

## 🎯 Tips
- CountDownLatch: One-time wait
- CyclicBarrier: Reusable barrier
- Semaphore: Resource limiting
- Phaser: Complex multi-phase

