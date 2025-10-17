# 🚀 Phase 1: Core Library & IntelliJ Plugin MVP

**Thời gian:** 1-2 tháng  
**Mục tiêu:** Xây dựng core library để log SQL ra JSON và MVP IntelliJ plugin hiển thị popup SQL

---

## 🎯 Tổng quan mục tiêu

### ✅ Deliverables chính
- **Core Library**: Intercept SQL từ Hibernate và log ra JSON (file/socket)
- **IntelliJ Plugin MVP**: Đọc JSON và hiển thị popup SQL
- **End-to-end**: Từ Spring Boot app → SQL capture → IDE visualization

### 🎯 Success Criteria
- [ ] Intercept SQL từ Hibernate thành công
- [ ] Log JSON format đúng chuẩn  
- [ ] Zero performance impact khi disabled
- [ ] Right-click → "Show SQL" hoạt động trong IntelliJ
- [ ] Popup hiển thị SQL đúng format
- [ ] Demo video showing complete workflow

---

## 🏗️ Phase 1A: Core Library (Tuần 1-3)

### 📁 Cấu trúc Core Library

```
spring-jpa-query-visualizer-core/
├── src/main/java/
│   ├── com/springjpa/visualizer/
│   │   ├── interceptor/
│   │   │   ├── SqlInterceptor.java              # Main interceptor interface
│   │   │   ├── HibernateStatementInspector.java # Hibernate interceptor
│   │   │   └── DataSourceProxyInterceptor.java  # DataSource interceptor
│   │   ├── model/
│   │   │   ├── SqlQueryInfo.java                # Query metadata model
│   │   │   ├── ExecutionContext.java            # Context info
│   │   │   └── QueryMetadata.java               # Entity mapping info
│   │   ├── logger/
│   │   │   ├── JsonQueryLogger.java             # JSON file logger
│   │   │   ├── SocketQueryLogger.java           # Socket logger
│   │   │   ├── QueryLogger.java                 # Logger interface
│   │   │   └── QueryLoggerFactory.java          # Logger factory
│   │   ├── config/
│   │   │   ├── VisualizerConfig.java            # Configuration
│   │   │   └── VisualizerProperties.java       # Properties
│   │   ├── parser/
│   │   │   ├── StackTraceParser.java            # Parse method context
│   │   │   └── EntityMapper.java                # Map entity info
│   │   └── VisualizerAutoConfiguration.java     # Spring Boot auto-config
├── src/main/resources/
│   └── META-INF/
│       └── spring.factories
├── src/test/java/
│   └── com/springjpa/visualizer/
│       ├── interceptor/
│       ├── logger/
│       └── config/
└── pom.xml
```

### 🔧 Core Components Implementation

#### 1. SqlQueryInfo Model
- **Mục đích**: Chứa metadata của SQL query được thực thi
- **Thông tin bao gồm**:
  - Raw SQL query
  - Entity class name và method name
  - Execution time và parameters
  - Stack trace và timestamp
  - Session ID và thread name

#### 2. SQL Interceptor Implementation
- **HibernateStatementInspector**: Implement StatementInspector interface của Hibernate
- **Chức năng**:
  - Capture SQL execution
  - Extract context từ stack trace
  - Map parameters và execution time
  - Build SqlQueryInfo object

#### 3. JSON Logger Implementation
- **JsonQueryLogger**: Log queries ra file JSON
- **Tính năng**:
  - Async file writing để không block application
  - File rotation based on size/time
  - Thread-safe operations
  - Error handling không làm crash app

### ⚙️ Spring Boot Integration

#### Auto Configuration
- **VisualizerAutoConfiguration**: Tự động config khi detect Hibernate
- **Conditional beans**: Chỉ tạo khi enabled trong properties
- **Dependency injection**: Inject QueryLogger vào interceptor

#### Properties Configuration
- **spring.jpa.visualizer.enabled**: Enable/disable visualizer
- **Output configuration**: File path hoặc socket port
- **Performance settings**: Slow query threshold
- **Context settings**: Stack trace depth, session info

---

## 🔌 Phase 1B: IntelliJ Plugin MVP (Tuần 4-6)

### 📁 Plugin Structure

