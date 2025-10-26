package exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 1: Synchronized Keyword Demo
 * 
 * TODO Tasks:
 * 1. Implement UnsafeCounter - counter without synchronization (demonstrate race condition)
 * 2. Implement SynchronizedCounter - counter with synchronized keyword
 * 3. Compare results and observe race condition in UnsafeCounter
 * 4. Measure performance difference between synchronized and unsynchronized
 */
public class SyncDemo {
    
    private static final int NUM_THREADS = 5;
    private static final int INCREMENTS_PER_THREAD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Synchronized Keyword Demo");
        System.out.println("==========================================\n");
        
        // TODO: Create and test UnsafeCounter
        System.out.println("Testing UnsafeCounter...");
        testUnsafeCounter();
        
        // TODO: Create and test SynchronizedCounter
        System.out.println("\nTesting SynchronizedCounter...");
        testSynchronizedCounter();
        
        // TODO: Performance comparison
        System.out.println("\nPerformance Comparison...");
        performanceComparison();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void testUnsafeCounter() {
        // TODO: Implement UnsafeCounter
        // Hint: Simple counter without any synchronization
        // Expected: Race condition, final count < NUM_THREADS * INCREMENTS_PER_THREAD
        
        UnsafeCounter counter = new UnsafeCounter();
        List<Thread> threads = new ArrayList<>();
        
        // TODO: Create NUM_THREADS threads, each incrementing INCREMENTS_PER_THREAD times
        // TODO: Start and wait for all threads
        
        System.out.println("Expected: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Actual: " + counter.getCount());
        System.out.println("Race condition occurred: " + 
            (counter.getCount() != NUM_THREADS * INCREMENTS_PER_THREAD));
    }
    
    private static void testSynchronizedCounter() {
        // TODO: Implement SynchronizedCounter
        // Hint: Use synchronized keyword on increment method
        // Expected: Correct final count = NUM_THREADS * INCREMENTS_PER_THREAD
        
        SynchronizedCounter counter = new SynchronizedCounter();
        List<Thread> threads = new ArrayList<>();
        
        // TODO: Create NUM_THREADS threads, each incrementing INCREMENTS_PER_THREAD times
        // TODO: Start and wait for all threads
        
        System.out.println("Expected: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Actual: " + counter.getCount());
        System.out.println("Correct result: " + 
            (counter.getCount() == NUM_THREADS * INCREMENTS_PER_THREAD));
    }
    
    private static void performanceComparison() {
        // TODO: Measure time for synchronized vs unsynchronized
        // TODO: Print performance difference
        
        long start, end;
        
        start = System.nanoTime();
        // TODO: Run synchronized test
        end = System.nanoTime();
        long syncTime = (end - start) / 1_000_000; // Convert to milliseconds
        
        start = System.nanoTime();
        // TODO: Run unsynchronized test
        end = System.nanoTime();
        long unsyncTime = (end - start) / 1_000_000;
        
        System.out.println("Synchronized time: " + syncTime + "ms");
        System.out.println("Unsynchronized time: " + unsyncTime + "ms");
        System.out.println("Performance difference: " + 
            String.format("%.2f%%", ((double)syncTime / unsyncTime - 1) * 100));
    }
    
    // TODO: Implement this class
    // Counter without synchronization - demonstrates race condition
    static class UnsafeCounter {
        private int count = 0;
        
        // TODO: Implement increment() without synchronization
        public void increment() {
            // TODO: Just increment count
            // This is not atomic! Multiple threads can read the same value
        }
        
        public int getCount() {
            return count;
        }
    }
    
    // TODO: Implement this class
    // Counter with synchronized keyword
    static class SynchronizedCounter {
        private int count = 0;
        
        // TODO: Implement increment() with synchronized keyword
        public synchronized void increment() {
            // TODO: Synchronized increment - only one thread at a time
        }
        
        public synchronized int getCount() {
            return count;
        }
    }
}
