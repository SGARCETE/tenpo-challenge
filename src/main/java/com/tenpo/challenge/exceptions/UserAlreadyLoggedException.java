package com.tenpo.challenge.exceptions;

public class UserAlreadyLoggedException extends RuntimeException {
    private String message;

    public UserAlreadyLoggedException(String message) {
        super(message);
    }
}
