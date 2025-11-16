# Week 11 - Realtime System

## 📚 Kiến Thức Cần Học
- Real-time system requirements
- Deterministic execution
- Priority scheduling
- Deadline handling
- Rate monotonic scheduling
- Earliest deadline first
- Jitter và latency control
- Real-time Java features

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `PriorityDemo` → `DeadlineDemo` → `SchedulingDemo`
2. **Thực hành với Projects**: Real-time task scheduler, Deadline manager
3. **So sánh**: Different scheduling algorithms

## 🎯 Bài Tập Tuần 11

### Exercise 1: Priority Scheduling
- [ ] Thread priorities
- [ ] Priority inheritance
- [ ] Priority inversion

### Exercise 2: Deadline Management
- [ ] Task với deadlines
- [ ] Deadline monitoring
- [ ] Missed deadline handling

### Exercise 3: Scheduling Algorithms
- [ ] Rate Monotonic Scheduling
- [ ] Earliest Deadline First
- [ ] Comparison

## 🏆 Mini Projects

### 1. Real-Time Task Scheduler
**Mô tả**: Scheduler với deadline support
- Task priorities
- Deadline tracking
- Missed deadline detection

### 2. Deadline Manager
**Mô tả**: Quản lý tasks với deadlines
- Deadline monitoring
- Priority adjustment
- Statistics

## 📁 File Structure
```
week11-realtime-system/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── PriorityDemo.java
    │   │   ├── DeadlineDemo.java
    │   │   └── SchedulingDemo.java
    │   └── projects/
    │       ├── RealTimeScheduler.java
    │       └── DeadlineManager.java
    └── test/java/
```

## 💡 Key Concepts

### 1. Thread Priorities
```java
thread.setPriority(Thread.MAX_PRIORITY);
```

### 2. Deadline
```java
long deadline = System.currentTimeMillis() + timeout;
if (System.currentTimeMillis() > deadline) {
    // missed deadline
}
```

## 🚨 Common Pitfalls
1. **Priority inversion**: High priority blocked by low priority
2. **Ignoring deadlines**: Tasks miss deadlines
3. **Non-deterministic**: JVM không guarantee real-time

## 🎯 Tips
- Java không phải real-time system
- Priorities chỉ là hints
- Use external real-time JVM nếu cần