```
spring-jpa-query-visualizer-intellij/
├── src/main/kotlin/
│   ├── com/springjpa/visualizer/intellij/
│   │   ├── actions/
│   │   │   ├── ShowSqlQueryAction.java        # Main action handler
│   │   │   └── RefreshQueriesAction.java      # Refresh action
│   │   ├── ui/
│   │   │   ├── SqlQueryPopupDialog.java       # Popup dialog
│   │   │   ├── SqlQueryPanel.java             # Main panel
│   │   │   └── QueryListPanel.java            # Query list
│   │   ├── services/
│   │   │   ├── JsonQueryReader.java           # JSON parser
│   │   │   ├── QueryCache.java                # In-memory cache
│   │   │   └── QueryFilterService.java        # Query filtering
│   │   ├── settings/
│   │   │   ├── VisualizerSettings.java        # Plugin settings
│   │   │   └── VisualizerConfigurable.java   # Settings UI
│   │   └── utils/
│   │       ├── SqlFormatter.java              # SQL formatting
│   │       └── JsonUtils.java                 # JSON utilities
├── resources/
│   ├── META-INF/
│   │   └── plugin.xml                         # Plugin descriptor
│   ├── icons/
│   └── messages/
└── build.gradle.kts
```

### 🔧 Core Plugin Features

#### 1. JSON Reader & Parser
- **JsonQueryReader**: Đọc và parse JSON từ file/socket
- **Chức năng**:
  - Read JSON file với line-delimited format
  - Parse từng JSON object thành SqlQueryInfo
  - Handle file rotation và connection errors
  - Stream processing cho real-time updates

#### 2. Popup Action Implementation
- **ShowSqlQueryAction**: Main action handler
- **Workflow**:
  - Right-click trên repository method
  - Extract method context từ cursor position
  - Filter queries theo method/entity
  - Display trong popup dialog

#### 3. Popup Dialog UI
- **SqlQueryPopupDialog**: Main UI component
- **Layout**:
  - Query list panel bên trái
  - SQL display area bên phải
  - Syntax highlighting cho SQL
  - Copy to clipboard functionality

### ⚙️ Plugin Configuration

#### plugin.xml
- **Plugin metadata**: ID, name, version, vendor
- **Dependencies**: Java và Spring modules
- **Actions**: Context menu actions với keyboard shortcuts
- **Extensions**: Settings configuration UI

---

## 🧪 Phase 1C: Integration & Testing (Tuần 7-8)

### 🧪 Test Scenarios

#### 1. End-to-End Test Application
- **TestApplication**: Spring Boot app với multiple scenarios
- **Test cases**:
  - Simple repository method queries
  - Complex queries với joins
  - Multiple queries trong single request
  - Performance testing với slow queries

#### 2. Test Cases Checklist
- [ ] **Simple Repository Method**: Basic SQL capture
- [ ] **Complex Query**: JOIN SQL capture  
- [ ] **Multiple Queries**: Multiple JSON entries
- [ ] **Performance**: Slow query detection
- [ ] **Error Handling**: Graceful fallback
- [ ] **Plugin Integration**: Right-click → Popup display
- [ ] **Query Filtering**: Filter by method/entity

### 🔧 Performance & Error Handling

#### Performance Considerations
- **Query Sampling**: Tránh overhead với high-frequency queries
- **Async Logging**: Non-blocking operations
- **Memory Management**: Efficient caching và cleanup
- **Configurable Thresholds**: Adjustable performance settings

#### Error Handling Strategy
- **Safe Logging**: Never break application khi logging fails
- **Graceful Degradation**: Fallback options khi plugin fails
- **User Feedback**: Clear error messages và recovery options
- **Logging**: Comprehensive error logging cho debugging

---

## 📅 Timeline Chi tiết

### 🗓️ Tuần 1-2: Core Library Foundation
**Mục tiêu:** Setup và implement core components

#### Tuần 1
- [ ] **Day 1-2**: Setup project structure với Maven/Gradle
- [ ] **Day 3-4**: Implement SqlQueryInfo model và related classes
- [ ] **Day 5**: Create HibernateStatementInspector interface

#### Tuần 2  
- [ ] **Day 1-2**: Implement JSON file logger với async writing
- [ ] **Day 3-4**: Create StackTraceParser để extract method context
- [ ] **Day 5**: Unit tests cho core components

### 🗓️ Tuần 3: Spring Boot Integration
**Mục tiêu:** Auto-configuration và properties

