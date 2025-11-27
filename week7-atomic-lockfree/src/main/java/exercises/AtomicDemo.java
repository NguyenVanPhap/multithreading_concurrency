package exercises;

import java.util.concurrent.atomic.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 1: Atomic Demo
 * 
 * TODO Tasks:
 * 1. AtomicInteger operations
 * 2. AtomicLong operations
 * 3. AtomicBoolean operations
 * 4. Compare-and-Swap operations
 */
public class AtomicDemo {
    
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Atomic Operations Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test AtomicInteger
        System.out.println("Test 1: AtomicInteger");
        testAtomicInteger();
        
        // TODO: Test AtomicLong
        System.out.println("\nTest 2: AtomicLong");
        testAtomicLong();
        
        // TODO: Test AtomicBoolean
        System.out.println("\nTest 3: AtomicBoolean");
        testAtomicBoolean();
        
        // TODO: Test CAS operations
        System.out.println("\nTest 4: Compare-And-Swap");
        testCompareAndSwap();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test AtomicInteger
     */
    private static void testAtomicInteger() {
        // TODO: Tạo AtomicInteger với initial value 0
        AtomicInteger counter = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Submit NUM_THREADS tasks
        // TODO: Mỗi task thực hiện OPERATIONS_PER_THREAD increments
        // TODO: Sử dụng incrementAndGet() hoặc getAndIncrement()
        
        // TODO: Wait for all threads
        // TODO: In ra final value (should be NUM_THREADS * OPERATIONS_PER_THREAD)
    }
    
    /**
     * TODO: Test AtomicLong
     */
    private static void testAtomicLong() {
        // TODO: Tạo AtomicLong
        AtomicLong sum = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Multiple threads add values
        // TODO: Sử dụng addAndGet() hoặc getAndAdd()
        // TODO: In ra final sum
    }
    
    /**
     * TODO: Test AtomicBoolean
     */
    private static void testAtomicBoolean() {
        // TODO: Tạo AtomicBoolean
        AtomicBoolean flag = null; // TODO: Create với initial false
        
        // TODO: Test compareAndSet
        // TODO: Test getAndSet
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test Compare-And-Swap operations
     */
    private static void testCompareAndSwap() {
        AtomicInteger value = null; // TODO: Create với initial 0
        
        // TODO: CAS loop pattern
        // TODO: Get expected value
        // TODO: Calculate new value
        // TODO: Use compareAndSet() trong loop
        // TODO: Retry nếu CAS fails
        // TODO: In ra kết quả
    }
}

