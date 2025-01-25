package com.example.autenticationservice.domain.exceptions;

public class ExpireOtpException extends RuntimeException {
  public ExpireOtpException(String message) {
    super(message);
  }
}
