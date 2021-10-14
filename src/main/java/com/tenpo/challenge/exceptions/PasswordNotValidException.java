package com.tenpo.challenge.exceptions;

public class PasswordNotValidException extends RuntimeException {
    private String message;

    public PasswordNotValidException(String message) {
        super(message);
    }
}
