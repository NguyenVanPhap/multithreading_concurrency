package projects.asyncserviceorchestrator.config;

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
public class ServiceConfig {

    private final String serviceName;
    private final Duration timeout;
    private final int maxRetries;
    private final boolean cpuBound;
    private final int priority; // số càng lớn, ưu tiên càng cao

    // (Nâng cao) metadata linh hoạt: headers, tags, v.v.
    private final Map<String, String> metadata;

    private ServiceConfig(Builder builder) {
        this.serviceName = builder.serviceName;
        this.timeout = builder.timeout;
        this.maxRetries = builder.maxRetries;
        this.cpuBound = builder.cpuBound;
        this.priority = builder.priority;
        this.metadata = Collections.unmodifiableMap(new HashMap<>(builder.metadata));
    }

    public String getServiceName() {
        return serviceName;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public boolean isCpuBound() {
        return cpuBound;
    }

    public int getPriority() {
        return priority;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    // TODO: (nâng cao) encapsulate logic, ví dụ:
    //  - boolean isHighPriority()
    //  - boolean isRetryEnabled()
    //  - Duration computeNextBackoff(int attempt)

    /**
     * TODO: implement logic xác định service có priority cao hay không.
     */
    public boolean isHighPriority() {
        throw new UnsupportedOperationException("TODO: implement isHighPriority()");
    }

    /**
     * TODO: implement logic xem service này có bật retry không (dựa vào maxRetries / metadata).
     */
    public boolean isRetryEnabled() {
        throw new UnsupportedOperationException("TODO: implement isRetryEnabled()");
    }

    /**
     * TODO: tính toán backoff cho lần retry thứ {@code attempt}.
     * Gợi ý: dùng metadata để chọn chiến lược (linear / exponential / jitter).
     */
    public Duration computeNextBackoff(int attempt) {
        throw new UnsupportedOperationException("TODO: implement computeNextBackoff(int)");
    }

    // ===== Builder =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String serviceName;
        private Duration timeout = Duration.ofSeconds(1);
        private int maxRetries = 0;
        private boolean cpuBound = false;
        private int priority = 0;
        private Map<String, String> metadata = new HashMap<>();

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder cpuBound(boolean cpuBound) {
            this.cpuBound = cpuBound;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder metadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata.putAll(metadata);
            return this;
        }

        public ServiceConfig build() {
            // TODO: validate serviceName != null, timeout > 0, maxRetries >= 0, ...
            return new ServiceConfig(this);
        }
    }
}


