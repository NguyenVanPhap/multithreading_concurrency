package project;

import java.util.concurrent.*;
import java.util.PriorityQueue;

/**
 * TODO: Implement TaskQueue
 * 
 * Priority queue cho tasks
 * - Thread-safe operations
 * - Blocking operations
 * - Priority ordering
 */
public class TaskQueue {
    
    private final BlockingQueue<Task> queue;
    
    public TaskQueue(int capacity) {
        // TODO: Tạo PriorityBlockingQueue hoặc custom implementation
        this.queue = null; // TODO: Create
    }
    
    /**
     * TODO: Add task to queue
     */
    public void enqueue(Task task) throws InterruptedException {
        // TODO: Put task vào queue (blocking nếu full)
    }
    
    /**
     * TODO: Get task from queue
     */
    public Task dequeue() throws InterruptedException {
        // TODO: Take task từ queue (blocking nếu empty)
        return null; // TODO: Return task
    }
    
    /**
     * TODO: Get queue size
     */
    public int size() {
        // TODO: Return queue size
        return 0; // TODO: Return size
    }
    
    /**
     * TODO: Check if empty
     */
    public boolean isEmpty() {
        // TODO: Return true if empty
        return false; // TODO: Return isEmpty
    }
}

