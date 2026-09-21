
package edu.spu.se411.lab07_polymorphism;

import java.time.LocalDate;

public class FlightBooking extends Booking {

    private final double baseTicketPrice;
    private Double luggageWeight;

    public FlightBooking(String bookingId,
                         String customerFullName,
                         LocalDate travelDate,
                         String destinationCity,
                         double baseTicketPrice)
            throws InvalidArgumentException {

        super(bookingId, customerFullName,
              travelDate, destinationCity);

        if (!Double.isFinite(baseTicketPrice)
                || baseTicketPrice < 0) {

            throw new InvalidArgumentException(
                    "Base ticket price must be a valid non-negative number.");
        }

        this.baseTicketPrice = baseTicketPrice;
        this.luggageWeight = null;
    }

    public void setLuggageWeight(Double weight)
            throws InvalidArgumentException {

        if (weight != null
                && (!Double.isFinite(weight)
                    || weight < GlobalConfig.MIN_LUGGAGE_WEIGHT
                    || weight > GlobalConfig.MAX_LUGGAGE_WEIGHT)) {

            throw new InvalidArgumentException(
                    "Luggage weight must be between 0 and 40 kg.");
        }

        this.luggageWeight = weight;
    }

    @Override
    public double calculateTotalPrice()
            throws MissingInformationException {

        if (luggageWeight == null) {

            throw new MissingInformationException(
                    "Luggage weight has not been provided.");
        }

        return baseTicketPrice
                + (luggageWeight
                   * GlobalConfig.EXTRA_LUGGAGE_RATE);
    }

    @Override
    public void displayBookingDetails() {

        super.displayBookingDetails();

        System.out.println("Booking Type: Flight");
        System.out.println("Base Price: " + baseTicketPrice);
        System.out.println("Luggage Weight: " + luggageWeight);
    }
}