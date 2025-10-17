package com.springjpa.visualizer.config;

import com.springjpa.visualizer.interceptor.HibernateStatementInspector;
import com.springjpa.visualizer.logger.QueryLogger;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * Health indicator for Spring JPA Query Visualizer.
 * Provides health status information for monitoring and diagnostics.
 */
@Component
@ConditionalOnClass(HealthIndicator.class)
public class VisualizerHealthIndicator implements HealthIndicator {
    
    private final QueryLogger queryLogger;
    private final HibernateStatementInspector statementInspector;
    
    public VisualizerHealthIndicator(QueryLogger queryLogger, HibernateStatementInspector statementInspector) {
        this.queryLogger = queryLogger;
        this.statementInspector = statementInspector;
    }
    
    @Override
    public Health health() {
        try {
            Health.Builder builder = Health.up();
            
            // Check query logger status
            builder.withDetail("queryLogger.enabled", queryLogger.isEnabled());
            builder.withDetail("queryLogger.name", queryLogger.getName());
            
            // Check statement inspector status
            builder.withDetail("statementInspector.enabled", statementInspector.isEnabled());
            builder.withDetail("statementInspector.name", statementInspector.getName());
            builder.withDetail("statementInspector.activeQueries", statementInspector.getActiveQueryCount());
            
            // Add performance statistics
            var performanceStats = statementInspector.getPerformanceStats();
            builder.withDetail("performance.totalQueries", performanceStats.getTotalQueries());
            builder.withDetail("performance.avgExecutionTime", performanceStats.getAvgExecutionTime());
            builder.withDetail("performance.slowQueries", performanceStats.getSlowQueries());
            builder.withDetail("performance.slowQueryRate", performanceStats.getSlowQueryRate());
            
            // Add sampling statistics
            var samplingStats = statementInspector.getSamplingStats();
            builder.withDetail("sampling.totalQueries", samplingStats.getTotalQueries());
            builder.withDetail("sampling.sampledQueries", samplingStats.getSampledQueries());
            builder.withDetail("sampling.actualRate", samplingStats.getActualRate());
            
            // Add entity statistics
            var entityStats = statementInspector.getEntityStats();
            builder.withDetail("entities.tracked", entityStats.size());
            
            // Add query type statistics
            var queryTypeStats = statementInspector.getQueryTypeStats();
            builder.withDetail("queryTypes.tracked", queryTypeStats.size());
            
            // Add recent slow queries count
            var recentSlowQueries = statementInspector.getRecentSlowQueries();
            builder.withDetail("recentSlowQueries.count", recentSlowQueries.size());
            
            // Check for any issues
            if (performanceStats.getSlowQueryRate() > 0.5) {
                builder.withDetail("warning", "High slow query rate detected: " + 
                                 String.format("%.2f%%", performanceStats.getSlowQueryRate() * 100));
            }
            
            if (statementInspector.getActiveQueryCount() > 100) {
                builder.withDetail("warning", "High number of active queries: " + 
                                 statementInspector.getActiveQueryCount());
            }
            
            return builder.build();
            
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("errorType", e.getClass().getSimpleName())
                    .build();
        }
    }
}
