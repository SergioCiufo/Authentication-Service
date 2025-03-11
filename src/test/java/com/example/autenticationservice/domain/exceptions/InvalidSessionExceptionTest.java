package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidSessionExceptionTest {
    @Test
    public void shouldThrowInvalidSessionException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        InvalidSessionException exception = new InvalidSessionException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
