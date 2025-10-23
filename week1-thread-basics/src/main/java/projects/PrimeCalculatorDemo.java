package projects;

import java.util.*;
import java.util.concurrent.*;

/**
 * Prime Calculator Demo - Minh họa Multi-threading hiệu quả
 * 
 * Mục đích: So sánh performance giữa single-thread và multi-thread
 * khi xử lý CPU-intensive tasks (tính toán số nguyên tố)
 * 
 * Bài học:
 * - Multi-threading hiệu quả với CPU-intensive tasks
 * - Cách chia nhỏ công việc cho nhiều thread
 * - Đo lường performance thực tế
 */
public class PrimeCalculatorDemo {
    
    private static final int MAX_NUMBER = 1000000; // Tính số nguyên tố từ 2 đến 1,000,000
    private static final int THREAD_COUNT = 4; // Số thread để sử dụng
    
    public static void main(String[] args) {
        System.out.println("🔢 === PRIME CALCULATOR DEMO === 🔢\n");
        System.out.println("Mục đích: So sánh performance giữa Single-thread và Multi-thread");
        System.out.println("Task: Tìm tất cả số nguyên tố từ 2 đến " + MAX_NUMBER + "\n");
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            System.out.println("Chọn chế độ:");
            System.out.println("1. Single-threaded (tuần tự)");
            System.out.println("2. Multi-threaded (song song)");
            System.out.println("3. So sánh Performance");
            System.out.println("4. Thoát");
            System.out.print("Nhập lựa chọn (1-4): ");
            
            try {
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        runSingleThreaded();
                        break;
                    case 2:
                        runMultiThreaded();
                        break;
                    case 3:
                        runPerformanceComparison();
                        break;
                    case 4:
                        System.out.println("Cảm ơn bạn! 🔢");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập 1-4.");
                }
                
                System.out.println("\n" + "=".repeat(60) + "\n");
                
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        }
    }
    
    /**
     * Chạy tính toán số nguyên tố với single-thread
     */
    private static void runSingleThreaded() {
        System.out.println("\n--- Single-threaded Calculation ---");
        System.out.println("Đang tính toán số nguyên tố từ 2 đến " + MAX_NUMBER + "...");
        
        long startTime = System.currentTimeMillis();
        List<Integer> primes = findPrimesSingleThread(2, MAX_NUMBER);
        long endTime = System.currentTimeMillis();
        
        System.out.println("✅ Hoàn thành!");
        System.out.println("📊 Kết quả:");
        System.out.println("   - Tìm thấy " + primes.size() + " số nguyên tố");
        System.out.println("   - Thời gian: " + (endTime - startTime) + " ms");
        System.out.println("   - 10 số nguyên tố đầu tiên: " + primes.subList(0, Math.min(10, primes.size())));
    }
    
    /**
     * Chạy tính toán số nguyên tố với multi-thread
     */
    private static void runMultiThreaded() {
        System.out.println("\n--- Multi-threaded Calculation ---");
        System.out.println("Đang tính toán số nguyên tố từ 2 đến " + MAX_NUMBER + " với " + THREAD_COUNT + " threads...");
        
        long startTime = System.currentTimeMillis();
        List<Integer> primes = findPrimesMultiThread(2, MAX_NUMBER, THREAD_COUNT);
        long endTime = System.currentTimeMillis();
        
        System.out.println("✅ Hoàn thành!");
        System.out.println("📊 Kết quả:");
        System.out.println("   - Tìm thấy " + primes.size() + " số nguyên tố");
        System.out.println("   - Thời gian: " + (endTime - startTime) + " ms");
        System.out.println("   - 10 số nguyên tố đầu tiên: " + primes.subList(0, Math.min(10, primes.size())));
    }
    
    /**
     * So sánh performance giữa single-thread và multi-thread
     */
    private static void runPerformanceComparison() {
        System.out.println("\n--- Performance Comparison ---");
        System.out.println("Đang chạy cả hai phương pháp để so sánh...\n");
        
        // Single-threaded
        System.out.println("🔄 Chạy Single-threaded...");
        long singleStart = System.currentTimeMillis();
        List<Integer> singlePrimes = findPrimesSingleThread(2, MAX_NUMBER);
        long singleTime = System.currentTimeMillis() - singleStart;
        
        // Multi-threaded
        System.out.println("🔄 Chạy Multi-threaded...");
        long multiStart = System.currentTimeMillis();
        List<Integer> multiPrimes = findPrimesMultiThread(2, MAX_NUMBER, THREAD_COUNT);
        long multiTime = System.currentTimeMillis() - multiStart;
        
        // Kết quả
        System.out.println("\n📊 === KẾT QUẢ SO SÁNH ===");
        System.out.println("Single-threaded:");
        System.out.println("   - Thời gian: " + singleTime + " ms");
        System.out.println("   - Số nguyên tố: " + singlePrimes.size());
        
        System.out.println("Multi-threaded (" + THREAD_COUNT + " threads):");
        System.out.println("   - Thời gian: " + multiTime + " ms");
        System.out.println("   - Số nguyên tố: " + multiPrimes.size());
        
        double speedup = (double) singleTime / multiTime;
        System.out.println("\n🚀 Speedup: " + String.format("%.2f", speedup) + "x");
        
        if (speedup > 1) {
            System.out.println("✅ Multi-threading nhanh hơn " + String.format("%.1f", speedup) + " lần!");
        } else {
            System.out.println("❌ Single-threading nhanh hơn. Có thể do overhead của threads.");
        }
        
        // Kiểm tra kết quả có giống nhau không
        if (singlePrimes.size() == multiPrimes.size()) {
            System.out.println("✅ Kết quả giống nhau - thuật toán chính xác!");
        } else {
            System.out.println("❌ Kết quả khác nhau - có lỗi trong thuật toán!");
        }
    }
    
    /**
     * Tìm số nguyên tố với single-thread
     */
    private static List<Integer> findPrimesSingleThread(int start, int end) {
        List<Integer> primes = new ArrayList<>();
        
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        
        return primes;
    }
    
    /**
     * Tìm số nguyên tố với multi-thread
     */
    private static List<Integer> findPrimesMultiThread(int start, int end, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<List<Integer>>> futures = new ArrayList<>();
        
        // Chia công việc cho các thread
        int rangeSize = (end - start + 1) / threadCount;
        
        for (int i = 0; i < threadCount; i++) {
            int threadStart = start + i * rangeSize;
            int threadEnd = (i == threadCount - 1) ? end : threadStart + rangeSize - 1;
            
            Future<List<Integer>> future = executor.submit(() -> {
                List<Integer> threadPrimes = new ArrayList<>();
                for (int j = threadStart; j <= threadEnd; j++) {
                    if (isPrime(j)) {
                        threadPrimes.add(j);
                    }
                }
                return threadPrimes;
            });
            
            futures.add(future);
        }
        
        // Thu thập kết quả
        List<Integer> allPrimes = new ArrayList<>();
        try {
            for (Future<List<Integer>> future : futures) {
                allPrimes.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        
        // Sắp xếp kết quả
        Collections.sort(allPrimes);
        return allPrimes;
    }
    
    /**
     * Kiểm tra một số có phải là số nguyên tố không
     * Đây là thuật toán đơn giản nhưng CPU-intensive
     */
    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        // Kiểm tra từ 3 đến sqrt(n)
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
