package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

/**
 * Advanced Project: Task Queue Processor
 * 
 * Hệ thống xử lý task queue với:
 * - BlockingQueue cho task queue
 * - ThreadPoolExecutor cho processing
 * - Priority queue cho task ordering
 * - Backpressure handling
 * - Task retry mechanism
 * - Statistics tracking
 * 
 * TODO Tasks:
 * 1. Implement task queue với BlockingQueue
 * 2. ThreadPoolExecutor với custom queue
 * 3. Priority queue cho task ordering
 * 4. Backpressure với bounded queue
 * 5. Task retry với exponential backoff
 * 6. Graceful shutdown
 * 7. Performance metrics
 */
public class TaskQueueProcessor {
    
    // Task queue với priority
    private final BlockingQueue<Task> taskQueue;
    
    // Thread pool executor
    private final ThreadPoolExecutor executor;
    
    // Retry queue
    private final BlockingQueue<FailedTask> retryQueue;
    private final ScheduledExecutorService retryExecutor;
    
    // Statistics
    private final AtomicInteger submittedTasks = new AtomicInteger(0);
    private final AtomicInteger completedTasks = new AtomicInteger(0);
    private final AtomicInteger failedTasks = new AtomicInteger(0);
    private final AtomicInteger retriedTasks = new AtomicInteger(0);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);
    
    private volatile boolean running = true;
    
    public TaskQueueProcessor(int queueCapacity, int corePoolSize, 
                             int maxPoolSize) {
        // TODO: Tạo priority blocking queue
        this.taskQueue = new PriorityBlockingQueue<>(queueCapacity, 
            Comparator.comparing(Task::getPriority).reversed());
        
        // TODO: Tạo ThreadPoolExecutor với custom queue
        this.executor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(queueCapacity),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "TaskProcessor-" + 
                                         threadNumber.getAndIncrement());
                    t.setDaemon(false);
                    return t;
                }
            },
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    System.out.println("[REJECTED] Task rejected, queue full!");
                    // TODO: Handle rejection (log, retry, etc.)
                }
            }
        );
        
        // Retry queue và executor
        this.retryQueue = new LinkedBlockingQueue<>();
        this.retryExecutor = Executors.newScheduledThreadPool(1);
        
        // Start retry processor
        startRetryProcessor();
    }
    
    /**
     * Submit task để xử lý
     */
    public CompletableFuture<TaskResult> submitTask(Task task) {
        if (!running) {
            return CompletableFuture.completedFuture(
                TaskResult.failure(task.getId(), "Processor is shutting down")
            );
        }
        
        submittedTasks.incrementAndGet();
        
        // TODO: Submit task vào queue
        CompletableFuture<TaskResult> future = new CompletableFuture<>();
        
        try {
            taskQueue.put(task);
            
            executor.submit(() -> {
                processTask(task, future);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.complete(TaskResult.failure(task.getId(), "Interrupted"));
        }
        
        return future;
    }
    
    /**
     * Process task
     */
    private void processTask(Task task, CompletableFuture<TaskResult> future) {
        long startTime = System.nanoTime();
        
        try {
            // TODO: Simulate task processing
            Thread.sleep(new Random().nextInt(500) + 100);
            
            // 20% chance of failure để test retry
            if (Math.random() < 0.2) {
                throw new RuntimeException("Task processing failed");
            }
            
            TaskResult result = TaskResult.success(task.getId(), 
                "Processed: " + task.getData());
            
            completedTasks.incrementAndGet();
            long duration = System.nanoTime() - startTime;
            totalProcessingTime.addAndGet(duration);
            
            future.complete(result);
            
        } catch (Exception e) {
            // TODO: Retry với exponential backoff
            handleTaskFailure(task, future, e, 0);
        }
    }
    
    /**
     * Handle task failure với retry
     */
    private void handleTaskFailure(Task task, CompletableFuture<TaskResult> future, 
                                   Exception error, int attempt) {
        final int MAX_RETRIES = 3;
        
        if (attempt >= MAX_RETRIES) {
            failedTasks.incrementAndGet();
            future.complete(TaskResult.failure(task.getId(), 
                "Max retries exceeded: " + error.getMessage()));
            return;
        }
        
        // TODO: Schedule retry với exponential backoff
        long delayMs = 100L * (1L << attempt); // 100, 200, 400ms
        
        retryQueue.offer(new FailedTask(task, future, attempt + 1, delayMs));
        retriedTasks.incrementAndGet();
    }
    
    /**
     * Start retry processor
     */
    private void startRetryProcessor() {
        retryExecutor.scheduleAtFixedRate(() -> {
            List<FailedTask> readyToRetry = new ArrayList<>();
            
            // Collect tasks ready to retry
            retryQueue.drainTo(readyToRetry);
            
            for (FailedTask failedTask : readyToRetry) {
                if (System.currentTimeMillis() >= failedTask.getRetryTime()) {
                    // Retry task
                    executor.submit(() -> {
                        processTask(failedTask.getTask(), failedTask.getFuture());
                    });
                } else {
                    // Put back to queue
                    retryQueue.offer(failedTask);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Get statistics
     */
    public ProcessorStatistics getStatistics() {
        return new ProcessorStatistics(
            submittedTasks.get(),
            completedTasks.get(),
            failedTasks.get(),
            retriedTasks.get(),
            taskQueue.size(),
            executor.getActiveCount(),
            executor.getQueue().size(),
            totalProcessingTime.get() / 1_000_000 // Convert to ms
        );
    }
    
    /**
     * Shutdown gracefully
     */
    public void shutdown() {
        running = false;
        
        // Stop accepting new tasks
        executor.shutdown();
        retryExecutor.shutdown();
        
        try {
            // Wait for running tasks
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            
            if (!retryExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                retryExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            retryExecutor.shutdownNow();
        }
    }
    
    // Inner classes
    static class Task {
        private final String id;
        private final String data;
        private final int priority;
        
        public Task(String id, String data, int priority) {
            this.id = id;
            this.data = data;
            this.priority = priority;
        }
        
        public String getId() { return id; }
        public String getData() { return data; }
        public int getPriority() { return priority; }
    }
    
    static class TaskResult {
        private final String taskId;
        private final boolean success;
        private final String result;
        private final String error;
        
        private TaskResult(String taskId, boolean success, String result, String error) {
            this.taskId = taskId;
            this.success = success;
            this.result = result;
            this.error = error;
        }
        
        public static TaskResult success(String taskId, String result) {
            return new TaskResult(taskId, true, result, null);
        }
        
        public static TaskResult failure(String taskId, String error) {
            return new TaskResult(taskId, false, null, error);
        }
        
        public boolean isSuccess() { return success; }
        public String getResult() { return result; }
        public String getError() { return error; }
    }
    
    static class FailedTask {
        private final Task task;
        private final CompletableFuture<TaskResult> future;
        private final int attempt;
        private final long retryTime;
        
        public FailedTask(Task task, CompletableFuture<TaskResult> future, 
                         int attempt, long delayMs) {
            this.task = task;
            this.future = future;
            this.attempt = attempt;
            this.retryTime = System.currentTimeMillis() + delayMs;
        }
        
        public Task getTask() { return task; }
        public CompletableFuture<TaskResult> getFuture() { return future; }
        public int getAttempt() { return attempt; }
        public long getRetryTime() { return retryTime; }
    }
    
    static class ProcessorStatistics {
        private final int submitted;
        private final int completed;
        private final int failed;
        private final int retried;
        private final int queueSize;
        private final int activeThreads;
        private final int executorQueueSize;
        private final long avgProcessingTimeMs;
        
        public ProcessorStatistics(int submitted, int completed, int failed, 
                                  int retried, int queueSize, int activeThreads,
                                  int executorQueueSize, long avgProcessingTimeMs) {
            this.submitted = submitted;
            this.completed = completed;
            this.failed = failed;
            this.retried = retried;
            this.queueSize = queueSize;
            this.activeThreads = activeThreads;
            this.executorQueueSize = executorQueueSize;
            this.avgProcessingTimeMs = avgProcessingTimeMs;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Stats: submitted=%d, completed=%d, failed=%d, retried=%d, " +
                "queueSize=%d, activeThreads=%d, executorQueue=%d, " +
                "avgProcessingTime=%dms",
                submitted, completed, failed, retried, queueSize, 
                activeThreads, executorQueueSize, avgProcessingTimeMs
            );
        }
    }
    
    // Main method
    public static void main(String[] args) throws InterruptedException {
        TaskQueueProcessor processor = new TaskQueueProcessor(100, 5, 10);
        
        System.out.println("==========================================");
        System.out.println("  Task Queue Processor Demo");
        System.out.println("==========================================\n");
        
        // Submit tasks với different priorities
        List<CompletableFuture<TaskResult>> futures = new ArrayList<>();
        
        for (int i = 1; i <= 20; i++) {
            final int taskNum = i;
            int priority = new Random().nextInt(10);
            
            Task task = new Task("task" + taskNum, "data" + taskNum, priority);
            futures.add(processor.submitTask(task));
        }
        
        System.out.println("Submitted " + futures.size() + " tasks\n");
        
        // Wait for completion
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // Print results
        System.out.println("\n=== Task Results ===");
        futures.stream()
            .limit(10)
            .forEach(f -> {
                TaskResult result = f.join();
                System.out.println("Task " + result.taskId + ": " + 
                    (result.isSuccess() ? "SUCCESS" : "FAILED"));
            });
        
        System.out.println("\n" + processor.getStatistics());
        
        processor.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

