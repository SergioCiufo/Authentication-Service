package com.example.autenticationservice.domain.exceptions;

public class InvalidSessionException extends ApplicationException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
