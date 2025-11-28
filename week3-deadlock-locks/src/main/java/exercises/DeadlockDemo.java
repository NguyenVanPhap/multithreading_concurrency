package exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Exercise 1: Deadlock Demo
 * 
 * TODO Tasks:
 * 1. Tạo deadlock scenario với 2 threads và 2 locks
 * 2. Phát hiện deadlock bằng ThreadMXBean
 * 3. Implement giải pháp tránh deadlock với lock ordering
 * 4. So sánh performance trước và sau khi fix
 */
public class DeadlockDemo {
    
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Deadlock Demo");
        System.out.println("==========================================\n");
        
        // TODO: Test deadlock scenario
        System.out.println("Test 1: Deadlock Scenario");
        testDeadlock();
        
        // TODO: Test deadlock detection
        System.out.println("\nTest 2: Deadlock Detection");
        testDeadlockDetection();
        
        // TODO: Test deadlock prevention với lock ordering
        System.out.println("\nTest 3: Deadlock Prevention (Lock Ordering)");
        testDeadlockPrevention();
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    /**
     * TODO: Tạo deadlock scenario
     * Thread 1: lock1 -> lock2
     * Thread 2: lock2 -> lock1
     * Điều này sẽ gây ra deadlock!
     */
    private static void testDeadlock() {
        // TODO: Tạo Thread 1 - Acquire lock1, sau đó lock2
        Thread thread1 = new Thread(() -> {
            // TODO: Acquire lock1, then lock2
            // TODO: Sử dụng try-finally để đảm bảo unlock
            // TODO: Thêm sleep để simulate work và tăng khả năng deadlock
            lock1.lock();
            try {
                // Mô phỏng công việc với thời gian ngủ dài hơn
                try {
                    Thread.sleep(100); // THAY ĐỔI: lâu hơn để tăng khả năng deadlock
                    lock2.lock();
                    System.out.println(Thread.currentThread().getName() + " đã chiếm cả hai khóa.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock2.unlock(); // Đảm bảo giải phóng lock2
                }
            } finally {
                lock1.unlock(); // Đảm bảo giải phóng lock1
            }
        });
        
        // TODO: Tạo Thread 2 - Acquire lock2, sau đó lock1 (OPPOSITE ORDER - causes deadlock!)
        Thread thread2 = new Thread(() -> {
            // TODO: Acquire lock2, then lock1 (OPPOSITE ORDER!)
            // TODO: Sử dụng try-finally để đảm bảo unlock
            // TODO: Thêm sleep để simulate work
            lock2.lock();
            try {
                // Mô phỏng công việc với thời gian ngủ dài hơn
                try {
                    Thread.sleep(100); // THAY ĐỔI: lâu hơn để tăng khả năng deadlock
                    lock1.lock();
                    System.out.println(Thread.currentThread().getName() + " đã chiếm cả hai khóa.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock1.unlock(); // Đảm bảo giải phóng lock1
                }
            } finally {
                lock2.unlock(); // Đảm bảo giải phóng lock2
            }
        });
        
        thread1.setName("Thread-1");
        thread2.setName("Thread-2");
        
        // TODO: Start cả 2 threads
        // TODO: Wait for threads (they will deadlock, so this will hang)
        // TODO: Sử dụng join với timeout để phát hiện deadlock
        // TODO: Kiểm tra nếu threads còn alive sau timeout -> deadlock detected

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Kiểm tra deadlock
        if (thread1.isAlive() && thread2.isAlive()) {
            System.out.println("Deadlock detected between Thread-1 and Thread-2!");
        } else {
            System.out.println("No deadlock detected.");
        }

    }
    
    /**
     * TODO: Implement deadlock detection sử dụng ThreadMXBean
     */
    private static void testDeadlockDetection() {
        // TODO: Lấy ThreadMXBean từ ManagementFactory
        ThreadMXBean threadMX = null; // TODO: Get from ManagementFactory
        
        // TODO: Tạo deadlock scenario (tương tự testDeadlock)
        Thread thread1 = null; // TODO: Create thread với lock1 -> lock2
        Thread thread2 = null; // TODO: Create thread với lock2 -> lock1
        
        // TODO: Start threads
        // TODO: Wait một chút để deadlock xảy ra
        
        // TODO: Sử dụng ThreadMXBean.findDeadlockedThreads() để phát hiện deadlock
        // TODO: Kiểm tra kết quả và in ra thread names nếu có deadlock
        
        // TODO: Interrupt threads để dừng chúng
    }
    
    /**
     * TODO: Implement deadlock prevention với lock ordering
     * Luôn acquire locks theo cùng một thứ tự: lock1 -> lock2
     */
    private static void testDeadlockPrevention() {
        // TODO: Tạo Thread 1 - Acquire locks theo thứ tự: lock1 -> lock2
        Thread thread1 = new Thread(() -> {
            // TODO: Acquire lock1, then lock2 (CÙNG thứ tự!)
            // TODO: Sử dụng try-finally
            // TODO: In ra messages để track progress
        });
        
        // TODO: Tạo Thread 2 - Acquire locks theo CÙNG thứ tự: lock1 -> lock2 (NOT lock2 -> lock1!)
        Thread thread2 = new Thread(() -> {
            // TODO: Acquire lock1, then lock2 (CÙNG thứ tự như thread1!)
            // TODO: Sử dụng try-finally
            // TODO: In ra messages để track progress
        });
        
        thread1.setName("Thread-1-Safe");
        thread2.setName("Thread-2-Safe");
        
        // TODO: Start threads và measure time
        // TODO: Wait for completion
        // TODO: In ra thời gian hoàn thành
    }
}

