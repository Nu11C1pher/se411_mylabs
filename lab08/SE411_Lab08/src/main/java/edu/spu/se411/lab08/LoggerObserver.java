package edu.spu.se411.lab08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);

    @Override
    public void update(Subject subject) {
        if (subject instanceof Sensor) {
            Sensor sensor = (Sensor) subject;
            System.out.printf("[Logger] %s changed to %.2f%n",
                    sensor.getName(), sensor.getReading());
            logger.info("{} sensor reading changed to {}", sensor.getName(), sensor.getReading());
        }
    }
}
