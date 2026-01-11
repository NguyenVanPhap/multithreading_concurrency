package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.UUID;

/**
 * Advanced Project: Event-Driven Message Broker
 * 
 * Message broker với:
 * - BlockingQueue cho mỗi topic
 * - ThreadPoolExecutor cho message processing
 * - Topic routing
 * - Consumer group management
 * - Message acknowledgment
 * 
 * TODO Tasks:
 * 1. Implement topic-based routing
 * 2. BlockingQueue cho mỗi topic
 * 3. Consumer group management
 * 4. Message processing với ThreadPoolExecutor
 * 5. Message acknowledgment
 * 6. At-least-once delivery guarantee
 */
public class EventDrivenMessageBroker {
    
    // Topic queues
    private final ConcurrentHashMap<String, BlockingQueue<Message>> topicQueues;
    
    // Consumer groups
    private final ConcurrentHashMap<String, List<Consumer>> consumerGroups;
    
    // Executor cho message processing
    private final ThreadPoolExecutor processorExecutor;
    
    // Statistics
    private final AtomicLong publishedMessages = new AtomicLong(0);
    private final AtomicLong consumedMessages = new AtomicLong(0);
    private final AtomicLong acknowledgedMessages = new AtomicLong(0);
    
    private volatile boolean running = true;
    
    public EventDrivenMessageBroker(int processorPoolSize) {
        this.topicQueues = new ConcurrentHashMap<>();
        this.consumerGroups = new ConcurrentHashMap<>();
        
        // TODO: Tạo ThreadPoolExecutor
        this.processorExecutor = new ThreadPoolExecutor(
            processorPoolSize / 2,
            processorPoolSize,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "MessageProcessor-" + 
                                         threadNumber.getAndIncrement());
                    t.setDaemon(false);
                    return t;
                }
            }
        );
    }
    
    /**
     * Publish message to topic
     */
    public CompletableFuture<Void> publish(String topic, String message) {
        if (!running) {
            return CompletableFuture.completedFuture(null);
        }
        
        // TODO: Get or create topic queue
        BlockingQueue<Message> queue = topicQueues.computeIfAbsent(topic, 
            k -> new LinkedBlockingQueue<>());
        
        Message msg = new Message(UUID.randomUUID().toString(), topic, message);
        
        try {
            queue.put(msg);
            publishedMessages.incrementAndGet();
            System.out.println("Published to " + topic + ": " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * Subscribe consumer to topic
     */
    public void subscribe(String topic, String consumerGroup, 
                         MessageHandler handler) {
        // TODO: Create consumer và add to group
        Consumer consumer = new Consumer(topic, consumerGroup, handler);
        
        consumerGroups.computeIfAbsent(consumerGroup, k -> new CopyOnWriteArrayList<>())
            .add(consumer);
        
        // Start consuming
        startConsumer(consumer);
        
        System.out.println("Consumer subscribed to " + topic + 
                         " in group " + consumerGroup);
    }
    
    /**
     * Start consumer
     */
    private void startConsumer(Consumer consumer) {
        processorExecutor.submit(() -> {
            BlockingQueue<Message> queue = topicQueues.get(consumer.getTopic());
            if (queue == null) {
                return;
            }
            
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    Message message = queue.take();
                    
                    // Process message
                    processorExecutor.submit(() -> {
                        try {
                            consumer.getHandler().handle(message);
                            consumedMessages.incrementAndGet();
                            
                            // Acknowledge
                            acknowledge(message);
                        } catch (Exception e) {
                            System.err.println("Error processing message: " + 
                                             e.getMessage());
                            // TODO: Handle error (retry, dead letter queue, etc.)
                        }
                    });
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    /**
     * Acknowledge message
     */
    private void acknowledge(Message message) {
        acknowledgedMessages.incrementAndGet();
        // TODO: Implement acknowledgment logic
    }
    
    /**
     * Get statistics
     */
    public BrokerStatistics getStatistics() {
        return new BrokerStatistics(
            publishedMessages.get(),
            consumedMessages.get(),
            acknowledgedMessages.get(),
            topicQueues.size(),
            consumerGroups.size()
        );
    }
    
    /**
     * Shutdown gracefully
     */
    public void shutdown() {
        running = false;
        
        processorExecutor.shutdown();
        
        try {
            if (!processorExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                processorExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            processorExecutor.shutdownNow();
        }
    }
    
    // Inner classes
    static class Message {
        private final String id;
        private final String topic;
        private final String content;
        private final long timestamp;
        
        public Message(String id, String topic, String content) {
            this.id = id;
            this.topic = topic;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getId() { return id; }
        public String getTopic() { return topic; }
        public String getContent() { return content; }
        public long getTimestamp() { return timestamp; }
    }
    
    interface MessageHandler {
        void handle(Message message) throws Exception;
    }
    
    static class Consumer {
        private final String topic;
        private final String consumerGroup;
        private final MessageHandler handler;
        
        public Consumer(String topic, String consumerGroup, MessageHandler handler) {
            this.topic = topic;
            this.consumerGroup = consumerGroup;
            this.handler = handler;
        }
        
        public String getTopic() { return topic; }
        public String getConsumerGroup() { return consumerGroup; }
        public MessageHandler getHandler() { return handler; }
    }
    
    static class BrokerStatistics {
        private final long published;
        private final long consumed;
        private final long acknowledged;
        private final int topics;
        private final int consumerGroups;
        
        public BrokerStatistics(long published, long consumed, long acknowledged,
                               int topics, int consumerGroups) {
            this.published = published;
            this.consumed = consumed;
            this.acknowledged = acknowledged;
            this.topics = topics;
            this.consumerGroups = consumerGroups;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Broker Stats: published=%d, consumed=%d, acknowledged=%d, " +
                "topics=%d, consumerGroups=%d",
                published, consumed, acknowledged, topics, consumerGroups
            );
        }
    }
    
    // Main method
    public static void main(String[] args) throws InterruptedException {
        EventDrivenMessageBroker broker = new EventDrivenMessageBroker(10);
        
        System.out.println("==========================================");
        System.out.println("  Event-Driven Message Broker Demo");
        System.out.println("==========================================\n");
        
        // Subscribe consumers
        broker.subscribe("orders", "group1", message -> {
            System.out.println("  [Consumer1] Processed: " + message.getContent());
        });
        
        broker.subscribe("orders", "group1", message -> {
            System.out.println("  [Consumer2] Processed: " + message.getContent());
        });
        
        broker.subscribe("payments", "group2", message -> {
            System.out.println("  [Consumer3] Processed: " + message.getContent());
        });
        
        Thread.sleep(1000);
        
        // Publish messages
        System.out.println("\nPublishing messages...\n");
        
        for (int i = 1; i <= 10; i++) {
            broker.publish("orders", "Order #" + i);
            if (i % 3 == 0) {
                broker.publish("payments", "Payment #" + i);
            }
            Thread.sleep(200);
        }
        
        Thread.sleep(2000);
        
        System.out.println("\n" + broker.getStatistics());
        
        broker.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

