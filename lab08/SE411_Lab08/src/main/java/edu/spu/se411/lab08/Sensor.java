package edu.spu.se411.lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Shared implementation for all observable sensors. */
public abstract class Sensor implements Subject, Cloneable {
    private final String name;
    private double reading;
    private List<Observer> observers = new ArrayList<>();

    protected Sensor(String name, double initialReading) {
        this.name = Objects.requireNonNull(name, "name");
        this.reading = initialReading;
    }

    public String getName() {
        return name;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double newReading) {
        if (Double.compare(reading, newReading) != 0) {
            reading = newReading;
            notifyObservers();
        }
    }

    @Override
    public void register(Observer o) {
        Objects.requireNonNull(o, "observer");
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        // A snapshot avoids modification errors if an observer changes registrations.
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update(this);
        }
    }

    @Override
    public Sensor clone() {
        try {
            Sensor copy = (Sensor) super.clone();
            // Clones keep the reading and sensor type, but never inherit observers.
            copy.observers = new ArrayList<>();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Sensor implements Cloneable", e);
        }
    }
}
