package com.springjpa.visualizer.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PerformanceMonitor
 */
class PerformanceMonitorTest {
    
    private PerformanceMonitor performanceMonitor;
    
    @BeforeEach
    void setUp() {
        performanceMonitor = new PerformanceMonitor(1000, true); // 1 second threshold
    }
    
    @Test
    void testRecordQuery() {
        // Given
        String sql = "SELECT * FROM users";
        String entityName = "User";
        String queryType = "SELECT";
        long executionTime = 500L;
        
        // When
        performanceMonitor.recordQuery(sql, entityName, queryType, executionTime);
        
        // Then
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        assertEquals(1, stats.getTotalQueries());
        assertEquals(0, stats.getSlowQueries());
        assertEquals(500, stats.getTotalExecutionTime());
        assertEquals(500.0, stats.getAvgExecutionTime());
        assertEquals(0.0, stats.getSlowQueryRate());
        assertEquals(500, stats.getMinExecutionTime());
        assertEquals(500, stats.getMaxExecutionTime());
    }
    
    @Test
    void testRecordSlowQuery() {
        // Given
        String sql = "SELECT * FROM users";
        String entityName = "User";
        String queryType = "SELECT";
        long executionTime = 1500L; // Above 1000ms threshold
        
        // When
        performanceMonitor.recordQuery(sql, entityName, queryType, executionTime);
        
        // Then
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        assertEquals(1, stats.getTotalQueries());
        assertEquals(1, stats.getSlowQueries());
        assertEquals(1500, stats.getTotalExecutionTime());
        assertEquals(1500.0, stats.getAvgExecutionTime());
        assertEquals(1.0, stats.getSlowQueryRate());
        
        // Check slow queries
        Map<String, PerformanceMonitor.SlowQueryInfo> slowQueries = performanceMonitor.getRecentSlowQueries();
        assertEquals(1, slowQueries.size());
        
        PerformanceMonitor.SlowQueryInfo slowQuery = slowQueries.values().iterator().next();
        assertEquals(sql, slowQuery.getSql());
        assertEquals(entityName, slowQuery.getEntityName());
        assertEquals(queryType, slowQuery.getQueryType());
        assertEquals(1500L, slowQuery.getExecutionTimeMs());
        assertNotNull(slowQuery.getTimestamp());
    }
    
    @Test
    void testRecordMultipleQueries() {
        // Given
        String[] queries = {
            "SELECT * FROM users",
            "INSERT INTO users VALUES (?, ?)",
            "UPDATE users SET name = ?",
            "DELETE FROM users WHERE id = ?"
        };
        
        String[] entities = {"User", "User", "User", "User"};
        String[] types = {"SELECT", "INSERT", "UPDATE", "DELETE"};
        long[] times = {100L, 200L, 300L, 400L};
        
        // When
        for (int i = 0; i < queries.length; i++) {
            performanceMonitor.recordQuery(queries[i], entities[i], types[i], times[i]);
        }
        
        // Then
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        assertEquals(4, stats.getTotalQueries());
        assertEquals(0, stats.getSlowQueries());
        assertEquals(1000, stats.getTotalExecutionTime());
        assertEquals(250.0, stats.getAvgExecutionTime());
        assertEquals(100, stats.getMinExecutionTime());
        assertEquals(400, stats.getMaxExecutionTime());
    }
    
