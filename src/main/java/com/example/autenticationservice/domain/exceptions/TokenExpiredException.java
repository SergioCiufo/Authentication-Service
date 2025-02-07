package com.example.autenticationservice.domain.exceptions;

public class TokenExpiredException extends ApplicationException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
