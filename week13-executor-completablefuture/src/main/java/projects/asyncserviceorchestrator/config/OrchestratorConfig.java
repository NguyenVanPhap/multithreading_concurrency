package projects.asyncserviceorchestrator.config;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;

/**
 * Cấu hình tổng thể cho Async Orchestrator:
 * - Số thread cho từng loại Executor
 * - Timeout mặc định
 * <p>
 * Pattern: immutable + Lombok @Builder để dễ mở rộng.
 * Hầu hết logic ở đây khá đơn giản, chủ yếu là chỗ để bạn gắn thêm field/TODO.
 */
@Value
@Builder
public class OrchestratorConfig {

    @Builder.Default
    int ioThreads = 10;
    
    @Builder.Default
    int cpuThreads = Runtime.getRuntime().availableProcessors();
    
    @Builder.Default
    int schedulerThreads = 4;
    
    @Builder.Default
    Duration defaultRequestTimeout = Duration.ofSeconds(1);
    
    @Builder.Default
    int globalMaxRetries = 3;
    
    @Builder.Default
    int defaultCircuitBreakerFailureThreshold = 5;
    
    @Builder.Default
    boolean metricsEnabled = true;
    
    @Builder.Default
    boolean tracingEnabled = false;
    
    @Builder.Default
    String loggingLevel = "INFO";
    
    // Validation method để gọi sau khi build (optional)
    public void validate() {
        if (ioThreads <= 0) {
            throw new IllegalArgumentException("ioThreads must be > 0");
        }
        if (cpuThreads <= 0) {
            throw new IllegalArgumentException("cpuThreads must be > 0");
        }
        if (schedulerThreads <= 0) {
            throw new IllegalArgumentException("schedulerThreads must be > 0");
        }
        if (defaultRequestTimeout == null || defaultRequestTimeout.isNegative() || defaultRequestTimeout.isZero()) {
            throw new IllegalArgumentException("defaultRequestTimeout must be positive");
        }
        if (globalMaxRetries < 0) {
            throw new IllegalArgumentException("globalMaxRetries must be >= 0");
        }
        if (defaultCircuitBreakerFailureThreshold <= 0) {
            throw new IllegalArgumentException("defaultCircuitBreakerFailureThreshold must be > 0");
        }
        if (loggingLevel == null || loggingLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("loggingLevel cannot be null or empty");
        }
    }
}