- [ ] **Day 1-2**: Implement VisualizerAutoConfiguration
- [ ] **Day 3**: Create VisualizerProperties với validation
- [ ] **Day 4**: Integration tests với Spring Boot
- [ ] **Day 5**: Documentation và examples

### 🗓️ Tuần 4-5: IntelliJ Plugin MVP
**Mục tiêu:** Basic plugin functionality

#### Tuần 4
- [ ] **Day 1-2**: Setup IntelliJ plugin project với Gradle
- [ ] **Day 3-4**: Implement JsonQueryReader và basic parsing
- [ ] **Day 5**: Create ShowSqlQueryAction với context menu

#### Tuần 5
- [ ] **Day 1-2**: Implement SqlQueryPopupDialog với basic UI
- [ ] **Day 3**: Add SQL syntax highlighting
- [ ] **Day 4**: Implement query filtering by method/entity
- [ ] **Day 5**: Plugin testing và debugging

### 🗓️ Tuần 6: Plugin Enhancement
**Mục tiêu:** UI improvements và error handling

- [ ] **Day 1-2**: Improve popup UI với better layout
- [ ] **Day 3**: Add copy to clipboard functionality
- [ ] **Day 4**: Implement settings configuration
- [ ] **Day 5**: Error handling và user feedback

### 🗓️ Tuần 7-8: Integration & Polish
**Mục tiêu:** End-to-end testing và documentation

#### Tuần 7
- [ ] **Day 1-2**: Create comprehensive test Spring Boot app
- [ ] **Day 3-4**: End-to-end testing scenarios
- [ ] **Day 5**: Performance testing và optimization

#### Tuần 8
- [ ] **Day 1-2**: Documentation và README updates
- [ ] **Day 3**: Demo video creation
- [ ] **Day 4**: Final testing và bug fixes
- [ ] **Day 5**: Release preparation

---

## 🎯 Success Metrics

### 📊 Core Library Metrics
- [ ] **SQL Capture Rate**: >95% queries captured
- [ ] **Performance Impact**: <1ms overhead per query
- [ ] **Memory Usage**: <10MB additional memory
- [ ] **Error Rate**: <0.1% logging failures

### 📊 Plugin Metrics  
- [ ] **Response Time**: <500ms popup display time
- [ ] **Accuracy**: >90% correct method-to-SQL mapping
- [ ] **Usability**: <3 clicks to view SQL
- [ ] **Stability**: No plugin crashes during testing

### 📊 Integration Metrics
- [ ] **End-to-End Success**: 100% demo scenarios working
- [ ] **Documentation**: Complete setup guide
- [ ] **Examples**: Working sample applications
- [ ] **Community**: Initial feedback collection

---

## 🚀 Next Steps After Phase 1

### Phase 2 Preview (Tuần 9-12)
- **VSCode Extension**: Port IntelliJ functionality to VSCode
- **SQL Formatting**: Advanced SQL beautification
- **Query History**: Persistent query storage và search
- **Performance Dashboard**: Basic performance metrics

### Phase 3 Preview (Tuần 13-16)  
- **Cloud Dashboard**: Web-based query analysis
- **Team Collaboration**: Shared query insights
- **Advanced Analytics**: Query performance trends
- **Pro Features**: Enterprise-grade features

---

## 📝 Notes & Considerations

### 🔧 Technical Decisions
- **SQL Interception**: Chọn `StatementInspector` over `DataSourceProxy` cho better Hibernate integration
- **JSON Format**: Line-delimited JSON cho easy streaming và parsing
- **Plugin Architecture**: Kotlin cho IntelliJ plugin development
- **Async Logging**: Non-blocking để minimize performance impact

### ⚠️ Risks & Mitigations
- **Performance Impact**: Implement sampling và async logging
- **Plugin Stability**: Extensive testing với different IntelliJ versions
- **JSON Parsing**: Robust error handling cho malformed data
- **User Experience**: Clear error messages và fallback options

### 🎯 Quality Gates
- **Code Coverage**: >80% unit test coverage
- **Performance**: <1ms overhead per query
- **Documentation**: Complete API documentation
- **Examples**: Working sample applications

---

*Cập nhật lần cuối: [Ngày hiện tại]*  
*Phiên bản: 1.0*  
*Tác giả: Spring JPA Query Visualizer Team*
