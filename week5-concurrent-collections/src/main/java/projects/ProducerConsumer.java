package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project 1: Producer-Consumer với BlockingQueue
 * <p>
 * Classic producer-consumer pattern
 * - Multiple producers và consumers
 * - Thread-safe queue
 * - Graceful shutdown
 * <p>
 * TODO Tasks:
 * 1. Implement Producer class
 * 2. Implement Consumer class
 * 3. Use BlockingQueue
 * 4. Statistics tracking
 * 5. Graceful shutdown
 */
public class ProducerConsumer {

    private static final int NUM_PRODUCERS = 5;
    private static final int NUM_CONSUMERS = 3;
    private static final int QUEUE_CAPACITY = 20;
    private static final int ITEMS_PER_PRODUCER = 100;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Producer-Consumer Demo");
        System.out.println("==========================================\n");

        // TODO: Tạo BlockingQueue
        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY); // TODO: Create với capacity QUEUE_CAPACITY

        AtomicBoolean running = new AtomicBoolean(true);
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);

        // Tạo và start producer threads
        Producer[] producers = new Producer[NUM_PRODUCERS];
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = new Producer(queue, running, produced, ITEMS_PER_PRODUCER);
            producers[i].start();
        }

        // Tạo và start consumer threads
        Consumer[] consumers = new Consumer[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Consumer(queue, running, consumed);
            consumers[i].start();
        }

        long startTime = System.currentTimeMillis();

        // Đợi tất cả producers hoàn thành
        for (Producer producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Không còn producer mới, báo cho consumers biết để dần dừng lại
        running.set(false);

        // Đợi consumers xử lý hết items trong queue rồi kết thúc
        for (Consumer consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.currentTimeMillis();

        // In ra statistics
        System.out.println("\n==========================================");
        System.out.println("Produced items : " + produced.get());
        System.out.println("Consumed items : " + consumed.get());
        System.out.println("Remaining in queue: " + queue.size());
        System.out.println("Total time (ms): " + (endTime - startTime));
        System.out.println("==========================================");
    }

    /**
     * TODO: Implement Producer class
     */
    static class Producer extends Thread {
        private final BlockingQueue<Item> queue;
        private final AtomicBoolean running;
        private final AtomicInteger produced;
        private final int itemsToProduce;

        public Producer(BlockingQueue<Item> queue, AtomicBoolean running,
                        AtomicInteger produced, int itemsToProduce) {
            this.queue = queue;
            this.running = running;
            this.produced = produced;
            this.itemsToProduce = itemsToProduce;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < itemsToProduce; i++) {
                    if (!running.get()) {
                        break;
                    }
                    Item item = new Item(i, "Data-" + i + " by " + Thread.currentThread().getName());
                    queue.put(item); // blocks if full
                    produced.incrementAndGet();
                    // Simulate production time
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * TODO: Implement Consumer class
     */
    static class Consumer extends Thread {
        private final BlockingQueue<Item> queue;
        private final AtomicBoolean running;
        private final AtomicInteger consumed;

        public Consumer(BlockingQueue<Item> queue, AtomicBoolean running,
                        AtomicInteger consumed) {
            this.queue = queue;
            this.running = running;
            this.consumed = consumed;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Khi đã được báo dừng và queue rỗng => thoát
                    if (!running.get() && queue.isEmpty()) {
                        break;
                    }

                    // poll với timeout để có thể thoát khi running=false
                    Item item = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (item == null) {
                        // Không có item hiện tại, thử lại vòng sau
                        continue;
                    }

                    // Process item (simulated by sleep)
                    Thread.sleep(50); // Simulate processing time
                    consumed.incrementAndGet();
                    // Có thể log nhẹ nếu muốn
                    // System.out.println(Thread.currentThread().getName() + " consumed " + item);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Item class
     */
    static class Item {
        private final int id;
        private final String data;

        public Item(int id, String data) {
            this.id = id;
            this.data = data;
        }

        public int getId() {
            return id;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Item{id=" + id + ", data='" + data + "'}";
        }
    }
}

