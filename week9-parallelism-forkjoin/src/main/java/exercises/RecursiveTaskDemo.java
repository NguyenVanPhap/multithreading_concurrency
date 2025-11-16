package exercises;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Exercise 2: RecursiveTask Demo
 * 
 * TODO Tasks:
 * 1. Implement RecursiveTask
 * 2. Divide-and-conquer
 * 3. Result computation
 */
public class RecursiveTaskDemo {
    
    private static final int THRESHOLD = 1000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  RecursiveTask Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test sum array
        System.out.println("Test 1: Sum Array");
        testSumArray();
        
        // TODO: Test factorial
        System.out.println("\nTest 2: Factorial");
        testFactorial();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test sum array với RecursiveTask
     */
    private static void testSumArray() {
        int[] array = null; // TODO: Create large array (10000 elements)
        
        ForkJoinPool pool = null; // TODO: Create
        
        // TODO: Tạo SumTask
        SumTask task = null; // TODO: Create với array
        
        // TODO: Invoke task
        // TODO: In ra kết quả
    }
    
    /**
     * TODO: Test factorial với RecursiveTask
     */
    private static void testFactorial() {
        int n = 20;
        
        ForkJoinPool pool = null; // TODO: Create
        
        // TODO: Tạo FactorialTask
        // TODO: Invoke và in ra kết quả
    }
    
    /**
     * TODO: Implement SumTask extends RecursiveTask
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
    
    /**
     * TODO: Implement FactorialTask extends RecursiveTask
     */
    static class FactorialTask extends RecursiveTask<Long> {
        private final int n;
        
        public FactorialTask(int n) {
            this.n = n;
        }
        
        @Override
        protected Long compute() {
            // TODO: Nếu n <= THRESHOLD, tính trực tiếp
            // TODO: Nếu không, chia đôi và fork
            // TODO: Join results
            return 0L; // TODO: Return factorial
        }
    }
}

