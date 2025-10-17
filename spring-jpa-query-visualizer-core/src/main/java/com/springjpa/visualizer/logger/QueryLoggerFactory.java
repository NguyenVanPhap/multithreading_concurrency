package com.springjpa.visualizer.logger;

import com.springjpa.visualizer.config.VisualizerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating QueryLogger instances based on configuration.
 * Supports different logger types like file-based JSON logging and socket-based logging.
 */
public class QueryLoggerFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(QueryLoggerFactory.class);
    
    /**
     * Create a QueryLogger instance based on the provided configuration
     * 
     * @param properties the visualizer configuration properties
     * @return a configured QueryLogger instance
     */
    public static QueryLogger createLogger(VisualizerProperties properties) {
        if (properties == null || !properties.isEnabled()) {
            logger.info("Visualizer is disabled, returning NoOpQueryLogger");
            return new NoOpQueryLogger();
        }
        
        VisualizerProperties.Output output = properties.getOutput();
        if (output == null) {
            logger.warn("No output configuration found, using default file logger");
            return createDefaultFileLogger();
        }
        
        switch (output.getType()) {
            case FILE:
                return createFileLogger(output.getFile());
            case SOCKET:
                return createSocketLogger(output.getSocket());
            default:
                logger.warn("Unknown output type: {}, using default file logger", output.getType());
                return createDefaultFileLogger();
        }
    }
    
    /**
     * Create a file-based JSON logger
     */
    private static QueryLogger createFileLogger(VisualizerProperties.File fileConfig) {
        if (fileConfig == null) {
            return createDefaultFileLogger();
        }
        
        String logPath = fileConfig.getPath() != null ? fileConfig.getPath() : "./logs/sql-queries.json";
        long maxSize = fileConfig.getMaxSize() > 0 ? fileConfig.getMaxSize() : 10 * 1024 * 1024; // 10MB default
        int maxFiles = fileConfig.getMaxFiles() > 0 ? fileConfig.getMaxFiles() : 5;
        
        logger.info("Creating JsonQueryLogger with path: {}, maxSize: {} bytes, maxFiles: {}", 
                   logPath, maxSize, maxFiles);
        
        return new JsonQueryLogger(logPath, maxSize, maxFiles);
    }
    
    /**
     * Create a socket-based logger
     */
    private static QueryLogger createSocketLogger(VisualizerProperties.Socket socketConfig) {
        if (socketConfig == null) {
            logger.warn("Socket configuration is null, using default file logger");
            return createDefaultFileLogger();
        }
        
        String host = socketConfig.getHost() != null ? socketConfig.getHost() : "localhost";
        int port = socketConfig.getPort() > 0 ? socketConfig.getPort() : 7777;
        
        logger.info("Creating SocketQueryLogger with host: {}, port: {}", host, port);
        
        return new SocketQueryLogger(host, port);
    }
    
    /**
     * Create a default file logger with standard configuration
     */
    private static QueryLogger createDefaultFileLogger() {
        return new JsonQueryLogger("./logs/sql-queries.json", 10 * 1024 * 1024, 5);
    }
    
    /**
     * No-operation logger for when visualizer is disabled
     */
    private static class NoOpQueryLogger implements QueryLogger {
        
        @Override
        public void log(com.springjpa.visualizer.model.SqlQueryInfo queryInfo) {
            // No operation
        }
        
        @Override
        public boolean isEnabled() {
            return false;
        }
        
        @Override
        public void setEnabled(boolean enabled) {
            // No operation
        }
        
        @Override
        public String getName() {
            return "NoOpQueryLogger";
        }
        
        @Override
        public void initialize() {
            // No operation
        }
        
        @Override
        public void shutdown() {
            // No operation
        }
    }
}
