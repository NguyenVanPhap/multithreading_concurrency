package exercises;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 3: Semaphore Demo
 * 
 * TODO Tasks:
 * 1. Giới hạn số threads truy cập
 * 2. Fair vs unfair semaphore
 * 3. Acquire và release
 */
public class SemaphoreDemo {
    
    private static final int PERMITS = 3;
    private static final int NUM_THREADS = 10;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Semaphore Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic Semaphore
        System.out.println("Test 1: Basic Semaphore");
        testBasicSemaphore();
        
        // TODO: Test fair vs unfair
        System.out.println("\nTest 2: Fair vs Unfair Semaphore");
        testFairSemaphore();
        
        // TODO: Test tryAcquire với timeout
        System.out.println("\nTest 3: TryAcquire với Timeout");
        testTryAcquire();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic Semaphore
     */
    private static void testBasicSemaphore() {
        // TODO: Tạo Semaphore với PERMITS permits
        Semaphore semaphore = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Submit NUM_THREADS tasks
        // TODO: Mỗi task acquire() -> use resource -> release()
        // TODO: Quan sát chỉ PERMITS threads có thể access cùng lúc
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test fair vs unfair semaphore
     */
    private static void testFairSemaphore() {
        // TODO: Test unfair semaphore
        Semaphore unfair = null; // TODO: Create unfair
        
        // TODO: Test fair semaphore
        Semaphore fair = null; // TODO: Create fair
        
        // TODO: So sánh behavior
    }
    
    /**
     * TODO: Test tryAcquire với timeout
     */
    private static void testTryAcquire() {
        Semaphore semaphore = null; // TODO: Create với 1 permit
        
        // TODO: Thread 1 acquire và hold lâu
        // TODO: Thread 2 tryAcquire với timeout
        // TODO: Quan sát timeout behavior
    }
}

