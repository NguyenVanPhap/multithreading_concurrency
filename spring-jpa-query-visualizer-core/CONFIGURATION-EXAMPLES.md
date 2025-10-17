# Spring JPA Query Visualizer - Configuration Examples

Đây là các ví dụ configuration cho Spring JPA Query Visualizer Core Library.

## 📋 Table of Contents

- [Basic Configuration](#basic-configuration)
- [File Logging](#file-logging)
- [Socket Logging](#socket-logging)
- [Performance Monitoring](#performance-monitoring)
- [Context Configuration](#context-configuration)
- [Production Configuration](#production-configuration)
- [Development Configuration](#development-configuration)
- [Testing Configuration](#testing-configuration)

## Basic Configuration

### Minimal Setup
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
```

### With Default Values
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./logs/sql-queries.json
          max-size: 10485760  # 10MB
          max-files: 5
      performance:
        enabled: true
        slow-query-threshold: 1000
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: true
        max-stack-depth: 10
        include-session-info: true
        include-thread-info: true
```

## File Logging

### Standard File Logging
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./logs/sql-queries.json
          max-size: 10485760  # 10MB
          max-files: 5
```

### Custom File Location
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: /var/log/app/sql-queries.json
          max-size: 52428800  # 50MB
          max-files: 10
```

### High-Volume File Logging
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./logs/sql-queries.json
          max-size: 104857600  # 100MB
          max-files: 20
```

## Socket Logging

### Basic Socket Logging
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: socket
        socket:
          host: localhost
          port: 7777
          timeout: 5000
```

### Custom Socket Configuration
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: socket
        socket:
          host: 192.168.1.100
          port: 8888
          timeout: 10000
```

### Socket với Fallback File
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: socket
        socket:
          host: localhost
          port: 7777
          timeout: 5000
        file:
          path: ./logs/sql-queries-fallback.json
          max-size: 10485760
          max-files: 3
```

## Performance Monitoring

### Basic Performance Monitoring
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      performance:
        enabled: true
        slow-query-threshold: 1000  # 1 second
```

### Aggressive Performance Monitoring
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      performance:
        enabled: true
        slow-query-threshold: 100   # 100ms
        sampling-enabled: false
        sampling-rate: 1.0
```

### Performance với Sampling
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      performance:
        enabled: true
        slow-query-threshold: 500
        sampling-enabled: true
        sampling-rate: 0.1  # Sample 10% of queries
```

### High-Performance Mode
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      performance:
        enabled: true
        slow-query-threshold: 2000
        sampling-enabled: true
        sampling-rate: 0.05  # Sample 5% of queries
```

## Context Configuration

### Minimal Context
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      context:
        include-stack-trace: false
        max-stack-depth: 5
        include-session-info: false
        include-thread-info: false
```

### Detailed Context
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      context:
        include-stack-trace: true
        max-stack-depth: 20
        include-session-info: true
        include-thread-info: true
```

### Debug Context
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      context:
        include-stack-trace: true
        max-stack-depth: 50
        include-session-info: true
        include-thread-info: true
```

## Production Configuration

### Production-Ready Setup
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: /var/log/app/sql-queries.json
          max-size: 52428800  # 50MB
          max-files: 10
      performance:
        enabled: true
        slow-query-threshold: 2000  # 2 seconds
        sampling-enabled: true
        sampling-rate: 0.1  # Sample 10%
      context:
        include-stack-trace: false
        max-stack-depth: 5
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: WARN
```

### High-Volume Production
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: /var/log/app/sql-queries.json
          max-size: 104857600  # 100MB
          max-files: 20
      performance:
        enabled: true
        slow-query-threshold: 5000  # 5 seconds
        sampling-enabled: true
        sampling-rate: 0.01  # Sample 1%
      context:
        include-stack-trace: false
        max-stack-depth: 3
        include-session-info: false
        include-thread-info: false

logging:
  level:
    com.springjpa.visualizer: ERROR
```

## Development Configuration

### Development với Full Logging
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./logs/sql-queries.json
          max-size: 10485760  # 10MB
          max-files: 5
      performance:
        enabled: true
        slow-query-threshold: 100  # 100ms
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: true
        max-stack-depth: 20
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: DEBUG
    org.hibernate.SQL: DEBUG
```

### Development với Socket Logging
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: socket
        socket:
          host: localhost
          port: 7777
          timeout: 5000
      performance:
        enabled: true
        slow-query-threshold: 50  # 50ms
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: true
        max-stack-depth: 30
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: DEBUG
```

## Testing Configuration

### Unit Testing
```yaml
spring:
  jpa:
    visualizer:
      enabled: false  # Disable for unit tests
```

### Integration Testing
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./test-logs/sql-queries.json
          max-size: 1048576  # 1MB
          max-files: 2
      performance:
        enabled: true
        slow-query-threshold: 100
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: false
        max-stack-depth: 5
        include-session-info: false
        include-thread-info: false

logging:
  level:
    com.springjpa.visualizer: WARN
```

### Performance Testing
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./perf-test-logs/sql-queries.json
          max-size: 52428800  # 50MB
          max-files: 5
      performance:
        enabled: true
        slow-query-threshold: 10  # 10ms
        sampling-enabled: true
        sampling-rate: 0.5  # Sample 50%
      context:
        include-stack-trace: true
        max-stack-depth: 10
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: INFO
```

## Environment-Specific Configurations

### application-dev.yml
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: socket
        socket:
          host: localhost
          port: 7777
      performance:
        slow-query-threshold: 100
        sampling-enabled: false
      context:
        include-stack-trace: true
        max-stack-depth: 20

logging:
  level:
    com.springjpa.visualizer: DEBUG
```

### application-prod.yml
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: /var/log/app/sql-queries.json
          max-size: 52428800
          max-files: 10
      performance:
        slow-query-threshold: 2000
        sampling-enabled: true
        sampling-rate: 0.1
      context:
        include-stack-trace: false
        max-stack-depth: 5

logging:
  level:
    com.springjpa.visualizer: WARN
```

### application-test.yml
```yaml
spring:
  jpa:
    visualizer:
      enabled: false  # Disable for tests
```

## Advanced Configurations

### Multi-Environment Setup
```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  jpa:
    visualizer:
      enabled: ${VISUALIZER_ENABLED:true}
      output:
        type: ${VISUALIZER_OUTPUT_TYPE:file}
        file:
          path: ${VISUALIZER_LOG_PATH:./logs/sql-queries.json}
          max-size: ${VISUALIZER_MAX_FILE_SIZE:10485760}
          max-files: ${VISUALIZER_MAX_FILES:5}
        socket:
          host: ${VISUALIZER_SOCKET_HOST:localhost}
          port: ${VISUALIZER_SOCKET_PORT:7777}
          timeout: ${VISUALIZER_SOCKET_TIMEOUT:5000}
      performance:
        enabled: ${VISUALIZER_PERFORMANCE_ENABLED:true}
        slow-query-threshold: ${VISUALIZER_SLOW_QUERY_THRESHOLD:1000}
        sampling-enabled: ${VISUALIZER_SAMPLING_ENABLED:false}
        sampling-rate: ${VISUALIZER_SAMPLING_RATE:1.0}
      context:
        include-stack-trace: ${VISUALIZER_INCLUDE_STACK_TRACE:true}
        max-stack-depth: ${VISUALIZER_MAX_STACK_DEPTH:10}
        include-session-info: ${VISUALIZER_INCLUDE_SESSION_INFO:true}
        include-thread-info: ${VISUALIZER_INCLUDE_THREAD_INFO:true}
```

### Docker Configuration
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: /app/logs/sql-queries.json
          max-size: 52428800
          max-files: 10
      performance:
        enabled: true
        slow-query-threshold: 1000
        sampling-enabled: true
        sampling-rate: 0.1
      context:
        include-stack-trace: false
        max-stack-depth: 5
        include-session-info: true
        include-thread-info: true
```

## Troubleshooting Configurations

### Debug Configuration
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./debug-logs/sql-queries.json
          max-size: 10485760
          max-files: 10
      performance:
        enabled: true
        slow-query-threshold: 1  # 1ms - catch everything
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: true
        max-stack-depth: 50
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.springframework.boot.autoconfigure: DEBUG
```

### Performance Debug Configuration
```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./perf-debug-logs/sql-queries.json
          max-size: 104857600
          max-files: 20
      performance:
        enabled: true
        slow-query-threshold: 0  # Catch all queries
        sampling-enabled: false
        sampling-rate: 1.0
      context:
        include-stack-trace: true
        max-stack-depth: 100
        include-session-info: true
        include-thread-info: true

logging:
  level:
    com.springjpa.visualizer: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.stat: DEBUG
```

---

*For more information, see the main project README.md*
