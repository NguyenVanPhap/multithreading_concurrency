//package exercises;
//
//import java.util.concurrent.*;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Exercise: Pipeline Processing Demo
// *
// * TODO Tasks:
// * 1. Tạo pipeline với nhiều stages
// * 2. Mỗi stage phụ thuộc vào stage trước
// * 3. Parallel processing trong stage
// */
//public class PipelineDemo {
//
//    public static void main(String[] args) {
//        System.out.println("==========================================");
//        System.out.println("  Pipeline Processing Demo");
//        System.out.println("==========================================\n");
//
//        // TODO: Tạo ExecutorService cho pipeline
//        // Hướng dẫn:
//        //  1. Dùng Executors.newFixedThreadPool(5) để tạo executor
//        //  2. Đảm bảo shutdown() executor sau khi pipeline hoàn thành
//
//        // TODO: Tạo pipeline
//        // Hướng dẫn gợi ý flow:
//        //  1. Tạo danh sách inputs: Arrays.asList(\"input1\", \"input2\", \"input3\")\n        //  2. Gọi fetchStage(inputs, executor) để tạo stage1\n        //  3. thenCompose stage1 -> processStage(...) để tạo stage2\n        //  4. thenCompose stage2 -> aggregateStage(...) để tạo stage3\n        //  5. join() stage3 để lấy finalResult và in ra\n        //  6. Đặt toàn bộ logic trong khối try/finally để shutdown executor
//
//        System.out.println("\n==========================================");
//        System.out.println("  Demo completed!");
//        System.out.println("==========================================");
//    }
//
//    private static CompletableFuture<List<String>> fetchStage(
//            List<String> inputs, ExecutorService executor) {
//
//        System.out.println("[Stage 1] Fetching data...");
//
//        // TODO: Fetch data song song
//        // Hướng dẫn:
//        //  1. Với mỗi input, tạo một CompletableFuture.supplyAsync(...) dùng executor
//        //  2. Trong supplier, sleep ~200ms và trả về \"Fetched: \" + input\n        //  3. Gom tất cả futures vào List<CompletableFuture<String>>\n        //  4. Dùng CompletableFuture.allOf(...) để đợi tất cả xong\n        //  5. Sau khi allOf hoàn thành, join từng future để tạo List<String> kết quả\n        throw new UnsupportedOperationException("TODO: implement fetchStage");
//    }
//
//    private static CompletableFuture<List<String>> processStage(
//            List<String> inputs, ExecutorService executor) {
//
//        System.out.println("[Stage 2] Processing data...");
//
//        // TODO: Process data song song
//        // Hướng dẫn (tương tự fetchStage):\n        //  1. Duyệt inputs và tạo futures với supplyAsync\n        //  2. Trong supplier, sleep ~200ms và trả về \"Processed: \" + input\n        //  3. Dùng allOf + join để trả về List<String> kết quả\n        throw new UnsupportedOperationException("TODO: implement processStage");
//    }
//
//    private static CompletableFuture<String> aggregateStage(
//            List<String> inputs, ExecutorService executor) {
//
//        System.out.println("[Stage 3] Aggregating data...");
//
//        // TODO: Aggregate data
//        // Hướng dẫn:
//        //  1. Dùng CompletableFuture.supplyAsync(...) với executor\n        //  2. Trong supplier, sleep ~200ms\n        //  3. Dùng String.join(\" | \", inputs) để nối các phần tử thành một chuỗi\n        //  4. Trả về chuỗi đó từ supplier\n        throw new UnsupportedOperationException("TODO: implement aggregateStage");
//    }
//}
//
