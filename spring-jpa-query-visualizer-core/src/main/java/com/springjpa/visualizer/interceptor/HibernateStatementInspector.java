package com.springjpa.visualizer.interceptor;

import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.model.SqlQueryInfo;
import com.springjpa.visualizer.parser.*;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hibernate StatementInspector implementation that captures SQL queries
 * and their execution context for visualization purposes.
 */
public class HibernateStatementInspector implements StatementInspector {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateStatementInspector.class);
    
    private final QueryLogger queryLogger;
    private final StackTraceParser stackTraceParser;
    private final EntityMapper entityMapper;
    private final QuerySampler querySampler;
    private final PerformanceMonitor performanceMonitor;
    private final Map<String, Long> queryStartTimes;
    private volatile boolean enabled = true;
    
    public HibernateStatementInspector(QueryLogger queryLogger, StackTraceParser stackTraceParser) {
        this.queryLogger = queryLogger;
        this.stackTraceParser = stackTraceParser;
        this.entityMapper = new EntityMapper();
        this.querySampler = new QuerySampler();
        this.performanceMonitor = new PerformanceMonitor();
        this.queryStartTimes = new ConcurrentHashMap<>();
    }
    
    public HibernateStatementInspector(QueryLogger queryLogger) {
        this(queryLogger, new StackTraceParser());
    }
    
    public HibernateStatementInspector(QueryLogger queryLogger, StackTraceParser stackTraceParser, 
                                     EntityMapper entityMapper, QuerySampler querySampler, 
                                     PerformanceMonitor performanceMonitor) {
        this.queryLogger = queryLogger;
        this.stackTraceParser = stackTraceParser;
        this.entityMapper = entityMapper;
        this.querySampler = querySampler;
        this.performanceMonitor = performanceMonitor;
        this.queryStartTimes = new ConcurrentHashMap<>();
    }
    
    @Override
    public String inspect(String sql) {
        if (!enabled || sql == null || sql.trim().isEmpty()) {
            return sql;
        }
        
        try {
            // Check if query should be sampled
            if (!querySampler.shouldSample(sql)) {
                return sql;
            }
            
            // Record start time for this query
            String queryKey = generateQueryKey(sql);
            queryStartTimes.put(queryKey, System.currentTimeMillis());
            
            // Extract context from stack trace
            StackTraceParser.ContextInfo contextInfo = stackTraceParser.extractContext();
            
            // Extract entity mapping information
            EntityMapper.EntityMappingInfo entityMapping = entityMapper.extractEntityMapping(
                sql, contextInfo.getEntityName(), contextInfo.getClassName());
            
            // Build SqlQueryInfo
            SqlQueryInfo queryInfo = buildQueryInfo(sql, contextInfo, entityMapping);
            
            // Log the query information
            queryLogger.log(queryInfo);
            
            logger.debug("Intercepted SQL query: {} for method: {}", 
                        sql.substring(0, Math.min(50, sql.length())), 
                        contextInfo.getMethodName());
            
        } catch (Exception e) {
            // Never let the interceptor break the application
            logger.warn("Failed to intercept SQL query: {}", e.getMessage());
        }
        
        // Always return the original SQL unchanged
        return sql;
    }
    
    /**
     * Build SqlQueryInfo from SQL, context, and entity mapping
     */
    private SqlQueryInfo buildQueryInfo(String sql, StackTraceParser.ContextInfo contextInfo, 
                                       EntityMapper.EntityMappingInfo entityMapping) {
        SqlQueryInfo queryInfo = new SqlQueryInfo();
        
        queryInfo.setSql(sql);
        queryInfo.setMethodName(contextInfo.getMethodName());
        queryInfo.setClassName(contextInfo.getClassName());
        queryInfo.setEntityName(contextInfo.getEntityName());
        queryInfo.setParameters(contextInfo.getParameters());
        queryInfo.setStackTrace(contextInfo.getStackTrace());
        queryInfo.setSessionId(contextInfo.getSessionId());
        
        // Calculate execution time if available
        String queryKey = generateQueryKey(sql);
        Long startTime = queryStartTimes.get(queryKey);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            queryInfo.setExecutionTimeMs(executionTime);
            queryStartTimes.remove(queryKey);
            
            // Record performance metrics
            performanceMonitor.recordQuery(sql, contextInfo.getEntityName(), 
                                         entityMapping.getQueryType(), executionTime);
        }
        
        return queryInfo;
    }
    
    /**
     * Generate a unique key for the query to track execution time
     */
    private String generateQueryKey(String sql) {
        return Thread.currentThread().getName() + "_" + sql.hashCode() + "_" + System.currentTimeMillis();
    }
    
    /**
     * Check if the interceptor is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Enable or disable the interceptor
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        logger.info("HibernateStatementInspector {}", enabled ? "enabled" : "disabled");
    }
    
    /**
     * Get the name of this interceptor
     */
    public String getName() {
        return "HibernateStatementInspector";
    }
    
    /**
     * Get current query count (for monitoring purposes)
     */
    public int getActiveQueryCount() {
        return queryStartTimes.size();
    }
    
    /**
     * Clear any pending query start times (for cleanup)
     */
    public void clearPendingQueries() {
        queryStartTimes.clear();
    }
    
    /**
     * Get performance statistics
     */
    public PerformanceMonitor.PerformanceStats getPerformanceStats() {
        return performanceMonitor.getOverallStats();
    }
    
    /**
     * Get query sampling statistics
     */
    public QuerySampler.SamplingStats getSamplingStats() {
        return querySampler.getStats();
    }
    
    /**
     * Get entity statistics
     */
    public Map<String, PerformanceMonitor.EntityStats> getEntityStats() {
        return performanceMonitor.getEntityStats();
    }
    
    /**
     * Get query type statistics
     */
    public Map<String, PerformanceMonitor.QueryTypeStats> getQueryTypeStats() {
        return performanceMonitor.getQueryTypeStats();
    }
    
    /**
     * Get recent slow queries
     */
    public Map<String, PerformanceMonitor.SlowQueryInfo> getRecentSlowQueries() {
        return performanceMonitor.getRecentSlowQueries();
    }
    
    /**
     * Reset all statistics
     */
    public void resetStatistics() {
        performanceMonitor.resetStats();
        querySampler.resetStats();
        clearPendingQueries();
        logger.info("All statistics reset");
    }
    
    /**
     * Get entity mapper for external access
     */
    public EntityMapper getEntityMapper() {
        return entityMapper;
    }
    
    /**
     * Get query sampler for external access
     */
    public QuerySampler getQuerySampler() {
        return querySampler;
    }
    
    /**
     * Get performance monitor for external access
     */
    public PerformanceMonitor getPerformanceMonitor() {
        return performanceMonitor;
    }
}
