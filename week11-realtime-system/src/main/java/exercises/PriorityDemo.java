package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 1: Priority Scheduling
 * 
 * TODO Tasks:
 * 1. Thread priorities
 * 2. Priority inheritance
 * 3. Priority inversion
 */
public class PriorityDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Priority Scheduling Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test thread priorities
        System.out.println("Test 1: Thread Priorities");
        testThreadPriorities();
        
        // TODO: Test priority inversion
        System.out.println("\nTest 2: Priority Inversion");
        testPriorityInversion();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test thread priorities
     */
    private static void testThreadPriorities() {
        // TODO: Tạo threads với different priorities
        // TODO: MIN_PRIORITY, NORM_PRIORITY, MAX_PRIORITY
        // TODO: Quan sát execution order (note: không guarantee)
    }
    
    /**
     * TODO: Test priority inversion
     */
    private static void testPriorityInversion() {
        // TODO: High priority thread blocked by low priority thread
        // TODO: Demonstrate priority inversion problem
        // TODO: In ra observations
    }
}

