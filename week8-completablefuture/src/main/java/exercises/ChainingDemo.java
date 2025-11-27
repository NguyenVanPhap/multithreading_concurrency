package exercises;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Exercise 2: Chaining Operations
 * 
 * TODO Tasks:
 * 1. thenApply, thenAccept, thenRun
 * 2. thenCompose (flatMap)
 * 3. thenCombine
 */
public class ChainingDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  CompletableFuture Chaining Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test thenApply
        System.out.println("Test 1: thenApply");
        testThenApply();
        
        // TODO: Test thenAccept và thenRun
        System.out.println("\nTest 2: thenAccept và thenRun");
        testThenAcceptAndRun();
        
        // TODO: Test thenCompose
        System.out.println("\nTest 3: thenCompose");
        testThenCompose();
        
        // TODO: Test thenCombine
        System.out.println("\nTest 4: thenCombine");
        testThenCombine();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test thenApply (map)
     */
    private static void testThenApply() {
        CompletableFuture<String> future = null; // TODO: Create với supplyAsync
        
        // TODO: Chain thenApply để transform result
        // TODO: "hello" -> "HELLO" -> "HELLO WORLD"
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test thenAccept và thenRun
     */
    private static void testThenAcceptAndRun() {
        CompletableFuture<String> future = null; // TODO: Create
        
        // TODO: thenAccept - consume result
        // TODO: thenRun - run action không cần result
        // TODO: Chain operations
    }
    
    /**
     * TODO: Test thenCompose (flatMap)
     */
    private static void testThenCompose() {
        // TODO: Tạo future1 return "Hello"
        CompletableFuture<String> future1 = null; // TODO: Create
        
        // TODO: thenCompose để tạo future2 từ result của future1
        // TODO: future2 return "Hello World"
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test thenCombine
     */
    private static void testThenCombine() {
        // TODO: Tạo 2 futures
        CompletableFuture<String> future1 = null; // TODO: Return "Hello"
        CompletableFuture<String> future2 = null; // TODO: Return "World"
        
        // TODO: Combine 2 futures
        // TODO: Combine result thành "Hello World"
        // TODO: In ra kết quả
    }
}

