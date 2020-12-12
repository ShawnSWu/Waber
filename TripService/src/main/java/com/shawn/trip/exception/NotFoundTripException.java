package com.shawn.trip.exception;

public class NotFoundTripException extends RuntimeException {
    public NotFoundTripException(String message) {
        super(String.format("Not found trip %s", message));
    }
}
