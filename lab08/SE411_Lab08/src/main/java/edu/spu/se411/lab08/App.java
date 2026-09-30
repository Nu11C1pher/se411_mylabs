package edu.spu.se411.lab08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // FileAppender needs its parent directory to exist before the first log entry.
        try {
            Files.createDirectories(Paths.get("logs", "App", "log4j"));
        } catch (IOException e) {
            System.err.println("Could not create log directory: " + e.getMessage());
            return;
        }

        logger.info("Application is starting...");
        Random random = new Random();
        TemperatureSensor temp = new TemperatureSensor(20);
        HumiditySensor humidity = new HumiditySensor(40);

        Observer dashboard = new DashboardObserver();
        Observer sensorLogger = new LoggerObserver();

        // Polymorphism: both sensors register the same Observer implementations.
        temp.register(dashboard);
        temp.register(sensorLogger);
        humidity.register(dashboard);
        humidity.register(sensorLogger);

        for (int i = 0; i < 10; i++) {
            System.out.printf("%n--- Reading %d --- %n", i + 1);
            temp.setReading(20 + random.nextDouble() * 15);
            humidity.setReading(40 + random.nextDouble() * 20);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Sensor simulation interrupted", e);
                return;
            }
        }

        // Clone test: original observers MUST NOT receive changes from its clone.
        System.out.println("\n--- Clone test (no output from existing observers expected) ---");
        TemperatureSensor tempClone = temp.clone();
        tempClone.setReading(99); // Its observer list is independent and starts empty.

        System.out.println("--- Register dashboard on clone only ---");
        tempClone.register(dashboard);
        tempClone.setReading(25); // Dashboard only; logger is not subscribed to clone.

        System.out.println("--- Original sensor still has its own observers ---");
        temp.setReading(26); // Both original observers receive this update.
        logger.info("Application finished.");
    }
}
