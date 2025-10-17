package com.springjpa.visualizer.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for VisualizerProperties validation
 */
class VisualizerPropertiesValidationTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testValidProperties() {
        // Given
        VisualizerProperties properties = createValidProperties();
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).isEmpty();
    }
    
    @Test
    void testInvalidFilePath() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().getFile().setPath(""); // Empty path
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Log file path cannot be blank");
    }
    
    @Test
    void testInvalidMaxFileSize() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().getFile().setMaxSize(0); // Invalid size
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Max file size must be at least 1 byte");
    }
    
    @Test
    void testInvalidMaxFiles() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().getFile().setMaxFiles(0); // Invalid count
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Max files must be at least 1");
    }
    
    @Test
    void testInvalidSocketHost() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().setType(VisualizerProperties.Output.Type.SOCKET);
        properties.getOutput().getSocket().setHost(""); // Empty host
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Socket host cannot be blank");
    }
    
    @Test
    void testInvalidSocketPort() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().setType(VisualizerProperties.Output.Type.SOCKET);
        properties.getOutput().getSocket().setPort(0); // Invalid port
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Port must be at least 1");
    }
    
    @Test
    void testInvalidSocketTimeout() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().setType(VisualizerProperties.Output.Type.SOCKET);
        properties.getOutput().getSocket().setTimeout(0); // Invalid timeout
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Timeout must be at least 1ms");
    }
    
    @Test
    void testInvalidSlowQueryThreshold() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getPerformance().setSlowQueryThreshold(-1); // Invalid threshold
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Slow query threshold cannot be negative");
    }
    
    @Test
    void testInvalidMaxStackDepth() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getContext().setMaxStackDepth(0); // Invalid depth
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Max stack depth must be at least 1");
    }
    
    @Test
    void testMultipleViolations() {
        // Given
        VisualizerProperties properties = createValidProperties();
        properties.getOutput().getFile().setPath(""); // Invalid path
        properties.getOutput().getFile().setMaxSize(0); // Invalid size
        properties.getPerformance().setSlowQueryThreshold(-1); // Invalid threshold
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(3);
    }
    
    @Test
    void testNullProperties() {
        // Given
        VisualizerProperties properties = new VisualizerProperties();
        properties.setOutput(null);
        properties.setPerformance(null);
        properties.setContext(null);
        
        // When
        Set<ConstraintViolation<VisualizerProperties>> violations = validator.validate(properties);
        
        // Then
        assertThat(violations).hasSize(3);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("must not be null");
    }
    
    private VisualizerProperties createValidProperties() {
        VisualizerProperties properties = new VisualizerProperties();
        
        // Set valid file configuration
        properties.getOutput().setType(VisualizerProperties.Output.Type.FILE);
        properties.getOutput().getFile().setPath("./logs/sql-queries.json");
        properties.getOutput().getFile().setMaxSize(10485760); // 10MB
        properties.getOutput().getFile().setMaxFiles(5);
        
        // Set valid socket configuration
        properties.getOutput().getSocket().setHost("localhost");
        properties.getOutput().getSocket().setPort(7777);
        properties.getOutput().getSocket().setTimeout(5000);
        
        // Set valid performance configuration
        properties.getPerformance().setSlowQueryThreshold(1000);
        
        // Set valid context configuration
        properties.getContext().setMaxStackDepth(10);
        
        return properties;
    }
}
