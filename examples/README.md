# Spring JPA Query Visualizer - Example Application

Đây là example application để test Spring JPA Query Visualizer Core Library.

## Cấu trúc

```
examples/
├── test-app/
│   ├── src/main/java/
│   │   ├── com/example/
│   │   │   ├── TestApplication.java
│   │   │   ├── entity/
│   │   │   │   └── User.java
│   │   │   └── repository/
│   │   │       └── UserRepository.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql
│   └── pom.xml
└── README.md
```

## Cách sử dụng

1. **Build core library:**
   ```bash
   cd spring-jpa-query-visualizer-core
   mvn clean install
   ```

2. **Run example application:**
   ```bash
   cd examples/test-app
   mvn spring-boot:run
   ```

3. **Check logs:**
   - SQL queries sẽ được log vào `./logs/sql-queries.json`
   - Mở file để xem JSON format của captured queries

## Configuration

Example application sử dụng configuration mặc định:

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

## Test Scenarios

Application sẽ test các scenarios sau:

1. **Simple Repository Methods:**
   - `findAll()`
   - `findById()`
   - `findByEmail()`

2. **Complex Queries:**
   - `findByEmailAndStatus()`
   - `findByCreatedDateBetween()`

3. **Performance Testing:**
   - Multiple queries trong single request
   - Slow query detection

## Expected Output

JSON log file sẽ chứa entries như:

```json
{
  "sql": "SELECT u.id, u.name, u.email FROM users u WHERE u.email = ?",
  "entityName": "User",
  "methodName": "findByEmail",
  "className": "com.example.repository.UserRepository",
  "executionTimeMs": 45,
  "parameters": {
    "email": "test@example.com"
  },
  "timestamp": "2024-01-15T10:30:45.123",
  "sessionId": "session_123",
  "threadName": "http-nio-8080-exec-1",
  "queryId": "query_1705312245123_main_12345"
}
```

## Troubleshooting

- **No logs generated:** Check `spring.jpa.visualizer.enabled=true`
- **Permission errors:** Ensure write permissions cho log directory
- **Empty JSON:** Verify Hibernate is properly configured
- **Performance issues:** Enable sampling với `sampling-enabled=true`
