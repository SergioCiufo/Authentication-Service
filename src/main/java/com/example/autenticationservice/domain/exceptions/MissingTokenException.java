package com.example.autenticationservice.domain.exceptions;

public class MissingTokenException extends ApplicationException {
    public MissingTokenException(String message) {
      super(message);
    }
}
