package exercises;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Exercise 1: ConcurrentMap Demo
 * <p>
 * TODO Tasks:
 * 1. Sử dụng ConcurrentHashMap
 * 2. So sánh với synchronized HashMap
 * 3. Atomic operations (putIfAbsent, replace, compute)
 * 4. Performance comparison
 */
public class ConcurrentMapDemo {

    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 1000;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ConcurrentMap Demo");
        System.out.println("==========================================\n");

        System.out.println("Test 1: ConcurrentHashMap");
        testConcurrentHashMap();

        System.out.println("\nTest 2: Synchronized HashMap");
        testSynchronizedHashMap();

        System.out.println("\nTest 3: Atomic Operations");
        testAtomicOperations();

        System.out.println("\nTest 4: Performance Comparison");
        testPerformanceComparison();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    private static void testConcurrentHashMap() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        AtomicInteger counter = new AtomicInteger(0);

        int sampleEvery = Math.max(OPERATIONS_PER_THREAD / 5, 1);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    String key = "key-" + (j % 20); // cố tình trùng key để test concurrent updates
                    int value = counter.incrementAndGet();

                    map.put(key, value);
                    map.get(key);

                    if (value % 7 == 0) {
                        map.remove(key);
                    }

                    if (j % sampleEvery == 0) {
                        System.out.println(Thread.currentThread().getName()
                            + " -> sample op #" + j + " | size=" + map.size());
                    }
                }
            });
        }

        shutdownAndAwait(executor);

        System.out.println("Final size of ConcurrentHashMap: " + map.size());
        for (int i = 0; i < 5; i++) {
            String key = "key-" + i;
            System.out.println(key + " => " + map.get(key));
        }
    }

    private static void testSynchronizedHashMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    String key = "key-" + (j % 20);
                    int value = counter.incrementAndGet();

                    map.put(key, value);
                    map.get(key);

                    if (value % 9 == 0) {
                        map.remove(key);
                    }
                }
            });
        }

        shutdownAndAwait(executor);

        System.out.println("Final size of synchronized HashMap: " + map.size());
        for (int i = 0; i < 5; i++) {
            String key = "key-" + i;
            System.out.println(key + " => " + map.get(key));
        }
    }

    private static void testAtomicOperations() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        System.out.println("putIfAbsent:");
        map.putIfAbsent("user1", 1);
        map.putIfAbsent("user1", 99); // bỏ qua vì đã tồn tại
        System.out.println("user1 => " + map.get("user1"));

        System.out.println("\nreplace (oldValue, newValue):");
        boolean replaced = map.replace("user1", 1, 2);
        System.out.println("replaced? " + replaced + ", user1 => " + map.get("user1"));

        System.out.println("\ncompute:");
        map.compute("user1", (k, v) -> v == null ? 0 : v + 10);
        System.out.println("user1 => " + map.get("user1"));

        System.out.println("\ncomputeIfAbsent:");
        map.computeIfAbsent("user2", k -> 5);
        map.computeIfAbsent("user2", k -> 999); // bỏ qua vì đã có
        System.out.println("user2 => " + map.get("user2"));

        System.out.println("\ncomputeIfPresent:");
        map.computeIfPresent("user2", (k, v) -> v * 2);
        map.computeIfPresent("missing", (k, v) -> v * 2); // không chạy
        System.out.println("user2 => " + map.get("user2"));

        System.out.println("\nmerge:");
        map.merge("user3", 1, Integer::sum); // thêm mới
        map.merge("user3", 4, Integer::sum); // cộng dồn
        System.out.println("user3 => " + map.get("user3"));
    }

    private static void testPerformanceComparison() {
        long concurrentTime = measurePerformance(() -> new ConcurrentHashMap<>());
        long synchronizedTime = measurePerformance(() -> Collections.synchronizedMap(new HashMap<>()));

        System.out.println("ConcurrentHashMap time: " + concurrentTime + " ms");
        System.out.println("Synchronized HashMap time: " + synchronizedTime + " ms");

        if (concurrentTime == synchronizedTime) {
            System.out.println("Hiệu năng ngang nhau (ít khi xảy ra).");
        } else if (concurrentTime < synchronizedTime) {
            System.out.println("ConcurrentHashMap nhanh hơn ~" + (synchronizedTime - concurrentTime) + " ms");
        } else {
            System.out.println("Synchronized HashMap nhanh hơn ~" + (concurrentTime - synchronizedTime) + " ms");
        }
    }

    private static long measurePerformance(Supplier<Map<String, Integer>> mapSupplier) {
        Map<String, Integer> map = mapSupplier.get();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        AtomicInteger counter = new AtomicInteger(0);

        long start = System.nanoTime();
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    String key = "perf-" + (j % 100);
                    int value = counter.incrementAndGet();

                    map.put(key, value);
                    map.get(key);
                    if ((value & 15) == 0) {
                        map.remove(key);
                    }
                }
            });
        }
        shutdownAndAwait(executor);

        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }

    private static void shutdownAndAwait(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

