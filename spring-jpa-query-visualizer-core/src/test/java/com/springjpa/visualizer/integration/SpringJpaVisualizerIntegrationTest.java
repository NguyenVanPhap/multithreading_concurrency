package com.springjpa.visualizer.integration;

import com.springjpa.visualizer.config.VisualizerProperties;
import com.springjpa.visualizer.interceptor.HibernateStatementInspector;
import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.model.SqlQueryInfo;
import com.springjpa.visualizer.parser.StackTraceParser;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive integration tests for Spring JPA Query Visualizer
 */
class SpringJpaVisualizerIntegrationTest {
    
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    com.springjpa.visualizer.config.VisualizerConfig.class,
                    com.springjpa.visualizer.VisualizerAutoConfiguration.class
            ));
    
    @Test
    void testCompleteIntegrationWithFileLogging() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.type=file",
                        "spring.jpa.visualizer.output.file.path=./test-logs/integration-test.json",
                        "spring.jpa.visualizer.performance.slow-query-threshold=500",
                        "spring.jpa.visualizer.context.include-stack-trace=true"
                )
                .run(context -> {
                    // Verify all beans are created
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    assertThat(context).hasSingleBean(StatementInspector.class);
                    assertThat(context).hasSingleBean(StackTraceParser.class);
                    assertThat(context).hasSingleBean(VisualizerProperties.class);
                    
                    // Get beans
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    StatementInspector inspector = context.getBean(StatementInspector.class);
                    VisualizerProperties properties = context.getBean(VisualizerProperties.class);
                    
                    // Verify configuration
                    assertThat(queryLogger.isEnabled()).isTrue();
                    assertThat(inspector).isInstanceOf(HibernateStatementInspector.class);
                    assertThat(properties.isEnabled()).isTrue();
                    assertThat(properties.getOutput().getType()).isEqualTo(VisualizerProperties.Output.Type.FILE);
                    assertThat(properties.getPerformance().getSlowQueryThreshold()).isEqualTo(500);
                    
                    // Test SQL interception
                    HibernateStatementInspector hibernateInspector = (HibernateStatementInspector) inspector;
                    String testSql = "SELECT * FROM users WHERE id = ?";
                    
                    String result = hibernateInspector.inspect(testSql);
                    assertThat(result).isEqualTo(testSql); // Should return original SQL
                    
                    // Verify performance monitoring is working
                    var performanceStats = hibernateInspector.getPerformanceStats();
                    assertThat(performanceStats).isNotNull();
                    
                    // Verify sampling is working
                    var samplingStats = hibernateInspector.getSamplingStats();
                    assertThat(samplingStats).isNotNull();
                });
    }
    
    @Test
    void testIntegrationWithSocketLogging() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.type=socket",
                        "spring.jpa.visualizer.output.socket.host=localhost",
                        "spring.jpa.visualizer.output.socket.port=7777",
                        "spring.jpa.visualizer.output.socket.timeout=5000"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    assertThat(queryLogger.isEnabled()).isTrue();
                    assertThat(queryLogger.getName()).contains("Socket");
                });
    }
    
    @Test
    void testIntegrationWithPerformanceMonitoring() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.performance.enabled=true",
                        "spring.jpa.visualizer.performance.slow-query-threshold=100",
                        "spring.jpa.visualizer.performance.sampling-enabled=true",
                        "spring.jpa.visualizer.performance.sampling-rate=0.5"
                )
                .run(context -> {
                    StatementInspector inspector = context.getBean(StatementInspector.class);
                    assertThat(inspector).isInstanceOf(HibernateStatementInspector.class);
                    
                    HibernateStatementInspector hibernateInspector = (HibernateStatementInspector) inspector;
                    
                    // Test multiple queries
                    String[] queries = {
                        "SELECT * FROM users",
                        "INSERT INTO users VALUES (?, ?)",
                        "UPDATE users SET name = ?",
                        "DELETE FROM users WHERE id = ?"
                    };
                    
                    for (String sql : queries) {
                        String result = hibernateInspector.inspect(sql);
                        assertThat(result).isEqualTo(sql);
                    }
                    
                    // Verify performance monitoring
                    var performanceStats = hibernateInspector.getPerformanceStats();
                    assertThat(performanceStats).isNotNull();
                    
                    // Verify entity statistics
                    var entityStats = hibernateInspector.getEntityStats();
                    assertThat(entityStats).isNotNull();
                    
                    // Verify query type statistics
                    var queryTypeStats = hibernateInspector.getQueryTypeStats();
                    assertThat(queryTypeStats).isNotNull();
                });
    }
    
    @Test
    void testIntegrationWithCustomConfiguration() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.file.path=./custom-logs/queries.json",
                        "spring.jpa.visualizer.output.file.max-size=5242880",
                        "spring.jpa.visualizer.output.file.max-files=3",
                        "spring.jpa.visualizer.performance.slow-query-threshold=2000",
                        "spring.jpa.visualizer.context.max-stack-depth=5",
                        "spring.jpa.visualizer.context.include-session-info=false"
                )
                .run(context -> {
                    VisualizerProperties properties = context.getBean(VisualizerProperties.class);
                    
                    // Verify custom configuration
                    assertThat(properties.getOutput().getFile().getPath())
                            .isEqualTo("./custom-logs/queries.json");
                    assertThat(properties.getOutput().getFile().getMaxSize())
                            .isEqualTo(5242880);
                    assertThat(properties.getOutput().getFile().getMaxFiles())
                            .isEqualTo(3);
                    assertThat(properties.getPerformance().getSlowQueryThreshold())
                            .isEqualTo(2000);
                    assertThat(properties.getContext().getMaxStackDepth())
                            .isEqualTo(5);
                    assertThat(properties.getContext().isIncludeSessionInfo())
                            .isFalse();
                });
    }
    
    @Test
    void testIntegrationWhenDisabled() {
        contextRunner
                .withPropertyValues("spring.jpa.visualizer.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(QueryLogger.class);
                    assertThat(context).doesNotHaveBean(StatementInspector.class);
                });
    }
    
    @Test
    void testIntegrationWithExistingBeans() {
        contextRunner
                .withUserConfiguration(ExistingBeansConfiguration.class)
                .withPropertyValues("spring.jpa.visualizer.enabled=true")
                .run(context -> {
                    // Should use existing beans
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    assertThat(context).hasSingleBean(StatementInspector.class);
                    
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    assertThat(queryLogger.getName()).isEqualTo("TestQueryLogger");
                });
    }
    
    @Test
    void testIntegrationWithMockLogger() {
        AtomicInteger logCount = new AtomicInteger(0);
        
        contextRunner
                .withUserConfiguration(MockLoggerConfiguration.class)
                .withPropertyValues("spring.jpa.visualizer.enabled=true")
                .run(context -> {
                    StatementInspector inspector = context.getBean(StatementInspector.class);
                    assertThat(inspector).isInstanceOf(HibernateStatementInspector.class);
                    
                    HibernateStatementInspector hibernateInspector = (HibernateStatementInspector) inspector;
                    
                    // Test SQL interception
                    String testSql = "SELECT * FROM users WHERE email = ?";
                    String result = hibernateInspector.inspect(testSql);
                    assertThat(result).isEqualTo(testSql);
                    
                    // Verify logger was called
                    assertThat(logCount.get()).isGreaterThan(0);
                });
    }
    
    @Configuration
    static class ExistingBeansConfiguration {
        
        @Bean
        public QueryLogger queryLogger() {
            return new QueryLogger() {
                @Override
                public void log(SqlQueryInfo queryInfo) {
                    // No-op implementation
                }
                
                @Override
                public boolean isEnabled() {
                    return true;
                }
                
                @Override
                public void setEnabled(boolean enabled) {
                    // No-op implementation
                }
                
                @Override
                public String getName() {
                    return "TestQueryLogger";
                }
                
                @Override
                public void initialize() {
                    // No-op implementation
                }
                
                @Override
                public void shutdown() {
                    // No-op implementation
                }
            };
        }
        
        @Bean
        public StatementInspector statementInspector() {
            return sql -> sql;
        }
        
        @Bean
        public StackTraceParser stackTraceParser() {
            return new StackTraceParser();
        }
    }
    
    @Configuration
    static class MockLoggerConfiguration {
        
        @Bean
        public QueryLogger queryLogger() {
            return new QueryLogger() {
                private final AtomicInteger logCount = new AtomicInteger(0);
                
                @Override
                public void log(SqlQueryInfo queryInfo) {
                    logCount.incrementAndGet();
                }
                
                @Override
                public boolean isEnabled() {
                    return true;
                }
                
                @Override
                public void setEnabled(boolean enabled) {
                    // No-op implementation
                }
                
                @Override
                public String getName() {
                    return "MockQueryLogger";
                }
                
                @Override
                public void initialize() {
                    // No-op implementation
                }
                
                @Override
                public void shutdown() {
                    // No-op implementation
                }
            };
        }
    }
}
