# Week 3 - Deadlock và Advanced Locks

## 📚 Kiến Thức Cần Học
- Deadlock là gì và cách phát hiện
- Các điều kiện gây ra deadlock (4 điều kiện cần thiết)
- Cách tránh deadlock: Lock Ordering, Timeout, TryLock
- Dining Philosophers Problem (classic deadlock example)
- Lock timeout và interrupt handling
- Resource ordering và lock hierarchy
- Deadlock detection và recovery
- Livelock vs Deadlock vs Starvation

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `DeadlockDemo` → `LockOrderingDemo` → `TimeoutLockDemo`
2. **Thực hành với Projects**: Hiểu cách áp dụng deadlock prevention trong thực tế
3. **Phân tích**: So sánh các kỹ thuật tránh deadlock
4. **Practice**: Implement Dining Philosophers với deadlock prevention

## 🎯 Bài Tập Tuần 3

### Exercise 1: Deadlock Demo
- [ ] Tạo deadlock scenario với 2 threads và 2 locks
- [ ] Phát hiện deadlock bằng thread dump
- [ ] Implement giải pháp tránh deadlock
- [ ] So sánh performance trước và sau khi fix

### Exercise 2: Lock Ordering Demo
- [ ] Implement lock ordering để tránh deadlock
- [ ] So sánh consistent vs inconsistent lock ordering
- [ ] Thực hành với multiple resources
- [ ] Test với nhiều threads

### Exercise 3: Timeout Lock Demo
- [ ] Sử dụng tryLock() với timeout
- [ ] Implement deadlock detection với timeout
- [ ] Handle timeout gracefully
- [ ] So sánh timeout vs blocking lock

## 🏆 Mini Projects

### 1. Dining Philosophers
**Mô tả**: Classic deadlock problem - 5 triết gia ngồi quanh bàn, mỗi người cần 2 đũa để ăn
- Multiple philosophers competing for shared resources (chopsticks)
- Deadlock khi tất cả đều giữ 1 đũa và chờ đũa thứ 2
- Implement các giải pháp: lock ordering, timeout, odd-even strategy

**Features**:
- [ ] Implement Philosopher class với eating/thinking
- [ ] Implement Chopstick resource
- [ ] Tạo deadlock scenario
- [ ] Fix deadlock với lock ordering
- [ ] Fix deadlock với timeout
- [ ] Fix deadlock với odd-even strategy
- [ ] Statistics tracking (eating time, waiting time)

**API Requirements**:
```java
public class Philosopher {
    public Philosopher(int id, Chopstick left, Chopstick right);
    public void start();
    public void stop();
    public int getEatCount();
    public long getTotalEatingTime();
}

public class Chopstick {
    public Chopstick(int id);
    public boolean tryPickUp(long timeoutMs) throws InterruptedException;
    public void putDown();
    public int getId();
}
```

### 2. Resource Manager
**Mô tả**: Quản lý tài nguyên với deadlock prevention
- Multiple threads yêu cầu multiple resources
- Deadlock prevention với lock ordering
- Timeout và retry mechanism
- Resource allocation tracking

**Features**:
- [ ] ResourceManager với multiple resources
- [ ] Thread-safe resource allocation
- [ ] Lock ordering để tránh deadlock
- [ ] Timeout cho resource requests
- [ ] Deadlock detection và recovery
- [ ] Statistics và monitoring

**API Requirements**:
```java
public class ResourceManager {
    public ResourceManager(int numResources);
    public boolean acquireResources(List<Integer> resourceIds, long timeoutMs) throws InterruptedException;
    public void releaseResources(List<Integer> resourceIds);
    public boolean isDeadlockDetected();
    public Map<Integer, Thread> getResourceOwners();
}
```

## 📁 File Structure
```
week3-deadlock-locks/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── DeadlockDemo.java
    │   │   ├── LockOrderingDemo.java
    │   │   └── TimeoutLockDemo.java
    │   └── projects/
    │       ├── DiningPhilosophers.java
    │       └── ResourceManager.java
    └── test/java/
        └── DeadlockDemoTest.java
```

## 🚀 Cách Chạy

