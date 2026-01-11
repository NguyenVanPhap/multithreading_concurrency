package exercises;

import java.util.concurrent.*;
import java.util.*;

/**
 * Exercise: Producer-Consumer Pattern Demo
 * 
 * TODO Tasks:
 * 1. Multiple producers
 * 2. Multiple consumers
 * 3. Thread-safe queue operations
 */
public class ProducerConsumerDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("==========================================");
        System.out.println("  Producer-Consumer Demo");
        System.out.println("==========================================\n");
        
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(20);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        try {
            // Multiple producers
            for (int i = 0; i < 3; i++) {
                final int producerId = i;
                executor.submit(() -> {
                    for (int j = 0; j < 10; j++) {
                        try {
                            String item = "Producer" + producerId + "-Item" + j;
                            queue.put(item);
                            System.out.println("Produced: " + item);
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }
            
            Thread.sleep(500);
            
            // Multiple consumers
            for (int i = 0; i < 2; i++) {
                final int consumerId = i;
                executor.submit(() -> {
                    while (true) {
                        try {
                            String item = queue.take();
                            System.out.println("Consumer" + consumerId + 
                                             " consumed: " + item);
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }
            
            Thread.sleep(5000);
            
        } finally {
            executor.shutdownNow();
        }
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

