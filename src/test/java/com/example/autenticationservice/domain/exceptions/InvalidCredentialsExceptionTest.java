package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidCredentialsExceptionTest {
    @Test
    public void shouldThrowInvalidCredentialsException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
