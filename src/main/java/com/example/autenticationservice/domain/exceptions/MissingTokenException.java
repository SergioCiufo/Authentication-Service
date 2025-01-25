package com.example.autenticationservice.domain.exceptions;

public class MissingTokenException extends RuntimeException {
  public MissingTokenException(String message) {
    super(message);
  }
}
