package projects.asyncserviceorchestrator.core;

import projects.asyncserviceorchestrator.config.OrchestratorConfig;
import projects.asyncserviceorchestrator.config.ServiceConfig;
import projects.asyncserviceorchestrator.metrics.MetricsRegistry;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * "Trái tim" của project mới: engine điều phối các service bất đồng bộ.
 *
 * Nơi đây bạn sẽ:
 * - Quản lý executors (IO / CPU / Scheduler)
 * - Lưu ServiceConfig, CircuitBreaker
 * - Expose API orchestrate phức tạp cho các scenario (Checkout, Refund, ...)
 *
 * Hiện tại chỉ implement phần skeleton để mọi thứ biên dịch được;
 * phần thực sự "ngon" (CompletableFuture chain, retry, circuit breaker, ...)
 * sẽ là các TODO cho bạn tự làm.
 */
public class AsyncOrchestratorEngine {

    private final OrchestratorConfig config;
    private final ExecutorService ioExecutor;
    private final ExecutorService cpuExecutor;
    private final ScheduledExecutorService scheduler;

    private final MetricsRegistry metricsRegistry = new MetricsRegistry();

    private final Map<String, ServiceConfig> serviceConfigs = new ConcurrentHashMap<>();
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();

    public AsyncOrchestratorEngine(OrchestratorConfig config) {
        this.config = Objects.requireNonNull(config);
        this.ioExecutor = Executors.newFixedThreadPool(config.getIoThreads());
        this.cpuExecutor = Executors.newFixedThreadPool(config.getCpuThreads());
        this.scheduler = Executors.newScheduledThreadPool(config.getSchedulerThreads());
    }

    // TODO: cho phép inject custom logger/tracer để theo dõi luồng gọi service
    // private final OrchestratorLogger logger;

    public MetricsRegistry getMetricsRegistry() {
        return metricsRegistry;
    }

    /**
     * TODO: Đăng ký một ServiceConfig vào engine.
     * Hướng dẫn:
     *  1. Validate serviceConfig (serviceName != null, timeout > 0, ...)
     *  2. Lưu vào serviceConfigs map
     *  3. Khởi tạo CircuitBreaker cho service này nếu chưa có
     *  (Nâng cao) Tạo executor riêng cho high-priority service (bulkhead pattern)
     */
    public void registerService(ServiceConfig serviceConfig) {
        throw new UnsupportedOperationException("TODO: implement registerService");
    }

    /**
     * TODO: Lấy config của một service, fallback về default nếu chưa có.
     * Hướng dẫn:
     *  - Kiểm tra serviceConfigs có chứa serviceName không
     *  - Nếu có, return config đó
     *  - Nếu không, tạo default config từ OrchestratorConfig.getDefaultRequestTimeout()
     *  (Nâng cao) Cho phép custom default config thay vì hard-code
     */
    public ServiceConfig getServiceConfig(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement getServiceConfig");
    }

    /**
     * TODO: API tổng quát để orchestrate nhiều service song song.
     * Hướng dẫn:
     *  1. Tạo CompletableFuture cho từng serviceName bằng cách gọi callSingleService()
     *  2. Dùng CompletableFuture.allOf() để đợi tất cả hoàn thành
     *  3. (Nâng cao) Thiết kế lớp OrchestratedRequest/OrchestratedResponse riêng để mô tả request phức tạp
     *  4. (Nâng cao) Dùng thenCombine / anyOf để combine kết quả theo logic nghiệp vụ
     */
    public CompletableFuture<Void> orchestrateSimple(List<String> serviceNames) {
        throw new UnsupportedOperationException("TODO: implement orchestrateSimple");
    }

    /**
     * TODO: API orchestration nâng cao với dependency giữa các bước.
     *
     * Gợi ý:
     *  - OrchestratedRequest mô tả graph các step + dependency
     *  - OrchestratedResult chứa kết quả của từng step + danh sách lỗi
     */
    public CompletableFuture<OrchestratedResult> orchestrate(OrchestratedRequest request) {
        throw new UnsupportedOperationException("TODO: implement orchestrate(OrchestratedRequest)");
    }

    /**
     * TODO: Gọi một service đơn lẻ.
     * Hướng dẫn chi tiết:
     *  1. Lấy ServiceConfig cho serviceName (dùng getServiceConfig())
     *  2. Lấy CircuitBreaker cho service này (tạo mới nếu chưa có)
     *  3. Kiểm tra CircuitBreaker state:
     *     - Nếu OPEN: recordFailure() và return CompletableFuture với error message
     *     - Nếu CLOSED hoặc HALF_OPEN: tiếp tục
     *  4. Record call: metricsRegistry.recordCall(serviceName)
     *  5. Chọn executor phù hợp (ioExecutor nếu IO-bound, cpuExecutor nếu CPU-bound)
     *  6. Tạo CompletableFuture bằng CompletableFuture.supplyAsync(() -> simulateServiceCall(serviceName), executor)
     *  7. Áp dụng timeout: cf.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
     *  8. Handle kết quả:
     *     - Nếu thành công: recordSuccess(), cb.recordSuccess(), return result
     *     - Nếu thất bại: recordFailure(), cb.recordFailure(), gọi callWithRetry() nếu còn retry
     *  9. (Nâng cao) Đo latency và record vào MetricsRegistry
     */
    public CompletableFuture<String> callSingleService(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement callSingleService");
    }

