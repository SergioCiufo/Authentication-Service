package com.example.autenticationservice.application.exceptions;

import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import com.example.autenticationservice.domain.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
    private RefreshTokenApp refreshTokenApp;

    private void logError(Exception ex, WebRequest request) {
        log.error(
                "An error happened while calling {} API: {}",
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getMessage(), ex
        );
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
        logError(ex, request);
        return ResponseEntity
                .status(401)
                .body(ex.getMessage());
    }

    @ExceptionHandler({MissingTokenException.class})
    public ResponseEntity<Object> handleMissingTokenException(MissingTokenException ex, WebRequest request) {
        logError(ex, request);

        ResponseCookie cleanRefreshCookie = refreshTokenApp.getCleanJwtCookie();

        return ResponseEntity
                .status(401)
                .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                .body(ex.getMessage());
    }

    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
        logError(ex, request);
        return ResponseEntity
                .status(403)
                .body(ex.getMessage());
    }
}
