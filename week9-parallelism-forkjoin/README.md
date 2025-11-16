# Week 9 - Parallelism và ForkJoin

## 📚 Kiến Thức Cần Học
- ForkJoinPool và work-stealing algorithm
- RecursiveTask và RecursiveAction
- Parallel Streams
- Spliterator
- Divide-and-conquer algorithms
- Performance tuning
- When to use ForkJoin vs ExecutorService

## 🎓 Learning Path
1. **Bắt đầu với Exercises**: `ForkJoinDemo` → `RecursiveTaskDemo` → `ParallelStreamDemo`
2. **Thực hành với Projects**: Parallel algorithms, Data processing
3. **So sánh**: ForkJoin vs ExecutorService vs Parallel Streams

## 🎯 Bài Tập Tuần 9

### Exercise 1: ForkJoin Demo
- [ ] ForkJoinPool basics
- [ ] Work-stealing
- [ ] Performance characteristics

### Exercise 2: RecursiveTask Demo
- [ ] Implement RecursiveTask
- [ ] Divide-and-conquer
- [ ] Result computation

### Exercise 3: Parallel Streams
- [ ] Parallel stream operations
- [ ] Performance comparison
- [ ] Common pitfalls

## 🏆 Mini Projects

### 1. Parallel Array Sum
**Mô tả**: Tính tổng array song song
- ForkJoin approach
- Parallel stream approach
- Performance comparison

### 2. Parallel Merge Sort
**Mô tả**: Merge sort song song
- RecursiveTask implementation
- Performance testing

## 📁 File Structure
```
week9-parallelism-forkjoin/
├── README.md
├── LEARNING_GUIDE.md
├── pom.xml
├── run_demo.bat
└── src/
    ├── main/java/
    │   ├── exercises/
    │   │   ├── ForkJoinDemo.java
    │   │   ├── RecursiveTaskDemo.java
    │   │   └── ParallelStreamDemo.java
    │   └── projects/
    │       ├── ParallelArraySum.java
    │       └── ParallelMergeSort.java
    └── test/java/
```

## 💡 Key Concepts

### 1. ForkJoinPool
```java
ForkJoinPool pool = ForkJoinPool.commonPool();
pool.invoke(task);
```

### 2. RecursiveTask
```java
class MyTask extends RecursiveTask<Integer> {
    protected Integer compute() {
        if (small enough) return solve directly;
        else {
            split task;
            fork();
            join();
        }
    }
}
```

### 3. Parallel Streams
```java
list.parallelStream()
    .map(x -> x * 2)
    .reduce(0, Integer::sum);
```

## 🚨 Common Pitfalls
1. **Too fine-grained tasks**: Overhead lớn hơn benefit
2. **Not using threshold**: Tasks quá nhỏ
3. **Shared mutable state**: Race conditions

## 🎯 Tips
- ForkJoin: Divide-and-conquer, CPU-intensive
- Parallel Streams: Data processing
- Threshold: Balance task size

