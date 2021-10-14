package com.tenpo.challenge.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private String message;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
