package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

/**
 * Advanced Project: Distributed Cache Manager
 * 
 * Hệ thống cache phân tán với:
 * - ConcurrentHashMap cho storage
 * - ExecutorService cho background tasks
 * - Cache warming với parallel loading
 * - TTL expiration
 * - Background cleanup
 * - Statistics tracking
 * 
 * TODO Tasks:
 * 1. Implement cache với ConcurrentHashMap
 * 2. TTL (Time To Live) expiration
 * 3. Background cleanup thread
 * 4. Cache warming với parallel loading
 * 5. Statistics tracking (hit/miss rate)
 * 6. Thread-safe operations
 */
public class DistributedCacheManager {
    
    // Cache storage với metadata
    private final ConcurrentHashMap<String, CacheEntry> cache;
    
    // Executors
    private final ExecutorService readExecutor;      // Cho read operations
    private final ExecutorService writeExecutor;     // Cho write operations
    private final ExecutorService warmupExecutor;    // Cho cache warming
    private final ScheduledExecutorService cleanupExecutor; // Cho cleanup
    
    // Statistics
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicInteger size = new AtomicInteger(0);
    
    private final long defaultTTL; // milliseconds
    private volatile boolean running = true;
    
    public DistributedCacheManager(long defaultTTL) {
        this.defaultTTL = defaultTTL;
        this.cache = new ConcurrentHashMap<>();
        
        // TODO: Initialize executors
        this.readExecutor = Executors.newFixedThreadPool(20);
        this.writeExecutor = Executors.newFixedThreadPool(10);
        this.warmupExecutor = Executors.newFixedThreadPool(5);
        this.cleanupExecutor = Executors.newScheduledThreadPool(1);
        
        // Start background cleanup
        startCleanupTask();
    }
    
    /**
     * Get value from cache
     */
    public CompletableFuture<String> get(String key) {
        return CompletableFuture.supplyAsync(() -> {
            CacheEntry entry = cache.get(key);
            
            if (entry == null || entry.isExpired()) {
                misses.incrementAndGet();
                if (entry != null && entry.isExpired()) {
                    cache.remove(key);
                    size.decrementAndGet();
                }
                return null;
            }
            
            hits.incrementAndGet();
            return entry.getValue();
        }, readExecutor);
    }
    
    /**
     * Put value into cache
     */
    public CompletableFuture<Void> put(String key, String value) {
        return CompletableFuture.runAsync(() -> {
            CacheEntry entry = new CacheEntry(value, System.currentTimeMillis() + defaultTTL);
            CacheEntry oldEntry = cache.put(key, entry);
            
            if (oldEntry == null) {
                size.incrementAndGet();
            }
        }, writeExecutor);
    }
    
    /**
     * Put với custom TTL
     */
    public CompletableFuture<Void> put(String key, String value, long ttlMs) {
        return CompletableFuture.runAsync(() -> {
            CacheEntry entry = new CacheEntry(value, System.currentTimeMillis() + ttlMs);
            CacheEntry oldEntry = cache.put(key, entry);
            
            if (oldEntry == null) {
                size.incrementAndGet();
            }
        }, writeExecutor);
    }
    
    /**
     * Cache warming - load nhiều keys song song
     */
    public CompletableFuture<Void> warmup(Map<String, String> data) {
        System.out.println("Warming cache with " + data.size() + " entries...");
        
        // TODO: Load data song song
        List<CompletableFuture<Void>> futures = data.entrySet().stream()
            .map(entry -> CompletableFuture.runAsync(() -> {
                put(entry.getKey(), entry.getValue()).join();
            }, warmupExecutor))
            .collect(java.util.stream.Collectors.toList());
        
        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        return all.thenRun(() -> {
            System.out.println("Cache warmed up with " + data.size() + " entries");
        });
    }
    
