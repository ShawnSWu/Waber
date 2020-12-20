package com.shawn.user.exception;

public class AccountIsNotExistException extends RuntimeException {
    public AccountIsNotExistException() {
        super("Account is not exist.");
    }
}
