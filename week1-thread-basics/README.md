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

## 💡 Tips
- Dùng Thread.sleep() để simulate movement delay
- Random sử dụng java.util.Random hoặc ThreadLocalRandom
- Thread.join() để đợi tất cả threads hoàn thành
- Thread.interrupt() để dừng thread gracefully
