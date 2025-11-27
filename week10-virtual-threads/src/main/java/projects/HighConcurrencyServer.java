package projects;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project 1: High-Concurrency Server
 * 
 * Server với virtual threads
 * - Handle many concurrent connections
 * - I/O-bound operations
 * - Performance testing
 * 
 * TODO Tasks:
 * 1. Implement Server với virtual thread executor
 * 2. Handle connections
 * 3. I/O operations
 * 4. Statistics
 */
public class HighConcurrencyServer {
    
    private static final int NUM_CONNECTIONS = 100000;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  High-Concurrency Server Demo");
        System.out.println("==========================================\n");
        
        // TODO: Tạo server
        Server server = null; // TODO: Create
        
        // TODO: Handle many connections
        // TODO: Measure performance
        // TODO: In ra statistics
    }
    
    /**
     * TODO: Implement Server class
     */
    static class Server {
        private final ExecutorService executor;
        private final AtomicInteger connectionCount;
        
        public Server() {
            this.executor = null; // TODO: Create virtual thread executor
            this.connectionCount = null; // TODO: Create AtomicInteger
        }
        
        public void handleConnection(Connection connection) {
            // TODO: Submit connection handling task
            // TODO: Simulate I/O operations (sleep)
            // TODO: Increment connection count
        }
        
        public void shutdown() {
            // TODO: Shutdown executor
        }
        
        public int getConnectionCount() {
            // TODO: Return connection count
            return 0; // TODO: Return count
        }
    }
    
    static class Connection {
        private final int id;
        
        public Connection(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
    }
}

