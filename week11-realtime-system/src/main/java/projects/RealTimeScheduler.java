package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.PriorityQueue;

/**
 * Project 1: Real-Time Task Scheduler
 * 
 * Scheduler với deadline support
 * - Task priorities
 * - Deadline tracking
 * - Missed deadline detection
 * 
 * TODO Tasks:
 * 1. Implement Task với priority và deadline
 * 2. Implement Scheduler
 * 3. Schedule tasks theo priority/deadline
 * 4. Detect missed deadlines
 */
public class RealTimeScheduler {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Real-Time Task Scheduler Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo scheduler
        Scheduler scheduler = null; // TODO: Create
        
        // TODO: Submit tasks với different priorities và deadlines
        // TODO: Run scheduler
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement Task class
     */
    static class Task implements Comparable<Task> {
        private final int id;
        private final int priority;
        private final long deadline;
        private final Runnable work;
        
        public Task(int id, int priority, long deadline, Runnable work) {
            this.id = id;
            this.priority = priority;
            this.deadline = deadline;
            this.work = work;
        }
        
        @Override
        public int compareTo(Task other) {
            // TODO: Compare by priority (higher first) hoặc deadline (earlier first)
            return 0; // TODO: Return comparison result
        }
        
        public int getId() { return id; }
        public int getPriority() { return priority; }
        public long getDeadline() { return deadline; }
        public Runnable getWork() { return work; }
        
        public boolean isDeadlineMissed() {
            // TODO: Check if current time > deadline
            return false; // TODO: Return true if missed
        }
    }
    
    /**
     * TODO: Implement Scheduler class
     */
    static class Scheduler {
        private final PriorityQueue<Task> taskQueue;
        private final ExecutorService executor;
        private final AtomicInteger missedDeadlines;
        
        public Scheduler() {
            this.taskQueue = null; // TODO: Create PriorityQueue
            this.executor = null; // TODO: Create ExecutorService
            this.missedDeadlines = null; // TODO: Create AtomicInteger
        }
        
        public void submit(Task task) {
            // TODO: Add task to queue
        }
        
        public void start() {
            // TODO: Process tasks từ queue
            // TODO: Check deadlines
            // TODO: Execute tasks
        }
        
        public void shutdown() {
            // TODO: Shutdown executor
        }
        
        public int getMissedDeadlines() {
            // TODO: Return missed deadlines count
            return 0; // TODO: Return count
        }
    }
}

