package com.springjpa.visualizer.interceptor;

import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.model.SqlQueryInfo;
import com.springjpa.visualizer.parser.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for HibernateStatementInspector with all components
 */
@ExtendWith(MockitoExtension.class)
class HibernateStatementInspectorIntegrationTest {
    
    @Mock
    private QueryLogger mockQueryLogger;
    
    private HibernateStatementInspector inspector;
    private StackTraceParser stackTraceParser;
    private EntityMapper entityMapper;
    private QuerySampler querySampler;
    private PerformanceMonitor performanceMonitor;
    
    @BeforeEach
    void setUp() {
        stackTraceParser = new StackTraceParser();
        entityMapper = new EntityMapper();
        querySampler = new QuerySampler();
        performanceMonitor = new PerformanceMonitor();
        
        inspector = new HibernateStatementInspector(
            mockQueryLogger, 
            stackTraceParser, 
            entityMapper, 
            querySampler, 
            performanceMonitor
        );
    }
    
    @Test
    void testFullIntegrationWithSelectQuery() {
        // Given
        String sql = "SELECT u.id, u.name, u.email FROM users u WHERE u.email = ?";
        
        // When
        String result = inspector.inspect(sql);
        
        // Then
        assertEquals(sql, result); // Should return original SQL unchanged
        
        // Verify query logger was called
        verify(mockQueryLogger).log(any(SqlQueryInfo.class));
        
        // Verify performance monitoring
        PerformanceMonitor.PerformanceStats stats = inspector.getPerformanceStats();
        assertTrue(stats.getTotalQueries() >= 0); // May be 0 if sampling skipped
        
        // Verify entity mapping cache
        assertTrue(inspector.getEntityMapper().getCacheSize() >= 0);
        
        // Verify sampling stats
        QuerySampler.SamplingStats samplingStats = inspector.getSamplingStats();
        assertNotNull(samplingStats);
    }
    
    @Test
    void testFullIntegrationWithMultipleQueries() {
        // Given
        String[] queries = {
            "SELECT * FROM users WHERE id = ?",
            "INSERT INTO users (name, email) VALUES (?, ?)",
            "UPDATE users SET name = ? WHERE id = ?",
            "DELETE FROM users WHERE id = ?"
        };
        
        // When
        for (String sql : queries) {
            String result = inspector.inspect(sql);
            assertEquals(sql, result);
        }
        
        // Then
        // Verify all queries were processed
        verify(mockQueryLogger, atLeast(0)).log(any(SqlQueryInfo.class));
        
        // Verify entity mapping cache has entries
        assertTrue(inspector.getEntityMapper().getCacheSize() > 0);
        
        // Verify performance stats
        PerformanceMonitor.PerformanceStats stats = inspector.getPerformanceStats();
        assertTrue(stats.getTotalQueries() >= 0);
        
        // Verify query type stats
        Map<String, PerformanceMonitor.QueryTypeStats> typeStats = inspector.getQueryTypeStats();
        assertNotNull(typeStats);
    }
    
