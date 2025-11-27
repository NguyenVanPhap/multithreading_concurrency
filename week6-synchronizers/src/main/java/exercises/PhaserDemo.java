package exercises;

import java.util.concurrent.Phaser;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 4: Phaser Demo
 * 
 * TODO Tasks:
 * 1. Multi-phase synchronization
 * 2. Dynamic parties
 * 3. Phase advancement
 */
public class PhaserDemo {
    
    private static final int NUM_THREADS = 5;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Phaser Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test basic Phaser
        System.out.println("Test 1: Basic Phaser");
        testBasicPhaser();
        
        // TODO: Test multi-phase
        System.out.println("\nTest 2: Multi-Phase Phaser");
        testMultiPhasePhaser();
        
        // TODO: Test dynamic parties
        System.out.println("\nTest 3: Dynamic Parties");
        testDynamicParties();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Test basic Phaser
     */
    private static void testBasicPhaser() {
        // TODO: Tạo Phaser với NUM_THREADS parties
        Phaser phaser = null; // TODO: Create
        
        ExecutorService executor = null; // TODO: Create
        
        // TODO: Submit tasks
        // TODO: Mỗi task arriveAndAwaitAdvance()
        // TODO: Quan sát phase advancement
    }
    
    /**
     * TODO: Test multi-phase phaser
     */
    private static void testMultiPhasePhaser() {
        Phaser phaser = null; // TODO: Create
        
        // TODO: Multiple phases
        // TODO: Threads advance qua nhiều phases
        // TODO: Quan sát phase numbers
    }
    
    /**
     * TODO: Test dynamic parties
     */
    private static void testDynamicParties() {
        Phaser phaser = null; // TODO: Create
        
        // TODO: Register và deregister parties dynamically
        // TODO: Quan sát behavior
    }
}

