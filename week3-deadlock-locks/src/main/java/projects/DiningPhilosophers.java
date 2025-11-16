package projects;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project 1: Dining Philosophers Problem
 * 
 * Classic deadlock problem: 5 philosophers sit around a table,
 * each needs 2 chopsticks to eat. Deadlock occurs when all
 * philosophers hold one chopstick and wait for the second.
 * 
 * TODO Tasks:
 * 1. Implement Philosopher class với eating/thinking
 * 2. Implement Chopstick resource
 * 3. Tạo deadlock scenario
 * 4. Fix deadlock với lock ordering
 * 5. Fix deadlock với timeout
 * 6. Fix deadlock với odd-even strategy
 * 7. Statistics tracking
 */
public class DiningPhilosophers {
    
    private static final int NUM_PHILOSOPHERS = 5;
    private static final int EATING_TIME_MS = 100;
    private static final int THINKING_TIME_MS = 50;
    private static final int SIMULATION_DURATION_MS = 5000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Dining Philosophers Problem");
        System.out.println("==========================================\n");
        
        // TODO: Test deadlock scenario
        System.out.println("Test 1: Deadlock Scenario");
        testDeadlockScenario();
        
        // TODO: Test với lock ordering
        System.out.println("\nTest 2: Lock Ordering Solution");
        testLockOrdering();
        
        // TODO: Test với timeout
        System.out.println("\nTest 3: Timeout Solution");
        testTimeoutSolution();
        
