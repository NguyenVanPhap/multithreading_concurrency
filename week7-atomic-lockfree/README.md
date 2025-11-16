# Week 7 - Atomic và Lock-free

## 📚 Kiến Thức Cần Học
- AtomicInteger, AtomicLong, AtomicBoolean
- AtomicReference và AtomicStampedReference
- AtomicArray classes
- Compare-and-Swap (CAS) operations
- Lock-free algorithms
- ABA problem và giải pháp
- Performance comparison: Atomic vs Synchronized vs Lock

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `AtomicDemo` → `AtomicReferenceDemo` → `LockFreeDemo`
2. **Thực hành với Projects**: Implement lock-free data structures
3. **So sánh performance**: Atomic vs Synchronized

## 🎯 Bài Tập Tuần 7

### Exercise 1: Atomic Demo
- [ ] AtomicInteger operations
- [ ] AtomicLong operations
- [ ] AtomicBoolean operations
- [ ] Compare-and-Swap

### Exercise 2: AtomicReference Demo
- [ ] AtomicReference
- [ ] AtomicStampedReference (tránh ABA problem)
- [ ] Compare-and-Set operations

### Exercise 3: Lock-Free Demo
- [ ] Lock-free counter
- [ ] Lock-free stack
- [ ] Lock-free queue

## 🏆 Mini Projects

### 1. Lock-Free Stack
**Mô tả**: Implement lock-free stack
- CAS-based operations
- Thread-safe push/pop
- Performance testing

### 2. Lock-Free Counter
**Mô tả**: High-performance counter
- Atomic operations
- Performance comparison
- Statistics

## 📁 File Structure
```
week7-atomic-lockfree/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── AtomicDemo.java
    │   │   ├── AtomicReferenceDemo.java
    │   │   └── LockFreeDemo.java
    │   └── projects/
    │       ├── LockFreeStack.java
    │       └── LockFreeCounter.java
    └── test/java/
```

## 💡 Key Concepts

### 1. Atomic Operations
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
counter.compareAndSet(0, 1);
```

### 2. CAS (Compare-And-Swap)
```java
int expected = counter.get();
int newValue = expected + 1;
while (!counter.compareAndSet(expected, newValue)) {
    expected = counter.get();
    newValue = expected + 1;
}
```

## 🚨 Common Pitfalls
1. **ABA problem**: Dùng AtomicStampedReference
2. **CAS loops**: Có thể spin nhiều lần
3. **Not understanding memory model**: Visibility guarantees

## 🎯 Tips
- Atomic operations: Fast, lock-free
- CAS: Optimistic concurrency
- Lock-free: Better performance, more complex

