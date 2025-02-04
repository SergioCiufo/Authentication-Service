package com.example.autenticationservice.domain.api;

public interface JwtService {

    String generateAccessToken(String username);

    String getUsernameFromAccessToken(String token);

    String extractAccessJwt();

    String generateRefreshToken(String username);

    String extractRefreshJwt();

    boolean validateRefreshToken(String token);

    int getExpirationDate();
}
