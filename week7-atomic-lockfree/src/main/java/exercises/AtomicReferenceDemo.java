package exercises;

import java.util.concurrent.atomic.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 2: AtomicReference Demo
 * 
 * TODO Tasks:
 * 1. AtomicReference
 * 2. AtomicStampedReference (tránh ABA problem)
 * 3. Compare-and-Set operations
 */
public class AtomicReferenceDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  AtomicReference Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test AtomicReference
        System.out.println("Test 1: AtomicReference");
        testAtomicReference();
        
        // TODO: Test AtomicStampedReference
        System.out.println("\nTest 2: AtomicStampedReference");
        testAtomicStampedReference();
        
        // TODO: Test ABA problem
        System.out.println("\nTest 3: ABA Problem Demo");
        testABAProblem();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test AtomicReference
     */
    private static void testAtomicReference() {
        // TODO: Tạo AtomicReference với String
        AtomicReference<String> ref = null; // TODO: Create
        
        // TODO: Test get()
        // TODO: Test set()
        // TODO: Test compareAndSet()
        // TODO: Test getAndSet()
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test AtomicStampedReference
     */
    private static void testAtomicStampedReference() {
        // TODO: Tạo AtomicStampedReference với initial value và stamp
        AtomicStampedReference<String> ref = null; // TODO: Create
        
        // TODO: Test getReference() và getStamp()
        // TODO: Test compareAndSet() với expected reference và stamp
        // TODO: Test get() để lấy cả reference và stamp
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test ABA problem
     */
    private static void testABAProblem() {
        // TODO: Demonstrate ABA problem với AtomicReference
        AtomicReference<Integer> ref = null; // TODO: Create với value 10
        
        // TODO: Thread 1: Read value, sleep, try to update
        // TODO: Thread 2: Change 10 -> 20 -> 10 (ABA)
        // TODO: Thread 1 CAS succeeds (nhưng không phát hiện được change)
        
        // TODO: Fix với AtomicStampedReference
        AtomicStampedReference<Integer> stampedRef = null; // TODO: Create
        // TODO: Demonstrate stamp prevents ABA problem
    }
}

