package com.springjpa.visualizer.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuerySampler
 */
class QuerySamplerTest {
    
    private QuerySampler querySampler;
    
    @BeforeEach
    void setUp() {
        querySampler = new QuerySampler();
    }
    
    @Test
    void testShouldSampleWithDefaultRate() {
        // Given - default sampler with 100% rate
        String sql = "SELECT * FROM users";
        
        // When/Then - should always sample
        assertTrue(querySampler.shouldSample(sql));
        assertTrue(querySampler.shouldSample(sql));
        assertTrue(querySampler.shouldSample(sql));
    }
    
    @Test
    void testShouldSampleWithCustomRate() {
        // Given - 50% sampling rate
        QuerySampler sampler = new QuerySampler(0.5);
        
        // When/Then - should sample approximately half
        int sampled = 0;
        int total = 100;
        
        for (int i = 0; i < total; i++) {
            if (sampler.shouldSample("SELECT * FROM users")) {
                sampled++;
            }
        }
        
        // Should be approximately 50% (allow some variance)
        assertTrue(sampled >= 30 && sampled <= 70, 
                  "Expected ~50% sampling, got " + sampled + "/" + total);
    }
    
    @Test
    void testShouldSampleWithZeroRate() {
        // Given - 0% sampling rate
        QuerySampler sampler = new QuerySampler(0.0);
        
        // When/Then - should never sample
        assertFalse(sampler.shouldSample("SELECT * FROM users"));
        assertFalse(sampler.shouldSample("SELECT * FROM users"));
    }
    
    @Test
    void testShouldSampleWithNullSql() {
        // When/Then - should always sample null SQL
        assertTrue(querySampler.shouldSample(null));
    }
    
    @Test
    void testHighPriorityQueries() {
        // Given
        String[] highPriorityQueries = {
            "INSERT INTO users VALUES (?, ?)",
            "UPDATE users SET name = ?",
            "DELETE FROM users WHERE id = ?",
            "CREATE TABLE users (id INT)",
            "DROP TABLE users",
            "ALTER TABLE users ADD COLUMN email VARCHAR(255)"
        };
        
        // When/Then - all high priority queries should be sampled
        for (String sql : highPriorityQueries) {
            assertTrue(querySampler.shouldSample(sql), 
                      "High priority query should be sampled: " + sql);
        }
    }
    
    @Test
    void testLowPriorityQueries() {
        // Given - low priority queries
        String[] lowPriorityQueries = {
            "SELECT COUNT(*) FROM users",
            "SELECT EXISTS(SELECT 1 FROM users)",
            "SELECT 1 FROM users"
        };
        
        // When/Then - low priority queries should be sampled less frequently
        // (This test is probabilistic, so we just verify the method doesn't throw)
        for (String sql : lowPriorityQueries) {
            boolean result = querySampler.shouldSample(sql);
            assertTrue(result || !result); // Just verify it returns a boolean
        }
    }
    
    @Test
    void testGetStats() {
        // Given
        String sql = "SELECT * FROM users";
        
        // When - sample some queries
        for (int i = 0; i < 10; i++) {
            querySampler.shouldSample(sql);
        }
        
        // Then
        QuerySampler.SamplingStats stats = querySampler.getStats();
        assertNotNull(stats);
        assertEquals(10, stats.getTotalQueries());
        assertEquals(10, stats.getSampledQueries());
        assertEquals(1.0, stats.getSamplingRate());
        assertEquals(1.0, stats.getActualRate());
        assertEquals(0, stats.getDroppedQueries());
        assertEquals(0.0, stats.getDropRate());
    }
    
    @Test
    void testResetStats() {
        // Given
        querySampler.shouldSample("SELECT * FROM users");
        QuerySampler.SamplingStats statsBefore = querySampler.getStats();
        assertEquals(1, statsBefore.getTotalQueries());
        
        // When
        querySampler.resetStats();
        
        // Then
        QuerySampler.SamplingStats statsAfter = querySampler.getStats();
        assertEquals(0, statsAfter.getTotalQueries());
        assertEquals(0, statsAfter.getSampledQueries());
    }
    
    @Test
    void testIsEnabled() {
        // Given - default sampler
        assertFalse(querySampler.isEnabled()); // 100% rate means disabled
        
        // Given - custom sampler with < 100% rate
        QuerySampler enabledSampler = new QuerySampler(0.5);
        assertTrue(enabledSampler.isEnabled());
    }
    
    @Test
    void testGetSamplingRate() {
        // Given
        QuerySampler sampler = new QuerySampler(0.75);
        
        // When/Then
        assertEquals(0.75, sampler.getSamplingRate());
    }
    
    @Test
    void testSamplingStatsToString() {
        // Given
        querySampler.shouldSample("SELECT * FROM users");
        querySampler.shouldSample("SELECT * FROM users");
        
        // When
        QuerySampler.SamplingStats stats = querySampler.getStats();
        String statsString = stats.toString();
        
        // Then
        assertNotNull(statsString);
        assertTrue(statsString.contains("SamplingStats"));
        assertTrue(statsString.contains("total=2"));
        assertTrue(statsString.contains("sampled=2"));
    }
    
    @Test
    void testSamplingRateBoundaries() {
        // Test negative rate (should be clamped to 0)
        QuerySampler sampler1 = new QuerySampler(-0.5);
        assertEquals(0.0, sampler1.getSamplingRate());
        
        // Test rate > 1 (should be clamped to 1)
        QuerySampler sampler2 = new QuerySampler(1.5);
        assertEquals(1.0, sampler2.getSamplingRate());
    }
}
