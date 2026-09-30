package edu.spu.se411.lab08;

public class DashboardObserver implements Observer {
    @Override
    public void update(Subject subject) {
        if (subject instanceof Sensor) {
            Sensor sensor = (Sensor) subject;
            System.out.printf("[Dashboard] %s updated: %.2f %s%n",
                    sensor.getName(), sensor.getReading(),
                    sensor instanceof TemperatureSensor ? "°C" : "%");
        }
    }
}
