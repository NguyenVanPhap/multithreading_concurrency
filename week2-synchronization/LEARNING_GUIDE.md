# Learning Guide - Week 2: Synchronization

## 🎯 Learning Objectives
Khi hoàn thành tuần này, bạn sẽ có thể:
- Hiểu và sử dụng `synchronized` keyword
- Sử dụng `ReentrantLock` và các advanced features
- Implement thread-safe data structures
- Tránh deadlock trong multi-threaded applications
- Sử dụng Condition cho producer-consumer pattern
- So sánh hiệu năng của các synchronization mechanisms

## 📖 Theory

### 1. Synchronized Keyword

#### Synchronized Methods
```java
public class Counter {
    private int count = 0;
    
    // Synchronized method - locks on 'this'
    public synchronized void increment() {
        count++;
    }
    
    // Synchronized static method - locks on class
    public static synchronized void staticMethod() {
        // ...
    }
}
```

**How it works:**
- Each object has an intrinsic lock (monitor)
- `synchronized` method acquires this lock
- Only one thread can hold the lock at a time
- Automatically released when method exits

#### Synchronized Blocks
```java
public class Counter {
    private final Object lock = new Object();
    private int count = 0;
    
    public void increment() {
        // Synchronized block - locks on specific object
        synchronized (lock) {
            count++;
        }
    }
}
```

**Benefits:**
- Can lock on different objects
- More granular control
- Reduces lock contention

### 2. ReentrantLock

```java
public class Counter {
    private final Lock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // Always unlock in finally!
        }
    }
    
    public boolean incrementWithTimeout() throws InterruptedException {
        if (lock.tryLock(5, TimeUnit.SECONDS)) {
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
```

**Advantages over synchronized:**
- Can try to acquire lock with timeout
- Can be interrupted
- Fairness option
- Multiple conditions per lock

### 3. ReadWriteLock

```java
public class ReadWriteCounter {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private int count = 0;
    
    public int read() {
        readLock.lock();
        try {
            return count; // Multiple readers can read concurrently
        } finally {
            readLock.unlock();
        }
    }
    
    public void write(int value) {
        writeLock.lock();
        try {
            count = value; // Only one writer at a time
        } finally {
            writeLock.unlock();
        }
    }
}
```

**Use cases:**
- Many readers, few writers
- Thread-safe collections implementation
- Cache systems
- Database access patterns

### 4. Condition

```java
public class BoundedQueue<T> {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<T> queue;
    private final int capacity;
    
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await(); // Wait until not full
            }
            queue.offer(item);
            notEmpty.signal(); // Notify consumers
        } finally {
            lock.unlock();
        }
    }
    
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // Wait until not empty
            }
            T item = queue.poll();
            notFull.signal(); // Notify producers
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

## 🏋️ Practice Exercises

### Exercise 1: SyncDemo
**Tasks:**
1. Create `UnsafeCounter` - không có synchronization
2. Create `SynchronizedCounter` - dùng synchronized keyword
3. Create `LockedCounter` - dùng ReentrantLock
4. Run multiple threads incrementing counter
5. Compare results và performance

**Learning points:**
- Race condition
- synchronized vs Lock
- Performance implications

### Exercise 2: LockDemo
**Tasks:**
1. Implement `SharedResource` với ReentrantLock
2. Try acquiring lock with timeout
3. Handle interruption
4. Test fairness

**Learning points:**
- tryLock() với timeout
- Interrupt handling
- Fair vs unfair locks

### Exercise 3: ReadWriteLockDemo
**Tasks:**
1. Create ReadWriteCounter
2. Add many reader threads
3. Add few writer threads
4. Measure performance vs synchronized

**Learning points:**
- Multiple concurrent readers
- Exclusive writer lock
- Performance benefits

## 🔨 Projects

### Project 1: Bank System

**Requirements:**
- Thread-safe BankAccount
- Multiple account operations
- Deadlock prevention
- Transaction logging

**Implementation steps:**
1. Create BankAccount với ReentrantLock
2. Implement deposit và withdraw
3. Implement transfer (needs multiple locks!)
4. Use lock ordering để tránh deadlock
5. Add transaction logging

**Key concepts:**
- Lock ordering (consistent ordering để tránh deadlock)
- tryLock() cho timeout
- Proper unlock trong finally

**Example lock ordering:**
```java
public boolean transfer(BankAccount target, double amount) {
    // Always acquire locks in consistent order
    BankAccount first = accountId < target.accountId ? this : target;
    BankAccount second = accountId < target.accountId ? target : this;
    
    first.lock.lock();
    try {
        second.lock.lock();
        try {
            // Transfer logic
        } finally {
            second.lock.unlock();
        }
    } finally {
        first.lock.unlock();
    }
}
```

### Project 2: Message Queue

**Requirements:**
- Bounded queue
- Thread-safe put và take
- Producer-Consumer pattern
- Graceful shutdown

**Implementation steps:**
1. Create BoundedMessageQueue
2. Use Lock và Condition
3. Implement producer threads
4. Implement consumer threads
5. Add shutdown mechanism

**Key concepts:**
- Condition.await() và signal()
- while loop với condition checking
- Graceful shutdown pattern

## 🐛 Debugging Tips

### Check for deadlock:
```bash
# Thread dump
jstack <pid>

# Analyze for deadlock
# Look for "BLOCKED" threads with same lock
```

### Check lock contention:
```java
ReentrantLock lock = new ReentrantLock(true); // fair
// Enable contention monitoring
// Use JVisualVM to monitor thread contention
```

## 📚 Further Reading
- Oracle Java Concurrency Tutorial
- Effective Java - Item 81: Prefer concurrency utilities to wait and notify
- Java Concurrency in Practice - Brian Goetz

## ✅ Checklist
- [ ] Understand synchronized keyword
- [ ] Can use ReentrantLock
- [ ] Understand ReadWriteLock
- [ ] Can implement producer-consumer
- [ ] Know how to prevent deadlock
- [ ] Can debug synchronization issues
- [ ] Understand when to use each mechanism
