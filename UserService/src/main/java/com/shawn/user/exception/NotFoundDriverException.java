package com.shawn.user.exception;

public class NotFoundDriverException extends RuntimeException {
    public NotFoundDriverException(String message) {
        super(String.format("Not found driver %s", message));
    }
}
