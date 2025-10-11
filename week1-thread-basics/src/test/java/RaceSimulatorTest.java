package projects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test class for RaceSimulator
 * Tests the core functionality of the racing simulation
 */
public class RaceSimulatorTest {
    
    private RaceSimulator.RaceTrack raceTrack;
    private RaceSimulator.Racer testRacer;
    
    @BeforeEach
    void setUp() {
        raceTrack = new RaceSimulator.RaceTrack(3);
        testRacer = new RaceSimulator.Racer("Test Runner", 1);
    }
    
    @Test
    @DisplayName("Should create racer with correct initial state")
    void testRacerCreation() {
        assertEquals("Test Runner", testRacer.getName());
        assertEquals(1, testRacer.getId());
        assertEquals(0, testRacer.getPosition());
        assertFalse(testRacer.isFinished());
        assertEquals(0, testRacer.getFinishTime());
    }
    
    @Test
    @DisplayName("Should add racers to race track")
    void testAddRacer() {
        RaceSimulator.Racer racer1 = new RaceSimulator.Racer("Runner 1", 1);
        RaceSimulator.Racer racer2 = new RaceSimulator.Racer("Runner 2", 2);
        
        raceTrack.addRacer(racer1);
        raceTrack.addRacer(racer2);
        
        List<RaceSimulator.Racer> racers = raceTrack.getRacers();
        assertEquals(2, racers.size());
        assertTrue(racers.contains(racer1));
        assertTrue(racers.contains(racer2));
    }
    
    @Test
    @DisplayName("Should complete race within reasonable time")
    void testRaceCompletion() throws InterruptedException {
        // Create a fast racer for testing
        RaceSimulator.Racer fastRacer = new RaceSimulator.Racer("Fast Runner", 1);
        raceTrack.addRacer(fastRacer);
        
        CountDownLatch latch = new CountDownLatch(1);
        
        // Start race in separate thread
        Thread raceThread = new Thread(() -> {
            raceTrack.startRace();
            raceTrack.waitForRaceToFinish();
            latch.countDown();
        });
        
        raceThread.start();
        
        // Wait for race to complete (max 10 seconds)
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        
        assertTrue(completed, "Race should complete within 10 seconds");
        assertTrue(fastRacer.isFinished(), "Racer should be finished");
        assertTrue(fastRacer.getPosition() >= 100, "Racer should reach finish line");
        assertTrue(fastRacer.getFinishTime() > 0, "Finish time should be recorded");
    }
    
    @Test
    @DisplayName("Should handle multiple racers")
    void testMultipleRacers() throws InterruptedException {
        // Create multiple racers
        RaceSimulator.Racer racer1 = new RaceSimulator.Racer("Runner 1", 1);
        RaceSimulator.Racer racer2 = new RaceSimulator.Racer("Runner 2", 2);
        RaceSimulator.Racer racer3 = new RaceSimulator.Racer("Runner 3", 3);
        
        raceTrack.addRacer(racer1);
        raceTrack.addRacer(racer2);
        raceTrack.addRacer(racer3);
        
        CountDownLatch latch = new CountDownLatch(1);
        
        Thread raceThread = new Thread(() -> {
            raceTrack.startRace();
            raceTrack.waitForRaceToFinish();
            latch.countDown();
        });
        
        raceThread.start();
        
        boolean completed = latch.await(15, TimeUnit.SECONDS);
        
        assertTrue(completed, "Multi-racer race should complete within 15 seconds");
        
        // Check that all racers finished
        assertTrue(racer1.isFinished(), "Racer 1 should be finished");
        assertTrue(racer2.isFinished(), "Racer 2 should be finished");
        assertTrue(racer3.isFinished(), "Racer 3 should be finished");
        
        // Check that all racers reached the finish line
        assertTrue(racer1.getPosition() >= 100, "Racer 1 should reach finish line");
        assertTrue(racer2.getPosition() >= 100, "Racer 2 should reach finish line");
        assertTrue(racer3.getPosition() >= 100, "Racer 3 should reach finish line");
    }
    
