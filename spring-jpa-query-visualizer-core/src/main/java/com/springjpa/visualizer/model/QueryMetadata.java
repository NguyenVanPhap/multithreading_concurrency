package com.springjpa.visualizer.model;

import java.util.Map;
import java.util.Objects;

/**
 * Contains metadata about entity-to-table mapping and query structure.
 * Provides information about how JPA entities map to database tables and columns.
 */
public class QueryMetadata {
    
    private String entityName;
    private String tableName;
    private Map<String, String> fieldToColumnMapping;
    private Map<String, String> columnToFieldMapping;
    private String primaryKeyColumn;
    private String primaryKeyField;
    private Map<String, Object> queryParameters;
    private String queryType; // SELECT, INSERT, UPDATE, DELETE
    private boolean isNativeQuery;
    private String originalQuery;

    public QueryMetadata() {
    }

    public QueryMetadata(String entityName, String tableName) {
        this.entityName = entityName;
        this.tableName = tableName;
    }

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

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map<String, Object> queryParameters) {
        this.queryParameters = queryParameters;
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

    /**
     * Get column name for a given entity field
     */
    public String getColumnName(String fieldName) {
        if (fieldToColumnMapping == null) {
            return fieldName; // Default to field name if no mapping
        }
        return fieldToColumnMapping.getOrDefault(fieldName, fieldName);
    }

    /**
     * Get field name for a given column name
     */
    public String getFieldName(String columnName) {
        if (columnToFieldMapping == null) {
            return columnName; // Default to column name if no mapping
        }
        return columnToFieldMapping.getOrDefault(columnName, columnName);
    }

    /**
     * Check if this is a SELECT query
     */
    public boolean isSelectQuery() {
        return "SELECT".equalsIgnoreCase(queryType);
    }

    /**
     * Check if this is an INSERT query
     */
    public boolean isInsertQuery() {
        return "INSERT".equalsIgnoreCase(queryType);
    }

    /**
     * Check if this is an UPDATE query
     */
    public boolean isUpdateQuery() {
        return "UPDATE".equalsIgnoreCase(queryType);
    }

    /**
     * Check if this is a DELETE query
     */
    public boolean isDeleteQuery() {
        return "DELETE".equalsIgnoreCase(queryType);
    }

    /**
     * Get display name for the query type
     */
    public String getQueryTypeDisplayName() {
        if (queryType == null) {
            return "UNKNOWN";
        }
        return queryType.toUpperCase();
    }

    /**
     * Check if this metadata has complete mapping information
     */
    public boolean hasCompleteMapping() {
        return fieldToColumnMapping != null && 
               columnToFieldMapping != null && 
               !fieldToColumnMapping.isEmpty() && 
               !columnToFieldMapping.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryMetadata that = (QueryMetadata) o;
        return Objects.equals(entityName, that.entityName) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(queryType, that.queryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityName, tableName, queryType);
    }

    @Override
    public String toString() {
        return "QueryMetadata{" +
                "entityName='" + entityName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", queryType='" + queryType + '\'' +
                ", isNativeQuery=" + isNativeQuery +
                '}';
    }
}
