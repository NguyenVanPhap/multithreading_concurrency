package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.Random;

/**
 * Advanced Project: Concurrent Data Aggregator
 * 
 * Hệ thống tổng hợp dữ liệu từ nhiều nguồn với:
 * - ConcurrentHashMap cho data storage
 * - ExecutorService cho parallel collection
 * - Atomic operations (compute, merge)
 * - Real-time aggregation
 * 
 * TODO Tasks:
 * 1. Implement data collection với parallel threads
 * 2. Concurrent aggregation vào ConcurrentHashMap
 * 3. Atomic operations (compute, merge)
 * 4. Real-time statistics
 * 5. Data consistency guarantees
 */
public class ConcurrentDataAggregator {
    
    // Data storage
    private final ConcurrentHashMap<String, AggregatedValue> dataMap;
    
    // Executors
    private final ExecutorService collectionExecutor;
    private final ExecutorService aggregationExecutor;
    
    // Statistics
    private final AtomicInteger totalRecords = new AtomicInteger(0);
    private final AtomicInteger processedRecords = new AtomicInteger(0);
    
    public ConcurrentDataAggregator(int parallelism) {
        this.dataMap = new ConcurrentHashMap<>();
        this.collectionExecutor = Executors.newFixedThreadPool(parallelism);
        this.aggregationExecutor = Executors.newFixedThreadPool(parallelism);
    }
    
    /**
     * Collect data từ nhiều sources song song
     */
    public CompletableFuture<Void> collectFromSources(List<DataSource> sources) {
        System.out.println("Collecting data from " + sources.size() + " sources...");
        
        // TODO: Collect từ mỗi source song song
        List<CompletableFuture<List<DataRecord>>> futures = sources.stream()
            .map(source -> CompletableFuture.supplyAsync(() -> {
                return collectFromSource(source);
            }, collectionExecutor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        // TODO: Aggregate tất cả records
        return all.thenCompose(v -> {
            List<DataRecord> allRecords = futures.stream()
                .flatMap(f -> f.join().stream())
                .collect(Collectors.toList());
            
            return aggregateRecords(allRecords);
        });
    }
    
    /**
     * Collect data từ một source
     */
    private List<DataRecord> collectFromSource(DataSource source) {
        // TODO: Simulate data collection
        List<DataRecord> records = new ArrayList<>();
        
        try {
            Thread.sleep(new Random().nextInt(500) + 200);
            
            for (int i = 0; i < 10; i++) {
                records.add(new DataRecord(
                    source.getName() + "_key" + i,
                    new Random().nextInt(100)
                ));
            }
            
            System.out.println("  Collected " + records.size() + 
                             " records from " + source.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return records;
    }
    
    /**
     * Aggregate records vào map
     */
    private CompletableFuture<Void> aggregateRecords(List<DataRecord> records) {
        System.out.println("\nAggregating " + records.size() + " records...");
        
        totalRecords.addAndGet(records.size());
        
        // TODO: Aggregate song song với atomic operations
        List<CompletableFuture<Void>> futures = records.stream()
            .map(record -> CompletableFuture.runAsync(() -> {
                // TODO: Sử dụng compute hoặc merge để aggregate
                dataMap.compute(record.getKey(), (key, existing) -> {
                    if (existing == null) {
                        processedRecords.incrementAndGet();
                        return new AggregatedValue(record.getValue(), 1);
                    } else {
                        return new AggregatedValue(
                            existing.getSum() + record.getValue(),
                            existing.getCount() + 1
                        );
                    }
                });
            }, aggregationExecutor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return all.thenRun(() -> {
            System.out.println("Aggregation completed!");
        });
    }
    
    /**
     * Merge data từ external source
     */
    public void mergeData(String key, int value) {
        // TODO: Sử dụng merge operation
        dataMap.merge(key, new AggregatedValue(value, 1), (existing, newVal) -> {
            return new AggregatedValue(
                existing.getSum() + newVal.getSum(),
                existing.getCount() + newVal.getCount()
            );
        });
    }
    
    /**
     * Get aggregated value
     */
    public AggregatedValue get(String key) {
        return dataMap.get(key);
    }
    
    /**
     * Get all aggregated data
     */
    public Map<String, AggregatedValue> getAllData() {
        return new HashMap<>(dataMap);
    }
    
    /**
     * Get statistics
     */
    public AggregationStatistics getStatistics() {
        int uniqueKeys = dataMap.size();
        int totalCount = dataMap.values().stream()
            .mapToInt(AggregatedValue::getCount)
            .sum();
        long totalSum = dataMap.values().stream()
            .mapToLong(AggregatedValue::getSum)
            .sum();
        
        return new AggregationStatistics(
            uniqueKeys,
            totalCount,
            totalSum,
            totalRecords.get(),
            processedRecords.get()
        );
    }
    
    public void shutdown() {
        collectionExecutor.shutdown();
        aggregationExecutor.shutdown();
        
        try {
            if (!collectionExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                collectionExecutor.shutdownNow();
            }
            if (!aggregationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                aggregationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Data classes
    static class DataSource {
        private final String name;
        
        public DataSource(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    static class DataRecord {
        private final String key;
        private final int value;
        
        public DataRecord(String key, int value) {
            this.key = key;
            this.value = value;
        }
        
        public String getKey() { return key; }
        public int getValue() { return value; }
    }
    
    static class AggregatedValue {
        private final long sum;
        private final int count;
        
        public AggregatedValue(long sum, int count) {
            this.sum = sum;
            this.count = count;
        }
        
        public long getSum() { return sum; }
        public int getCount() { return count; }
        public double getAverage() { 
            return count > 0 ? (double) sum / count : 0.0; 
        }
        
        @Override
        public String toString() {
            return String.format("sum=%d, count=%d, avg=%.2f", 
                               sum, count, getAverage());
        }
    }
    
    static class AggregationStatistics {
        private final int uniqueKeys;
        private final int totalCount;
        private final long totalSum;
        private final int totalRecords;
        private final int processedRecords;
        
        public AggregationStatistics(int uniqueKeys, int totalCount, 
                                    long totalSum, int totalRecords, 
                                    int processedRecords) {
            this.uniqueKeys = uniqueKeys;
            this.totalCount = totalCount;
            this.totalSum = totalSum;
            this.totalRecords = totalRecords;
            this.processedRecords = processedRecords;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Stats: uniqueKeys=%d, totalCount=%d, totalSum=%d, " +
                "totalRecords=%d, processedRecords=%d",
                uniqueKeys, totalCount, totalSum, totalRecords, processedRecords
            );
        }
    }
    
    // Main method
    public static void main(String[] args) {
        ConcurrentDataAggregator aggregator = new ConcurrentDataAggregator(10);
        
        System.out.println("==========================================");
        System.out.println("  Concurrent Data Aggregator Demo");
        System.out.println("==========================================\n");
        
        // Tạo data sources
        List<DataSource> sources = Arrays.asList(
            new DataSource("Database1"),
            new DataSource("Database2"),
            new DataSource("API1"),
            new DataSource("API2"),
            new DataSource("FileSystem1")
        );
        
        // Collect và aggregate
        aggregator.collectFromSources(sources).join();
        
        // Print results
        System.out.println("\n=== Aggregated Data ===");
        aggregator.getAllData().entrySet().stream()
            .limit(10)
            .forEach(entry -> {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            });
        
        System.out.println("\n" + aggregator.getStatistics());
        
        aggregator.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

