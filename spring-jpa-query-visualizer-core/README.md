# Spring JPA Query Visualizer Core

**Spring JPA Query Visualizer Core** là thư viện Java giúp developers **xem trực tiếp SQL được sinh ra bởi Hibernate/JPA** và theo dõi performance của các queries.

## 🚀 Quick Start

### 1. Thêm Dependency

#### Maven
```xml
<dependency>
    <groupId>com.springjpa.visualizer</groupId>
    <artifactId>spring-jpa-query-visualizer-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

#### Gradle
```gradle
implementation 'com.springjpa.visualizer:spring-jpa-query-visualizer-core:0.1.0-SNAPSHOT'
```

### 2. Configuration

Thêm vào `application.yml`:

```yaml
spring:
  jpa:
    visualizer:
      enabled: true
      output:
        type: file
        file:
          path: ./logs/sql-queries.json
          max-size: 10MB
          max-files: 5
      performance:
        enabled: true
        slow-query-threshold: 1000ms
      context:
        include-stack-trace: true
        max-stack-depth: 10
```

### 3. Chạy Application

SQL queries sẽ được tự động capture và log vào file JSON!

## 📋 Features

### ✅ Core Features
- **SQL Interception**: Tự động capture mọi SQL query từ Hibernate
- **JSON Logging**: Log queries ra file JSON với format chuẩn
- **Performance Monitoring**: Track execution time và detect slow queries
- **Entity Mapping**: Tự động map entity names với table names
- **Query Sampling**: Optimize performance với configurable sampling

### ✅ Advanced Features
- **Socket Logging**: Real-time streaming cho IDE integration
- **File Rotation**: Automatic log file rotation với size limits
- **Statistics**: Comprehensive performance và usage statistics
- **Health Monitoring**: Spring Boot Actuator health indicators
- **Metrics**: Micrometer metrics integration
- **Validation**: Configuration properties validation

## ⚙️ Configuration

### Basic Configuration

```yaml
spring:
  jpa:
    visualizer:
      enabled: true  # Enable/disable visualizer
```

### Output Configuration

#### File Logging (Default)
```yaml
spring:
  jpa:
    visualizer:
      output:
        type: file
        file:
          path: ./logs/sql-queries.json  # Log file path
          max-size: 10485760            # Max file size (10MB)
          max-files: 5                  # Max number of files
```

#### Socket Logging
```yaml
spring:
  jpa:
    visualizer:
      output:
        type: socket
        socket:
          host: localhost               # Socket host
          port: 7777                    # Socket port
          timeout: 5000                 # Connection timeout (ms)
```

### Performance Configuration

```yaml
spring:
  jpa:
    visualizer:
      performance:
        enabled: true                   # Enable performance monitoring
        slow-query-threshold: 1000      # Slow query threshold (ms)
        sampling-enabled: false         # Enable query sampling
        sampling-rate: 1.0              # Sampling rate (0.0-1.0)
```

### Context Configuration

```yaml
spring:
  jpa:
    visualizer:
      context:
        include-stack-trace: true       # Include stack trace
        max-stack-depth: 10             # Max stack trace depth
        include-session-info: true      # Include session info
        include-thread-info: true       # Include thread info
```

## 📊 Output Format

### JSON Log Format

```json
{
  "sql": "SELECT u.id, u.name, u.email FROM users u WHERE u.email = ?",
  "entityName": "User",
  "methodName": "findByEmail",
  "className": "com.example.repository.UserRepository",
  "executionTimeMs": 45,
  "parameters": {
    "email": "john.doe@example.com"
  },
  "timestamp": "2024-01-15T10:30:45.123",
  "sessionId": "session_123",
  "threadName": "http-nio-8080-exec-1",
  "queryId": "query_1705312245123_main_12345"
}
```

### Performance Statistics

```json
{
  "totalQueries": 150,
  "slowQueries": 5,
  "avgExecutionTime": 125.5,
  "slowQueryRate": 0.033,
  "minExecutionTime": 12,
  "maxExecutionTime": 2500
}
```

## 🔧 API Reference

### HibernateStatementInspector

Main interceptor class cho Hibernate integration.

```java
@Autowired
private HibernateStatementInspector inspector;

// Get performance statistics
PerformanceMonitor.PerformanceStats stats = inspector.getPerformanceStats();

// Get sampling statistics
QuerySampler.SamplingStats samplingStats = inspector.getSamplingStats();

