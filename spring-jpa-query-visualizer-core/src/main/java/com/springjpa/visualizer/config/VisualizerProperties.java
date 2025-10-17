package com.springjpa.visualizer.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Spring JPA Query Visualizer.
 * Maps to spring.jpa.visualizer.* properties in application.yml/properties.
 */
@ConfigurationProperties(prefix = "spring.jpa.visualizer")
@Validated
@ValidVisualizerProperties
public class VisualizerProperties {
    
    /**
     * Whether the visualizer is enabled
     */
    private boolean enabled = true;
    
    /**
     * Output configuration
     */
    @Valid
    @NotNull
    private Output output = new Output();
    
    /**
     * Performance configuration
     */
    @Valid
    @NotNull
    private Performance performance = new Performance();
    
    /**
     * Context configuration
     */
    @Valid
    @NotNull
    private Context context = new Context();
    
    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Output getOutput() {
        return output;
    }
    
    public void setOutput(Output output) {
        this.output = output;
    }
    
    public Performance getPerformance() {
        return performance;
    }
    
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }
    
    public Context getContext() {
        return context;
    }
    
    public void setContext(Context context) {
        this.context = context;
    }
    
    /**
     * Output configuration
     */
    public static class Output {
        
        /**
         * Output type: file or socket
         */
        private Type type = Type.FILE;
        
        /**
         * File output configuration
         */
        private File file = new File();
        
        /**
         * Socket output configuration
         */
        private Socket socket = new Socket();
        
        // Getters and Setters
        public Type getType() {
            return type;
        }
        
        public void setType(Type type) {
            this.type = type;
        }
        
        public File getFile() {
            return file;
        }
        
        public void setFile(File file) {
            this.file = file;
        }
        
        public Socket getSocket() {
            return socket;
        }
        
        public void setSocket(Socket socket) {
            this.socket = socket;
        }
        
        /**
         * Output type enumeration
         */
        public enum Type {
            FILE, SOCKET
        }
        
        /**
         * File output configuration
         */
        public static class File {
            
            /**
             * Log file path
             */
            @NotBlank(message = "Log file path cannot be blank")
            private String path = "./logs/sql-queries.json";
            
            /**
             * Maximum file size in bytes before rotation
             */
            @Min(value = 1, message = "Max file size must be at least 1 byte")
            private long maxSize = 10 * 1024 * 1024; // 10MB
            
            /**
             * Maximum number of log files to keep
             */
            @Min(value = 1, message = "Max files must be at least 1")
            private int maxFiles = 5;
            
            // Getters and Setters
            public String getPath() {
                return path;
            }
            
            public void setPath(String path) {
                this.path = path;
            }
            
            public long getMaxSize() {
                return maxSize;
            }
            
            public void setMaxSize(long maxSize) {
                this.maxSize = maxSize;
            }
            
            public int getMaxFiles() {
                return maxFiles;
            }
            
            public void setMaxFiles(int maxFiles) {
                this.maxFiles = maxFiles;
            }
        }
        
        /**
         * Socket output configuration
         */
        public static class Socket {
            
            /**
             * Socket host
             */
            @NotBlank(message = "Socket host cannot be blank")
            private String host = "localhost";
            
            /**
             * Socket port
             */
            @Min(value = 1, message = "Port must be at least 1")
            private int port = 7777;
            
            /**
             * Connection timeout in milliseconds
             */
            @Min(value = 1, message = "Timeout must be at least 1ms")
            private int timeout = 5000;
            
            // Getters and Setters
            public String getHost() {
                return host;
            }
            
            public void setHost(String host) {
                this.host = host;
            }
            
            public int getPort() {
                return port;
            }
            
            public void setPort(int port) {
                this.port = port;
            }
            
            public int getTimeout() {
                return timeout;
            }
            
            public void setTimeout(int timeout) {
                this.timeout = timeout;
            }
        }
    }
    
    /**
     * Performance configuration
     */
    public static class Performance {
        
        /**
         * Whether performance monitoring is enabled
         */
        private boolean enabled = true;
        
        /**
         * Slow query threshold in milliseconds
         */
        @Min(value = 0, message = "Slow query threshold cannot be negative")
        private long slowQueryThreshold = 1000;
        
        /**
         * Whether to sample queries to reduce overhead
         */
        private boolean samplingEnabled = false;
        
        /**
         * Sampling rate (1.0 = 100%, 0.1 = 10%)
         */
        private double samplingRate = 1.0;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public long getSlowQueryThreshold() {
            return slowQueryThreshold;
        }
        
        public void setSlowQueryThreshold(long slowQueryThreshold) {
            this.slowQueryThreshold = slowQueryThreshold;
        }
        
        public boolean isSamplingEnabled() {
            return samplingEnabled;
        }
        
        public void setSamplingEnabled(boolean samplingEnabled) {
            this.samplingEnabled = samplingEnabled;
        }
        
        public double getSamplingRate() {
            return samplingRate;
        }
        
        public void setSamplingRate(double samplingRate) {
            this.samplingRate = samplingRate;
        }
    }
    
    /**
     * Context configuration
     */
    public static class Context {
        
        /**
         * Whether to include stack trace in query info
         */
        private boolean includeStackTrace = true;
        
        /**
         * Maximum stack trace depth
         */
        @Min(value = 1, message = "Max stack depth must be at least 1")
        private int maxStackDepth = 10;
        
        /**
         * Whether to include session information
         */
        private boolean includeSessionInfo = true;
        
        /**
         * Whether to include thread information
         */
        private boolean includeThreadInfo = true;
        
        // Getters and Setters
        public boolean isIncludeStackTrace() {
            return includeStackTrace;
        }
        
        public void setIncludeStackTrace(boolean includeStackTrace) {
            this.includeStackTrace = includeStackTrace;
        }
        
        public int getMaxStackDepth() {
            return maxStackDepth;
        }
        
        public void setMaxStackDepth(int maxStackDepth) {
            this.maxStackDepth = maxStackDepth;
        }
        
        public boolean isIncludeSessionInfo() {
            return includeSessionInfo;
        }
        
        public void setIncludeSessionInfo(boolean includeSessionInfo) {
            this.includeSessionInfo = includeSessionInfo;
        }
        
        public boolean isIncludeThreadInfo() {
            return includeThreadInfo;
        }
        
        public void setIncludeThreadInfo(boolean includeThreadInfo) {
            this.includeThreadInfo = includeThreadInfo;
        }
    }
}
