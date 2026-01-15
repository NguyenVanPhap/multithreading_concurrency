package projects.asyncserviceorchestrator.config;

import java.time.Duration;

/**
 * Cấu hình tổng thể cho Async Orchestrator:
 * - Số thread cho từng loại Executor
 * - Timeout mặc định
 *
 * Pattern: immutable + builder để dễ mở rộng.
 * Hầu hết logic ở đây khá đơn giản, chủ yếu là chỗ để bạn gắn thêm field/TODO.
 */
public class OrchestratorConfig {

    private final int ioThreads;
    private final int cpuThreads;
    private final int schedulerThreads;
    private final Duration defaultRequestTimeout;

    // TODO: (nâng cao) thêm các cấu hình:
    //  - int globalMaxRetries;
    //  - int defaultCircuitBreakerFailureThreshold;
    //  - boolean metricsEnabled;
    //  - boolean tracingEnabled;
    //  - String loggingLevel;

    private OrchestratorConfig(Builder builder) {
        this.ioThreads = builder.ioThreads;
        this.cpuThreads = builder.cpuThreads;
        this.schedulerThreads = builder.schedulerThreads;
        this.defaultRequestTimeout = builder.defaultRequestTimeout;
    }

    public int getIoThreads() {
        return ioThreads;
    }

    public int getCpuThreads() {
        return cpuThreads;
    }

    public int getSchedulerThreads() {
        return schedulerThreads;
    }

    public Duration getDefaultRequestTimeout() {
        return defaultRequestTimeout;
    }

    // ===== Builder =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int ioThreads = 10;
        private int cpuThreads = Runtime.getRuntime().availableProcessors();
        private int schedulerThreads = 4;
        private Duration defaultRequestTimeout = Duration.ofSeconds(1);
        // TODO: thêm các field builder tương ứng với phần nâng cao ở trên

        public Builder ioThreads(int ioThreads) {
            this.ioThreads = ioThreads;
            return this;
        }

        public Builder cpuThreads(int cpuThreads) {
            this.cpuThreads = cpuThreads;
            return this;
        }

        public Builder schedulerThreads(int schedulerThreads) {
            this.schedulerThreads = schedulerThreads;
            return this;
        }

        public Builder defaultRequestTimeout(Duration timeout) {
            this.defaultRequestTimeout = timeout;
            return this;
        }

        public OrchestratorConfig build() {
            // TODO: validate input (ioThreads > 0, cpuThreads > 0, timeout != null, ...)
            //  - nếu config sai, ném custom exception: InvalidOrchestratorConfigException
            return new OrchestratorConfig(this);
        }
    }
}


