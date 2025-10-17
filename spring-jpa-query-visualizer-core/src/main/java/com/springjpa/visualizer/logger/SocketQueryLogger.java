package com.springjpa.visualizer.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springjpa.visualizer.model.SqlQueryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Socket-based query logger that sends SQL query information to a remote socket.
 * Useful for real-time monitoring and IDE integration.
 */
public class SocketQueryLogger implements QueryLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(SocketQueryLogger.class);
    
    private final String host;
    private final int port;
    private final ObjectMapper objectMapper;
    private final BlockingQueue<SqlQueryInfo> logQueue;
    private final AtomicBoolean enabled;
    private final AtomicBoolean initialized;
    private final AtomicLong queryCounter;
    
    private volatile Socket socket;
    private volatile OutputStream outputStream;
    private Thread logWriterThread;
    
    public SocketQueryLogger(String host, int port) {
        this.host = host;
        this.port = port;
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
                // Start background writer thread
                startLogWriterThread();
                
                logger.info("SocketQueryLogger initialized for {}:{}", host, port);
                
            } catch (Exception e) {
                logger.error("Failed to initialize SocketQueryLogger: {}", e.getMessage(), e);
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
        
        // Close socket connection
        closeSocket();
        
        logger.info("SocketQueryLogger shutdown completed");
    }
    
    @Override
    public boolean isEnabled() {
        return enabled.get();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
        logger.info("SocketQueryLogger {}", enabled ? "enabled" : "disabled");
    }
    
    @Override
    public String getName() {
        return "SocketQueryLogger";
    }
    
    /**
     * Start the background log writer thread
     */
    private void startLogWriterThread() {
        logWriterThread = new Thread(() -> {
            logger.debug("Socket log writer thread started");
            
            while (enabled.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    // Take query from queue (blocks until available)
                    SqlQueryInfo queryInfo = logQueue.take();
                    
                    // Send to socket
                    sendQueryToSocket(queryInfo);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.warn("Error in socket log writer thread: {}", e.getMessage());
                    // Try to reconnect
                    reconnectSocket();
                }
            }
            
            logger.debug("Socket log writer thread stopped");
        }, "SocketQueryLogger-Writer");
        
        logWriterThread.setDaemon(true);
        logWriterThread.start();
    }
    
    /**
     * Send a single query to the socket
     */
    private void sendQueryToSocket(SqlQueryInfo queryInfo) {
        try {
            // Ensure socket is connected
            if (!isSocketConnected()) {
                connectSocket();
            }
            
            // Convert to JSON
            String json = objectMapper.writeValueAsString(queryInfo);
            
            // Send to socket
            synchronized (this) {
                if (outputStream != null) {
                    outputStream.write(json.getBytes());
                    outputStream.write('\n'); // Add newline delimiter
                    outputStream.flush();
                }
            }
            
            // Increment counter
            long count = queryCounter.incrementAndGet();
            
            if (count % 100 == 0) {
                logger.debug("Sent {} queries to socket", count);
            }
            
        } catch (Exception e) {
            logger.warn("Failed to send query to socket: {}", e.getMessage());
            // Mark socket as disconnected for reconnection
            closeSocket();
        }
    }
    
    /**
     * Connect to the socket
     */
    private void connectSocket() throws IOException {
        logger.debug("Connecting to socket {}:{}", host, port);
        
        socket = new Socket(host, port);
        outputStream = socket.getOutputStream();
        
        logger.info("Connected to socket {}:{}", host, port);
    }
    
    /**
     * Reconnect to the socket
     */
    private void reconnectSocket() {
        closeSocket();
        
        // Wait a bit before reconnecting
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        try {
            connectSocket();
        } catch (IOException e) {
            logger.warn("Failed to reconnect to socket {}:{}: {}", host, port, e.getMessage());
        }
    }
    
    /**
     * Check if socket is connected
     */
    private boolean isSocketConnected() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
    
    /**
     * Close the socket connection
     */
    private void closeSocket() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.warn("Failed to close output stream: {}", e.getMessage());
            }
            outputStream = null;
        }
        
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.warn("Failed to close socket: {}", e.getMessage());
            }
            socket = null;
        }
    }
    
    /**
     * Get the socket host
     */
    public String getHost() {
        return host;
    }
    
    /**
     * Get the socket port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Get total number of queries sent
     */
    public long getSentQueryCount() {
        return queryCounter.get();
    }
    
    /**
     * Get current queue size
     */
    public int getQueueSize() {
        return logQueue.size();
    }
    
    /**
     * Check if currently connected to socket
     */
    public boolean isConnected() {
        return isSocketConnected();
    }
}
