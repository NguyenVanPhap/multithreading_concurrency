package exercises;

import java.util.concurrent.*;
import java.util.*;

/**
 * Exercise: ConcurrentHashMap + ExecutorService Demo
 * 
 * TODO Tasks:
 * 1. Tạo ConcurrentHashMap
 * 2. Sử dụng ExecutorService cho parallel operations
 * 3. Atomic operations (compute, merge)
 */
public class ConcurrentMapExecutorDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ConcurrentHashMap + ExecutorService Demo");
        System.out.println("==========================================\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        try {
            // Demo 1: Parallel puts
            demoParallelPuts(map, executor);
            
            // Demo 2: Atomic operations
            demoAtomicOperations(map, executor);
            
            // Demo 3: Parallel processing
            demoParallelProcessing(map, executor);
            
        } finally {
            executor.shutdown();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void demoParallelPuts(ConcurrentHashMap<String, Integer> map, 
                                         ExecutorService executor) {
        System.out.println("Demo 1: Parallel Puts");
        System.out.println("----------------------");
        
        // TODO: Put nhiều entries song song
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            final int key = i;
            futures.add(CompletableFuture.runAsync(() -> {
                map.put("key" + key, key * 10);
            }, executor));
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        System.out.println("Map size: " + map.size());
        System.out.println("Sample entries: " + map.entrySet().stream()
            .limit(5)
            .map(e -> e.getKey() + "=" + e.getValue())
            .reduce((a, b) -> a + ", " + b)
            .orElse("") + "\n");
    }
    
    private static void demoAtomicOperations(ConcurrentHashMap<String, Integer> map, 
                                            ExecutorService executor) {
        System.out.println("Demo 2: Atomic Operations");
        System.out.println("-------------------------");
        
        // TODO: Sử dụng compute
        map.compute("key5", (k, v) -> v == null ? 100 : v + 50);
        System.out.println("After compute: key5 = " + map.get("key5"));
        
        // TODO: Sử dụng merge
        map.merge("key6", 200, (old, newVal) -> old + newVal);
        System.out.println("After merge: key6 = " + map.get("key6") + "\n");
    }
    
    private static void demoParallelProcessing(ConcurrentHashMap<String, Integer> map, 
                                              ExecutorService executor) {
        System.out.println("Demo 3: Parallel Processing");
        System.out.println("----------------------------");
        
        // TODO: Process entries song song
        List<CompletableFuture<Integer>> futures = map.entrySet().stream()
            .map(entry -> CompletableFuture.supplyAsync(() -> {
                return entry.getValue() * 2;
            }, executor))
            .collect(java.util.stream.Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        all.join();
        
        int sum = futures.stream()
            .mapToInt(CompletableFuture::join)
            .sum();
        
        System.out.println("Sum of processed values: " + sum + "\n");
    }
}

