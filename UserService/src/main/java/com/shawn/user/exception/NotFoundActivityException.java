package com.shawn.user.exception;

public class NotFoundActivityException extends RuntimeException {
    public NotFoundActivityException() {
        super("Activity is not exist.");
    }
}
