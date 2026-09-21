
package edu.spu.se411.lab07_polymorphism;

import java.time.LocalDate;

public class TrainBooking extends Booking {

    private final SeatClass seatClass;
    private Double distance;

    public TrainBooking(String bookingId,
                        String customerFullName,
                        LocalDate travelDate,
                        String destinationCity,
                        SeatClass seatClass)
            throws InvalidArgumentException {

        super(bookingId, customerFullName,
              travelDate, destinationCity);

        if (seatClass == null) {
            throw new InvalidArgumentException(
                    "Seat class is required.");
        }

        this.seatClass = seatClass;
        this.distance = null;
    }

    public void setDistance(Double distance)
            throws InvalidArgumentException {

        if (distance != null
                && (!Double.isFinite(distance)
                    || distance < GlobalConfig.MIN_TRAIN_DISTANCE
                    || distance > GlobalConfig.MAX_TRAIN_DISTANCE)) {

            throw new InvalidArgumentException(
                    "Train distance must be between 1 and 2000 km.");
        }

        this.distance = distance;
    }

    @Override
    public double calculateTotalPrice()
            throws MissingInformationException {

        if (distance == null) {
            throw new MissingInformationException(
                    "Train distance has not been provided.");
        }

        double rate;

        if (seatClass == SeatClass.STANDARD) {
            rate = GlobalConfig.TRAIN_STANDARD_RATE;
        } else {
            rate = GlobalConfig.TRAIN_FIRST_CLASS_RATE;
        }

        return distance * rate;
    }

    @Override
    public void displayBookingDetails() {

        super.displayBookingDetails();

        System.out.println("Booking Type: Train");
        System.out.println("Seat Class: " + seatClass);
        System.out.println("Distance: " + distance + " km");
    }
}