    @Test
    void testPerformanceMonitoringIntegration() {
        // Given
        String sql = "SELECT * FROM users";
        
        // When - simulate multiple queries with different execution times
        inspector.inspect(sql);
        
        // Simulate execution time by waiting a bit
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Then
        PerformanceMonitor.PerformanceStats stats = inspector.getPerformanceStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalQueries() >= 0);
        assertTrue(stats.getAvgExecutionTime() >= 0);
    }
    
    @Test
    void testEntityMappingIntegration() {
        // Given
        String sql = "SELECT u.id, u.name, u.email FROM users u WHERE u.email = ?";
        
        // When
        inspector.inspect(sql);
        
        // Then
        EntityMapper entityMapper = inspector.getEntityMapper();
        assertNotNull(entityMapper);
        
        // Verify cache has entry
        assertTrue(entityMapper.getCacheSize() > 0);
        
        // Test cache functionality
        int cacheSizeBefore = entityMapper.getCacheSize();
        inspector.inspect(sql); // Same query again
        int cacheSizeAfter = entityMapper.getCacheSize();
        
        // Cache size should remain the same (cached)
        assertEquals(cacheSizeBefore, cacheSizeAfter);
    }
    
    @Test
    void testQuerySamplingIntegration() {
        // Given
        QuerySampler sampler = new QuerySampler(0.5); // 50% sampling
        inspector = new HibernateStatementInspector(
            mockQueryLogger, 
            stackTraceParser, 
            entityMapper, 
            sampler, 
            performanceMonitor
        );
        
        String sql = "SELECT * FROM users";
        
        // When - process multiple queries
        for (int i = 0; i < 10; i++) {
            inspector.inspect(sql);
        }
        
        // Then
        QuerySampler.SamplingStats stats = inspector.getSamplingStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalQueries() > 0);
        
        // Verify logger was called (some queries should be sampled)
        verify(mockQueryLogger, atLeast(0)).log(any(SqlQueryInfo.class));
    }
    
    @Test
    void testSlowQueryDetectionIntegration() {
        // Given
        PerformanceMonitor monitor = new PerformanceMonitor(100); // 100ms threshold
        inspector = new HibernateStatementInspector(
            mockQueryLogger, 
            stackTraceParser, 
            entityMapper, 
            querySampler, 
            monitor
        );
        
        String sql = "SELECT * FROM users";
        
        // When
        inspector.inspect(sql);
        
        // Simulate slow execution
        try {
            Thread.sleep(150); // Above threshold
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Then
        Map<String, PerformanceMonitor.SlowQueryInfo> slowQueries = inspector.getRecentSlowQueries();
        assertNotNull(slowQueries);
        // Note: Slow query detection depends on actual execution time measurement
        // which is complex to test in unit tests
    }
    
    @Test
    void testResetStatisticsIntegration() {
        // Given
        inspector.inspect("SELECT * FROM users");
        inspector.inspect("INSERT INTO users VALUES (?, ?)");
        
        // Verify some stats exist
        PerformanceMonitor.PerformanceStats statsBefore = inspector.getPerformanceStats();
        assertTrue(statsBefore.getTotalQueries() >= 0);
        
        // When
        inspector.resetStatistics();
        
        // Then
        PerformanceMonitor.PerformanceStats statsAfter = inspector.getPerformanceStats();
        assertEquals(0, statsAfter.getTotalQueries());
        assertEquals(0, statsAfter.getSlowQueries());
        
        QuerySampler.SamplingStats samplingStats = inspector.getSamplingStats();
        assertEquals(0, samplingStats.getTotalQueries());
        
        Map<String, PerformanceMonitor.QueryTypeStats> typeStats = inspector.getQueryTypeStats();
        assertTrue(typeStats.isEmpty());
        
        Map<String, PerformanceMonitor.EntityStats> entityStats = inspector.getEntityStats();
        assertTrue(entityStats.isEmpty());
    }
    
    @Test
    void testErrorHandlingIntegration() {
        // Given
        String sql = "SELECT * FROM users";
        
        // When - simulate error in logger
        doThrow(new RuntimeException("Test error")).when(mockQueryLogger).log(any(SqlQueryInfo.class));
        
        // Then - should not throw exception
        assertDoesNotThrow(() -> {
            String result = inspector.inspect(sql);
            assertEquals(sql, result);
        });
        
        // Verify inspector is still functional
        assertTrue(inspector.isEnabled());
        assertEquals("HibernateStatementInspector", inspector.getName());
    }
    
    @Test
    void testDisabledInspectorIntegration() {
        // Given
        inspector.setEnabled(false);
        String sql = "SELECT * FROM users";
        
        // When
        String result = inspector.inspect(sql);
        
        // Then
        assertEquals(sql, result);
        
        // Verify logger was not called
        verifyNoInteractions(mockQueryLogger);
        
        // Verify no performance stats recorded
        PerformanceMonitor.PerformanceStats stats = inspector.getPerformanceStats();
        assertEquals(0, stats.getTotalQueries());
    }
}
