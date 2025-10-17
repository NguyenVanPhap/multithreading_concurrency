package com.springjpa.visualizer.config;

import com.springjpa.visualizer.interceptor.HibernateStatementInspector;
import com.springjpa.visualizer.logger.QueryLogger;
import com.springjpa.visualizer.parser.StackTraceParser;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for VisualizerConfig auto-configuration
 */
class VisualizerConfigIntegrationTest {
    
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(VisualizerConfig.class));
    
    @Test
    void testAutoConfigurationWithDefaultProperties() {
        contextRunner
                .withPropertyValues("spring.jpa.visualizer.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    assertThat(context).hasSingleBean(StatementInspector.class);
                    assertThat(context).hasSingleBean(StackTraceParser.class);
                    assertThat(context).hasSingleBean(VisualizerProperties.class);
                    
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    assertThat(queryLogger.isEnabled()).isTrue();
                    
                    StatementInspector inspector = context.getBean(StatementInspector.class);
                    assertThat(inspector).isInstanceOf(HibernateStatementInspector.class);
                });
    }
    
    @Test
    void testAutoConfigurationWithFileOutput() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.type=file",
                        "spring.jpa.visualizer.output.file.path=./test-logs/sql-queries.json"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    assertThat(queryLogger.isEnabled()).isTrue();
                });
    }
    
    @Test
    void testAutoConfigurationWithSocketOutput() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.type=socket",
                        "spring.jpa.visualizer.output.socket.host=localhost",
                        "spring.jpa.visualizer.output.socket.port=7777"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    
                    QueryLogger queryLogger = context.getBean(QueryLogger.class);
                    assertThat(queryLogger.isEnabled()).isTrue();
                });
    }
    
    @Test
    void testAutoConfigurationWithPerformanceSettings() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.performance.slow-query-threshold=2000",
                        "spring.jpa.visualizer.performance.sampling-enabled=true",
                        "spring.jpa.visualizer.performance.sampling-rate=0.5"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(StatementInspector.class);
                    
                    StatementInspector inspector = context.getBean(StatementInspector.class);
                    assertThat(inspector).isInstanceOf(HibernateStatementInspector.class);
                });
    }
    
    @Test
    void testAutoConfigurationWithContextSettings() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.context.include-stack-trace=false",
                        "spring.jpa.visualizer.context.max-stack-depth=5",
                        "spring.jpa.visualizer.context.include-session-info=false"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(VisualizerProperties.class);
                    
                    VisualizerProperties properties = context.getBean(VisualizerProperties.class);
                    assertThat(properties.getContext().isIncludeStackTrace()).isFalse();
                    assertThat(properties.getContext().getMaxStackDepth()).isEqualTo(5);
                    assertThat(properties.getContext().isIncludeSessionInfo()).isFalse();
                });
    }
    
    @Test
    void testAutoConfigurationWhenDisabled() {
        contextRunner
                .withPropertyValues("spring.jpa.visualizer.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(QueryLogger.class);
                    assertThat(context).doesNotHaveBean(StatementInspector.class);
                });
    }
    
    @Test
    void testAutoConfigurationWithExistingBeans() {
        contextRunner
                .withUserConfiguration(ExistingBeansConfiguration.class)
                .withPropertyValues("spring.jpa.visualizer.enabled=true")
                .run(context -> {
                    // Should use existing beans instead of creating new ones
                    assertThat(context).hasSingleBean(QueryLogger.class);
                    assertThat(context).hasSingleBean(StatementInspector.class);
                    assertThat(context).hasSingleBean(StackTraceParser.class);
                });
    }
    
    @Test
    void testAutoConfigurationWithCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "spring.jpa.visualizer.enabled=true",
                        "spring.jpa.visualizer.output.file.max-size=5242880",
                        "spring.jpa.visualizer.output.file.max-files=3",
                        "spring.jpa.visualizer.performance.slow-query-threshold=500"
                )
                .run(context -> {
                    VisualizerProperties properties = context.getBean(VisualizerProperties.class);
                    
                    assertThat(properties.getOutput().getFile().getMaxSize()).isEqualTo(5242880);
                    assertThat(properties.getOutput().getFile().getMaxFiles()).isEqualTo(3);
                    assertThat(properties.getPerformance().getSlowQueryThreshold()).isEqualTo(500);
                });
    }
    
    @Configuration
    static class ExistingBeansConfiguration {
        
        @Bean
        public QueryLogger queryLogger() {
            return new QueryLogger() {
                @Override
                public void log(com.springjpa.visualizer.model.SqlQueryInfo queryInfo) {
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
            return sql -> sql; // Return SQL unchanged
        }
        
        @Bean
        public StackTraceParser stackTraceParser() {
            return new StackTraceParser();
        }
    }
}
