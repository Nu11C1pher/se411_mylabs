package edu.psu.se411.lab12_threads.model;
import java.util.concurrent.locks.ReentrantLock;
import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;
public class CryptoAccount {
	private final String id;
	private Double balance;
	private final ReentrantLock lock = new ReentrantLock();
	public CryptoAccount(String id, Double balance) {
		if(id == null || id.isEmpty() || balance == null || !Double.isFinite(balance) || balance < 0)
			throw new IllegalArgumentException("Invalid account");
		this.id = id;
		this.balance = balance;
	}
	public void addToBalance(Double amount) {
		checkAmount(amount);
		lock.lock();
		try { balance += amount; } finally { lock.unlock(); }
	}
	public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		checkAmount(amount);
		lock.lock();
		try {
			if(amount > balance) throw new InsufficientBalanceException(id);
			balance -= amount;
		} finally { lock.unlock(); }
	}
	private void checkAmount(Double amount) {
		if(amount == null || !Double.isFinite(amount) || amount < 0)
			throw new IllegalArgumentException("Invalid amount");
	}
	public String getId() { return id; }
	public ReentrantLock getLock() { return lock; }
	public Double getBalance() {
		lock.lock();
		try { return balance; } finally { lock.unlock(); }
	}
}
