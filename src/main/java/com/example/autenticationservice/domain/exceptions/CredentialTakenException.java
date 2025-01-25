package com.example.autenticationservice.domain.exceptions;

public class CredentialTakenException extends RuntimeException {
  public CredentialTakenException(String message) {
    super(message);
  }
}
