package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

/**
 * Project 2: Deadline Manager
 * 
 * Quản lý tasks với deadlines
 * - Deadline monitoring
 * - Priority adjustment
 * - Statistics
 * 
 * TODO Tasks:
 * 1. Implement DeadlineManager
 * 2. Monitor deadlines
 * 3. Adjust priorities
 * 4. Track statistics
 */
public class DeadlineManager {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Deadline Manager Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo deadline manager
        Manager manager = null; // TODO: Create
        
        // TODO: Submit tasks với deadlines
        // TODO: Monitor và manage
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement Manager class
     */
    static class Manager {
        private final ExecutorService executor;
        private final AtomicInteger missedDeadlines;
        private final List<TimedTask> tasks;
        
        public Manager() {
            this.executor = null; // TODO: Create ExecutorService
            this.missedDeadlines = null; // TODO: Create AtomicInteger
            this.tasks = null; // TODO: Create thread-safe list
        }
        
        public void submitTask(TimedTask task) {
            // TODO: Submit task
            // TODO: Monitor deadline
        }
        
        public void monitorDeadlines() {
            // TODO: Periodically check deadlines
            // TODO: Detect missed deadlines
            // TODO: Adjust priorities if needed
        }
        
        public void shutdown() {
            // TODO: Shutdown executor
        }
        
        public int getMissedDeadlines() {
            // TODO: Return count
            return 0; // TODO: Return count
        }
    }
    
    static class TimedTask {
        private final int id;
        private final long deadline;
        private final Runnable work;
        
        public TimedTask(int id, long deadline, Runnable work) {
            this.id = id;
            this.deadline = deadline;
            this.work = work;
        }
        
        public int getId() { return id; }
        public long getDeadline() { return deadline; }
        public Runnable getWork() { return work; }
    }
}

