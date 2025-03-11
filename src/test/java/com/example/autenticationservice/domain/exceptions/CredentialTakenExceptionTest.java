package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CredentialTakenExceptionTest {
    @Test
    public void shouldThrowCredentialTakenException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        CredentialTakenException exception = new CredentialTakenException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
