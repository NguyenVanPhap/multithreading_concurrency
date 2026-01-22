package projects.asyncserviceorchestrator.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Circuit Breaker pattern để bảo vệ service khỏi quá tải khi service đang gặp sự cố.
 * 
 * Có 3 trạng thái:
 * - CLOSED: Bình thường, cho phép gọi service
 * - OPEN: Service đang lỗi, từ chối tất cả request (sau cooldown sẽ chuyển sang HALF_OPEN)
 * - HALF_OPEN: Thử lại một vài request, nếu thành công sẽ về CLOSED, nếu fail sẽ về OPEN
 */
public class CircuitBreaker {
    
    public enum State {CLOSED, OPEN, HALF_OPEN}

    private static final int SUCCESS_THRESHOLD = 2;
    private static final long OPEN_COOLDOWN_MILLIS = 30_000L; // 30 giây

    private final int failureThreshold;
    private volatile State state = State.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private volatile long openedAtMillis = 0L;

    public CircuitBreaker(int failureThreshold) {
        this.failureThreshold = Math.max(1, failureThreshold);
    }

    /**
     * Ghi nhận một lần gọi service thành công.
     * Hướng dẫn:
     *  - Nếu state = HALF_OPEN:
     *    + increment successCount
     *    + Nếu successCount >= SUCCESS_THRESHOLD: chuyển về CLOSED, reset counters
     *  - Nếu state = CLOSED: reset failureCount về 0
     */
    public void recordSuccess() {
        State currentState = state;
        
        if (currentState == State.HALF_OPEN) {
            int success = successCount.incrementAndGet();
            if (success >= SUCCESS_THRESHOLD) {
                // Chuyển về CLOSED và reset counters
                synchronized (this) {
                    state = State.CLOSED;
                    failureCount.set(0);
                    successCount.set(0);
                    openedAtMillis = 0L;
                }
            }
        } else if (currentState == State.CLOSED) {
            // Reset failure count khi thành công trong CLOSED state
            failureCount.set(0);
        }
    }

    /**
     * Ghi nhận một lần gọi service thất bại.
     * Hướng dẫn:
     *  - Nếu state = OPEN: return ngay (không làm gì)
     *  - Increment failureCount
     *  - Nếu failureCount >= FAILURE_THRESHOLD: chuyển sang OPEN
     */
    public void recordFailure() {
        State currentState = state;
        
        // Nếu đang OPEN, không làm gì
        if (currentState == State.OPEN) {
            return;
        }
        
        // HALF_OPEN hoặc CLOSED: tăng failure count
        int failures = failureCount.incrementAndGet();
        
        if (failures >= failureThreshold) {
            synchronized (this) {
                // Chuyển sang OPEN và ghi nhận thời gian
                state = State.OPEN;
                openedAtMillis = System.currentTimeMillis();
                successCount.set(0);
            }
        }
    }

    /**
     * Kiểm tra CircuitBreaker có đang OPEN không.
     * Nếu OPEN và đã qua cooldown period, tự động chuyển sang HALF_OPEN.
     */
    public boolean isOpen() {
        State currentState = state;
        
        if (currentState != State.OPEN) {
            return false;
        }
        
        // Kiểm tra cooldown: nếu đã qua 30 giây, chuyển sang HALF_OPEN
        long now = System.currentTimeMillis();
        if (now - openedAtMillis >= OPEN_COOLDOWN_MILLIS) {
            synchronized (this) {
                // Double-check để tránh race condition
                if (state == State.OPEN && (now - openedAtMillis >= OPEN_COOLDOWN_MILLIS)) {
                    state = State.HALF_OPEN;
                    successCount.set(0);
                    failureCount.set(0);
                    return false; // HALF_OPEN cho phép thử lại
                }
            }
        }
        
        return true; // Vẫn đang OPEN và chưa qua cooldown
    }

    /**
     * Lấy state hiện tại của CircuitBreaker.
     */
    public State getState() {
        // Kiểm tra và tự động transition OPEN -> HALF_OPEN nếu cần
        isOpen(); // Gọi để trigger transition nếu cần
        return state;
    }
}

