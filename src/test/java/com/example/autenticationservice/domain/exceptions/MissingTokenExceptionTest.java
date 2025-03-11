package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MissingTokenExceptionTest {
    @Test
    public void shouldThrowMissingTokenException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        MissingTokenException exception = new MissingTokenException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
