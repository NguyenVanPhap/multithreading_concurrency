package exercises;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 1: ConcurrentMap Demo
 * 
 * TODO Tasks:
 * 1. Sử dụng ConcurrentHashMap
 * 2. So sánh với synchronized HashMap
 * 3. Atomic operations (putIfAbsent, replace, compute)
 * 4. Performance comparison
 */
public class ConcurrentMapDemo {
    
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  ConcurrentMap Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test ConcurrentHashMap
        System.out.println("Test 1: ConcurrentHashMap");
        testConcurrentHashMap();
        
        // TODO: Test synchronized HashMap
        System.out.println("\nTest 2: Synchronized HashMap");
        testSynchronizedHashMap();
        
        // TODO: Test atomic operations
        System.out.println("\nTest 3: Atomic Operations");
        testAtomicOperations();
        
        // TODO: Performance comparison
        System.out.println("\nTest 4: Performance Comparison");
        testPerformanceComparison();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test ConcurrentHashMap
     */
    private static void testConcurrentHashMap() {
        // TODO: Tạo ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = null; // TODO: Use new ConcurrentHashMap<>()
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Submit tasks từ multiple threads
        // TODO: Mỗi thread thực hiện OPERATIONS_PER_THREAD operations
        // TODO: Put, get, remove operations
        
        // TODO: Wait for all threads
        // TODO: In ra size và một số values
    }
    
    /**
     * TODO: Test synchronized HashMap
     */
    private static void testSynchronizedHashMap() {
        // TODO: Tạo synchronized HashMap
        Map<String, Integer> map = null; // TODO: Use Collections.synchronizedMap(new HashMap<>())
        
        ExecutorService executor = null; // TODO: Create ExecutorService
        
        // TODO: Submit tasks từ multiple threads (tương tự testConcurrentHashMap)
        // TODO: Quan sát performance
        
        // TODO: Wait for all threads
        // TODO: In ra size
    }
    
    /**
     * TODO: Test atomic operations
     */
    private static void testAtomicOperations() {
        ConcurrentHashMap<String, Integer> map = null; // TODO: Create ConcurrentHashMap
        
        // TODO: Test putIfAbsent
        // TODO: Test replace (oldValue, newValue)
        // TODO: Test compute (key, remappingFunction)
        // TODO: Test computeIfAbsent
        // TODO: Test computeIfPresent
        // TODO: Test merge
        
        // TODO: In ra kết quả các operations
    }
    
    /**
     * TODO: Performance comparison
     */
    private static void testPerformanceComparison() {
        // TODO: Test ConcurrentHashMap performance
        long concurrentTime = 0; // TODO: Measure time
        
        // TODO: Test Synchronized HashMap performance
        long synchronizedTime = 0; // TODO: Measure time
        
        // TODO: In ra kết quả so sánh
    }
}

