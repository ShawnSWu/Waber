package com.shawn.user.exception;

public class NotFoundDriverCarTypeException extends RuntimeException {
    public NotFoundDriverCarTypeException() {
        super("Not found driver's car type");
    }
}