    /**
     * Background cleanup task
     */
    private void startCleanupTask() {
        // TODO: Schedule periodic cleanup
        cleanupExecutor.scheduleAtFixedRate(() -> {
            if (!running) return;
            
            int removed = 0;
            long now = System.currentTimeMillis();
            
            Iterator<Map.Entry<String, CacheEntry>> it = cache.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, CacheEntry> entry = it.next();
                if (entry.getValue().isExpired(now)) {
                    it.remove();
                    removed++;
                    size.decrementAndGet();
                }
            }
            
            if (removed > 0) {
                System.out.println("Cleanup: Removed " + removed + " expired entries");
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    /**
     * Compute if absent - atomic operation
     */
    public CompletableFuture<String> computeIfAbsent(String key, 
                                                     java.util.function.Function<String, String> loader) {
        return CompletableFuture.supplyAsync(() -> {
            return cache.computeIfAbsent(key, k -> {
                String value = loader.apply(k);
                size.incrementAndGet();
                return new CacheEntry(value, System.currentTimeMillis() + defaultTTL);
            }).getValue();
        }, writeExecutor);
    }
    
    /**
     * Get statistics
     */
    public CacheStatistics getStatistics() {
        long totalRequests = hits.get() + misses.get();
        double hitRate = totalRequests > 0 ? 
            (double) hits.get() / totalRequests * 100 : 0.0;
        
        return new CacheStatistics(
            size.get(),
            hits.get(),
            misses.get(),
            hitRate
        );
    }
    
    /**
     * Shutdown gracefully
     */
    public void shutdown() {
        running = false;
        
        readExecutor.shutdown();
        writeExecutor.shutdown();
        warmupExecutor.shutdown();
        cleanupExecutor.shutdown();
        
        try {
            if (!readExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                readExecutor.shutdownNow();
            }
            if (!writeExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                writeExecutor.shutdownNow();
            }
            if (!warmupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                warmupExecutor.shutdownNow();
            }
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Inner classes
    static class CacheEntry {
        private final String value;
        private final long expiryTime;
        
        public CacheEntry(String value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        public String getValue() { return value; }
        
        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }
        
        public boolean isExpired(long now) {
            return now > expiryTime;
        }
    }
    
    static class CacheStatistics {
        private final int size;
        private final long hits;
        private final long misses;
        private final double hitRate;
        
        public CacheStatistics(int size, long hits, long misses, double hitRate) {
            this.size = size;
            this.hits = hits;
            this.misses = misses;
            this.hitRate = hitRate;
        }
        
        public int getSize() { return size; }
        public long getHits() { return hits; }
        public long getMisses() { return misses; }
        public double getHitRate() { return hitRate; }
        
        @Override
        public String toString() {
            return String.format(
                "Cache Stats: size=%d, hits=%d, misses=%d, hitRate=%.2f%%",
                size, hits, misses, hitRate
            );
        }
    }
    
    // Main method
    public static void main(String[] args) throws InterruptedException {
        DistributedCacheManager cache = new DistributedCacheManager(5000); // 5 seconds TTL
        
        System.out.println("==========================================");
        System.out.println("  Distributed Cache Manager Demo");
        System.out.println("==========================================\n");
        
        // Warmup cache
        Map<String, String> warmupData = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            warmupData.put("key" + i, "value" + i);
        }
        cache.warmup(warmupData).join();
        
        Thread.sleep(1000);
        
        // Concurrent reads
        System.out.println("\nPerforming concurrent reads...");
        List<CompletableFuture<String>> readFutures = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            final int keyNum = i;
            readFutures.add(cache.get("key" + keyNum));
        }
        
        CompletableFuture.allOf(readFutures.toArray(new CompletableFuture[0])).join();
        
        // Concurrent writes
        System.out.println("Performing concurrent writes...");
        List<CompletableFuture<Void>> writeFutures = new ArrayList<>();
        for (int i = 101; i <= 150; i++) {
            final int keyNum = i;
            writeFutures.add(cache.put("key" + keyNum, "value" + keyNum));
        }
        
        CompletableFuture.allOf(writeFutures.toArray(new CompletableFuture[0])).join();
        
        Thread.sleep(2000);
        
        // Print statistics
        System.out.println("\n" + cache.getStatistics());
        
        // Wait for cleanup
        Thread.sleep(4000);
        
        System.out.println("\nAfter expiration: " + cache.getStatistics());
        
        cache.shutdown();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
}

