package project;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

/**
 * Final Project: Distributed Task Processing System
 * 
 * TODO Tasks:
 * 1. Implement TaskProcessor - main coordinator
 * 2. Integrate TaskQueue, WorkerPool, ResultAggregator
 * 3. Handle task submission và execution
 * 4. Error handling và retry
 * 5. Monitoring và statistics
 */
public class TaskProcessor {
    
    private final TaskQueue taskQueue;
    private final WorkerPool workerPool;
    private final ResultAggregator resultAggregator;
    private final MonitoringSystem monitoring;
    private volatile boolean running = false;
    
    public TaskProcessor(int workerPoolSize, int queueCapacity) {
        this.taskQueue = null; // TODO: Create TaskQueue với queueCapacity
        this.workerPool = null; // TODO: Create WorkerPool với workerPoolSize
        this.resultAggregator = null; // TODO: Create ResultAggregator
        this.monitoring = null; // TODO: Create MonitoringSystem
    }
    
    public void start() {
        // TODO: Start processor
        // TODO: Start worker pool
        // TODO: Start monitoring
        running = true;
    }
    
    public CompletableFuture<TaskResult> submitTask(Task task) {
        // TODO: Submit task to queue
        // TODO: Return CompletableFuture
        return null; // TODO: Return future
    }
    
    public void shutdown() {
        // TODO: Graceful shutdown
        // TODO: Stop accepting new tasks
        // TODO: Wait for current tasks
        // TODO: Shutdown worker pool
        // TODO: Print final statistics
    }
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Distributed Task Processing System");
        System.out.println("==========================================\n");
        
        // TODO: Tạo TaskProcessor
        TaskProcessor processor = null; // TODO: Create
        
        // TODO: Start processor
        // TODO: Submit many tasks
        // TODO: Wait for completion
        // TODO: Shutdown
    }
}

