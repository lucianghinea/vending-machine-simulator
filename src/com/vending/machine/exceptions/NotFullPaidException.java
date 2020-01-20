package com.vending.machine.exceptions;

public class NotFullPaidException extends RuntimeException {

    private String message;
    private long remaining;

    public NotFullPaidException(String message, long remaining) {
        this.message = message;
        this.remaining = remaining;
    }

    public long getRemaining() {
        return this.remaining;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
