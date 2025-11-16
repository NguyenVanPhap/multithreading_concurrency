package exercises;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 1: CompletableFuture Basics
 * 
 * TODO Tasks:
 * 1. Creating CompletableFuture
 * 2. Getting results
 * 3. Async operations
 */
public class CompletableFutureDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  CompletableFuture Basics Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test supplyAsync
        System.out.println("Test 1: supplyAsync");
        testSupplyAsync();
        
        // TODO: Test runAsync
        System.out.println("\nTest 2: runAsync");
        testRunAsync();
        
        // TODO: Test get và join
        System.out.println("\nTest 3: get() vs join()");
        testGetAndJoin();
        
        // TODO: Test với custom executor
        System.out.println("\nTest 4: Custom Executor");
        testCustomExecutor();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test supplyAsync
     */
    private static void testSupplyAsync() {
        // TODO: Tạo CompletableFuture với supplyAsync
        CompletableFuture<String> future = null; // TODO: Use CompletableFuture.supplyAsync()
        // TODO: Supply function return "Hello World"
        
        // TODO: Get result
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test runAsync
     */
    private static void testRunAsync() {
        // TODO: Tạo CompletableFuture với runAsync
        CompletableFuture<Void> future = null; // TODO: Use CompletableFuture.runAsync()
        // TODO: Run task in ra message
        
        // TODO: Wait for completion
        // TODO: In ra "Task completed"
    }
    
    /**
     * TODO: Test get() vs join()
     */
    private static void testGetAndJoin() {
        CompletableFuture<String> future = null; // TODO: Create với supplyAsync
        
        // TODO: Test get() - throws checked exception
        // TODO: Test join() - throws unchecked exception
        // TODO: So sánh
    }
    
    /**
     * TODO: Test với custom executor
     */
    private static void testCustomExecutor() {
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Tạo CompletableFuture với custom executor
        CompletableFuture<String> future = null; // TODO: Use supplyAsync với executor
        
        // TODO: Get result
        // TODO: Shutdown executor
    }
}

