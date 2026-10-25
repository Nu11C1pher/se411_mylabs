package edu.psu.se411.lab12_threads;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import edu.psu.se411.lab12_threads.model.*;
public class App {
	public static Integer nbAccounts = 10;
	public static Double initialBalance = 10000.0;
	public static Integer nbTransactions = 1000000;
	public static void main(String[] args) throws InterruptedException { run(false, args); }
	public static void run(boolean cached, String[] args) throws InterruptedException {
		int count = args.length > 0 ? Integer.parseInt(args[0]) : nbAccounts;
		int transactions = args.length > 1 ? Integer.parseInt(args[1]) : nbTransactions;
		if(count < 2 || transactions < 1) throw new IllegalArgumentException("Invalid workload");
		CryptoAccount[] accounts = new CryptoAccount[count];
		for(int i = 0; i < count; i++) accounts[i] = new CryptoAccount("acc_" + i, initialBalance);
		double initial = sumTotalMoneyInAccounts(accounts);
		int threads = Math.min(32, Runtime.getRuntime().availableProcessors());
		AtomicInteger created = new AtomicInteger();
		ThreadFactory factory = task -> new Thread(task, "worker-" + created.incrementAndGet());
		ExecutorService executor = cached ? Executors.newCachedThreadPool(factory)
				: Executors.newFixedThreadPool(threads, factory);
		// Same submission limit in both programs prevents native-thread exhaustion.
		Semaphore pending = new Semaphore(256);
		AtomicInteger completed = new AtomicInteger();
		AtomicInteger failures = new AtomicInteger();
		Random random = new Random(411);
		long startTime = System.nanoTime();
		try {
			for(int i = 0; i < transactions; i++) {
				int sender = random.nextInt(count);
				int receiver = random.nextInt(count - 1);
				if(receiver >= sender) receiver++;
				Transaction t = new Transaction("id_" + i, accounts[sender],
						accounts[receiver], random.nextInt(10000) / 100.0);
				pending.acquire();
				executor.execute(() -> {
					try { t.run(); }
					catch(RuntimeException e) { failures.incrementAndGet(); }
					finally { completed.incrementAndGet(); pending.release(); }
				});
			}
		} finally {
			executor.shutdown();
			if(!executor.awaitTermination(5, TimeUnit.MINUTES)) {
				executor.shutdownNow();
				throw new IllegalStateException("Transactions did not finish");
			}
		}
		double milliseconds = (System.nanoTime() - startTime) / 1000000.0;
		double total = sumTotalMoneyInAccounts(accounts);
		for(CryptoAccount account : accounts)
			if(account.getBalance() < 0) throw new IllegalStateException("Negative balance");
		if(completed.get() != transactions || failures.get() != 0 || Math.abs(total - initial) > 0.001)
			throw new IllegalStateException("Verification failed");
		System.out.printf("%s accounts=%d transactions=%d time_ms=%.3f threads=%d initial=%.2f final=%.2f completed=%d errors=%d%n",
				cached ? "cached" : "fixed", count, transactions, milliseconds, created.get(),
				initial, total, completed.get(), failures.get());
	}
	private static Double sumTotalMoneyInAccounts(CryptoAccount[] accounts) {
		Double total = 0.0;
		for(CryptoAccount c : accounts) total += c.getBalance();
		return total;
	}
}
