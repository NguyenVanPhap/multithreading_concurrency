package project;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO: Implement WorkerPool
 * 
 * Thread pool management
 * - Worker assignment
 * - Load balancing
 * - Task execution
 */
public class WorkerPool {
    
    private final ExecutorService executor;
    private final TaskQueue taskQueue;
    private final AtomicInteger activeWorkers;
    
    public WorkerPool(int poolSize, TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
        this.executor = null; // TODO: Create ExecutorService với poolSize
        this.activeWorkers = null; // TODO: Create AtomicInteger
    }
    
    public void start() {
        // TODO: Start workers
        // TODO: Workers lấy tasks từ queue và execute
    }
    
    public void shutdown() {
        // TODO: Shutdown executor
        // TODO: Wait for tasks to complete
    }
    
    public int getActiveWorkers() {
        // TODO: Return active workers count
        return 0; // TODO: Return count
    }
}

