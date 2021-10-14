package com.tenpo.challenge.exceptions;

public class UserNotLoggedException extends RuntimeException {
    private String message;

    public UserNotLoggedException(String message) {
        super(message);
    }

}
