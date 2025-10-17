package com.springjpa.visualizer.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Validator for VisualizerProperties to ensure configuration is valid
 */
public class VisualizerPropertiesValidator implements ConstraintValidator<ValidVisualizerProperties, VisualizerProperties> {
    
    @Override
    public void initialize(ValidVisualizerProperties constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(VisualizerProperties properties, ConstraintValidatorContext context) {
        if (properties == null) {
            return true; // Let @NotNull handle null validation
        }
        
        boolean isValid = true;
        
        // Validate output configuration
        if (properties.getOutput() != null) {
            isValid &= validateOutputConfiguration(properties.getOutput(), context);
        }
        
        // Validate performance configuration
        if (properties.getPerformance() != null) {
            isValid &= validatePerformanceConfiguration(properties.getPerformance(), context);
        }
        
        // Validate context configuration
        if (properties.getContext() != null) {
            isValid &= validateContextConfiguration(properties.getContext(), context);
        }
        
        return isValid;
    }
    
    private boolean validateOutputConfiguration(VisualizerProperties.Output output, ConstraintValidatorContext context) {
        boolean isValid = true;
        
        // Validate file configuration
        if (output.getType() == VisualizerProperties.Output.Type.FILE && output.getFile() != null) {
            VisualizerProperties.File fileConfig = output.getFile();
            
            // Validate file path
            if (StringUtils.hasText(fileConfig.getPath())) {
                try {
                    Path logPath = Paths.get(fileConfig.getPath());
                    Path parentDir = logPath.getParent();
                    
                    if (parentDir != null && !Files.exists(parentDir)) {
                        // Try to create directory
                        try {
                            Files.createDirectories(parentDir);
                        } catch (Exception e) {
                            context.disableDefaultConstraintViolation();
                            context.buildConstraintViolationWithTemplate(
                                "Cannot create log directory: " + parentDir)
                                .addPropertyNode("output.file.path")
                                .addConstraintViolation();
                            isValid = false;
                        }
                    }
                } catch (Exception e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                        "Invalid log file path: " + fileConfig.getPath())
                        .addPropertyNode("output.file.path")
                        .addConstraintViolation();
                    isValid = false;
                }
            }
            
            // Validate max file size
            if (fileConfig.getMaxSize() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Max file size must be greater than 0")
                    .addPropertyNode("output.file.maxSize")
                    .addConstraintViolation();
                isValid = false;
            }
            
            // Validate max files
            if (fileConfig.getMaxFiles() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Max files must be greater than 0")
                    .addPropertyNode("output.file.maxFiles")
                    .addConstraintViolation();
                isValid = false;
            }
        }
        
        // Validate socket configuration
        if (output.getType() == VisualizerProperties.Output.Type.SOCKET && output.getSocket() != null) {
            VisualizerProperties.Socket socketConfig = output.getSocket();
            
            // Validate port
            if (socketConfig.getPort() <= 0 || socketConfig.getPort() > 65535) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Port must be between 1 and 65535")
                    .addPropertyNode("output.socket.port")
                    .addConstraintViolation();
                isValid = false;
            }
            
            // Validate host
            if (!StringUtils.hasText(socketConfig.getHost())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Host cannot be empty")
                    .addPropertyNode("output.socket.host")
                    .addConstraintViolation();
                isValid = false;
            }
            
            // Validate timeout
            if (socketConfig.getTimeout() <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Timeout must be greater than 0")
                    .addPropertyNode("output.socket.timeout")
                    .addConstraintViolation();
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    private boolean validatePerformanceConfiguration(VisualizerProperties.Performance performance, ConstraintValidatorContext context) {
        boolean isValid = true;
        
        // Validate slow query threshold
        if (performance.getSlowQueryThreshold() < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Slow query threshold cannot be negative")
                .addPropertyNode("performance.slowQueryThreshold")
                .addConstraintViolation();
            isValid = false;
        }
        
        // Validate sampling rate
        if (performance.getSamplingRate() < 0.0 || performance.getSamplingRate() > 1.0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Sampling rate must be between 0.0 and 1.0")
                .addPropertyNode("performance.samplingRate")
                .addConstraintViolation();
            isValid = false;
        }
        
        return isValid;
    }
    
    private boolean validateContextConfiguration(VisualizerProperties.Context context, ConstraintValidatorContext constraintContext) {
        boolean isValid = true;
        
        // Validate max stack depth
        if (context.getMaxStackDepth() <= 0) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate(
                "Max stack depth must be greater than 0")
                .addPropertyNode("context.maxStackDepth")
                .addConstraintViolation();
            isValid = false;
        }
        
        return isValid;
    }
}
