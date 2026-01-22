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
import java.util.stream.Collectors;

/**
 * "Trái tim" của project mới: engine điều phối các service bất đồng bộ.
 * <p>
 * Nơi đây bạn sẽ:
 * - Quản lý executors (IO / CPU / Scheduler)
 * - Lưu ServiceConfig, CircuitBreaker
 * - Expose API orchestrate phức tạp cho các scenario (Checkout, Refund, ...)
 * <p>
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
     * Hướng dẫn chi tiết:
     *  1. Validate serviceConfig:
     *     - Kiểm tra serviceConfig != null
     *     - Kiểm tra serviceName != null và không rỗng
     *     - Kiểm tra timeout != null, > 0 và không âm
     *     - Nếu không hợp lệ, throw IllegalArgumentException với message rõ ràng
     *  2. Lưu vào serviceConfigs map với key là serviceName
     *  3. Khởi tạo CircuitBreaker cho service này nếu chưa có:
     *     - Dùng circuitBreakers.putIfAbsent() để tránh ghi đè
     *     - Tạo CircuitBreaker với failureThreshold từ config.getDefaultCircuitBreakerFailureThreshold()
     *  (Nâng cao) Tạo executor riêng cho high-priority service (bulkhead pattern):
     *     - Kiểm tra serviceConfig.isHighPriority()
     *     - Tạo dedicated executor cho service này để tránh ảnh hưởng lẫn nhau
     */
    public void registerService(ServiceConfig serviceConfig) {
        if (serviceConfig == null || serviceConfig.getServiceName() == null) {
            throw new IllegalArgumentException("ServiceConfig or serviceName cannot be null");
        }

        if (serviceConfig.getTimeout() == null || serviceConfig.getTimeout().isNegative() || serviceConfig.getTimeout().isZero()) {
            throw new IllegalArgumentException("ServiceConfig timeout must be positive");
        }

        serviceConfigs.put(serviceConfig.getServiceName(), serviceConfig);
        circuitBreakers.putIfAbsent(
            serviceConfig.getServiceName(),
            new CircuitBreaker(config.getDefaultCircuitBreakerFailureThreshold())
        );


    }

    /**
     * TODO: Lấy config của một service, fallback về default nếu chưa có.
     * Hướng dẫn chi tiết:
     *  1. Validate serviceName != null, throw IllegalArgumentException nếu null
     *  2. Kiểm tra serviceConfigs.containsKey(serviceName):
     *     - Nếu có: return serviceConfigs.get(serviceName)
     *     - Nếu không: tạo default config:
     *       * Dùng ServiceConfig.builder()
     *       * Set serviceName = serviceName truyền vào
     *       * Set timeout = config.getDefaultRequestTimeout()
     *       * Set maxRetries = config.getGlobalMaxRetries()
     *       * Các field khác dùng default value (cpuBound=false, priority=0, metadata=empty)
     *       * Gọi build() để tạo ServiceConfig
     *  3. Return ServiceConfig (đã đăng ký hoặc default)
     *  (Nâng cao) Cho phép custom default config thay vì hard-code:
     *     - Thêm method setDefaultServiceConfig(ServiceConfig) để set default template
     *     - Khi tạo default, copy từ template thay vì hard-code
     */
    public ServiceConfig getServiceConfig(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName cannot be null");
        }

        if (serviceConfigs.containsKey(serviceName)) {
            return serviceConfigs.get(serviceName);
        } else {
            return ServiceConfig.builder()
                .serviceName(serviceName)
                .timeout(config.getDefaultRequestTimeout())
                .maxRetries(config.getGlobalMaxRetries())
                .build();
        }


        //throw new UnsupportedOperationException("TODO: implement getServiceConfig");
    }

    /**
     * TODO: API tổng quát để orchestrate nhiều service song song.
     * Hướng dẫn chi tiết:
     *  1. Validate input:
     *     - Kiểm tra serviceNames != null và không rỗng
     *     - Nếu rỗng, return CompletableFuture.completedFuture(null)
     *  2. Tạo CompletableFuture cho từng serviceName:
     *     - Dùng stream().map(serviceName -> callSingleService(serviceName))
     *     - Collect thành List<CompletableFuture<String>>
     *  3. Dùng CompletableFuture.allOf() để đợi tất cả hoàn thành:
     *     - Convert list thành array: futures.toArray(new CompletableFuture[0])
     *     - CompletableFuture.allOf(...array)
     *  4. Handle exception:
     *     - Dùng handle() hoặc exceptionally() để catch exception từ bất kỳ service nào
     *     - Có thể dùng join() trong thenRun() để propagate exception nếu cần
     *  5. Return CompletableFuture<Void> (không cần kết quả, chỉ cần biết đã xong)
     *  (Nâng cao) Thiết kế lớp OrchestratedRequest/OrchestratedResponse riêng để mô tả request phức tạp
     *  (Nâng cao) Dùng thenCombine / anyOf để combine kết quả theo logic nghiệp vụ
     */
    public CompletableFuture<Void> orchestrateSimple(List<String> serviceNames) {
        if (serviceNames == null) {
            throw new IllegalArgumentException("serviceNames cannot be null");
        }
        if (serviceNames.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<String>> completableFutureList = serviceNames.stream()
            .map(this::callSingleService)
            .toList();

        CompletableFuture<Void> all = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));


        return all.exceptionally(ex -> {
            // propagate exception
            System.err.println("Error in orchestration: " + ex.getMessage());
            return null; // Return Void
        });

    }

    /**
     * TODO: API orchestration nâng cao với dependency giữa các bước.
     * Hướng dẫn chi tiết:
     *  1. Validate request:
     *     - Kiểm tra request != null, throw IllegalArgumentException nếu null
     *     - Có thể validate dependency graph không có cycle (nếu request có method isValidDependencyGraph())
     *  2. Tạo Map<String, CompletableFuture<StepResult>> để lưu future của từng step
     *  3. Duyệt qua từng step trong request.getSteps():
     *     - Với mỗi step, kiểm tra dependencies:
     *       * Nếu không có dependency: tạo CompletableFuture.completedFuture(null) cho depsDone
     *       * Nếu có dependencies: dùng CompletableFuture.allOf() để đợi tất cả dependencies hoàn thành
     *     - Sau khi depsDone, gọi callSingleService(step.getServiceName())
     *     - Wrap kết quả thành StepResult (success hoặc failure)
     *     - Lưu vào map với key là step.getStepName()
     *  4. Dùng Semaphore để giới hạn số step chạy đồng thời (request.getMaxConcurrentSteps())
     *  5. Đợi tất cả step hoàn thành: CompletableFuture.allOf(...)
     *  6. Tổng hợp kết quả:
     *     - Tạo Map<String, StepResult> từ các future đã complete
     *     - Tạo List<String> errors từ các step failed
     *     - Xác định status: SUCCESS nếu không có lỗi, PARTIAL_SUCCESS nếu có lỗi nhưng allowPartialSuccess=true, FAILED nếu không
     *  7. Tạo OrchestratedResponse với requestId, status, stepResults, errors, totalDuration
     *  8. Return CompletableFuture<OrchestratedResponse>
     * <p>
     *  Lưu ý:
     *  - Dùng thenCompose() để chain dependency
     *  - Handle timeout cho từng step bằng step.getTimeout()
     *  - Handle exception từng step và wrap thành StepResult.failure()
     */
    public CompletableFuture<OrchestratedResult> orchestrate(OrchestratedRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("OrchestratedRequest cannot be null");
        }


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
     * Hướng dẫn chi tiết:
     *  1. Validate serviceName != null
     *  2. Giả lập latency:
     *     - Dùng ThreadLocalRandom.current().nextInt(100, 401) để random 100-400ms
     *     - Thread.sleep(sleepMs)
     *     - Handle InterruptedException: set interrupt flag và throw RuntimeException
     *  3. Giả lập random failure:
     *     - Dùng ThreadLocalRandom.current().nextInt(100) < 20 để có 20% chance
     *     - Nếu fail: throw new RuntimeException("Random failure when calling " + serviceName)
     *  4. Return string "Data from {serviceName}" nếu thành công
     * <p>
     *  Lưu ý:
     *  - Method này là private, chỉ được gọi từ callSingleService() hoặc callWithRetry()
     *  - Không cần handle timeout ở đây (timeout được handle ở CompletableFuture.orTimeout())
     *  (Nâng cao) Tách phần này sang interface ServiceClient để có thể:
     *    - Tạo interface ServiceClient với method call(String serviceName): CompletableFuture<String>
     *    - Implement ServiceClientSimulator cho simulate
     *    - Implement ServiceClientHttp cho HTTP client thực tế
     *    - Mock trong unit test dễ dàng hơn
     */
    private String simulateServiceCall(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement simulateServiceCall");
    }

    /**
     * TODO: Shutdown executors gracefully.
     * Hướng dẫn chi tiết:
     *  1. Gọi shutdown() cho tất cả executors (không nhận task mới):
     *     - ioExecutor.shutdown()
     *     - cpuExecutor.shutdown()
     *     - scheduler.shutdown()
     *  2. Đợi các task hoàn thành với timeout:
     *     - Dùng awaitTermination(5, TimeUnit.SECONDS) cho mỗi executor
     *     - Nếu timeout (return false): gọi shutdownNow() để force stop
     *  3. Handle InterruptedException:
     *     - Nếu bị interrupt trong lúc awaitTermination: set interrupt flag lại
     *     - Gọi shutdownNow() để force stop
     *  4. Log thông tin:
     *     - Nếu shutdownNow() được gọi: in số lượng task bị cancel
     *     - Có thể dùng System.err.println() hoặc logger nếu có
     *  5. Lưu ý: Có thể tạo helper method shutdownExecutor(ExecutorService, String name) để tránh code lặp
     * <p>
     *  Ví dụ pattern:
     *  try {
     *    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
     *      List<Runnable> dropped = executor.shutdownNow();
     *      System.err.println("Force shutdown " + name + ", dropped tasks: " + dropped.size());
     *    }
     *  } catch (InterruptedException e) {
     *    Thread.currentThread().interrupt();
     *    executor.shutdownNow();
     *  }
     */
    public void shutdown() {
        throw new UnsupportedOperationException("TODO: implement shutdown");
    }

    // ======================
    // Orchestration model
    // ======================

    /**
     * TODO: mô tả một request orchestration phức tạp với nhiều step và dependency.
     * <p>
     * Gợi ý:
     * - có thể dùng builder pattern
     * - mỗi step chứa: tên, serviceName, danh sách step phụ thuộc
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

}


