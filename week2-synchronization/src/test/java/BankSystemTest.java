package projects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BankSystem
 * 
 * TODO Tasks:
 * 1. Implement test for deposit operation
 * 2. Implement test for withdraw operation
 * 3. Implement test for transfer operation
 * 4. Implement test for concurrent operations
 * 5. Implement test for deadlock prevention
 * 6. Implement test for timeout behavior
 */
public class BankSystemTest {
    
    private Bank bank;
    private BankAccount account1;
    private BankAccount account2;
    
    @BeforeEach
    void setUp() {
        // TODO: Initialize bank and accounts
        bank = new Bank();
        // TODO: Create test accounts
    }
    
    @Test
    @DisplayName("Test deposit operation")
    void testDeposit() {
        // TODO: Test successful deposit
        // TODO: Verify balance after deposit
        // TODO: Test invalid deposit (negative amount)
    }
    
    @Test
    @DisplayName("Test withdraw operation")
    void testWithdraw() throws InterruptedException {
        // TODO: Test successful withdrawal
        // TODO: Verify balance after withdrawal
        // TODO: Test insufficient funds
        // TODO: Test invalid amount (negative)
    }
    
    @Test
    @DisplayName("Test withdrawal timeout")
    void testWithdrawTimeout() {
        // TODO: Test withdrawal with timeout
        // TODO: Should succeed if lock acquired within timeout
        // TODO: Should fail if lock not acquired within timeout
    }
    
    @Test
    @DisplayName("Test transfer operation")
    void testTransfer() throws InterruptedException {
        // TODO: Test successful transfer
        // TODO: Verify balances after transfer
        // TODO: Test insufficient funds for transfer
    }
    
    @Test
    @DisplayName("Test concurrent deposits")
    void testConcurrentDeposits() throws InterruptedException {
        // TODO: Create multiple threads depositing concurrently
        // TODO: Verify final balance is correct
        // TODO: Should equal initial balance + sum of all deposits
        
        int numThreads = 10;
        double amountPerThread = 100.0;
        double initialBalance = 1000.0;
        
        BankAccount account = new BankAccount("test", initialBalance);
        
        // TODO: Create and start threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        // TODO: Submit tasks
        // TODO: Wait for completion
        // TODO: Verify final balance
    }
    
    @Test
    @DisplayName("Test concurrent withdrawals")
    void testConcurrentWithdrawals() throws InterruptedException {
        // TODO: Test concurrent withdrawals
        // TODO: Some should succeed, some might fail (insufficient funds)
        // TODO: Verify no race condition
    }
    
    @Test
    @DisplayName("Test concurrent transfers")
    void testConcurrentTransfers() throws InterruptedException {
        // TODO: Create multiple threads doing transfers between accounts
        // TODO: Verify no deadlock
        // TODO: Verify final balances are correct
        // TODO: Total balance should be conserved
        
        BankAccount acc1 = new BankAccount("A", 1000.0);
        BankAccount acc2 = new BankAccount("B", 1000.0);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Boolean>> futures = new ArrayList<>();
        
        // TODO: Create bidirectional transfers
        // TODO: This is a good test for deadlock prevention!
        
        // TODO: Wait for all transfers
        // TODO: Verify no deadlock occurred
        // TODO: Verify final balances
    }
    
    @Test
    @DisplayName("Test deadlock prevention with lock ordering")
    void testDeadlockPrevention() throws InterruptedException {
        // TODO: Test that lock ordering prevents deadlock
        // TODO: Create circular transfer scenario
        // TODO: Thread 1: Transfer from A to B
        // TODO: Thread 2: Transfer from B to A
        // TODO: Should not deadlock with lock ordering!
        
        BankAccount acc1 = new BankAccount("A", 1000.0);
        BankAccount acc2 = new BankAccount("Z", 1000.0);
        
        // TODO: Create threads doing bidirectional transfers
        // TODO: If lock ordering is correct, no deadlock should occur
        // TODO: Use timeout to fail if deadlock
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        Future<?> f1 = executor.submit(() -> {
            // TODO: Transfer from acc1 to acc2
        });
        
        Future<?> f2 = executor.submit(() -> {
            // TODO: Transfer from acc2 to acc1
        });
        
        // TODO: Wait with timeout
        try {
            f1.get(5, TimeUnit.SECONDS);
            f2.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            fail("Deadlock detected! Test timed out.");
        }
    }
    
    @Test
    @DisplayName("Test transaction history")
    void testTransactionHistory() throws InterruptedException {
        // TODO: Perform multiple transactions
        // TODO: Verify transaction history is recorded
        // TODO: Verify history is in correct order
    }
    
    @Test
    @DisplayName("Test balance query during concurrent operations")
    void testBalanceQuery() throws InterruptedException {
        // TODO: Query balance while other threads are modifying
        // TODO: Balance should always be consistent
        // TODO: Should not see negative balance during transfer
    }
    
    @Test
    @DisplayName("Stress test with many threads")
    void stressTest() throws InterruptedException {
        // TODO: Create many accounts
        // TODO: Many threads doing random operations
        // TODO: Verify total balance is conserved
        // TODO: No deadlock or race conditions
    }
}
