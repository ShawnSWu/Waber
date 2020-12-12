package com.shawn.match.exception;

public class NotMatchCarTypeException extends RuntimeException {
    public NotMatchCarTypeException() {
        super("Not matched car type.");
    }
}
