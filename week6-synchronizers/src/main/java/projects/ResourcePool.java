package projects;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Project 2: Resource Pool với Semaphore
 * 
 * Quản lý pool tài nguyên với Semaphore
 * - Limited resources
 * - Acquire/release mechanism
 * - Statistics
 * 
 * TODO Tasks:
 * 1. Implement ResourcePool với Semaphore
 * 2. Acquire/release resources
 * 3. Timeout handling
 * 4. Statistics tracking
 */
public class ResourcePool {
    
    private static final int POOL_SIZE = 5;
    private static final int NUM_USERS = 10;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Resource Pool Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo resource pool
        Pool<Resource> pool = null; // TODO: Create với POOL_SIZE
        
        // TODO: Multiple users request resources
        // TODO: Quan sát behavior
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement Pool interface
     */
    interface Pool<T> {
        T acquire() throws InterruptedException;
        T acquire(long timeout, TimeUnit unit) throws InterruptedException;
        void release(T resource);
        int available();
        PoolStats getStats();
    }
    
    /**
     * TODO: Implement ResourcePool class
     */
    static class ResourcePoolImpl<T> implements Pool<T> {
        private final List<T> resources;
        private final Semaphore semaphore;
        private final AtomicInteger acquiredCount = new AtomicInteger(0);
        private final AtomicInteger releasedCount = new AtomicInteger(0);
        
        public ResourcePoolImpl(List<T> resources) {
            this.resources = null; // TODO: Initialize
            this.semaphore = null; // TODO: Create Semaphore với permits = resources.size()
        }
        
        @Override
        public T acquire() throws InterruptedException {
            // TODO: Acquire permit từ semaphore
            // TODO: Get resource từ pool
            // TODO: Increment acquiredCount
            return null; // TODO: Return resource
        }
        
        @Override
        public T acquire(long timeout, TimeUnit unit) throws InterruptedException {
            // TODO: Try acquire với timeout
            // TODO: Nếu timeout -> return null
            // TODO: Nếu success -> get resource
            return null; // TODO: Return resource or null
        }
        
        @Override
        public void release(T resource) {
            // TODO: Return resource to pool
            // TODO: Release semaphore permit
            // TODO: Increment releasedCount
        }
        
        @Override
        public int available() {
            // TODO: Return số permits available
            return 0; // TODO: Return available permits
        }
        
        @Override
        public PoolStats getStats() {
            // TODO: Return statistics
            return null; // TODO: Create và return PoolStats
        }
    }
    
    static class Resource {
        private final int id;
        
        public Resource(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
    }
    
    static class PoolStats {
        private final int acquired;
        private final int released;
        private final int available;
        
        public PoolStats(int acquired, int released, int available) {
            this.acquired = acquired;
            this.released = released;
            this.available = available;
        }
        
        @Override
        public String toString() {
            return String.format("Pool Stats: Acquired=%d, Released=%d, Available=%d",
                    acquired, released, available);
        }
    }
}

