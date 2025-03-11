package com.example.autenticationservice.domain.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExpireOtpExceptionTest {
    @Test
    public void shouldThrowExpireOtpException_withMessage() {
        //PARAMETERS
        String errorMessage = "errorMessage";

        //TEST
        ExpireOtpException exception = new ExpireOtpException(errorMessage);

        //RESULTS
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }
}
