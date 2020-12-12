package com.shawn.user.exception;

public class NotFoundUserLocationException extends RuntimeException {
    public NotFoundUserLocationException(String message) {
        super(String.format("Not found user location %s", message));
    }
}
