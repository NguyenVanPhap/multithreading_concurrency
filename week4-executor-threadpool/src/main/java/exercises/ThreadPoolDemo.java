package exercises;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: ThreadPool Demo
 * <p>
 * TODO Tasks:
 * 1. Tạo FixedThreadPool
 * 2. Tạo CachedThreadPool
 * 3. Tạo SingleThreadExecutor
 * 4. So sánh performance và behavior
 * 5. Custom ThreadPoolExecutor
 * 6. RejectedExecutionHandler
 */
public class ThreadPoolDemo {

    private static final int NUM_TASKS = 20;
    private static final int TASK_DURATION_MS = 100;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Thread Pool Types Demo");
        System.out.println("==========================================\n");

        // TODO: Test FixedThreadPool
        System.out.println("Test 1: FixedThreadPool");
        testFixedThreadPool();

        // TODO: Test CachedThreadPool
        System.out.println("\nTest 2: CachedThreadPool");
        testCachedThreadPool();

        // TODO: Test SingleThreadExecutor
        System.out.println("\nTest 3: SingleThreadExecutor");
        testSingleThreadExecutor();

        // TODO: Test Custom ThreadPoolExecutor
        System.out.println("\nTest 4: Custom ThreadPoolExecutor");
        testCustomThreadPool();

