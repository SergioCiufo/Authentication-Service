package com.example.autenticationservice.application.exceptions;

import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import com.example.autenticationservice.domain.exceptions.AutenticationServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AutenticationServiceExceptionHandlerTest {
    @InjectMocks
    private AutenticationServiceExceptionHandler autenticationServiceExceptionHandler;

    @Mock
    private RefreshTokenApp refreshTokenApp;

    @Test
    void shouldHandleAutenticationServiceException_whenExceptionThrown() {
        //PARAMETERS
        AutenticationServiceException exception = mock(AutenticationServiceException.class);
        ServletWebRequest servletWebRequest = mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        //MOCK
        doReturn(httpServletRequest).when(servletWebRequest).getRequest();
        doReturn("/test-endpoint").when(httpServletRequest).getRequestURI();


        //TEST
        ResponseEntity<Object> result = autenticationServiceExceptionHandler.handleAutenticationServiceException(exception, servletWebRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.getStatusCode());
        Assertions.assertNull(result.getBody());
    }
}
