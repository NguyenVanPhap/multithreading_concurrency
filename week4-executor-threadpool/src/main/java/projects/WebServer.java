package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project 1: Web Server Simulator
 * <p>
 * Mô phỏng web server với thread pool
 * - Handle multiple concurrent requests
 * - Thread pool management
 * - Request queuing
 * - Graceful shutdown
 * <p>
 * TODO Tasks:
 * 1. Implement WebServer class với ThreadPoolExecutor
 * 2. Handle HTTP requests (simulated)
 * 3. Request queue management
 * 4. Statistics tracking
 * 5. Graceful shutdown
 * 6. RejectedExecutionHandler
 */
public class WebServer {

    private final ThreadPoolExecutor executor;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger completedCount = new AtomicInteger(0);
    private final AtomicInteger rejectedCount = new AtomicInteger(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private volatile boolean running = false;

    public WebServer(int poolSize, int queueCapacity) {
        // TODO: Tạo ThreadPoolExecutor với custom configuration
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueCapacity); // TODO: Create ArrayBlockingQueue với queueCapacity

        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // TODO: Handle rejected requests
                // TODO: Increment rejectedCount
                // TODO: In ra message

                int count = rejectedCount.incrementAndGet();

                System.out.println(
                        "[REJECTED] Task bị từ chối | totalRejected=" + count +
                                " | activeThreads=" + executor.getActiveCount() +
                                " | queueSize=" + executor.getQueue().size()
                );

            }
        };

        this.executor = new ThreadPoolExecutor(poolSize / 2, poolSize, 60L, TimeUnit.SECONDS, workQueue, handler); // TODO: Create ThreadPoolExecutor
        // TODO: corePoolSize = poolSize / 2
        // TODO: maximumPoolSize = poolSize
        // TODO: keepAliveTime = 60 seconds
    }

    public void start() {
        running = true;
        System.out.println("WebServer started with pool size: " + executor.getMaximumPoolSize() +
                ", queue capacity: " + executor.getQueue().remainingCapacity());
    }

    public void handleRequest(Request request) {
        if (!running) {
            System.out.println("Server is shutting down, rejecting request");
            return;
        }

        requestCount.incrementAndGet();

        // TODO: Submit request handling task
        // TODO: Track start time
        // TODO: Process request
        // TODO: Calculate response time và update statistics
        // TODO: Handle exceptions

        try {
            executor.submit(() -> {
                long startTime = System.nanoTime();
                try {
                    processRequest(request);
                    System.out.printf(
                            "Request %d [%s] processed by %s%n",
                            request.getId(),
                            request.getType(),
                            Thread.currentThread().getName()
                    );
                } catch (Exception e) {
                    System.out.println("Request " + request.getId() + " failed");
                } finally {
                    long duration = System.nanoTime() - startTime;
                    totalResponseTime.addAndGet(duration);
                    completedCount.incrementAndGet();
                }
            });
        } catch (RejectedExecutionException e) {
            rejectedCount.incrementAndGet();
        }
    }

    private void processRequest(Request request) throws InterruptedException {
        // Simulate request processing với different processing times
        int processingTime;
        switch (request.getType()) {
            case STATIC:
                processingTime = 50;
                break;
            case DYNAMIC:
                processingTime = 200;
                break;
            case DATABASE:
                processingTime = 500;
                break;
            default:
                processingTime = 100;
        }
        Thread.sleep(processingTime);
    }

    public void shutdown() {
        System.out.println("\nShutting down server...");
        running = false;

        // Shutdown executor gracefully
        executor.shutdown();
        try {
            // Wait for tasks to complete với timeout 10 seconds
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Tasks not completed, forcing shutdown...");
                executor.shutdownNow();
                
                // Wait thêm 5 seconds
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Server shutdown complete");
    }

    public ServerStats getStats() {
        return new ServerStats(
                requestCount.get(),
                completedCount.get(),
                rejectedCount.get(),
                totalResponseTime.get(),
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount()
        );
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Web Server Simulator");
        System.out.println("==========================================\n");

        // TODO: Create web server
        WebServer server = new WebServer(10, 50);
        server.start();

        // TODO: Simulate incoming requests
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Request.Type type = Request.Type.values()[random.nextInt(Request.Type.values().length)];
            Request request = new Request(i, type);
            server.handleRequest(request);

            // TODO: Random delay between requests
            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // TODO: Wait for requests to process
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Print statistics
        ServerStats stats = server.getStats();
        System.out.println("\n" + stats);

        // TODO: Shutdown server
        server.shutdown();
    }

    // Request class
    static class Request {
        private final int id;
        private final Type type;

        public Request(int id, Type type) {
            this.id = id;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public Type getType() {
            return type;
        }

        enum Type {
            STATIC,    // Static files (fast)
            DYNAMIC,   // Dynamic content (medium)
            DATABASE   // Database queries (slow)
        }


    }

    // Statistics class
    static class ServerStats {
        private final int totalRequests;
        private final int completedRequests;
        private final int rejectedRequests;
        private final long totalResponseTime;
        private final int activeThreads;
        private final int queuedRequests;
        private final long completedTasks;

        public ServerStats(int totalRequests, int completedRequests, int rejectedRequests,
                           long totalResponseTime, int activeThreads, int queuedRequests,
                           long completedTasks) {
            this.totalRequests = totalRequests;
            this.completedRequests = completedRequests;
            this.rejectedRequests = rejectedRequests;
            this.totalResponseTime = totalResponseTime;
            this.activeThreads = activeThreads;
            this.queuedRequests = queuedRequests;
            this.completedTasks = completedTasks;
        }

        @Override
        public String toString() {
            double avgResponseTime = completedRequests > 0 ?
                    (double) totalResponseTime / completedRequests : 0;

            return "Server Statistics:\n" +
                    "  Total Requests: " + totalRequests + "\n" +
                    "  Completed: " + completedRequests + "\n" +
                    "  Rejected: " + rejectedRequests + "\n" +
                    "  Average Response Time: " + String.format("%.2f", avgResponseTime) + "ms\n" +
                    "  Active Threads: " + activeThreads + "\n" +
                    "  Queued Requests: " + queuedRequests + "\n" +
                    "  Completed Tasks: " + completedTasks;
        }
    }
}

