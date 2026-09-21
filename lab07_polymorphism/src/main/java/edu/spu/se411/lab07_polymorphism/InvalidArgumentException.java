
package edu.spu.se411.lab07_polymorphism;

public class InvalidArgumentException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message) {
        super(message);
    }
}