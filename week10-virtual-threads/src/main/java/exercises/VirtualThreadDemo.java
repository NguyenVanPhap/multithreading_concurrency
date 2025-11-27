package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 1: Virtual Thread Basics
 * 
 * TODO Tasks:
 * 1. Creating virtual threads
 * 2. Thread.ofVirtual()
 * 3. Basic operations
 * 
 * Note: Requires Java 19+
 */
public class VirtualThreadDemo {
    
    private static final int NUM_THREADS = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Virtual Threads Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test Thread.ofVirtual()
        System.out.println("Test 1: Thread.ofVirtual()");
        testThreadOfVirtual();
        
        // TODO: Test creating many virtual threads
        System.out.println("\nTest 2: Many Virtual Threads");
        testManyVirtualThreads();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test Thread.ofVirtual()
     */
    private static void testThreadOfVirtual() {
        // TODO: Tạo virtual thread với Thread.ofVirtual()
        Thread virtualThread = null; // TODO: Use Thread.ofVirtual().start()
        // TODO: Task in ra thread name và type
        
        // TODO: Wait for thread
        // TODO: In ra thread information
    }
    
    /**
     * TODO: Test creating many virtual threads
     */
    private static void testManyVirtualThreads() {
        // TODO: Tạo NUM_THREADS virtual threads
        // TODO: Mỗi thread sleep một chút
        // TODO: Wait for all threads
        // TODO: Quan sát có thể tạo nhiều threads mà không tốn nhiều resources
    }
}

