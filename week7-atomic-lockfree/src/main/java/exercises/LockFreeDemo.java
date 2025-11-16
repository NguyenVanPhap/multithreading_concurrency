package exercises;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 3: Lock-Free Demo
 * 
 * TODO Tasks:
 * 1. Lock-free counter
 * 2. Lock-free stack (basic)
 * 3. Lock-free queue (basic)
 */
public class LockFreeDemo {
    
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Lock-Free Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test lock-free counter
        System.out.println("Test 1: Lock-Free Counter");
        testLockFreeCounter();
        
        // TODO: Test lock-free stack
        System.out.println("\nTest 2: Lock-Free Stack");
        testLockFreeStack();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test lock-free counter
     */
    private static void testLockFreeCounter() {
        // TODO: Implement lock-free counter với AtomicInteger
        LockFreeCounter counter = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Multiple threads increment
        // TODO: Wait for completion
        // TODO: In ra final value
    }
    
    /**
     * TODO: Test lock-free stack
     */
    private static void testLockFreeStack() {
        // TODO: Implement basic lock-free stack
        LockFreeStack<Integer> stack = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Multiple threads push và pop
        // TODO: Wait for completion
        // TODO: In ra final state
    }
    
    /**
     * TODO: Implement LockFreeCounter
     */
    static class LockFreeCounter {
        private final AtomicInteger count;
        
        public LockFreeCounter() {
            this.count = null; // TODO: Create AtomicInteger với 0
        }
        
        public void increment() {
            // TODO: Increment atomically
        }
        
        public int get() {
            // TODO: Return current value
            return 0; // TODO: Return count
        }
    }
    
    /**
     * TODO: Implement LockFreeStack (simplified)
     */
    static class LockFreeStack<T> {
        // TODO: Use AtomicReference cho head node
        
        public void push(T item) {
            // TODO: Lock-free push với CAS
        }
        
        public T pop() {
            // TODO: Lock-free pop với CAS
            return null; // TODO: Return popped item
        }
        
        public boolean isEmpty() {
            // TODO: Check if empty
            return false; // TODO: Return true if empty
        }
    }
}

