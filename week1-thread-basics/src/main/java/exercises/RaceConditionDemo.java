package exercises;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Race Condition Demo - Week 1 Exercise 2
 * 
 * TODO: Complete this exercise to understand race conditions!
 * 
 * Learning objectives:
 * - Multiple threads accessing shared resources without synchronization
 * - Inconsistent results due to race conditions
 * - Solutions using synchronized methods and atomic operations
 */
public class RaceConditionDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Race Condition Demo ===\n");
        
        // Demo 1: Basic race condition with Counter
        // TODO: Try running Demo 1 multiple times to see non-deterministic results
        // TODO: Experiment: change THREAD_COUNT/INCREMENTS_PER_THREAD to amplify race
        demonstrateBasicRaceCondition();
        
        // Demo 2: Race condition with multiple operations
        // TODO: Experiment: vary deposit/withdraw amounts to produce negative balances
        demonstrateComplexRaceCondition();
        
        // Demo 3: Solution using synchronized methods
        // TODO: Measure and compare performance vs. Demo 1
        demonstrateSynchronizedSolution();
        
        // Demo 4: Solution using AtomicInteger
        // TODO: Compare performance with Demo 3 and discuss differences
        demonstrateAtomicSolution();
        
        // Demo 5: Race condition in array access
        // TODO: Replace array with AtomicIntegerArray as an experiment
        demonstrateArrayRaceCondition();
    }
    
    /**
     * TODO: Demo 1 - Basic race condition demonstration
     * 
     * Your tasks:
     * 1. Create a Counter class with increment() method
     * 2. Create 10 threads, each incrementing the counter 1000 times
     * 3. Use CountDownLatch to wait for all threads to complete
     * 4. Compare actual result with expected result
     * 5. Observe the race condition in action!
     */
    private static void demonstrateBasicRaceCondition() throws InterruptedException {
        System.out.println("--- Demo 1: Basic Race Condition ---");
        
        final int THREAD_COUNT = 10;
        final int INCREMENTS_PER_THREAD = 1000;
        final int EXPECTED_RESULT = THREAD_COUNT * INCREMENTS_PER_THREAD;
        
        // TODO: Create a Counter instance
        Counter counter = new Counter();
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create and start threads
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                // TODO: Loop INCREMENTS_PER_THREAD times
                // TODO: Call counter.increment() (this causes race condition!)
                // TODO: Optional: add Thread.yield() or sleep(0-2ms) to increase interleaving

                for(int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                    Thread.yield();
                }

                latch.countDown();
            });
            // TODO: Name the threads for easier logging, e.g., threads[i].setName("Inc-"+i)
            threads[i].setName("Inc-" + i);
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads, each incrementing " + 
                          INCREMENTS_PER_THREAD + " times...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        for(int i = 0; i < THREAD_COUNT; i++) {
            threads[i].start();
        }
        // TODO: Wait for all threads to complete using latch.await()
        latch.await();
        // TODO: Alternative: use Thread.join() instead of CountDownLatch (compare APIs)
       /* for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }*/
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
        // TODO: Print the difference between expected and actual values
        System.out.println("Difference: " + (EXPECTED_RESULT - counter.getValue()));
        // TODO: Run this section in a loop (e.g., 10 times) to observe variance
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println();
    }
    
    /**
     * TODO: Demo 2 - Complex race condition with multiple operations
     * 
     * Your tasks:
     * 1. Create a BankAccount with initial balance 1000
     * 2. Create 5 threads, each doing 100 operations (deposit/withdraw)
     * 3. Use CountDownLatch to wait for all threads
     * 4. Compare final balance with expected balance
     * 5. Observe race condition in bank account operations
     */
    private static void demonstrateComplexRaceCondition() throws InterruptedException {
        System.out.println("--- Demo 2: Complex Race Condition ---");
        
        // TODO: Create BankAccount with initial balance 1000
        SynchronizedBankAccount account = new SynchronizedBankAccount(1000);
        final int THREAD_COUNT = 200;
        final int OPERATIONS_PER_THREAD = 10000;
        
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create threads with mixed operations
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // TODO: Loop OPERATIONS_PER_THREAD times
                // TODO: If j % 2 == 0, call account.deposit(10)
                // TODO: Else, call account.withdraw(5)
                // TODO: Print completion message
                // TODO: Optional: add random small sleep to simulate I/O/processing
                // TODO: Optional: capture and log per-thread net effect
                for(int j=0 ; j< OPERATIONS_PER_THREAD; j++) {
                    if(j % 2 == 0) {
                        account.deposit(10);
                    } else {
                        account.withdraw(5);
                    }
                    
                    // Add Thread.yield() to increase chance of race condition
                    //Thread.yield();
                    
                    // Only print every 1000 operations to reduce output
                    if (j % 1000 == 0) {
                        System.out.println("Thread " + threadId + " completed operation " + j + " on account balance: " + account.getBalance());
                    }
                }
                latch.countDown();
            });
            // TODO: Name worker threads (e.g., "Worker-" + threadId)
            threads[i].setName("Worker-" + threadId);
        }
        
        System.out.println("Initial balance: " + account.getBalance());
        System.out.println("Starting " + THREAD_COUNT + " threads with mixed operations...");
        
        // TODO: Start all threads
        for(int i = 0; i < THREAD_COUNT; i++) {
            threads[i].start();
        }
        // TODO: Wait for all threads using latch.await()
        latch.await();

        System.out.println("Final balance: " + account.getBalance());
        
        // Calculate expected balance correctly:
        // Each thread: 5000 deposits of 10 + 5000 withdrawals of 5 = 50000 - 25000 = 25000
        // 5 threads: 5 * 25000 = 125000
        // Initial: 1000, Expected: 1000 + 125000 = 126000
        int expectedBalance = 1000 + (THREAD_COUNT * OPERATIONS_PER_THREAD * 5) / 2;
        System.out.println("Expected balance: " + expectedBalance);
        System.out.println("Race condition occurred: " + (account.getBalance() != expectedBalance));
        // TODO: Discuss why expected may not match due to non-atomic read-modify-write
        // TODO: Implement a synchronized version of BankAccount and compare results
        System.out.println();
    }
    
    /**
     * TODO: Demo 3 - Solution using synchronized methods
     * 
     * Your tasks:
     * 1. Create SynchronizedCounter instance
     * 2. Create 10 threads, each incrementing 1000 times
     * 3. Use CountDownLatch to wait for completion
     * 4. Measure time taken and compare with expected result
     * 5. Observe that synchronized solution gives correct result
     */
    private static void demonstrateSynchronizedSolution() {
        System.out.println("--- Demo 3: Synchronized Solution ---");
        
        final int THREAD_COUNT = 10;
        final int INCREMENTS_PER_THREAD = 1000;
        final int EXPECTED_RESULT = THREAD_COUNT * INCREMENTS_PER_THREAD;
        
        // TODO: Create SynchronizedCounter instance
        SynchronizedCounter counter = new SynchronizedCounter();
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create threads that increment synchronized counter
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                // TODO: Loop INCREMENTS_PER_THREAD times
                // TODO: Call counter.increment() (this is thread-safe!)
                // TODO: Optional: add sleep to compare overhead when synchronized
                latch.countDown();
            });
            // TODO: Name the threads for clarity
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads with synchronized counter...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads using latch.await()
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
        // TODO: Compare time taken to Demo 1
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println();
    }
    
    /**
     * TODO: Demo 4 - Solution using AtomicInteger
     * 
     * Your tasks:
     * 1. Create AtomicCounter instance
     * 2. Create 10 threads, each incrementing 1000 times
     * 3. Use CountDownLatch to wait for completion
     * 4. Measure time taken and compare with expected result
     * 5. Observe that atomic solution gives correct result and better performance
     */
    private static void demonstrateAtomicSolution() throws InterruptedException {
        System.out.println("--- Demo 4: Atomic Solution ---");
        
        final int THREAD_COUNT = 10;
        final int INCREMENTS_PER_THREAD = 1000;
        final int EXPECTED_RESULT = THREAD_COUNT * INCREMENTS_PER_THREAD;
        
        // TODO: Create AtomicCounter instance
        AtomicCounter counter = new AtomicCounter();
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create threads that increment atomic counter
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                // TODO: Loop INCREMENTS_PER_THREAD times
                // TODO: Call counter.increment() (this is lock-free and thread-safe!)
                // TODO: Optional: add occasional Thread.yield() for fairness experiments
                for(int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                    System.out.println("Thread " + Thread.currentThread().getName() + " incremented to " + counter.getValue());
                    // Optional yield
                    if (j % 100 == 0) {
                        Thread.yield();
                    }
                }
                latch.countDown();
            });
            // TODO: Name threads
            threads[i].setName("AtomicInc-" + i);
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads with atomic counter...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].start();
        }
        latch.await();
        
        // TODO: Wait for all threads using latch.await()
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Expected result: " + EXPECTED_RESULT);
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
        // TODO: Compare throughput to synchronized solution
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println();
    }
    
    /**
     * TODO: Demo 5 - Race condition in array access
     * 
     * Your tasks:
     * 1. Create a shared array of size 100
     * 2. Create 5 threads, each incrementing all array elements
     * 3. Use CountDownLatch to wait for completion
     * 4. Check how many array elements have correct final values
     * 5. Observe race condition in array element access
     */
    private static void demonstrateArrayRaceCondition() {
        System.out.println("--- Demo 5: Array Race Condition ---");
        
        final int ARRAY_SIZE = 100;
        final int THREAD_COUNT = 5;
        // TODO: Create shared array
        int[] sharedArray = new int[ARRAY_SIZE];
        // TODO: Optional: try with AtomicIntegerArray to fix race per element
        
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create threads that increment array elements
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // TODO: Loop through all array elements
                // TODO: Increment each element: sharedArray[j] = sharedArray[j] + 1
                // TODO: Print completion message
                // TODO: Optional: randomize iteration order to increase collision probability
                latch.countDown();
            });
            // TODO: Name threads (e.g., "ArrayWorker-" + threadId)
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads to increment array elements...");
        System.out.println("Expected final values: " + THREAD_COUNT);
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads using latch.await()
        
        // TODO: Check results - count correct values
        int correctValues = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (sharedArray[i] == THREAD_COUNT) {
                correctValues++;
            }
        }
        
        System.out.println("Correct values: " + correctValues + "/" + ARRAY_SIZE);
        System.out.println("Race condition occurred: " + (correctValues < ARRAY_SIZE));
        // TODO: Identify which indices are incorrect and print a few samples
        System.out.println();
    }
    
    /**
     * TODO: Simple Counter class with race condition
     * 
     * Your tasks:
     * 1. Add a private int field called 'value'
     * 2. Implement increment() method that increments value
     * 3. Implement getValue() method that returns value
     * 
     * Note: This will have race condition because increment() is not thread-safe!
     */
    static class Counter {
        // TODO: Add private int field 'value' initialized to 0
        private int value = 0; // not thread-safe (intentionally for demo)
        // TODO: Optional: add getAndIncrement() returning previous value
        
        // TODO: Implement increment() method
        public void increment() {
            // TODO: Increment the value (this causes race condition!)
            value = value + 1;
            // TODO: Optional: simulate work with small sleep
        }
        
        // TODO: Implement getValue() method
        public int getValue() {
            // TODO: Return the current value
            return value;
        }
    }
    
    /**
     * TODO: Synchronized Counter class - thread-safe solution
     * 
     * Your tasks:
     * 1. Add a private int field called 'value'
     * 2. Implement synchronized increment() method
     * 3. Implement synchronized getValue() method
     * 
     * Note: The synchronized keyword makes these methods thread-safe!
     */
    static class SynchronizedCounter {
        // TODO: Add private int field 'value' initialized to 0
        // TODO: Optional: expose add(int delta) as synchronized method
        
        // TODO: Implement synchronized increment() method
        public synchronized void increment() {
            // TODO: Increment the value (this is thread-safe!)
        }
        
        // TODO: Implement synchronized getValue() method
        public synchronized int getValue() {
            // TODO: Return the current value
            return 0; // Remove this line when you implement the method
        }
    }
    
    /**
     * TODO: Atomic Counter class - lock-free thread-safe solution
     * 
     * Your tasks:
     * 1. Add an AtomicInteger field called 'value'
     * 2. Implement increment() method using atomic operation
     * 3. Implement getValue() method
     * 
     * Note: AtomicInteger provides lock-free thread-safe operations!
     */
    static class AtomicCounter {
        // TODO: Add AtomicInteger field 'value' initialized to 0
        // TODO: Optional: demonstrate addAndGet(int) vs incrementAndGet()
        private AtomicInteger value = new AtomicInteger(0);
        
        // TODO: Implement increment() method using atomic operation
        public void increment() {
            // TODO: Use value.incrementAndGet() for atomic increment
            value.incrementAndGet();
        }
        
        // TODO: Implement getValue() method
        public int getValue() {
            // TODO: Return value.get()
            //return 0; // Remove this line when you implement the method
            return value.get();
        }
    }
    
    /**
     * Bank Account class with race conditions
     */
    static class BankAccount {
        private double balance;
        
        public BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }
        
        public void deposit(double amount) {
            balance += amount; // Race condition!
            // TODO: Optional: add sleep to widen the race window
        }
        
        public void withdraw(double amount) {
            balance -= amount; // Race condition!
            // TODO: Optional: check for insufficient funds and log anomalies
        }
        
        public double getBalance() {
            return balance;
        }
        
        // TODO: Create a thread-safe variant using synchronized methods and compare behavior
    }
    
    /**
     * Synchronized Bank Account class - thread-safe solution
     */
    static class SynchronizedBankAccount {
        private double balance;
        
        public SynchronizedBankAccount(double initialBalance) {
            this.balance = initialBalance;
        }
        
        public synchronized void deposit(double amount) {
            balance += amount; // Thread-safe with synchronized!
        }
        
        public synchronized void withdraw(double amount) {
            balance -= amount; // Thread-safe with synchronized!
        }
        
        public synchronized double getBalance() {
            return balance;
        }
    }
}
