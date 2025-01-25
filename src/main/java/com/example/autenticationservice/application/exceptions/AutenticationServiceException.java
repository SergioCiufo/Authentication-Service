package com.example.autenticationservice.application.exceptions;

public class AutenticationServiceException extends RuntimeException {
  public AutenticationServiceException(String message) {
    super(message);
  }

  public AutenticationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
