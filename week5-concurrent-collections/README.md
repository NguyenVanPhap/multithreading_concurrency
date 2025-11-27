# Week 5 - Concurrent Collections

## 📚 Kiến Thức Cần Học
- ConcurrentHashMap và khi nào dùng
- BlockingQueue và các implementations
- CopyOnWriteArrayList và CopyOnWriteArraySet
- ConcurrentLinkedQueue và ConcurrentLinkedDeque
- ConcurrentSkipListMap và ConcurrentSkipListSet
- Synchronized collections vs Concurrent collections
- Performance characteristics
- Thread-safe collections best practices

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `ConcurrentMapDemo` → `BlockingQueueDemo` → `CopyOnWriteDemo`
2. **Thực hành với Projects**: Hiểu cách dùng concurrent collections trong thực tế
3. **So sánh performance**: Synchronized vs Concurrent collections
4. **Practice**: Implement producer-consumer với BlockingQueue

## 🎯 Bài Tập Tuần 5

### Exercise 1: ConcurrentMap Demo
- [ ] Sử dụng ConcurrentHashMap
- [ ] So sánh với synchronized HashMap
- [ ] Atomic operations (putIfAbsent, replace, etc.)
- [ ] Performance comparison

### Exercise 2: BlockingQueue Demo
- [ ] ArrayBlockingQueue
- [ ] LinkedBlockingQueue
- [ ] PriorityBlockingQueue
- [ ] SynchronousQueue
- [ ] Producer-Consumer pattern

### Exercise 3: CopyOnWrite Demo
- [ ] CopyOnWriteArrayList
- [ ] CopyOnWriteArraySet
- [ ] Use cases và trade-offs

## 🏆 Mini Projects

### 1. Producer-Consumer với BlockingQueue
**Mô tả**: Classic producer-consumer pattern
- Multiple producers và consumers
- Thread-safe queue
- Graceful shutdown

**Features**:
- [ ] Producer threads
- [ ] Consumer threads
- [ ] BlockingQueue
- [ ] Statistics tracking
- [ ] Graceful shutdown

### 2. Concurrent Cache
**Mô tả**: Thread-safe cache với ConcurrentHashMap
- Cache với expiration
- Thread-safe operations
- Performance optimization

**Features**:
- [ ] ConcurrentHashMap-based cache
- [ ] Expiration mechanism
- [ ] Thread-safe get/put
- [ ] Statistics

## 📁 File Structure
```
week5-concurrent-collections/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── ConcurrentMapDemo.java
    │   │   ├── BlockingQueueDemo.java
    │   │   └── CopyOnWriteDemo.java
    │   └── projects/
    │       ├── ProducerConsumer.java
    │       └── ConcurrentCache.java
    └── test/java/
```

## 🚀 Cách Chạy

```bash
cd week5-concurrent-collections
mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapDemo"
```

## 💡 Key Concepts

### 1. ConcurrentHashMap
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.putIfAbsent("key", 2); // Only if absent
map.compute("key", (k, v) -> v + 1);
```

### 2. BlockingQueue
```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
queue.put("item"); // Block if full
String item = queue.take(); // Block if empty
```

### 3. CopyOnWriteArrayList
```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
// Read operations are fast, writes create new array
```

## 🚨 Common Pitfalls
1. **Using synchronized collections**: Slower than concurrent collections
2. **Not understanding BlockingQueue capacity**: Unbounded can cause OOM
3. **CopyOnWrite for frequent writes**: Very expensive
4. **Not using atomic operations**: Race conditions

## 🎯 Tips
- Use ConcurrentHashMap for concurrent access
- Use BlockingQueue for producer-consumer
- CopyOnWrite for read-heavy, write-rare scenarios
- Understand performance trade-offs

