package edu.psu.se411.lab11_threads.model;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {
    private final String id;
    private double balance;

    public CryptoAccount(String id, Double balance) {
        if (id == null || balance == null || !Double.isFinite(balance) || balance < 0) {
            throw new IllegalArgumentException("Invalid account");
        }
        this.id = id;
        this.balance = balance;
    }

    private static void validateAmount(Double amount) {
        if (amount == null || !Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and nonnegative");
        }
    }

    // Deliberately unsynchronized for the first experiment.
    public void addToBalance(Double amount) {
        validateAmount(amount);
        double previous = balance;
        Thread.yield(); // Widen the race window so lost updates are easier to observe.
        balance = previous + amount;
    }

    public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
        validateAmount(amount);
        double previous = balance;
        if (previous < amount) {
            throw new InsufficientBalanceException("Insufficient balance in " + id);
        }
        Thread.yield();
        balance = previous - amount;
    }

    public String getId() { return id; }
    public Double getBalance() { return balance; }
}
