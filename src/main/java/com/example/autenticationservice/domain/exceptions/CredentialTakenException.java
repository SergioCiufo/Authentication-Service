package com.example.autenticationservice.domain.exceptions;

public class CredentialTakenException extends ApplicationException {
    public CredentialTakenException(String message) {
        super(message);
    }
}

