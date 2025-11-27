package exercises;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 1: Executor Demo
 * 
 * TODO Tasks:
 * 1. Sử dụng Executor interface
 * 2. So sánh Executor vs Thread.start()
 * 3. Implement ExecutorService
 * 4. Submit tasks và get results với Future
 * 5. Handle exceptions
 */
public class ExecutorDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Executor Framework Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic Executor
        System.out.println("Test 1: Basic Executor");
        testBasicExecutor();
        
        // TODO: Test ExecutorService với Runnable
        System.out.println("\nTest 2: ExecutorService với Runnable");
        testExecutorServiceRunnable();
        
        // TODO: Test ExecutorService với Callable và Future
        System.out.println("\nTest 3: ExecutorService với Callable và Future");
        testExecutorServiceCallable();
        
        // TODO: Test invokeAll
        System.out.println("\nTest 4: invokeAll");
        testInvokeAll();
        
        // TODO: Test invokeAny
        System.out.println("\nTest 5: invokeAny");
        testInvokeAny();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic Executor interface
     * So sánh với Thread.start()
     */
    private static void testBasicExecutor() {
        // TODO: Tạo Executor với FixedThreadPool (3 threads)
        Executor executor = null; // TODO: Use Executors.newFixedThreadPool()
        
        System.out.println("Using Executor:");
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // TODO: Execute task với executor.execute()
            // TODO: Task nên in ra taskId và thread name
            // TODO: Thêm sleep để simulate work
        }
        
        // TODO: Shutdown executor (cast to ExecutorService)
        // TODO: Wait for termination với timeout
        // TODO: Nếu không terminate, force shutdown
    }
    
    /**
     * TODO: Test ExecutorService với Runnable tasks
     */
    private static void testExecutorServiceRunnable() {
        // TODO: Tạo ExecutorService với FixedThreadPool (3 threads)
        ExecutorService executor = null; // TODO: Use Executors.newFixedThreadPool()
        
        List<Future<?>> futures = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // TODO: Submit Runnable task và lưu Future vào list
            // TODO: Task nên in ra "Task X running" và "Task X completed"
            // TODO: Thêm sleep để simulate work
        }
        
        // TODO: Wait for all tasks to complete
        // TODO: Dùng future.get() để wait
        // TODO: Handle InterruptedException và ExecutionException
        
        System.out.println("All tasks completed");
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test ExecutorService với Callable và Future
     * Callable có thể return value và throw exception
     */
    private static void testExecutorServiceCallable() {
        // TODO: Tạo ExecutorService
        ExecutorService executor = null; // TODO: Use Executors.newFixedThreadPool()
        
        List<Future<String>> futures = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // TODO: Submit Callable task (return String)
            // TODO: Task nên sleep một chút và return "Result from task X"
            // TODO: Lưu Future vào list
        }
        
        // TODO: Get results từ Future
        // TODO: Dùng future.get(timeout) với timeout 1 second
        // TODO: Handle InterruptedException, ExecutionException, TimeoutException
        // TODO: In ra kết quả
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test invokeAll - submit multiple Callables và wait for all
     */
    private static void testInvokeAll() {
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        List<Callable<String>> tasks = new ArrayList<>();
        
        // TODO: Tạo list of Callable tasks (5 tasks)
        // TODO: Mỗi task sleep 200ms và return "Task X result"
        
        try {
            // TODO: Invoke all tasks với executor.invokeAll()
            List<Future<String>> futures = null; // TODO: Invoke all
            
            // TODO: Process results
            // TODO: Dùng future.get() để lấy kết quả
            // TODO: Handle ExecutionException
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // TODO: Shutdown executor
    }
    
    /**
     * TODO: Test invokeAny - submit multiple Callables và get first result
     */
    private static void testInvokeAny() {
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        List<Callable<String>> tasks = new ArrayList<>();
        
        // TODO: Tạo tasks với different durations
        // TODO: Task 0: delay 100ms, Task 1: delay 200ms, ..., Task 4: delay 500ms
        // TODO: Mỗi task return "Fastest task: X"
        
        try {
            // TODO: Invoke any - get first completed result
            String result = null; // TODO: Use executor.invokeAny()
            System.out.println("First completed: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("All tasks failed: " + e.getCause());
        }
        
        // TODO: Shutdown executor
    }
}

