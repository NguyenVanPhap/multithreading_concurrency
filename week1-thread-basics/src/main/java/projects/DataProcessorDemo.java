package projects;

import java.util.*;
import java.util.concurrent.*;

/**
 * Data Processor Demo - Minh họa Multi-threading với xử lý dữ liệu lớn
 * 
 * Mục đích: So sánh performance khi xử lý mảng dữ liệu lớn
 * 
 * Bài học:
 * - Multi-threading hiệu quả với CPU-intensive operations
 * - Cách chia nhỏ dữ liệu cho nhiều thread
 * - Parallel processing patterns
 */
public class DataProcessorDemo {
    
    private static final int DATA_SIZE = 100_000_000; // 100 triệu phần tử (tăng lên để thấy rõ benefit)
    private static final int THREAD_COUNT = 8; // Tăng số threads
    
    public static void main(String[] args) {
        // Auto-run performance comparison if args provided
        if (args.length > 0 && args[0].equals("test")) {
            runPerformanceComparison();
            return;
        }
        
        System.out.println("📊 === DATA PROCESSOR DEMO === 📊\n");
        System.out.println("Mục đích: So sánh xử lý dữ liệu lớn giữa Single-thread và Multi-thread");
        System.out.println("Task: Tính tổng bình phương của " + DATA_SIZE + " số ngẫu nhiên\n");
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            System.out.println("Chọn chế độ:");
            System.out.println("1. Single-threaded Processing");
            System.out.println("2. Multi-threaded Processing");
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
                        System.out.println("Cảm ơn bạn! 📊");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập 1-4.");
                }
                