    /**
     * TODO: Implement retry với exponential backoff cho một service đơn lẻ.
     * Hướng dẫn chi tiết:
     *  1. Kiểm tra attempt > serviceConfig.getMaxRetries(): nếu vượt quá, return failure
     *  2. Kiểm tra CircuitBreaker.isOpen(): nếu OPEN, return failure
     *  3. Tính toán delay cho lần retry này:
     *     - Dùng serviceConfig.computeNextBackoff(attempt) để lấy Duration
     *     - Hoặc tự tính exponential: baseDelay * (2 ^ attempt) với jitter
     *  4. Dùng scheduler.schedule(() -> {...}, delay, TimeUnit.MILLISECONDS) để delay retry
     *  5. Trong scheduled task:
     *     - Record retry: metricsRegistry.recordRetry(serviceName)
     *     - Gọi lại callSingleService() hoặc simulateServiceCall() trực tiếp
     *     - Nếu thành công: recordSuccess(), cb.recordSuccess(), return result
     *     - Nếu thất bại: gọi đệ quy callWithRetry(serviceName, attempt+1, ...)
     *  6. Return CompletableFuture từ scheduled task
     */
    public CompletableFuture<String> callWithRetry(
            String serviceName,
            int attempt,
            ServiceConfig serviceConfig,
            CircuitBreaker circuitBreaker
    ) {
        throw new UnsupportedOperationException("TODO: implement callWithRetry");
    }

    /**
     * TODO: Hàm mô phỏng việc gọi service thực tế (HTTP call, DB call, v.v.).
     * Hướng dẫn:
     *  - Giả lập latency: Thread.sleep(random 100-400ms)
     *  - Giả lập random failure: 20% chance throw RuntimeException
     *  - Return string "Data from {serviceName}" nếu thành công
     *  (Nâng cao) Tách phần này sang interface ServiceClient để có thể:
     *    - Mock trong unit test
     *    - Thay thế bằng HTTP client thực tế (OkHttp, Apache HttpClient, ...)
     */
    private String simulateServiceCall(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement simulateServiceCall");
    }

    /**
     * TODO: Shutdown executors gracefully.
     * Hướng dẫn:
     *  1. Gọi shutdown() cho ioExecutor, cpuExecutor, scheduler
     *  2. Dùng awaitTermination(timeout, TimeUnit.SECONDS) để đợi các task hoàn thành
     *  3. Nếu timeout mà chưa xong, gọi shutdownNow() để force stop
     *  4. Log nếu có task bị interrupt hoặc không shutdown được
     */
    public void shutdown() {
        throw new UnsupportedOperationException("TODO: implement shutdown");
    }

    // ======================
    // Orchestration model
    // ======================

    /**
     * TODO: mô tả một request orchestration phức tạp với nhiều step và dependency.
     *
     * Gợi ý:
     *  - có thể dùng builder pattern
     *  - mỗi step chứa: tên, serviceName, danh sách step phụ thuộc
     */
    public static class OrchestratedRequest {
        // TODO: khai báo các field cần thiết (steps, metadata, timeout tổng, ...)
        // TODO: thêm builder để tạo graph orchestration
    }

    /**
     * TODO: chứa kết quả orchestration: kết quả từng step, danh sách lỗi, trạng thái cuối cùng.
     */
    public static class OrchestratedResult {
        // TODO: map stepName -> result, danh sách error, duration tổng, ...
    }

    // ======================
    // Inner CircuitBreaker
    // ======================

    public static class CircuitBreaker {
        public enum State {CLOSED, OPEN, HALF_OPEN}

        // TODO: Khai báo các field cần thiết:
        //  - private volatile State state = State.CLOSED;
        //  - private final AtomicInteger failureCount = new AtomicInteger(0);
        //  - private final AtomicInteger successCount = new AtomicInteger(0);
        //  - private static final int FAILURE_THRESHOLD = 5;
        //  - private static final int SUCCESS_THRESHOLD = 2;
        //  (Nâng cao) private ScheduledFuture<?> resetFuture; để cancel scheduled transition nếu cần
        //
        // Lưu ý: Bạn cần khai báo các field này để code compile được

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
         *  - Nếu state = OPEN: return ngay (không làm gì)
         *  - Increment failureCount
         *  - Nếu failureCount >= FAILURE_THRESHOLD: chuyển sang OPEN
         *  - (Nâng cao) Dùng scheduler để schedule transition từ OPEN -> HALF_OPEN sau X ms
         *    (ví dụ: sau 30 giây, chuyển sang HALF_OPEN để thử lại)
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

        /**
         * TODO: Lấy state hiện tại của CircuitBreaker.
         */
        public State getState() {
            throw new UnsupportedOperationException("TODO: implement getState");
        }
    }
}