        // TODO: Test RejectedExecutionHandler
        System.out.println("\nTest 5: RejectedExecutionHandler");
        testRejectedExecutionHandler();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    /**
     * TODO: Test FixedThreadPool
     * Fixed number of threads, unbounded queue
     */
    private static void testFixedThreadPool() {
        // TODO: Tạo FixedThreadPool với 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        // TODO: Submit NUM_TASKS tasks
        // TODO: Mỗi task sleep TASK_DURATION_MS và return taskId
        // TODO: In ra taskId và thread name
        // TODO: Lưu Future vào list

        for (int i = 0; i < NUM_TASKS; i++) {

            int taskId = i;
            futures.add(executor.submit(() -> {
                System.out.println("Task " + taskId + "is running");

                try {
                    Thread.sleep(TASK_DURATION_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return taskId;
            }));

        }


        // TODO: Wait for all tasks với future.get()
        try {
            for (Future<Integer> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        }


        // TODO: Handle exceptions

        long endTime = System.currentTimeMillis();
        System.out.println("FixedThreadPool completed in " + (endTime - startTime) + "ms");

        // TODO: Shutdown executor
        executor.shutdown();
    }

    /**
     * TODO: Test CachedThreadPool
     * Creates new threads as needed, reuses existing threads
     */
    private static void testCachedThreadPool() {
        // TODO: Tạo CachedThreadPool
        ExecutorService executor = Executors.newCachedThreadPool(); // TODO: Use Executors.newCachedThreadPool()

        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        // TODO: Submit NUM_TASKS tasks (tương tự testFixedThreadPool)
        // TODO: Wait for all tasks
        // TODO: Measure và in ra thời gian
        for (int i = 0; i < NUM_TASKS; i++) {
            int taskId = i;
            futures.add(executor.submit(() -> {
                System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(TASK_DURATION_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return taskId;
            }));
        }

        try {
            for (Future<Integer> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("CachedThreadPool completed in " + (endTime - startTime) + "ms");

        // TODO: Shutdown executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * TODO: Test SingleThreadExecutor
     * Single thread, tasks execute sequentially
     */
    private static void testSingleThreadExecutor() {
        // TODO: Tạo SingleThreadExecutor
        ExecutorService executor = Executors.newSingleThreadExecutor(); // TODO: Use Executors.newSingleThreadExecutor()

        long startTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        // TODO: Submit NUM_TASKS tasks (tương tự các test trước)
        // TODO: Quan sát tasks chạy tuần tự (1 thread)
        // TODO: Wait for all tasks
        // TODO: Measure và in ra thời gian

        for (int i = 0; i < NUM_TASKS; i++) {
            int taskId = i;
            futures.add(executor.submit(() -> {
                System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(TASK_DURATION_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return taskId;
            }));
        }
        try {
            for (Future<Integer> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("SingleThreadExecutor completed in " + (endTime - startTime) + "ms");

        // TODO: Shutdown executor
        executor.shutdown();
    }

    /**
     * TODO: Test Custom ThreadPoolExecutor
     * Custom core pool size, max pool size, queue, etc.
     */
    private static void testCustomThreadPool() {
        // TODO: Tạo custom ThreadPoolExecutor
        int corePoolSize = 2;
        int maximumPoolSize = 5;
        long keepAliveTime = 60L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10); // TODO: Create LinkedBlockingQueue với capacity 10
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                // TODO: Create custom thread với name "CustomThread-X"
                // TODO: Set daemon = false
                Thread thread = new Thread(r, "CustomThread-" + threadNumber.getAndIncrement());
                thread.setDaemon(false);
                return thread;
            }
        };

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, threadFactory, handler); // TODO: Create ThreadPoolExecutor với các params trên

        // TODO: In ra thông tin pool (core size, max size, queue capacity)
        System.out.println("=== POOL CONFIG ===");
        System.out.println("Core pool size     : " + executor.getCorePoolSize());
        System.out.println("Max pool size      : " + executor.getMaximumPoolSize());
        System.out.println("Queue capacity     : " + executor.getQueue().remainingCapacity() + " / " + (executor.getQueue().size() + executor.getQueue().remainingCapacity()));
        System.out.println();


        // TODO: Submit NUM_TASKS tasks
        // TODO: Monitor pool (active count, queue size, completed count)
        // TODO: Shutdown executor
        // 2️⃣ Submit tasks
        for (int i = 0; i < NUM_TASKS; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " started on " + threadName);
                try {
                    Thread.sleep(1000); // giả lập task nặng
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " finished on " + threadName);
            });
        }

        // 3️⃣ Monitor pool
        for (int i = 0; i < 5; i++) {
            System.out.println("=== POOL STATUS ===");
            System.out.println("Active threads     : " + executor.getActiveCount());
            System.out.println("Pool size          : " + executor.getPoolSize());
            System.out.println("Queue size         : " + executor.getQueue().size());
            System.out.println("Completed tasks    : " + executor.getCompletedTaskCount());
            System.out.println();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 4️⃣ Shutdown executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

    /**
     * TODO: Test RejectedExecutionHandler
     * Handle rejected tasks when pool is full
     */
    private static void testRejectedExecutionHandler() {
        // TODO: Tạo ThreadPoolExecutor với bounded queue
        int corePoolSize = 2;
        int maximumPoolSize = 3;
        long keepAliveTime = 30L;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5);

        // TODO: Custom RejectedExecutionHandler
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // TODO: Handle rejected task
                // TODO: In ra thông tin (task, pool size, active count, queue size)
                ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;

                System.err.println("🚨 TASK REJECTED");
                System.err.println("Task              : " + r);
                System.err.println("Pool size         : " + pool.getPoolSize());
                System.err.println("Active threads    : " + pool.getActiveCount());
                System.err.println("Queue size        : " + pool.getQueue().size());
                System.err.println("Completed tasks   : " + pool.getCompletedTaskCount());
                System.err.println("----------------------------------");

            }
        };

        ThreadFactory threadFactory = r -> new Thread(r, "reject-demo-thread");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, threadFactory, handler); // TODO: Create ThreadPoolExecutor với các params trên


        // TODO: Submit more tasks than pool can handle (NUM_TASKS * 2)
        // TODO: In ra số tasks và capacity
        // TODO: Wait một chút để quan sát
        // TODO: Shutdown executor
        // 3️⃣ Submit nhiều task hơn khả năng xử lý
        int NUM_TASKS = (corePoolSize + workQueue.remainingCapacity()) * 2;

        System.out.println("Submitting " + NUM_TASKS + " tasks");
        System.out.println("Pool capacity = core(" + corePoolSize +
                ") + queue(" + workQueue.remainingCapacity() +
                ") + extra(" + (maximumPoolSize - corePoolSize) + ")");
        System.out.println();

        for (int i = 0; i < NUM_TASKS; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId +
                        " running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000); // giữ thread lâu để gây overload
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        try {


            // 4️⃣ Wait để quan sát reject
            Thread.sleep(5000);

            // 5️⃣ Shutdown executor
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

