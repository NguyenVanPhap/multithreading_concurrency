package exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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
                System.out.println(Thread.currentThread().getName() + " started");
                for (int op = 0; op < OPERATIONS_PER_THREAD; op++) {
                    resource.increment();
                }
            });
            thread.setName("Thread_"+ i);
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }

        // TODO: Wait for all threads to complete
        for (Thread t : threads) {
            try {
                t.join(); // Wait until thread t finishes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Final value: " + resource.getValue());
        System.out.println("Expected: " + (NUM_THREADS * OPERATIONS_PER_THREAD));
    }
    
    private static void testTryLockWithTimeout() {
        // Create a resource and a thread that holds the lock for a while
        SharedResource resource = new SharedResource();

        Thread holder = new Thread(() -> {
            System.out.println("Holder thread acquiring lock for 3 seconds...");
            resource.holdLockForSeconds(3);
            System.out.println("Holder thread released lock");
        });
        holder.setName("Holder");
        holder.start();

        // Give the holder a moment to acquire the lock
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Attempt to acquire with timeout while lock is held (expected: false)
        boolean acquiredWhileHeld = resource.tryLock(2);
        System.out.println("TryLock(2s) while held: " + acquiredWhileHeld);

        // Wait for holder to release
        try { holder.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Attempt again after release (expected: true)
        boolean acquiredAfterRelease = resource.tryLock(2);
        System.out.println("TryLock(2s) after release: " + acquiredAfterRelease);
    }
    
    private static void testFairness() {
        // TODO: Test unfair lock (default)
        System.out.println("Testing unfair lock...");
        testLockFairness(false);
        
        // TODO: Test fair lock
        System.out.println("\nTesting fair lock...");
        testLockFairness(true);
        
        System.out.println("\nNote: Fair locks guarantee FIFO but may be slower");

        // Additional barging demo to visualize difference under contention
        System.out.println("\nBarging demo with unfair lock...");
        testBargingFairness(false);

        System.out.println("\nBarging demo with fair lock...");
        testBargingFairness(true);

        // Targeted arrival-barging order demo
        System.out.println("\nArrival-barging order demo (unfair)...");
        testArrivalBarging(false);

        System.out.println("\nArrival-barging order demo (fair)...");
        testArrivalBarging(true);
    }
    
    private static void testLockFairness(boolean fair) {
        SharedResource resource = new SharedResource(fair);

        final int workers = 8;
        List<String> acquisitionOrder = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        // Pre-acquire the lock so workers queue up
        resource.lockDirect();
        try {
            // Create workers that will block on the lock, then record order when entering
            for (int i = 0; i < workers; i++) {
                Thread t = new Thread(() -> resource.enterAndRecord(acquisitionOrder));
                t.setName("W" + i);
                threads.add(t);
                t.start();
            }

            // Allow time for all workers to be queued on the lock
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        } finally {
            // Release so workers can proceed
            resource.unlockDirect();
        }

        // Wait for completion
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("Fair=" + fair + " acquisition order: " + acquisitionOrder);
    }

    // Demonstrate barging: an active thread may reacquire the lock repeatedly with unfair lock
    private static void testBargingFairness(boolean fair) {
        SharedResource resource = new SharedResource(fair);

        final int workerCount = 4;
        final long testDurationMs = 800;
        AtomicBoolean running = new AtomicBoolean(true);
        List<String> order = Collections.synchronizedList(new ArrayList<>());

        Thread hog = new Thread(() -> {
            while (running.get()) {
                resource.lockDirect();
                try {
                    order.add(Thread.currentThread().getName());
                } finally {
                    resource.unlockDirect();
                }
                try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        hog.setName("H");

        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < workerCount; i++) {
            Thread w = new Thread(() -> {
                while (running.get()) {
                    resource.lockDirect();
                    try {
                        order.add(Thread.currentThread().getName());
                    } finally {
                        resource.unlockDirect();
                    }
                    try { Thread.sleep(2); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            });
            w.setName("W" + i);
            workers.add(w);
        }

        hog.start();
        for (Thread w : workers) { w.start(); }

        try { Thread.sleep(testDurationMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        running.set(false);

        try { hog.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        for (Thread w : workers) { try { w.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }

        Map<String, Integer> counts = new ConcurrentHashMap<>();
        for (String name : order) {
            counts.merge(name, 1, Integer::sum);
        }
        System.out.println("Fair=" + fair + " counts: " + counts);
    }

    // Show that a late-arriving thread can cut ahead with unfair lock
    private static void testArrivalBarging(boolean fair) {
        SharedResource resource = new SharedResource(fair);
        List<String> order = Collections.synchronizedList(new ArrayList<>());

        // Stage: hold lock so initial workers queue
        resource.lockDirect();
        try {
            List<Thread> queued = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Thread w = new Thread(() -> resource.enterAndRecord(order));
                w.setName("Q" + i);
                queued.add(w);
                w.start();
            }

            // Give time to enqueue
            try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Late arrival starts right before unlock
            Thread late = new Thread(() -> resource.enterAndRecord(order));
            late.setName("LATE");
            late.start();

            // Tiny pause to align arrival near release
            try { Thread.sleep(5); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        } finally {
            resource.unlockDirect();
        }

        // Wait a bit for all to run
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("Fair=" + fair + " arrival-barging order: " + order);
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

        // Hold the lock for a specified number of seconds (helper for demos)
        public void holdLockForSeconds(int seconds) {
            lock.lock();
            try {
                try {
                    Thread.sleep(seconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                lock.unlock();
            }
        }

        // Helper for fairness demo: record order when entering critical section
        public void enterAndRecord(List<String> order) {
            lock.lock();
            try {
                order.add(Thread.currentThread().getName());
                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } finally {
                lock.unlock();
            }
        }

        // Helpers to stage threads by holding the lock externally
        public void lockDirect() {
            lock.lock();
        }

        public void unlockDirect() {
            lock.unlock();
        }
    }
}
