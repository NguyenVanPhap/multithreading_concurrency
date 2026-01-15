package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project 2: Concurrent Cache
 * 
 * Thread-safe cache với ConcurrentHashMap
 * - Cache với expiration
 * - Thread-safe operations
 * - Performance optimization
 * 
 * TODO Tasks:
 * 1. Implement CacheEntry với expiration
 * 2. Implement ConcurrentCache với ConcurrentHashMap
 * 3. Thread-safe get/put
 * 4. Expiration mechanism
 * 5. Statistics
 */
public class ConcurrentCache {
    
    private static final int CACHE_SIZE = 1000;
    private static final long DEFAULT_TTL_MS = 5000; // 5 seconds
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Concurrent Cache Demo");
        System.out.println("==========================================\n");
        
        // Tạo cache với TTL mặc định
        Cache<String, String> cache = new ConcurrentCacheImpl<>(DEFAULT_TTL_MS);
        
        // Basic operations
        cache.put("k1", "v1");
        cache.put("k2", "v2", 2000);
        System.out.println("get(k1) = " + cache.get("k1"));
        System.out.println("get(k2) = " + cache.get("k2"));
        
        // Expiration demo (đợi cho k2 hết hạn)
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("after sleep, get(k2) = " + cache.get("k2"));
        
        // Concurrent access demo
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            final int idx = i;
            executor.submit(() -> cache.put("ck" + idx, "cv" + idx));
            executor.submit(() -> cache.get("ck" + (idx / 2)));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // In ra statistics
        System.out.println(cache.getStats());
    }
    
    /**
     * TODO: Implement Cache interface
     */
    interface Cache<K, V> {
        V get(K key);
        void put(K key, V value);
        void put(K key, V value, long ttlMs);
        V remove(K key);
        void clear();
        int size();
        CacheStats getStats();
    }
    
    /**
     * CacheEntry với expiration time.
     */
    static class CacheEntry<V> {
        private final V value;
        private final long expireTime;
        
        public CacheEntry(V value, long ttlMs) {
            this.value = value;
            // expireTime = currentTime + ttlMs
            this.expireTime = System.currentTimeMillis() + ttlMs;
        }
        
        public V getValue() {
            return value;
        }
        
        public boolean isExpired() {
            // current time > expireTime thì coi như đã hết hạn
            return System.currentTimeMillis() > expireTime;
        }
    }
    
    /**
     * ConcurrentCache implementation dùng ConcurrentHashMap.
     */
    static class ConcurrentCacheImpl<K, V> implements Cache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> map;
        private final long defaultTtlMs;
        private final int maxSize;
        private final AtomicInteger hits = new AtomicInteger(0);
        private final AtomicInteger misses = new AtomicInteger(0);
        private final AtomicInteger evictions = new AtomicInteger(0);
        
        public ConcurrentCacheImpl(long defaultTtlMs) {
            this.map = new ConcurrentHashMap<>();
            this.defaultTtlMs = defaultTtlMs;
            this.maxSize = CACHE_SIZE;
        }
        
        @Override
        public V get(K key) {
            CacheEntry<V> entry = map.get(key);
            if (entry == null) {
                misses.incrementAndGet();
                return null;
            }
            
            if (entry.isExpired()) {
                map.remove(key);
                misses.incrementAndGet();
                return null;
            }
            
            hits.incrementAndGet();
            return entry.getValue();
        }
        
        @Override
        public void put(K key, V value) {
            // TODO: Put với default TTL
            put(key, value, defaultTtlMs);
        }
        
        @Override
        public void put(K key, V value, long ttlMs) {
            // Create CacheEntry với TTL
            CacheEntry<V> entry = new CacheEntry<>(value, ttlMs);
            
            // Handle eviction nếu cache full (best-effort, không cần chính xác tuyệt đối)
            while (map.size() >= maxSize) {
                evictOne();
                // nếu không evict được gì thì break để tránh vòng lặp vô hạn
                if (map.size() >= maxSize) {
                    break;
                }
            }
            
            // Put vào map
            map.put(key, entry);
        }
        
        @Override
        public V remove(K key) {
            CacheEntry<V> removed = map.remove(key);
            return removed != null ? removed.getValue() : null;
        }
        
        @Override
        public void clear() {
            map.clear();
        }
        
        @Override
        public int size() {
            return map.size();
        }
        
        @Override
        public CacheStats getStats() {
            return new CacheStats(
                hits.get(),
                misses.get(),
                evictions.get(),
                map.size()
            );
        }
        
        /**
         * Evict một phần tử khỏi cache:
         * - Ưu tiên xoá entry đã hết hạn
         * - Nếu không có, xoá một key bất kỳ
         */
        private void evictOne() {
            // 1. Thử xoá entry đã hết hạn trước
            for (K key : map.keySet()) {
                CacheEntry<V> entry = map.get(key);
                if (entry != null && entry.isExpired()) {
                    if (map.remove(key, entry)) {
                        evictions.incrementAndGet();
                        return;
                    }
                }
            }
            
            // 2. Nếu không có expired, xoá 1 key bất kỳ
            for (K key : map.keySet()) {
                CacheEntry<V> entry = map.remove(key);
                if (entry != null) {
                    evictions.incrementAndGet();
                    return;
                }
            }
        }
    }
    
    /**
     * Cache statistics
     */
    static class CacheStats {
        private final int hits;
        private final int misses;
        private final int evictions;
        private final int size;
        
        public CacheStats(int hits, int misses, int evictions, int size) {
            this.hits = hits;
            this.misses = misses;
            this.evictions = evictions;
            this.size = size;
        }
        
        @Override
        public String toString() {
            double hitRate = (hits + misses) > 0 ? (double) hits / (hits + misses) * 100 : 0;
            return String.format("Cache Stats: Hits=%d, Misses=%d, Evictions=%d, Size=%d, HitRate=%.2f%%",
                    hits, misses, evictions, size, hitRate);
        }
    }
}

