package exercises;

import java.util.concurrent.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Exercise: Map Aggregation Demo
 * 
 * TODO Tasks:
 * 1. Aggregate data vào ConcurrentHashMap
 * 2. Parallel aggregation
 * 3. Atomic merge operations
 */
public class MapAggregationDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Map Aggregation Demo");
        System.out.println("==========================================\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ConcurrentHashMap<String, Integer> aggregated = new ConcurrentHashMap<>();
        
        try {
            // TODO: Aggregate data từ nhiều sources
            List<String> dataSources = Arrays.asList("Source1", "Source2", "Source3");
            
            List<CompletableFuture<Void>> futures = dataSources.stream()
                .map(source -> CompletableFuture.runAsync(() -> {
                    // Simulate data collection
                    for (int i = 0; i < 5; i++) {
                        String key = "key" + i;
                        int value = new Random().nextInt(100);
                        
                        // Aggregate using merge
                        aggregated.merge(key, value, Integer::sum);
                    }
                    System.out.println("Aggregated from " + source);
                }, executor))
                .collect(Collectors.toList());
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            System.out.println("\nAggregated results:");
            aggregated.forEach((key, value) -> {
                System.out.println("  " + key + ": " + value);
            });
            
        } finally {
            executor.shutdown();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