### Sử dụng Maven:
```bash
cd week3-deadlock-locks

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.DeadlockDemo"
mvn compile exec:java -Dexec.mainClass="exercises.LockOrderingDemo"
mvn compile exec:java -Dexec.mainClass="exercises.TimeoutLockDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.DiningPhilosophers"
mvn compile exec:java -Dexec.mainClass="projects.ResourceManager"
```

### Sử dụng batch file (Windows):
```bash
cd week3-deadlock-locks
run_demo.bat
```

### Chạy trực tiếp với Java:
```bash
cd week3-deadlock-locks/src/main/java

# Compile
javac exercises/*.java projects/*.java

# Run
java exercises.DeadlockDemo
java projects.DiningPhilosophers
```

## 🔍 Deadlock Detection

### 4 Điều Kiện Cần Thiết Cho Deadlock:
1. **Mutual Exclusion**: Tài nguyên không thể chia sẻ
2. **Hold and Wait**: Thread giữ tài nguyên và chờ tài nguyên khác
3. **No Preemption**: Không thể lấy tài nguyên từ thread đang giữ
4. **Circular Wait**: Chuỗi vòng tròn các thread chờ nhau

### Cách Phát Hiện Deadlock:
```bash
# Thread dump khi deadlock
jstack <pid>

# Hoặc trong code
ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = threadMX.findDeadlockedThreads();
```

## 💡 Key Concepts

### 1. Lock Ordering (Consistent Ordering)
```java
// ❌ BAD: Inconsistent ordering → Deadlock
Thread 1: lock A, then lock B
Thread 2: lock B, then lock A

// ✅ GOOD: Consistent ordering
Thread 1: lock A, then lock B
Thread 2: lock A, then lock B
```

### 2. Timeout Locks
```java
Lock lock1 = new ReentrantLock();
Lock lock2 = new ReentrantLock();

if (lock1.tryLock(5, TimeUnit.SECONDS)) {
    try {
        if (lock2.tryLock(5, TimeUnit.SECONDS)) {
            try {
                // critical section
            } finally {
                lock2.unlock();
            }
        } else {
            // timeout - release first lock
        }
    } finally {
        lock1.unlock();
    }
}
```

### 3. Lock Ordering với ID
```java
// Sort resource IDs để đảm bảo consistent ordering
List<Integer> resourceIds = Arrays.asList(3, 1, 2);
Collections.sort(resourceIds); // [1, 2, 3]

for (Integer id : resourceIds) {
    locks.get(id).lock();
}
```

### 4. Odd-Even Strategy (Dining Philosophers)
```java
// Philosophers với ID lẻ: left then right
// Philosophers với ID chẵn: right then left
if (id % 2 == 0) {
    right.lock();
    left.lock();
} else {
    left.lock();
    right.lock();
}
```

## 🚨 Common Pitfalls
1. **Inconsistent lock ordering**: Dễ dẫn đến deadlock
2. **Nested locks**: Luôn acquire theo cùng một thứ tự
3. **Forgetting timeout**: Blocking forever khi deadlock
4. **Not releasing locks**: Memory leak và deadlock
5. **Locking too many resources**: Tăng khả năng deadlock
6. **Not handling InterruptedException**: Thread không thể dừng

## 🎯 Tips
- Luôn acquire locks theo cùng một thứ tự (lock ordering)
- Sử dụng timeout cho locks (tryLock với timeout)
- Giảm số lượng locks cần thiết
- Sử dụng lock-free data structures khi có thể
- Monitor thread dumps để phát hiện deadlock
- Test với nhiều threads và scenarios
- Implement deadlock detection và recovery

## 📊 So Sánh Các Kỹ Thuật Tránh Deadlock

| Kỹ Thuật | Ưu Điểm | Nhược Điểm | Khi Nào Dùng |
|----------|---------|------------|--------------|
| **Lock Ordering** | Đơn giản, hiệu quả | Cần biết trước tất cả resources | Khi có thể sort resources |
| **Timeout** | Phát hiện deadlock, có thể recover | Phức tạp hơn, có thể retry | Khi cần resilience |
| **TryLock** | Non-blocking, flexible | Cần handle failure | Khi không muốn block |
| **Odd-Even** | Đơn giản cho DP problem | Chỉ áp dụng cho một số cases | Dining Philosophers |
| **Single Lock** | Không có deadlock | Giảm concurrency | Khi không cần fine-grained |

