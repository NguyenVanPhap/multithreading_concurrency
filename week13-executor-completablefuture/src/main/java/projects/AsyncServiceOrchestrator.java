package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.Random;

/**
 * Advanced Project: Async Service Orchestrator
 * 
 * Hệ thống điều phối nhiều microservices bất đồng bộ với:
 * - Custom ExecutorService cho different service types
 * - CompletableFuture chains
 * - Retry mechanism với exponential backoff
 * - Circuit breaker pattern
 * - Timeout handling
 * - Error aggregation
 * 
 * TODO Tasks:
 * 1. Implement service calls với CompletableFuture
 * 2. Custom ExecutorService cho IO và CPU tasks
 * 3. Retry logic với exponential backoff
 * 4. Circuit breaker để tránh cascade failures
 * 5. Timeout cho từng service call
 * 6. Combine results từ nhiều services
 * 7. Error handling và reporting
 */
public class AsyncServiceOrchestrator {
    
    // Executors cho different service types
    private final ExecutorService ioExecutor;      // Cho I/O-bound services
    private final ExecutorService cpuExecutor;    // Cho CPU-bound processing
    private final ExecutorService scheduledExecutor; // Cho retry scheduling
    
    // Circuit breaker state
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();
    
    // Statistics
    private final AtomicInteger totalCalls = new AtomicInteger(0);
    private final AtomicInteger successCalls = new AtomicInteger(0);
    private final AtomicInteger failedCalls = new AtomicInteger(0);
    private final AtomicInteger retryCount = new AtomicInteger(0);
    
