package exercises;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.BrokenBarrierException;

/**
 * Exercise 2: CyclicBarrier Demo
 * 
 * TODO Tasks:
 * 1. Đồng bộ threads tại barrier
 * 2. Reusable barrier
 * 3. Barrier action
 */
public class CyclicBarrierDemo {
    
    private static final int NUM_THREADS = 5;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  CyclicBarrier Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic CyclicBarrier
        System.out.println("Test 1: Basic CyclicBarrier");
        testBasicCyclicBarrier();
        
        // TODO: Test với barrier action
        System.out.println("\nTest 2: CyclicBarrier với Action");
        testCyclicBarrierWithAction();
        
        // TODO: Test reusable barrier
        System.out.println("\nTest 3: Reusable Barrier");
        testReusableBarrier();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic CyclicBarrier
     */
    private static void testBasicCyclicBarrier() {
        // TODO: Tạo CyclicBarrier với NUM_THREADS parties
        CyclicBarrier barrier = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Submit NUM_THREADS tasks
        // TODO: Mỗi task làm việc, sau đó await() tại barrier
        // TODO: Quan sát tất cả threads đợi nhau tại barrier
        // TODO: Khi đủ parties, tất cả tiếp tục
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test với barrier action
     */
    private static void testCyclicBarrierWithAction() {
        // TODO: Tạo CyclicBarrier với barrier action
        // TODO: Barrier action chạy khi tất cả threads arrive
        CyclicBarrier barrier = null; // TODO: Create với Runnable action
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Submit tasks
        // TODO: Quan sát barrier action được execute
    }
    
    /**
     * TODO: Test reusable barrier
     */
    private static void testReusableBarrier() {
        CyclicBarrier barrier = null; // TODO: Create
        
        // TODO: Multiple rounds
        // TODO: Barrier có thể reuse sau mỗi round
        // TODO: Quan sát barrier reset tự động
    }
}

