package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Random;

/**
 * Advanced Project: Data Pipeline Processor
 * 
 * Multi-stage data processing pipeline với:
 * - Stage 1: Data fetching (parallel)
 * - Stage 2: Data transformation (parallel với dependencies)
 * - Stage 3: Data validation
 * - Stage 4: Data aggregation
 * - Stage 5: Result storage
 * 
 * TODO Tasks:
 * 1. Implement multi-stage pipeline với CompletableFuture
 * 2. Parallel processing trong mỗi stage
 * 3. Dependency management giữa stages
 * 4. Error recovery và partial results
 * 5. Progress tracking
 * 6. Resource cleanup
 */
public class DataPipelineProcessor {
    
    private final ExecutorService executor;
    private final int parallelism;
    
    public DataPipelineProcessor(int parallelism) {
        this.parallelism = parallelism;
        this.executor = Executors.newFixedThreadPool(parallelism);
    }
    
    /**
     * Process data qua nhiều stages
     */
    public CompletableFuture<PipelineResult> processPipeline(
            List<String> dataSources) {
        
        System.out.println("Starting pipeline with " + dataSources.size() + 
                          " data sources...\n");
        
        // Stage 1: Fetch data (parallel)
        CompletableFuture<List<RawData>> fetchStage = 
            fetchDataStage(dataSources);
        
        // Stage 2: Transform data (parallel, depends on Stage 1)
        CompletableFuture<List<TransformedData>> transformStage = 
            fetchStage.thenCompose(this::transformDataStage);
        
        // Stage 3: Validate data (depends on Stage 2)
        CompletableFuture<List<ValidatedData>> validateStage = 
            transformStage.thenCompose(this::validateDataStage);
        
        // Stage 4: Aggregate data (depends on Stage 3)
        CompletableFuture<AggregatedData> aggregateStage = 
            validateStage.thenCompose(this::aggregateDataStage);
        
        // Stage 5: Store results (depends on Stage 4)
        CompletableFuture<StorageResult> storeStage = 
            aggregateStage.thenCompose(this::storeDataStage);
        
        // TODO: Combine all stages và track progress
        // TODO: Handle errors ở mỗi stage
        // TODO: Return final result với statistics
        
        return storeStage.thenApply(result -> {
            return new PipelineResult(
                result.isSuccess(),
                result.getRecordsStored(),
                result.getErrors()
            );
        }).exceptionally(throwable -> {
            System.err.println("Pipeline failed: " + throwable.getMessage());
            return new PipelineResult(false, 0, 
                Collections.singletonList(throwable.getMessage()));
        });
    }
    
