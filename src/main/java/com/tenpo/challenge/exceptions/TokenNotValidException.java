package com.tenpo.challenge.exceptions;

public class TokenNotValidException extends RuntimeException {
    private String message;

    public TokenNotValidException(String message) {
        super(message);
    }
}
