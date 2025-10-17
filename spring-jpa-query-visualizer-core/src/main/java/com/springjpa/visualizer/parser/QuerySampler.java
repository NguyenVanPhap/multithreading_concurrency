package com.springjpa.visualizer.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Query sampler for performance optimization.
 * Controls which queries should be processed based on sampling rate and patterns.
 */
public class QuerySampler {
    
    private static final Logger logger = LoggerFactory.getLogger(QuerySampler.class);
    
    private final double samplingRate;
    private final boolean enabled;
    private final AtomicLong totalQueries;
    private final AtomicLong sampledQueries;
    
    // Patterns for queries that should always be sampled
    private static final String[] HIGH_PRIORITY_PATTERNS = {
        "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER"
    };
    
    // Patterns for queries that can be sampled less frequently
    private static final String[] LOW_PRIORITY_PATTERNS = {
        "SELECT COUNT", "SELECT EXISTS", "SELECT 1"
    };
    
    public QuerySampler(double samplingRate) {
        this.samplingRate = Math.max(0.0, Math.min(1.0, samplingRate));
        this.enabled = samplingRate < 1.0;
        this.totalQueries = new AtomicLong(0);
        this.sampledQueries = new AtomicLong(0);
        
        logger.info("QuerySampler initialized with rate: {} (enabled: {})", 
                   this.samplingRate, this.enabled);
    }
    
    public QuerySampler() {
        this(1.0); // Default: sample all queries
    }
    
    /**
     * Determine if a query should be sampled
     */
    public boolean shouldSample(String sql) {
        if (!enabled || sql == null) {
            return true; // Always sample if disabled or null
        }
        
        long total = totalQueries.incrementAndGet();
        
        // Always sample high-priority queries
        if (isHighPriorityQuery(sql)) {
            sampledQueries.incrementAndGet();
            return true;
        }
        
        // Sample low-priority queries less frequently
        if (isLowPriorityQuery(sql)) {
            boolean shouldSample = (total % 10) == 0; // Sample every 10th query
            if (shouldSample) {
                sampledQueries.incrementAndGet();
            }
            return shouldSample;
        }
        
        // Use sampling rate for regular queries
        boolean shouldSample = (total % Math.round(1.0 / samplingRate)) == 0;
        if (shouldSample) {
            sampledQueries.incrementAndGet();
        }
        
        return shouldSample;
    }
    
    /**
     * Check if query is high priority (should always be sampled)
     */
    private boolean isHighPriorityQuery(String sql) {
        if (sql == null) {
            return false;
        }
        
        String upperSql = sql.toUpperCase();
        for (String pattern : HIGH_PRIORITY_PATTERNS) {
            if (upperSql.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if query is low priority (can be sampled less frequently)
     */
    private boolean isLowPriorityQuery(String sql) {
        if (sql == null) {
            return false;
        }
        
        String upperSql = sql.toUpperCase();
        for (String pattern : LOW_PRIORITY_PATTERNS) {
            if (upperSql.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get current sampling statistics
     */
    public SamplingStats getStats() {
        long total = totalQueries.get();
        long sampled = sampledQueries.get();
        
        return new SamplingStats(total, sampled, samplingRate);
    }
    
    /**
     * Reset sampling statistics
     */
    public void resetStats() {
        totalQueries.set(0);
        sampledQueries.set(0);
        logger.debug("Sampling statistics reset");
    }
    
    /**
     * Check if sampling is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Get sampling rate
     */
    public double getSamplingRate() {
        return samplingRate;
    }
    
    /**
     * Sampling statistics
     */
    public static class SamplingStats {
        private final long totalQueries;
        private final long sampledQueries;
        private final double samplingRate;
        private final double actualRate;
        
        public SamplingStats(long totalQueries, long sampledQueries, double samplingRate) {
            this.totalQueries = totalQueries;
            this.sampledQueries = sampledQueries;
            this.samplingRate = samplingRate;
            this.actualRate = totalQueries > 0 ? (double) sampledQueries / totalQueries : 0.0;
        }
        
        public long getTotalQueries() {
            return totalQueries;
        }
        
        public long getSampledQueries() {
            return sampledQueries;
        }
        
        public double getSamplingRate() {
            return samplingRate;
        }
        
        public double getActualRate() {
            return actualRate;
        }
        
        public long getDroppedQueries() {
            return totalQueries - sampledQueries;
        }
        
        public double getDropRate() {
            return totalQueries > 0 ? (double) getDroppedQueries() / totalQueries : 0.0;
        }
        
        @Override
        public String toString() {
            return String.format("SamplingStats{total=%d, sampled=%d, rate=%.2f, actual=%.2f}", 
                               totalQueries, sampledQueries, samplingRate, actualRate);
        }
    }
}
