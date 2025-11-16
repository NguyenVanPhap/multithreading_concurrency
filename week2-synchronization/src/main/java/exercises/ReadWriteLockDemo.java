package exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Exercise 3: ReadWriteLock Demo
 * 
 * TODO Tasks:
 * 1. Implement ReadWriteCounter with ReadWriteLock
 * 2. Create multiple reader threads (read-heavy)
 * 3. Create few writer threads
 * 4. Observe concurrent reading behavior
 * 5. Measure performance vs synchronized
 * 6. Demonstrate lock downgrading (write → read)
 */
public class ReadWriteLockDemo {
    
    private static final int NUM_READERS = 10;
    private static final int NUM_WRITERS = 2;
    private static final int READ_OPERATIONS = 10000;
    private static final int WRITE_OPERATIONS = 1000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ReadWriteLock Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test ReadWriteCounter
        System.out.println("Test 1: ReadWriteLock Performance");
        testReadWriteLock();
        
        // TODO: Compare with synchronized
        System.out.println("\nTest 2: Synchronized Performance (for comparison)");
        testSynchronizedCounter();
        
        // TODO: Test lock downgrading
        System.out.println("\nTest 3: Lock Downgrading");
        testLockDowngrading();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void testReadWriteLock() {
        // TODO: Implement ReadWriteCounter
        ReadWriteCounter counter = new ReadWriteCounter();
        
        List<Thread> readers = new ArrayList<>();
        List<Thread> writers = new ArrayList<>();
        
        // TODO: Create reader threads
        // Each reader should read READ_OPERATIONS times
        for (int i = 0; i < NUM_READERS; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < READ_OPERATIONS; j++) {
                    counter.increment();
                }
            });
            thread.setName("Thread #" + i);
            readers.add(thread);
        }
        
        // TODO: Create writer threads
        // Each writer should write WRITE_OPERATIONS times
        
        long start = System.nanoTime();
        
        // TODO: Start all threads
        // TODO: Wait for all threads to complete
        
        long end = System.nanoTime();
        long duration = (end - start) / 1_000_000;
        
        System.out.println("ReadWriteLock time: " + duration + "ms");
        System.out.println("Final value: " + counter.getValue());
    }
    
    private static void testSynchronizedCounter() {
        // TODO: Implement SynchronizedCounter with synchronized keyword
        SynchronizedCounter counter = new SynchronizedCounter();
        
        List<Thread> readers = new ArrayList<>();
        List<Thread> writers = new ArrayList<>();
        
        // TODO: Create same number of reader and writer threads
        // TODO: Each thread does same number of operations
        
        long start = System.nanoTime();
        
        // TODO: Start all threads
        // TODO: Wait for all threads to complete
        
        long end = System.nanoTime();
        long duration = (end - start) / 1_000_000;
        
        System.out.println("Synchronized time: " + duration + "ms");
        System.out.println("Note: This is slower because readers can't run concurrently");
    }
    
    private static void testLockDowngrading() {
        // TODO: Demonstrate downgrading from write lock to read lock
        
        ReadWriteCounter counter = new ReadWriteCounter();
        
        // TODO: Acquire write lock
        // TODO: Do write operations
        // TODO: Downgrade to read lock (without releasing write lock first!)
        // TODO: Do read operations
        // TODO: Unlock read lock
        
        System.out.println("Lock downgrading: Writers can temporarily become readers");
    }
    
    // TODO: Implement this class
    // Counter using ReadWriteLock
    static class ReadWriteCounter {
        private final ReadWriteLock rwLock;
        private final Random random;
        private int value = 0;
        
        public ReadWriteCounter() {
            // TODO: Initialize ReadWriteLock
            this.rwLock = new ReentrantReadWriteLock();
            this.random = new Random();
        }
        
        // TODO: Implement read operation with read lock
        public int read() {
            // TODO: Acquire read lock
            // TODO: Return value
            // TODO: Always unlock read lock in finally
            return 0;
        }
        
        // TODO: Implement write operation with write lock
        public void write(int newValue) {
            // TODO: Acquire write lock
            // TODO: Update value
            // TODO: Always unlock write lock in finally
        }
        
        // TODO: Implement increment operation
        public void increment() {
            // TODO: Get write lock
            // TODO: Increment value
            // TODO: Unlock
        }
        
        public int getValue() {
            return value;
        }
        
        // TODO: Implement upgrade/downgrade method
        public void readThenUpdate() {
            // TODO: Demonstrate lock upgrade/downgrade
            // Hint: Read → Upgrade to write → Downgrade to read → Unlock read
        }
    }
    
    // TODO: Implement this class
    // Counter using synchronized for comparison
    static class SynchronizedCounter {
        private int value = 0;
        private final Random random;
        
        public SynchronizedCounter() {
            this.random = new Random();
        }
        
        // TODO: Implement synchronized read
        public synchronized int read() {
            // TODO: Simulate some work
            return value;
        }
        
        // TODO: Implement synchronized write
        public synchronized void write(int newValue) {
            // TODO: Update value
            this.value = newValue;
        }
        
        // TODO: Implement synchronized increment
        public synchronized void increment() {
            value++;
        }
        
        public int getValue() {
            return value;
        }
    }
}
