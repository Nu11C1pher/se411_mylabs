# SE411 Lab 08 — Advanced OO in Java: Observer Pattern

## Run
Open a terminal in the project folder (the directory containing `pom.xml`) and execute:

```bash
mvn clean package exec:java
```

Requires JDK 11+ and Maven. Each of 10 iterations updates the temperature and humidity readings (with a one-second pause), notifying both observers. The logger also writes to `logs/App/log4j/log.out`.

## Clone demonstration
After the 10 iterations, a temperature sensor is cloned. Its first update has **no observer output**, proving the original observer registrations were not copied. The dashboard is then registered on the clone, so its next update notifies the dashboard alone. Changing the original still notifies its two original observers.

## Design
`Subject` and `Observer` define the required contracts. `Sensor` is an abstract observable base class that reuses registration, notifications, readings, and cloning logic. `TemperatureSensor` and `HumiditySensor` inherit it; `DashboardObserver` and `LoggerObserver` implement the observer interface.
