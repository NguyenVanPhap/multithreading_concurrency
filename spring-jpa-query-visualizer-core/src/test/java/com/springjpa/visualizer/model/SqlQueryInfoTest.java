package com.springjpa.visualizer.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SqlQueryInfo model
 */
class SqlQueryInfoTest {
    
    @Test
    void testDefaultConstructor() {
        SqlQueryInfo queryInfo = new SqlQueryInfo();
        
        assertNotNull(queryInfo.getTimestamp());
        assertNotNull(queryInfo.getThreadName());
        assertNotNull(queryInfo.getQueryId());
        assertEquals(Thread.currentThread().getName(), queryInfo.getThreadName());
    }
    
    @Test
    void testConstructorWithRequiredFields() {
        String sql = "SELECT * FROM users WHERE id = ?";
        String entityName = "User";
        String methodName = "findById";
        String className = "UserRepository";
        
        SqlQueryInfo queryInfo = new SqlQueryInfo(sql, entityName, methodName, className);
        
        assertEquals(sql, queryInfo.getSql());
        assertEquals(entityName, queryInfo.getEntityName());
        assertEquals(methodName, queryInfo.getMethodName());
        assertEquals(className, queryInfo.getClassName());
        assertNotNull(queryInfo.getQueryId());
        assertNotNull(queryInfo.getTimestamp());
    }
    
    @Test
    void testSettersAndGetters() {
        SqlQueryInfo queryInfo = new SqlQueryInfo();
        
        String sql = "SELECT * FROM users";
        String entityName = "User";
        String methodName = "findAll";
        String className = "UserRepository";
        long executionTime = 150L;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        String stackTrace = "com.example.UserRepository.findAll()";
        LocalDateTime timestamp = LocalDateTime.now();
        String sessionId = "session123";
        String threadName = "main";
        String queryId = "query123";
        
        queryInfo.setSql(sql);
        queryInfo.setEntityName(entityName);
        queryInfo.setMethodName(methodName);
        queryInfo.setClassName(className);
        queryInfo.setExecutionTimeMs(executionTime);
        queryInfo.setParameters(parameters);
        queryInfo.setStackTrace(stackTrace);
        queryInfo.setTimestamp(timestamp);
        queryInfo.setSessionId(sessionId);
        queryInfo.setThreadName(threadName);
        queryInfo.setQueryId(queryId);
        
        assertEquals(sql, queryInfo.getSql());
        assertEquals(entityName, queryInfo.getEntityName());
        assertEquals(methodName, queryInfo.getMethodName());
        assertEquals(className, queryInfo.getClassName());
        assertEquals(executionTime, queryInfo.getExecutionTimeMs());
        assertEquals(parameters, queryInfo.getParameters());
        assertEquals(stackTrace, queryInfo.getStackTrace());
        assertEquals(timestamp, queryInfo.getTimestamp());
        assertEquals(sessionId, queryInfo.getSessionId());
        assertEquals(threadName, queryInfo.getThreadName());
        assertEquals(queryId, queryInfo.getQueryId());
    }
    
    @Test
    void testIsSlowQuery() {
        SqlQueryInfo queryInfo = new SqlQueryInfo();
        
        queryInfo.setExecutionTimeMs(500L);
        assertFalse(queryInfo.isSlowQuery(1000L));
        assertTrue(queryInfo.isSlowQuery(300L));
        
        queryInfo.setExecutionTimeMs(1500L);
        assertTrue(queryInfo.isSlowQuery(1000L));
    }
    
    @Test
    void testGetFormattedExecutionTime() {
        SqlQueryInfo queryInfo = new SqlQueryInfo();
        
        queryInfo.setExecutionTimeMs(500L);
        assertEquals("500ms", queryInfo.getFormattedExecutionTime());
        
        queryInfo.setExecutionTimeMs(1500L);
        assertEquals("1.50s", queryInfo.getFormattedExecutionTime());
        
        queryInfo.setExecutionTimeMs(2000L);
        assertEquals("2.00s", queryInfo.getFormattedExecutionTime());
    }
    
    @Test
    void testEqualsAndHashCode() {
        SqlQueryInfo queryInfo1 = new SqlQueryInfo();
        queryInfo1.setQueryId("query123");
        
        SqlQueryInfo queryInfo2 = new SqlQueryInfo();
        queryInfo2.setQueryId("query123");
        
        SqlQueryInfo queryInfo3 = new SqlQueryInfo();
        queryInfo3.setQueryId("query456");
        
        assertEquals(queryInfo1, queryInfo2);
        assertNotEquals(queryInfo1, queryInfo3);
        assertEquals(queryInfo1.hashCode(), queryInfo2.hashCode());
        assertNotEquals(queryInfo1.hashCode(), queryInfo3.hashCode());
    }
    
    @Test
    void testToString() {
        SqlQueryInfo queryInfo = new SqlQueryInfo("SELECT * FROM users", "User", "findAll", "UserRepository");
        queryInfo.setExecutionTimeMs(100L);
        
        String toString = queryInfo.toString();
        
        assertTrue(toString.contains("SqlQueryInfo"));
        assertTrue(toString.contains("SELECT * FROM users"));
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("findAll"));
        assertTrue(toString.contains("UserRepository"));
        assertTrue(toString.contains("100"));
    }
}
