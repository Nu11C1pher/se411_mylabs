
package edu.spu.se411.lab07_polymorphism;

import java.time.LocalDate;

public abstract class Booking {

    private final String bookingId;
    private final String customerFullName;
    private final LocalDate travelDate;
    private final String destinationCity;

    public Booking(String bookingId,
                   String customerFullName,
                   LocalDate travelDate,
                   String destinationCity)
            throws InvalidArgumentException {

        if (bookingId == null || bookingId.isBlank()) {
            throw new InvalidArgumentException(
                    "Booking ID is required.");
        }

        if (customerFullName == null
                || customerFullName.isBlank()) {
            throw new InvalidArgumentException(
                    "Customer full name is required.");
        }

        if (travelDate == null) {
            throw new InvalidArgumentException(
                    "Travel date is required.");
        }

        if (destinationCity == null
                || destinationCity.isBlank()) {
            throw new InvalidArgumentException(
                    "Destination city is required.");
        }

        this.bookingId = bookingId;
        this.customerFullName = customerFullName;
        this.travelDate = travelDate;
        this.destinationCity = destinationCity;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public abstract double calculateTotalPrice()
            throws MissingInformationException,
                   InvalidArgumentException;

    public void displayBookingDetails() {

        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customerFullName);
        System.out.println("Travel Date: " + travelDate);
        System.out.println("Destination: " + destinationCity);
    }
}