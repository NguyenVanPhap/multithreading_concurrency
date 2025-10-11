package exercises;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread Creation Demo - Week 1 Exercise 1
 * 
 * TODO: Complete this exercise to learn thread creation!
 * 
 * Tasks:
 * 1. Extending Thread class
 * 2. Implementing Runnable interface
 * 3. Using Lambda expressions
 * 4. Thread methods: start(), join(), sleep(), yield()
 */
public class ThreadCreationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Thread Creation Demo ===\n");
        
        // TODO 1: Thread vs Runnable comparison
        demonstrateThreadVsRunnable();
        
        // TODO 2: Thread methods (join, sleep, yield)
        demonstrateThreadMethods();
        
        // TODO 3: Creating multiple threads (10 threads, each count 1-100)
        demonstrateMultipleThreads();
        
        // TODO 4: Lambda expressions for thread creation
        demonstrateLambdaThreads();
    }
    
    /**
     * TODO: Demo 1 - Compare Thread class vs Runnable interface
     * 
     * Your tasks:
     * 1. Create a thread by extending Thread class
     * 2. Create a thread by implementing Runnable interface
     * 3. Start both threads and wait for them to complete using join()
     * 4. Observe the difference between the two approaches
     */
    private static void demonstrateThreadVsRunnable() {
        System.out.println("--- Demo 1: Thread vs Runnable ---");
        
        // TODO: Method 1 - Extending Thread class
        System.out.println("Creating thread by extending Thread class:");
        // TODO: Create MyThread instance and start it
        // TODO: Use join() to wait for completion
        
        System.out.println();
        
        // TODO: Method 2 - Implementing Runnable interface
        System.out.println("Creating thread by implementing Runnable:");
        // TODO: Create Thread with MyRunnable and start it
        // TODO: Use join() to wait for completion
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 2 - Demonstrate thread methods
     * 
     * Your tasks:
     * 1. Create a thread that uses Thread.sleep()
     * 2. Create a thread that uses Thread.yield()
     * 3. Start both threads and observe the behavior
     * 4. Use join() to wait for both threads to complete
     */
    private static void demonstrateThreadMethods() {
        System.out.println("--- Demo 2: Thread Methods ---");
        
        // TODO: Create a thread that sleeps for 2 seconds
        Thread sleepThread = new Thread(() -> {
            System.out.println("Sleep Thread: Starting...");
            // TODO: Add Thread.sleep(2000) with proper exception handling
            // TODO: Print message after waking up
        });
        
        // TODO: Create a thread that yields 5 times
        Thread yieldThread = new Thread(() -> {
            System.out.println("Yield Thread: Starting...");
            // TODO: Loop 5 times, print progress and call Thread.yield()
            // TODO: Print completion message
        });
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete using join()
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 3 - Create multiple threads (10 threads counting 1-100)
     * 
     * Your tasks:
     * 1. Create 10 threads, each counting from 1 to 100
     * 2. Each thread should print its progress every 20 numbers
     * 3. Start all threads
     * 4. Wait for all threads to complete using join()
     */
    private static void demonstrateMultipleThreads() {
        System.out.println("--- Demo 3: Multiple Threads ---");
        
        final int THREAD_COUNT = 10;
        Thread[] threads = new Thread[THREAD_COUNT];
        
        // TODO: Create 10 threads in a loop
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i + 1;
            threads[i] = new Thread(() -> {
                System.out.println("Thread " + threadId + " started");
                // TODO: Loop from 1 to 100
                // TODO: Print progress every 20 numbers
                System.out.println("Thread " + threadId + " completed");
            });
        }
        
        // TODO: Start all threads
        System.out.println("Starting " + THREAD_COUNT + " threads...");
        
        // TODO: Wait for all threads to complete using join()
        
        System.out.println("All threads completed!\n");
    }
    
    /**
     * TODO: Demo 4 - Lambda expressions for thread creation
     * 
     * Your tasks:
     * 1. Create a thread using lambda expression
     * 2. The thread should print a message, sleep for 1 second, then print goodbye
     * 3. Start the thread and wait for it to complete
     */
    private static void demonstrateLambdaThreads() {
        System.out.println("--- Demo 4: Lambda Threads ---");
        
        // TODO: Create a thread using lambda expression
        Thread lambdaThread = new Thread(() -> {
            // TODO: Print "Hello from lambda!"
            // TODO: Sleep for 1000ms with proper exception handling
            // TODO: Print "Goodbye!"
        });
        
        // TODO: Start the thread
        
        // TODO: Wait for thread to complete using join()
        
        System.out.println("Lambda demo completed!\n");
    }
    
    /**
     * TODO: Custom Thread class (extending Thread)
     * 
     * Your tasks:
     * 1. Complete the constructor
     * 2. Implement the run() method to count from start to end
     * 3. Add proper exception handling for InterruptedException
     * 4. Print progress messages
     */
    static class MyThread extends Thread {
        private final String name;
        private final int start;
        private final int end;
        
        // TODO: Complete the constructor
        public MyThread(String name, int start, int end) {
            // TODO: Call super constructor with name
            // TODO: Initialize instance variables
        }
        
        @Override
        public void run() {
            // TODO: Print start message
            
            // TODO: Loop from start to end
            // TODO: Print progress for each number
            // TODO: Add Thread.sleep(500) with exception handling
            
            // TODO: Print completion message
        }
    }
    
    /**
     * TODO: Custom Runnable class (implementing Runnable)
     * 
     * Your tasks:
     * 1. Complete the constructor
     * 2. Implement the run() method to count from start to end
     * 3. Add proper exception handling for InterruptedException
     * 4. Print progress messages
     */
    static class MyRunnable implements Runnable {
        private final String name;
        private final int start;
        private final int end;
        
        // TODO: Complete the constructor
        public MyRunnable(String name, int start, int end) {
            // TODO: Initialize instance variables
        }
        
        @Override
        public void run() {
            // TODO: Print start message
            
            // TODO: Loop from start to end
            // TODO: Print progress for each number
            // TODO: Add Thread.sleep(500) with exception handling
            
            // TODO: Print completion message
        }
    }
}
