package projects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project 1: Async API Client
 * 
 * Simulate async API calls
 * - Multiple API calls
 * - Combine results
 * - Error handling
 * 
 * TODO Tasks:
 * 1. Implement API call simulation
 * 2. Multiple concurrent calls
 * 3. Combine results
 * 4. Error handling và retry
 */
public class AsyncAPIClient {
    
    private static final int NUM_APIS = 5;
    private static final ExecutorService executor = null; // TODO: Create ExecutorService
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Async API Client Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo API client
        APIClient client = null; // TODO: Create
        
        // TODO: Make multiple API calls
        // TODO: Combine results
        // TODO: Handle errors
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Implement APIClient class
     */
    static class APIClient {
        private final ExecutorService executor;
        
        public APIClient(ExecutorService executor) {
            this.executor = executor;
        }
        
        /**
         * TODO: Simulate API call
         */
        public CompletableFuture<APIResponse> callAPI(String endpoint) {
            // TODO: Return CompletableFuture với supplyAsync
            // TODO: Simulate network delay (random 100-500ms)
            // TODO: Simulate occasional failures
            // TODO: Return APIResponse
            return null; // TODO: Return CompletableFuture
        }
        
        /**
         * TODO: Call multiple APIs và combine results
         */
        public CompletableFuture<List<APIResponse>> callMultipleAPIs(List<String> endpoints) {
            // TODO: Create list of futures
            // TODO: Call each API
            // TODO: Use allOf để wait for all
            // TODO: Collect results
            return null; // TODO: Return combined results
        }
        
        /**
         * TODO: Call API với retry
         */
        public CompletableFuture<APIResponse> callAPIWithRetry(String endpoint, int maxRetries) {
            // TODO: Implement retry logic
            // TODO: Retry on failure
            return null; // TODO: Return result
        }
    }
    
    static class APIResponse {
        private final String endpoint;
        private final String data;
        private final boolean success;
        
        public APIResponse(String endpoint, String data, boolean success) {
            this.endpoint = endpoint;
            this.data = data;
            this.success = success;
        }
        
        public String getEndpoint() { return endpoint; }
        public String getData() { return data; }
        public boolean isSuccess() { return success; }
    }
}

