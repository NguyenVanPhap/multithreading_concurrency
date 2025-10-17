package com.springjpa.visualizer.config;

import org.springframework.boot.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.boot.configurationmetadata.ConfigurationMetadataRepositoryContributor;
import org.springframework.stereotype.Component;

/**
 * Contributes configuration metadata for Spring JPA Query Visualizer properties.
 * This helps IDEs provide better autocomplete and documentation for configuration properties.
 */
@Component
public class VisualizerConfigurationMetadataContributor implements ConfigurationMetadataRepositoryContributor {
    
    @Override
    public void contribute(ConfigurationMetadataRepository repository) {
        // Main configuration properties
        addProperty(repository, "spring.jpa.visualizer.enabled", 
                   "Whether the Spring JPA Query Visualizer is enabled", 
                   "true", "java.lang.Boolean");
        
        // Output configuration
        addProperty(repository, "spring.jpa.visualizer.output.type", 
                   "Output type for SQL query logging (file or socket)", 
                   "file", "com.springjpa.visualizer.config.VisualizerProperties$Output$Type");
        
        addProperty(repository, "spring.jpa.visualizer.output.file.path", 
                   "Path to the SQL query log file", 
                   "./logs/sql-queries.json", "java.lang.String");
        
        addProperty(repository, "spring.jpa.visualizer.output.file.max-size", 
                   "Maximum size of each log file before rotation (in bytes)", 
                   "10485760", "java.lang.Long");
        
        addProperty(repository, "spring.jpa.visualizer.output.file.max-files", 
                   "Maximum number of log files to keep", 
                   "5", "java.lang.Integer");
        
        addProperty(repository, "spring.jpa.visualizer.output.socket.host", 
                   "Host for socket-based logging", 
                   "localhost", "java.lang.String");
        
        addProperty(repository, "spring.jpa.visualizer.output.socket.port", 
                   "Port for socket-based logging", 
                   "7777", "java.lang.Integer");
        
        addProperty(repository, "spring.jpa.visualizer.output.socket.timeout", 
                   "Connection timeout for socket-based logging (in milliseconds)", 
                   "5000", "java.lang.Integer");
        
        // Performance configuration
        addProperty(repository, "spring.jpa.visualizer.performance.enabled", 
                   "Whether performance monitoring is enabled", 
                   "true", "java.lang.Boolean");
        
        addProperty(repository, "spring.jpa.visualizer.performance.slow-query-threshold", 
                   "Threshold for slow query detection (in milliseconds)", 
                   "1000", "java.lang.Long");
        
        addProperty(repository, "spring.jpa.visualizer.performance.sampling-enabled", 
                   "Whether query sampling is enabled to reduce overhead", 
                   "false", "java.lang.Boolean");
        
        addProperty(repository, "spring.jpa.visualizer.performance.sampling-rate", 
                   "Sampling rate for queries (0.0 to 1.0)", 
                   "1.0", "java.lang.Double");
        
        // Context configuration
        addProperty(repository, "spring.jpa.visualizer.context.include-stack-trace", 
                   "Whether to include stack trace in query information", 
                   "true", "java.lang.Boolean");
        
        addProperty(repository, "spring.jpa.visualizer.context.max-stack-depth", 
                   "Maximum depth of stack trace to include", 
                   "10", "java.lang.Integer");
        
        addProperty(repository, "spring.jpa.visualizer.context.include-session-info", 
                   "Whether to include session information", 
                   "true", "java.lang.Boolean");
        
        addProperty(repository, "spring.jpa.visualizer.context.include-thread-info", 
                   "Whether to include thread information", 
                   "true", "java.lang.Boolean");
    }
    
    private void addProperty(ConfigurationMetadataRepository repository, String name, 
                            String description, String defaultValue, String type) {
        ConfigurationMetadataProperty property = new ConfigurationMetadataProperty();
        property.setName(name);
        property.setDescription(description);
        property.setDefaultValue(defaultValue);
        property.setType(type);
        property.setSourceType("com.springjpa.visualizer.config.VisualizerProperties");
        
        repository.add(property);
    }
}
