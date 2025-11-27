package projects;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

/**
 * Project 1: Parallel Array Sum
 * 
 * Tính tổng array song song
 * - ForkJoin approach
 * - Parallel stream approach
 * - Performance comparison
 * 
 * TODO Tasks:
 * 1. Implement ForkJoin sum
 * 2. Implement parallel stream sum
 * 3. Compare performance
 */
public class ParallelArraySum {
    
    private static final int ARRAY_SIZE = 100000000;
    private static final int THRESHOLD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Parallel Array Sum Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo large array
        int[] array = null; // TODO: Create với ARRAY_SIZE elements
        
        // TODO: Test ForkJoin sum
        System.out.println("Test 1: ForkJoin Sum");
        long forkJoinTime = testForkJoinSum(array);
        
        // TODO: Test Parallel Stream sum
        System.out.println("\nTest 2: Parallel Stream Sum");
        long streamTime = testParallelStreamSum(array);
        
        // TODO: So sánh performance
        System.out.println("\nPerformance Comparison:");
        System.out.println("  ForkJoin: " + forkJoinTime + "ms");
        System.out.println("  Parallel Stream: " + streamTime + "ms");
    }
    
    /**
     * TODO: Test ForkJoin sum
     */
    private static long testForkJoinSum(int[] array) {
        ForkJoinPool pool = null; // TODO: Create
        
        SumTask task = null; // TODO: Create với array
        
        long startTime = System.currentTimeMillis();
        // TODO: Invoke task
        long result = 0; // TODO: Get result
        long endTime = System.currentTimeMillis();
        
        System.out.println("Sum: " + result);
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Test parallel stream sum
     */
    private static long testParallelStreamSum(int[] array) {
        long startTime = System.currentTimeMillis();
        // TODO: Convert to list và use parallelStream
        long result = 0; // TODO: Calculate sum
        long endTime = System.currentTimeMillis();
        
        System.out.println("Sum: " + result);
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Implement SumTask
     */
    static class SumTask extends RecursiveTask<Long> {
        private final int[] array;
        private final int start;
        private final int end;
        
        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        @Override
        protected Long compute() {
            // TODO: Nếu size <= THRESHOLD, tính trực tiếp
            // TODO: Nếu không, chia đôi và fork
            // TODO: Join results
            return 0L; // TODO: Return sum
        }
    }
}

