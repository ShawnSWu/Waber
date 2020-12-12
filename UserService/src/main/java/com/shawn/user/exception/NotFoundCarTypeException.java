package com.shawn.user.exception;

public class NotFoundCarTypeException extends RuntimeException {
    public NotFoundCarTypeException(String message) {
        super(String.format("Not found car type %s", message));
    }
}
