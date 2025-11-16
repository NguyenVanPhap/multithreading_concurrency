package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 2: Deadline Management
 * 
 * TODO Tasks:
 * 1. Task với deadlines
 * 2. Deadline monitoring
 * 3. Missed deadline handling
 */
public class DeadlineDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Deadline Management Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test deadline tracking
        System.out.println("Test 1: Deadline Tracking");
        testDeadlineTracking();
        
        // TODO: Test missed deadline detection
        System.out.println("\nTest 2: Missed Deadline Detection");
        testMissedDeadline();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test deadline tracking
     */
    private static void testDeadlineTracking() {
        // TODO: Tạo tasks với deadlines
        // TODO: Monitor deadline
        // TODO: In ra tasks completed before deadline
    }
    
    /**
     * TODO: Test missed deadline detection
     */
    private static void testMissedDeadline() {
        AtomicInteger missedDeadlines = null; // TODO: Create
        
        // TODO: Tạo tasks với tight deadlines
        // TODO: Detect missed deadlines
        // TODO: In ra số missed deadlines
    }
}

