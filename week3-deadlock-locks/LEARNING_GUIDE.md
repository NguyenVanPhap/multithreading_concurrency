# Learning Guide - Week 3: Deadlock và Advanced Locks

## 🎯 Mục Tiêu Học Tập

Sau khi hoàn thành tuần này, bạn sẽ:
- Hiểu được deadlock là gì và 4 điều kiện cần thiết
- Biết cách phát hiện deadlock trong code
- Nắm vững các kỹ thuật tránh deadlock: lock ordering, timeout, odd-even
- Áp dụng được vào các bài toán thực tế như Dining Philosophers
- Hiểu được sự khác biệt giữa deadlock, livelock và starvation

## 📖 Lý Thuyết Cần Nắm

### 1. Deadlock là gì?

Deadlock xảy ra khi 2 hoặc nhiều threads bị block vĩnh viễn, chờ đợi nhau giải phóng tài nguyên.

**Ví dụ đơn giản:**
```
Thread 1: Giữ lock A, chờ lock B
Thread 2: Giữ lock B, chờ lock A
→ Deadlock! Cả 2 đều không thể tiếp tục
```

### 2. 4 Điều Kiện Cần Thiết Cho Deadlock

1. **Mutual Exclusion**: Tài nguyên không thể chia sẻ đồng thời
2. **Hold and Wait**: Thread giữ tài nguyên và chờ tài nguyên khác
3. **No Preemption**: Không thể lấy tài nguyên từ thread đang giữ
4. **Circular Wait**: Chuỗi vòng tròn các thread chờ nhau

**Chỉ cần phá vỡ 1 trong 4 điều kiện là tránh được deadlock!**

### 3. Các Kỹ Thuật Tránh Deadlock

#### a) Lock Ordering (Consistent Ordering)
- **Ý tưởng**: Luôn acquire locks theo cùng một thứ tự
- **Cách làm**: Sort resource IDs trước khi acquire
- **Ưu điểm**: Đơn giản, hiệu quả
- **Nhược điểm**: Cần biết trước tất cả resources

```java
// ❌ BAD: Inconsistent ordering
Thread 1: lock A → lock B
Thread 2: lock B → lock A  // Deadlock!

// ✅ GOOD: Consistent ordering
Thread 1: lock A → lock B
Thread 2: lock A → lock B  // Safe!
```

#### b) Timeout Locks
- **Ý tưởng**: Sử dụng tryLock với timeout
- **Cách làm**: Nếu timeout, release tất cả locks đã acquire và retry
- **Ưu điểm**: Phát hiện deadlock, có thể recover
- **Nhược điểm**: Phức tạp hơn, có thể retry nhiều lần

```java
if (lock1.tryLock(5, TimeUnit.SECONDS)) {
    try {
        if (lock2.tryLock(5, TimeUnit.SECONDS)) {
            try {
                // critical section
            } finally {
                lock2.unlock();
            }
        } else {
            // Timeout - release first lock
            lock1.unlock();
        }
    } finally {
        if (acquired) {
            lock1.unlock();
        }
    }
}
```

#### c) Odd-Even Strategy (Dining Philosophers)
- **Ý tưởng**: Philosophers với ID lẻ và chẵn acquire theo thứ tự khác nhau
- **Cách làm**: 
  - Odd: left → right
  - Even: right → left
- **Ưu điểm**: Đơn giản cho DP problem
- **Nhược điểm**: Chỉ áp dụng cho một số cases cụ thể

```java
if (id % 2 == 0) {
    right.lock();
    left.lock();
} else {
    left.lock();
    right.lock();
}
```

## 🏃 Hướng Dẫn Thực Hành

### Bước 1: DeadlockDemo.java

**Mục tiêu**: Hiểu deadlock và cách phát hiện

1. **testDeadlock()**: 
   - TODO: Tạo 2 threads với lock ordering ngược nhau
   - Quan sát deadlock xảy ra
   - Threads sẽ không bao giờ hoàn thành

2. **testDeadlockDetection()**:
   - TODO: Sử dụng ThreadMXBean.findDeadlockedThreads()
   - Phát hiện deadlock trong runtime
   - In ra thread names bị deadlock

3. **testDeadlockPrevention()**:
   - TODO: Sửa lại để tất cả threads acquire locks theo cùng thứ tự
   - Quan sát threads hoàn thành thành công

**Tips**:
- Dùng `jstack <pid>` để xem thread dump khi deadlock
- ThreadMXBean là công cụ mạnh để detect deadlock programmatically

### Bước 2: LockOrderingDemo.java

**Mục tiêu**: Thực hành lock ordering với multiple resources

1. **testInconsistentOrdering()**:
   - TODO: Random resource IDs, không sort
   - Sử dụng tryLock với timeout để tránh block forever
   - Quan sát một số threads timeout (possible deadlock)

2. **testConsistentOrdering()**:
   - TODO: Sort resource IDs trước khi acquire
   - Sử dụng blocking lock (an toàn với consistent ordering)
   - Tất cả threads đều thành công

3. **testMultipleResources()**:
   - TODO: Request 2-4 resources ngẫu nhiên
   - Sort và acquire theo thứ tự
   - Test với nhiều threads

