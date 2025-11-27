package exercises;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 3: CopyOnWrite Demo
 * 
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
     * TODO: Test CopyOnWriteArrayList
     */
    private static void testCopyOnWriteArrayList() {
        // TODO: Tạo CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Multiple threads đọc và ghi
        // TODO: Quan sát behavior
        // TODO: Reads không block writes
        // TODO: Writes tạo copy mới
        
        // TODO: Wait for threads
        // TODO: In ra size
    }
    
    /**
     * TODO: Test CopyOnWriteArraySet
     */
    private static void testCopyOnWriteArraySet() {
        // TODO: Tạo CopyOnWriteArraySet
        CopyOnWriteArraySet<Integer> set = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Multiple threads add và contains
        // TODO: Quan sát behavior
        
        // TODO: Wait for threads
        // TODO: In ra size
    }
    
    /**
     * TODO: Test read-heavy scenario (CopyOnWrite shines)
     */
    private static void testReadHeavy() {
        CopyOnWriteArrayList<Integer> cowList = null; // TODO: Create
        List<Integer> syncList = null; // TODO: Create synchronized list
        
        // TODO: Add initial elements
        // TODO: Many reader threads, few writer threads
        // TODO: Measure performance
        
        // TODO: So sánh performance
    }
    
    /**
     * TODO: Test write-heavy scenario (CopyOnWrite expensive)
     */
    private static void testWriteHeavy() {
        CopyOnWriteArrayList<Integer> cowList = null; // TODO: Create
        List<Integer> syncList = null; // TODO: Create synchronized list
        
        // TODO: Many writer threads, few reader threads
        // TODO: Measure performance
        
        // TODO: So sánh performance (CopyOnWrite sẽ chậm hơn)
    }
}

