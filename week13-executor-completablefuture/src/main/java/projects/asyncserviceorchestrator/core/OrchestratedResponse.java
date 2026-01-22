package projects.asyncserviceorchestrator.core;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.util.*;

/**
 * Chứa kết quả orchestration: kết quả từng step, danh sách lỗi, trạng thái cuối cùng.
 * 
 * Pattern: immutable + Lombok @Builder để dễ mở rộng.
 */
@Value
@Builder
public class OrchestratedResponse {
    
    /**
     * Kết quả của một step cụ thể.
     */
    @Value
    @Builder
    public static class StepResult {
        String stepName;
        String serviceName;
        boolean success;
        String result; // Kết quả nếu thành công
        String error; // Lỗi nếu thất bại
        @Builder.Default
        Duration duration = Duration.ZERO;
        @Builder.Default
        int attemptCount = 1;
        @Builder.Default
        Map<String, String> metadata = new HashMap<>();
        
        public Map<String, String> getMetadata() {
            return Collections.unmodifiableMap(metadata);
        }
        
        public static StepResult success(String stepName, String serviceName, String result, Duration duration) {
            return StepResult.builder()
                    .stepName(stepName)
                    .serviceName(serviceName)
                    .success(true)
                    .result(result)
                    .duration(duration)
                    .build();
        }
        
        public static StepResult failure(String stepName, String serviceName, String error, Duration duration) {
            return StepResult.builder()
                    .stepName(stepName)
                    .serviceName(serviceName)
                    .success(false)
                    .error(error)
                    .duration(duration)
                    .build();
        }
    }
    
    public enum Status {
        SUCCESS,        // Tất cả step thành công
        PARTIAL_SUCCESS, // Một số step thành công, một số fail (khi allowPartialSuccess = true)
        FAILED,         // Có step fail và không cho phép partial success
        TIMEOUT,        // Vượt quá totalTimeout
        CANCELLED       // Bị hủy bởi người dùng hoặc hệ thống
    }
    
    @Builder.Default
    String requestId = UUID.randomUUID().toString();
    
    @Builder.Default
    Status status = Status.FAILED;
    
    @Builder.Default
    Map<String, StepResult> stepResults = new HashMap<>();
    
    @Builder.Default
    List<String> errors = new ArrayList<>();
    
    @Builder.Default
    Duration totalDuration = Duration.ZERO;
    
    @Builder.Default
    Map<String, String> metadata = new HashMap<>();
    
    public Map<String, StepResult> getStepResults() {
        return Collections.unmodifiableMap(stepResults);
    }
    
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
    
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
    
    /**
     * Lấy kết quả của một step cụ thể.
     */
    public Optional<StepResult> getStepResult(String stepName) {
        return Optional.ofNullable(stepResults.get(stepName));
    }
    
    /**
     * Kiểm tra xem tất cả step có thành công không.
     */
    public boolean isAllStepsSuccessful() {
        return stepResults.values().stream()
                .allMatch(StepResult::isSuccess);
    }
    
    /**
     * Lấy danh sách các step thành công.
     */
    public List<String> getSuccessfulSteps() {
        return stepResults.values().stream()
                .filter(StepResult::isSuccess)
                .map(StepResult::getStepName)
                .toList();
    }
    
    /**
     * Lấy danh sách các step thất bại.
     */
    public List<String> getFailedSteps() {
        return stepResults.values().stream()
                .filter(step -> !step.isSuccess())
                .map(StepResult::getStepName)
                .toList();
    }
    
    /**
     * Lấy tổng số step đã thực hiện.
     */
    public int getTotalSteps() {
        return stepResults.size();
    }
    
    /**
     * Lấy số step thành công.
     */
    public int getSuccessfulStepCount() {
        return (int) stepResults.values().stream()
                .filter(StepResult::isSuccess)
                .count();
    }
    
    /**
     * Lấy số step thất bại.
     */
    public int getFailedStepCount() {
        return (int) stepResults.values().stream()
                .filter(step -> !step.isSuccess())
                .count();
    }
    
    /**
     * Tính tỷ lệ thành công (0.0 - 1.0).
     */
    public double getSuccessRate() {
        if (stepResults.isEmpty()) {
            return 0.0;
        }
        return (double) getSuccessfulStepCount() / getTotalSteps();
    }
    
    /**
     * Tạo response thành công.
     */
    public static OrchestratedResponse success(String requestId, Map<String, StepResult> stepResults, Duration totalDuration) {
        return OrchestratedResponse.builder()
                .requestId(requestId)
                .status(Status.SUCCESS)
                .stepResults(stepResults)
                .totalDuration(totalDuration)
                .build();
    }
    
    /**
     * Tạo response thất bại.
     */
    public static OrchestratedResponse failure(String requestId, Map<String, StepResult> stepResults, 
                                               List<String> errors, Duration totalDuration) {
        return OrchestratedResponse.builder()
                .requestId(requestId)
                .status(Status.FAILED)
                .stepResults(stepResults)
                .errors(errors)
                .totalDuration(totalDuration)
                .build();
    }
    
    /**
     * Tạo response partial success.
     */
    public static OrchestratedResponse partialSuccess(String requestId, Map<String, StepResult> stepResults,
                                                     List<String> errors, Duration totalDuration) {
        return OrchestratedResponse.builder()
                .requestId(requestId)
                .status(Status.PARTIAL_SUCCESS)
                .stepResults(stepResults)
                .errors(errors)
                .totalDuration(totalDuration)
                .build();
    }
    
    /**
     * Tạo response timeout.
     */
    public static OrchestratedResponse timeout(String requestId, Map<String, StepResult> stepResults, Duration totalDuration) {
        return OrchestratedResponse.builder()
                .requestId(requestId)
                .status(Status.TIMEOUT)
                .stepResults(stepResults)
                .errors(List.of("Request timeout after " + totalDuration))
                .totalDuration(totalDuration)
                .build();
    }
    
    // Custom validation trong builder
    public static class OrchestratedResponseBuilder {
        public OrchestratedResponse build() {
            // Validation
            String finalRequestId = requestId;
            if (finalRequestId == null || finalRequestId.trim().isEmpty()) {
                finalRequestId = UUID.randomUUID().toString();
            }
            
            Status finalStatus = status;
            if (finalStatus == null) {
                finalStatus = Status.FAILED;
            }
            
            Map<String, StepResult> finalStepResults = stepResults;
            if (finalStepResults == null) {
                finalStepResults = new HashMap<>();
            }
            
            List<String> finalErrors = errors;
            if (finalErrors == null) {
                finalErrors = new ArrayList<>();
            }
            
            Duration finalDuration = totalDuration;
            if (finalDuration == null) {
                finalDuration = Duration.ZERO;
            }
            
            Map<String, String> finalMetadata = metadata;
            if (finalMetadata == null) {
                finalMetadata = new HashMap<>();
            }
            
            // Tự động xác định status nếu chưa được set rõ ràng
            if (finalStatus == Status.FAILED && finalStepResults.isEmpty() && finalErrors.isEmpty()) {
                // Nếu không có step results và errors, có thể là timeout hoặc cancelled
                finalStatus = Status.TIMEOUT;
            }
            
            return new OrchestratedResponse(
                finalRequestId,
                finalStatus,
                finalStepResults,
                finalErrors,
                finalDuration,
                finalMetadata
            );
        }
    }
}

