package exercises;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 1: Executor Demo
 * <p>
 * TODO Tasks:
 * 1. Sử dụng Executor interface
 * 2. So sánh Executor vs Thread.start()
 * 3. Implement ExecutorService
 * 4. Submit tasks và get results với Future
 * 5. Handle exceptions
 */
public class ExecutorDemo {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Executor Framework Demo");
        System.out.println("==========================================\n");

        // TODO: Test basic Executor
        System.out.println("Test 1: Basic Executor");
        testBasicExecutor();

        // TODO: Test ExecutorService với Runnable
        System.out.println("\nTest 2: ExecutorService với Runnable");
        testExecutorServiceRunnable();

        // TODO: Test ExecutorService với Callable và Future
        System.out.println("\nTest 3: ExecutorService với Callable và Future");
        testExecutorServiceCallable();

        // TODO: Test invokeAll
        System.out.println("\nTest 4: invokeAll");
        testInvokeAll();

        // TODO: Test invokeAny
        System.out.println("\nTest 5: invokeAny");
        testInvokeAny();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    /**
     * TODO: Test basic Executor interface
     * So sánh với Thread.start()
     */
    private static void testBasicExecutor() {
        // TODO: Tạo Executor với FixedThreadPool (3 threads)
        Executor executor = Executors.newFixedThreadPool(3); // TODO: Use Executors.newFixedThreadPool()

        System.out.println("Using Executor:");
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // TODO: Execute task với executor.execute()
            // TODO: Task nên in ra taskId và thread name
            // TODO: Thêm sleep để simulate work
            executor.execute(() -> {
                System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

        ExecutorService executorService = (ExecutorService) executor;

        // TODO: Shutdown executor (cast to ExecutorService)
        // TODO: Wait for termination với timeout
        // TODO: Nếu không terminate, force shutdown

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Timeout reached, forcing shutdown");
                executorService.shutdownNow();

                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * TODO: Test ExecutorService với Runnable tasks
     */
    private static void testExecutorServiceRunnable() {
        // TODO: Tạo ExecutorService với FixedThreadPool (3 threads)
        ExecutorService executor = Executors.newFixedThreadPool(3); // TODO: Use Executors.newFixedThreadPool()

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final int taskId = i;

            // TODO: Submit Runnable task và lưu Future vào list
            // TODO: Task nên in ra "Task X running" và "Task X completed"
            // TODO: Thêm sleep để simulate work
            futures.add(executor.submit(() -> {
                System.out.println("Task " + taskId + " running on thread" + Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Task " + taskId + " interrupted");
                }
                System.out.println("Task " + taskId + " completed on thread" + Thread.currentThread().getName());
            }));
        }

        // TODO: Wait for all tasks to complete
        // TODO: Dùng future.get() để wait
        // TODO: Handle InterruptedException và ExecutionException
        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted");

        } catch (ExecutionException e) {
            System.out.println("Execution exception");
        }
        System.out.println("All tasks completed");

        // TODO: Shutdown executor
        executor.shutdown(); // không nhận task mới

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // ép dừng các task đang chạy
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // best practice
        }
    }

    /**
     * TODO: Test ExecutorService với Callable và Future
     * Callable có thể return value và throw exception
     */
    private static void testExecutorServiceCallable() {
        // TODO: Tạo ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(3); // TODO: Use Executors.newFixedThreadPool()

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // TODO: Submit Callable task (return String)
            // TODO: Task nên sleep một chút và return "Result from task X"
            // TODO: Lưu Future vào list
            Future<String> future = executor.submit(() -> {
                try {
                    Thread.sleep(1000); // sleep 1s cho dễ thấy async
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Result from task " + taskId;
            });


            futures.add(future);

        }

        // TODO: Get results từ Future
        // TODO: Dùng future.get(timeout) với timeout 1 second
        // TODO: Handle InterruptedException, ExecutionException, TimeoutException
        // TODO: In ra kết quả

        for (Future<String> future : futures) {

            try {
                String result = future.get(1, TimeUnit.SECONDS);
                System.out.println("Result:" + result);

            } catch (TimeoutException e) {
                System.out.println("Task time out");
                future.cancel(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Task interrupted");
            } catch (ExecutionException e) {
                System.out.println("Execution exception");
            }

        }

        // TODO: Shutdown executor
        executor.shutdown(); // không nhận task mới

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // ép dừng các task đang chạy
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // best practice
        }
    }

    /**
     * TODO: Test invokeAll - submit multiple Callables và wait for all
     */
    private static void testInvokeAll() {
        // 1. Create ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = new ArrayList<>();

        // 2. Create 5 Callable tasks
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            tasks.add(() -> {
                Thread.sleep(200);
                return "Task " + taskId + " result";
            });
        }

        try {
            // 3. Invoke all tasks (blocking)
            List<Future<String>> futures = executor.invokeAll(tasks);

            // 4. Process results
            for (Future<String> future : futures) {
                try {
                    System.out.println(future.get());
                } catch (ExecutionException e) {
                    System.err.println("Task failed: " + e.getCause());
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted");

        } finally {
            // 5. Shutdown executor
            executor.shutdown();
        }
    }

    /**
     * Test invokeAny - submit multiple Callables và get first result
     */
    private static void testInvokeAny() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = new ArrayList<>();

        // Tạo 5 tasks với thời gian chạy khác nhau
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            final int delay = (i + 1) * 100; // 100ms, 200ms, ..., 500ms

            tasks.add(() -> {
                Thread.sleep(delay);
                return "Fastest task: " + taskId + " (delay " + delay + "ms)";
            });
        }

        try {
            // invokeAny() sẽ block cho đến khi có 1 task hoàn thành thành công
            String result = executor.invokeAny(tasks);
            System.out.println("First completed: " + result);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        } catch (ExecutionException e) {
            System.out.println("All tasks failed: " + e.getCause());
        } finally {
            executor.shutdown();
        }
    }
}

