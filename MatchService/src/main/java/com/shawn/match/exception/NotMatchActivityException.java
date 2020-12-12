package com.shawn.match.exception;

public class NotMatchActivityException extends RuntimeException {
    public NotMatchActivityException() {
        super("Not matched activity.");
    }
}
