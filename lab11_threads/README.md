# Lab 11 Introduction to Threads

Ten accounts start with 1,000 riyals each. The application runs two separate experiments, each with 10,000 Java virtual threads and fresh accounts. Java 21 or later is required.

Run with Maven:

```sh
mvn clean package exec:java
```

The first experiment uses `Transaction.execute()` without synchronization. Concurrent balance reads and writes can lose updates, so the final total can differ from 10,000 riyals. `Thread.yield()` widens the race window for demonstration; scheduling and results vary between runs.

The second uses `Transaction.executeSynchronized()`, which calls one `static synchronized` transfer method. Its shared class monitor protects the entire withdrawal and deposit across all transaction objects. Synchronizing each transaction's instance method would give each object a different lock and would not solve the problem. All concurrent transfers in the safe experiment must use the synchronized entry point.

The main thread joins every transaction thread before calculating the total. It verifies the synchronized total is 10,000 riyals within a small floating-point tolerance. Rejected payments leave balances unchanged. Start, stop, and exception events are written to `logs/log.out`.

This lab uses double values to match the starter API. Production currency calculations should use integer minor units or BigDecimal. The single shared lock is simple but serializes all transfers.
