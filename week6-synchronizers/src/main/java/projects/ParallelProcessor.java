package projects;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Project 1: Parallel Processing với Synchronizers
 * 
 * Xử lý song song với các synchronizers
 * - Phân chia công việc
 * - Đồng bộ hóa kết quả
 * - Collect results
 * 
 * TODO Tasks:
 * 1. Sử dụng CountDownLatch để đợi workers
 * 2. Sử dụng CyclicBarrier để đồng bộ phases
 * 3. Collect và merge results
 */
public class ParallelProcessor {
    
    private static final int NUM_WORKERS = 5;
    private static final int NUM_PHASES = 3;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Parallel Processor Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo processor
        Processor processor = null; // TODO: Create
        
        // TODO: Process data
        // TODO: Collect results
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Implement Processor class
     */
    static class Processor {
        private final ExecutorService executor;
        private final CountDownLatch completionLatch;
        private final CyclicBarrier phaseBarrier;
        private final List<Result> results;
        
        public Processor(int numWorkers, int numPhases) {
            this.executor = null; // TODO: Create ExecutorService
            this.completionLatch = null; // TODO: Create CountDownLatch
            this.phaseBarrier = null; // TODO: Create CyclicBarrier với barrier action
            this.results = null; // TODO: Create thread-safe list
        }
        
        /**
         * TODO: Process data in parallel
         */
        public List<Result> process(List<Data> dataList) throws InterruptedException {
            // TODO: Divide data among workers
            // TODO: Submit tasks
            // TODO: Wait for completion
            // TODO: Return results
            return null; // TODO: Return results
        }
        
        public void shutdown() {
            // TODO: Shutdown executor
        }
    }
    
    static class Data {
        private final int id;
        private final String content;
        
        public Data(int id, String content) {
            this.id = id;
            this.content = content;
        }
        
        public int getId() { return id; }
        public String getContent() { return content; }
    }
    
    static class Result {
        private final int dataId;
        private final String processedContent;
        
        public Result(int dataId, String processedContent) {
            this.dataId = dataId;
            this.processedContent = processedContent;
        }
        
        public int getDataId() { return dataId; }
        public String getProcessedContent() { return processedContent; }
    }
}

