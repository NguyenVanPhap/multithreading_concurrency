package exercises;

import java.util.concurrent.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exercise: ExecutorService + CompletableFuture Demo
 * 
 * TODO Tasks:
 * 1. Tạo custom ExecutorService
 * 2. Sử dụng CompletableFuture với custom executor
 * 3. Chain operations với thenApply, thenCompose
 * 4. Combine multiple futures
 */
public class ExecutorCompletableFutureDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ExecutorService + CompletableFuture Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        try {
            // Demo 1: Basic usage
            demoBasicUsage(executor);
            
            // Demo 2: Chaining
            demoChaining(executor);
            
            // Demo 3: Combining
            demoCombining(executor);
            
        } finally {
            executor.shutdown();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void demoBasicUsage(ExecutorService executor) {
        System.out.println("Demo 1: Basic Usage");
        System.out.println("-------------------");
        
        // TODO: Tạo CompletableFuture với custom executor
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                return "Hello from async task";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Interrupted";
            }
        }, executor);
        
        String result = future.join();
        System.out.println("Result: " + result + "\n");
    }
    
    private static void demoChaining(ExecutorService executor) {
        System.out.println("Demo 2: Chaining Operations");
        System.out.println("----------------------------");
        
        // TODO: Chain operations
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> "hello", executor)
            .thenApply(s -> s.toUpperCase())
            .thenApply(s -> s + " WORLD");
        
        System.out.println("Chained result: " + future.join() + "\n");
    }
    
    private static void demoCombining(ExecutorService executor) {
        System.out.println("Demo 3: Combining Futures");
        System.out.println("-------------------------");
        
        // TODO: Combine multiple futures
        CompletableFuture<String> f1 = CompletableFuture
            .supplyAsync(() -> "Result1", executor);
        CompletableFuture<String> f2 = CompletableFuture
            .supplyAsync(() -> "Result2", executor);
        
        CompletableFuture<String> combined = f1.thenCombine(
            f2, (r1, r2) -> r1 + " + " + r2
        );
        
        System.out.println("Combined: " + combined.join() + "\n");
    }
}

