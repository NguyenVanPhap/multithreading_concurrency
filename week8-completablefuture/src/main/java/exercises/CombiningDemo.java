package exercises;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Exercise 3: Combining Futures
 * 
 * TODO Tasks:
 * 1. allOf
 * 2. anyOf
 * 3. Exception handling
 */
public class CombiningDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Combining Futures Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test allOf
        System.out.println("Test 1: allOf");
        testAllOf();
        
        // TODO: Test anyOf
        System.out.println("\nTest 2: anyOf");
        testAnyOf();
        
        // TODO: Test exception handling
        System.out.println("\nTest 3: Exception Handling");
        testExceptionHandling();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test allOf
     */
    private static void testAllOf() {
        // TODO: Tạo multiple futures
        List<CompletableFuture<String>> futures = null; // TODO: Create list of futures
        
        // TODO: Use CompletableFuture.allOf()
        CompletableFuture<Void> allOf = null; // TODO: Combine all futures
        
        // TODO: Wait for all to complete
        // TODO: Get results từ từng future
        // TODO: In ra tất cả results
    }
    
    /**
     * TODO: Test anyOf
     */
    private static void testAnyOf() {
        // TODO: Tạo multiple futures với different delays
        List<CompletableFuture<String>> futures = null; // TODO: Create
        
        // TODO: Use CompletableFuture.anyOf()
        CompletableFuture<Object> anyOf = null; // TODO: Get first completed
        
        // TODO: Get result
        // TODO: In ra first completed result
    }
    
    /**
     * TODO: Test exception handling
     */
    private static void testExceptionHandling() {
        CompletableFuture<String> future = null; // TODO: Create future throws exception
        
        // TODO: Test handle() - handles cả success và exception
        // TODO: Test exceptionally() - chỉ handle exception
        // TODO: Test whenComplete() - similar to handle nhưng không transform
        // TODO: In ra kết quả
    }
}

