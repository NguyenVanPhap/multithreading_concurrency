package exercises;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Exercise 1: ForkJoin Demo
 * 
 * TODO Tasks:
 * 1. ForkJoinPool basics
 * 2. Work-stealing
 * 3. Performance characteristics
 */
public class ForkJoinDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ForkJoin Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test ForkJoinPool
        System.out.println("Test 1: ForkJoinPool");
        testForkJoinPool();
        
        // TODO: Test work-stealing
        System.out.println("\nTest 2: Work-Stealing");
        testWorkStealing();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test ForkJoinPool
     */
    private static void testForkJoinPool() {
        // TODO: Tạo ForkJoinPool
        ForkJoinPool pool = null; // TODO: Use ForkJoinPool.commonPool() hoặc new ForkJoinPool()
        
        // TODO: In ra parallelism level
        // TODO: Submit task
        // TODO: Get result
        // TODO: Shutdown pool
    }
    
    /**
     * TODO: Test work-stealing
     */
    private static void testWorkStealing() {
        ForkJoinPool pool = null; // TODO: Create
        
        // TODO: Submit multiple tasks
        // TODO: Quan sát work-stealing behavior
        // TODO: Tasks được steal từ busy threads
    }
}

