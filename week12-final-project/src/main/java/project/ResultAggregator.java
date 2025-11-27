package project;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO: Implement ResultAggregator
 * 
 * Collect results từ workers
 * - Combine results
 * - Statistics
 */
public class ResultAggregator {
    
    private final CompletionService<TaskResult> completionService;
    private final AtomicInteger completedTasks;
    private final AtomicInteger failedTasks;
    
    public ResultAggregator(ExecutorService executor) {
        this.completionService = null; // TODO: Create ExecutorCompletionService
        this.completedTasks = null; // TODO: Create AtomicInteger
        this.failedTasks = null; // TODO: Create AtomicInteger
    }
    
    /**
     * TODO: Submit task và track result
     */
    public void submitTask(Callable<TaskResult> task) {
        // TODO: Submit to completionService
    }
    
    /**
     * TODO: Collect results
     */
    public List<TaskResult> collectResults(int numTasks) throws InterruptedException {
        List<TaskResult> results = new ArrayList<>();
        
        // TODO: Collect results as they complete
        // TODO: Use completionService.take()
        // TODO: Handle exceptions
        // TODO: Update statistics
        
        return results;
    }
    
    public int getCompletedTasks() {
        // TODO: Return completed count
        return 0; // TODO: Return count
    }
    
    public int getFailedTasks() {
        // TODO: Return failed count
        return 0; // TODO: Return count
    }
}

