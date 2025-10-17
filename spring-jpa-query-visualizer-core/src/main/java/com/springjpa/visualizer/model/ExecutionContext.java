package com.springjpa.visualizer.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the execution context of a SQL query.
 * Contains information about the method call context, entity mapping, and execution environment.
 */
public class ExecutionContext {
    
    private String methodName;
    private String className;
    private String packageName;
    private String entityName;
    private String repositoryName;
    private Map<String, Object> methodParameters;
    private String stackTrace;
    private String sessionId;
    private String threadName;
    private long executionStartTime;
    private long executionEndTime;

    public ExecutionContext() {
        this.executionStartTime = System.currentTimeMillis();
        this.threadName = Thread.currentThread().getName();
    }

    public ExecutionContext(String methodName, String className) {
        this();
        this.methodName = methodName;
        this.className = className;
        this.packageName = extractPackageName(className);
    }

    // Getters and Setters
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
        this.packageName = extractPackageName(className);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Map<String, Object> getMethodParameters() {
        return methodParameters;
    }

    public void setMethodParameters(Map<String, Object> methodParameters) {
        this.methodParameters = methodParameters;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
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

    public long getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(long executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public long getExecutionEndTime() {
        return executionEndTime;
    }

    public void setExecutionEndTime(long executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    /**
     * Calculate execution time in milliseconds
     */
    public long getExecutionTimeMs() {
        return executionEndTime - executionStartTime;
    }

    /**
     * Mark execution as completed
     */
    public void markExecutionCompleted() {
        this.executionEndTime = System.currentTimeMillis();
    }

    /**
     * Extract package name from fully qualified class name
     */
    private String extractPackageName(String className) {
        if (className == null || !className.contains(".")) {
            return "";
        }
        int lastDotIndex = className.lastIndexOf('.');
        return className.substring(0, lastDotIndex);
    }

    /**
     * Check if this is a Spring Data JPA repository method
     */
    public boolean isRepositoryMethod() {
        return className != null && 
               (className.contains("Repository") || 
                className.contains("DAO") ||
                packageName.contains("repository") ||
                packageName.contains("dao"));
    }

    /**
     * Get short class name without package
     */
    public String getShortClassName() {
        if (className == null || !className.contains(".")) {
            return className;
        }
        return className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Get method signature for display
     */
    public String getMethodSignature() {
        return getShortClassName() + "." + methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(methodName, that.methodName) &&
               Objects.equals(className, that.className) &&
               Objects.equals(threadName, that.threadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, className, threadName);
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", entityName='" + entityName + '\'' +
                ", executionTimeMs=" + getExecutionTimeMs() +
                '}';
    }
}
