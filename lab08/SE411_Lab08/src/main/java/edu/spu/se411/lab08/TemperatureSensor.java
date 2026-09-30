package edu.spu.se411.lab08;

public class TemperatureSensor extends Sensor {
    public TemperatureSensor(double initialReading) {
        super("Temperature", initialReading);
    }

    @Override
    public TemperatureSensor clone() {
        return (TemperatureSensor) super.clone();
    }
}
