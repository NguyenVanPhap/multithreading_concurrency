# Week 14 - ConcurrentHashMap + ExecutorService (Advanced)

## 📚 Mô Tả

Project nâng cao kết hợp **ConcurrentHashMap** và **ExecutorService** để xây dựng hệ thống xử lý dữ liệu phân tán và cache management.

## 🎯 Mục Tiêu Học Tập

- Kết hợp ConcurrentHashMap với ExecutorService
- Xây dựng distributed cache system
- Concurrent data aggregation
- Thread-safe map operations với parallel processing
- Performance optimization

## 🏆 Projects

### 1. Distributed Cache Manager (Khó)
**Mô tả**: Hệ thống cache phân tán với concurrent access
- Multi-threaded cache operations
- Background cache warming
- Cache expiration và cleanup
- Statistics tracking
- Concurrent read/write operations

**Features**:
- [ ] ConcurrentHashMap cho cache storage
- [ ] ExecutorService cho background tasks
- [ ] Cache warming với parallel loading
- [ ] TTL (Time To Live) expiration
- [ ] Background cleanup thread
- [ ] Cache statistics (hit rate, miss rate)
- [ ] Thread-safe operations

### 2. Concurrent Data Aggregator (Khó)
**Mô tả**: Hệ thống tổng hợp dữ liệu từ nhiều nguồn
- Parallel data collection
- Concurrent aggregation vào map
- Real-time statistics
- Data merging strategies

**Features**:
- [ ] ConcurrentHashMap cho data storage
- [ ] ExecutorService cho parallel collection
- [ ] Atomic operations (compute, merge)
- [ ] Real-time aggregation
- [ ] Data consistency guarantees
- [ ] Performance metrics

## 📁 File Structure
```
week14-concurrentmap-executor/
├── README.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── ConcurrentMapExecutorDemo.java
    │   │   └── MapAggregationDemo.java
    │   └── projects/
    │       ├── DistributedCacheManager.java
    │       └── ConcurrentDataAggregator.java
    └── test/java/
```

## 🚀 Cách Chạy

```bash
cd week14-concurrentmap-executor

# Chạy exercises
mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapExecutorDemo"
mvn compile exec:java -Dexec.mainClass="exercises.MapAggregationDemo"

# Chạy projects
mvn compile exec:java -Dexec.mainClass="projects.DistributedCacheManager"
mvn compile exec:java -Dexec.mainClass="projects.ConcurrentDataAggregator"
```

## 💡 Key Concepts

### 1. ConcurrentHashMap với ExecutorService
```java
ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
ExecutorService executor = Executors.newFixedThreadPool(10);

// Parallel cache operations
executor.submit(() -> cache.put("key1", "value1"));
executor.submit(() -> cache.put("key2", "value2"));
```

### 2. Atomic Operations
```java
cache.compute("key", (k, v) -> v == null ? "default" : v + "updated");
cache.merge("key", "value", (old, newVal) -> old + newVal);
```

### 3. Parallel Processing
```java
List<CompletableFuture<Void>> futures = keys.stream()
    .map(key -> CompletableFuture.runAsync(() -> 
        processKey(cache, key), executor))
    .collect(Collectors.toList());
```

## 🚨 Common Pitfalls
1. **Not using atomic operations**: Race conditions với get-then-put
2. **Memory leaks**: Không cleanup expired entries
3. **Thread pool sizing**: Quá nhỏ hoặc quá lớn
4. **Not handling exceptions**: Exceptions trong compute functions
5. **Concurrent modifications**: Iterating while modifying

