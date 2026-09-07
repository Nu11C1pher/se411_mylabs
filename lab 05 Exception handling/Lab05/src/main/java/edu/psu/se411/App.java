package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;
import edu.psu.se411.exceptions.InvalidAgeException;
import edu.psu.se411.wallet.Wallet;

public class App {

    public static void validateAge(int age)
            throws InvalidAgeException {

        if (age < 18) {
            throw new InvalidAgeException(
                    "Age must be 18 or older. Entered age: " + age);
        }

        System.out.println("Age is valid.");
    }

    public static void main(String[] args) {

        // Exercise 1
        System.out.println("Exercise 1: Age validation");

        try {
            validateAge(20);
            validateAge(16);
        } catch (InvalidAgeException e) {
            System.out.println(
                    "InvalidAgeException: " + e.getMessage());
        }

        // Exercise 2
        System.out.println("\nExercise 2: Online wallet");

        Wallet wallet = new Wallet(500.00);

        try {
            wallet.withdraw(150.00);

            System.out.println("Withdrawal successful.");
            System.out.println(
                    "Current wallet balance: SAR "
                    + wallet.getBalance());

            wallet.withdraw(400.00);

        } catch (InsufficientFundsException e) {
            System.out.println(
                    "InsufficientFundsException: "
                    + e.getMessage());
        }
    }
}