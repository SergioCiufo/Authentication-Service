package com.example.autenticationservice.domain.api;

public interface JwtService {

    String extractAccessJwt();

    String extractRefreshJwt();

}