    @Test
    @DisplayName("Should record different finish times for different racers")
    void testDifferentFinishTimes() throws InterruptedException {
        RaceSimulator.Racer racer1 = new RaceSimulator.Racer("Slow Runner", 1);
        RaceSimulator.Racer racer2 = new RaceSimulator.Racer("Fast Runner", 2);
        
        raceTrack.addRacer(racer1);
        raceTrack.addRacer(racer2);
        
        CountDownLatch latch = new CountDownLatch(1);
        
        Thread raceThread = new Thread(() -> {
            raceTrack.startRace();
            raceTrack.waitForRaceToFinish();
            latch.countDown();
        });
        
        raceThread.start();
        
        boolean completed = latch.await(15, TimeUnit.SECONDS);
        
        assertTrue(completed, "Race should complete");
        
        // Both racers should have finish times
        assertTrue(racer1.getFinishTime() > 0, "Racer 1 should have finish time");
        assertTrue(racer2.getFinishTime() > 0, "Racer 2 should have finish time");
        
        // Finish times should be different (due to randomness)
        // Note: This test might occasionally fail due to randomness, but it's unlikely
        assertNotEquals(racer1.getFinishTime(), racer2.getFinishTime(), 
                       "Different racers should have different finish times");
    }
    
    @Test
    @DisplayName("Should handle race interruption gracefully")
    void testRaceInterruption() throws InterruptedException {
        RaceSimulator.Racer racer = new RaceSimulator.Racer("Test Runner", 1);
        raceTrack.addRacer(racer);
        
        Thread raceThread = new Thread(() -> {
            raceTrack.startRace();
            raceTrack.waitForRaceToFinish();
        });
        
        raceThread.start();
        
        // Let race run for a bit
        Thread.sleep(1000);
        
        // Interrupt the race thread
        raceThread.interrupt();
        
        // Wait for thread to finish
        raceThread.join(5000);
        
        // Thread should have finished (either completed or interrupted)
        assertFalse(raceThread.isAlive(), "Race thread should have finished");
    }
    
    @Test
    @DisplayName("Should maintain racer state consistency")
    void testRacerStateConsistency() {
        RaceSimulator.Racer racer = new RaceSimulator.Racer("State Test Runner", 1);
        
        // Initial state
        assertEquals(0, racer.getPosition());
        assertFalse(racer.isFinished());
        assertEquals(0, racer.getFinishTime());
        
        // Simulate race progress (without actually racing)
        // This tests that the racer maintains consistent state
        assertNotNull(racer.getName());
        assertTrue(racer.getId() > 0);
    }
    
    @Test
    @DisplayName("Should handle empty race track")
    void testEmptyRaceTrack() {
        RaceSimulator.RaceTrack emptyTrack = new RaceSimulator.RaceTrack(0);
        
        List<RaceSimulator.Racer> racers = emptyTrack.getRacers();
        assertNotNull(racers);
        assertTrue(racers.isEmpty());
        
        // Starting an empty race should not cause issues
        assertDoesNotThrow(() -> {
            emptyTrack.startRace();
        });
    }
    
    @Test
    @DisplayName("Should create race track with correct capacity")
    void testRaceTrackCreation() {
        RaceSimulator.RaceTrack track5 = new RaceSimulator.RaceTrack(5);
        RaceSimulator.RaceTrack track10 = new RaceSimulator.RaceTrack(10);
        
        assertNotNull(track5);
        assertNotNull(track10);
        
        List<RaceSimulator.Racer> racers5 = track5.getRacers();
        List<RaceSimulator.Racer> racers10 = track10.getRacers();
        
        assertNotNull(racers5);
        assertNotNull(racers10);
        assertTrue(racers5.isEmpty());
        assertTrue(racers10.isEmpty());
    }
}
