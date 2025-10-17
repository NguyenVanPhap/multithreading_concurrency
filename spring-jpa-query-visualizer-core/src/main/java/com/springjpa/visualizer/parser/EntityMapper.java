package com.springjpa.visualizer.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maps JPA entity information to database table and column information.
 * Extracts entity-to-table mapping details from class names, annotations, and SQL queries.
 */
public class EntityMapper {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityMapper.class);
    
    // Cache for entity mappings to avoid repeated processing
    private final Map<String, EntityMappingInfo> entityCache;
    
    // Patterns for common entity naming conventions
    private static final Pattern ENTITY_CLASS_PATTERN = Pattern.compile(
        ".*\\.(\\w+)(?:Entity)?$", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile(
        "FROM\\s+(?:\\w+\\.)?(\\w+)\\s+", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern COLUMN_PATTERN = Pattern.compile(
        "\\b(\\w+)\\s*=", 
        Pattern.CASE_INSENSITIVE
    );
    
    public EntityMapper() {
        this.entityCache = new HashMap<>();
    }
    
    /**
     * Extract entity mapping information from SQL query and context
     */
    public EntityMappingInfo extractEntityMapping(String sql, String entityName, String className) {
        String cacheKey = generateCacheKey(sql, entityName, className);
        
        return entityCache.computeIfAbsent(cacheKey, key -> {
            EntityMappingInfo mapping = new EntityMappingInfo();
            
            try {
                // Extract table name from SQL
                String tableName = extractTableName(sql);
                mapping.setTableName(tableName);
                
                // Extract entity name
                mapping.setEntityName(entityName != null ? entityName : extractEntityNameFromClass(className));
                
                // Extract field-to-column mappings
                Map<String, String> fieldMappings = extractFieldMappings(sql, entityName);
                mapping.setFieldToColumnMapping(fieldMappings);
                
                // Create reverse mapping
                Map<String, String> columnMappings = createReverseMapping(fieldMappings);
                mapping.setColumnToFieldMapping(columnMappings);
                
                // Determine query type
                String queryType = determineQueryType(sql);
                mapping.setQueryType(queryType);
                
                // Check if it's a native query
                boolean isNative = isNativeQuery(sql);
                mapping.setNativeQuery(isNative);
                
                // Set original query
                mapping.setOriginalQuery(sql);
                
                logger.debug("Extracted entity mapping: {} -> {}", mapping.getEntityName(), mapping.getTableName());
                
            } catch (Exception e) {
                logger.warn("Failed to extract entity mapping: {}", e.getMessage());
                // Return basic mapping with defaults
                mapping.setEntityName(entityName);
                mapping.setTableName(entityName != null ? entityName.toLowerCase() : "unknown");
                mapping.setQueryType("UNKNOWN");
            }
            
            return mapping;
        });
    }
    
    /**
     * Extract table name from SQL query
     */
    private String extractTableName(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "unknown";
        }
        
        String upperSql = sql.toUpperCase();
        
        // Try different patterns based on query type
        if (upperSql.startsWith("SELECT")) {
            return extractTableFromSelect(sql);
        } else if (upperSql.startsWith("INSERT")) {
            return extractTableFromInsert(sql);
        } else if (upperSql.startsWith("UPDATE")) {
            return extractTableFromUpdate(sql);
        } else if (upperSql.startsWith("DELETE")) {
            return extractTableFromDelete(sql);
        }
        
        // Fallback: try generic pattern
        Matcher matcher = TABLE_NAME_PATTERN.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return "unknown";
    }
    
    /**
     * Extract table name from SELECT query
     */
    private String extractTableFromSelect(String sql) {
        // Pattern: SELECT ... FROM table_name
        Pattern pattern = Pattern.compile("FROM\\s+(?:\\w+\\.)?(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }
    
    /**
     * Extract table name from INSERT query
     */
    private String extractTableFromInsert(String sql) {
        // Pattern: INSERT INTO table_name
        Pattern pattern = Pattern.compile("INSERT\\s+INTO\\s+(?:\\w+\\.)?(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }
    
    /**
     * Extract table name from UPDATE query
     */
    private String extractTableFromUpdate(String sql) {
        // Pattern: UPDATE table_name
        Pattern pattern = Pattern.compile("UPDATE\\s+(?:\\w+\\.)?(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }
    
    /**
     * Extract table name from DELETE query
     */
    private String extractTableFromDelete(String sql) {
        // Pattern: DELETE FROM table_name
        Pattern pattern = Pattern.compile("DELETE\\s+FROM\\s+(?:\\w+\\.)?(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }
    
    /**
     * Extract entity name from class name
     */
    private String extractEntityNameFromClass(String className) {
        if (className == null) {
            return "unknown";
        }
        
        Matcher matcher = ENTITY_CLASS_PATTERN.matcher(className);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // Fallback: use last part of class name
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            return className.substring(lastDotIndex + 1);
        }
        
        return className;
    }
    
    /**
     * Extract field-to-column mappings from SQL
     */
    private Map<String, String> extractFieldMappings(String sql, String entityName) {
        Map<String, String> mappings = new HashMap<>();
        
        if (sql == null || sql.trim().isEmpty()) {
            return mappings;
        }
        
        try {
            // For SELECT queries, extract column mappings
            if (sql.toUpperCase().startsWith("SELECT")) {
                extractSelectMappings(sql, mappings);
            }
            
            // For INSERT/UPDATE queries, extract field mappings
            if (sql.toUpperCase().contains("INSERT") || sql.toUpperCase().contains("UPDATE")) {
                extractInsertUpdateMappings(sql, mappings);
            }
            
            // Add common JPA mappings if not found
            addCommonJpaMappings(mappings, entityName);
            
        } catch (Exception e) {
            logger.warn("Failed to extract field mappings: {}", e.getMessage());
        }
        
        return mappings;
    }
    
    /**
     * Extract mappings from SELECT query
     */
    private void extractSelectMappings(String sql, Map<String, String> mappings) {
        // Pattern: SELECT alias.column AS field
        Pattern pattern = Pattern.compile("SELECT\\s+(?:\\w+\\.)?(\\w+)\\s+(?:AS\\s+)?(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        
        while (matcher.find()) {
            String column = matcher.group(1);
            String field = matcher.group(2);
            mappings.put(field, column);
        }
    }
    
    /**
     * Extract mappings from INSERT/UPDATE query
     */
    private void extractInsertUpdateMappings(String sql, Map<String, String> mappings) {
        // Pattern: SET field = value
        Pattern pattern = Pattern.compile("SET\\s+(\\w+)\\s*=", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        
        while (matcher.find()) {
            String field = matcher.group(1);
            // Assume column name is same as field name (common JPA convention)
            mappings.put(field, field);
        }
    }
    
    /**
     * Add common JPA field mappings
     */
    private void addCommonJpaMappings(Map<String, String> mappings, String entityName) {
        // Add common fields if not already present
        if (!mappings.containsKey("id")) {
            mappings.put("id", "id");
        }
        if (!mappings.containsKey("createdAt")) {
            mappings.put("createdAt", "created_at");
        }
        if (!mappings.containsKey("updatedAt")) {
            mappings.put("updatedAt", "updated_at");
        }
        if (!mappings.containsKey("version")) {
            mappings.put("version", "version");
        }
    }
    
    /**
     * Create reverse mapping (column -> field)
     */
    private Map<String, String> createReverseMapping(Map<String, String> fieldMappings) {
        Map<String, String> reverseMappings = new HashMap<>();
        
        for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
            reverseMappings.put(entry.getValue(), entry.getKey());
        }
        
        return reverseMappings;
    }
    
    /**
     * Determine query type from SQL
     */
    private String determineQueryType(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "UNKNOWN";
        }
        
        String upperSql = sql.toUpperCase().trim();
        
        if (upperSql.startsWith("SELECT")) {
            return "SELECT";
        } else if (upperSql.startsWith("INSERT")) {
            return "INSERT";
        } else if (upperSql.startsWith("UPDATE")) {
            return "UPDATE";
        } else if (upperSql.startsWith("DELETE")) {
            return "DELETE";
        } else if (upperSql.startsWith("CREATE")) {
            return "CREATE";
        } else if (upperSql.startsWith("DROP")) {
            return "DROP";
        } else if (upperSql.startsWith("ALTER")) {
            return "ALTER";
        }
        
        return "UNKNOWN";
    }
    
    /**
     * Check if query is a native query
     */
    private boolean isNativeQuery(String sql) {
        if (sql == null) {
            return false;
        }
        
        // Native queries often contain database-specific syntax
        String upperSql = sql.toUpperCase();
        
        return upperSql.contains("NATIVE") ||
               upperSql.contains("RAW") ||
               upperSql.contains("CUSTOM") ||
               upperSql.contains("FUNCTION") ||
               upperSql.contains("PROCEDURE");
    }
    
    /**
     * Generate cache key for entity mapping
     */
    private String generateCacheKey(String sql, String entityName, String className) {
        return String.format("%s_%s_%s", 
            sql != null ? sql.hashCode() : "null",
            entityName != null ? entityName : "null",
            className != null ? className : "null");
    }
    
    /**
     * Clear entity cache
     */
    public void clearCache() {
        entityCache.clear();
        logger.debug("Entity mapping cache cleared");
    }
    
    /**
     * Get cache size
     */
    public int getCacheSize() {
        return entityCache.size();
    }
    
    /**
     * Entity mapping information
     */
    public static class EntityMappingInfo {
        private String entityName;
        private String tableName;
        private Map<String, String> fieldToColumnMapping;
        private Map<String, String> columnToFieldMapping;
        private String queryType;
        private boolean isNativeQuery;
        private String originalQuery;
        
        // Getters and Setters
        public String getEntityName() {
            return entityName;
        }
        
        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }
        
        public String getTableName() {
            return tableName;
        }
        
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
        
        public Map<String, String> getFieldToColumnMapping() {
            return fieldToColumnMapping;
        }
        
        public void setFieldToColumnMapping(Map<String, String> fieldToColumnMapping) {
            this.fieldToColumnMapping = fieldToColumnMapping;
        }
        
        public Map<String, String> getColumnToFieldMapping() {
            return columnToFieldMapping;
        }
        
        public void setColumnToFieldMapping(Map<String, String> columnToFieldMapping) {
            this.columnToFieldMapping = columnToFieldMapping;
        }
        
        public String getQueryType() {
            return queryType;
        }
        
        public void setQueryType(String queryType) {
            this.queryType = queryType;
        }
        
        public boolean isNativeQuery() {
            return isNativeQuery;
        }
        
        public void setNativeQuery(boolean nativeQuery) {
            isNativeQuery = nativeQuery;
        }
        
        public String getOriginalQuery() {
            return originalQuery;
        }
        
        public void setOriginalQuery(String originalQuery) {
            this.originalQuery = originalQuery;
        }
        
        @Override
        public String toString() {
            return "EntityMappingInfo{" +
                    "entityName='" + entityName + '\'' +
                    ", tableName='" + tableName + '\'' +
                    ", queryType='" + queryType + '\'' +
                    ", isNativeQuery=" + isNativeQuery +
                    '}';
        }
    }
}
