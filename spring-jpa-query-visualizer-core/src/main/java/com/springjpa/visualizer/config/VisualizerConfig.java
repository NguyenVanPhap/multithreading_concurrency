package com.springjpa.visualizer.config;

import com.springjpa.visualizer.interceptor.HibernateStatementInspector;
import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.logger.QueryLoggerFactory;
import com.springjpa.visualizer.parser.StackTraceParser;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Auto-configuration class for Spring JPA Query Visualizer.
 * Automatically configures the visualizer when Spring Boot detects Hibernate.
 */
@Configuration
@ConditionalOnClass({StatementInspector.class})
@EnableConfigurationProperties(VisualizerProperties.class)
public class VisualizerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(VisualizerConfig.class);
    
    private final VisualizerProperties properties;
    private QueryLogger queryLogger;
    
    public VisualizerConfig(VisualizerProperties properties) {
        this.properties = properties;
    }
    
    @PostConstruct
    public void initialize() {
        logger.info("Initializing Spring JPA Query Visualizer with enabled: {}", properties.isEnabled());
    }
    
    @PreDestroy
    public void shutdown() {
        if (queryLogger != null) {
            logger.info("Shutting down Spring JPA Query Visualizer");
            queryLogger.shutdown();
        }
    }
    
    /**
     * Create QueryLogger bean based on configuration
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.jpa.visualizer", name = "enabled", havingValue = "true", matchIfMissing = true)
    public QueryLogger queryLogger() {
        logger.info("Creating QueryLogger with type: {}", properties.getOutput().getType());
        
        queryLogger = QueryLoggerFactory.createLogger(properties);
        queryLogger.initialize();
        
        return queryLogger;
    }
    
    /**
     * Create StackTraceParser bean
     */
    @Bean
    @ConditionalOnMissingBean
    public StackTraceParser stackTraceParser() {
        logger.debug("Creating StackTraceParser");
        return new StackTraceParser();
    }
    
    /**
     * Create HibernateStatementInspector bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.jpa.visualizer", name = "enabled", havingValue = "true", matchIfMissing = true)
    public StatementInspector statementInspector(QueryLogger queryLogger, StackTraceParser stackTraceParser) {
        logger.info("Creating HibernateStatementInspector");
        
        HibernateStatementInspector inspector = new HibernateStatementInspector(queryLogger, stackTraceParser);
        
        // Configure based on properties
        if (properties.getPerformance() != null && properties.getPerformance().isSamplingEnabled()) {
            logger.info("Query sampling enabled with rate: {}", properties.getPerformance().getSamplingRate());
            // TODO: Implement sampling logic in HibernateStatementInspector
        }
        
        return inspector;
    }
    
    /**
     * Create VisualizerProperties bean if not already present
     */
    @Bean
    @ConditionalOnMissingBean
    public VisualizerProperties visualizerProperties() {
        logger.debug("Creating default VisualizerProperties");
        return new VisualizerProperties();
    }
}
