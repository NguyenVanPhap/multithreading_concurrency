package exercises;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Exercise 3: Parallel Streams
 * 
 * TODO Tasks:
 * 1. Parallel stream operations
 * 2. Performance comparison
 * 3. Common pitfalls
 */
public class ParallelStreamDemo {
    
    private static final int SIZE = 10000000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Parallel Streams Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test parallel sum
        System.out.println("Test 1: Parallel Sum");
        testParallelSum();
        
        // TODO: Test parallel filter và map
        System.out.println("\nTest 2: Parallel Filter và Map");
        testParallelFilterMap();
        
        // TODO: Performance comparison
        System.out.println("\nTest 3: Performance Comparison");
        testPerformanceComparison();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test parallel sum
     */
    private static void testParallelSum() {
        // TODO: Tạo list of integers
        List<Integer> numbers = null; // TODO: Create với SIZE elements
        
        // TODO: Sequential sum
        long sequentialSum = 0; // TODO: Use stream().sum()
        
        // TODO: Parallel sum
        long parallelSum = 0; // TODO: Use parallelStream().sum()
        
        // TODO: In ra kết quả và so sánh
    }
    
    /**
     * TODO: Test parallel filter và map
     */
    private static void testParallelFilterMap() {
        List<Integer> numbers = null; // TODO: Create
        
        // TODO: Parallel filter (even numbers)
        // TODO: Parallel map (square)
        // TODO: Collect results
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Performance comparison
     */
    private static void testPerformanceComparison() {
        List<Integer> numbers = null; // TODO: Create large list
        
        // TODO: Measure sequential stream time
        long sequentialTime = 0; // TODO: Measure
        
        // TODO: Measure parallel stream time
        long parallelTime = 0; // TODO: Measure
        
        // TODO: In ra so sánh
    }
}

