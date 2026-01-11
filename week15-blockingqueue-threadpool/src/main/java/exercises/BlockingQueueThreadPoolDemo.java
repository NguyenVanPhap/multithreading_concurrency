package exercises;

import java.util.concurrent.*;
import java.util.*;

/**
 * Exercise: BlockingQueue + ThreadPool Demo
 * 
 * TODO Tasks:
 * 1. Tạo BlockingQueue
 * 2. Sử dụng ThreadPoolExecutor với queue
 * 3. Producer-Consumer pattern
 */
public class BlockingQueueThreadPoolDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println("  BlockingQueue + ThreadPool Demo");
        System.out.println("==========================================\n");
        
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5)
        );
        
        try {
            // Demo 1: Basic producer-consumer
            demoProducerConsumer(queue, executor);
            
            // Demo 2: ThreadPool với custom queue
            demoThreadPoolWithQueue(executor);
            
        } finally {
            executor.shutdown();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void demoProducerConsumer(BlockingQueue<String> queue, 
                                            ThreadPoolExecutor executor) 
            throws InterruptedException {
        System.out.println("Demo 1: Producer-Consumer");
        System.out.println("-------------------------");
        
        // Producer
        executor.submit(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put("Item " + i);
                    System.out.println("Produced: Item " + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        // Consumer
        executor.submit(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String item = queue.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        Thread.sleep(3000);
        System.out.println();
    }
    
    private static void demoThreadPoolWithQueue(ThreadPoolExecutor executor) {
        System.out.println("Demo 2: ThreadPool với Custom Queue");
        System.out.println("-----------------------------------");
        
        // Submit tasks
        for (int i = 0; i < 10; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("Processing task " + taskNum + 
                                 " on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        System.out.println("Active threads: " + executor.getActiveCount());
        System.out.println("Queue size: " + executor.getQueue().size());
        System.out.println();
    }
}

