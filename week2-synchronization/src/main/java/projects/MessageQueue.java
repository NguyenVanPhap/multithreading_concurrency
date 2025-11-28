package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Project: Thread-Safe Bounded Message Queue
 * 
 * TODO Tasks:
 * 1. Implement BoundedMessageQueue with Lock and Condition
 * 2. Implement put() method with blocking when full
 * 3. Implement take() method with blocking when empty
 * 4. Create Producer threads
 * 5. Create Consumer threads
 * 6. Implement graceful shutdown
 * 7. Add statistics tracking
 */
public class MessageQueue {
    
    private static final int QUEUE_CAPACITY = 10;
    private static final int NUM_PRODUCERS = 3;
    private static final int NUM_CONSUMERS = 2;
    private static final int MESSAGES_PER_PRODUCER = 20;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Message Queue Demo - Producer/Consumer");
        System.out.println("==========================================\n");
        
        // TODO: Create bounded message queue
        BoundedMessageQueue<String> queue = new BoundedMessageQueue<>(QUEUE_CAPACITY);
        
        // TODO: Start producer threads
        System.out.println("Starting producers and consumers...\n");
        List<Thread> producers = startProducers(queue);
        
        // TODO: Start consumer threads
        List<Thread> consumers = startConsumers(queue);
        
        // TODO: Wait for all producers to complete
        System.out.println("Waiting for all producers to finish...");
        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // TODO: Shutdown queue (stop accepting new messages)
        System.out.println("\nShutting down queue...");
        queue.shutdown();
        
        // TODO: Wait for all consumers to empty queue
        System.out.println("Waiting for consumers to process remaining messages...");
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // TODO: Display statistics
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
        queue.printStatistics();
    }
    
    private static List<Thread> startProducers(BoundedMessageQueue<String> queue) {
        // TODO: Create producer threads
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 1; i <= NUM_PRODUCERS; i++) {
            final int producerId = i;
            Thread producer = new Thread(() -> {
                // TODO: Produce MESSAGES_PER_PRODUCER messages
                // TODO: Each message should have format: "Producer-X: Message-Y"
                // TODO: Use put() method

                for (int j = 1; j <= MESSAGES_PER_PRODUCER; j++) {
                    String message = "Producer-" + producerId + ": Message-" + j;
                    try {
                        queue.put(message);
                        System.out.println("Produced: " + message);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                
            }, "Producer-" + producerId);
            
            threads.add(producer);
            producer.start();
        }
        
        return threads;
    }
    
    private static List<Thread> startConsumers(BoundedMessageQueue<String> queue) {
        // TODO: Create consumer threads
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 1; i <= NUM_CONSUMERS; i++) {
            final int consumerId = i;
            Thread consumer = new Thread(() -> {
                // TODO: Consume messages
                // TODO: Use take() method
                // TODO: Process messages until shutdown and queue empty
                // TODO: Print consumed messages

                while (true) {
                    try {
                        String message = queue.take();
                        if (message != null) {
                            System.out.println("Consumed by " + Thread.currentThread().getName() + ": " + message);
                        } else if (queue.isShutdown() && queue.isEmpty()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
            }, "Consumer-" + consumerId);
            
            threads.add(consumer);
            consumer.start();
        }
        
        return threads;
    }
    
    // TODO: Implement BoundedMessageQueue class
    static class BoundedMessageQueue<T> {
        private final Queue<T> queue;
        private final int capacity;
        private final Lock lock;
        private final Condition notFull;
        private final Condition notEmpty;
        private volatile boolean isShutdown;
        
        // Statistics
        private int totalProduced;
        private int totalConsumed;
        private int blockedProducers;
        private int blockedConsumers;
        
        public BoundedMessageQueue(int capacity) {
            // TODO: Initialize fields
            this.capacity = capacity;
            this.queue = new LinkedList<>();
            this.lock = new ReentrantLock();
            this.notFull = lock.newCondition();
            this.notEmpty = lock.newCondition();
            this.isShutdown = false;
        }
        
        // TODO: Implement put - blocks when full
        public void put(T message) throws InterruptedException {
            // TODO: Acquire lock
            // TODO: While queue is full and not shutdown, await on notFull
            // TODO: Check if shutdown
            // TODO: Add message to queue
            // TODO: Signal notEmpty
            // TODO: Update statistics
            // TODO: Always unlock in finally
            
            lock.lock();
            try {
                while (queue.size() == capacity && !isShutdown) {
                    // TODO: Wait for space
                    blockedProducers++;
                    notFull.await();
                    blockedProducers--;
                }
                
                if (isShutdown) {
                    throw new IllegalStateException("Queue is shutdown");
                }
                
                // TODO: Add message
                // TODO: Signal consumers
                // TODO: Update stats

                queue.add(message);
                notEmpty.signal();
                totalProduced++;
            } finally {
                lock.unlock();
            }
        }
        
        // TODO: Implement take - blocks when empty
        public T take() throws InterruptedException {
            // TODO: Acquire lock
            // TODO: While queue is empty and not shutdown, await on notEmpty
            // TODO: Remove and return message
            // TODO: Signal notFull
            // TODO: Update statistics
            // TODO: Always unlock in finally

            lock.lock();
            try {
                if (queue.isEmpty() && !isShutdown) {
                    blockedConsumers++;
                    notEmpty.await();
                    blockedConsumers--;
                }

                if (!queue.isEmpty()) {
                    T message = queue.poll();
                    notFull.signal();
                    totalConsumed++;
                    return message;
                }

                blockedConsumers++;
            } finally {
                lock.unlock();
            }

            return null;
        }
        
        // TODO: Implement shutdown
        public void shutdown() {
            // TODO: Set isShutdown to true
            // TODO: Signal all waiting threads
            lock.lock();
            try {
                isShutdown = true;
                notFull.signalAll();
                notEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }
        
        public boolean isShutdown() {
            return isShutdown;
        }
        
        public int size() {
            lock.lock();
            try {
                return queue.size();
            } finally {
                lock.unlock();
            }
        }
        
        public boolean isEmpty() {
            return size() == 0;
        }
        
        // TODO: Implement statistics
        public void printStatistics() {
            System.out.println("\n=== Queue Statistics ===");
            System.out.println("Total produced: " + totalProduced);
            System.out.println("Total consumed: " + totalConsumed);
            System.out.println("Remaining in queue: " + queue.size());
            System.out.println("Blocked producers: " + blockedProducers);
            System.out.println("Blocked consumers: " + blockedConsumers);
            // TODO: Add more detailed statistics
            System.out.println("=========================");

        }
    }
}
