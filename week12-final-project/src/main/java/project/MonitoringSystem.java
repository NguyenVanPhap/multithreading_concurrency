package project;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO: Implement MonitoringSystem
 * 
 * Track performance metrics
 * - Monitor system health
 * - Generate reports
 */
public class MonitoringSystem {
    
    private final AtomicInteger totalTasks;
    private final AtomicInteger completedTasks;
    private final AtomicInteger failedTasks;
    private final AtomicLong totalProcessingTime;
    
    public MonitoringSystem() {
        this.totalTasks = null; // TODO: Create AtomicInteger
        this.completedTasks = null; // TODO: Create AtomicInteger
        this.failedTasks = null; // TODO: Create AtomicInteger
        this.totalProcessingTime = null; // TODO: Create AtomicLong
    }
    
    public void recordTaskSubmitted() {
        // TODO: Increment totalTasks
    }
    
    public void recordTaskCompleted(long processingTime) {
        // TODO: Increment completedTasks
        // TODO: Add processingTime
    }
    
    public void recordTaskFailed() {
        // TODO: Increment failedTasks
    }
    
    public void printStatistics() {
        // TODO: Print all statistics
        // TODO: Calculate success rate, average processing time, etc.
    }
}

