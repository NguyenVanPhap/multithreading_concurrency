package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 2: Virtual Thread Executor
 * 
 * TODO Tasks:
 * 1. Executors.newVirtualThreadPerTaskExecutor()
 * 2. Submit tasks
 * 3. Performance testing
 */
public class VirtualThreadExecutorDemo {
    
    private static final int NUM_TASKS = 100000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Virtual Thread Executor Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test virtual thread executor
        System.out.println("Test 1: Virtual Thread Executor");
        testVirtualThreadExecutor();
        
        // TODO: Test với I/O-bound tasks
        System.out.println("\nTest 2: I/O-Bound Tasks");
        testIOBoundTasks();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test virtual thread executor
     */
    private static void testVirtualThreadExecutor() {
        // TODO: Tạo virtual thread executor
        ExecutorService executor = null; // TODO: Use Executors.newVirtualThreadPerTaskExecutor()
        
        // TODO: Submit NUM_TASKS tasks
        // TODO: Mỗi task in ra thread name
        // TODO: Wait for all tasks
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test với I/O-bound tasks
     */
    private static void testIOBoundTasks() {
        ExecutorService executor = null; // TODO: Create virtual thread executor
        
        // TODO: Submit many I/O-bound tasks (sleep)
        // TODO: Quan sát virtual threads handle I/O efficiently
        // TODO: Measure performance
    }
}

