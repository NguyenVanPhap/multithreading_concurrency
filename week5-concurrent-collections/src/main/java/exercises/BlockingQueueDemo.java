package exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 2: BlockingQueue Demo
 * <p>
 * TODO Tasks:
 * 1. ArrayBlockingQueue
 * 2. LinkedBlockingQueue
 * 3. PriorityBlockingQueue
 * 4. SynchronousQueue
 * 5. Producer-Consumer pattern
 */
public class BlockingQueueDemo {

    private static final int QUEUE_CAPACITY = 10;
    private static final int NUM_PRODUCERS = 3;
    private static final int NUM_CONSUMERS = 2;
    private static final int ITEMS_PER_PRODUCER = 20;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  BlockingQueue Demo");
        System.out.println("==========================================\n");

        // TODO: Test ArrayBlockingQueue
        System.out.println("Test 1: ArrayBlockingQueue");
        testArrayBlockingQueue();

        // TODO: Test LinkedBlockingQueue
        System.out.println("\nTest 2: LinkedBlockingQueue");
        testLinkedBlockingQueue();

        // TODO: Test PriorityBlockingQueue
        System.out.println("\nTest 3: PriorityBlockingQueue");
        testPriorityBlockingQueue();

        // TODO: Test SynchronousQueue
        System.out.println("\nTest 4: SynchronousQueue");
        testSynchronousQueue();

