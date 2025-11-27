package exercises;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: ThreadPool Demo
 * 
 * TODO Tasks:
 * 1. Tạo FixedThreadPool
 * 2. Tạo CachedThreadPool
 * 3. Tạo SingleThreadExecutor
 * 4. So sánh performance và behavior
 * 5. Custom ThreadPoolExecutor
 * 6. RejectedExecutionHandler
 */
public class ThreadPoolDemo {
    
    private static final int NUM_TASKS = 20;
    private static final int TASK_DURATION_MS = 100;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Thread Pool Types Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test FixedThreadPool
        System.out.println("Test 1: FixedThreadPool");
        testFixedThreadPool();
        
        // TODO: Test CachedThreadPool
        System.out.println("\nTest 2: CachedThreadPool");
        testCachedThreadPool();
        
        // TODO: Test SingleThreadExecutor
        System.out.println("\nTest 3: SingleThreadExecutor");
        testSingleThreadExecutor();
        
        // TODO: Test Custom ThreadPoolExecutor
        System.out.println("\nTest 4: Custom ThreadPoolExecutor");
        testCustomThreadPool();
        
        // TODO: Test RejectedExecutionHandler
        System.out.println("\nTest 5: RejectedExecutionHandler");
        testRejectedExecutionHandler();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test FixedThreadPool
     * Fixed number of threads, unbounded queue
     */
    private static void testFixedThreadPool() {
        // TODO: Tạo FixedThreadPool với 5 threads
        ExecutorService executor = null; // TODO: Use Executors.newFixedThreadPool(5)
        
        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();
        
        // TODO: Submit NUM_TASKS tasks
        // TODO: Mỗi task sleep TASK_DURATION_MS và return taskId
        // TODO: In ra taskId và thread name
        // TODO: Lưu Future vào list
        
        // TODO: Wait for all tasks với future.get()
        // TODO: Handle exceptions
        
        long endTime = System.currentTimeMillis();
        System.out.println("FixedThreadPool completed in " + (endTime - startTime) + "ms");
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test CachedThreadPool
     * Creates new threads as needed, reuses existing threads
     */
    private static void testCachedThreadPool() {
        // TODO: Tạo CachedThreadPool
        ExecutorService executor = null; // TODO: Use Executors.newCachedThreadPool()
        
        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();
        
        // TODO: Submit NUM_TASKS tasks (tương tự testFixedThreadPool)
        // TODO: Wait for all tasks
        // TODO: Measure và in ra thời gian
        
        long endTime = System.currentTimeMillis();
        System.out.println("CachedThreadPool completed in " + (endTime - startTime) + "ms");
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test SingleThreadExecutor
     * Single thread, tasks execute sequentially
     */
    private static void testSingleThreadExecutor() {
        // TODO: Tạo SingleThreadExecutor
        ExecutorService executor = null; // TODO: Use Executors.newSingleThreadExecutor()
        
        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();
        
        // TODO: Submit NUM_TASKS tasks (tương tự các test trước)
        // TODO: Quan sát tasks chạy tuần tự (1 thread)
        // TODO: Wait for all tasks
        // TODO: Measure và in ra thời gian
        
        long endTime = System.currentTimeMillis();
        System.out.println("SingleThreadExecutor completed in " + (endTime - startTime) + "ms");
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test Custom ThreadPoolExecutor
     * Custom core pool size, max pool size, queue, etc.
     */
    private static void testCustomThreadPool() {
        // TODO: Tạo custom ThreadPoolExecutor
        int corePoolSize = 2;
        int maximumPoolSize = 5;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = null; // TODO: Create LinkedBlockingQueue với capacity 10
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                // TODO: Create custom thread với name "CustomThread-X"
                // TODO: Set daemon = false
                return null; // TODO: Return thread
            }
        };
        
        ThreadPoolExecutor executor = null; // TODO: Create ThreadPoolExecutor với các params trên
        
        // TODO: In ra thông tin pool (core size, max size, queue capacity)
        
        // TODO: Submit NUM_TASKS tasks
        // TODO: Monitor pool (active count, queue size, completed count)
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test RejectedExecutionHandler
     * Handle rejected tasks when pool is full
     */
    private static void testRejectedExecutionHandler() {
        // TODO: Tạo ThreadPoolExecutor với bounded queue
        int corePoolSize = 2;
        int maximumPoolSize = 3;
        BlockingQueue<Runnable> workQueue = null; // TODO: Create ArrayBlockingQueue với capacity 5
        
        // TODO: Custom RejectedExecutionHandler
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // TODO: Handle rejected task
                // TODO: In ra thông tin (task, pool size, active count, queue size)
            }
        };
        
        ThreadPoolExecutor executor = null; // TODO: Create ThreadPoolExecutor với handler
        
        // TODO: Submit more tasks than pool can handle (NUM_TASKS * 2)
        // TODO: In ra số tasks và capacity
        // TODO: Wait một chút để quan sát
        // TODO: Shutdown executor
    }
}

