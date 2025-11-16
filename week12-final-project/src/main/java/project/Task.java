package project;

import java.util.concurrent.Callable;

/**
 * TODO: Implement Task class
 */
public class Task implements Callable<TaskResult>, Comparable<Task> {
    
    private final int id;
    private final int priority;
    private final String description;
    private final long estimatedDuration;
    
    public Task(int id, int priority, String description, long estimatedDuration) {
        this.id = id;
        this.priority = priority;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
    }
    
    @Override
    public TaskResult call() throws Exception {
        // TODO: Execute task
        // TODO: Simulate work với estimatedDuration
        // TODO: Return TaskResult
        return null; // TODO: Return result
    }
    
    @Override
    public int compareTo(Task other) {
        // TODO: Compare by priority (higher priority first)
        return 0; // TODO: Return comparison result
    }
    
    public int getId() { return id; }
    public int getPriority() { return priority; }
    public String getDescription() { return description; }
    public long getEstimatedDuration() { return estimatedDuration; }
}

