package exercises;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
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
        Map<Integer, ReentrantLock> locks = new HashMap<>();
        for (int i = 0; i < NUM_RESOURCES; i++) {
            locks.put(i, new ReentrantLock());
        }

        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger deadlockCount = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;

            Thread thread = new Thread(() -> {
                ThreadLocalRandom rand = ThreadLocalRandom.current();

                // ---------------------------------------------------
                // TODO: Random resource IDs (KHÔNG SORT!)
                // ---------------------------------------------------
                int r1 = rand.nextInt(NUM_RESOURCES);
                int r2 = rand.nextInt(NUM_RESOURCES);
                while (r2 == r1) {
                    r2 = rand.nextInt(NUM_RESOURCES);
                }

                List<Integer> resourceIds = new ArrayList<>();
                resourceIds.add(r1);
                resourceIds.add(r2);

                // Randomize order → inconsistent ordering
                Collections.shuffle(resourceIds);

                System.out.println(Thread.currentThread().getName() +
                        " wants locks: " + resourceIds);

                List<ReentrantLock> acquiredLocks = new ArrayList<>();
                long timeoutTotalMs = 500; // tổng timeout
                long deadline = System.currentTimeMillis() + timeoutTotalMs;

                try {
                    // ---------------------------------------------------
                    // TODO: Acquire locks theo thứ tự random
                    // ---------------------------------------------------
                    for (int lockId : resourceIds) {
                        ReentrantLock lock = locks.get(lockId);

                        long remaining = deadline - System.currentTimeMillis();
                        if (remaining <= 0) {
                            System.out.println(Thread.currentThread().getName()
                                    + " TIMEOUT BEFORE tryLock()");
                            deadlockCount.incrementAndGet();
                            return;
                        }

                        // ---------------------------------------------------
                        // TODO: tryLock với timeout để tránh block forever
                        // ---------------------------------------------------
                        if (lock.tryLock(remaining, TimeUnit.MILLISECONDS)) {
                            acquiredLocks.add(lock);
                        } else {
                            System.out.println(Thread.currentThread().getName()
                                    + " TIMEOUT on lock " + lockId);
                            deadlockCount.incrementAndGet();
                            return;
                        }
                    }

                    // Nếu acquire đủ 2 lock → success
                    System.out.println(Thread.currentThread().getName() +
                            " acquired BOTH locks: " + resourceIds);

                    successCount.incrementAndGet();

                    // TODO: do some work
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // ---------------------------------------------------
                    // TODO: Release locks trong reverse order
                    // ---------------------------------------------------
                    Collections.reverse(acquiredLocks);
                    for (ReentrantLock lock : acquiredLocks) {
                        try {
                            if (lock.isHeldByCurrentThread()) {
                                lock.unlock();
                            }
                        } catch (IllegalMonitorStateException ignored) {}
                    }
                }

                latch.countDown();
            });

            thread.setName("Thread-" + threadId);
            threads.add(thread);
            thread.start(); // TODO: Start thread
        }

        // TODO: Wait for threads với timeout
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: In ra kết quả
        System.out.println("\n===== FINAL RESULT =====");
        System.out.println("Success count   = " + successCount.get());
        System.out.println("Deadlock count  = " + deadlockCount.get());
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
                ThreadLocalRandom rand = ThreadLocalRandom.current();

                int r1 = rand.nextInt(NUM_RESOURCES);
                int r2 = rand.nextInt(NUM_RESOURCES);
                while (r2 == r1) {
                    r2 = rand.nextInt(NUM_RESOURCES);
                }

                List<Integer> resourceIds = new ArrayList<>();
                resourceIds.add(r1);
                resourceIds.add(r2);


                Collections.sort(resourceIds);
                
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

