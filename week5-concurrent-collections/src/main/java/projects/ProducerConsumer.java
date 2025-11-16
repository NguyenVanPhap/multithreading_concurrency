package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project 1: Producer-Consumer với BlockingQueue
 * 
 * Classic producer-consumer pattern
 * - Multiple producers và consumers
 * - Thread-safe queue
 * - Graceful shutdown
 * 
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
        BlockingQueue<Item> queue = null; // TODO: Create với capacity QUEUE_CAPACITY
        
        AtomicBoolean running = new AtomicBoolean(true);
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        // TODO: Tạo và start producer threads
        // TODO: Tạo và start consumer threads
        
        // TODO: Run for a while
        // TODO: Graceful shutdown
        // TODO: Wait for all threads
        // TODO: In ra statistics
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
            // TODO: Produce itemsToProduce items
            // TODO: Use queue.put() để add items (blocks if full)
            // TODO: Increment produced counter
            // TODO: Stop when running = false
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
            // TODO: Consume items từ queue
            // TODO: Use queue.take() để get items (blocks if empty)
            // TODO: Process items
            // TODO: Increment consumed counter
            // TODO: Stop when running = false và queue empty
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

