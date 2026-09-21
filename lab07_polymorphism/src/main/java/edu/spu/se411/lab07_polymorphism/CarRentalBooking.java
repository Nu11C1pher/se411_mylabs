
package edu.spu.se411.lab07_polymorphism;

import java.time.LocalDate;

public class CarRentalBooking extends Booking {

    private final double dailyRentalRate;
    private Integer numberOfDays;

    public CarRentalBooking(String bookingId,
                            String customerFullName,
                            LocalDate travelDate,
                            String destinationCity,
                            double dailyRentalRate)
            throws InvalidArgumentException {

        super(bookingId, customerFullName,
              travelDate, destinationCity);

        if (!Double.isFinite(dailyRentalRate)
                || dailyRentalRate < 0) {

            throw new InvalidArgumentException(
                    "Daily rental rate must be a valid non-negative number.");
        }

        this.dailyRentalRate = dailyRentalRate;
        this.numberOfDays = null;
    }

    public void setNumberOfDays(Integer days)
            throws InvalidArgumentException {

        if (days != null
                && (days < GlobalConfig.MIN_RENTAL_DAYS
                    || days > GlobalConfig.MAX_RENTAL_DAYS)) {

            throw new InvalidArgumentException(
                    "Rental days must be between 1 and 30.");
        }

        this.numberOfDays = days;
    }

    @Override
    public double calculateTotalPrice()
            throws MissingInformationException {

        if (numberOfDays == null) {

            throw new MissingInformationException(
                    "Number of rental days has not been provided.");
        }

        return dailyRentalRate * numberOfDays;
    }

    @Override
    public void displayBookingDetails() {

        super.displayBookingDetails();

        System.out.println("Booking Type: Car Rental");
        System.out.println("Daily Rate: " + dailyRentalRate);
        System.out.println("Rental Days: " + numberOfDays);
    }
}