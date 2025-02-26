package com.example.autenticationservice.application.exceptions;

import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import com.example.autenticationservice.domain.exceptions.ApplicationException;
import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionHandlerTest {
    @InjectMocks
    private ApplicationExceptionHandler applicationExceptionHandler;

    @Mock
    private RefreshTokenApp refreshTokenApp;

    @Test
    void shouldHandleApplicationException_whenExceptionThrown() {
        //PARAMETERS
        ApplicationException exception = new ApplicationException("Unauthorized");
        ServletWebRequest servletWebRequest = mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        //MOCK
        doReturn(httpServletRequest).when(servletWebRequest).getRequest();
        doReturn("/test-endpoint").when(httpServletRequest).getRequestURI();

        //TEST
        ResponseEntity<Object> result = applicationExceptionHandler.handleApplicationException(exception, servletWebRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        Assertions.assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void shouldHandleMissionTokenException_whenExceptionThrown() {
        //PARAMETERS
        MissingTokenException exception = new MissingTokenException("Unauthorized");
        ServletWebRequest servletWebRequest = mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        ResponseCookie cleanRefreshCookie = ResponseCookie.from("token", "").build();

        //MOCK
        doReturn(httpServletRequest).when(servletWebRequest).getRequest();
        doReturn("/test-endpoint").when(httpServletRequest).getRequestURI();
        doReturn(cleanRefreshCookie).when(refreshTokenApp).getCleanJwtCookie();

        //TEST
        ResponseEntity<Object> result = applicationExceptionHandler.handleMissingTokenException(exception, servletWebRequest);


        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        Assertions.assertEquals("Unauthorized", result.getBody());

        verify(refreshTokenApp).getCleanJwtCookie();
    }

    @Test
    void shouldHandleTokenExpiredException_whenExceptionThrown() {
        //PARAMETERS
        TokenExpiredException exception = new TokenExpiredException("Forbidden");
        ServletWebRequest servletWebRequest = mock(ServletWebRequest.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        //MOCK
        doReturn(httpServletRequest).when(servletWebRequest).getRequest();
        doReturn("/test-endpoint").when(httpServletRequest).getRequestURI();

        //TEST
        ResponseEntity<Object> result = applicationExceptionHandler.handleTokenExpiredException(exception, servletWebRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals("Forbidden", result.getBody());
    }
}
