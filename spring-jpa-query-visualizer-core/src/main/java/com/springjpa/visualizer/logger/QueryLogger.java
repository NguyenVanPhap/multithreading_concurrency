package com.springjpa.visualizer.logger;

import com.springjpa.visualizer.model.SqlQueryInfo;

/**
 * Interface for logging SQL query information.
 * Implementations can log to files, sockets, databases, or other destinations.
 */
public interface QueryLogger {
    
    /**
     * Log SQL query information
     * 
     * @param queryInfo the SQL query information to log
     */
    void log(SqlQueryInfo queryInfo);
    
    /**
     * Check if the logger is enabled
     * 
     * @return true if logging is enabled, false otherwise
     */
    boolean isEnabled();
    
    /**
     * Enable or disable logging
     * 
     * @param enabled true to enable logging, false to disable
     */
    void setEnabled(boolean enabled);
    
    /**
     * Get the name of this logger
     * 
     * @return the logger name
     */
    String getName();
    
    /**
     * Initialize the logger (called once during startup)
     */
    void initialize();
    
    /**
     * Shutdown the logger (called during application shutdown)
     */
    void shutdown();
}
