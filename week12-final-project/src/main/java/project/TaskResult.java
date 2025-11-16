package project;

/**
 * TODO: Implement TaskResult class
 */
public class TaskResult {
    
    private final int taskId;
    private final boolean success;
    private final String result;
    private final long processingTime;
    private final String errorMessage;
    
    public TaskResult(int taskId, boolean success, String result, 
                     long processingTime, String errorMessage) {
        this.taskId = taskId;
        this.success = success;
        this.result = result;
        this.processingTime = processingTime;
        this.errorMessage = errorMessage;
    }
    
    public int getTaskId() { return taskId; }
    public boolean isSuccess() { return success; }
    public String getResult() { return result; }
    public long getProcessingTime() { return processingTime; }
    public String getErrorMessage() { return errorMessage; }
    
    @Override
    public String toString() {
        // TODO: Return string representation
        return ""; // TODO: Return formatted string
    }
}

