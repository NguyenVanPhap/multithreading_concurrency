package projects;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
        
        // TODO: Tạo cache
        Cache<String, String> cache = null; // TODO: Create với DEFAULT_TTL_MS
        
        // TODO: Test basic operations
        // TODO: Test expiration
        // TODO: Test concurrent access
        // TODO: Test statistics
        
        // TODO: In ra statistics
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
     * TODO: Implement CacheEntry
     */
    static class CacheEntry<V> {
        private final V value;
        private final long expireTime;
        
        public CacheEntry(V value, long ttlMs) {
            this.value = value;
            // TODO: Calculate expireTime = currentTime + ttlMs
            this.expireTime = 0; // TODO: Set expire time
        }
        
        public V getValue() {
            return value;
        }
        
        public boolean isExpired() {
            // TODO: Check if current time > expireTime
            return false; // TODO: Return true if expired
        }
    }
    
    /**
     * TODO: Implement ConcurrentCache class
     */
    static class ConcurrentCacheImpl<K, V> implements Cache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> map;
        private final long defaultTtlMs;
        private final AtomicInteger hits = new AtomicInteger(0);
        private final AtomicInteger misses = new AtomicInteger(0);
        private final AtomicInteger evictions = new AtomicInteger(0);
        
        public ConcurrentCacheImpl(long defaultTtlMs) {
            this.map = null; // TODO: Create ConcurrentHashMap
            this.defaultTtlMs = defaultTtlMs;
        }
        
        @Override
        public V get(K key) {
            // TODO: Get entry từ map
            // TODO: Check if expired
            // TODO: If expired, remove và increment misses
            // TODO: If not expired, increment hits và return value
            return null; // TODO: Return value or null
        }
        
        @Override
        public void put(K key, V value) {
            // TODO: Put với default TTL
            put(key, value, defaultTtlMs);
        }
        
        @Override
        public void put(K key, V value, long ttlMs) {
            // TODO: Create CacheEntry với TTL
            // TODO: Put vào map
            // TODO: Handle eviction nếu cache full
        }
        
        @Override
        public V remove(K key) {
            // TODO: Remove từ map
            return null; // TODO: Return removed value
        }
        
        @Override
        public void clear() {
            // TODO: Clear map
        }
        
        @Override
        public int size() {
            // TODO: Return map size
            return 0; // TODO: Return size
        }
        
        @Override
        public CacheStats getStats() {
            // TODO: Return statistics
            return null; // TODO: Create và return CacheStats
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

