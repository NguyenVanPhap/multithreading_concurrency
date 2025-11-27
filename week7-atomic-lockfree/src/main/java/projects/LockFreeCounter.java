package projects;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Project 2: Lock-Free Counter
 * 
 * High-performance counter
 * - Atomic operations
 * - Performance comparison
 * - Statistics
 * 
 * TODO Tasks:
 * 1. Implement LockFreeCounter với AtomicLong
 * 2. Compare với synchronized counter
 * 3. Performance testing
 * 4. Statistics tracking
 */
public class LockFreeCounter {
    
    private static final int NUM_THREADS = 20;
    private static final int OPERATIONS_PER_THREAD = 100000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Lock-Free Counter Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test lock-free counter
        System.out.println("Test 1: Lock-Free Counter");
        long lockFreeTime = testLockFreeCounter();
        
        // TODO: Test synchronized counter
        System.out.println("\nTest 2: Synchronized Counter");
        long syncTime = testSynchronizedCounter();
        
        // TODO: So sánh performance
        System.out.println("\nPerformance Comparison:");
        System.out.println("  Lock-Free: " + lockFreeTime + "ms");
        System.out.println("  Synchronized: " + syncTime + "ms");
        System.out.println("  Speedup: " + String.format("%.2f", (double) syncTime / lockFreeTime) + "x");
    }
    
    /**
     * TODO: Test lock-free counter
     */
    private static long testLockFreeCounter() {
        Counter counter = null; // TODO: Create LockFreeCounter
        
        ExecutorService executor = null; // TODO: Create
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Submit tasks
        // TODO: Wait for completion
        
        long endTime = System.currentTimeMillis();
        System.out.println("Final value: " + counter.getValue());
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Test synchronized counter
     */
    private static long testSynchronizedCounter() {
        Counter counter = null; // TODO: Create SynchronizedCounter
        
        ExecutorService executor = null; // TODO: Create
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Submit tasks
        // TODO: Wait for completion
        
        long endTime = System.currentTimeMillis();
        System.out.println("Final value: " + counter.getValue());
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Implement Counter interface
     */
    interface Counter {
        void increment();
        long getValue();
    }
    
    /**
     * TODO: Implement LockFreeCounter
     */
    static class LockFreeCounterImpl implements Counter {
        private final AtomicLong count;
        
        public LockFreeCounterImpl() {
            this.count = null; // TODO: Create AtomicLong với 0
        }
        
        @Override
        public void increment() {
            // TODO: Increment atomically
        }
        
        @Override
        public long getValue() {
            // TODO: Return value
            return 0; // TODO: Return count
        }
    }
    
    /**
     * TODO: Implement SynchronizedCounter
     */
    static class SynchronizedCounterImpl implements Counter {
        private long count = 0;
        
        @Override
        public synchronized void increment() {
            // TODO: Increment với synchronized
        }
        
        @Override
        public synchronized long getValue() {
            // TODO: Return value
            return 0; // TODO: Return count
        }
    }
}