    public AsyncServiceOrchestrator() {
        // TODO: Initialize executors
        // ioExecutor: 20 threads cho I/O operations
        // cpuExecutor: số cores cho CPU operations
        // scheduledExecutor: 5 threads cho scheduled tasks
        
        this.ioExecutor = Executors.newFixedThreadPool(20);
        this.cpuExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );
        this.scheduledExecutor = Executors.newScheduledThreadPool(5);
    }
    
    /**
     * Gọi nhiều services song song và kết hợp kết quả
     */
    public CompletableFuture<OrchestrationResult> orchestrateServices(
            List<ServiceRequest> requests) {
        
        totalCalls.addAndGet(requests.size());
        
        // TODO: Tạo CompletableFuture cho mỗi service call
        // TODO: Apply timeout, retry, và circuit breaker
        // TODO: Combine tất cả results với allOf
        // TODO: Aggregate errors nếu có
        
        List<CompletableFuture<ServiceResponse>> futures = requests.stream()
            .map(request -> callServiceWithRetry(request))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return allFutures.thenApply(v -> {
            List<ServiceResponse> responses = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
            
            long successCount = responses.stream()
                .filter(r -> r.isSuccess())
                .count();
            
            List<String> errors = responses.stream()
                .filter(r -> !r.isSuccess())
                .map(r -> r.getError())
                .collect(Collectors.toList());
            
            return new OrchestrationResult(responses, successCount, errors);
        });
    }
    
    /**
     * Gọi service với retry và circuit breaker
     */
    private CompletableFuture<ServiceResponse> callServiceWithRetry(
            ServiceRequest request) {
        
        CircuitBreaker cb = circuitBreakers.computeIfAbsent(
            request.getServiceName(), 
            k -> new CircuitBreaker()
        );
        
        // TODO: Check circuit breaker state
        // TODO: Nếu OPEN, return failure ngay
        // TODO: Nếu CLOSED hoặc HALF_OPEN, thử gọi service
        
        if (cb.isOpen()) {
            return CompletableFuture.completedFuture(
                ServiceResponse.failure(request.getServiceName(), 
                    "Circuit breaker is OPEN")
            );
        }
        
        return CompletableFuture
            .supplyAsync(() -> callService(request), ioExecutor)
            .orTimeout(request.getTimeoutMs(), TimeUnit.MILLISECONDS)
            .handle((response, throwable) -> {
                if (throwable != null || !response.isSuccess()) {
                    // TODO: Retry với exponential backoff
                    // TODO: Update circuit breaker state
                    cb.recordFailure();
                    return retryWithBackoff(request, cb, 0);
                } else {
                    cb.recordSuccess();
                    successCalls.incrementAndGet();
                    return response;
                }
            });
    }
    
    /**
     * Retry với exponential backoff
     */
    private ServiceResponse retryWithBackoff(
            ServiceRequest request, 
            CircuitBreaker cb, 
            int attempt) {
        
        final int MAX_RETRIES = 3;
        final long BASE_DELAY_MS = 100;
        
        if (attempt >= MAX_RETRIES) {
            failedCalls.incrementAndGet();
            return ServiceResponse.failure(
                request.getServiceName(), 
                "Max retries exceeded"
            );
        }
        
        // TODO: Calculate exponential backoff delay
        // TODO: Schedule retry với ScheduledExecutorService
        // TODO: Recursive retry call
        
        long delayMs = BASE_DELAY_MS * (1L << attempt); // Exponential: 100, 200, 400ms
        
        try {
            Thread.sleep(delayMs);
            retryCount.incrementAndGet();
            
            ServiceResponse response = callService(request);
            if (response.isSuccess()) {
                cb.recordSuccess();
                successCalls.incrementAndGet();
                return response;
            } else {
                cb.recordFailure();
                return retryWithBackoff(request, cb, attempt + 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ServiceResponse.failure(
                request.getServiceName(), 
                "Interrupted during retry"
            );
        }
    }
    
    /**
     * Simulate service call
     */
    private ServiceResponse callService(ServiceRequest request) {
        // TODO: Simulate service call với random delay
        // TODO: Random failures để test retry và circuit breaker
        
        try {
            // Simulate network delay
            Thread.sleep(new Random().nextInt(200) + 50);
            
            // 20% chance of failure để test retry
            if (Math.random() < 0.2) {
                return ServiceResponse.failure(
                    request.getServiceName(),
                    "Service temporarily unavailable"
                );
            }
            
            return ServiceResponse.success(
                request.getServiceName(),
                "Data from " + request.getServiceName()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ServiceResponse.failure(
                request.getServiceName(),
                "Interrupted"
            );
        }
    }
    
    /**
     * Shutdown executors gracefully
     */
    public void shutdown() {
        // TODO: Shutdown all executors
        ioExecutor.shutdown();
        cpuExecutor.shutdown();
        scheduledExecutor.shutdown();
        
        try {
            if (!ioExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                ioExecutor.shutdownNow();
            }
            if (!cpuExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cpuExecutor.shutdownNow();
            }
            if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ioExecutor.shutdownNow();
            cpuExecutor.shutdownNow();
            scheduledExecutor.shutdownNow();
        }
    }
    
    public void printStatistics() {
        System.out.println("\n=== Orchestrator Statistics ===");
        System.out.println("Total calls: " + totalCalls.get());
        System.out.println("Success: " + successCalls.get());
        System.out.println("Failed: " + failedCalls.get());
        System.out.println("Retries: " + retryCount.get());
        System.out.println("Circuit breakers: " + circuitBreakers.size());
    }
    
    // Inner classes
    static class ServiceRequest {
        private final String serviceName;
        private final long timeoutMs;
        
        public ServiceRequest(String serviceName, long timeoutMs) {
            this.serviceName = serviceName;
            this.timeoutMs = timeoutMs;
        }
        
        public String getServiceName() { return serviceName; }
        public long getTimeoutMs() { return timeoutMs; }
    }
    
    static class ServiceResponse {
        private final String serviceName;
        private final boolean success;
        private final String data;
        private final String error;
        
        private ServiceResponse(String serviceName, boolean success, 
                               String data, String error) {
            this.serviceName = serviceName;
            this.success = success;
            this.data = data;
            this.error = error;
        }
        
        public static ServiceResponse success(String serviceName, String data) {
            return new ServiceResponse(serviceName, true, data, null);
        }
        
        public static ServiceResponse failure(String serviceName, String error) {
            return new ServiceResponse(serviceName, false, null, error);
        }
        
        public boolean isSuccess() { return success; }
        public String getData() { return data; }
        public String getError() { return error; }
        public String getServiceName() { return serviceName; }
    }
    
    static class OrchestrationResult {
        private final List<ServiceResponse> responses;
        private final long successCount;
        private final List<String> errors;
        
        public OrchestrationResult(List<ServiceResponse> responses, 
                                  long successCount, 
                                  List<String> errors) {
            this.responses = responses;
            this.successCount = successCount;
            this.errors = errors;
        }
        
        public List<ServiceResponse> getResponses() { return responses; }
        public long getSuccessCount() { return successCount; }
        public List<String> getErrors() { return errors; }
    }
    
    static class CircuitBreaker {
        private volatile State state = State.CLOSED;
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);
        private static final int FAILURE_THRESHOLD = 5;
        private static final int SUCCESS_THRESHOLD = 2;
        
        enum State { CLOSED, OPEN, HALF_OPEN }
        
        public void recordSuccess() {
            if (state == State.HALF_OPEN) {
                successCount.incrementAndGet();
                if (successCount.get() >= SUCCESS_THRESHOLD) {
                    state = State.CLOSED;
                    failureCount.set(0);
                    successCount.set(0);
                }
            } else if (state == State.CLOSED) {
                failureCount.set(0);
            }
        }
        
        public void recordFailure() {
            failureCount.incrementAndGet();
            if (state == State.CLOSED && failureCount.get() >= FAILURE_THRESHOLD) {
                state = State.OPEN;
                // TODO: Schedule transition to HALF_OPEN after timeout
            } else if (state == State.HALF_OPEN) {
                state = State.OPEN;
            }
        }
        
        public boolean isOpen() {
            return state == State.OPEN;
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        AsyncServiceOrchestrator orchestrator = new AsyncServiceOrchestrator();
        
        System.out.println("==========================================");
        System.out.println("  Async Service Orchestrator Demo");
        System.out.println("==========================================\n");
        
        // Tạo danh sách service requests
        List<ServiceRequest> requests = Arrays.asList(
            new ServiceRequest("UserService", 1000),
            new ServiceRequest("OrderService", 1500),
            new ServiceRequest("PaymentService", 2000),
            new ServiceRequest("InventoryService", 1000),
            new ServiceRequest("NotificationService", 800)
        );
        
        System.out.println("Calling " + requests.size() + " services in parallel...\n");
        
        long startTime = System.currentTimeMillis();
        
        CompletableFuture<OrchestrationResult> future = 
            orchestrator.orchestrateServices(requests);
        
        OrchestrationResult result = future.join();
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("\n=== Results ===");
        System.out.println("Duration: " + duration + "ms");
        System.out.println("Success: " + result.getSuccessCount() + "/" + 
                          requests.size());
        System.out.println("Errors: " + result.getErrors().size());
        
        orchestrator.printStatistics();
        orchestrator.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

