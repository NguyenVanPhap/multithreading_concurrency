# Spring JPA Query Visualizer - Test Application

Đây là test application để demo và test Spring JPA Query Visualizer Core Library.

## 🚀 Quick Start

### 1. Build Core Library
```bash
cd spring-jpa-query-visualizer-core
mvn clean install
```

### 2. Run Test Application
```bash
cd examples/test-app
mvn spring-boot:run
```

### 3. Test Endpoints
Application sẽ chạy trên `http://localhost:8080`

## 📋 Test Scenarios

### Simple Queries
- `GET /api/users` - Test `findAll()`
- `GET /api/users/{id}` - Test `findById()`
- `GET /api/users/email/{email}` - Test `findByEmail()`
- `GET /api/users/name/{name}` - Test `findByName()`

### Complex Queries
- `GET /api/users/email/{email}/status/{status}` - Test `findByEmailAndStatus()`
- `GET /api/users/search/name/{name}` - Test `findByNameContainingIgnoreCase()`
- `GET /api/users/age/between/{minAge}/{maxAge}` - Test `findByAgeBetween()`

### Custom Queries
- `GET /api/users/custom/search?name={name}&status={status}` - Test JPQL query
- `GET /api/users/native/age/{minAge}/{maxAge}` - Test native SQL query
- `GET /api/users/count/status/{status}` - Test count query

### CRUD Operations
- `POST /api/users` - Test INSERT
- `PUT /api/users/{id}` - Test UPDATE
- `DELETE /api/users/{id}` - Test DELETE

### Performance Testing
- `GET /api/users/test/multiple` - Test multiple queries in single request

## 📊 Expected Output

### JSON Log File (`./logs/sql-queries.json`)
```json
{
  "sql": "SELECT u.id, u.name, u.email, u.age, u.status, u.created_at, u.updated_at FROM users u WHERE u.email = ?",
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

### Performance Metrics
- Total queries executed
- Average execution time
- Slow query detection
- Query type statistics
- Entity statistics

## 🔧 Configuration

### File Logging (Default)
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
```

### Socket Logging
```yaml
spring:
  jpa:
    visualizer:
      output:
        type: socket
        socket:
          host: localhost
          port: 7777
```

### Performance Monitoring
```yaml
spring:
  jpa:
    visualizer:
      performance:
        enabled: true
        slow-query-threshold: 1000ms
        sampling-enabled: false
        sampling-rate: 1.0
```

## 🧪 Test Commands

### Basic Queries
```bash
# Get all users
curl http://localhost:8080/api/users

# Get user by email
curl http://localhost:8080/api/users/email/john.doe@example.com

# Get users by status
curl http://localhost:8080/api/users/status/ACTIVE

# Search users by name
curl http://localhost:8080/api/users/search/name/John
```

### Complex Queries
```bash
# Get users by email and status
curl http://localhost:8080/api/users/email/john.doe@example.com/status/ACTIVE

# Get users by age range
curl http://localhost:8080/api/users/age/between/25/35

# Custom JPQL query
curl "http://localhost:8080/api/users/custom/search?name=John&status=ACTIVE"

# Native SQL query
curl http://localhost:8080/api/users/native/age/25/35
```

### CRUD Operations
```bash
# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","age":30}'

# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated User","email":"updated@example.com","age":31}'

# Delete user
curl -X DELETE http://localhost:8080/api/users/1
```

### Performance Testing
```bash
# Test multiple queries
curl http://localhost:8080/api/users/test/multiple

# Test count queries
curl http://localhost:8080/api/users/count/status/ACTIVE

# Test exists queries
curl http://localhost:8080/api/users/exists/email/john.doe@example.com
```

## 📈 Monitoring

### Log Files
- `./logs/sql-queries.json` - SQL query logs
- `./logs/application.log` - Application logs

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Performance Metrics
Check application logs for:
- Query execution times
- Slow query warnings
- Performance statistics
- Sampling information

## 🐛 Troubleshooting

### No SQL Logs Generated
1. Check `spring.jpa.visualizer.enabled=true`
2. Verify log directory permissions
3. Check application logs for errors

### Empty JSON File
1. Verify Hibernate is properly configured
2. Check if queries are actually being executed
3. Enable debug logging: `logging.level.com.springjpa.visualizer=DEBUG`

### Performance Issues
1. Enable sampling: `sampling-enabled=true`
2. Adjust sampling rate: `sampling-rate=0.1`
3. Increase slow query threshold

### Connection Issues (Socket Mode)
1. Check if port 7777 is available
2. Verify firewall settings
3. Check IDE plugin connection

## 📝 Sample Data

Application tự động tạo 10 sample users với:
- Different names và emails
- Various ages (25-42)
- Different statuses (ACTIVE, INACTIVE, SUSPENDED)
- Created timestamps

## 🎯 Next Steps

1. **Test Core Library**: Verify SQL interception works
2. **Check JSON Output**: Validate log format
3. **Performance Testing**: Monitor execution times
4. **IDE Integration**: Test với IntelliJ plugin (Phase 1B)
5. **Customization**: Adjust configuration as needed

---

*For more information, see the main project README.md*