        // TODO: Test Producer-Consumer
        System.out.println("\nTest 5: Producer-Consumer Pattern");
        testProducerConsumer();

        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }

    /**
     * TODO: Test ArrayBlockingQueue
     */
    private static void testArrayBlockingQueue() {
        // TODO: Tạo ArrayBlockingQueue với capacity QUEUE_CAPACITY
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY); // TODO: Use new ArrayBlockingQueue<>(QUEUE_CAPACITY)

        // TODO: Test put() - blocks if full
        try {
            for (int i = 0; i < QUEUE_CAPACITY; i++) {
                queue.put("Item " + i);
                System.out.println("Put: Item " + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /*try {
            // This put should block since the queue is full
            System.out.println("Attempting to put one more item (should block)...");
            queue.put("Item " + QUEUE_CAPACITY);
            System.out.println("Put: Item " + QUEUE_CAPACITY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }*/

        // TODO: Test take() - blocks if empty
        try {
            for (int i = 0; i < QUEUE_CAPACITY; i++) {
                String item = queue.take();
                System.out.println("Took: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
//        try {
//            // This take should block since the queue is empty
//            System.out.println("Attempting to take one more item (should block)...");
//            String item = queue.take();
//            System.out.println("Took: " + item);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        // TODO: Test offer() với timeout
        try {
            boolean offered = queue.offer("Offered Item", 2, TimeUnit.SECONDS);
            System.out.println("Offered Item: " + offered);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Test poll() với timeout
        try {
            String polledItem = queue.poll(2, TimeUnit.SECONDS);
            System.out.println("Polled Item: " + polledItem);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // TODO: In ra kết quả
        System.out.println("Final queue size: " + queue.size());
    }

    /**
     * TODO: Test LinkedBlockingQueue
     */
    private static void testLinkedBlockingQueue() {
        // TODO: Tạo LinkedBlockingQueue (bounded hoặc unbounded)
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(); // TODO: Create

        // TODO: Test operations tương tự ArrayBlockingQueue
        // TODO: So sánh với ArrayBlockingQueue

        // TODO: Test put() - blocks if full
        try {
            for (int i = 0; i < QUEUE_CAPACITY; i++) {
                queue.put("Item " + i);
                System.out.println("Put: Item " + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            // This put should block since the queue is full
            System.out.println("Attempting to put one more item (should not block)...");
            queue.put("Item " + QUEUE_CAPACITY);
            System.out.println("Put: Item " + QUEUE_CAPACITY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Test take() - blocks if empty
        try {
            for (int i = 0; i < QUEUE_CAPACITY; i++) {
                String item = queue.take();
                System.out.println("Took: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            // This take should block since the queue is empty
            System.out.println("Attempting to take one more item (should not block)...");
            String item = queue.take();
            System.out.println("Took: " + item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Test offer() với timeout
        try {
            boolean offered = queue.offer("Offered Item", 2, TimeUnit.SECONDS);
            System.out.println("Offered Item: " + offered);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO: Test poll() với timeout
        try {
            String polledItem = queue.poll(2, TimeUnit.SECONDS);
            System.out.println("Polled Item: " + polledItem);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // TODO: In ra kết quả
        System.out.println("Final queue size: " + queue.size());
    }

    /**
     * TODO: Test PriorityBlockingQueue
     */
    private static void testPriorityBlockingQueue() {
        // TODO: Tạo PriorityBlockingQueue
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(); // TODO: Create

        // TODO: Add elements với different priorities
        try {
            queue.put(10);
            queue.put(1);
            queue.put(5);
            queue.put(3);
            queue.put(7);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // TODO: Take elements và quan sát order (priority order)
        // TODO: In ra thứ tự elements
        try {
            while (!queue.isEmpty()) {
                Integer item = queue.take();
                System.out.println("Took: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * TODO: Test SynchronousQueue
     */
    private static void testSynchronousQueue() {
        // TODO: Tạo SynchronousQueue
        SynchronousQueue<String> queue = new SynchronousQueue<>(); // TODO: Create

        // TODO: Producer thread - put elements
        // TODO: Consumer thread - take elements
        // TODO: Quan sát direct handoff (no storage)

        Thread producer = new Thread(() -> {
            try {
                String[] items = {"A", "B", "C"};
                for (String item : items) {
                    System.out.println("Producing: " + item);
                    queue.put(item);
                    System.out.println("Produced: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    String item = queue.take();
                    System.out.println("Consumed: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }


    private static void testProducerConsumer() {


        final int QUEUE_CAPACITY = 5;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 2;
        final int ITEMS_PER_PRODUCER = 10;
        // 1️⃣ Tạo BlockingQueue (bounded)
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);

        List<Thread> threads = new ArrayList<>();

        // 2️⃣ Tạo Producer threads
        for (int i = 1; i <= NUM_PRODUCERS; i++) {
            int producerId = i;
            Thread producer = new Thread(() -> {
                try {
                    for (int j = 1; j <= ITEMS_PER_PRODUCER; j++) {
                        int item = produced.incrementAndGet();
                        queue.put(item); // BLOCK nếu queue full
                        System.out.println(
                            "Producer-" + producerId + " produced: " + item +
                                " | queue size=" + queue.size()
                        );
                        Thread.sleep(100); // giả lập xử lý
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads.add(producer);
        }

        // 3️⃣ Tạo Consumer threads
        for (int i = 1; i <= NUM_CONSUMERS; i++) {
            int consumerId = i;
            Thread consumer = new Thread(() -> {
                try {
                    while (true) {
                        Integer item = queue.take(); // BLOCK nếu queue empty
                        consumed.incrementAndGet();
                        System.out.println(
                            "Consumer-" + consumerId + " consumed: " + item +
                                " | queue size=" + queue.size()
                        );
                        Thread.sleep(150); // giả lập xử lý
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads.add(consumer);
        }

        // 4️⃣ Start all threads
        threads.forEach(Thread::start);

        // 5️⃣ Đợi producers xong
        try {


            for (int i = 0; i < NUM_PRODUCERS; i++) {
                threads.get(i).join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 6️⃣ Stop consumers
        for (int i = NUM_PRODUCERS; i < threads.size(); i++) {
            threads.get(i).interrupt();
        }

        // 7️⃣ Statistics
        System.out.println("\n=== STATISTICS ===");
        System.out.println("Produced: " + produced.get());
        System.out.println("Consumed: " + consumed.get());
    }

}

