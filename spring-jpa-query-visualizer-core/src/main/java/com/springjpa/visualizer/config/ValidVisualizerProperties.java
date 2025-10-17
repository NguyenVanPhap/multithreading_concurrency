package com.springjpa.visualizer.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation for VisualizerProperties
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VisualizerPropertiesValidator.class)
@Documented
public @interface ValidVisualizerProperties {
    
    String message() default "Invalid visualizer properties configuration";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