**Tips**:
- Luôn sort resource IDs trước khi acquire
- Release locks theo reverse order
- Dùng Collections.sort() cho đơn giản

### Bước 3: TimeoutLockDemo.java

**Mục tiêu**: Sử dụng timeout để tránh deadlock

1. **testTimeoutLock()**:
   - TODO: tryLock với timeout
   - Quan sát timeout khi lock bị giữ lâu

2. **testDeadlockDetectionWithTimeout()**:
   - TODO: Sử dụng timeout để detect deadlock
   - Release locks khi timeout
   - Tránh block forever

3. **testGracefulTimeout()**:
   - TODO: Implement retry mechanism
   - Handle timeout gracefully
   - Retry với backoff nếu cần

4. **testPerformanceComparison()**:
   - TODO: So sánh blocking lock vs timeout lock
   - Quan sát overhead của timeout

**Tips**:
- Timeout locks có overhead nhỏ so với blocking locks
- Luôn release locks khi timeout
- Có thể implement exponential backoff cho retry

### Bước 4: DiningPhilosophers.java

**Mục tiêu**: Áp dụng các kỹ thuật vào bài toán thực tế

1. **Philosopher class**:
   - TODO: Implement eat() với các strategies
   - Track statistics (eatCount, totalEatingTime)

2. **Chopstick class**:
   - TODO: Implement tryPickUp() với timeout
   - Implement putDown()

3. **Test các solutions**:
   - Deadlock scenario: Tất cả left → right
   - Lock ordering: Sort by ID
   - Timeout: tryLock với timeout
   - Odd-even: Different order cho odd/even

**Tips**:
- Dining Philosophers là classic deadlock problem
- So sánh số lượng meals giữa các solutions
- Odd-even strategy thường hiệu quả nhất cho DP

### Bước 5: ResourceManager.java

**Mục tiêu**: Quản lý tài nguyên với deadlock prevention

1. **Manager class**:
   - TODO: acquireResources() với lock ordering
   - TODO: releaseResources() theo reverse order
   - TODO: Track resource owners

2. **Test scenarios**:
   - Basic allocation: Random resources
   - Lock ordering: Sort resources
   - Timeout: Short timeout
   - Deadlock detection: ThreadMXBean

**Tips**:
- Sort resource IDs trong acquireResources()
- Release theo reverse order
- Track owners để debug
- Sử dụng ConcurrentHashMap cho thread-safe tracking

## 🐛 Common Mistakes

1. **Unlock trong finally khi chưa acquire**:
   ```java
   // ❌ BAD
   lock.lock();
   try {
       // ...
   } finally {
       lock.unlock(); // Always unlock, even if not acquired
   }
   
   // ✅ GOOD
   boolean acquired = false;
   if (lock.tryLock()) {
       acquired = true;
       try {
           // ...
       } finally {
           if (acquired) {
               lock.unlock();
           }
       }
   }
   ```

2. **Không sort resources**:
   ```java
   // ❌ BAD: Random order
   for (Integer id : resourceIds) {
       locks.get(id).lock();
   }
   
   // ✅ GOOD: Sorted order
   Collections.sort(resourceIds);
   for (Integer id : resourceIds) {
       locks.get(id).lock();
   }
   ```

3. **Unlock sai thứ tự**:
   ```java
   // ❌ BAD: Same order
   lock1.unlock();
   lock2.unlock();
   
   // ✅ GOOD: Reverse order
   lock2.unlock();
   lock1.unlock();
   ```

## 📊 So Sánh Các Solutions

| Solution | Complexity | Performance | Deadlock Prevention | Use Case |
|----------|-----------|-------------|---------------------|----------|
| Lock Ordering | Low | High | Excellent | When resources can be sorted |
| Timeout | Medium | Medium | Good | When need resilience |
| Odd-Even | Low | High | Good | Dining Philosophers |
| Single Lock | Low | Low | Perfect | When fine-grained not needed |

## 🎓 Bài Tập Thêm

1. **Implement livelock scenario**: Threads liên tục retry nhưng không tiến bộ
2. **Implement starvation scenario**: Một thread không bao giờ được acquire lock
3. **Deadlock recovery**: Detect và recover từ deadlock
4. **Lock-free solution**: Sử dụng atomic operations thay vì locks

## 📚 Tài Liệu Tham Khảo

- Java Concurrency in Practice - Chapter 10: Avoiding Liveness Hazards
- Oracle Java Tutorial: Deadlock
- Java Thread Dump Analysis

## ✅ Checklist Hoàn Thành

- [ ] Hiểu được 4 điều kiện deadlock
- [ ] Implement được deadlock scenario
- [ ] Phát hiện được deadlock với ThreadMXBean
- [ ] Implement lock ordering solution
- [ ] Implement timeout solution
- [ ] Implement odd-even strategy
- [ ] Hoàn thành Dining Philosophers
- [ ] Hoàn thành Resource Manager
- [ ] So sánh được các solutions
- [ ] Hiểu được khi nào dùng solution nào

Chúc bạn học tốt! 🚀

