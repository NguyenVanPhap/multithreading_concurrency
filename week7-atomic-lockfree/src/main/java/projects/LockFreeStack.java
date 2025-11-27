package projects;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project 1: Lock-Free Stack
 * 
 * Implement lock-free stack
 * - CAS-based operations
 * - Thread-safe push/pop
 * - Performance testing
 * 
 * TODO Tasks:
 * 1. Implement Node class
 * 2. Implement push() với CAS
 * 3. Implement pop() với CAS
 * 4. Handle empty stack
 * 5. Performance testing
 */
public class LockFreeStack {
    
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Lock-Free Stack Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo lock-free stack
        Stack<Integer> stack = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Multiple threads push và pop
        // TODO: Measure performance
        // TODO: Verify correctness
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement Stack interface
     */
    interface Stack<T> {
        void push(T item);
        T pop();
        boolean isEmpty();
        int size();
    }
    
    /**
     * TODO: Implement LockFreeStack class
     */
    static class LockFreeStackImpl<T> implements Stack<T> {
        private final AtomicReference<Node<T>> head;
        private final AtomicInteger size;
        
        public LockFreeStackImpl() {
            this.head = null; // TODO: Create AtomicReference với null
            this.size = null; // TODO: Create AtomicInteger với 0
        }
        
        @Override
        public void push(T item) {
            // TODO: Create new node
            // TODO: CAS loop để push
            // TODO: Update head atomically
            // TODO: Increment size
        }
        
        @Override
        public T pop() {
            // TODO: CAS loop để pop
            // TODO: Update head atomically
            // TODO: Decrement size
            // TODO: Return value
            return null; // TODO: Return popped value or null
        }
        
        @Override
        public boolean isEmpty() {
            // TODO: Check if head is null
            return false; // TODO: Return true if empty
        }
        
        @Override
        public int size() {
            // TODO: Return size
            return 0; // TODO: Return size
        }
    }
    
    /**
     * TODO: Implement Node class
     */
    static class Node<T> {
        private final T value;
        private final Node<T> next;
        
        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
        
        public T getValue() {
            return value;
        }
        
        public Node<T> getNext() {
            return next;
        }
    }
}

