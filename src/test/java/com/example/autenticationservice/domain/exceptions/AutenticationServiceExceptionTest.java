package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AutenticationServiceExceptionTest {
    @Test
    public void shouldThrowAutenticationServiceException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        AutenticationServiceException exception = new AutenticationServiceException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowAutenticationServiceException_withMessageAndCause() {
        //PARAMETERS
        String errorMessage = "errorMessage";
        Throwable cause = new RuntimeException("errorCause");

        //TEST
        AutenticationServiceException exception = new AutenticationServiceException(errorMessage, cause);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}
