package exercises;

import java.util.concurrent.TimeUnit;

/**
 * Thread Methods Demo - Week 1 Exercise
 * 
 * TODO: Complete this exercise to learn thread methods!
 * 
 * Your learning tasks:
 * - join(): Wait for thread completion
 * - sleep(): Pause thread execution
 * - yield(): Give other threads a chance
 * - interrupt(): Interrupt thread execution
 * - isInterrupted(): Check interruption status
 * - getName(), setName(): Thread naming
 * - getPriority(), setPriority(): Thread priority
 * - getState(): Thread state monitoring
 */
public class ThreadMethodsDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Thread Methods Demo ===\n");
        
        // Demo 1: join() method
        demonstrateJoin();
        
        // Demo 2: sleep() method
        demonstrateSleep();
        
        // Demo 3: yield() method
        demonstrateYield();
        
        // Demo 4: interrupt() method
        demonstrateInterrupt();
        
        // Demo 5: Thread naming and priority
        demonstrateNamingAndPriority();
        
        // Demo 6: Thread states
        demonstrateThreadStates();
        
        // Demo 7: Combined example
        demonstrateCombined();
    }
    
    /**
     * TODO: Demo 1 - join() method - Wait for thread completion
     * 
     * Your tasks:
     * 1. Create two worker threads that do different amounts of work
     * 2. Start both threads
     * 3. Use join() to wait for both to complete
     * 4. Measure and display the total time taken
     */
    private static void demonstrateJoin() {
        System.out.println("--- Demo 1: join() Method ---");
        
        // TODO: Create Worker 1 thread that sleeps for 2000ms
        Thread worker1 = new Thread(() -> {
            System.out.println("Worker 1: Starting work...");
            // TODO: Add Thread.sleep(2000) with exception handling
            // TODO: Print completion message
        });
        
        // TODO: Create Worker 2 thread that sleeps for 1500ms
        Thread worker2 = new Thread(() -> {
            System.out.println("Worker 2: Starting work...");
            // TODO: Add Thread.sleep(1500) with exception handling
            // TODO: Print completion message
        });
        
        // TODO: Record start time
        long startTime = System.currentTimeMillis();
        
        // TODO: Start both workers
        
        // TODO: Wait for both workers to complete using join()
        
        // TODO: Calculate and display total time
        long endTime = System.currentTimeMillis();
        System.out.println("All workers completed in " + (endTime - startTime) + " ms\n");
    }
    
    /**
     * TODO: Demo 2 - sleep() method - Pause thread execution
     * 
     * Your tasks:
     * 1. Create a sleeper thread that sleeps for 3 seconds
     * 2. Create a monitor thread that counts seconds
     * 3. Start both threads and observe the timing
     */
    private static void demonstrateSleep() {
        System.out.println("--- Demo 2: sleep() Method ---");
        
        // TODO: Create a sleeper thread
        Thread sleeper = new Thread(() -> {
            System.out.println("Sleeper: Going to sleep...");
            // TODO: Sleep for 3 seconds with proper exception handling
            // TODO: Print wake-up message
        });
        
        // TODO: Create a monitor thread that counts seconds
        Thread monitor = new Thread(() -> {
            // TODO: Loop 3 times, sleep 1 second each, print count
        });
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 3 - yield() method - Give other threads a chance
     * 
     * Your tasks:
     * 1. Create a greedy thread that does intensive work without yielding
     * 2. Create a polite thread that yields after each work unit
     * 3. Start both threads and observe the difference in behavior
     */
    private static void demonstrateYield() {
        System.out.println("--- Demo 3: yield() Method ---");
        
        // TODO: Create a greedy thread (no yielding)
        Thread greedyThread = new Thread(() -> {
            System.out.println("Greedy Thread: Starting intensive work...");
            // TODO: Loop 5 times, do busy work without yielding
            // TODO: Print progress and add busy work loop
            System.out.println("Greedy Thread: Finished!");
        });
        
        // TODO: Create a polite thread (with yielding)
        Thread politeThread = new Thread(() -> {
            System.out.println("Polite Thread: Starting cooperative work...");
            // TODO: Loop 5 times, yield after each iteration
            // TODO: Print progress, call Thread.yield(), and sleep briefly
            System.out.println("Polite Thread: Finished!");
        });
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 4 - interrupt() method - Interrupt thread execution
     * 
     * Your tasks:
     * 1. Create a long-running thread that checks for interruption
     * 2. Create an interrupter thread that sends interrupt signal
     * 3. Handle InterruptedException properly
     * 4. Observe graceful interruption handling
     */
    private static void demonstrateInterrupt() {
        System.out.println("--- Demo 4: interrupt() Method ---");
        
        // TODO: Create long-running thread
        Thread longRunningThread = new Thread(() -> {
            System.out.println("Long Running Thread: Starting...");
            try {
                // TODO: Loop 10 times
                // TODO: Check Thread.currentThread().isInterrupted() and return if interrupted
                // TODO: Print work progress
                // TODO: Sleep for 500ms
                System.out.println("Long Running Thread: Completed normally");
            } catch (InterruptedException e) {
                // TODO: Print interruption message
                // TODO: Call Thread.currentThread().interrupt() to restore interrupted status
            }
        });
        
        // TODO: Create interrupter thread
        Thread interrupter = new Thread(() -> {
            try {
                // TODO: Sleep for 2500ms
                // TODO: Print interrupt signal message
                // TODO: Call longRunningThread.interrupt()
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 5 - Thread naming and priority
     * 
     * Your tasks:
     * 1. Create threads with custom names
     * 2. Set different priorities (MIN_PRIORITY vs MAX_PRIORITY)
     * 3. Display thread names and priorities
     * 4. Observe priority effects on scheduling
     */
    private static void demonstrateNamingAndPriority() {
        System.out.println("--- Demo 5: Naming and Priority ---");
        
        // TODO: Create low priority thread with custom name
        Thread lowPriorityThread = new Thread(() -> {
            // TODO: Print thread name and priority
            // TODO: Loop 3 times, print work progress, and call Thread.yield()
        }, "LowPriority-Thread");
        
        // TODO: Create high priority thread with custom name
        Thread highPriorityThread = new Thread(() -> {
            // TODO: Print thread name and priority
            // TODO: Loop 3 times, print work progress, and call Thread.yield()
        }, "HighPriority-Thread");
        
        // TODO: Set priorities
        // TODO: Set lowPriorityThread to MIN_PRIORITY
        // TODO: Set highPriorityThread to MAX_PRIORITY
        
        // TODO: Display main thread info
        Thread currentThread = Thread.currentThread();
        System.out.println("Main Thread: " + currentThread.getName() + 
                         " (Priority: " + currentThread.getPriority() + ")");
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 6 - Thread states monitoring
     * 
     * Your tasks:
     * 1. Create a state thread that runs and sleeps
     * 2. Create a monitor thread that watches state changes
     * 3. Display initial and final thread states
     * 4. Observe state transitions (NEW → RUNNABLE → TIMED_WAITING → TERMINATED)
     */
    private static void demonstrateThreadStates() {
        System.out.println("--- Demo 6: Thread States ---");
        
        // TODO: Create state thread
        Thread stateThread = new Thread(() -> {
            try {
                System.out.println("State Thread: Running...");
                // TODO: Sleep for 2000ms
                System.out.println("State Thread: Still running...");
                // TODO: Sleep for 1000ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "State-Monitor");
        
        // TODO: Create monitor thread
        Thread monitor = new Thread(() -> {
            try {
                // TODO: While stateThread.isAlive(), monitor its state
                // TODO: Print state every 500ms
                // TODO: Print final state when thread dies
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "State-Monitor");
        
        // TODO: Print initial state
        System.out.println("Initial state: " + stateThread.getState());
        
        // TODO: Start both threads
        
        // TODO: Wait for both threads to complete
        
        System.out.println();
    }
    
    /**
     * TODO: Demo 7 - Combined example using multiple methods
     * 
     * Your tasks:
     * 1. Create a Runnable task with interruption handling
     * 2. Create multiple worker threads with different priorities
     * 3. Use join() with timeout
     * 4. Handle timeout by interrupting threads
     * 5. Display final thread states
     */
    private static void demonstrateCombined() {
        System.out.println("--- Demo 7: Combined Example ---");
        
        // TODO: Create a Runnable task
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": Starting task...");
            
            // TODO: Loop 10 times
            // TODO: Check for interruption and return if interrupted
            // TODO: Print step progress
            // TODO: Sleep for 300ms with exception handling
            // TODO: Yield every 3 steps
            
            System.out.println(threadName + ": Task completed successfully!");
        };
        
        // TODO: Create multiple worker threads
        Thread[] workers = new Thread[3];
        for (int i = 0; i < workers.length; i++) {
            // TODO: Create worker thread with name "Worker-" + (i + 1)
            // TODO: Set priority to NORM_PRIORITY + i
        }
        
        // TODO: Start all workers
        System.out.println("Starting all workers...");
        
        // TODO: Wait for all workers with timeout (5000ms per worker)
        // TODO: If worker is still alive after timeout, interrupt it
        // TODO: Wait for interrupted worker to finish
        
        System.out.println("All workers finished!");
        
        // TODO: Display final states of all workers
        for (Thread worker : workers) {
            System.out.println(worker.getName() + " final state: " + worker.getState());
        }
    }
}
