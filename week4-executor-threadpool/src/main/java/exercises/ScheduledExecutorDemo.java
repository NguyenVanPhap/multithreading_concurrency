package exercises;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3: ScheduledExecutor Demo
 * <p>
 * TODO Tasks:
 * 1. Schedule tasks với delay
 * 2. Schedule tasks với fixed rate
 * 3. Schedule tasks với fixed delay
 * 4. Cancel scheduled tasks
 * 5. Handle exceptions trong scheduled tasks
 */
public class ScheduledExecutorDemo {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ScheduledExecutor Demo");
        System.out.println("==========================================\n");

        // TODO: Test schedule với delay
        System.out.println("Test 1: Schedule với Delay");
        testScheduleWithDelay();

        // TODO: Test scheduleAtFixedRate
        System.out.println("\nTest 2: ScheduleAtFixedRate");
        testScheduleAtFixedRate();

        // TODO: Test scheduleWithFixedDelay
        System.out.println("\nTest 3: ScheduleWithFixedDelay");
        testScheduleWithFixedDelay();

        // TODO: Test cancel scheduled tasks
        System.out.println("\nTest 4: Cancel Scheduled Tasks");
        testCancelScheduledTasks();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    /**
     * TODO: Test schedule với delay
     * Execute task sau một khoảng thời gian
     */
    private static void testScheduleWithDelay() {
        // Tạo ScheduledExecutorService với 3 threads
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        System.out.println("Scheduling tasks with delays...");

        // Schedule Runnable với delay 1 second
        ScheduledFuture<?> future1 = scheduler.schedule(() -> {
            System.out.println("Runnable Task 1 executed after 1 second");
        }, 1, TimeUnit.SECONDS);

        // Schedule Runnable với delay 2 seconds
        ScheduledFuture<?> future2 = scheduler.schedule(() -> {
            System.out.println("Runnable Task 2 executed after 2 seconds");
        }, 2, TimeUnit.SECONDS);

        // Schedule Callable với delay 500ms
        ScheduledFuture<String> future3 = scheduler.schedule(() -> {
            System.out.println("Callable Task executed after 500ms");
            return "Callable Result";
        }, 500, TimeUnit.MILLISECONDS);

        try {
            // Với Runnable → get() trả về null
            future1.get();
            future2.get();

            // Với Callable → get() trả về kết quả
            String result = future3.get();
            System.out.println("Result from callable: " + result);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Shutdown scheduler
        scheduler.shutdown();
    }


    /**
     * TODO: Test scheduleAtFixedRate
     * Execute task với fixed rate (không phụ thuộc vào execution time)
     */
    private static void testScheduleAtFixedRate() {
        // Create ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        AtomicInteger counter = new AtomicInteger(0);

        System.out.println("Starting fixed rate task (every 500ms)...");

        // Schedule task với fixed rate (0 initial delay, 500ms period)
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            int count = counter.incrementAndGet();
            long now = System.currentTimeMillis();

            System.out.println(
                    "Execution #" + count + " at " + now
            );

            // Simulate work (200ms)
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }, 0, 500, TimeUnit.MILLISECONDS);

        try {
            // Run for 3 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Cancel task
        future.cancel(true);

        // In ra total executions
        System.out.println("Total executions: " + counter.get());

        // Shutdown scheduler
        scheduler.shutdown();
    }


    /**
     * TODO: Test scheduleWithFixedDelay
     * Execute task với fixed delay (delay sau khi task hoàn thành)
     */
    private static void testScheduleWithFixedDelay() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // TODO: Create ScheduledExecutorService

        AtomicInteger counter = new AtomicInteger(0);

        System.out.println("Starting fixed delay task (500ms delay after completion)...");

        // TODO: Schedule task với fixed delay (0 initial delay, 500ms delay)
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(() -> {
            int count = counter.incrementAndGet();
            long start = System.currentTimeMillis();

            System.out.println(
                    "Execution #" + count + " at " + start
            );

            // Simulate work (200ms)
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            long end = System.currentTimeMillis();
            System.out.println(
                    "Task end   #" + count + " at " + end +
                            " (duration=" + (end - start) + "ms)"
            );

        }, 0, 500, TimeUnit.MILLISECONDS); // TODO: Use scheduleWithFixedDelay()


        // TODO: Task nên track start/end time và in ra
        // TODO: Simulate work (sleep 300ms)

        // TODO: Run for 3 seconds
        // TODO: Cancel task
        // TODO: In ra total executions
        // TODO: Shutdown scheduler
        try {
            // Run for 3 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Cancel task
        future.cancel(true);

        // In ra total executions
        System.out.println("Total executions: " + counter.get());

        // Shutdown scheduler
        scheduler.shutdown();
    }

    /**
     * TODO: Test cancel scheduled tasks
     * Cancel tasks và handle cancellation
     */
    /**
     * Test cancel scheduled tasks
     * Cancel tasks và handle cancellation
     */
    private static void testCancelScheduledTasks() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        List<ScheduledFuture<?>> futures = new ArrayList<>();

        System.out.println("Scheduling tasks...");

        for (int i = 0; i < 5; i++) {
            final int taskId = i;

            ScheduledFuture<?> future = scheduler.schedule(
                    () -> {
                        System.out.println("Task " + taskId + " executed");
                    },
                    (i + 1) * 500,     // delay: 500ms, 1000ms, ...
                    TimeUnit.MILLISECONDS
            );

            futures.add(future);
        }

        // Cancel tasks 1, 3, 4
        System.out.println("Cancelling tasks 1, 3, 4...");
        futures.get(1).cancel(false);
        futures.get(3).cancel(false);
        futures.get(4).cancel(false);

        // Check cancellation status
        for (int i = 0; i < futures.size(); i++) {
            ScheduledFuture<?> f = futures.get(i);
            System.out.println(
                    "Task " + i +
                            " | cancelled=" + f.isCancelled() +
                            " | done=" + f.isDone()
            );
        }

        try {
            // Wait for remaining tasks
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final status:");
        for (int i = 0; i < futures.size(); i++) {
            ScheduledFuture<?> f = futures.get(i);
            System.out.println(
                    "Task " + i +
                            " | cancelled=" + f.isCancelled() +
                            " | done=" + f.isDone()
            );
        }

        scheduler.shutdown();
    }

}

