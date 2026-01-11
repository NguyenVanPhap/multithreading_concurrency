package projects;

import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project 2: Task Processor
 * <p>
 * Batch processing với ExecutorService
 * - Process multiple tasks concurrently
 * - Future và Callable
 * - CompletionService
 * - Error handling
 * <p>
 * TODO Tasks:
 * 1. Implement TaskProcessor với ExecutorService
 * 2. Submit Callable tasks
 * 3. Collect results với Future
 * 4. Use CompletionService
 * 5. Handle exceptions
 * 6. Progress tracking
 */
public class TaskProcessor {

    private final ExecutorService executor;
    private final AtomicInteger completedTasks = new AtomicInteger(0);
    private final AtomicInteger failedTasks = new AtomicInteger(0);

    public TaskProcessor(int poolSize) {
        // TODO: Tạo ExecutorService với FixedThreadPool
        this.executor = Executors.newFixedThreadPool(poolSize); // TODO: Use Executors.newFixedThreadPool(poolSize)
    }

    /**
     * TODO: Submit tasks và return Futures
     */
    public <T> List<Future<T>> submitTasks(List<Callable<T>> tasks) {
        List<Future<T>> futures = new ArrayList<>();

        // TODO: Submit all tasks
        // TODO: Lưu Future vào list
        // TODO: Return list of futures
        for (Callable<T> task : tasks) {
            futures.add(executor.submit(task));
        }


        return futures;
    }

    /**
     * TODO: Process tasks và collect results
     * Wait for all tasks to complete
     */
    public <T> List<T> processTasks(List<Callable<T>> tasks) throws InterruptedException {
        List<T> results = new ArrayList<>();

        // TODO: Submit all tasks
        List<Future<T>> futures = new ArrayList<>(); // TODO: Submit tasks
        for (Callable<T> task : tasks) {
            futures.add(executor.submit(task));
        }

        // TODO: Collect results
        // TODO: Dùng future.get() để lấy kết quả
        // TODO: Handle ExecutionException
        // TODO: Increment completedTasks hoặc failedTasks
        // TODO: Add result hoặc null vào results

        try {
            for (Future<T> future : futures) {
                try {
                    T result = future.get();
                    results.add(result);
                    completedTasks.incrementAndGet();
                } catch (ExecutionException e) {
                    failedTasks.incrementAndGet();
                    results.add(null);
                    System.err.println("Task failed: " + e.getCause().getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return results;
    }

    /**
     * TODO: Process tasks với CompletionService
     * Get results as they complete (not in submission order)
     */
    public <T> List<T> processTasksWithCompletionService(List<Callable<T>> tasks) throws InterruptedException {
        List<T> results = new ArrayList<>();

        // TODO: Tạo CompletionService với ExecutorCompletionService
        CompletionService<T> completionService = new ExecutorCompletionService<>(executor); // TODO: Create

        // TODO: Submit all tasks
        int numTasks = tasks.size();
        // TODO: Submit từng task

        for (int i = 0; i < numTasks; i++) {
            completionService.submit(tasks.get(i));
        }

        // TODO: Collect results as they complete
        // TODO: Dùng completionService.take() để lấy completed task
        // TODO: Dùng future.get() để lấy result
        // TODO: Handle ExecutionException
        // TODO: In ra progress
        int completed = 0;


        for (int i = 0; i < numTasks; i++) {
            try {
                Future<T> future = completionService.take();
                try {
                    T result = future.get();
                    results.add(result);
                    completed++;
                    completedTasks.incrementAndGet();
                    System.out.println("✅ Completed " + completed + "/" + tasks.size()
                            + " → Result: " + result);
                } catch (ExecutionException e) {
                    failedTasks.incrementAndGet();
                    results.add(null);
                    System.err.println("❌ Task failed: " + e.getCause().getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Interrupted while waiting for task completion");
                break;
            }
        }


        return results;
    }

    /**
     * TODO: Process tasks với timeout
     */
    public <T> List<T> processTasksWithTimeout(List<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        List<T> results = new ArrayList<>();

        // TODO: Submit all tasks
        List<Future<T>> futures = new ArrayList<>(); // TODO: Submit tasks
        for (Callable<T> task : tasks) {
            futures.add(executor.submit(task));
        }

        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);

        // TODO: Collect results với timeout
        // TODO: Loop qua futures
        // TODO: Calculate remaining time
        // TODO: Nếu timeout -> cancel future và add null
        // TODO: Dùng future.get(remainingTime) với timeout
        // TODO: Handle ExecutionException và TimeoutException
        // TODO: Update statistics

        for (Future<T> future : futures) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining < 0) {
                future.cancel(true);
                results.add(null);
            }
            try {
                T result = future.get(remaining, TimeUnit.MILLISECONDS);
                results.add(result);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                failedTasks.incrementAndGet();
            } catch (TimeoutException e) {
                future.cancel(true);
                results.add(null);
            }
        }


        return results;
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getCompletedTasks() {
        return completedTasks.get();
    }

    public int getFailedTasks() {
        return failedTasks.get();
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Task Processor Demo");
        System.out.println("==========================================\n");

        // TODO: Create task processor
        TaskProcessor processor = new TaskProcessor(5);

        // TODO: Create sample tasks
        List<Callable<String>> tasks = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            final int duration = 100 + random.nextInt(400); // 100-500ms

            // TODO: Create Callable tasks
            tasks.add(() -> {
                Thread.sleep(duration);

                // TODO: Simulate occasional failures
                if (random.nextInt(10) == 0) {
                    throw new RuntimeException("Task " + taskId + " failed");
                }

                return "Result from task " + taskId;
            });
        }

        try {
            // TODO: Test 1: Process với Future
            System.out.println("Test 1: Processing with Futures");
            long startTime = System.currentTimeMillis();
            List<String> results1 = processor.processTasks(tasks);
            long endTime = System.currentTimeMillis();
            System.out.println("Completed in " + (endTime - startTime) + "ms");
            System.out.println("Results: " + results1.size());

            // Reset counters
            processor.completedTasks.set(0);
            processor.failedTasks.set(0);

            // TODO: Test 2: Process với CompletionService
            System.out.println("\nTest 2: Processing with CompletionService");
            startTime = System.currentTimeMillis();
            List<String> results2 = processor.processTasksWithCompletionService(tasks);
            endTime = System.currentTimeMillis();
            System.out.println("Completed in " + (endTime - startTime) + "ms");
            System.out.println("Results: " + results2.size());

            // Reset counters
            processor.completedTasks.set(0);
            processor.failedTasks.set(0);

            // TODO: Test 3: Process với timeout
            System.out.println("\nTest 3: Processing with Timeout");
            startTime = System.currentTimeMillis();
            List<String> results3 = processor.processTasksWithTimeout(tasks, 2, TimeUnit.SECONDS);
            endTime = System.currentTimeMillis();
            System.out.println("Completed in " + (endTime - startTime) + "ms");
            System.out.println("Results: " + results3.size());

            System.out.println("\nFinal Statistics:");
            System.out.println("  Completed: " + processor.getCompletedTasks());
            System.out.println("  Failed: " + processor.getFailedTasks());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Shutdown processor
        processor.shutdown();
    }
}

