package com.springjpa.visualizer.config;

import com.springjpa.visualizer.interceptor.HibernateStatementInspector;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Micrometer metrics contributor for Spring JPA Query Visualizer.
 * Exposes performance metrics for monitoring and alerting.
 */
@Component
@ConditionalOnClass(MeterRegistry.class)
public class VisualizerMetricsContributor {
    
    private final MeterRegistry meterRegistry;
    private final HibernateStatementInspector statementInspector;
    
    // Metrics
    private Counter totalQueriesCounter;
    private Counter slowQueriesCounter;
    private Timer queryExecutionTimer;
    private AtomicLong activeQueriesGauge;
    private AtomicLong entityCountGauge;
    private AtomicLong queryTypeCountGauge;
    
    public VisualizerMetricsContributor(MeterRegistry meterRegistry, 
                                      HibernateStatementInspector statementInspector) {
        this.meterRegistry = meterRegistry;
        this.statementInspector = statementInspector;
    }
    
    @PostConstruct
    public void initializeMetrics() {
        // Initialize counters
        totalQueriesCounter = Counter.builder("spring.jpa.visualizer.queries.total")
                .description("Total number of SQL queries processed")
                .register(meterRegistry);
        
        slowQueriesCounter = Counter.builder("spring.jpa.visualizer.queries.slow")
                .description("Number of slow SQL queries detected")
                .register(meterRegistry);
        
        // Initialize timer
        queryExecutionTimer = Timer.builder("spring.jpa.visualizer.queries.execution.time")
                .description("SQL query execution time")
                .register(meterRegistry);
        
        // Initialize gauges
        activeQueriesGauge = new AtomicLong(0);
        Gauge.builder("spring.jpa.visualizer.queries.active")
                .description("Number of currently active queries")
                .register(meterRegistry, activeQueriesGauge, AtomicLong::get);
        
        entityCountGauge = new AtomicLong(0);
        Gauge.builder("spring.jpa.visualizer.entities.count")
                .description("Number of tracked entities")
                .register(meterRegistry, entityCountGauge, AtomicLong::get);
        
        queryTypeCountGauge = new AtomicLong(0);
        Gauge.builder("spring.jpa.visualizer.query.types.count")
                .description("Number of tracked query types")
                .register(meterRegistry, queryTypeCountGauge, AtomicLong::get);
        
        // Initialize performance metrics gauges
        Gauge.builder("spring.jpa.visualizer.performance.avg.execution.time")
                .description("Average SQL query execution time")
                .register(meterRegistry, this, VisualizerMetricsContributor::getAvgExecutionTime);
        
        Gauge.builder("spring.jpa.visualizer.performance.slow.query.rate")
                .description("Rate of slow queries")
                .register(meterRegistry, this, VisualizerMetricsContributor::getSlowQueryRate);
        
        Gauge.builder("spring.jpa.visualizer.sampling.rate")
                .description("Actual query sampling rate")
                .register(meterRegistry, this, VisualizerMetricsContributor::getSamplingRate);
        
        Gauge.builder("spring.jpa.visualizer.recent.slow.queries.count")
                .description("Number of recent slow queries")
                .register(meterRegistry, this, VisualizerMetricsContributor::getRecentSlowQueriesCount);
    }
    
    /**
     * Record a query execution
     */
    public void recordQuery(long executionTimeMs, boolean isSlowQuery) {
        totalQueriesCounter.increment();
        
        if (isSlowQuery) {
            slowQueriesCounter.increment();
        }
        
        queryExecutionTimer.record(executionTimeMs, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
    
    /**
     * Update active queries count
     */
    public void updateActiveQueriesCount(long count) {
        activeQueriesGauge.set(count);
    }
    
    /**
     * Update entity count
     */
    public void updateEntityCount(long count) {
        entityCountGauge.set(count);
    }
    
    /**
     * Update query type count
     */
    public void updateQueryTypeCount(long count) {
        queryTypeCountGauge.set(count);
    }
    
    // Gauge value providers
    private double getAvgExecutionTime() {
        try {
            return statementInspector.getPerformanceStats().getAvgExecutionTime();
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double getSlowQueryRate() {
        try {
            return statementInspector.getPerformanceStats().getSlowQueryRate();
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double getSamplingRate() {
        try {
            return statementInspector.getSamplingStats().getActualRate();
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double getRecentSlowQueriesCount() {
        try {
            return statementInspector.getRecentSlowQueries().size();
        } catch (Exception e) {
            return 0.0;
        }
    }
}
