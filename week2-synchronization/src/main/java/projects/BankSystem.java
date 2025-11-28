package projects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Project: Thread-Safe Banking System
 * 
 * TODO Tasks:
 * 1. Implement BankAccount with ReentrantLock
 * 2. Implement deposit() and withdraw() methods
 * 3. Implement transfer() method with deadlock prevention (lock ordering)
 * 4. Add transaction logging
 * 5. Implement withdrawal with timeout
 * 6. Add balance query and transaction history
 * 7. Test with multiple concurrent operations
 */
public class BankSystem {
    
    private static final int NUM_ACCOUNTS = 10;
    private static final int NUM_TRANSACTIONS = 100;
    private static final double INITIAL_BALANCE = 10000.0;
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  Thread-Safe Banking System Demo");
        System.out.println("==========================================\n");
        
        // TODO: Create bank system
        Bank bank = new Bank();
        
        // TODO: Create accounts
        System.out.println("Creating accounts...");
        createAccounts(bank);
        
        // TODO: Run concurrent transactions
        System.out.println("\nRunning concurrent transactions...");
        runTransactions(bank);
        
        // TODO: Display final state
        System.out.println("\nFinal Account Balances:");
        displayBalances(bank);
        
        // TODO: Display transaction logs
        System.out.println("\nTransaction Log (last 10):");
        displayRecentTransactions(bank);
        