    /**
     * Stage 1: Fetch data from multiple sources in parallel
     */
    private CompletableFuture<List<RawData>> fetchDataStage(
            List<String> dataSources) {
        
        System.out.println("[Stage 1] Fetching data from " + 
                          dataSources.size() + " sources...");
        
        // TODO: Tạo CompletableFuture cho mỗi data source
        // TODO: Fetch song song
        // TODO: Combine results với allOf
        
        List<CompletableFuture<RawData>> futures = dataSources.stream()
            .map(source -> CompletableFuture.supplyAsync(() -> {
                System.out.println("  Fetching from: " + source);
                try {
                    Thread.sleep(new Random().nextInt(500) + 200);
                    return new RawData(source, "Data from " + source);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }, executor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> allFetched = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return allFetched.thenApply(v -> 
            futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }
    
    /**
     * Stage 2: Transform data in parallel
     */
    private CompletableFuture<List<TransformedData>> transformDataStage(
            List<RawData> rawDataList) {
        
        System.out.println("\n[Stage 2] Transforming " + 
                          rawDataList.size() + " records...");
        
        // TODO: Transform mỗi record song song
        // TODO: Handle transformation errors
        
        List<CompletableFuture<TransformedData>> futures = rawDataList.stream()
            .map(data -> CompletableFuture.supplyAsync(() -> {
                System.out.println("  Transforming: " + data.getSource());
                try {
                    Thread.sleep(new Random().nextInt(300) + 100);
                    return new TransformedData(
                        data.getSource(),
                        data.getContent().toUpperCase()
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }, executor))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> allTransformed = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return allTransformed.thenApply(v -> 
            futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }
    
    /**
     * Stage 3: Validate data
     */
    private CompletableFuture<List<ValidatedData>> validateDataStage(
            List<TransformedData> transformedDataList) {
        
        System.out.println("\n[Stage 3] Validating " + 
                          transformedDataList.size() + " records...");
        
        // TODO: Validate data
        // TODO: Filter invalid records
        
        return CompletableFuture.supplyAsync(() -> {
            return transformedDataList.stream()
                .filter(data -> data.getContent().length() > 5) // Simple validation
                .map(data -> new ValidatedData(
                    data.getSource(),
                    data.getContent()
                ))
                .collect(Collectors.toList());
        }, executor);
    }
    
    /**
     * Stage 4: Aggregate data
     */
    private CompletableFuture<AggregatedData> aggregateDataStage(
            List<ValidatedData> validatedDataList) {
        
        System.out.println("\n[Stage 4] Aggregating " + 
                          validatedDataList.size() + " records...");
        
        // TODO: Aggregate data
        // TODO: Calculate statistics
        
        return CompletableFuture.supplyAsync(() -> {
            String aggregated = validatedDataList.stream()
                .map(ValidatedData::getContent)
                .collect(Collectors.joining(", "));
            
            return new AggregatedData(
                validatedDataList.size(),
                aggregated
            );
        }, executor);
    }
    
    /**
     * Stage 5: Store results
     */
    private CompletableFuture<StorageResult> storeDataStage(
            AggregatedData aggregatedData) {
        
        System.out.println("\n[Stage 5] Storing " + 
                          aggregatedData.getRecordCount() + " records...");
        
        // TODO: Store data
        // TODO: Return storage result
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                System.out.println("  Stored successfully!");
                return new StorageResult(true, aggregatedData.getRecordCount(), 
                                       Collections.emptyList());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new StorageResult(false, 0, 
                    Collections.singletonList("Storage interrupted"));
            }
        }, executor);
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }
    
    // Data classes
    static class RawData {
        private final String source;
        private final String content;
        
        public RawData(String source, String content) {
            this.source = source;
            this.content = content;
        }
        
        public String getSource() { return source; }
        public String getContent() { return content; }
    }
    
    static class TransformedData {
        private final String source;
        private final String content;
        
        public TransformedData(String source, String content) {
            this.source = source;
            this.content = content;
        }
        
        public String getSource() { return source; }
        public String getContent() { return content; }
    }
    
    static class ValidatedData {
        private final String source;
        private final String content;
        
        public ValidatedData(String source, String content) {
            this.source = source;
            this.content = content;
        }
        
        public String getSource() { return source; }
        public String getContent() { return content; }
    }
    
    static class AggregatedData {
        private final int recordCount;
        private final String aggregatedContent;
        
        public AggregatedData(int recordCount, String aggregatedContent) {
            this.recordCount = recordCount;
            this.aggregatedContent = aggregatedContent;
        }
        
        public int getRecordCount() { return recordCount; }
        public String getAggregatedContent() { return aggregatedContent; }
    }
    
    static class StorageResult {
        private final boolean success;
        private final int recordsStored;
        private final List<String> errors;
        
        public StorageResult(boolean success, int recordsStored, 
                           List<String> errors) {
            this.success = success;
            this.recordsStored = recordsStored;
            this.errors = errors;
        }
        
        public boolean isSuccess() { return success; }
        public int getRecordsStored() { return recordsStored; }
        public List<String> getErrors() { return errors; }
    }
    
    static class PipelineResult {
        private final boolean success;
        private final int recordsProcessed;
        private final List<String> errors;
        
        public PipelineResult(boolean success, int recordsProcessed, 
                             List<String> errors) {
            this.success = success;
            this.recordsProcessed = recordsProcessed;
            this.errors = errors;
        }
        
        public boolean isSuccess() { return success; }
        public int getRecordsProcessed() { return recordsProcessed; }
        public List<String> getErrors() { return errors; }
    }
    
    // Main method
    public static void main(String[] args) {
        DataPipelineProcessor processor = new DataPipelineProcessor(10);
        
        System.out.println("==========================================");
        System.out.println("  Data Pipeline Processor Demo");
        System.out.println("==========================================\n");
        
        List<String> dataSources = Arrays.asList(
            "Database1", "Database2", "API1", "API2", "FileSystem1"
        );
        
        long startTime = System.currentTimeMillis();
        
        CompletableFuture<PipelineResult> future = 
            processor.processPipeline(dataSources);
        
        PipelineResult result = future.join();
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("\n=== Pipeline Results ===");
        System.out.println("Duration: " + duration + "ms");
        System.out.println("Success: " + result.isSuccess());
        System.out.println("Records processed: " + result.getRecordsProcessed());
        System.out.println("Errors: " + result.getErrors().size());
        
        processor.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

