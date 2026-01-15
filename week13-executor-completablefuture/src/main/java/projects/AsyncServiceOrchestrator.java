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
    
    // Statistics tổng thể
    private final AtomicInteger totalCalls = new AtomicInteger(0);
    private final AtomicInteger successCalls = new AtomicInteger(0);
    private final AtomicInteger failedCalls = new AtomicInteger(0);
    private final AtomicInteger retryCount = new AtomicInteger(0);
    
    // Advanced: per-service config & metrics (cho bạn tự triển khai thêm)
    // TODO: dùng map này để lưu config từng service (timeout, priority, loại IO/CPU, maxRetries,...)
    private final Map<String, ServiceConfig> serviceConfigs = new ConcurrentHashMap<>();
    
    /**
     * TODO: Khởi tạo các executors.
     * Hướng dẫn:
     *  - ioExecutor: Executors.newFixedThreadPool(20) cho I/O operations
     *  - cpuExecutor: Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) cho CPU operations
     *  - scheduledExecutor: Executors.newScheduledThreadPool(5) cho scheduled tasks (retry scheduling)
     */
    public AsyncServiceOrchestrator() {
        throw new UnsupportedOperationException("TODO: implement constructor - initialize executors");
    }
    
    // =========================
    // Service registration API
    // =========================
    
    /**
     * Đăng ký service với config chi tiết.
     * Gợi ý:
     * - Lưu config vào serviceConfigs
     * - Khởi tạo sẵn CircuitBreaker cho service đó
     * - (Nâng cao) Gán executor chuyên biệt cho từng service (bulkhead pattern)
     */
    public void registerService(String serviceName, long timeoutMs, int priority, boolean cpuBound) {
        // TODO: implement registerService
        // Ví dụ:
        // - serviceConfigs.put(serviceName, new ServiceConfig(...));
        // - circuitBreakers.putIfAbsent(serviceName, new CircuitBreaker());
        // - (nâng cao) tạo Executor riêng cho service ưu tiên cao
        throw new UnsupportedOperationException("TODO: registerService");
    }
    
    /**
     * Lấy config hiện tại của 1 service.
     * TODO: xử lý trường hợp chưa đăng ký service (trả default config hoặc Optional).
     */
    public ServiceConfig getServiceConfig(String serviceName) {
        // TODO: return config phù hợp hoặc default
        throw new UnsupportedOperationException("TODO: getServiceConfig");
    }
    
    /**
     * Cho phép override timeout của 1 service lúc runtime.
     * Gợi ý use-case: khi phát hiện service chậm, bạn có thể tăng timeout tạm thời.
     */
    public void overrideTimeout(String serviceName, long newTimeoutMs) {
        // TODO: update timeout trong ServiceConfig
        // Gợi ý: serviceConfigs.computeIfPresent(...)
        throw new UnsupportedOperationException("TODO: overrideTimeout");
    }
    
    /**
     * (Nâng cao) Dump toàn bộ thông tin debug hiện tại của orchestrator:
     * - State của từng CircuitBreaker
     * - Thống kê calls/retries theo từng service
     * - Cấu hình timeout/priority hiện tại
     */
    public void dumpDebugInfo() {
        // TODO: in ra console hoặc build JSON string
        throw new UnsupportedOperationException("TODO: dumpDebugInfo");
    }
    
    /**
     * TODO: Gọi nhiều services song song và kết hợp kết quả.
     * Hướng dẫn:
     *  1. Tăng totalCalls với số lượng requests
     *  2. Tạo CompletableFuture cho mỗi service call bằng cách gọi callServiceWithRetry(request)
     *  3. Dùng CompletableFuture.allOf() để đợi tất cả futures hoàn thành
     *  4. Dùng thenApply() để aggregate kết quả:
     *     - Lấy tất cả ServiceResponse từ futures
     *     - Đếm số success và collect danh sách errors
     *     - Tạo OrchestrationResult với responses, successCount, errors
     */
    public CompletableFuture<OrchestrationResult> orchestrateServices(
            List<ServiceRequest> requests) {
        throw new UnsupportedOperationException("TODO: implement orchestrateServices");
    }
    
    /**
     * TODO: Gọi service với retry và circuit breaker.
     * Hướng dẫn:
     *  1. Lấy CircuitBreaker cho service này (tạo mới nếu chưa có)
     *  2. Kiểm tra CircuitBreaker state:
     *     - Nếu OPEN: return CompletableFuture.completedFuture với ServiceResponse.failure()
     *     - Nếu CLOSED hoặc HALF_OPEN: tiếp tục
     *  3. Tạo CompletableFuture bằng CompletableFuture.supplyAsync(() -> callService(request), ioExecutor)
     *  4. Áp dụng timeout: cf.orTimeout(request.getTimeoutMs(), TimeUnit.MILLISECONDS)
     *  5. Handle kết quả:
     *     - Nếu thành công: cb.recordSuccess(), successCalls.incrementAndGet(), return response
     *     - Nếu thất bại: cb.recordFailure(), gọi retryWithBackoff(request, cb, 0)
     */
    private CompletableFuture<ServiceResponse> callServiceWithRetry(
            ServiceRequest request) {
        throw new UnsupportedOperationException("TODO: implement callServiceWithRetry");
    }
    
    /**
     * TODO: Retry với exponential backoff.
     * Hướng dẫn:
     *  1. Kiểm tra attempt >= MAX_RETRIES (ví dụ: 3): nếu vượt quá, return ServiceResponse.failure()
     *  2. Tính toán exponential backoff delay: BASE_DELAY_MS * (2 ^ attempt) 
     *     Ví dụ: 100ms, 200ms, 400ms cho attempt 0, 1, 2
     *  3. Dùng scheduledExecutor.schedule() để delay retry
     *  4. Trong scheduled task:
     *     - retryCount.incrementAndGet()
     *     - Gọi lại callService(request)
     *     - Nếu thành công: cb.recordSuccess(), successCalls.incrementAndGet(), return response
     *     - Nếu thất bại: cb.recordFailure(), gọi đệ quy retryWithBackoff(request, cb, attempt + 1)
     *  5. Handle InterruptedException nếu có
     */
    private ServiceResponse retryWithBackoff(
            ServiceRequest request, 
            CircuitBreaker cb, 
            int attempt) {
        throw new UnsupportedOperationException("TODO: implement retryWithBackoff");
    }
    
    /**
     * TODO: Simulate service call.
     * Hướng dẫn:
     *  - Giả lập network delay: Thread.sleep(random 50-250ms)
     *  - Giả lập random failure: 20% chance return ServiceResponse.failure()
     *  - Nếu thành công: return ServiceResponse.success() với data
     *  - Handle InterruptedException nếu có
     */
    private ServiceResponse callService(ServiceRequest request) {
        throw new UnsupportedOperationException("TODO: implement callService");
    }
    
    /**
     * TODO: Shutdown executors gracefully.
     * Hướng dẫn:
     *  1. Gọi shutdown() cho ioExecutor, cpuExecutor, scheduledExecutor
     *  2. Dùng awaitTermination(5, TimeUnit.SECONDS) để đợi các task hoàn thành
     *  3. Nếu timeout mà chưa xong, gọi shutdownNow() để force stop
     *  4. Handle InterruptedException nếu có
     */
    public void shutdown() {
        throw new UnsupportedOperationException("TODO: implement shutdown");
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
        // TODO: Khai báo các field cần thiết:
        //  - volatile State state = State.CLOSED
        //  - AtomicInteger failureCount = new AtomicInteger(0)
        //  - AtomicInteger successCount = new AtomicInteger(0)
        //  - static final int FAILURE_THRESHOLD = 5
        //  - static final int SUCCESS_THRESHOLD = 2
        
        enum State { CLOSED, OPEN, HALF_OPEN }
        
        /**
         * TODO: Ghi nhận một lần gọi service thành công.
         * Hướng dẫn:
         *  - Nếu state = HALF_OPEN:
         *    + increment successCount
         *    + Nếu successCount >= SUCCESS_THRESHOLD: chuyển về CLOSED, reset counters
         *  - Nếu state = CLOSED: reset failureCount về 0
         */
        public void recordSuccess() {
            throw new UnsupportedOperationException("TODO: implement recordSuccess");
        }
        
        /**
         * TODO: Ghi nhận một lần gọi service thất bại.
         * Hướng dẫn:
         *  - Increment failureCount
         *  - Nếu state = CLOSED và failureCount >= FAILURE_THRESHOLD: chuyển sang OPEN
         *  - Nếu state = HALF_OPEN: chuyển về OPEN
         *  - (Nâng cao) Schedule transition từ OPEN -> HALF_OPEN sau timeout (ví dụ: 30 giây)
         */
        public void recordFailure() {
            throw new UnsupportedOperationException("TODO: implement recordFailure");
        }
        
        /**
         * TODO: Kiểm tra CircuitBreaker có đang OPEN không.
         */
        public boolean isOpen() {
            throw new UnsupportedOperationException("TODO: implement isOpen");
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

