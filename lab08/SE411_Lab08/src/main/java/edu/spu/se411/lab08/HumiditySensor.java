package edu.spu.se411.lab08;

public class HumiditySensor extends Sensor {
    public HumiditySensor(double initialReading) {
        super("Humidity", initialReading);
    }

    @Override
    public HumiditySensor clone() {
        return (HumiditySensor) super.clone();
    }
}
