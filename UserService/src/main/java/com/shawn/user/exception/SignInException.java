package com.shawn.user.exception;

public class SignInException extends RuntimeException {
    public SignInException() {
        super("Email or password is not correct.");
    }
}
