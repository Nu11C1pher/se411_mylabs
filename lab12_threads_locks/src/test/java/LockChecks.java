package edu.psu.se411.lab12_threads;
import java.util.concurrent.*;
import edu.psu.se411.lab12_threads.model.*;
import edu.psu.se411.lab12_threads.exceptions.*;
public class LockChecks {
	public static void main(String[] args) throws Exception {
		CryptoAccount a = new CryptoAccount("a", 10000.0);
		CryptoAccount b = new CryptoAccount("b", 10000.0);
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Future<?> first = pool.submit(() -> {
			for(int i = 0; i < 100000; i++) new Transaction("ab", a, b, 0.01).run();
		});
		Future<?> second = pool.submit(() -> {
			for(int i = 0; i < 100000; i++) new Transaction("ba", b, a, 0.01).run();
		});
		pool.shutdown();
		if(!pool.awaitTermination(30, TimeUnit.SECONDS)) {
			System.err.println("Deadlock check failed");
			System.exit(1);
		}
		first.get();
		second.get();
		if(Math.abs(a.getBalance() - 10000) > 0.001 || Math.abs(b.getBalance() - 10000) > 0.001)
			throw new AssertionError("Incorrect transfer");
		try {
			new Transaction("fail", a, b, 1000000.0).execute();
			throw new AssertionError("Expected insufficient balance");
		} catch(InsufficientBalanceException expected) { }
		// Another thread must be able to use both locks after the exception.
		ExecutorService check = Executors.newSingleThreadExecutor();
		try {
			check.submit(() -> new Transaction("after", a, b, 1.0).run()).get(5, TimeUnit.SECONDS);
		} finally { check.shutdownNow(); }
		if(Math.abs(a.getBalance() - 9999) > 0.001 || Math.abs(b.getBalance() - 10001) > 0.001)
			throw new AssertionError("Exception changed balances");
		System.out.println("PASS: opposite transfers, conserved totals, insufficient balance, locks released after exception.");
	}
}
