package com.springjpa.visualizer.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springjpa.visualizer.model.SqlQueryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * JSON-based query logger that writes SQL query information to files.
 * Uses asynchronous writing to minimize performance impact on the application.
 */
public class JsonQueryLogger implements QueryLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonQueryLogger.class);
    
    private final ObjectMapper objectMapper;
    private final String logFilePath;
    private final long maxFileSizeBytes;
    private final int maxFiles;
    private final BlockingQueue<SqlQueryInfo> logQueue;
    private final AtomicBoolean enabled;
    private final AtomicBoolean initialized;
    private final AtomicLong queryCounter;
    
    private volatile BufferedWriter writer;
    private volatile Path currentLogFile;
    private Thread logWriterThread;
    
    public JsonQueryLogger(String logFilePath, long maxFileSizeBytes, int maxFiles) {
        this.logFilePath = logFilePath;
        this.maxFileSizeBytes = maxFileSizeBytes;
        this.maxFiles = maxFiles;
        this.logQueue = new LinkedBlockingQueue<>();
        this.enabled = new AtomicBoolean(true);
        this.initialized = new AtomicBoolean(false);
        this.queryCounter = new AtomicLong(0);
        
        // Configure Jackson ObjectMapper
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
    @Override
    public void log(SqlQueryInfo queryInfo) {
        if (!enabled.get() || queryInfo == null) {
            return;
        }
        
        try {
            // Add to queue for asynchronous processing
            boolean added = logQueue.offer(queryInfo);
            if (!added) {
                logger.warn("Log queue is full, dropping query: {}", queryInfo.getQueryId());
            }
        } catch (Exception e) {
            logger.warn("Failed to queue query for logging: {}", e.getMessage());
        }
    }
    
    @Override
    public void initialize() {
        if (initialized.compareAndSet(false, true)) {
            try {
                // Create log directory if it doesn't exist
                Path logDir = Paths.get(logFilePath).getParent();
                if (logDir != null && !Files.exists(logDir)) {
                    Files.createDirectories(logDir);
                }
                
                // Initialize current log file
                initializeLogFile();
                
                // Start background writer thread
                startLogWriterThread();
                
                logger.info("JsonQueryLogger initialized with log file: {}", currentLogFile);
                
            } catch (Exception e) {
                logger.error("Failed to initialize JsonQueryLogger: {}", e.getMessage(), e);
                initialized.set(false);
            }
        }
    }
    
    @Override
    public void shutdown() {
        enabled.set(false);
        
        if (logWriterThread != null && logWriterThread.isAlive()) {
            logWriterThread.interrupt();
            try {
                logWriterThread.join(5000); // Wait up to 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Flush remaining logs
        flushRemainingLogs();
        
        // Close writer
        closeWriter();
        
        logger.info("JsonQueryLogger shutdown completed");
    }
    
    @Override
    public boolean isEnabled() {
        return enabled.get();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
        logger.info("JsonQueryLogger {}", enabled ? "enabled" : "disabled");
    }
    
    @Override
    public String getName() {
        return "JsonQueryLogger";
    }
    
    /**
     * Initialize the current log file
     */
    private void initializeLogFile() throws IOException {
        currentLogFile = Paths.get(logFilePath);
        
        // Create file if it doesn't exist
        if (!Files.exists(currentLogFile)) {
            Files.createFile(currentLogFile);
        }
        
        // Open writer
        writer = new BufferedWriter(new FileWriter(currentLogFile.toFile(), true));
    }
    
    /**
     * Start the background log writer thread
     */
    private void startLogWriterThread() {
        logWriterThread = new Thread(() -> {
            logger.debug("Log writer thread started");
            
            while (enabled.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    // Take query from queue (blocks until available)
                    SqlQueryInfo queryInfo = logQueue.take();
                    
                    // Write to file
                    writeQueryToFile(queryInfo);
                    
                    // Check if file rotation is needed
                    checkAndRotateFile();
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.warn("Error in log writer thread: {}", e.getMessage());
                }
            }
            
            logger.debug("Log writer thread stopped");
        }, "SqlQueryLogger-Writer");
        
        logWriterThread.setDaemon(true);
        logWriterThread.start();
    }
    
    /**
     * Write a single query to the log file
     */
    private void writeQueryToFile(SqlQueryInfo queryInfo) {
        try {
            // Convert to JSON
            String json = objectMapper.writeValueAsString(queryInfo);
            
            // Write to file
            synchronized (writer) {
                writer.write(json);
                writer.newLine();
                writer.flush();
            }
            
            // Increment counter
            long count = queryCounter.incrementAndGet();
            
            if (count % 100 == 0) {
                logger.debug("Logged {} queries", count);
            }
            
        } catch (Exception e) {
            logger.warn("Failed to write query to file: {}", e.getMessage());
        }
    }
    
    /**
     * Check if file rotation is needed and perform it
     */
    private void checkAndRotateFile() {
        try {
            if (Files.size(currentLogFile) > maxFileSizeBytes) {
                rotateLogFile();
            }
        } catch (IOException e) {
            logger.warn("Failed to check file size for rotation: {}", e.getMessage());
        }
    }
    
    /**
     * Rotate the log file
     */
    private void rotateLogFile() throws IOException {
        logger.info("Rotating log file: {}", currentLogFile);
        
        // Close current writer
        closeWriter();
        
        // Create new file with timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = currentLogFile.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        
        Path newFile = currentLogFile.getParent().resolve(baseName + "_" + timestamp + extension);
        
        // Move current file to new name
        Files.move(currentLogFile, newFile);
        
        // Clean up old files
        cleanupOldFiles();
        
        // Initialize new log file
        initializeLogFile();
    }
    
    /**
     * Clean up old log files
     */
    private void cleanupOldFiles() {
        try {
            Path logDir = currentLogFile.getParent();
            String baseName = currentLogFile.getFileName().toString();
            baseName = baseName.substring(0, baseName.lastIndexOf('.'));
            
            Files.list(logDir)
                .filter(path -> path.getFileName().toString().startsWith(baseName))
                .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))
                .skip(maxFiles)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        logger.debug("Deleted old log file: {}", path);
                    } catch (IOException e) {
                        logger.warn("Failed to delete old log file {}: {}", path, e.getMessage());
                    }
                });
                
        } catch (IOException e) {
            logger.warn("Failed to cleanup old log files: {}", e.getMessage());
        }
    }
    
    /**
     * Flush remaining logs in queue
     */
    private void flushRemainingLogs() {
        logger.debug("Flushing {} remaining logs", logQueue.size());
        
        while (!logQueue.isEmpty()) {
            try {
                SqlQueryInfo queryInfo = logQueue.poll();
                if (queryInfo != null) {
                    writeQueryToFile(queryInfo);
                }
            } catch (Exception e) {
                logger.warn("Error flushing remaining logs: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Close the current writer
     */
    private void closeWriter() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.warn("Failed to close log writer: {}", e.getMessage());
            }
            writer = null;
        }
    }
    
    /**
     * Get current log file path
     */
    public Path getCurrentLogFile() {
        return currentLogFile;
    }
    
    /**
     * Get total number of queries logged
     */
    public long getLoggedQueryCount() {
        return queryCounter.get();
    }
    
    /**
     * Get current queue size
     */
    public int getQueueSize() {
        return logQueue.size();
    }
}