        System.out.println("\n==========================================");
        System.out.println("  Demo completed!");
        System.out.println("==========================================");
    }
    
    private static void createAccounts(Bank bank) {
        // TODO: Create NUM_ACCOUNTS accounts with INITIAL_BALANCE
        for (int i = 1; i <= NUM_ACCOUNTS; i++) {
            // TODO: Create account
            // TODO: Add to bank
            BankAccount account = new BankAccount("acc_"+i, INITIAL_BALANCE);
            bank.addAccount(account);
        }
        System.out.println("Created " + NUM_ACCOUNTS + " accounts");
    }
    
    private static void runTransactions(Bank bank) {
        List<Thread> threads = new ArrayList<>();
        
        // TODO: Create threads for concurrent transactions
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                // TODO: Perform random transactions
                // - Deposit random amount
                // - Withdraw random amount
                // - Transfer between random accounts
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                for (int j = 0; j < NUM_TRANSACTIONS; j++) {
                    int action = rand.nextInt(3);
                    String accId1 = "acc_" + (rand.nextInt(NUM_ACCOUNTS) + 1);
                    BankAccount account1 = bank.getAccount(accId1);
                    double amount = rand.nextDouble(1, 500);
                    System.out.println("Thread " + Thread.currentThread().getName() + " performing action " + action + " on " + accId1 + " amount " + amount);

                    try {
                        switch (action) {
                            case 0: // Deposit
                                account1.deposit(amount);
                                break;
                            case 1: // Withdraw
                                account1.withdraw(amount);
                                break;
                            case 2: // Transfer
                                String accId2;
                                do {
                                    accId2 = "acc_" + (rand.nextInt(NUM_ACCOUNTS) + 1);
                                } while (accId2.equals(accId1));
                                BankAccount account2 = bank.getAccount(accId2);
                                account1.transfer(account2, amount);
                                break;

                        }
                    } catch (InterruptedException | IllegalArgumentException e) {
                        System.out.println("Transaction failed: " + e.getMessage());
                    }
                }
            });
            threads.add(thread);
        }
        
        // TODO: Start and wait for all threads
        threads.forEach(Thread::start);
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void displayBalances(Bank bank) {
        // TODO: Display all account balances
        for (BankAccount account : bank.getAllAccounts()) {
            System.out.println("Account " + account.getAccountId() + ": Balance = " + account.getBalance());
        }
    }
    
    private static void displayRecentTransactions(Bank bank) {
        // TODO: Display last 10 transactions
        List<Transaction> recent = bank.getRecentTransactions(10);
        for (Transaction tx : recent) {
            System.out.println(tx);
        }
    }
    
    // TODO: Implement BankAccount class
    static class BankAccount {
        private final String accountId;
        private double balance;
        private final Lock lock;
        private final List<Transaction> transactions;
        
        public BankAccount(String accountId, double initialBalance) {
            // TODO: Initialize fields
            // TODO: Use ReentrantLock
            this.accountId = accountId;
            this.balance = initialBalance;
            this.lock = new ReentrantLock();
            this.transactions = new ArrayList<>();
        }
        
        // TODO: Implement deposit
        public boolean deposit(double amount) {
            // TODO: Validate amount
            // TODO: Acquire lock
            // TODO: Update balance
            // TODO: Log transaction
            // TODO: Always unlock in finally
            System.out.println("Depositing " + amount + " to " + accountId);

            if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
               throw new IllegalArgumentException("Amount must be a positive number");
            }
            lock.lock();
            try {
                balance += amount;
                System.out.println("Deposited " + amount + " to " + accountId);
                transactions.add(new Transaction("DEPOSIT", accountId, accountId, amount, true));
            } finally {
                lock.unlock();
            }

            return false;
        }
        
        // TODO: Implement withdraw with timeout
        public boolean withdraw(double amount) throws InterruptedException {
            // TODO: Validate amount
            // TODO: Try to acquire lock with timeout (2 seconds)
            // TODO: Check sufficient balance
            // TODO: Update balance
            // TODO: Log transaction
            // TODO: Always unlock in finally
            System.out.println("Withdrawing " + amount + " from " + accountId);
            if(amount <= 0) {
                System.out.println("Amount must be a positive number");
                throw new IllegalArgumentException("Amount must be a positive number");
            }
            boolean acquired = false;
            try {
                acquired = lock.tryLock(2, TimeUnit.SECONDS);
                if(amount > balance) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                balance -= amount;
                System.out.println("Withdrawn " + amount + " from " + accountId);
                transactions.add(new Transaction("WITHDRAW", accountId, accountId, amount, true));
            } catch (InterruptedException e) {
                return false;
            } finally {
                if(acquired)
                    lock.unlock();
            }
            return false;
        }
        
        // TODO: Implement withdraw (no timeout)
        public boolean withdraw(double amount, boolean waitForLock) throws InterruptedException {
            // TODO: If waitForLock, use lock()
            // TODO: Else, use tryLock()
            // TODO: Handle accordingly
            System.out.println("Withdrawing " + amount + " from " + accountId);
            if(!waitForLock) {
                return withdraw(amount);
            }

            if(amount <= 0) {
                System.out.println("Amount must be a positive number");
                throw new IllegalArgumentException("Amount must be a positive number");
            }


            try {
                lock.lock();
                if(amount > balance) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                balance -= amount;
                System.out.println("Withdrawn " + amount + " from " + accountId);
                transactions.add(new Transaction("WITHDRAW", accountId, accountId, amount, true));
            } finally {
                lock.unlock();
            }
            return false;
        }
        
        // TODO: Implement transfer to another account
        public boolean transfer(BankAccount target, double amount) throws InterruptedException {
            // TODO: CRITICAL: Prevent deadlock with lock ordering!
            // TODO: Order locks by accountId (compareTo or ID comparison)
            // TODO: Always acquire "smaller" lock first
            
            // TODO: Determine which account is "first" and "second"
            
            // TODO: Acquire first lock
            // TODO: Then acquire second lock
            // TODO: Try to withdraw from this account
            // TODO: Deposit to target account
            // TODO: Log transaction
            // TODO: Unlock both locks in proper order (reverse!)
            System.out.println("Transferring " + amount + " from " + this.accountId + " to " + target.accountId);

            this.lock.lock();
            try {
                target.lock.lock();
                try {
                    if(amount > this.balance) {
                        throw new IllegalArgumentException("Insufficient balance");
                    }
                    this.balance -= amount;
                    target.balance += amount;
                    System.out.println("Transferred " + amount + " from " + this.accountId + " to " + target.accountId);
                    Transaction tx = new Transaction("TRANSFER", this.accountId, target.accountId, amount, true);
                } finally {
                    target.lock.unlock();
                }
            } finally {
                this.lock.unlock();
            }

            return false;
        }
        
        // TODO: Implement balance query
        public double getBalance() {
            // TODO: Use read lock or synchronized read
            // TODO: Return current balance
            try {
                lock.lock();
                return balance;
            } finally {
                lock.unlock();
            }
        }
        
        public String getAccountId() {
            return accountId;
        }
        
        // TODO: Implement transaction history
        public List<Transaction> getTransactionHistory() {
            // TODO: Return copy of transaction list
            try {
                lock.lock();
                return new ArrayList<>(transactions);
            } finally {
                lock.unlock();
            }
        }
        
        // Helper method to determine lock order
        public boolean isBefore(BankAccount other) {
            // TODO: Compare accountId to determine lock order
            return this.accountId.compareTo(other.accountId) < 0;
        }
    }
    
    // TODO: Implement Transaction class
    static class Transaction {
        private final String transactionId;
        private final String fromAccount;
        private final String toAccount;
        private final String type; // "DEPOSIT", "WITHDRAW", "TRANSFER"
        private final double amount;
        private final long timestamp;
        private final boolean success;
        
        public Transaction(String type, String fromAccount, String toAccount, 
                          double amount, boolean success) {
            // TODO: Initialize fields
            this.transactionId = UUID.randomUUID().toString();
            this.type = type;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
            this.success = success;
            this.timestamp = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            // TODO: Format transaction as string
            return String.format("[%s] %s %.2f %s -> %s %s", 
                transactionId.substring(0, 8), type, amount, fromAccount, toAccount, 
                success ? "SUCCESS" : "FAILED");
        }
    }
    
    // TODO: Implement Bank class
    static class Bank {
        private final Map<String, BankAccount> accounts;
        private final List<Transaction> allTransactions;
        
        public Bank() {
            // TODO: Initialize with concurrent collections
            this.accounts = new ConcurrentHashMap<>();
            this.allTransactions = Collections.synchronizedList(new ArrayList<>());
        }
        
        public void addAccount(BankAccount account) {
            accounts.put(account.getAccountId(), account);
        }
        
        public BankAccount getAccount(String accountId) {
            return accounts.get(accountId);
        }
        
        public Collection<BankAccount> getAllAccounts() {
            return accounts.values();
        }
        
        public List<Transaction> getRecentTransactions(int count) {
            // TODO: Return last N transactions
            int size = allTransactions.size();
            int start = Math.max(0, size - count);
            return new ArrayList<>(allTransactions.subList(start, size));
        }
    }
}
