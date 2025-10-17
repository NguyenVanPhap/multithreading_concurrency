package com.springjpa.visualizer;

import com.springjpa.visualizer.config.VisualizerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration entry point for Spring JPA Query Visualizer.
 * This class is automatically loaded by Spring Boot when the jar is on the classpath.
 */
@AutoConfiguration
@Import(VisualizerConfig.class)
public class VisualizerAutoConfiguration {
    
    // This class serves as the entry point for auto-configuration
    // The actual configuration is in VisualizerConfig
}
