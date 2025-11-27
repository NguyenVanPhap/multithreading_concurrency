package exercises;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 2: BlockingQueue Demo
 * 
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
        BlockingQueue<String> queue = null; // TODO: Use new ArrayBlockingQueue<>(QUEUE_CAPACITY)
        
        // TODO: Test put() - blocks if full
        // TODO: Test take() - blocks if empty
        // TODO: Test offer() với timeout
        // TODO: Test poll() với timeout
        
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test LinkedBlockingQueue
     */
    private static void testLinkedBlockingQueue() {
        // TODO: Tạo LinkedBlockingQueue (bounded hoặc unbounded)
        BlockingQueue<Integer> queue = null; // TODO: Create
        
        // TODO: Test operations tương tự ArrayBlockingQueue
        // TODO: So sánh với ArrayBlockingQueue
    }
    
    /**
     * TODO: Test PriorityBlockingQueue
     */
    private static void testPriorityBlockingQueue() {
        // TODO: Tạo PriorityBlockingQueue
        BlockingQueue<Integer> queue = null; // TODO: Create
        
        // TODO: Add elements với different priorities
        // TODO: Take elements và quan sát order (priority order)
        // TODO: In ra thứ tự elements
    }
    
    /**
     * TODO: Test SynchronousQueue
     */
    private static void testSynchronousQueue() {
        // TODO: Tạo SynchronousQueue
        SynchronousQueue<String> queue = null; // TODO: Create
        
        // TODO: Producer thread - put elements
        // TODO: Consumer thread - take elements
        // TODO: Quan sát direct handoff (no storage)
    }
    
    /**
     * TODO: Test Producer-Consumer pattern
     */
    private static void testProducerConsumer() {
        // TODO: Tạo BlockingQueue
        BlockingQueue<Integer> queue = null; // TODO: Create
        
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        // TODO: Tạo NUM_PRODUCERS producer threads
        // TODO: Mỗi producer tạo ITEMS_PER_PRODUCER items
        // TODO: Tạo NUM_CONSUMERS consumer threads
        // TODO: Consumers lấy items từ queue
        
        // TODO: Wait for all threads
        // TODO: In ra statistics (produced, consumed)
    }
}

