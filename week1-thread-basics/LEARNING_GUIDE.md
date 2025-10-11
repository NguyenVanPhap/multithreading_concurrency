# 🎯 Week 1 - Thread Basics Learning Guide

## 📚 Mục tiêu học tập

Sau khi hoàn thành tuần này, bạn sẽ hiểu:
- Thread lifecycle và các trạng thái
- Cách tạo thread (Thread class vs Runnable interface)
- Các method quan trọng: start(), join(), sleep(), yield(), interrupt()
- Race condition là gì và cách giải quyết
- Thread safety và synchronization cơ bản

## 🚀 Hướng dẫn học từng bài

### Bài 1: ThreadCreationDemo.java

**Mục tiêu**: Học cách tạo thread và so sánh các phương pháp

**Các TODO cần hoàn thành**:

1. **Demo 1 - Thread vs Runnable**:
   ```java
   // TODO: Tạo MyThread class kế thừa Thread
   // TODO: Tạo MyRunnable class implement Runnable
   // TODO: Khởi tạo và start cả hai loại thread
   // TODO: Sử dụng join() để đợi thread hoàn thành
   ```

2. **Demo 2 - Thread Methods**:
   ```java
   // TODO: Tạo thread sử dụng Thread.sleep()
   // TODO: Tạo thread sử dụng Thread.yield()
   // TODO: Quan sát sự khác biệt trong hành vi
   ```

3. **Demo 3 - Multiple Threads**:
   ```java
   // TODO: Tạo 10 threads, mỗi thread đếm từ 1-100
   // TODO: In progress mỗi 20 số để giảm output
   // TODO: Sử dụng join() để đợi tất cả threads
   ```

4. **Demo 4 - Lambda Threads**:
   ```java
   // TODO: Tạo thread bằng lambda expression
   // TODO: Thread sleep 1 giây và in message
   ```

**Tips học tập**:
- Quan sát thứ tự output để hiểu thread scheduling
- Thử thay đổi sleep time để thấy sự khác biệt
- So sánh Thread class vs Runnable interface

### Bài 2: ThreadMethodsDemo.java

**Mục tiêu**: Hiểu sâu các method quan trọng của Thread

**Các TODO cần hoàn thành**:

1. **join() method**:
   ```java
   // TODO: Tạo 2 worker threads với thời gian làm việc khác nhau
   // TODO: Đo thời gian tổng cộng
   // TODO: Sử dụng join() để đợi completion
   ```

2. **sleep() method**:
   ```java
   // TODO: Tạo sleeper thread ngủ 3 giây
   // TODO: Tạo monitor thread đếm giây
   // TODO: Quan sát timing
   ```

3. **yield() method**:
   ```java
   // TODO: Tạo greedy thread (không yield)
   // TODO: Tạo polite thread (có yield)
   // TODO: So sánh hành vi
   ```

4. **interrupt() method**:
   ```java
   // TODO: Tạo long-running thread
   // TODO: Tạo interrupter thread
   // TODO: Thực hành graceful interruption
   ```

**Tips học tập**:
- Luôn handle InterruptedException
- Quan sát thread states bằng getState()
- Thử nghiệm với different priorities

### Bài 3: RaceConditionDemo.java

**Mục tiêu**: Hiểu race condition và cách giải quyết

**Các TODO cần hoàn thành**:

1. **Counter class (Race Condition)**:
   ```java
   // TODO: Tạo Counter class với increment() method
   // TODO: Không sử dụng synchronization
   // TODO: Quan sát kết quả không nhất quán
   ```

2. **SynchronizedCounter class**:
   ```java
   // TODO: Thêm synchronized keyword
   // TODO: So sánh kết quả với Counter class
   ```

3. **AtomicCounter class**:
   ```java
   // TODO: Sử dụng AtomicInteger
   // TODO: So sánh performance với synchronized
   ```

**Tips học tập**:
- Chạy nhiều lần để thấy race condition
- Đo performance của các giải pháp khác nhau
- Hiểu tại sao `value++` không thread-safe

### Bài 4: RaceSimulator.java (Mini Project)

**Mục tiêu**: Áp dụng kiến thức vào project thực tế

**Các TODO cần hoàn thành**:

1. **Racer class**:
   ```java
   // TODO: Implement constructor
   // TODO: Implement race() method với random movement
   // TODO: Handle interruption gracefully
   // TODO: Implement getter methods
   ```

2. **Race Logic**:
   ```java
   // TODO: Tạo multiple racers
   // TODO: Start race simulation
   // TODO: Track progress real-time
   // TODO: Determine winner
   ```

**Tips học tập**:
- Sử dụng volatile cho shared variables
- Implement proper interruption handling
- Tạo UI đẹp với progress tracking

## 🔧 Cách chạy và test

### Compile và chạy:
```bash
cd week1-thread-basics

# Compile
mvn compile

# Chạy từng demo
mvn exec:java -Dexec.mainClass="exercises.ThreadCreationDemo"
mvn exec:java -Dexec.mainClass="exercises.ThreadMethodsDemo"
mvn exec:java -Dexec.mainClass="exercises.RaceConditionDemo"
mvn exec:java -Dexec.mainClass="projects.RaceSimulator"

# Chạy tests
mvn test
```

### Debug tips:
- Thêm `System.out.println()` để track thread execution
- Sử dụng thread names để phân biệt threads
- Quan sát thread IDs và states

## 🎯 Checklist hoàn thành

### ThreadCreationDemo:
- [ ] MyThread class hoàn chỉnh
- [ ] MyRunnable class hoàn chỉnh  
- [ ] Thread methods demo hoạt động
- [ ] Multiple threads demo hoạt động
- [ ] Lambda threads demo hoạt động

### ThreadMethodsDemo:
- [ ] join() demo hoạt động
- [ ] sleep() demo hoạt động
- [ ] yield() demo hoạt động
- [ ] interrupt() demo hoạt động
- [ ] Thread states demo hoạt động

### RaceConditionDemo:
- [ ] Counter class gây race condition
- [ ] SynchronizedCounter class thread-safe
- [ ] AtomicCounter class hoạt động
- [ ] Performance comparison hoạt động

### RaceSimulator:
- [ ] Racer class hoàn chỉnh
- [ ] Race simulation chạy được
- [ ] Winner determination hoạt động
- [ ] Performance comparison hoạt động

## 💡 Câu hỏi suy nghĩ

1. Tại sao `value++` không thread-safe?
2. Khi nào nên dùng synchronized vs AtomicInteger?
3. Thread.yield() có đảm bảo thread khác sẽ chạy không?
4. Làm sao để gracefully shutdown threads?
5. Race condition xảy ra ở đâu trong real-world applications?

## 🔗 Tài liệu tham khảo

- [Oracle Thread Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Concurrency in Practice](https://jcip.net/)
- [Thread States Diagram](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.State.html)

---

**Chúc bạn học tập vui vẻ! 🚀**

Nhớ rằng: Multithreading là một kỹ năng quan trọng trong Java development. Hãy thực hành nhiều và đừng ngại thử nghiệm!
