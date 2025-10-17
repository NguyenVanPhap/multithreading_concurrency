package com.springjpa.visualizer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Model class representing SQL query information captured by the visualizer.
 * Contains all metadata about a SQL query execution including context, performance, and parameters.
 */
public class SqlQueryInfo {
    
    @JsonProperty("sql")
    private String sql;
    
    @JsonProperty("entityName")
    private String entityName;
    
    @JsonProperty("methodName")
    private String methodName;
    
    @JsonProperty("className")
    private String className;
    
    @JsonProperty("executionTimeMs")
    private long executionTimeMs;
    
    @JsonProperty("parameters")
    private Map<String, Object> parameters;
    
    @JsonProperty("stackTrace")
    private String stackTrace;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    
    @JsonProperty("sessionId")
    private String sessionId;
    
    @JsonProperty("threadName")
    private String threadName;
    
    @JsonProperty("queryId")
    private String queryId;

    // Default constructor for Jackson
    public SqlQueryInfo() {
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
    }

    // Constructor with required fields
    public SqlQueryInfo(String sql, String entityName, String methodName, String className) {
        this();
        this.sql = sql;
        this.entityName = entityName;
        this.methodName = methodName;
        this.className = className;
        this.queryId = generateQueryId();
    }

    // Getters and Setters
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    /**
     * Generate a unique query ID based on timestamp and thread
     */
    private String generateQueryId() {
        return String.format("query_%d_%s_%d", 
            System.currentTimeMillis(), 
            Thread.currentThread().getName().hashCode(),
            hashCode());
    }

    /**
     * Check if this is a slow query based on threshold
     */
    public boolean isSlowQuery(long thresholdMs) {
        return executionTimeMs > thresholdMs;
    }

    /**
     * Get formatted execution time string
     */
    public String getFormattedExecutionTime() {
        if (executionTimeMs < 1000) {
            return executionTimeMs + "ms";
        } else {
            return String.format("%.2fs", executionTimeMs / 1000.0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlQueryInfo that = (SqlQueryInfo) o;
        return Objects.equals(queryId, that.queryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId);
    }

    @Override
    public String toString() {
        return "SqlQueryInfo{" +
                "queryId='" + queryId + '\'' +
                ", sql='" + sql + '\'' +
                ", entityName='" + entityName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", executionTimeMs=" + executionTimeMs +
                ", timestamp=" + timestamp +
                '}';
    }
}