                System.out.println("\n" + "=".repeat(60) + "\n");
                
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
                scanner.nextLine();
            }
        }
        }
    }
    
    /**
     * Tạo dữ liệu mẫu
     */
    private static double[] generateData(int size) {
        System.out.println("🔄 Đang tạo dữ liệu mẫu...");
        double[] data = new double[size];
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            data[i] = random.nextDouble() * 1000; // Số từ 0 đến 1000
        }
        
        System.out.println("✅ Đã tạo " + size + " số ngẫu nhiên");
        return data;
    }
    
    /**
     * Xử lý dữ liệu với single-thread
     */
    private static void runSingleThreaded() {
        System.out.println("\n--- Single-threaded Processing ---");
        
        double[] data = generateData(DATA_SIZE);
        
        System.out.println("🔄 Đang xử lý dữ liệu...");
        long startTime = System.currentTimeMillis();
        
        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        
        for (double value : data) {
            double squared = value * value;
            sum += squared;
            max = Math.max(max, squared);
            min = Math.min(min, squared);
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("✅ Hoàn thành!");
        System.out.println("📊 Kết quả:");
        System.out.println("   - Tổng bình phương: " + String.format("%.2f", sum));
        System.out.println("   - Giá trị lớn nhất: " + String.format("%.2f", max));
        System.out.println("   - Giá trị nhỏ nhất: " + String.format("%.2f", min));
        System.out.println("   - Thời gian: " + (endTime - startTime) + " ms");
    }
    
    /**
     * Xử lý dữ liệu với multi-thread
     */
    private static void runMultiThreaded() {
        System.out.println("\n--- Multi-threaded Processing ---");
        
        double[] data = generateData(DATA_SIZE);
        
        System.out.println("🔄 Đang xử lý dữ liệu với " + THREAD_COUNT + " threads...");
        long startTime = System.currentTimeMillis();
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<ProcessingResult>> futures = new ArrayList<>();
        
        // Chia dữ liệu cho các thread
        int chunkSize = DATA_SIZE / THREAD_COUNT;
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            // Sửa lỗi: Copy biến vào final để tránh closure capture sai
            final int chunkIndex = i;
            final int start = chunkIndex * chunkSize;
            final int end = (chunkIndex == THREAD_COUNT - 1) ? DATA_SIZE : start + chunkSize;
            
            Future<ProcessingResult> future = executor.submit(() -> {
                double sum = 0;
                double max = Double.MIN_VALUE;
                double min = Double.MAX_VALUE;
                
                for (int j = start; j < end; j++) {
                    double squared = data[j] * data[j];
                    sum += squared;
                    max = Math.max(max, squared);
                    min = Math.min(min, squared);
                }
                
                return new ProcessingResult(sum, max, min);
            });
            
            futures.add(future);
        }
        
        // Thu thập kết quả
        double totalSum = 0;
        double globalMax = Double.MIN_VALUE;
        double globalMin = Double.MAX_VALUE;
        
        try {
            for (Future<ProcessingResult> future : futures) {
                ProcessingResult result = future.get();
                totalSum += result.sum;
                globalMax = Math.max(globalMax, result.max);
                globalMin = Math.min(globalMin, result.min);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("✅ Hoàn thành!");
        System.out.println("📊 Kết quả:");
        System.out.println("   - Tổng bình phương: " + String.format("%.2f", totalSum));
        System.out.println("   - Giá trị lớn nhất: " + String.format("%.2f", globalMax));
        System.out.println("   - Giá trị nhỏ nhất: " + String.format("%.2f", globalMin));
        System.out.println("   - Thời gian: " + (endTime - startTime) + " ms");
    }
    
    /**
     * So sánh performance
     */
    private static void runPerformanceComparison() {
        System.out.println("\n--- Performance Comparison ---");
        System.out.println("Đang chạy cả hai phương pháp để so sánh...\n");
        
        // Hiển thị thông tin CPU
        int cpuCores = Runtime.getRuntime().availableProcessors();
        System.out.println("🖥️  CPU Info:");
        System.out.println("   - Available cores: " + cpuCores);
        System.out.println("   - Threads used: " + THREAD_COUNT);
        System.out.println();
        
        double[] data = generateData(DATA_SIZE);
        
        // Single-threaded
        System.out.println("🔄 Chạy Single-threaded...");
        long singleStart = System.currentTimeMillis();
        double singleSum = processDataSingleThread(data);
        long singleTime = System.currentTimeMillis() - singleStart;
        
        // Multi-threaded
        System.out.println("🔄 Chạy Multi-threaded...");
        long multiStart = System.currentTimeMillis();
        double multiSum = processDataMultiThread(data);
        long multiTime = System.currentTimeMillis() - multiStart;
        
        // Kết quả
        System.out.println("\n📊 === KẾT QUẢ SO SÁNH ===");
        System.out.println("Single-threaded:");
        System.out.println("   - Thời gian: " + singleTime + " ms");
        System.out.println("   - Tổng: " + String.format("%.2f", singleSum));
        
        System.out.println("Multi-threaded (" + THREAD_COUNT + " threads):");
        System.out.println("   - Thời gian: " + multiTime + " ms");
        System.out.println("   - Tổng: " + String.format("%.2f", multiSum));
        
        double speedup = (double) singleTime / multiTime;
        System.out.println("\n🚀 Speedup: " + String.format("%.2f", speedup) + "x");
        
        if (speedup > 1) {
            System.out.println("✅ Multi-threading nhanh hơn " + String.format("%.1f", speedup) + " lần!");
        } else {
            System.out.println("❌ Single-threading nhanh hơn. Có thể do overhead của threads.");
        }
        
        // Kiểm tra kết quả với thông tin chi tiết
        double difference = Math.abs(singleSum - multiSum);
        double relativeError = (difference / Math.abs(singleSum)) * 100;
        
        System.out.println("\n🔍 === PHÂN TÍCH SAI SỐ ===");
        System.out.println("Sai số tuyệt đối: " + String.format("%.10f", difference));
        System.out.println("Sai số tương đối: " + String.format("%.10f%%", relativeError));
        
        if (difference < 1.0) {
            System.out.println("✅ Kết quả giống nhau - thuật toán chính xác!");
            System.out.println("   (Sai số nhỏ là do floating point precision khi cộng theo thứ tự khác nhau)");
        } else if (relativeError < 0.00001) { // Less than 0.00001% error
            System.out.println("⚠️ Sai số rất nhỏ - có thể do floating point precision");
        } else {
            System.out.println("❌ Kết quả khác nhau đáng kể - có lỗi trong thuật toán!");
        }
    }
    
    private static double processDataSingleThread(double[] data) {
        double sum = 0;
        for (double value : data) {
            sum += value * value;
        }
        return sum;
    }
    
    private static double processDataMultiThread(double[] data) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Double>> futures = new ArrayList<>();
        
        int chunkSize = data.length / THREAD_COUNT;
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            // Sửa lỗi: Copy biến vào final để tránh closure capture sai
            final int chunkIndex = i;
            final int start = chunkIndex * chunkSize;
            final int end = (chunkIndex == THREAD_COUNT - 1) ? data.length : start + chunkSize;
            
            Future<Double> future = executor.submit(() -> {
                double sum = 0;
                for (int j = start; j < end; j++) {
                    sum += data[j] * data[j];
                }
                return sum;
            });
            
            futures.add(future);
        }
        
        double totalSum = 0;
        try {
            for (Future<Double> future : futures) {
                totalSum += future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        
        return totalSum;
    }
    
    /**
     * Class để lưu kết quả xử lý của mỗi thread
     */
    private static class ProcessingResult {
        final double sum;
        final double max;
        final double min;
        
        ProcessingResult(double sum, double max, double min) {
            this.sum = sum;
            this.max = max;
            this.min = min;
        }
    }
}
