package exercises;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 1: CountDownLatch Demo
 * 
 * TODO Tasks:
 * 1. Đợi nhiều threads hoàn thành
 * 2. One-time synchronization
 * 3. Use cases
 */
public class CountDownLatchDemo {
    
    private static final int NUM_WORKERS = 5;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  CountDownLatch Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic CountDownLatch
        System.out.println("Test 1: Basic CountDownLatch");
        testBasicCountDownLatch();
        
        // TODO: Test với timeout
        System.out.println("\nTest 2: CountDownLatch với Timeout");
        testCountDownLatchWithTimeout();
        
        // TODO: Test parallel initialization
        System.out.println("\nTest 3: Parallel Initialization");
        testParallelInitialization();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic CountDownLatch
     */
    private static void testBasicCountDownLatch() {
        // TODO: Tạo CountDownLatch với count = NUM_WORKERS
        CountDownLatch latch = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Submit NUM_WORKERS tasks
        // TODO: Mỗi task làm việc và countDown()
        
        // TODO: Main thread await() để đợi tất cả workers
        // TODO: In ra "All workers completed"
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test với timeout
     */
    private static void testCountDownLatchWithTimeout() {
        CountDownLatch latch = null; // TODO: Create với NUM_WORKERS
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Submit tasks
        // TODO: Main thread await với timeout
        // TODO: Nếu timeout -> in ra message
        // TODO: Nếu không timeout -> in ra success
    }
    
    /**
     * TODO: Test parallel initialization
     */
    private static void testParallelInitialization() {
        CountDownLatch initializationLatch = null; // TODO: Create
        CountDownLatch completionLatch = null; // TODO: Create
        
        // TODO: Multiple threads initialize resources
        // TODO: Main thread đợi initialization hoàn thành
        // TODO: Sau đó start work
        // TODO: Đợi work completion
    }
}

