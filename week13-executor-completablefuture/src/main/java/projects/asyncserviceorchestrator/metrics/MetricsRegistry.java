package projects.asyncserviceorchestrator.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private final Map<String, AtomicInteger> totalCallsPerService = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> successCallsPerService = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> failedCallsPerService = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> retriesPerService = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> latenciesPerService = new ConcurrentHashMap<>();

    /**
     * Ghi nhận một lần gọi service.
     */
    public void recordCall(String serviceName) {
        totalCallsPerService.computeIfAbsent(serviceName, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Ghi nhận một lần gọi service thành công.
     */
    public void recordSuccess(String serviceName) {
        successCallsPerService.computeIfAbsent(serviceName, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Ghi nhận một lần gọi service thất bại.
     */
    public void recordFailure(String serviceName) {
        failedCallsPerService.computeIfAbsent(serviceName, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Ghi nhận một lần retry.
     */
    public void recordRetry(String serviceName) {
        retriesPerService.computeIfAbsent(serviceName, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Lưu thời gian xử lý (latency) cho mỗi service để sau này tính min/max/avg/p95.
     */
    public void recordLatency(String serviceName, long millis) {
        latenciesPerService.computeIfAbsent(serviceName, k -> new ArrayList<>()).add(millis);
    }

    /**
     * In summary ra console.
     */
    public void printSummary() {
        System.out.println("\n========== METRICS SUMMARY ==========");
        
        int totalCalls = 0;
        int totalSuccess = 0;
        int totalFailed = 0;
        int totalRetries = 0;
        
        for (String serviceName : totalCallsPerService.keySet()) {
            int calls = totalCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int success = successCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int failed = failedCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int retries = retriesPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            
            double successRate = calls > 0 ? (success * 100.0 / calls) : 0.0;
            
            System.out.printf("Service: %s%n", serviceName);
            System.out.printf("  Total Calls: %d%n", calls);
            System.out.printf("  Success: %d (%.2f%%)%n", success, successRate);
            System.out.printf("  Failed: %d%n", failed);
            System.out.printf("  Retries: %d%n", retries);
            
            List<Long> latencies = latenciesPerService.get(serviceName);
            if (latencies != null && !latencies.isEmpty()) {
                long min = latencies.stream().mapToLong(Long::longValue).min().orElse(0);
                long max = latencies.stream().mapToLong(Long::longValue).max().orElse(0);
                double avg = latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
                long p95 = calculatePercentile(latencies, 95);
                System.out.printf("  Latency - Min: %dms, Max: %dms, Avg: %.2fms, P95: %dms%n", 
                    min, max, avg, p95);
            }
            System.out.println();
            
            totalCalls += calls;
            totalSuccess += success;
            totalFailed += failed;
            totalRetries += retries;
        }
        
        double overallSuccessRate = totalCalls > 0 ? (totalSuccess * 100.0 / totalCalls) : 0.0;
        System.out.println("========== OVERALL SUMMARY ==========");
        System.out.printf("Total Calls: %d%n", totalCalls);
        System.out.printf("Total Success: %d (%.2f%%)%n", totalSuccess, overallSuccessRate);
        System.out.printf("Total Failed: %d%n", totalFailed);
        System.out.printf("Total Retries: %d%n", totalRetries);
        System.out.println("=====================================\n");
    }

    /**
     * Export metrics dưới dạng JSON string để phục vụ cho HTTP endpoint / logging.
     */
    public String exportAsJson() {
        StringBuilder json = new StringBuilder("{\n");
        json.append("  \"services\": [\n");
        
        boolean first = true;
        for (String serviceName : totalCallsPerService.keySet()) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            int calls = totalCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int success = successCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int failed = failedCallsPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            int retries = retriesPerService.getOrDefault(serviceName, new AtomicInteger(0)).get();
            
            json.append("    {\n");
            json.append(String.format("      \"serviceName\": \"%s\",\n", serviceName));
            json.append(String.format("      \"totalCalls\": %d,\n", calls));
            json.append(String.format("      \"success\": %d,\n", success));
            json.append(String.format("      \"failed\": %d,\n", failed));
            json.append(String.format("      \"retries\": %d\n", retries));
            json.append("    }");
        }
        
        json.append("\n  ]\n");
        json.append("}");
        
        return json.toString();
    }

    private long calculatePercentile(List<Long> values, int percentile) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        List<Long> sorted = new ArrayList<>(values);
        sorted.sort(Long::compareTo);
        int index = (int) Math.ceil((percentile / 100.0) * sorted.size()) - 1;
        return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
    }
}


