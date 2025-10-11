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
    
    public static void main(String[] args) {
        System.out.println("=== Race Condition Demo ===\n");
        
        // Demo 1: Basic race condition with Counter
        demonstrateBasicRaceCondition();
        
        // Demo 2: Race condition with multiple operations
        demonstrateComplexRaceCondition();
        
        // Demo 3: Solution using synchronized methods
        demonstrateSynchronizedSolution();
        
        // Demo 4: Solution using AtomicInteger
        demonstrateAtomicSolution();
        
        // Demo 5: Race condition in array access
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
    private static void demonstrateBasicRaceCondition() {
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
                latch.countDown();
            });
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads, each incrementing " + 
                          INCREMENTS_PER_THREAD + " times...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads to complete using latch.await()
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
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
    private static void demonstrateComplexRaceCondition() {
        System.out.println("--- Demo 2: Complex Race Condition ---");
        
        // TODO: Create BankAccount with initial balance 1000
        BankAccount account = new BankAccount(1000);
        final int THREAD_COUNT = 5;
        final int OPERATIONS_PER_THREAD = 100;
        
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
                latch.countDown();
            });
        }
        
        System.out.println("Initial balance: " + account.getBalance());
        System.out.println("Starting " + THREAD_COUNT + " threads with mixed operations...");
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads using latch.await()
        
        System.out.println("Final balance: " + account.getBalance());
        System.out.println("Expected balance: " + (1000 + (THREAD_COUNT * OPERATIONS_PER_THREAD * 5) / 2));
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
                latch.countDown();
            });
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads with synchronized counter...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads using latch.await()
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
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
    private static void demonstrateAtomicSolution() {
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
                latch.countDown();
            });
        }
        
        System.out.println("Starting " + THREAD_COUNT + " threads with atomic counter...");
        System.out.println("Expected result: " + EXPECTED_RESULT);
        
        long startTime = System.currentTimeMillis();
        
        // TODO: Start all threads
        
        // TODO: Wait for all threads using latch.await()
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Actual result: " + counter.getValue());
        System.out.println("Race condition occurred: " + (counter.getValue() != EXPECTED_RESULT));
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
        
        Thread[] threads = new Thread[THREAD_COUNT];
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        
        // TODO: Create threads that increment array elements
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // TODO: Loop through all array elements
                // TODO: Increment each element: sharedArray[j] = sharedArray[j] + 1
                // TODO: Print completion message
                latch.countDown();
            });
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
        
        // TODO: Implement increment() method
        public void increment() {
            // TODO: Increment the value (this causes race condition!)
        }
        
        // TODO: Implement getValue() method
        public int getValue() {
            // TODO: Return the current value
            return 0; // Remove this line when you implement the method
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
        
        // TODO: Implement increment() method using atomic operation
        public void increment() {
            // TODO: Use value.incrementAndGet() for atomic increment
        }
        
        // TODO: Implement getValue() method
        public int getValue() {
            // TODO: Return value.get()
            return 0; // Remove this line when you implement the method
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
        }
        
        public void withdraw(double amount) {
            balance -= amount; // Race condition!
        }
        
        public double getBalance() {
            return balance;
        }
    }
}
