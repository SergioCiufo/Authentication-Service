package com.example.autenticationservice.domain.exceptions;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
