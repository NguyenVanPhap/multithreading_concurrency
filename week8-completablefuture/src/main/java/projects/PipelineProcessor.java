package projects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.function.Function;

/**
 * Project 2: Pipeline Processor
 * 
 * Data processing pipeline
 * - Sequential stages
 * - Parallel processing
 * - Result aggregation
 * 
 * TODO Tasks:
 * 1. Implement Pipeline với stages
 * 2. Sequential processing
 * 3. Parallel processing
 * 4. Result aggregation
 */
public class PipelineProcessor {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Pipeline Processor Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo pipeline
        Pipeline<String, String> pipeline = null; // TODO: Create
        
        // TODO: Process data
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Implement Pipeline interface
     */
    interface Pipeline<T, R> {
        CompletableFuture<R> process(T input);
        Pipeline<T, R> addStage(Function<T, R> stage);
    }
    
    /**
     * TODO: Implement PipelineImpl class
     */
    static class PipelineImpl<T, R> implements Pipeline<T, R> {
        private final List<Function<?, ?>> stages;
        private final ExecutorService executor;
        
        public PipelineImpl(ExecutorService executor) {
            this.stages = null; // TODO: Create list
            this.executor = executor;
        }
        
        @Override
        public CompletableFuture<R> process(T input) {
            // TODO: Process qua các stages
            // TODO: Chain CompletableFutures
            // TODO: Return final result
            return null; // TODO: Return CompletableFuture
        }
        
        @Override
        public Pipeline<T, R> addStage(Function<T, R> stage) {
            // TODO: Add stage to pipeline
            return this;
        }
    }
}

