package exercises;

import java.util.concurrent.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exercise: Pipeline Processing Demo
 * 
 * TODO Tasks:
 * 1. Tạo pipeline với nhiều stages
 * 2. Mỗi stage phụ thuộc vào stage trước
 * 3. Parallel processing trong stage
 */
public class PipelineDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Pipeline Processing Demo");
        System.out.println("==========================================\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        try {
            // TODO: Tạo pipeline
            List<String> inputs = Arrays.asList("input1", "input2", "input3");
            
            CompletableFuture<List<String>> stage1 = fetchStage(inputs, executor);
            CompletableFuture<List<String>> stage2 = stage1.thenCompose(
                results -> processStage(results, executor)
            );
            CompletableFuture<String> stage3 = stage2.thenCompose(
                results -> aggregateStage(results, executor)
            );
            
            String finalResult = stage3.join();
            System.out.println("\nFinal result: " + finalResult);
            
        } finally {
            executor.shutdown();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static CompletableFuture<List<String>> fetchStage(
            List<String> inputs, ExecutorService executor) {
        
        System.out.println("[Stage 1] Fetching data...");
        
        // TODO: Fetch data song song
        List<CompletableFuture<String>> futures = inputs.stream()
            .map(input -> CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(200);
                    return "Fetched: " + input;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Error";
                }
            }, executor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return all.thenApply(v -> 
            futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }
    
    private static CompletableFuture<List<String>> processStage(
            List<String> inputs, ExecutorService executor) {
        
        System.out.println("[Stage 2] Processing data...");
        
        // TODO: Process data song song
        List<CompletableFuture<String>> futures = inputs.stream()
            .map(input -> CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(200);
                    return "Processed: " + input;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Error";
                }
            }, executor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return all.thenApply(v -> 
            futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }
    
    private static CompletableFuture<String> aggregateStage(
            List<String> inputs, ExecutorService executor) {
        
        System.out.println("[Stage 3] Aggregating data...");
        
        // TODO: Aggregate data
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
                return String.join(" | ", inputs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }, executor);
    }
}

