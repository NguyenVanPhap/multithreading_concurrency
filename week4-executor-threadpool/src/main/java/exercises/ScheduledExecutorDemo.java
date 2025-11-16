package exercises;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3: ScheduledExecutor Demo
 * 
 * TODO Tasks:
 * 1. Schedule tasks với delay
 * 2. Schedule tasks với fixed rate
 * 3. Schedule tasks với fixed delay
 * 4. Cancel scheduled tasks
 * 5. Handle exceptions trong scheduled tasks
 */
public class ScheduledExecutorDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ScheduledExecutor Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test schedule với delay
        System.out.println("Test 1: Schedule với Delay");
        testScheduleWithDelay();
        
        // TODO: Test scheduleAtFixedRate
        System.out.println("\nTest 2: ScheduleAtFixedRate");
        testScheduleAtFixedRate();
        
        // TODO: Test scheduleWithFixedDelay
        System.out.println("\nTest 3: ScheduleWithFixedDelay");
        testScheduleWithFixedDelay();
        
        // TODO: Test cancel scheduled tasks
        System.out.println("\nTest 4: Cancel Scheduled Tasks");
        testCancelScheduledTasks();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test schedule với delay
     * Execute task sau một khoảng thời gian
     */
    private static void testScheduleWithDelay() {
        // TODO: Tạo ScheduledExecutorService với 3 threads
        ScheduledExecutorService scheduler = null; // TODO: Use Executors.newScheduledThreadPool(3)
        
        System.out.println("Scheduling tasks with delays...");
        
        // TODO: Schedule task với delay 1 second
        ScheduledFuture<?> future1 = null; // TODO: Schedule Runnable với delay 1s
        
        // TODO: Schedule task với delay 2 seconds
        ScheduledFuture<?> future2 = null; // TODO: Schedule Runnable với delay 2s
        
        // TODO: Schedule Callable với delay 500ms
        ScheduledFuture<String> future3 = null; // TODO: Schedule Callable với delay 500ms
        
        try {
            // TODO: Wait for results với future.get()
            // TODO: In ra kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        // TODO: Shutdown scheduler
    }
    
    /**
     * TODO: Test scheduleAtFixedRate
     * Execute task với fixed rate (không phụ thuộc vào execution time)
     */
    private static void testScheduleAtFixedRate() {
        ScheduledExecutorService scheduler = null; // TODO: Create ScheduledExecutorService
        
        AtomicInteger counter = new AtomicInteger(0);
        
        System.out.println("Starting fixed rate task (every 500ms)...");
        
        // TODO: Schedule task với fixed rate (0 initial delay, 500ms period)
        ScheduledFuture<?> future = null; // TODO: Use scheduleAtFixedRate()
        // TODO: Task nên increment counter và in ra count + timestamp
        // TODO: Simulate work (sleep 200ms)
        
        // TODO: Run for 3 seconds
        // TODO: Cancel task
        // TODO: In ra total executions
        // TODO: Shutdown scheduler
    }
    
    /**
     * TODO: Test scheduleWithFixedDelay
     * Execute task với fixed delay (delay sau khi task hoàn thành)
     */
    private static void testScheduleWithFixedDelay() {
        ScheduledExecutorService scheduler = null; // TODO: Create ScheduledExecutorService
        
        AtomicInteger counter = new AtomicInteger(0);
        
        System.out.println("Starting fixed delay task (500ms delay after completion)...");
        
        // TODO: Schedule task với fixed delay (0 initial delay, 500ms delay)
        ScheduledFuture<?> future = null; // TODO: Use scheduleWithFixedDelay()
        // TODO: Task nên track start/end time và in ra
        // TODO: Simulate work (sleep 300ms)
        
        // TODO: Run for 3 seconds
        // TODO: Cancel task
        // TODO: In ra total executions
        // TODO: Shutdown scheduler
    }
    
    /**
     * TODO: Test cancel scheduled tasks
     * Cancel tasks và handle cancellation
     */
    private static void testCancelScheduledTasks() {
        ScheduledExecutorService scheduler = null; // TODO: Create ScheduledExecutorService
        
        List<ScheduledFuture<?>> futures = new ArrayList<>();
        
        // TODO: Schedule multiple tasks (5 tasks) với different delays
        // TODO: Mỗi task delay (i+1)*500ms
        // TODO: Lưu futures vào list
        
        // TODO: Cancel some tasks (tasks 1, 3, 4)
        // TODO: Check cancellation status với isCancelled() và isDone()
        
        // TODO: Wait for remaining tasks (3 seconds)
        // TODO: Check final status
        // TODO: Shutdown scheduler
    }
}

