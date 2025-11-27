package exercises;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 3: Timeout Lock Demo
 * 
 * TODO Tasks:
 * 1. Sử dụng tryLock() với timeout
 * 2. Implement deadlock detection với timeout
 * 3. Handle timeout gracefully
 * 4. So sánh timeout vs blocking lock
 */
public class TimeoutLockDemo {
    
    private static final int TIMEOUT_SECONDS = 2;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Timeout Lock Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test timeout lock
        System.out.println("Test 1: Timeout Lock");
        testTimeoutLock();
        
        // TODO: Test deadlock detection với timeout
        System.out.println("\nTest 2: Deadlock Detection với Timeout");
        testDeadlockDetectionWithTimeout();
        
        // TODO: Test graceful timeout handling
        System.out.println("\nTest 3: Graceful Timeout Handling");
        testGracefulTimeout();
        
        // TODO: So sánh performance
        System.out.println("\nTest 4: Performance Comparison");
        testPerformanceComparison();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test tryLock với timeout
     */
    private static void testTimeoutLock() {
        // TODO: Tạo Lock
        Lock lock = null; // TODO: Use ReentrantLock
        
        // TODO: Thread 1: Hold lock for 3 seconds
        Thread holder = new Thread(() -> {
            // TODO: Acquire lock và hold trong 3 seconds
            // TODO: In ra message khi acquire và release
        });
        
        // TODO: Start holder thread
        // TODO: Wait một chút để holder acquire lock trước
        
        // TODO: Thread 2: Try to acquire với timeout
        Thread waiter = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            
            // TODO: Use tryLock với timeout (TIMEOUT_SECONDS)
            // TODO: Measure elapsed time
            // TODO: Nếu acquired, unlock trong finally
            // TODO: In ra kết quả (acquired hoặc timeout)
        });
        
        // TODO: Start waiter thread
        // TODO: Wait for both threads
    }
    
    /**
     * TODO: Test deadlock detection với timeout
     * Sử dụng timeout để phát hiện và tránh deadlock
     */
    private static void testDeadlockDetectionWithTimeout() {
        // TODO: Tạo 2 locks
        Lock lock1 = null; // TODO: Use ReentrantLock
        Lock lock2 = null; // TODO: Use ReentrantLock
        
        AtomicInteger deadlockDetected = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        
        // TODO: Thread 1: lock1 -> lock2
        Thread thread1 = new Thread(() -> {
            // TODO: Acquire lock1
            // TODO: Use tryLock với timeout cho lock2
            // TODO: Nếu timeout -> deadlock detected
            // TODO: Nếu success -> increment successCount
            // TODO: Unlock trong finally
        });
        
        // TODO: Thread 2: lock2 -> lock1 (opposite order - potential deadlock)
        Thread thread2 = new Thread(() -> {
            // TODO: Acquire lock2
            // TODO: Use tryLock với timeout cho lock1
            // TODO: Nếu timeout -> deadlock detected
            // TODO: Nếu success -> increment successCount
            // TODO: Unlock trong finally
        });
        
        // TODO: Start threads và wait
        // TODO: In ra kết quả (success count, deadlock detected count)
    }
    
    /**
     * TODO: Test graceful timeout handling
     * Retry mechanism khi timeout
     */
    private static void testGracefulTimeout() {
        // TODO: Tạo Lock
        Lock lock = null; // TODO: Use ReentrantLock
        final int MAX_RETRIES = 3;
        
        // TODO: Holder thread - hold lock trong 1.5s, release 0.5s, lặp lại MAX_RETRIES lần
        Thread holder = new Thread(() -> {
            // TODO: Loop MAX_RETRIES times
            // TODO: Acquire lock, hold 1.5s, release, wait 0.5s
        });
        
        // TODO: Start holder và wait một chút
        
        // TODO: Waiter thread với retry mechanism
        Thread waiter = new Thread(() -> {
            int retryCount = 0;
            boolean acquired = false;
            
            // TODO: Retry loop
            while (retryCount < MAX_RETRIES && !acquired) {
                // TODO: Try to acquire lock với timeout 1 second
                // TODO: Nếu acquired -> unlock và break
                // TODO: Nếu timeout -> increment retryCount và retry
            }
            
            // TODO: In ra kết quả (success hoặc failed)
        });
        
        // TODO: Start waiter và wait for both threads
    }
    
    /**
     * TODO: So sánh performance: blocking lock vs timeout lock
     */
    private static void testPerformanceComparison() {
        // TODO: Tạo Lock
        Lock lock = null; // TODO: Use ReentrantLock
        final int NUM_OPERATIONS = 1000;
        
        // TODO: Test 1: Blocking lock
        long startTime = System.currentTimeMillis();
        // TODO: Loop NUM_OPERATIONS times
        // TODO: Use lock.lock() và unlock()
        long blockingTime = System.currentTimeMillis() - startTime;
        
        // TODO: Test 2: Timeout lock (with very long timeout - should always succeed)
        startTime = System.currentTimeMillis();
        int successCount = 0;
        // TODO: Loop NUM_OPERATIONS times
        // TODO: Use tryLock với long timeout (10 seconds)
        // TODO: Count successes
        long timeoutTime = System.currentTimeMillis() - startTime;
        
        // TODO: In ra kết quả so sánh (blocking time, timeout time, overhead)
    }
}

