package exercises;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 2: Lock Ordering Demo
 * 
 * TODO Tasks:
 * 1. Implement lock ordering để tránh deadlock
 * 2. So sánh consistent vs inconsistent lock ordering
 * 3. Thực hành với multiple resources
 * 4. Test với nhiều threads
 */
public class LockOrderingDemo {
    
    private static final int NUM_RESOURCES = 5;
    private static final int NUM_THREADS = 10;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Lock Ordering Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test inconsistent ordering (causes deadlock)
        System.out.println("Test 1: Inconsistent Lock Ordering (Deadlock Risk)");
        testInconsistentOrdering();
        
        // TODO: Test consistent ordering (safe)
        System.out.println("\nTest 2: Consistent Lock Ordering (Safe)");
        testConsistentOrdering();
        
        // TODO: Test với multiple resources
        System.out.println("\nTest 3: Multiple Resources với Lock Ordering");
        testMultipleResources();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test inconsistent lock ordering - có thể gây deadlock
     * Thread 1: lock resource 1 -> resource 2
     * Thread 2: lock resource 2 -> resource 1
     */
    private static void testInconsistentOrdering() {
        // TODO: Tạo Map<Integer, Lock> với NUM_RESOURCES locks
        Map<Integer, Lock> locks = null; // TODO: Initialize
        
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger deadlockCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                // TODO: Random resource IDs (inconsistent ordering - KHÔNG sort!)
                List<Integer> resourceIds = null; // TODO: Create list với 2 random resource IDs
                
                // TODO: Acquire locks theo thứ tự random (NGUY HIỂM!)
                // TODO: Sử dụng tryLock với timeout để tránh block forever
                // TODO: Nếu timeout, release tất cả locks đã acquire và return
                // TODO: Nếu acquire thành công, do some work
                // TODO: Release locks trong finally block (reverse order)
                
                latch.countDown();
            });
            // TODO: Start thread
        }
        
        // TODO: Wait for threads với timeout
        // TODO: In ra kết quả (success count, deadlock count)
    }
    
    /**
     * TODO: Test consistent lock ordering - an toàn
     * Tất cả threads đều acquire locks theo cùng một thứ tự (sorted by ID)
     */
    private static void testConsistentOrdering() {
        // TODO: Tạo Map<Integer, Lock> với NUM_RESOURCES locks
        Map<Integer, Lock> locks = null; // TODO: Initialize
        
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                // TODO: Random resource IDs
                List<Integer> resourceIds = null; // TODO: Create list với 2 random resource IDs
                
                // TODO: Sort resource IDs để đảm bảo consistent ordering (QUAN TRỌNG!)
                // TODO: Acquire locks theo thứ tự đã sort (có thể dùng blocking lock)
                // TODO: Do some work
                // TODO: Release locks trong finally block (reverse order)
                
                latch.countDown();
            });
            // TODO: Start thread
        }
        
        // TODO: Wait for threads
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test với multiple resources và lock ordering
     */
    private static void testMultipleResources() {
        // TODO: Tạo Map<Integer, Lock> với NUM_RESOURCES locks
        Map<Integer, Lock> locks = null; // TODO: Initialize
        
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                // TODO: Random 2-4 resources
                // TODO: Sort resource IDs để đảm bảo consistent ordering
                // TODO: Acquire all locks theo thứ tự đã sort
                // TODO: Simulate work
                // TODO: Release all locks trong finally (reverse order)
                
                latch.countDown();
            });
            // TODO: Start thread
        }
        
        // TODO: Wait for all threads
        // TODO: In ra thời gian hoàn thành
    }
    
}