        // TODO: Test với odd-even strategy
        System.out.println("\nTest 4: Odd-Even Strategy");
        testOddEvenStrategy();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test deadlock scenario
     * Tất cả philosophers đều acquire left chopstick trước, rồi chờ right
     */
    private static void testDeadlockScenario() {
        Chopstick[] chopsticks = new Chopstick[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        AtomicBoolean running = new AtomicBoolean(true);
        
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Chopstick left = chopsticks[i];
            Chopstick right = chopsticks[(i + 1) % NUM_PHILOSOPHERS];
            
            // TODO: Create philosopher với deadlock-prone behavior
            // Tất cả đều: left -> right (causes deadlock!)
            philosophers[i] = new Philosopher(i, left, right, running, false, false);
        }
        
        // Start all philosophers
        for (Philosopher p : philosophers) {
            p.start();
        }
        
        // Run for a while
        try {
            Thread.sleep(SIMULATION_DURATION_MS);
            running.set(false);
            
            // Wait for all to finish
            for (Philosopher p : philosophers) {
                p.join(1000);
            }
            
            // Print statistics
            System.out.println("\nStatistics:");
            for (Philosopher p : philosophers) {
                System.out.println("  Philosopher " + p.getId() + ": " + 
                    p.getEatCount() + " meals, " + 
                    p.getTotalEatingTime() + "ms eating");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * TODO: Test với lock ordering
     * Sort chopsticks by ID để đảm bảo consistent ordering
     */
    private static void testLockOrdering() {
        Chopstick[] chopsticks = new Chopstick[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        AtomicBoolean running = new AtomicBoolean(true);
        
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Chopstick left = chopsticks[i];
            Chopstick right = chopsticks[(i + 1) % NUM_PHILOSOPHERS];
            
            // TODO: Create philosopher với lock ordering
            // Always acquire lower ID first
            philosophers[i] = new Philosopher(i, left, right, running, true, false);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
        
        try {
            Thread.sleep(SIMULATION_DURATION_MS);
            running.set(false);
            
            for (Philosopher p : philosophers) {
                p.join(1000);
            }
            
            System.out.println("\nStatistics:");
            int totalMeals = 0;
            for (Philosopher p : philosophers) {
                totalMeals += p.getEatCount();
                System.out.println("  Philosopher " + p.getId() + ": " + p.getEatCount() + " meals");
            }
            System.out.println("  Total meals: " + totalMeals);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * TODO: Test với timeout solution
     * Sử dụng tryLock với timeout để tránh deadlock
     */
    private static void testTimeoutSolution() {
        Chopstick[] chopsticks = new Chopstick[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        AtomicBoolean running = new AtomicBoolean(true);
        
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Chopstick left = chopsticks[i];
            Chopstick right = chopsticks[(i + 1) % NUM_PHILOSOPHERS];
            
            // TODO: Create philosopher với timeout
            philosophers[i] = new Philosopher(i, left, right, running, false, true);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
        
        try {
            Thread.sleep(SIMULATION_DURATION_MS);
            running.set(false);
            
            for (Philosopher p : philosophers) {
                p.join(1000);
            }
            
            System.out.println("\nStatistics:");
            int totalMeals = 0;
            for (Philosopher p : philosophers) {
                totalMeals += p.getEatCount();
                System.out.println("  Philosopher " + p.getId() + ": " + p.getEatCount() + " meals");
            }
            System.out.println("  Total meals: " + totalMeals);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * TODO: Test với odd-even strategy
     * Odd philosophers: left -> right
     * Even philosophers: right -> left
     */
    private static void testOddEvenStrategy() {
        Chopstick[] chopsticks = new Chopstick[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        AtomicBoolean running = new AtomicBoolean(true);
        
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Chopstick left = chopsticks[i];
            Chopstick right = chopsticks[(i + 1) % NUM_PHILOSOPHERS];
            
            // TODO: Create philosopher với odd-even strategy
            // Odd: left->right, Even: right->left
            boolean useOddEven = true;
            philosophers[i] = new Philosopher(i, left, right, running, false, false, useOddEven);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
        
        try {
            Thread.sleep(SIMULATION_DURATION_MS);
            running.set(false);
            
            for (Philosopher p : philosophers) {
                p.join(1000);
            }
            
            System.out.println("\nStatistics:");
            int totalMeals = 0;
            for (Philosopher p : philosophers) {
                totalMeals += p.getEatCount();
                System.out.println("  Philosopher " + p.getId() + ": " + p.getEatCount() + " meals");
            }
            System.out.println("  Total meals: " + totalMeals);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * TODO: Implement Philosopher class
     */
    static class Philosopher extends Thread {
        private final int id;
        private final Chopstick left;
        private final Chopstick right;
        private final AtomicBoolean running;
        private final boolean useLockOrdering;
        private final boolean useTimeout;
        private final boolean useOddEven;
        
        private int eatCount = 0;
        private long totalEatingTime = 0;
        
        public Philosopher(int id, Chopstick left, Chopstick right, AtomicBoolean running, 
                          boolean useLockOrdering, boolean useTimeout) {
            this(id, left, right, running, useLockOrdering, useTimeout, false);
        }
        
        public Philosopher(int id, Chopstick left, Chopstick right, AtomicBoolean running,
                          boolean useLockOrdering, boolean useTimeout, boolean useOddEven) {
            this.id = id;
            this.left = left;
            this.right = right;
            this.running = running;
            this.useLockOrdering = useLockOrdering;
            this.useTimeout = useTimeout;
            this.useOddEven = useOddEven;
            this.setName("Philosopher-" + id);
        }
        
        @Override
        public void run() {
            while (running.get()) {
                think();
                eat();
            }
        }
        
        private void think() {
            try {
                Thread.sleep(THINKING_TIME_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private void eat() {
            boolean acquired = false;
            
            try {
                if (useTimeout) {
                    // TODO: Use timeout solution
                    // TODO: Try to acquire left chopstick với timeout
                    // TODO: Nếu thành công, try to acquire right chopstick với timeout
                    // TODO: Nếu right timeout, release left
                    // TODO: Handle InterruptedException
                } else if (useLockOrdering) {
                    // TODO: Use lock ordering solution
                    // TODO: Determine first và second chopstick (lower ID first)
                    // TODO: Acquire first, then second
                    // TODO: Set acquired = true nếu thành công
                    // TODO: Nếu không acquired, unlock first trong finally
                } else if (useOddEven) {
                    // TODO: Use odd-even strategy
                    // TODO: If id is even: right -> left
                    // TODO: If id is odd: left -> right
                    // TODO: Acquire theo thứ tự đã chọn
                    // TODO: Set acquired = true nếu thành công
                } else {
                    // TODO: Deadlock-prone: left -> right (all philosophers)
                    // TODO: Acquire left, then right
                    // TODO: Set acquired = true nếu thành công
                }
                
                if (acquired) {
                    // TODO: Eating - sleep EATING_TIME_MS
                    // TODO: Track eating time
                    // TODO: Increment eatCount và totalEatingTime
                    
                    // TODO: Release chopsticks
                    if (useTimeout) {
                        // TODO: Put down both chopsticks
                    } else {
                        // TODO: Release in reverse order (theo strategy đã dùng)
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        public int getId() {
            return id;
        }
        
        public int getEatCount() {
            return eatCount;
        }
        
        public long getTotalEatingTime() {
            return totalEatingTime;
        }
    }
    
    /**
     * TODO: Implement Chopstick class
     */
    static class Chopstick {
        private final int id;
        private final Lock lock;
        
        public Chopstick(int id) {
            this.id = id;
            // TODO: Initialize lock với ReentrantLock
            this.lock = null; // TODO: Use new ReentrantLock()
        }
        
        public int getId() {
            return id;
        }
        
        // TODO: Implement tryPickUp với timeout
        public boolean tryPickUp(long timeout, TimeUnit unit) throws InterruptedException {
            // TODO: Use lock.tryLock với timeout
            return false; // TODO: Return result
        }
        
        // TODO: Implement putDown
        public void putDown() {
            // TODO: Unlock
        }
        
        // For non-timeout solutions
        public void lock() {
            // TODO: Lock
        }
        
        public void unlock() {
            // TODO: Unlock
        }
    }
}

