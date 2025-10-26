# Week 2 - Synchronization

## 📚 Kiến Thức Cần Học
- Synchronized keyword (method-level và block-level)
- Lock interface và ReentrantLock
- ReadWriteLock và StampedLock
- Condition trong Lock API
- Producer-Consumer pattern
- Thread-safe collections
- Deadlock và cách tránh deadlock
- Fairness trong Lock
- Lock timeout và interrupt handling

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `SyncDemo` → `LockDemo` → `ReadWriteLockDemo`
2. **Thực hành với Projects**: Hiểu cách dùng synchronization trong thực tế
3. **So sánh performance**: synchronized vs ReentrantLock vs ReadWriteLock
4. **Practice**: Implement thread-safe banking system

## 🎯 Bài Tập Tuần 2

### Exercise 1: Sync Demo
- [ ] Tạo class UnsafeCounter với race condition
- [ ] Implement SynchronizedCounter với synchronized keyword
- [ ] So sánh kết quả và performance
- [ ] Thực hành synchronized method và synchronized block

### Exercise 2: Lock Demo
- [ ] Implement SharedResource với ReentrantLock
- [ ] So sánh ReentrantLock với synchronized
- [ ] Thực hành tryLock() và lockInterruptibly()
- [ ] Fairness trong Lock

### Exercise 3: ReadWriteLock Demo
- [ ] Implement ReadWriteCounter
- [ ] So sánh ReadLock và WriteLock
- [ ] Thấy được ReadWriteLock hiệu quả khi nhiều reader, ít writer
- [ ] Practice upgrading/downgrading locks

## 🏆 Mini Projects

### 1. Bank System
**Mô tả**: Hệ thống ngân hàng thread-safe
- Multi-threaded banking operations
- Thread-safe account management
- Concurrent deposits và withdrawals
- Race condition protection

**Features**:
- [ ] Thread-safe BankAccount với ReentrantLock
- [ ] Transfer operation giữa các accounts
- [ ] Transaction logging
- [ ] Deadlock prevention (lock ordering)
- [ ] Withdrawal với timeout và cancellation
- [ ] Fairness testing

**API Requirements**:
```java
public class BankAccount {
    public BankAccount(String accountId, double initialBalance);
    public void deposit(double amount) throws IllegalArgumentException;
    public boolean withdraw(double amount) throws InterruptedException;
    public boolean transfer(BankAccount target, double amount) throws InterruptedException;
    public double getBalance();
    public String getAccountId();
}
```

### 2. Message Queue
**Mô tả**: Thread-safe message queue với Producer-Consumer pattern
- Multiple producers và consumers
- Bounded queue với blocking operations
- Condition để signal producer/consumer
- Graceful shutdown

**Features**:
- [ ] BoundedMessageQueue với capacity limit
- [ ] Producer threads thêm messages
- [ ] Consumer threads lấy messages
- [ ] Blocking khi queue full/empty
- [ ] Graceful shutdown mechanism
- [ ] Statistics tracking

**API Requirements**:
```java
public class BoundedMessageQueue<T> {
    public BoundedMessageQueue(int capacity);
    public void put(T message) throws InterruptedException;
    public T take() throws InterruptedException;
    public int size();
    public boolean isEmpty();
    public void shutdown();
}
```

## 📁 File Structure
```
week2-synchronization/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── SyncDemo.java
    │   │   ├── LockDemo.java
    │   │   └── ReadWriteLockDemo.java
    │   └── projects/
    │       ├── BankSystem.java
    │       └── MessageQueue.java
    └── test/java/
        └── BankSystemTest.java
```

## 🚀 Cách Chạy

### Sử dụng Maven:
```bash
cd week2-synchronization

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.SyncDemo"
mvn compile exec:java -Dexec.mainClass="exercises.LockDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ReadWriteLockDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.BankSystem"
mvn compile exec:java -Dexec.mainClass="projects.MessageQueue"
```

### Sử dụng batch file (Windows):
```bash
cd week2-synchronization
run_demo.bat
```

### Chạy trực tiếp với Java:
```bash
cd week2-synchronization/src/main/java

# Compile
javac exercises/*.java projects/*.java

# Run
java exercises.SyncDemo
java projects.BankSystem
java projects.MessageQueue
```

## 📊 Synchronization Mechanisms Comparison

| Mechanism | Pros | Cons | When to Use |
|-----------|------|------|-------------|
| **synchronized** | Simple, built-in, automatic unlock | No timeout, no tryLock, coarse-grained | Simple locks, method-level |
| **ReentrantLock** | Timeout, tryLock, fair locks, interruptible | Manual lock/unlock, more code | Need advanced features |
| **ReadWriteLock** | Multiple readers, lock downgrade | Complex, potential writer starvation | Read-heavy workloads |
| **StampedLock** | Optimistic reads, best performance | Complex API, not reentrant | Optimistic reading scenarios |

## 💡 Key Concepts

### 1. synchronized keyword
```java
// Method-level synchronization
public synchronized void method() { }

// Block-level synchronization
synchronized (lock) { }

// Static synchronization
public static synchronized void method() { }
```

### 2. ReentrantLock
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}

// With timeout
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
}
```

### 3. ReadWriteLock
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();

// Multiple threads can read concurrently
readLock.lock();
try {
    // read operation
} finally {
    readLock.unlock();
}

// Only one thread can write
writeLock.lock();
try {
    // write operation
} finally {
    writeLock.unlock();
}
```

### 4. Condition với Lock
```java
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();

lock.lock();
try {
    while (!conditionMet) {
        condition.await(); // Wait for condition
    }
    // Do work
    condition.signal(); // Notify waiting threads
} finally {
    lock.unlock();
}
```

## 🚨 Common Pitfalls
1. **Forgetting to unlock**: Luôn dùng try-finally với Lock
2. **Nested locks**: Dễ dẫn đến deadlock
3. **Too many synchronized blocks**: Performance degradation
4. **Not understanding fairness**: Fair locks có thể chậm hơn
5. **Ignoring interruption**: Luôn handle InterruptedException
6. **Race conditions**: Sử dụng volatile cho visibility, locks cho atomicity

## 🎯 Tips
- Dùng synchronized cho simple cases
- Dùng ReentrantLock khi cần timeout hoặc tryLock
- Dùng ReadWriteLock khi read-heavy
- Luôn unlock trong finally block
- Kiểm tra deadlock với lock ordering
- Fair locks có thể chậm hơn nhưng đảm bảo fairness
- Monitor thread dump để debug deadlock
