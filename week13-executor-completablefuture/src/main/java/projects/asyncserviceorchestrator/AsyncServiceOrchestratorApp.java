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

        // TODO: đọc cấu hình từ file (application.properties / JSON / YAML) thay vì hard-code
        //  Hướng dẫn:
        //  1. Tạo file config (ví dụ: config.properties hoặc config.json)
        //  2. Đọc các giá trị: ioThreads, cpuThreads, schedulerThreads, defaultRequestTimeout
        //  3. Dùng các giá trị đó để build OrchestratorConfig

        // TODO: Khởi tạo OrchestratorConfig
        //  Gợi ý: OrchestratorConfig config = OrchestratorConfig.builder()...build();

        // TODO: Khởi tạo AsyncOrchestratorEngine với config vừa tạo
        //  Gợi ý: AsyncOrchestratorEngine engine = new AsyncOrchestratorEngine(config);

        // TODO: Đăng ký các service quan trọng của hệ thống
        //  Bạn có thể tách ra file cấu hình riêng, hoặc đọc từ JSON/YAML
        //  Ví dụ các service cần đăng ký:
        //    - UserService (timeout=800ms, maxRetries=3, IO-bound, priority=10)
        //    - OrderService (timeout=1200ms, maxRetries=2, CPU-bound, priority=9)
        //    - PaymentService (timeout=1500ms, maxRetries=3, IO-bound, priority=10)
        //    - InventoryService (timeout=1000ms, maxRetries=2, CPU-bound, priority=8)
        //    - NotificationService (timeout=800ms, maxRetries=1, IO-bound, priority=5)
        //  Gợi ý: engine.registerService(ServiceConfig.builder()...build());

        // TODO: Support chọn scenario qua tham số dòng lệnh (args)
        //  Hướng dẫn:
        //  - Parse args[0] để xác định scenario nào cần chạy (checkout, refund, bulk-order, ...)
        //  - Tạo instance scenario tương ứng
        //  - Gọi runDemo() của scenario đó

        // TODO: Chạy demo scenario "Checkout"
        //  Gợi ý:
        //    CheckoutScenario checkoutScenario = new CheckoutScenario(engine);
        //    checkoutScenario.runDemo();

        // TODO: Thêm nhiều scenario khác
        //  - RefundScenario: xử lý hoàn tiền khi đơn hàng bị hủy
        //  - BulkOrderScenario: xử lý nhiều đơn hàng cùng lúc
        //  - HealthCheckScenario: kiểm tra health của tất cả services

        // TODO: Chạy nhiều lần để benchmark
        //  Hướng dẫn:
        //  - Chạy scenario N lần (ví dụ: 10 lần)
        //  - Đo thời gian mỗi lần chạy
        //  - Tính trung bình, min, max
        //  - In ra report

        // TODO: In metrics tổng kết
        //  Gợi ý: engine.getMetricsRegistry().printSummary();

        // TODO: Tắt hệ thống gracefully
        //  Gợi ý: engine.shutdown();

        System.out.println("==========================================");
        System.out.println("  Async Service Orchestrator - Finished");
        System.out.println("==========================================");
    }
}


