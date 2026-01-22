package projects.asyncserviceorchestrator.demo;

import projects.asyncserviceorchestrator.core.AsyncOrchestratorEngine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Demo scenario "Checkout" cho project lớn.
 *
 * Ý tưởng:
 * - Gọi nhiều service cùng lúc: UserService, OrderService, PaymentService, InventoryService, NotificationService
 * - Kết hợp kết quả (ví dụ: phải có user + order ok mới trả tiền, v.v.)
 *
 * Ở đây mới chỉ chạy song song đơn giản để bạn thấy flow tổng thể.
 * Phần thú vị (kết hợp kết quả, rollback khi có lỗi, v.v.) là TODO của bạn.
 */
public class CheckoutScenario {

    private final AsyncOrchestratorEngine engine;

    public CheckoutScenario(AsyncOrchestratorEngine engine) {
        this.engine = engine;
    }

    /**
     * Chạy demo scenario Checkout.
     */
    public void runDemo() {
        System.out.println("\n========== CHECKOUT SCENARIO ==========");
        System.out.println("Starting checkout process...\n");
        
        long startTime = System.currentTimeMillis();
        
        // Bước 1: Load user và validate cart song song
        System.out.println("Step 1: Loading user and validating cart (parallel)...");
        CompletableFuture<String> userFuture = engine.callSingleService("UserService");
        CompletableFuture<String> cartFuture = engine.callSingleService("OrderService");
        
        CompletableFuture<String> userAndCart = userFuture.thenCombine(cartFuture, (user, cart) -> {
            System.out.println("  ✓ User loaded: " + user);
            System.out.println("  ✓ Cart validated: " + cart);
            return "User and cart ready";
        });
        
        // Bước 2: Reserve inventory (phải đợi user và cart)
        System.out.println("\nStep 2: Reserving inventory...");
        CompletableFuture<String> inventoryFuture = userAndCart.thenCompose(v -> {
            return engine.callSingleService("InventoryService");
        });
        
        // Bước 3: Charge payment (phải đợi inventory)
        System.out.println("\nStep 3: Charging payment...");
        CompletableFuture<String> paymentFuture = inventoryFuture.thenCompose(v -> {
            System.out.println("  ✓ Inventory reserved: " + v);
            return engine.callSingleService("PaymentService");
        });
        
        // Bước 4: Create order (phải đợi payment)
        System.out.println("\nStep 4: Creating order...");
        CompletableFuture<String> orderFuture = paymentFuture.thenCompose(v -> {
            System.out.println("  ✓ Payment charged: " + v);
            return engine.callSingleService("OrderService");
        });
        
        // Bước 5: Send notification (có thể chạy song song với order, nhưng đợi order để có orderId)
        System.out.println("\nStep 5: Sending notification...");
        CompletableFuture<String> notificationFuture = orderFuture.thenCompose(v -> {
            System.out.println("  ✓ Order created: " + v);
            return engine.callSingleService("NotificationService");
        });
        
        // Đợi tất cả hoàn thành
        try {
            String result = notificationFuture.get();
            System.out.println("\n  ✓ Notification sent: " + result);
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("\n========== CHECKOUT COMPLETED ==========");
            System.out.println("Total duration: " + duration + "ms");
            System.out.println("Status: SUCCESS\n");
        } catch (Exception e) {
            System.err.println("\n========== CHECKOUT FAILED ==========");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


