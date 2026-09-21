
package edu.spu.se411.lab07_polymorphism;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = initializeLogger();

    private static Logger initializeLogger() {
        try {
            Files.createDirectories(
                Path.of("logs", "App", "log4j")
            );
        } catch (IOException e) {
            System.err.println(
                "Failed to create log directory: "
                + e.getMessage()
            );
        }

        return LoggerFactory.getLogger(App.class);
    }

    // Polymorphism: works with all booking types
    public static double computeTotalPrice(Booking booking)
            throws MissingInformationException,
                   InvalidArgumentException {

        return booking.calculateTotalPrice();
    }

    public static void testBooking(Booking booking) {
        try {
            double total = computeTotalPrice(booking);

            System.out.println(
                "Total Price: SAR " + total
            );

        } catch (MissingInformationException
                 | InvalidArgumentException e) {

            System.out.println(
                "Error: " + e.getMessage()
            );

            logger.error(
                "Error in booking " + booking.getBookingId(), e
            );
        }
    }

    public static void main(String[] args) {

        logger.info("Application is starting...");

        System.out.println(
            "===== TRAVEL AGENCY BOOKING SYSTEM ====="
        );

        try {

            FlightBooking flight = new FlightBooking(
                "F001", "Zaid Albelaikhi",
                LocalDate.of(2026, 10, 15),
                "London", 500.0
            );

            TrainBooking standardTrain = new TrainBooking(
                "T001", "Zaid Albelaikhi",
                LocalDate.of(2026, 10, 16),
                "Manchester", SeatClass.STANDARD
            );

            TrainBooking firstClassTrain = new TrainBooking(
                "T002", "Zaid Albelaikhi",
                LocalDate.of(2026, 10, 17),
                "Birmingham", SeatClass.FIRST_CLASS
            );

            CarRentalBooking car = new CarRentalBooking(
                "C001", "Zaid Albelaikhi",
                LocalDate.of(2026, 10, 18),
                "London", 150.0
            );

            Booking[] bookings = {
                flight, standardTrain, firstClassTrain, car
            };

            // Test missing information
            System.out.println(
                "\n===== TEST 1: MISSING INFORMATION ====="
            );

            for (Booking booking : bookings) {
                testBooking(booking);
            }

            // Test invalid inputs
            System.out.println(
                "\n===== TEST 2: INVALID INFORMATION ====="
            );

            try {
                flight.setLuggageWeight(45.0);
            } catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
                logger.error("Invalid luggage weight", e);
            }

            try {
                standardTrain.setDistance(2500.0);
            } catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
                logger.error("Invalid train distance", e);
            }

            try {
                car.setNumberOfDays(35);
            } catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
                logger.error("Invalid rental days", e);
            }

            // Provide valid information
            flight.setLuggageWeight(20.0);
            standardTrain.setDistance(300.0);
            firstClassTrain.setDistance(450.0);
            car.setNumberOfDays(5);

            // Test polymorphism
            System.out.println(
                "\n===== TEST 3: POLYMORPHISM ====="
            );

            for (Booking booking : bookings) {

                System.out.println(
                    "\n-----------------------------"
                );

                booking.displayBookingDetails();
                testBooking(booking);
            }

        } catch (InvalidArgumentException e) {

            System.out.println(
                "Application Error: " + e.getMessage()
            );

            logger.error("Booking processing failed", e);

        } catch (Exception e) {

            logger.error(
                "Unexpected application exception", e
            );

        } finally {

            logger.info("Application is stopping...");

            System.out.println(
                "\n===== APPLICATION FINISHED ====="
            );
        }
    }
}