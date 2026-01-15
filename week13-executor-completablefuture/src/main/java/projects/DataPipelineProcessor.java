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
     * TODO: Process data qua nhiều stages.
     * Hướng dẫn:
     *  1. Stage 1: Fetch data (parallel) - gọi fetchDataStage(dataSources)
     *  2. Stage 2: Transform data (depends on Stage 1) - dùng thenCompose() để chain
     *  3. Stage 3: Validate data (depends on Stage 2) - dùng thenCompose() để chain
     *  4. Stage 4: Aggregate data (depends on Stage 3) - dùng thenCompose() để chain
     *  5. Stage 5: Store results (depends on Stage 4) - dùng thenCompose() để chain
     *  6. Dùng thenApply() để convert StorageResult thành PipelineResult
     *  7. Dùng exceptionally() để handle errors và return PipelineResult với error message
     */
    public CompletableFuture<PipelineResult> processPipeline(
            List<String> dataSources) {
        throw new UnsupportedOperationException("TODO: implement processPipeline");
    }
    
    /**
     * TODO: Stage 1 - Fetch data from multiple sources in parallel.
     * Hướng dẫn:
     *  1. Tạo CompletableFuture cho mỗi data source bằng CompletableFuture.supplyAsync()
     *  2. Trong supplyAsync: giả lập fetch (Thread.sleep random 200-700ms), return RawData
     *  3. Collect tất cả futures vào List
     *  4. Dùng CompletableFuture.allOf() để đợi tất cả hoàn thành
     *  5. Dùng thenApply() để collect kết quả từ tất cả futures thành List<RawData>
     */
    private CompletableFuture<List<RawData>> fetchDataStage(
            List<String> dataSources) {
        throw new UnsupportedOperationException("TODO: implement fetchDataStage");
    }
    
    /**
     * TODO: Stage 2 - Transform data in parallel.
     * Hướng dẫn:
     *  1. Tạo CompletableFuture cho mỗi RawData bằng CompletableFuture.supplyAsync()
     *  2. Trong supplyAsync: transform data (ví dụ: toUpperCase()), giả lập delay (100-400ms)
     *  3. Return TransformedData
     *  4. Collect tất cả futures và dùng allOf() để đợi tất cả hoàn thành
     *  5. Dùng thenApply() để collect kết quả thành List<TransformedData>
     *  (Nâng cao) Handle transformation errors bằng handle() hoặc exceptionally()
     */
    private CompletableFuture<List<TransformedData>> transformDataStage(
            List<RawData> rawDataList) {
        throw new UnsupportedOperationException("TODO: implement transformDataStage");
    }
    
    /**
     * TODO: Stage 3 - Validate data.
     * Hướng dẫn:
     *  1. Dùng CompletableFuture.supplyAsync() để validate data
     *  2. Trong supplyAsync: filter invalid records (ví dụ: length > 5)
     *  3. Map valid records thành ValidatedData
     *  4. Return List<ValidatedData>
     */
    private CompletableFuture<List<ValidatedData>> validateDataStage(
            List<TransformedData> transformedDataList) {
        throw new UnsupportedOperationException("TODO: implement validateDataStage");
    }
    
    /**
     * TODO: Stage 4 - Aggregate data.
     * Hướng dẫn:
     *  1. Dùng CompletableFuture.supplyAsync() để aggregate data
     *  2. Trong supplyAsync: join tất cả content thành một string (ví dụ: dùng Collectors.joining())
     *  3. Tính số lượng records
     *  4. Return AggregatedData với recordCount và aggregatedContent
     */
    private CompletableFuture<AggregatedData> aggregateDataStage(
            List<ValidatedData> validatedDataList) {
        throw new UnsupportedOperationException("TODO: implement aggregateDataStage");
    }
    
    /**
     * TODO: Stage 5 - Store results.
     * Hướng dẫn:
     *  1. Dùng CompletableFuture.supplyAsync() để store data
     *  2. Trong supplyAsync: giả lập storage (Thread.sleep 500ms)
     *  3. Nếu thành công: return StorageResult với success=true, recordsStored, empty errors
     *  4. Nếu thất bại (InterruptedException): return StorageResult với success=false, error message
     */
    private CompletableFuture<StorageResult> storeDataStage(
            AggregatedData aggregatedData) {
        throw new UnsupportedOperationException("TODO: implement storeDataStage");
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

