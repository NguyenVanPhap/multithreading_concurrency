package com.springjpa.visualizer.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EntityMapper
 */
class EntityMapperTest {
    
    private EntityMapper entityMapper;
    
    @BeforeEach
    void setUp() {
        entityMapper = new EntityMapper();
    }
    
    @Test
    void testExtractEntityMappingWithSelectQuery() {
        // Given
        String sql = "SELECT u.id, u.name, u.email FROM users u WHERE u.email = ?";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("User", mapping.getEntityName());
        assertEquals("users", mapping.getTableName());
        assertEquals("SELECT", mapping.getQueryType());
        assertFalse(mapping.isNativeQuery());
        assertEquals(sql, mapping.getOriginalQuery());
        
        Map<String, String> fieldMappings = mapping.getFieldToColumnMapping();
        assertNotNull(fieldMappings);
        assertTrue(fieldMappings.containsKey("id"));
        assertTrue(fieldMappings.containsKey("name"));
        assertTrue(fieldMappings.containsKey("email"));
    }
    
    @Test
    void testExtractEntityMappingWithInsertQuery() {
        // Given
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("User", mapping.getEntityName());
        assertEquals("users", mapping.getTableName());
        assertEquals("INSERT", mapping.getQueryType());
        assertFalse(mapping.isNativeQuery());
    }
    
    @Test
    void testExtractEntityMappingWithUpdateQuery() {
        // Given
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("User", mapping.getEntityName());
        assertEquals("users", mapping.getTableName());
        assertEquals("UPDATE", mapping.getQueryType());
    }
    
    @Test
    void testExtractEntityMappingWithDeleteQuery() {
        // Given
        String sql = "DELETE FROM users WHERE id = ?";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("User", mapping.getEntityName());
        assertEquals("users", mapping.getTableName());
        assertEquals("DELETE", mapping.getQueryType());
    }
    
    @Test
    void testExtractEntityMappingWithNullValues() {
        // Given
        String sql = "SELECT * FROM users";
        String entityName = null;
        String className = null;
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("unknown", mapping.getEntityName());
        assertEquals("users", mapping.getTableName());
        assertEquals("SELECT", mapping.getQueryType());
    }
    
    @Test
    void testExtractEntityMappingWithEmptySql() {
        // Given
        String sql = "";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertEquals("User", mapping.getEntityName());
        assertEquals("unknown", mapping.getTableName());
        assertEquals("UNKNOWN", mapping.getQueryType());
    }
    
    @Test
    void testExtractEntityMappingWithNativeQuery() {
        // Given
        String sql = "SELECT * FROM users WHERE NATIVE_FUNCTION(id) = ?";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        assertNotNull(mapping);
        assertTrue(mapping.isNativeQuery());
    }
    
    @Test
    void testExtractEntityNameFromClassName() {
        // Test with package
        String className1 = "com.example.UserRepository";
        EntityMapper.EntityMappingInfo mapping1 = entityMapper.extractEntityMapping("SELECT * FROM users", null, className1);
        assertEquals("UserRepository", mapping1.getEntityName());
        
        // Test with Entity suffix
        String className2 = "com.example.UserEntity";
        EntityMapper.EntityMappingInfo mapping2 = entityMapper.extractEntityMapping("SELECT * FROM users", null, className2);
        assertEquals("User", mapping2.getEntityName());
        
        // Test with simple class name
        String className3 = "User";
        EntityMapper.EntityMappingInfo mapping3 = entityMapper.extractEntityMapping("SELECT * FROM users", null, className3);
        assertEquals("User", mapping3.getEntityName());
    }
    
    @Test
    void testCacheFunctionality() {
        // Given
        String sql = "SELECT * FROM users";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When - extract same mapping twice
        EntityMapper.EntityMappingInfo mapping1 = entityMapper.extractEntityMapping(sql, entityName, className);
        EntityMapper.EntityMappingInfo mapping2 = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then - should be same instance (cached)
        assertSame(mapping1, mapping2);
        assertEquals(1, entityMapper.getCacheSize());
    }
    
    @Test
    void testClearCache() {
        // Given
        entityMapper.extractEntityMapping("SELECT * FROM users", "User", "com.example.UserRepository");
        assertEquals(1, entityMapper.getCacheSize());
        
        // When
        entityMapper.clearCache();
        
        // Then
        assertEquals(0, entityMapper.getCacheSize());
    }
    
    @Test
    void testFieldToColumnMapping() {
        // Given
        String sql = "SELECT u.id, u.name, u.email FROM users u";
        String entityName = "User";
        String className = "com.example.UserRepository";
        
        // When
        EntityMapper.EntityMappingInfo mapping = entityMapper.extractEntityMapping(sql, entityName, className);
        
        // Then
        Map<String, String> fieldMappings = mapping.getFieldToColumnMapping();
        assertNotNull(fieldMappings);
        
        // Check reverse mapping
        Map<String, String> columnMappings = mapping.getColumnToFieldMapping();
        assertNotNull(columnMappings);
        
        // Verify reverse mapping works
        for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
            assertEquals(entry.getKey(), columnMappings.get(entry.getValue()));
        }
    }
}