// Get entity statistics
Map<String, PerformanceMonitor.EntityStats> entityStats = inspector.getEntityStats();

// Get recent slow queries
Map<String, PerformanceMonitor.SlowQueryInfo> slowQueries = inspector.getRecentSlowQueries();

// Reset all statistics
inspector.resetStatistics();
```

### QueryLogger

Interface cho different logging strategies.

```java
@Autowired
private QueryLogger queryLogger;

// Check if enabled
boolean enabled = queryLogger.isEnabled();

// Enable/disable
queryLogger.setEnabled(true);

// Get logger name
String name = queryLogger.getName();
```

## 📈 Monitoring

### Health Endpoint

Nếu sử dụng Spring Boot Actuator:

```bash
curl http://localhost:8080/actuator/health
```

Response:
```json
{
  "status": "UP",
  "components": {
    "visualizer": {
      "status": "UP",
      "details": {
        "queryLogger.enabled": true,
        "statementInspector.enabled": true,
        "performance.totalQueries": 150,
        "performance.avgExecutionTime": 125.5,
        "sampling.totalQueries": 150,
        "sampling.actualRate": 1.0
      }
    }
  }
}
```

### Metrics Endpoint

Nếu sử dụng Micrometer:

```bash
curl http://localhost:8080/actuator/metrics/spring.jpa.visualizer.queries.total
```

Available metrics:
- `spring.jpa.visualizer.queries.total` - Total queries processed
- `spring.jpa.visualizer.queries.slow` - Slow queries detected
- `spring.jpa.visualizer.queries.execution.time` - Query execution time
- `spring.jpa.visualizer.queries.active` - Currently active queries
- `spring.jpa.visualizer.entities.count` - Number of tracked entities
- `spring.jpa.visualizer.performance.avg.execution.time` - Average execution time
- `spring.jpa.visualizer.performance.slow.query.rate` - Slow query rate

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn verify
```

### Test Coverage

```bash
mvn jacoco:report
```

## 🐛 Troubleshooting

### Common Issues

#### 1. No SQL Logs Generated
- **Check**: `spring.jpa.visualizer.enabled=true`
- **Verify**: Log directory permissions
- **Check**: Application logs for errors

#### 2. Empty JSON File
- **Verify**: Hibernate is properly configured
- **Check**: Queries are actually being executed
- **Enable**: Debug logging: `logging.level.com.springjpa.visualizer=DEBUG`

#### 3. Performance Issues
- **Enable**: Query sampling: `sampling-enabled=true`
- **Adjust**: Sampling rate: `sampling-rate=0.1`
- **Increase**: Slow query threshold

#### 4. Socket Connection Issues
- **Check**: Port availability (default: 7777)
- **Verify**: Firewall settings
- **Test**: IDE plugin connection

### Debug Configuration

```yaml
logging:
  level:
    com.springjpa.visualizer: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## 🔄 Migration Guide

### From Version 0.0.x to 0.1.0

#### Configuration Changes
- `spring.jpa.visualizer.log-file-path` → `spring.jpa.visualizer.output.file.path`
- `spring.jpa.visualizer.max-file-size` → `spring.jpa.visualizer.output.file.max-size`
- `spring.jpa.visualizer.max-files` → `spring.jpa.visualizer.output.file.max-files`

#### API Changes
- `SqlInterceptor` interface updated với new methods
- `QueryLogger` interface updated với new methods
- New performance monitoring APIs

## 🤝 Contributing

### Development Setup

1. **Clone repository**
```bash
git clone https://github.com/yourusername/spring-jpa-query-visualizer.git
cd spring-jpa-query-visualizer/spring-jpa-query-visualizer-core
```

2. **Build project**
```bash
mvn clean install
```

3. **Run tests**
```bash
mvn test
```

### Code Style

- Follow Java coding conventions
- Use meaningful variable names
- Add comprehensive JavaDoc
- Write unit tests for new features
- Update documentation

## 📜 License

Apache License 2.0 - see [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Hibernate](https://hibernate.org/) - ORM framework
- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [Jackson](https://github.com/FasterXML/jackson) - JSON processing
- [Micrometer](https://micrometer.io/) - Metrics collection

---

**Version**: 0.1.0-SNAPSHOT  
**Java**: 17+  
**Spring Boot**: 3.2+  
**Hibernate**: 6.3+
