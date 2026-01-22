//package exercises;
//
//import java.util.concurrent.*;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Exercise: ExecutorService + CompletableFuture Demo
// *
// * TODO Tasks:
// * 1. Tạo custom ExecutorService
// * 2. Sử dụng CompletableFuture với custom executor
// * 3. Chain operations với thenApply, thenCompose
// * 4. Combine multiple futures
// */
//public class ExecutorCompletableFutureDemo {
//
//    public static void main(String[] args) {
//        System.out.println("==========================================");
//        System.out.println("  ExecutorService + CompletableFuture Demo");
//        System.out.println("==========================================\n");
//
//        // TODO: Tạo ExecutorService
//        // Hướng dẫn:
//        //  1. Chọn số lượng thread phù hợp (ví dụ: 5)
//        //  2. Dùng Executors.newFixedThreadPool(...) để tạo ExecutorService
//        //  3. Đảm bảo shutdown() executor ở cuối chương trình (trong finally)
//
//        // TODO: Gọi các demo bên dưới để chạy thử
//        // Hướng dẫn:
//        //  1. Gọi demoBasicUsage(executor);
//        //  2. Gọi demoChaining(executor);
//        //  3. Gọi demoCombining(executor);
//
//        System.out.println("\n==========================================");
//        System.out.println("  Demo completed!");
//        System.out.println("==========================================");
//    }
//
//    private static void demoBasicUsage(ExecutorService executor) {
//        System.out.println("Demo 1: Basic Usage");
//        System.out.println("-------------------");
//
//        // TODO: Tạo CompletableFuture với custom executor
//        // Hướng dẫn:
//        //  1. Dùng CompletableFuture.supplyAsync(supplier, executor)
//        //  2. Trong supplier, giả lập công việc mất 500ms (Thread.sleep)
//        //  3. Return một chuỗi kết quả, ví dụ: "Hello from async task"
//        //  4. Dùng future.join() để lấy kết quả và in ra
//        throw new UnsupportedOperationException("TODO: implement demoBasicUsage");
//    }
//
//    private static void demoChaining(ExecutorService executor) {
//        System.out.println("Demo 2: Chaining Operations");
//        System.out.println("----------------------------");
//
//        // TODO: Chain operations
//        // Hướng dẫn:
//        //  1. Bắt đầu bằng supplyAsync(() -> "hello", executor)
//        //  2. thenApply để chuyển thành chữ hoa
//        //  3. thenApply tiếp để nối thêm " WORLD"
//        //  4. join() và in kết quả ra console
//        throw new UnsupportedOperationException("TODO: implement demoChaining");
//    }
//
//    private static void demoCombining(ExecutorService executor) {
//        System.out.println("Demo 3: Combining Futures");
//        System.out.println("-------------------------");
//
//        // TODO: Combine multiple futures
//        // Hướng dẫn:
//        //  1. Tạo hai CompletableFuture<String> f1, f2 bằng supplyAsync với executor
//        //  2. Dùng thenCombine(f2, (r1, r2) -> ...) để kết hợp kết quả của f1 và f2
//        //  3. Trong lambda kết hợp, tạo chuỗi dạng "Result1 + Result2"
//        //  4. join() trên future đã combine và in ra
//        throw new UnsupportedOperationException("TODO: implement demoCombining");
//    }
//}
//
