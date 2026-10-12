package edu.psu.se411.lab11_threads;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.psu.se411.lab11_threads.model.CryptoAccount;
import edu.psu.se411.lab11_threads.model.Transaction;
import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

/** Build and run: mvn clean package exec:java (Java 21 or later). */
public class App {
    public static final int nbAccounts = 10;
    public static final double initialBalance = 1000.0;
    public static final int nbTransactions = 10000;

    public static double calculateTotal(List<CryptoAccount> accounts) {
        return accounts.stream().mapToDouble(CryptoAccount::getBalance).sum();
    }

    public static double runExperiment(boolean synchronizedTransfers, Logger logger)
            throws InterruptedException {
        List<CryptoAccount> accounts = new ArrayList<>();
        for (int i = 0; i < nbAccounts; i++) {
            accounts.add(new CryptoAccount("acc_" + i, initialBalance));
        }
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger completed = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < nbTransactions; i++) {
            // Virtual threads allow 10,000 actual threads without 10,000 OS stacks.
            threads.add(Thread.ofVirtual().name("transaction-" + i).start(() -> {
                try {
                    start.await();
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    Transaction transaction = new Transaction(
                            accounts.get(random.nextInt(nbAccounts)),
                            accounts.get(random.nextInt(nbAccounts)),
                            random.nextDouble(1.0, 11.0));
                    if (synchronizedTransfers) transaction.executeSynchronized();
                    else transaction.execute();
                    completed.incrementAndGet();
                } catch (InsufficientBalanceException exception) {
                    rejected.incrementAndGet();
                    logger.warn("Transaction rejected", exception);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    logger.error("Transaction interrupted", exception);
                }
            }));
        }
        start.countDown();
        for (Thread thread : threads) thread.join();
        double total = calculateTotal(accounts);
        System.out.printf("%s: threads=%d, completed=%d, rejected=%d, total=%.6f, difference=%.6f%n",
                synchronizedTransfers ? "Synchronized" : "Unsynchronized",
                threads.size(), completed.get(), rejected.get(), total,
                total - nbAccounts * initialBalance);
        return total;
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("logs"));
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Application is starting...");
        try {
            runExperiment(false, logger);
            double safeTotal = runExperiment(true, logger);
            if (Math.abs(safeTotal - nbAccounts * initialBalance) > 0.000001) {
                throw new IllegalStateException("Synchronized transfers changed the total");
            }
        } catch (Exception exception) {
            logger.error("Application failed", exception);
            throw exception;
        } finally {
            logger.info("Application is closing...");
        }
    }
}
