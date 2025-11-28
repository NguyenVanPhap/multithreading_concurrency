package projects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.stream.Collectors;

/**
 * Project 2: Resource Manager
 * 
 * Quản lý tài nguyên với deadlock prevention
 * - Multiple threads yêu cầu multiple resources
 * - Deadlock prevention với lock ordering
 * - Timeout và retry mechanism
 * - Resource allocation tracking
 * 
 * TODO Tasks:
 * 1. Implement ResourceManager với multiple resources
 * 2. Thread-safe resource allocation
 * 3. Lock ordering để tránh deadlock
 * 4. Timeout cho resource requests
 * 5. Deadlock detection và recovery
 * 6. Statistics và monitoring
 */
public class ResourceManager {
    
    private static final int NUM_RESOURCES = 10;
    private static final int NUM_THREADS = 20;
    private static final long TIMEOUT_MS = 2000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Resource Manager Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic resource allocation
        System.out.println("Test 1: Basic Resource Allocation");
        testBasicAllocation();
        
        // TODO: Test với lock ordering
        System.out.println("\nTest 2: Resource Allocation với Lock Ordering");
        testLockOrdering();
        
        // TODO: Test với timeout
        System.out.println("\nTest 3: Resource Allocation với Timeout");
        testTimeoutAllocation();
        
