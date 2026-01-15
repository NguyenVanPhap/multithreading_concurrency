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
     * TODO: Chạy demo scenario Checkout.
     * Hướng dẫn chi tiết:
     * 
     * Bước 1: Thiết kế DTO
     *  - Tạo class CheckoutRequest (userId, cartItems, paymentMethod, ...)
     *  - Tạo class CheckoutResult (status, orderId, errors, events, ...)
     * 
     * Bước 2: Xây dựng flow checkout thực sự
     *  Flow gợi ý:
     *    1. loadUser(userId) -> trả về User object
     *    2. validateCart(cartItems) -> kiểm tra cart hợp lệ
     *    3. reserveInventory(cartItems) -> giữ hàng trong kho
     *    4. chargePayment(paymentMethod, amount) -> thanh toán
     *    5. createOrder(userId, cartItems) -> tạo đơn hàng
     *    6. sendNotification(userId, orderId) -> gửi thông báo
     * 
     * Bước 3: Xử lý dependencies và error handling
     *  - Một số bước có thể chạy song song (ví dụ: loadUser và validateCart)
     *  - Một số bước phải tuần tự (ví dụ: phải reserveInventory trước khi chargePayment)
     *  - Dùng CompletableFuture.thenCompose() cho dependencies
     *  - Dùng CompletableFuture.thenCombine() cho các bước song song
     * 
     * Bước 4: Xử lý rollback (compensating transaction)
     *  - Nếu createOrder fail sau khi chargePayment thành công:
     *    + Gọi RefundService để hoàn tiền
     *  - Nếu chargePayment fail sau khi reserveInventory thành công:
     *    + Gọi ReleaseInventoryService để giải phóng hàng
     * 
     * Bước 5: Logging và metrics
     *  - Log từng bước: bắt đầu, thành công, thất bại
     *  - Đo thời gian mỗi bước
     *  - In ra kết quả cuối cùng
     */
    public void runDemo() {
        throw new UnsupportedOperationException("TODO: implement runDemo");
    }
}


