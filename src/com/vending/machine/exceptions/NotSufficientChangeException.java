package com.vending.machine.exceptions;

public class NotSufficientChangeException extends RuntimeException {

    private String message;

    public NotSufficientChangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
