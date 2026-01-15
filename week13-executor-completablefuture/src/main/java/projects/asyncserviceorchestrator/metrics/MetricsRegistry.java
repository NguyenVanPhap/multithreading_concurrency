package projects.asyncserviceorchestrator.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Registry lưu trữ metrics cho toàn bộ orchestrator.
 *
 * Mục tiêu:
 * - Tracking số lượng call / success / fail / retry theo từng serviceName
 * - Để bạn có chỗ mở rộng thêm các metric nâng cao (latency, P95, ...)
 *
 * Hiện tại mới là skeleton đơn giản, với nhiều TODO cho bạn.
 */
public class MetricsRegistry {

    // TODO: Khai báo các Map để lưu metrics theo từng service
    //  Gợi ý:
    //    - Map<String, AtomicInteger> totalCallsPerService = new ConcurrentHashMap<>();
    //    - Map<String, AtomicInteger> successCallsPerService = new ConcurrentHashMap<>();
    //    - Map<String, AtomicInteger> failedCallsPerService = new ConcurrentHashMap<>();
    //    - Map<String, AtomicInteger> retriesPerService = new ConcurrentHashMap<>();
    //  (Nâng cao) Map<String, List<Long>> latenciesPerService = new ConcurrentHashMap<>(); để track latency
    // 
    // Lưu ý: Bạn cần khai báo các field này để code compile được

    /**
     * TODO: Ghi nhận một lần gọi service.
     * Hướng dẫn: increment counter trong totalCallsPerService cho serviceName đó.
     */
    public void recordCall(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement recordCall");
    }

    /**
     * TODO: Ghi nhận một lần gọi service thành công.
     * Hướng dẫn: increment counter trong successCallsPerService.
     */
    public void recordSuccess(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement recordSuccess");
    }

    /**
     * TODO: Ghi nhận một lần gọi service thất bại.
     * Hướng dẫn: increment counter trong failedCallsPerService.
     */
    public void recordFailure(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement recordFailure");
    }

    /**
     * TODO: Ghi nhận một lần retry.
     * Hướng dẫn: increment counter trong retriesPerService.
     */
    public void recordRetry(String serviceName) {
        throw new UnsupportedOperationException("TODO: implement recordRetry");
    }

    /**
     * TODO: In summary ra console.
     * Hướng dẫn:
     *  - Duyệt qua tất cả services đã được gọi
     *  - In ra: serviceName, total calls, success, failed, retries
     *  - Tính và in tổng toàn hệ thống, tỉ lệ thành công (%)
     *  (Nâng cao) Bạn có thể đổi thành JSON, hoặc integrate với Prometheus
     */
    public void printSummary() {
        throw new UnsupportedOperationException("TODO: implement printSummary");
    }

    // TODO: (nâng cao)
    //  - track histogram latency (min/max/avg/p95) cho mỗi service
    //  - export metrics ra file hoặc HTTP endpoint

    /**
     * TODO: lưu thời gian xử lý (latency) cho mỗi service để sau này tính min/max/avg/p95.
     */
    public void recordLatency(String serviceName, long millis) {
        throw new UnsupportedOperationException("TODO: implement recordLatency");
    }

    /**
     * TODO: export metrics dưới dạng JSON string để phục vụ cho HTTP endpoint / logging.
     */
    public String exportAsJson() {
        throw new UnsupportedOperationException("TODO: implement exportAsJson");
    }
}


