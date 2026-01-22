package projects.asyncserviceorchestrator.config;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Cấu hình cho từng service cụ thể.
 *
 * Đây là nơi bạn có thể:
 * - Gắn timeout riêng cho từng service
 * - Chọn service là CPU-bound hay IO-bound
 * - Set maxRetries, priority, tags, headers mặc định, ...
 *
 * Lớp này cố tình thiết kế theo kiểu "mở rộng dần":
 * nhiều TODO để bạn tự thêm field + behavior theo nhu cầu.
 */
@Value
@Builder
public class ServiceConfig {

    String serviceName;
    
    @Builder.Default
    Duration timeout = Duration.ofSeconds(1);
    
    @Builder.Default
    int maxRetries = 0;
    
    @Builder.Default
    boolean cpuBound = false;
    
    @Builder.Default
    int priority = 0; // số càng lớn, ưu tiên càng cao

    // (Nâng cao) metadata linh hoạt: headers, tags, v.v.
    @Builder.Default
    Map<String, String> metadata = new HashMap<>();

    // Custom getter để đảm bảo metadata immutable
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    // TODO: (nâng cao) encapsulate logic, ví dụ:
    //  - boolean isHighPriority()
    //  - boolean isRetryEnabled()
    //  - Duration computeNextBackoff(int attempt)

    /**
     * Xác định service có priority cao hay không (priority >= 8).
     */
    public boolean isHighPriority() {
        return priority >= 8;
    }

    /**
     * Xem service này có bật retry không (dựa vào maxRetries > 0).
     */
    public boolean isRetryEnabled() {
        return maxRetries > 0;
    }

    /**
     * Tính toán backoff cho lần retry thứ {@code attempt}.
     * Hỗ trợ: exponential (mặc định), linear, và jitter.
     */
    public Duration computeNextBackoff(int attempt) {
        String strategy = metadata.getOrDefault("backoffStrategy", "exponential");
        long baseDelayMs = Long.parseLong(metadata.getOrDefault("baseDelayMs", "100"));
        
        long delayMs;
        switch (strategy.toLowerCase()) {
            case "linear":
                delayMs = baseDelayMs * attempt;
                break;
            case "jitter":
                long exponential = baseDelayMs * (1L << (attempt - 1));
                long jitter = (long) (exponential * 0.1 * Math.random());
                delayMs = exponential + jitter;
                break;
            case "exponential":
            default:
                delayMs = baseDelayMs * (1L << (attempt - 1));
                break;
        }
        
        // Giới hạn tối đa 30 giây
        delayMs = Math.min(delayMs, 30000);
        return Duration.ofMillis(delayMs);
    }

    // Custom validation trong builder
    public static class ServiceConfigBuilder {
        public ServiceConfig build() {
            // Validation trước khi build - truy cập fields từ builder class
            String name = this.serviceName;
            Duration t = this.timeout;
            int retries = this.maxRetries;
            boolean cpu = this.cpuBound;
            int prio = this.priority;
            Map<String, String> meta = this.metadata;
            
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("serviceName cannot be null or empty");
            }
            if (t == null || t.isNegative() || t.isZero()) {
                throw new IllegalArgumentException("timeout must be positive");
            }
            if (retries < 0) {
                throw new IllegalArgumentException("maxRetries must be >= 0");
            }
            if (prio < 0) {
                throw new IllegalArgumentException("priority must be >= 0");
            }
            // Đảm bảo metadata không null - tạo mới nếu null
            Map<String, String> finalMetadata = meta != null ? meta : new HashMap<>();
            // Gọi constructor với các fields từ builder (theo thứ tự: serviceName, timeout, maxRetries, cpuBound, priority, metadata)
            return new ServiceConfig(name, t, retries, cpu, prio, finalMetadata);
        }
    }
}


