package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationExceptionTest {
    @Test
    public void shouldThrowApplicationException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        ApplicationException exception = new ApplicationException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowApplicationException_withMessageAndCause() {
        //PARAMETERS
        String errorMessage = "errorMessage";
        Throwable cause = new RuntimeException("errorCause");

        //TEST
        ApplicationException exception = new ApplicationException(errorMessage, cause);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}
