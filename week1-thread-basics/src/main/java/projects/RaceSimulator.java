package projects;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Race Simulator - Week 1 Mini Project
 * 
 * TODO: Complete this fun racing simulation!
 * 
 * Learning objectives:
 * - Multiple racers (threads) with different speeds
 * - Real-time progress tracking
 * - Winner determination
 * - Performance comparison (1 vs N threads)
 * - Random movement simulation
 * - Thread lifecycle management
 */
public class RaceSimulator {
    
    private static final int RACE_DISTANCE = 100;
    private static final int DISPLAY_WIDTH = 50;
    
    public static void main(String[] args) {
        System.out.println("🏁 === RACE SIMULATOR === 🏁\n");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Single Racer Demo");
            System.out.println("2. Multi-Racer Race");
            System.out.println("3. Performance Comparison");
            System.out.println("4. Exit");
            System.out.print("Enter choice (1-4): ");
            
            try {
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        runSingleRacerDemo();
                        break;
                    case 2:
                        runMultiRacerRace();
                        break;
                    case 3:
                        runPerformanceComparison();
                        break;
                    case 4:
                        System.out.println("Thanks for racing! 🏁");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1-4.");
                }
                
                System.out.println("\n" + "=".repeat(60) + "\n");
                
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    /**
     * TODO: Demo with a single racer
     * 
     * Your tasks:
     * 1. Create a single Racer instance
     * 2. Create a RaceTrack for 1 racer
     * 3. Add racer to track
     * 4. Start race and wait for completion
     * 5. Display results
     */
    private static void runSingleRacerDemo() {
        System.out.println("\n--- Single Racer Demo ---");

        // TODO: Create a Racer with name "Solo Runner" and id 1
        // TODO: Create RaceTrack for 1 racer
        // TODO: Add racer to track
        // TODO: Start race
        // TODO: Wait for race to finish
        // TODO: Display results

        System.out.println("TODO: Implement runSingleRacerDemo()");
        System.out.println("Hint: create a Racer, a RaceTrack with 1 slot, add the racer, start, wait, then display results.");
        // Early return to keep program runnable while you implement this demo
        return;
    }
    
    /**
     * TODO: Multi-racer race simulation
     * 
     * Your tasks:
     * 1. Get user input for number of racers
     * 2. Validate input (2-10 racers)
     * 3. Create RaceTrack and racers with names and emojis
     * 4. Start race and wait for completion
     * 5. Handle input validation errors
     */
    private static void runMultiRacerRace() {
        System.out.println("\n--- Multi-Racer Race ---");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of racers (2-10): ");

        try {
            // TODO: Get user input for racer count
            int racerCount = scanner.nextInt();

            // TODO: Validate input (2-10)
            if (racerCount < 2 || racerCount > 10) {
                System.out.println("Invalid number. Using 5 racers.");
                racerCount = 5;
            }

            // TODO: Create RaceTrack
            // TODO: Create racers with names and emojis
            // TODO: Create racers in a loop and add to track
            // TODO: Start race and wait for completion
            // TODO: Display results

            // Stop here so you can complete the TODOs above before running the race
            System.out.println("TODO: Finish runMultiRacerRace(): create racers and start the race.");
            return;

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Running with 5 default racers.");
            runDefaultMultiRacerRace();
        }
    }
    
    /**
     * TODO: Default multi-racer race
     * 
     * Your tasks:
     * 1. Create RaceTrack for 5 racers
     * 2. Create 5 racers with different names and emojis
     * 3. Add all racers to track
     * 4. Start race and wait for completion
     * 5. Display results
     */
    private static void runDefaultMultiRacerRace() {
        // TODO: Create RaceTrack for 5 racers
        RaceTrack track = new RaceTrack(5);

        // TODO: Create array of 5 racers with names and emojis
        Racer[] racers = {
            new Racer("Lightning ⚡", 1),
            new Racer("Thunder 💥", 2),
            new Racer("Storm 🌪️", 3),
            new Racer("Flash 💨", 4),
            new Racer("Rocket 🚀", 5)
        };

        // TODO: Add all racers to track
        for (Racer racer : racers) {
            track.addRacer(racer);
        }

        // TODO: Start race and wait for completion
        track.startRace();
        track.waitForRaceToFinish();

        // TODO: Display results
        displayResults(track);
    }
    
    /**
     * TODO: Performance comparison between single-threaded and multi-threaded execution
     * 
     * Your tasks:
     * 1. Run multiple multi-threaded races and measure time
     * 2. Run equivalent single-threaded simulations and measure time
     * 3. Calculate and display speedup
     * 4. Compare performance results
     */
    private static void runPerformanceComparison() {
        System.out.println("\n--- Performance Comparison ---");

        final int RACER_COUNT = 10;
        final int RACE_COUNT = 5;

        System.out.println("Running " + RACE_COUNT + " races with " + RACER_COUNT + " racers each...");

        // TODO: Multi-threaded races timing
        long multiThreadStart = System.currentTimeMillis();
        for (int i = 0; i < RACE_COUNT; i++) {
            // TODO: Create RaceTrack and racers
            RaceTrack track = new RaceTrack(RACER_COUNT);
            for (int j = 0; j < RACER_COUNT; j++) {
                // TODO: Add racer to track
                track.addRacer(new Racer("Racer-" + j, j + 1));
            }
            // TODO: Start race and wait for completion
            track.startRace();
            track.waitForRaceToFinish();
        }
        long multiThreadTime = System.currentTimeMillis() - multiThreadStart;

        // TODO: Single-threaded simulation timing
        long singleThreadStart = System.currentTimeMillis();
        for (int i = 0; i < RACE_COUNT; i++) {
            // TODO: Call simulateSequentialRace
            simulateSequentialRace(RACER_COUNT);
        }
        long singleThreadTime = System.currentTimeMillis() - singleThreadStart;

        // TODO: Display performance results
        System.out.println("\n--- Performance Results ---");
        System.out.println("Multi-threaded time: " + multiThreadTime + " ms");
        System.out.println("Single-threaded time: " + singleThreadTime + " ms");
        System.out.println("Speedup: " + String.format("%.2f", (double) singleThreadTime / multiThreadTime) + "x");
        System.out.println();
    }
    
    /**
     * TODO: Simulate a race sequentially (single-threaded)
     * 
     * Your tasks:
     * 1. Create array to track racer positions
     * 2. Simulate race until all racers finish
     * 3. Move each racer randomly (1-3 units)
     * 4. Check if all racers have finished
     */
    private static void simulateSequentialRace(int racerCount) {
        // TODO: Create array to track positions
        int[] positions = new int[racerCount];
        Random random = new Random();

        // TODO: Race loop until all finished
        while (true) {
            boolean finished = true;
            // TODO: Loop through all racers
            for (int i = 0; i < racerCount; i++) {
                // TODO: If racer hasn't finished, move them
                if (positions[i] < RACE_DISTANCE) {
                    // TODO: Move racer 1-3 units randomly
                    positions[i] += random.nextInt(3) + 1;
                    finished = false;
                }
            }
            // TODO: If all finished, break loop
            if (finished) break;
        }
    }
    
    /**
     * TODO: Display race results
     * 
     * Your tasks:
     * 1. Get racers from track and sort by finish time
     * 2. Display results with medals and positions
     * 3. Announce the winner
     */
    private static void displayResults(RaceTrack track) {
        System.out.println("\n🏆 === RACE RESULTS === 🏆");

        // TODO: Get racers and sort by finish time
        List<Racer> racers = track.getRacers();
        racers.sort((r1, r2) -> Integer.compare(r1.getFinishTime(), r2.getFinishTime()));

        String[] medals = {"🥇", "🥈", "🥉"};
        String[] positions = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th"};

        // TODO: Display results for each racer
        for (int i = 0; i < racers.size(); i++) {
            Racer racer = racers.get(i);
            String medal = i < 3 ? medals[i] + " " : "   ";
            String position = positions[i];

            // TODO: Print racer result with medal, position, name, and time
            System.out.println(String.format("%s %s: %s (Time: %d ms)", 
                medal, position, racer.getName(), racer.getFinishTime()));
        }

        // TODO: Announce winner
        if (!racers.isEmpty()) {
            System.out.println("\n🏆 Winner: " + racers.get(0).getName() + " 🏆");
        }
    }
    
    /**
     * Race Track - manages the race and racers
     * 
     * TODO (Learning tasks):
     * 1) Store racers and race state (started/finished, finishedCount)
     * 2) Add racers to the track
     * 3) Start the race: countdown -> display -> start all racer tasks
     * 4) Track completion and mark race as finished when all done
     * 5) Periodically render the track until finished
     * 6) Shutdown the executor service safely
     */
    static class RaceTrack {
        private final List<Racer> racers = new ArrayList<>();
        private final AtomicBoolean raceStarted = new AtomicBoolean(false);
        private final AtomicBoolean raceFinished = new AtomicBoolean(false);
        private final AtomicInteger finishedCount = new AtomicInteger(0);
        private final ExecutorService executor = Executors.newCachedThreadPool();
        
        public RaceTrack(int expectedRacers) {
            System.out.println("Creating race track for " + expectedRacers + " racers...");
        }
        
        public void addRacer(Racer racer) {
            // TODO: Add the given racer to internal list
            racers.add(racer);
        }
        
        public void startRace() {
            // TODO: Ensure the race starts only once (use CAS on raceStarted)
            if (raceStarted.compareAndSet(false, true)) {
                System.out.println("\n🏁 Race starting in 3 seconds...");
                // TODO: Call countdown()
                countdown();
                
                System.out.println("\n🏁 GO! 🏁\n");
                // TODO: Initial render of the track
                displayRaceTrack();
                
                // TODO: Start all racer threads (submit tasks to executor)
                for (Racer racer : racers) {
                    executor.submit(() -> {
                        // TODO: Run racer.race() and update finished counters
                        racer.race();
                        if (finishedCount.incrementAndGet() == racers.size()) {
                            raceFinished.set(true);
                            System.out.println("\n🏁 Race finished! 🏁");
                        }
                    });
                }
            }
        }
        
        public void waitForRaceToFinish() {
            // TODO: While not finished, sleep briefly and re-render the track
            while (!raceFinished.get()) {
                try {
                    Thread.sleep(100);
                    if (raceStarted.get() && !raceFinished.get()) {
                        displayRaceTrack();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            // TODO: Shutdown executor gracefully (await termination, then force if needed)
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        private void countdown() {
            // TODO: Print 3..2..1 with 1s delay and handle interruption
            for (int i = 3; i > 0; i--) {
                System.out.println(i + "...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        
        private void displayRaceTrack() {
            // TODO: Render each racer's progress to the console
            System.out.print("\r" + "\033[2K"); // Clear line
            
            for (Racer racer : racers) {
                int position = racer.getPosition();
                String name = racer.getName();
                
                // Create visual track
                StringBuilder track = new StringBuilder();
                track.append(String.format("%-15s |", name));
                
                for (int i = 0; i < DISPLAY_WIDTH; i++) {
                    if (i == position * DISPLAY_WIDTH / RACE_DISTANCE) {
                        track.append("🏃");
                    } else if (i < position * DISPLAY_WIDTH / RACE_DISTANCE) {
                        track.append("=");
                    } else {
                        track.append("-");
                    }
                }
                
                track.append("| ").append(position).append("/").append(RACE_DISTANCE);
                if (racer.isFinished()) {
                    track.append(" ✅");
                }
                
                System.out.println(track.toString());
            }
            
            System.out.flush();
        }
        
        public List<Racer> getRacers() {
            // TODO: Return a copy of racers to avoid external modification
            return new ArrayList<>(racers);
        }
    }
    
    /**
     * TODO: Racer - represents a racing participant
     * 
     * Your tasks:
     * 1. Complete the constructor
     * 2. Implement the race() method with movement logic
     * 3. Add getter methods for all fields
     * 4. Handle interruption gracefully
     */
    static class Racer {
        private final String name;
        private final int id;
        private final Random random = new Random();
        private final long startTime;
        
        private volatile int position = 0;
        private volatile boolean finished = false;
        private volatile int finishTime = 0;
        
        // TODO: Complete the constructor
        public Racer(String name, int id) {
            // TODO: Initialize all fields
            // Hints: assign to this.name / this.id and set startTime = System.currentTimeMillis()
            this.name = name;
            this.id = id;
            this.startTime = System.currentTimeMillis();
        }
        
        // TODO: Implement the race method
        public void race() {
            // TODO: Implement movement and timing
            // Move forward until finished
            while (!finished && position < RACE_DISTANCE) {
                // Generate random movement (1-4 units)
                int step = random.nextInt(4) + 1;
                position += step;

                // Check for finish
                if (position >= RACE_DISTANCE) {
                    position = RACE_DISTANCE;
                    finished = true;
                    finishTime = (int) (System.currentTimeMillis() - startTime);
                    break;
                }

                // TODO: Add random delay (50-150ms) with proper exception handling for InterruptedException
            }
        }
        
        // TODO: Implement getter methods
        public String getName() {
			// Return name
			return name;
        }
        
        public int getPosition() {
			// Return current position
			return position;
        }
        
        public boolean isFinished() {
			// Return finished status
			return finished;
        }
        
        public int getFinishTime() {
			// Return finish time in ms
			return finishTime;
        }
        
        public int getId() {
			// Return racer id
			return id;
        }
    }
}
