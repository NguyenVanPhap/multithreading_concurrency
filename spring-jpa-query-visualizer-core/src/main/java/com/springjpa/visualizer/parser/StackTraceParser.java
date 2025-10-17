package com.springjpa.visualizer.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for extracting method context information from stack traces.
 * Analyzes the call stack to identify repository methods, entity names, and parameters.
 */
public class StackTraceParser {
    
    private static final Logger logger = LoggerFactory.getLogger(StackTraceParser.class);
    
    // Patterns for identifying Spring Data JPA repository methods
    private static final Pattern REPOSITORY_PATTERN = Pattern.compile(
        ".*\\.(\\w+Repository|\\w+DAO)\\..*", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern ENTITY_PATTERN = Pattern.compile(
        ".*\\.(\\w+)\\.(find|save|delete|update|count|exists).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern METHOD_PATTERN = Pattern.compile(
        ".*\\.(\\w+)\\.(\\w+)\\(.*\\)", 
        Pattern.CASE_INSENSITIVE
    );
    
    // Packages to ignore in stack trace analysis
    private static final String[] IGNORED_PACKAGES = {
        "org.springframework",
        "org.hibernate",
        "com.springjpa.visualizer",
        "java.lang.reflect",
        "sun.reflect",
        "jdk.internal.reflect"
    };
    
    /**
     * Extract context information from the current stack trace
     */
    public ContextInfo extractContext() {
        return extractContext(Thread.currentThread().getStackTrace());
    }
    
    /**
     * Extract context information from a given stack trace
     */
    public ContextInfo extractContext(StackTraceElement[] stackTrace) {
        ContextInfo contextInfo = new ContextInfo();
        
        try {
            // Analyze stack trace to find relevant method calls
            for (StackTraceElement element : stackTrace) {
                String className = element.getClassName();
                String methodName = element.getMethodName();
                
                // Skip ignored packages
                if (isIgnoredPackage(className)) {
                    continue;
                }
                
                // Look for repository methods
                if (isRepositoryMethod(className, methodName)) {
                    contextInfo.setClassName(className);
                    contextInfo.setMethodName(methodName);
                    contextInfo.setEntityName(extractEntityName(className));
                    break;
                }
                
                // Look for service or controller methods that might call repositories
                if (isServiceOrControllerMethod(className, methodName)) {
                    contextInfo.setClassName(className);
                    contextInfo.setMethodName(methodName);
                    // Continue looking for repository method
                }
            }
            
            // Extract parameters if possible
            contextInfo.setParameters(extractParameters(stackTrace));
            
            // Set session ID (simplified - in real implementation, get from Hibernate session)
            contextInfo.setSessionId(generateSessionId());
            
            // Set stack trace string
            contextInfo.setStackTrace(formatStackTrace(stackTrace));
            
        } catch (Exception e) {
            logger.warn("Failed to extract context from stack trace: {}", e.getMessage());
        }
        
        return contextInfo;
    }
    
    /**
     * Check if a package should be ignored in analysis
     */
    private boolean isIgnoredPackage(String className) {
        for (String ignoredPackage : IGNORED_PACKAGES) {
            if (className.startsWith(ignoredPackage)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a method is a repository method
     */
    private boolean isRepositoryMethod(String className, String methodName) {
        // Check for repository naming patterns
        if (className.contains("Repository") || className.contains("DAO")) {
            return true;
        }
        
        // Check for Spring Data JPA method patterns
        return methodName.matches("(find|save|delete|update|count|exists).*") ||
               methodName.matches(".*By.*") ||
               methodName.equals("findAll") ||
               methodName.equals("findById");
    }
    
    /**
     * Check if a method is a service or controller method
     */
    private boolean isServiceOrControllerMethod(String className, String methodName) {
        return className.contains("Service") || 
               className.contains("Controller") ||
               className.contains("Facade");
    }
    
    /**
     * Extract entity name from repository class name
     */
    private String extractEntityName(String className) {
        if (className == null) {
            return null;
        }
        
        // Try to extract entity name from repository class name
        Matcher matcher = REPOSITORY_PATTERN.matcher(className);
        if (matcher.matches()) {
            String repositoryName = matcher.group(1);
            // Remove "Repository" or "DAO" suffix
            return repositoryName.replaceAll("(Repository|DAO)$", "");
        }
        
        // Fallback: try to extract from method patterns
        return null;
    }
    
    /**
     * Extract method parameters from stack trace (simplified implementation)
     */
    private Map<String, Object> extractParameters(StackTraceElement[] stackTrace) {
        Map<String, Object> parameters = new HashMap<>();
        
        // This is a simplified implementation
        // In a real scenario, you might need to use reflection or other techniques
        // to extract actual parameter values
        
        parameters.put("extractedAt", System.currentTimeMillis());
        parameters.put("stackDepth", stackTrace.length);
        
        return parameters;
    }
    
    /**
     * Generate a session ID for tracking
     */
    private String generateSessionId() {
        return "session_" + Thread.currentThread().getName() + "_" + System.currentTimeMillis();
    }
    
    /**
     * Format stack trace as string
     */
    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
            StackTraceElement element = stackTrace[i];
            sb.append(element.getClassName())
              .append(".")
              .append(element.getMethodName())
              .append("(")
              .append(element.getFileName())
              .append(":")
              .append(element.getLineNumber())
              .append(")\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Context information extracted from stack trace
     */
    public static class ContextInfo {
        private String methodName;
        private String className;
        private String entityName;
        private Map<String, Object> parameters;
        private String sessionId;
        private String stackTrace;
        
        public ContextInfo() {
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
        
        public Map<String, Object> getParameters() {
            return parameters;
        }
        
        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
        
        public String getSessionId() {
            return sessionId;
        }
        
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
        
        public String getStackTrace() {
            return stackTrace;
        }
        
        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }
        
        @Override
        public String toString() {
            return "ContextInfo{" +
                    "methodName='" + methodName + '\'' +
                    ", className='" + className + '\'' +
                    ", entityName='" + entityName + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    '}';
        }
    }
}
