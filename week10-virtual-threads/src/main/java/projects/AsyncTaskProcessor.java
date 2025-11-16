package projects;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project 2: Async Task Processor
 * 
 * Process many async tasks
 * - Virtual thread executor
 * - Task management
 * - Statistics
 * 
 * TODO Tasks:
 * 1. Implement TaskProcessor với virtual threads
 * 2. Process tasks
 * 3. Statistics tracking
 */
public class AsyncTaskProcessor {
    
    private static final int NUM_TASKS = 50000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Async Task Processor Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo processor
        TaskProcessor processor = null; // TODO: Create
        
        // TODO: Process tasks
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement TaskProcessor class
     */
    static class TaskProcessor {
        private final ExecutorService executor;
        private final AtomicInteger completedTasks;
        
        public TaskProcessor() {
            this.executor = null; // TODO: Create virtual thread executor
            this.completedTasks = null; // TODO: Create AtomicInteger
        }
        
        public void processTask(Task task) {
            // TODO: Submit task
            // TODO: Simulate async processing
            // TODO: Increment completed count
        }
        
        public void shutdown() {
            // TODO: Shutdown executor
        }
        
        public int getCompletedTasks() {
            // TODO: Return completed count
            return 0; // TODO: Return count
        }
    }
    
    static class Task {
        private final int id;
        
        public Task(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
    }
}

