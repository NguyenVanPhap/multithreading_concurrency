package com.springjpa.visualizer.interceptor;

import com.springjpa.visualizer.model.SqlQueryInfo;

/**
 * Main interface for SQL query interception.
 * Implementations of this interface are responsible for capturing SQL queries
 * and their metadata during execution.
 */
public interface SqlInterceptor {
    
    /**
     * Intercept and process a SQL query before execution.
     * 
     * @param sql the SQL query string
     * @param context additional context information about the query execution
     * @return the original SQL query (should not be modified)
     */
    String intercept(String sql, ExecutionContext context);
    
    /**
     * Intercept and process a SQL query after execution.
     * 
     * @param sql the SQL query string
     * @param context additional context information about the query execution
     * @param executionTimeMs the execution time in milliseconds
     * @return the original SQL query (should not be modified)
     */
    String interceptAfter(String sql, ExecutionContext context, long executionTimeMs);
    
    /**
     * Check if this interceptor is enabled and should process queries.
     * 
     * @return true if the interceptor is enabled, false otherwise
     */
    boolean isEnabled();
    
    /**
     * Enable or disable this interceptor.
     * 
     * @param enabled true to enable, false to disable
     */
    void setEnabled(boolean enabled);
    
    /**
     * Get the name of this interceptor for logging and debugging purposes.
     * 
     * @return the interceptor name
     */
    String getName();
    
    /**
     * Inner class representing execution context for SQL queries.
     */
    class ExecutionContext {
        private String methodName;
        private String className;
        private String entityName;
        private Object[] parameters;
        private String sessionId;
        private long startTime;
        
        public ExecutionContext() {
            this.startTime = System.currentTimeMillis();
        }
        
        public ExecutionContext(String methodName, String className) {
            this();
            this.methodName = methodName;
            this.className = className;
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
        }
        
        public String getEntityName() {
            return entityName;
        }
        
        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }
        
        public Object[] getParameters() {
            return parameters;
        }
        
        public void setParameters(Object[] parameters) {
            this.parameters = parameters;
        }
        
        public String getSessionId() {
            return sessionId;
        }
        
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public long getExecutionTimeMs() {
            return System.currentTimeMillis() - startTime;
        }
        
        @Override
        public String toString() {
            return "ExecutionContext{" +
                    "methodName='" + methodName + '\'' +
                    ", className='" + className + '\'' +
                    ", entityName='" + entityName + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    '}';
        }
    }
}
