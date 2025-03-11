package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenExpiredExceptionTest {
    @Test
    public void shouldThrowTokenExpiredException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        TokenExpiredException exception = new TokenExpiredException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
