package exercises;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 3: CopyOnWrite Demo
 * <p>
 * TODO Tasks:
 * 1. CopyOnWriteArrayList
 * 2. CopyOnWriteArraySet
 * 3. Use cases và trade-offs
 * 4. Performance comparison với synchronized collections
 */
public class CopyOnWriteDemo {

    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 1000;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  CopyOnWrite Demo");
        System.out.println("==========================================\n");

        // TODO: Test CopyOnWriteArrayList
        System.out.println("Test 1: CopyOnWriteArrayList");
        testCopyOnWriteArrayList();

        // TODO: Test CopyOnWriteArraySet
        System.out.println("\nTest 2: CopyOnWriteArraySet");
        testCopyOnWriteArraySet();

        // TODO: Test read-heavy scenario
        System.out.println("\nTest 3: Read-Heavy Scenario");
        testReadHeavy();

        // TODO: Test write-heavy scenario
        System.out.println("\nTest 4: Write-Heavy Scenario");
        testWriteHeavy();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    /**
     * Test CopyOnWriteArrayList: nhiều thread ghi + vài thread đọc snapshot.
     */
    private static void testCopyOnWriteArrayList() {
        // Tạo CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Nhiều thread ghi vào list
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    list.add(Thread.currentThread().getName() + " - item " + j);
                }
            });
        }

        // Reader thread 1
        executor.submit(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " READ1, size=" + list.size());
                System.out.println("Snapshot READ1: " + list);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Reader thread 2
        executor.submit(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " READ2, size=" + list.size());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Force shutdown!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("FINAL SIZE = " + list.size());
    }

    /**
     * Test CopyOnWriteArraySet: nhiều thread add trùng lặp, set loại bỏ phần tử trùng.
     */
    private static void testCopyOnWriteArraySet() {
        // Tạo CopyOnWriteArraySet
        CopyOnWriteArraySet<Integer> set = new CopyOnWriteArraySet<>();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Nhiều thread cùng add giá trị (có trùng lặp)
        for (int i = 0; i < NUM_THREADS; i++) {
            final int base = i;
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    set.add(base * 1000 + (j % 100)); // cố ý trùng lặp trong 0..99
                    set.contains(base * 1000);        // chỉ để minh hoạ đọc
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Force shutdown (set)!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("CopyOnWriteArraySet SIZE = " + set.size());
        System.out.println("Some elements: " + set.stream().limit(20).toList());
    }

    /**
     * Test read-heavy scenario (CopyOnWrite shines)
     */
    private static void testReadHeavy() {
        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());

        // Thêm dữ liệu ban đầu
        for (int i = 0; i < 10_000; i++) {
            cowList.add(i);
            syncList.add(i);
        }

        int readerThreads = NUM_THREADS;
        int writerThreads = 2;

        // --- CopyOnWrite ---
        ExecutorService cowExec = Executors.newFixedThreadPool(readerThreads + writerThreads);
        long startCow = System.nanoTime();

        for (int i = 0; i < readerThreads; i++) {
            cowExec.submit(() -> {
                // Đọc toàn bộ list nhiều lần để tạo read-load
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    for (Integer v : cowList) {
                        // no-op
                    }
                }
            });
        }

        for (int i = 0; i < writerThreads; i++) {
            cowExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    cowList.add(k);
                }
            });
        }

        cowExec.shutdown();
        try {
            cowExec.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            cowExec.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long durationCowMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startCow);

        // --- Synchronized list ---
        ExecutorService syncExec = Executors.newFixedThreadPool(readerThreads + writerThreads);
        long startSync = System.nanoTime();

        for (int i = 0; i < readerThreads; i++) {
            syncExec.submit(() -> {
                // Đọc toàn bộ list nhiều lần để tạo read-load
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    synchronized (syncList) {
                        for (Integer v : syncList) {
                            // no-op
                        }
                    }
                }
            });
        }

        for (int i = 0; i < writerThreads; i++) {
            syncExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    synchronized (syncList) {
                        syncList.add(k);
                    }
                }
            });
        }

        syncExec.shutdown();
        try {
            syncExec.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            syncExec.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long durationSyncMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startSync);

        System.out.println("Read-heavy (CopyOnWrite)   = " + durationCowMs + " ms");
        System.out.println("Read-heavy (Synchronized)  = " + durationSyncMs + " ms");
    }

    /**
     * Test write-heavy scenario (CopyOnWrite expensive)
     */
    private static void testWriteHeavy() {
        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());

        int writerThreads = NUM_THREADS;
        int readerThreads = 2;

        // --- CopyOnWrite ---
        ExecutorService cowExec = Executors.newFixedThreadPool(writerThreads + readerThreads);
        long startCow = System.nanoTime();

        for (int i = 0; i < writerThreads; i++) {
            cowExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    cowList.add(k);
                }
            });
        }

        for (int i = 0; i < readerThreads; i++) {
            cowExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    for (Integer ignored : cowList) {
                        // chỉ để tạo read-load
                    }
                }
            });
        }

        cowExec.shutdown();
        try {
            cowExec.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            cowExec.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long durationCowMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startCow);

        // --- Synchronized list ---
        ExecutorService syncExec = Executors.newFixedThreadPool(writerThreads + readerThreads);
        long startSync = System.nanoTime();

        for (int i = 0; i < writerThreads; i++) {
            syncExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    synchronized (syncList) {
                        syncList.add(k);
                    }
                }
            });
        }

        for (int i = 0; i < readerThreads; i++) {
            syncExec.submit(() -> {
                for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                    synchronized (syncList) {
                        for (Integer ignored : syncList) {
                            // chỉ để tạo read-load
                        }
                    }
                }
            });
        }

        syncExec.shutdown();
        try {
            syncExec.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            syncExec.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long durationSyncMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startSync);

        System.out.println("Write-heavy (CopyOnWrite)  = " + durationCowMs + " ms");
        System.out.println("Write-heavy (Synchronized) = " + durationSyncMs + " ms");
    }
}