    @Test
    void testQueryTypeStats() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 100L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 200L);
        performanceMonitor.recordQuery("INSERT INTO users", "User", "INSERT", 300L);
        
        // When
        Map<String, PerformanceMonitor.QueryTypeStats> typeStats = performanceMonitor.getQueryTypeStats();
        
        // Then
        assertEquals(2, typeStats.size());
        
        PerformanceMonitor.QueryTypeStats selectStats = typeStats.get("SELECT");
        assertNotNull(selectStats);
        assertEquals(2, selectStats.getCount());
        assertEquals(300, selectStats.getTotalTime());
        assertEquals(150.0, selectStats.getAvgTime());
        assertEquals(100, selectStats.getMinTime());
        assertEquals(200, selectStats.getMaxTime());
        
        PerformanceMonitor.QueryTypeStats insertStats = typeStats.get("INSERT");
        assertNotNull(insertStats);
        assertEquals(1, insertStats.getCount());
        assertEquals(300, insertStats.getTotalTime());
        assertEquals(300.0, insertStats.getAvgTime());
    }
    
    @Test
    void testEntityStats() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 100L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 200L);
        performanceMonitor.recordQuery("SELECT * FROM orders", "Order", "SELECT", 300L);
        
        // When
        Map<String, PerformanceMonitor.EntityStats> entityStats = performanceMonitor.getEntityStats();
        
        // Then
        assertEquals(2, entityStats.size());
        
        PerformanceMonitor.EntityStats userStats = entityStats.get("User");
        assertNotNull(userStats);
        assertEquals(2, userStats.getCount());
        assertEquals(300, userStats.getTotalTime());
        assertEquals(150.0, userStats.getAvgTime());
        
        PerformanceMonitor.EntityStats orderStats = entityStats.get("Order");
        assertNotNull(orderStats);
        assertEquals(1, orderStats.getCount());
        assertEquals(300, orderStats.getTotalTime());
    }
    
    @Test
    void testRecordQueryWithNullValues() {
        // Given
        String sql = null;
        String entityName = null;
        String queryType = null;
        long executionTime = 500L;
        
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> {
            performanceMonitor.recordQuery(sql, entityName, queryType, executionTime);
        });
        
        // Then
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        assertEquals(1, stats.getTotalQueries());
    }
    
    @Test
    void testResetStats() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 100L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 1500L); // Slow query
        
        PerformanceMonitor.PerformanceStats statsBefore = performanceMonitor.getOverallStats();
        assertEquals(2, statsBefore.getTotalQueries());
        assertEquals(1, statsBefore.getSlowQueries());
        
        // When
        performanceMonitor.resetStats();
        
        // Then
        PerformanceMonitor.PerformanceStats statsAfter = performanceMonitor.getOverallStats();
        assertEquals(0, statsAfter.getTotalQueries());
        assertEquals(0, statsAfter.getSlowQueries());
        assertEquals(0, statsAfter.getTotalExecutionTime());
        assertEquals(0.0, statsAfter.getAvgExecutionTime());
        
        Map<String, PerformanceMonitor.QueryTypeStats> typeStats = performanceMonitor.getQueryTypeStats();
        assertTrue(typeStats.isEmpty());
        
        Map<String, PerformanceMonitor.EntityStats> entityStats = performanceMonitor.getEntityStats();
        assertTrue(entityStats.isEmpty());
        
        Map<String, PerformanceMonitor.SlowQueryInfo> slowQueries = performanceMonitor.getRecentSlowQueries();
        assertTrue(slowQueries.isEmpty());
    }
    
    @Test
    void testIsEnabled() {
        // Given - enabled monitor
        assertTrue(performanceMonitor.isEnabled());
        
        // Given - disabled monitor
        PerformanceMonitor disabledMonitor = new PerformanceMonitor(1000, false);
        assertFalse(disabledMonitor.isEnabled());
    }
    
    @Test
    void testGetSlowQueryThreshold() {
        // Given
        PerformanceMonitor monitor = new PerformanceMonitor(2000, true);
        
        // When/Then
        assertEquals(2000, monitor.getSlowQueryThresholdMs());
    }
    
    @Test
    void testPerformanceStatsToString() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 100L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 200L);
        
        // When
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        String statsString = stats.toString();
        
        // Then
        assertNotNull(statsString);
        assertTrue(statsString.contains("PerformanceStats"));
        assertTrue(statsString.contains("total=2"));
        assertTrue(statsString.contains("avg=150.00ms"));
    }
    
    @Test
    void testSlowQueryInfoToString() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 1500L);
        
        // When
        Map<String, PerformanceMonitor.SlowQueryInfo> slowQueries = performanceMonitor.getRecentSlowQueries();
        PerformanceMonitor.SlowQueryInfo slowQuery = slowQueries.values().iterator().next();
        String slowQueryString = slowQuery.toString();
        
        // Then
        assertNotNull(slowQueryString);
        assertTrue(slowQueryString.contains("SlowQueryInfo"));
        assertTrue(slowQueryString.contains("SELECT * FROM users"));
        assertTrue(slowQueryString.contains("User"));
        assertTrue(slowQueryString.contains("SELECT"));
        assertTrue(slowQueryString.contains("1500ms"));
    }
    
    @Test
    void testMinMaxExecutionTime() {
        // Given
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 100L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 500L);
        performanceMonitor.recordQuery("SELECT * FROM users", "User", "SELECT", 200L);
        
        // When
        PerformanceMonitor.PerformanceStats stats = performanceMonitor.getOverallStats();
        
        // Then
        assertEquals(100, stats.getMinExecutionTime());
        assertEquals(500, stats.getMaxExecutionTime());
    }
}
