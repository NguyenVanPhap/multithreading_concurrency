package com.springjpa.visualizer.interceptor;

import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.model.SqlQueryInfo;
import com.springjpa.visualizer.parser.StackTraceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HibernateStatementInspector
 */
@ExtendWith(MockitoExtension.class)
class HibernateStatementInspectorTest {
    
    @Mock
    private QueryLogger mockQueryLogger;
    
    @Mock
    private StackTraceParser mockStackTraceParser;
    
    private HibernateStatementInspector inspector;
    
    @BeforeEach
    void setUp() {
        inspector = new HibernateStatementInspector(mockQueryLogger, mockStackTraceParser);
    }
    
    @Test
    void testInspectWithEnabledInspector() {
        // Given
        String sql = "SELECT * FROM users WHERE id = ?";
        StackTraceParser.ContextInfo contextInfo = new StackTraceParser.ContextInfo();
        contextInfo.setMethodName("findById");
        contextInfo.setClassName("UserRepository");
        contextInfo.setEntityName("User");
        
        when(mockStackTraceParser.extractContext()).thenReturn(contextInfo);
        
        // When
        String result = inspector.inspect(sql);
        
        // Then
        assertEquals(sql, result); // Should return original SQL unchanged
        verify(mockStackTraceParser).extractContext();
        verify(mockQueryLogger).log(any(SqlQueryInfo.class));
    }
    
    @Test
    void testInspectWithDisabledInspector() {
        // Given
        inspector.setEnabled(false);
        String sql = "SELECT * FROM users WHERE id = ?";
        
        // When
        String result = inspector.inspect(sql);
        
        // Then
        assertEquals(sql, result);
        verifyNoInteractions(mockStackTraceParser);
        verifyNoInteractions(mockQueryLogger);
    }
    
    @Test
    void testInspectWithNullSql() {
        // When
        String result = inspector.inspect(null);
        
        // Then
        assertNull(result);
        verifyNoInteractions(mockStackTraceParser);
        verifyNoInteractions(mockQueryLogger);
    }
    
    @Test
    void testInspectWithEmptySql() {
        // When
        String result = inspector.inspect("");
        
        // Then
        assertEquals("", result);
        verifyNoInteractions(mockStackTraceParser);
        verifyNoInteractions(mockQueryLogger);
    }
    
    @Test
    void testInspectWithWhitespaceSql() {
        // When
        String result = inspector.inspect("   ");
        
        // Then
        assertEquals("   ", result);
        verifyNoInteractions(mockStackTraceParser);
        verifyNoInteractions(mockQueryLogger);
    }
    
    @Test
    void testInspectHandlesException() {
        // Given
        String sql = "SELECT * FROM users WHERE id = ?";
        when(mockStackTraceParser.extractContext()).thenThrow(new RuntimeException("Test exception"));
        
        // When
        String result = inspector.inspect(sql);
        
        // Then
        assertEquals(sql, result); // Should still return original SQL
        verify(mockStackTraceParser).extractContext();
        verifyNoInteractions(mockQueryLogger); // Should not log due to exception
    }
    
    @Test
    void testIsEnabled() {
        assertTrue(inspector.isEnabled());
        
        inspector.setEnabled(false);
        assertFalse(inspector.isEnabled());
        
        inspector.setEnabled(true);
        assertTrue(inspector.isEnabled());
    }
    
    @Test
    void testGetName() {
        assertEquals("HibernateStatementInspector", inspector.getName());
    }
    
    @Test
    void testGetActiveQueryCount() {
        assertEquals(0, inspector.getActiveQueryCount());
        
        // Simulate a query being processed
        String sql = "SELECT * FROM users";
        StackTraceParser.ContextInfo contextInfo = new StackTraceParser.ContextInfo();
        when(mockStackTraceParser.extractContext()).thenReturn(contextInfo);
        
        inspector.inspect(sql);
        
        // Query count should be 0 after processing completes
        assertEquals(0, inspector.getActiveQueryCount());
    }
    
    @Test
    void testClearPendingQueries() {
        inspector.clearPendingQueries();
        assertEquals(0, inspector.getActiveQueryCount());
    }
    
    @Test
    void testConstructorWithQueryLoggerOnly() {
        HibernateStatementInspector inspector2 = new HibernateStatementInspector(mockQueryLogger);
        
        assertNotNull(inspector2);
        assertTrue(inspector2.isEnabled());
        assertEquals("HibernateStatementInspector", inspector2.getName());
    }
}
