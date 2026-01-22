package projects.asyncserviceorchestrator;

import projects.asyncserviceorchestrator.config.OrchestratorConfig;
import projects.asyncserviceorchestrator.config.ServiceConfig;
import projects.asyncserviceorchestrator.core.AsyncOrchestratorEngine;
import projects.asyncserviceorchestrator.demo.CheckoutScenario;

import java.time.Duration;

/**
 * Entry-point cho "project lớn" AsyncServiceOrchestrator.
 *
 * Mục tiêu của folder này:
 * - Tách code thành nhiều lớp/packge rõ ràng (core/config/services/metrics/demo)
 * - Giống một mini microservice orchestration framework thu nhỏ
 * - Để lại rất nhiều TODO cho bạn tự triển khai logic chi tiết
 */
public class AsyncServiceOrchestratorApp {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Async Service Orchestrator - Big Project");
        System.out.println("==========================================");

        // Khởi tạo OrchestratorConfig
        OrchestratorConfig config = OrchestratorConfig.builder()
            .ioThreads(10)
            .cpuThreads(Runtime.getRuntime().availableProcessors())
            .schedulerThreads(4)
            .defaultRequestTimeout(Duration.ofSeconds(2))
            .globalMaxRetries(3)
            .defaultCircuitBreakerFailureThreshold(5)
            .metricsEnabled(true)
            .tracingEnabled(false)
            .loggingLevel("INFO")
            .build();

        // Khởi tạo AsyncOrchestratorEngine với config vừa tạo
        AsyncOrchestratorEngine engine = new AsyncOrchestratorEngine(config);

        // Đăng ký các service quan trọng của hệ thống
        System.out.println("\nRegistering services...");
        
        engine.registerService(ServiceConfig.builder()
            .serviceName("UserService")
            .timeout(Duration.ofMillis(800))
            .maxRetries(3)
            .cpuBound(false)
            .priority(10)
            .metadata("backoffStrategy", "exponential")
            .metadata("baseDelayMs", "100")
            .build());
        
        engine.registerService(ServiceConfig.builder()
            .serviceName("OrderService")
            .timeout(Duration.ofMillis(1200))
            .maxRetries(2)
            .cpuBound(true)
            .priority(9)
            .metadata("backoffStrategy", "exponential")
            .metadata("baseDelayMs", "150")
            .build());
        
        engine.registerService(ServiceConfig.builder()
            .serviceName("PaymentService")
            .timeout(Duration.ofMillis(1500))
            .maxRetries(3)
            .cpuBound(false)
            .priority(10)
            .metadata("backoffStrategy", "exponential")
            .metadata("baseDelayMs", "200")
            .build());
        
        engine.registerService(ServiceConfig.builder()
            .serviceName("InventoryService")
            .timeout(Duration.ofMillis(1000))
            .maxRetries(2)
            .cpuBound(true)
            .priority(8)
            .metadata("backoffStrategy", "exponential")
            .metadata("baseDelayMs", "100")
            .build());
        
        engine.registerService(ServiceConfig.builder()
            .serviceName("NotificationService")
            .timeout(Duration.ofMillis(800))
            .maxRetries(1)
            .cpuBound(false)
            .priority(5)
            .metadata("backoffStrategy", "linear")
            .metadata("baseDelayMs", "50")
            .build());
        
        System.out.println("Services registered successfully!\n");

        // Support chọn scenario qua tham số dòng lệnh (args)
        String scenario = args.length > 0 ? args[0] : "checkout";
        
        try {
            // Chạy demo scenario
            switch (scenario.toLowerCase()) {
                case "checkout":
                    System.out.println("Running Checkout scenario...\n");
                    CheckoutScenario checkoutScenario = new CheckoutScenario(engine);
                    checkoutScenario.runDemo();
                    break;
                default:
                    System.out.println("Unknown scenario: " + scenario);
                    System.out.println("Available scenarios: checkout");
                    System.out.println("Running default checkout scenario...\n");
                    CheckoutScenario defaultScenario = new CheckoutScenario(engine);
                    defaultScenario.runDemo();
                    break;
            }
            
            // Chạy nhiều lần để benchmark (optional)
            if (args.length > 1 && args[1].equals("benchmark")) {
                System.out.println("\n========== BENCHMARK MODE ==========");
                int iterations = 5;
                long[] durations = new long[iterations];
                
                for (int i = 0; i < iterations; i++) {
                    System.out.println("\nIteration " + (i + 1) + "/" + iterations);
                    long start = System.currentTimeMillis();
                    CheckoutScenario benchScenario = new CheckoutScenario(engine);
                    benchScenario.runDemo();
                    durations[i] = System.currentTimeMillis() - start;
                }
                
                // Tính toán statistics
                long sum = 0;
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                for (long d : durations) {
                    sum += d;
                    min = Math.min(min, d);
                    max = Math.max(max, d);
                }
                double avg = sum / (double) iterations;
                
                System.out.println("\n========== BENCHMARK RESULTS ==========");
                System.out.println("Iterations: " + iterations);
                System.out.println("Average: " + String.format("%.2f", avg) + "ms");
                System.out.println("Min: " + min + "ms");
                System.out.println("Max: " + max + "ms");
                System.out.println("=====================================\n");
            }
            
        } catch (Exception e) {
            System.err.println("Error running scenario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // In metrics tổng kết
            engine.getMetricsRegistry().printSummary();
            
            // Tắt hệ thống gracefully
            engine.shutdown();
        }

        System.out.println("==========================================");
        System.out.println("  Async Service Orchestrator - Finished");
        System.out.println("==========================================");
    }
}


