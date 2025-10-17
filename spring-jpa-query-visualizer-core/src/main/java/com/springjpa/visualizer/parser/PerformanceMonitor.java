package com.springjpa.visualizer.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Performance monitor for tracking SQL query performance metrics.
 * Collects statistics about query execution times, slow queries, and performance trends.
 */
public class PerformanceMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);
    
    private final long slowQueryThresholdMs;
    private final boolean enabled;
    
    // Performance counters
    private final LongAdder totalQueries;
    private final LongAdder slowQueries;
    private final LongAdder totalExecutionTime;
    private final AtomicLong minExecutionTime;
    private final AtomicLong maxExecutionTime;
    
    // Query type statistics
    private final ConcurrentHashMap<String, QueryTypeStats> queryTypeStats;
    
    // Entity statistics
    private final ConcurrentHashMap<String, EntityStats> entityStats;
    
    // Recent slow queries for analysis
    private final ConcurrentHashMap<String, SlowQueryInfo> recentSlowQueries;
    
    public PerformanceMonitor(long slowQueryThresholdMs, boolean enabled) {
        this.slowQueryThresholdMs = slowQueryThresholdMs;
        this.enabled = enabled;
        
        this.totalQueries = new LongAdder();
        this.slowQueries = new LongAdder();
        this.totalExecutionTime = new LongAdder();
        this.minExecutionTime = new AtomicLong(Long.MAX_VALUE);
        this.maxExecutionTime = new AtomicLong(0);
        
        this.queryTypeStats = new ConcurrentHashMap<>();
        this.entityStats = new ConcurrentHashMap<>();
        this.recentSlowQueries = new ConcurrentHashMap<>();
        
        logger.info("PerformanceMonitor initialized with slow query threshold: {}ms (enabled: {})", 
                   slowQueryThresholdMs, enabled);
    }
    
    public PerformanceMonitor(long slowQueryThresholdMs) {
        this(slowQueryThresholdMs, true);
    }
    
    public PerformanceMonitor() {
        this(1000, true); // Default: 1 second threshold
    }
    
    /**
     * Record a query execution
     */
    public void recordQuery(String sql, String entityName, String queryType, long executionTimeMs) {
        if (!enabled) {
            return;
        }
        
        try {
            // Update global counters
            totalQueries.increment();
            totalExecutionTime.add(executionTimeMs);
            
            // Update min/max execution times
            updateMinMaxExecutionTime(executionTimeMs);
            
            // Check if it's a slow query
            if (executionTimeMs > slowQueryThresholdMs) {
                slowQueries.increment();
                recordSlowQuery(sql, entityName, queryType, executionTimeMs);
            }
            
            // Update query type statistics
            updateQueryTypeStats(queryType, executionTimeMs);
            
            // Update entity statistics
            if (entityName != null) {
                updateEntityStats(entityName, executionTimeMs);
            }
            
        } catch (Exception e) {
            logger.warn("Failed to record query performance: {}", e.getMessage());
        }
    }
    
    /**
     * Update min/max execution times
     */
    private void updateMinMaxExecutionTime(long executionTimeMs) {
        // Update minimum
        long currentMin = minExecutionTime.get();
        while (executionTimeMs < currentMin && !minExecutionTime.compareAndSet(currentMin, executionTimeMs)) {
            currentMin = minExecutionTime.get();
        }
        
        // Update maximum
        long currentMax = maxExecutionTime.get();
        while (executionTimeMs > currentMax && !maxExecutionTime.compareAndSet(currentMax, executionTimeMs)) {
            currentMax = maxExecutionTime.get();
        }
    }
    
    /**
     * Record slow query information
     */
    private void recordSlowQuery(String sql, String entityName, String queryType, long executionTimeMs) {
        String slowQueryId = generateSlowQueryId(sql, entityName);
        
        SlowQueryInfo slowQueryInfo = new SlowQueryInfo();
        slowQueryInfo.setSql(sql);
        slowQueryInfo.setEntityName(entityName);
        slowQueryInfo.setQueryType(queryType);
        slowQueryInfo.setExecutionTimeMs(executionTimeMs);
        slowQueryInfo.setTimestamp(LocalDateTime.now());
        
        recentSlowQueries.put(slowQueryId, slowQueryInfo);
        
        // Keep only recent slow queries (limit to 100)
        if (recentSlowQueries.size() > 100) {
            String oldestKey = recentSlowQueries.keys().nextElement();
            recentSlowQueries.remove(oldestKey);
        }
        
        logger.warn("Slow query detected: {}ms - {} ({})", 
                   executionTimeMs, entityName, queryType);
    }
    
    /**
     * Update query type statistics
     */
    private void updateQueryTypeStats(String queryType, long executionTimeMs) {
        if (queryType == null) {
            queryType = "UNKNOWN";
        }
        
        queryTypeStats.computeIfAbsent(queryType, k -> new QueryTypeStats())
                     .recordQuery(executionTimeMs);
    }
    
    /**
     * Update entity statistics
     */
    private void updateEntityStats(String entityName, long executionTimeMs) {
        entityStats.computeIfAbsent(entityName, k -> new EntityStats())
                  .recordQuery(executionTimeMs);
    }
    
    /**
     * Generate unique ID for slow query
     */
    private String generateSlowQueryId(String sql, String entityName) {
        return String.format("slow_%s_%d", 
            entityName != null ? entityName : "unknown", 
            System.currentTimeMillis());
    }
    
    /**
     * Get overall performance statistics
     */
    public PerformanceStats getOverallStats() {
        long total = totalQueries.sum();
        long slow = slowQueries.sum();
        long totalTime = totalExecutionTime.sum();
        
        double avgExecutionTime = total > 0 ? (double) totalTime / total : 0.0;
        double slowQueryRate = total > 0 ? (double) slow / total : 0.0;
        
        return new PerformanceStats(
            total, slow, totalTime, avgExecutionTime, slowQueryRate,
            minExecutionTime.get() == Long.MAX_VALUE ? 0 : minExecutionTime.get(),
            maxExecutionTime.get()
        );
    }
    
    /**
     * Get query type statistics
     */
    public ConcurrentHashMap<String, QueryTypeStats> getQueryTypeStats() {
        return new ConcurrentHashMap<>(queryTypeStats);
    }
    
    /**
     * Get entity statistics
     */
    public ConcurrentHashMap<String, EntityStats> getEntityStats() {
        return new ConcurrentHashMap<>(entityStats);
    }
    
    /**
     * Get recent slow queries
     */
    public ConcurrentHashMap<String, SlowQueryInfo> getRecentSlowQueries() {
        return new ConcurrentHashMap<>(recentSlowQueries);
    }
    
    /**
     * Reset all statistics
     */
    public void resetStats() {
        totalQueries.reset();
        slowQueries.reset();
        totalExecutionTime.reset();
        minExecutionTime.set(Long.MAX_VALUE);
        maxExecutionTime.set(0);
        
        queryTypeStats.clear();
        entityStats.clear();
        recentSlowQueries.clear();
        
        logger.info("Performance statistics reset");
    }
    
    /**
     * Check if monitoring is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Get slow query threshold
     */
    public long getSlowQueryThresholdMs() {
        return slowQueryThresholdMs;
    }
    
    /**
     * Performance statistics
     */
    public static class PerformanceStats {
        private final long totalQueries;
        private final long slowQueries;
        private final long totalExecutionTime;
        private final double avgExecutionTime;
        private final double slowQueryRate;
        private final long minExecutionTime;
        private final long maxExecutionTime;
        
        public PerformanceStats(long totalQueries, long slowQueries, long totalExecutionTime,
                              double avgExecutionTime, double slowQueryRate,
                              long minExecutionTime, long maxExecutionTime) {
            this.totalQueries = totalQueries;
            this.slowQueries = slowQueries;
            this.totalExecutionTime = totalExecutionTime;
            this.avgExecutionTime = avgExecutionTime;
            this.slowQueryRate = slowQueryRate;
            this.minExecutionTime = minExecutionTime;
            this.maxExecutionTime = maxExecutionTime;
        }
        
        // Getters
        public long getTotalQueries() { return totalQueries; }
        public long getSlowQueries() { return slowQueries; }
        public long getTotalExecutionTime() { return totalExecutionTime; }
        public double getAvgExecutionTime() { return avgExecutionTime; }
        public double getSlowQueryRate() { return slowQueryRate; }
        public long getMinExecutionTime() { return minExecutionTime; }
        public long getMaxExecutionTime() { return maxExecutionTime; }
        
        @Override
        public String toString() {
            return String.format("PerformanceStats{total=%d, slow=%d, avg=%.2fms, slowRate=%.2f%%, min=%dms, max=%dms}",
                               totalQueries, slowQueries, avgExecutionTime, slowQueryRate * 100, 
                               minExecutionTime, maxExecutionTime);
        }
    }
    
    /**
     * Query type statistics
     */
    public static class QueryTypeStats {
        private final LongAdder count = new LongAdder();
        private final LongAdder totalTime = new LongAdder();
        private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxTime = new AtomicLong(0);
        
        public void recordQuery(long executionTimeMs) {
            count.increment();
            totalTime.add(executionTimeMs);
            
            // Update min/max
            long currentMin = minTime.get();
            while (executionTimeMs < currentMin && !minTime.compareAndSet(currentMin, executionTimeMs)) {
                currentMin = minTime.get();
            }
            
            long currentMax = maxTime.get();
            while (executionTimeMs > currentMax && !maxTime.compareAndSet(currentMax, executionTimeMs)) {
                currentMax = maxTime.get();
            }
        }
        
        public long getCount() { return count.sum(); }
        public long getTotalTime() { return totalTime.sum(); }
        public double getAvgTime() { 
            long count = this.count.sum();
            return count > 0 ? (double) totalTime.sum() / count : 0.0;
        }
        public long getMinTime() { return minTime.get() == Long.MAX_VALUE ? 0 : minTime.get(); }
        public long getMaxTime() { return maxTime.get(); }
    }
    
    /**
     * Entity statistics
     */
    public static class EntityStats {
        private final LongAdder count = new LongAdder();
        private final LongAdder totalTime = new LongAdder();
        private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxTime = new AtomicLong(0);
        
        public void recordQuery(long executionTimeMs) {
            count.increment();
            totalTime.add(executionTimeMs);
            
            // Update min/max
            long currentMin = minTime.get();
            while (executionTimeMs < currentMin && !minTime.compareAndSet(currentMin, executionTimeMs)) {
                currentMin = minTime.get();
            }
            
            long currentMax = maxTime.get();
            while (executionTimeMs > currentMax && !maxTime.compareAndSet(currentMax, executionTimeMs)) {
                currentMax = maxTime.get();
            }
        }
        
        public long getCount() { return count.sum(); }
        public long getTotalTime() { return totalTime.sum(); }
        public double getAvgTime() { 
            long count = this.count.sum();
            return count > 0 ? (double) totalTime.sum() / count : 0.0;
        }
        public long getMinTime() { return minTime.get() == Long.MAX_VALUE ? 0 : minTime.get(); }
        public long getMaxTime() { return maxTime.get(); }
    }
    
    /**
     * Slow query information
     */
    public static class SlowQueryInfo {
        private String sql;
        private String entityName;
        private String queryType;
        private long executionTimeMs;
        private LocalDateTime timestamp;
        
        // Getters and Setters
        public String getSql() { return sql; }
        public void setSql(String sql) { this.sql = sql; }
        
        public String getEntityName() { return entityName; }
        public void setEntityName(String entityName) { this.entityName = entityName; }
        
        public String getQueryType() { return queryType; }
        public void setQueryType(String queryType) { this.queryType = queryType; }
        
        public long getExecutionTimeMs() { return executionTimeMs; }
        public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        
        @Override
        public String toString() {
            return String.format("SlowQueryInfo{sql='%s', entity='%s', type='%s', time=%dms, time=%s}",
                               sql, entityName, queryType, executionTimeMs, timestamp);
        }
    }
}
