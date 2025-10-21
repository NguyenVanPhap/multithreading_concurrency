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
        Thread t1 = new MyThread("MyThread-1", 1, 5);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main interrupted while waiting for MyThread-1");
        }
        
        System.out.println();
        
        // TODO: Method 2 - Implementing Runnable interface
        System.out.println("Creating thread by implementing Runnable:");
        // TODO: Create Thread with MyRunnable and start it
        // TODO: Use join() to wait for completion
        Thread t2 = new Thread(new MyRunnable("MyRunnable-1", 1, 5), "MyRunnable-1");
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main interrupted while waiting for MyRunnable-1");
        }
        
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
            try {
                Thread.sleep(2000);
                System.out.println("Sleep Thread: Woke up after 2 seconds");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Sleep Thread: Interrupted while sleeping");
            }
        });
        
        // TODO: Create a thread that yields 5 times
        Thread yieldThread = new Thread(() -> {
            System.out.println("Yield Thread: Starting...");
            // TODO: Loop 5 times, print progress and call Thread.yield()
            // TODO: Print completion message
            for (int i = 1; i <= 5; i++) {
                System.out.println("Yield Thread: step " + i);
                Thread.yield();
            }
            System.out.println("Yield Thread: Completed yielding");
        });
        
        // TODO: Start both threads
        sleepThread.start();
        yieldThread.start();
        
        // TODO: Wait for both threads to complete using join()
        try {
            sleepThread.join();
            yieldThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main interrupted while waiting for demo 2 threads");
        }
        
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
                for (int n = 1; n <= 100; n++) {
                    if (n % 20 == 0) {
                        System.out.println("Thread " + threadId + " progress: " + n);
                    }
                    Thread.yield();
                }
                System.out.println("Thread " + threadId + " completed");
            });
        }
        
        // TODO: Start all threads
        System.out.println("Starting " + THREAD_COUNT + " threads...");
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].start();
        }
        
        // TODO: Wait for all threads to complete using join()
        for (int i = 0; i < THREAD_COUNT; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main interrupted while waiting for Thread " + (i + 1));
            }
        }
        
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
            System.out.println("Hello from lambda!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Lambda thread interrupted while sleeping");
            }
            System.out.println("Goodbye!");
        });
        
        // TODO: Start the thread
        lambdaThread.start();
        
        // TODO: Wait for thread to complete using join()
        try {
            lambdaThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main interrupted while waiting for lambda thread");
        }
        
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
            super(name);
            this.name = name;
            this.start = start;
            this.end = end;
        }
        
        @Override
        public void run() {
            // TODO: Print start message
            
            // TODO: Loop from start to end
            // TODO: Print progress for each number
            // TODO: Add Thread.sleep(500) with exception handling
            
            // TODO: Print completion message
            System.out.println(name + ": Starting count from " + start + " to " + end);
            for (int i = start; i <= end; i++) {
                System.out.println(name + ": " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + ": Interrupted while sleeping");
                    return;
                }
            }
            System.out.println(name + ": Completed");
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
            this.name = name;
            this.start = start;
            this.end = end;
        }
        
        @Override
        public void run() {
            // TODO: Print start message
            
            // TODO: Loop from start to end
            // TODO: Print progress for each number
            // TODO: Add Thread.sleep(500) with exception handling
            
            // TODO: Print completion message
            System.out.println(name + ": Starting count from " + start + " to " + end);
            for (int i = start; i <= end; i++) {
                System.out.println(name + ": " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + ": Interrupted while sleeping");
                    return;
                }
            }
            System.out.println(name + ": Completed");
        }
    }
}
