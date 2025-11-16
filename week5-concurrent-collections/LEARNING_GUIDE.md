# Learning Guide - Week 5: Concurrent Collections

## 🎯 Mục Tiêu Học Tập

Sau khi hoàn thành tuần này, bạn sẽ:
- Hiểu được ConcurrentHashMap và khi nào dùng
- Nắm vững BlockingQueue và các implementations
- Biết khi nào dùng CopyOnWrite collections
- So sánh được Synchronized vs Concurrent collections
- Áp dụng được vào producer-consumer pattern

## 📖 Lý Thuyết Cần Nắm

### 1. ConcurrentHashMap vs Synchronized HashMap

**Synchronized HashMap:**
- Lock toàn bộ map cho mọi operation
- Sequential access
- Chậm hơn

**ConcurrentHashMap:**
- Lock granular (segment-level)
- Concurrent reads, limited concurrent writes
- Nhanh hơn nhiều

### 2. BlockingQueue Types

- **ArrayBlockingQueue**: Bounded, array-based
- **LinkedBlockingQueue**: Unbounded hoặc bounded, linked-list
- **PriorityBlockingQueue**: Priority-ordered
- **SynchronousQueue**: No capacity, direct handoff

### 3. CopyOnWrite Collections

- **CopyOnWriteArrayList**: Copy array on write
- **CopyOnWriteArraySet**: Based on CopyOnWriteArrayList
- **Use case**: Read-heavy, write-rare scenarios

## 🏃 Hướng Dẫn Thực Hành

Xem README.md cho chi tiết các exercises và projects.

