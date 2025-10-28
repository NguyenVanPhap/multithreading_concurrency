package exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exercise 2: ReentrantLock Demo
 * 
 * TODO Tasks:
 * 1. Implement SharedResource with ReentrantLock
 * 2. Demonstrate lock(), tryLock(), and lockInterruptibly()
 * 3. Test timeout with tryLock(timeout)
 * 4. Compare fair vs unfair locks
 * 5. Handle interrupts properly
 */
public class LockDemo {
    
    private static final int NUM_THREADS = 3;
    private static final int OPERATIONS_PER_THREAD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ReentrantLock Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic lock
        System.out.println("Test 1: Basic Lock");
        testBasicLock();
        
        // TODO: Test tryLock with timeout
        System.out.println("\nTest 2: TryLock with Timeout");
        testTryLockWithTimeout();
        
        // TODO: Test fair vs unfair locks
        System.out.println("\nTest 3: Fair vs Unfair Locks");
        testFairness();
        
        // TODO: Test interruptible lock
        System.out.println("\nTest 4: Interruptible Lock");
        testInterruptibleLock();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void testBasicLock() {
        // TODO: Implement SharedResource with ReentrantLock
        // TODO: Create multiple threads accessing the resource
        // TODO: Observe thread-safe behavior
        
        SharedResource resource = new SharedResource();
        List<Thread> threads = new ArrayList<>();
        
        // TODO: Create and start threads
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(() -> {

            });
            threads.add(thread);
        }
        // TODO: Wait for all threads to complete
        
        System.out.println("Final value: " + resource.getValue());
        System.out.println("Expected: " + (NUM_THREADS * OPERATIONS_PER_THREAD));
    }
    
    private static void testTryLockWithTimeout() {
        // TODO: Create resource and attempt to acquire lock with timeout
        // TODO: Handle timeout case
        
        SharedResource resource = new SharedResource();
        boolean acquired = resource.tryLock(2); // Try to acquire for 2 seconds
        
        System.out.println("Lock acquired: " + acquired);
        
        // TODO: Clean up
    }
    
    private static void testFairness() {
        // TODO: Test unfair lock (default)
        System.out.println("Testing unfair lock...");
        testLockFairness(false);
        
        // TODO: Test fair lock
        System.out.println("\nTesting fair lock...");
        testLockFairness(true);
        
        System.out.println("\nNote: Fair locks guarantee FIFO but may be slower");
    }
    
    private static void testLockFairness(boolean fair) {
        // TODO: Create SharedResource with specified fairness
        // TODO: Monitor lock acquisition order
        // TODO: Fair locks should maintain order, unfair locks may be faster
        
        SharedResource resource = new SharedResource(fair);
        
        // TODO: Create threads and observe behavior
        System.out.println("Lock acquisition behavior tested");
    }
    
    private static void testInterruptibleLock() {
        // TODO: Create resource and thread
        // TODO: Thread should acquire lock with lockInterruptibly()
        // TODO: Interrupt the thread and observe behavior
        
        SharedResource resource = new SharedResource();
        
        Thread thread = new Thread(() -> {
            try {
                // TODO: Use lockInterruptibly() instead of lock()
                resource.someOperation();
                System.out.println("Thread completed normally");
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        });
        
        thread.start();
        
        // TODO: Sleep briefly, then interrupt
        try {
            Thread.sleep(100);
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // TODO: Implement this class
    // Thread-safe counter using ReentrantLock
    static class SharedResource {
        private final Lock lock;
        private int value = 0;
        
        public SharedResource() {
            // TODO: Initialize with unfair lock
            this.lock = new ReentrantLock();
        }
        
        public SharedResource(boolean fair) {
            // TODO: Initialize with specified fairness
            this.lock = new ReentrantLock(fair);
        }
        
        // TODO: Implement thread-safe increment
        public void increment() {
            // TODO: Use lock() and unlock() in try-finally
            lock.lock();
            // TODO: Increment value
            try {
                value++;
            } finally {
                lock.unlock();
            }
        }
        
        public int getValue() {
            return value;
        }
        
        // TODO: Implement tryLock with timeout
        public boolean tryLock(int seconds) {
            boolean acquired = false;
            try {
                // TODO: Use tryLock(timeout) with TimeUnit.SECONDS
                acquired = lock.tryLock(seconds, TimeUnit.SECONDS);
                // TODO: Always unlock in finally
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                if (acquired) {
                    lock.unlock();
                }
            }
            return acquired;
        }
        
        // TODO: Implement interruptible operation
        public void someOperation() throws InterruptedException {
            lock.lockInterruptibly(); // Acquire lock in interruptible way
            try {
                // TODO: Do some work
                // Simulate some work with the shared resource
                value++;
                // Sleep to simulate work and allow interruption
                Thread.sleep(1000);
            } finally {
                lock.unlock(); // Always unlock in finally
            }
        }
    }
}
