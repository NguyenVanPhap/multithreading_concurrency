package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 3: Scheduling Algorithms
 * 
 * TODO Tasks:
 * 1. Rate Monotonic Scheduling
 * 2. Earliest Deadline First
 * 3. Comparison
 */
public class SchedulingDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Scheduling Algorithms Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test Rate Monotonic Scheduling
        System.out.println("Test 1: Rate Monotonic Scheduling");
        testRateMonotonic();
        
        // TODO: Test Earliest Deadline First
        System.out.println("\nTest 2: Earliest Deadline First");
        testEarliestDeadlineFirst();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test Rate Monotonic Scheduling
     */
    private static void testRateMonotonic() {
        // TODO: Tasks với different rates (periods)
        // TODO: Higher rate = higher priority
        // TODO: Schedule tasks
        // TODO: In ra scheduling order
    }
    
    /**
     * TODO: Test Earliest Deadline First
     */
    private static void testEarliestDeadlineFirst() {
        // TODO: Tasks với deadlines
        // TODO: Schedule theo earliest deadline
        // TODO: In ra scheduling order
    }
}

