package projects.asyncserviceorchestrator.core;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.util.*;

/**
 * Mô tả một request orchestration phức tạp với nhiều step và dependency.
 * 
 * Mỗi request chứa:
 * - Danh sách các step cần thực hiện
 * - Dependency graph giữa các step
 * - Metadata và timeout tổng thể
 * 
 * Pattern: immutable + Lombok @Builder để dễ mở rộng.
 */
@Value
@Builder
public class OrchestratedRequest {
    
    /**
     * Mô tả một step trong orchestration.
     */
    @Value
    @Builder
    public static class Step {
        String stepName;
        String serviceName;
        @Builder.Default
        Set<String> dependencies = new HashSet<>(); // Tên các step phải hoàn thành trước
        @Builder.Default
        Map<String, String> metadata = new HashMap<>();
        @Builder.Default
        Duration timeout = Duration.ofSeconds(5);
        @Builder.Default
        int priority = 0;
        
        public Set<String> getDependencies() {
            return Collections.unmodifiableSet(dependencies);
        }
        
        public Map<String, String> getMetadata() {
            return Collections.unmodifiableMap(metadata);
        }
    }
    
    @Builder.Default
    String requestId = UUID.randomUUID().toString();
    
    @Builder.Default
    List<Step> steps = new ArrayList<>();
    
    @Builder.Default
    Duration totalTimeout = Duration.ofMinutes(5);
    
    @Builder.Default
    Map<String, String> metadata = new HashMap<>();
    
    @Builder.Default
    boolean allowPartialSuccess = false; // Cho phép một số step fail nhưng vẫn tiếp tục
    
    @Builder.Default
    int maxConcurrentSteps = 10; // Giới hạn số step chạy đồng thời
    
    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }
    
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
    
    /**
     * Lấy step theo tên.
     */
    public Optional<Step> getStep(String stepName) {
        return steps.stream()
                .filter(step -> step.getStepName().equals(stepName))
                .findFirst();
    }
    
    /**
     * Kiểm tra tính hợp lệ của dependency graph (không có cycle).
     */
    public boolean isValidDependencyGraph() {
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        
        // Khởi tạo graph và inDegree
        for (Step step : steps) {
            graph.putIfAbsent(step.getStepName(), new HashSet<>());
            inDegree.putIfAbsent(step.getStepName(), 0);
            
            for (String dep : step.getDependencies()) {
                graph.computeIfAbsent(dep, k -> new HashSet<>()).add(step.getStepName());
                inDegree.put(step.getStepName(), inDegree.getOrDefault(step.getStepName(), 0) + 1);
            }
        }
        
        // Topological sort để phát hiện cycle
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        int processed = 0;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            processed++;
            
            for (String neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return processed == steps.size();
    }
    
    // Custom validation trong builder
    public static class OrchestratedRequestBuilder {
        public OrchestratedRequest build() {
            // Validation
            if (steps == null || steps.isEmpty()) {
                throw new IllegalArgumentException("steps cannot be null or empty");
            }
            
            // Kiểm tra duplicate step names
            Set<String> stepNames = new HashSet<>();
            for (Step step : steps) {
                if (step.getStepName() == null || step.getStepName().trim().isEmpty()) {
                    throw new IllegalArgumentException("stepName cannot be null or empty");
                }
                if (step.getServiceName() == null || step.getServiceName().trim().isEmpty()) {
                    throw new IllegalArgumentException("serviceName cannot be null or empty");
                }
                if (stepNames.contains(step.getStepName())) {
                    throw new IllegalArgumentException("Duplicate stepName: " + step.getStepName());
                }
                stepNames.add(step.getStepName());
                
                // Kiểm tra dependencies có tồn tại
                for (String dep : step.getDependencies()) {
                    if (!stepNames.contains(dep) && 
                        steps.stream().noneMatch(s -> s.getStepName().equals(dep))) {
                        throw new IllegalArgumentException(
                            "Dependency '" + dep + "' not found for step '" + step.getStepName() + "'");
                    }
                }
            }
            
            if (totalTimeout == null || totalTimeout.isNegative() || totalTimeout.isZero()) {
                throw new IllegalArgumentException("totalTimeout must be positive");
            }
            
            if (maxConcurrentSteps <= 0) {
                throw new IllegalArgumentException("maxConcurrentSteps must be > 0");
            }
            
            // Tạo request và kiểm tra dependency graph
            String finalRequestId = requestId != null ? requestId : UUID.randomUUID().toString();
            List<Step> finalSteps = steps != null ? steps : new ArrayList<>();
            Map<String, String> finalMetadata = metadata != null ? metadata : new HashMap<>();
            
            OrchestratedRequest request = new OrchestratedRequest(
                finalRequestId,
                finalSteps,
                totalTimeout,
                finalMetadata,
                allowPartialSuccess,
                maxConcurrentSteps
            );
            
            if (!request.isValidDependencyGraph()) {
                throw new IllegalArgumentException("Invalid dependency graph: cycle detected");
            }
            
            return request;
        }
    }
}