        // TODO: Test deadlock detection
        System.out.println("\nTest 4: Deadlock Detection");
        testDeadlockDetection();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic resource allocation
     */
    private static void testBasicAllocation() {
        Manager manager = new Manager(NUM_RESOURCES);
        List<Thread> threads = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                // TODO: Request random 2-4 resources
                Random random = new Random();
                int numResources = 2 + random.nextInt(3);
                List<Integer> requestedResources = new ArrayList<>();
                
                for (int j = 0; j < numResources; j++) {
                    int resourceId = random.nextInt(NUM_RESOURCES);
                    if (!requestedResources.contains(resourceId)) {
                        requestedResources.add(resourceId);
                    }
                }
                
                // TODO: Acquire resources
                try {
                    if (manager.acquireResources(requestedResources, TIMEOUT_MS)) {
                        try {
                            // Simulate work
                            Thread.sleep(100);
                            successCount.incrementAndGet();
                        } finally {
                            manager.releaseResources(requestedResources);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.setName("Thread-" + threadId);
            threads.add(thread);
        }
        
        long startTime = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("Success: " + successCount.get() + "/" + NUM_THREADS);
        System.out.println("Time: " + (endTime - startTime) + "ms");
        System.out.println("Deadlock detected: " + manager.isDeadlockDetected());
    }
    
    /**
     * TODO: Test với lock ordering
     */
    private static void testLockOrdering() {
        Manager manager = new Manager(NUM_RESOURCES);
        List<Thread> threads = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                Random random = new Random();
                int numResources = 2 + random.nextInt(3);
                List<Integer> requestedResources = new ArrayList<>();
                
                for (int j = 0; j < numResources; j++) {
                    int resourceId = random.nextInt(NUM_RESOURCES);
                    if (!requestedResources.contains(resourceId)) {
                        requestedResources.add(resourceId);
                    }
                }
                
                // TODO: Resources sẽ được sort trong acquireResources
                try {
                    if (manager.acquireResources(requestedResources, TIMEOUT_MS)) {
                        try {
                            Thread.sleep(100);
                            successCount.incrementAndGet();
                        } finally {
                            manager.releaseResources(requestedResources);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.setName("Thread-" + threadId);
            threads.add(thread);
        }
        
        long startTime = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("Success: " + successCount.get() + "/" + NUM_THREADS);
        System.out.println("Time: " + (endTime - startTime) + "ms");
        System.out.println("Deadlock detected: " + manager.isDeadlockDetected());
    }
    
    /**
     * TODO: Test với timeout
     */
    private static void testTimeoutAllocation() {
        Manager manager = new Manager(NUM_RESOURCES);
        List<Thread> threads = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger timeoutCount = new AtomicInteger(0);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                Random random = new Random();
                List<Integer> requestedResources = Arrays.asList(
                    random.nextInt(NUM_RESOURCES),
                    random.nextInt(NUM_RESOURCES)
                );
                
                try {
                    // TODO: Use shorter timeout
                    if (manager.acquireResources(requestedResources, 500)) {
                        try {
                            Thread.sleep(200); // Hold longer than timeout
                            successCount.incrementAndGet();
                        } finally {
                            manager.releaseResources(requestedResources);
                        }
                    } else {
                        timeoutCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.setName("Thread-" + threadId);
            threads.add(thread);
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Success: " + successCount.get());
        System.out.println("Timeout: " + timeoutCount.get());
    }
    
    /**
     * TODO: Test deadlock detection
     */
    private static void testDeadlockDetection() {
        Manager manager = new Manager(NUM_RESOURCES);
        ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
        
        // Create threads that might deadlock
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                List<Integer> resources = Arrays.asList(threadId, (threadId + 1) % 5);
                try {
                    manager.acquireResources(resources, 3000);
                    try {
                        Thread.sleep(5000); // Hold for a long time
                    } finally {
                        manager.releaseResources(resources);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.setName("DeadlockTest-" + threadId);
            threads.add(thread);
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        // Check for deadlock after a delay
        try {
            Thread.sleep(1000);
            long[] deadlockedThreads = threadMX.findDeadlockedThreads();
            
            if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                System.out.println("🚨 Deadlock detected: " + deadlockedThreads.length + " threads");
            } else {
                System.out.println("✓ No deadlock detected");
            }
            
            // Interrupt all threads
            for (Thread t : threads) {
                t.interrupt();
            }
            
            for (Thread t : threads) {
                t.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * TODO: Implement ResourceManager class
     */
    static class Manager {
        private final Map<Integer, ReentrantLock> resources;
        private final Map<Integer, Thread> resourceOwners;
        private final AtomicBoolean deadlockDetected;
        
        public Manager(int numResources) {
            this.resources = new HashMap<>();
            this.resourceOwners = new ConcurrentHashMap<>();
            this.deadlockDetected = new AtomicBoolean(false);
            
            // TODO: Initialize resources với ReentrantLock
            for (int i = 0; i < numResources; i++) {
                // TODO: Put ReentrantLock vào map
                resources.put(i, new ReentrantLock());
            }
        }
        
        /**
         * TODO: Acquire multiple resources với lock ordering và timeout
         */
        public boolean acquireResources(List<Integer> resourceIds, long timeoutMs) throws InterruptedException {
            // TODO: Sort resource IDs để đảm bảo consistent ordering (tránh deadlock)
            List<Integer> sortedIds = resourceIds.stream().sorted(Comparator.reverseOrder()).toList(); // TODO: Sort resourceIds
            
            List<Lock> acquiredLocks = new ArrayList<>();
            long deadline = System.currentTimeMillis() + timeoutMs;
            
            try {
                // TODO: Acquire all locks theo thứ tự đã sort
                // TODO: Sử dụng tryLock với remaining time
                // TODO: Nếu timeout -> release all acquired locks và return false
                // TODO: Nếu thành công -> add vào acquiredLocks và track owner
                // TODO: Nếu tất cả thành công -> return true
                for (Integer id : sortedIds) {
                    ReentrantLock lock = resources.get(id);
                    long remainingTime = deadline - System.currentTimeMillis();
                    if (remainingTime <= 0 || !lock.tryLock(remainingTime, TimeUnit.MILLISECONDS)) {
                        releaseAcquiredLocks(acquiredLocks);
                        return false;
                    } else {
                        acquiredLocks.add(lock);
                        resourceOwners.put(id, Thread.currentThread());
                    }
                }
                return true;

            } catch (InterruptedException e) {
                // TODO: Release all acquired locks
                releaseAcquiredLocks(acquiredLocks);
                Thread.currentThread().interrupt();
                throw e;
            }
        }
        
        /**
         * TODO: Release multiple resources
         */
        public void releaseResources(List<Integer> resourceIds) {
            // TODO: Sort và release theo reverse order
            // TODO: Unlock từng lock và remove từ resourceOwners
            resourceIds.sort(Comparator.reverseOrder());

            for (Integer id : resourceIds) {
                ReentrantLock lock = resources.get(id);
                if(lock!=null && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    resourceOwners.remove(id);
                }
            }
        }
        
        private void releaseAcquiredLocks(List<Lock> locks) {
            Collections.reverse(locks);
            for (Lock lock : locks) {
                lock.unlock();
            }
            locks.clear();
        }
        
        public boolean isDeadlockDetected() {
            return deadlockDetected.get();
        }
        
        public Map<Integer, Thread> getResourceOwners() {
            return new HashMap<>(resourceOwners);
        }
    }
    
}

