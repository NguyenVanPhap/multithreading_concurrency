package projects;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.Random;

/**
 * Project 2: Parallel Merge Sort
 * 
 * Merge sort song song
 * - RecursiveTask implementation
 * - Performance testing
 * 
 * TODO Tasks:
 * 1. Implement MergeSortTask extends RecursiveAction
 * 2. Divide array
 * 3. Merge sorted parts
 * 4. Performance testing
 */
public class ParallelMergeSort {
    
    private static final int ARRAY_SIZE = 1000000;
    private static final int THRESHOLD = 10000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Parallel Merge Sort Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo array với random values
        int[] array = null; // TODO: Create
        
        // TODO: Test parallel merge sort
        long parallelTime = testParallelMergeSort(array);
        
        // TODO: Test sequential merge sort
        long sequentialTime = testSequentialMergeSort(array);
        
        // TODO: So sánh performance
        System.out.println("\nPerformance Comparison:");
        System.out.println("  Parallel: " + parallelTime + "ms");
        System.out.println("  Sequential: " + sequentialTime + "ms");
    }
    
    /**
     * TODO: Test parallel merge sort
     */
    private static long testParallelMergeSort(int[] array) {
        int[] copy = array.clone();
        
        ForkJoinPool pool = null; // TODO: Create
        
        MergeSortTask task = null; // TODO: Create với copy
        
        long startTime = System.currentTimeMillis();
        // TODO: Invoke task
        long endTime = System.currentTimeMillis();
        
        // TODO: Verify sorted
        System.out.println("Sorted: " + isSorted(copy));
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Test sequential merge sort
     */
    private static long testSequentialMergeSort(int[] array) {
        int[] copy = array.clone();
        
        long startTime = System.currentTimeMillis();
        // TODO: Sequential merge sort
        long endTime = System.currentTimeMillis();
        
        System.out.println("Sorted: " + isSorted(copy));
        System.out.println("Time: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    /**
     * TODO: Implement MergeSortTask extends RecursiveAction
     */
    static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;
        
        public MergeSortTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        @Override
        protected void compute() {
            // TODO: Nếu size <= THRESHOLD, sort trực tiếp
            // TODO: Nếu không, chia đôi và fork
            // TODO: Merge sorted parts
        }
        
        private void merge(int[] array, int start, int mid, int end) {
            // TODO: Implement merge logic
        }
    }
    
    private static boolean isSorted(int[] array) {
        // TODO: Check if array is sorted
        return false; // TODO: Return true if sorted
    }
}

