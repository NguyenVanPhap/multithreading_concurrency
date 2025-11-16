package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 3: Performance Comparison
 * 
 * TODO Tasks:
 * 1. Virtual threads vs Platform threads
 * 2. High-concurrency scenarios
 * 3. I/O-bound vs CPU-bound
 */
public class PerformanceDemo {
    
    private static final int NUM_TASKS = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Performance Comparison Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test I/O-bound với virtual threads
        System.out.println("Test 1: I/O-Bound - Virtual Threads");
        long virtualTime = testIOBoundVirtualThreads();
        
        // TODO: Test I/O-bound với platform threads
        System.out.println("\nTest 2: I/O-Bound - Platform Threads");
        long platformTime = testIOBoundPlatformThreads();
        
        // TODO: So sánh
        System.out.println("\nPerformance Comparison:");
        System.out.println("  Virtual Threads: " + virtualTime + "ms");
        System.out.println("  Platform Threads: " + platformTime + "ms");
    }
    
    /**
     * TODO: Test I/O-bound với virtual threads
     */
    private static long testIOBoundVirtualThreads() {
        ExecutorService executor = null; // TODO: Create virtual thread executor
        
        long startTime = System.currentTimeMillis();
        // TODO: Submit NUM_TASKS I/O-bound tasks
        // TODO: Wait for completion
        long endTime = System.currentTimeMillis();
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Test I/O-bound với platform threads
     */
    private static long testIOBoundPlatformThreads() {
        ExecutorService executor = null; // TODO: Create fixed thread pool
        
        long startTime = System.currentTimeMillis();
        // TODO: Submit NUM_TASKS I/O-bound tasks
        // TODO: Wait for completion
        long endTime = System.currentTimeMillis();
        
        return endTime - startTime;
    }
}

