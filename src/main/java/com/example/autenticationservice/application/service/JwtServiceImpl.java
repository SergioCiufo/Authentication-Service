package com.example.autenticationservice.application.service;

import com.example.autenticationservice.application.jwt.AccessTokenApp;
import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import com.example.autenticationservice.domain.api.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

// TODO è più una utility che un service vero e proprio, essendo così poco utilizzato e con così poco contributo utilizzerei la notazione
// @Component, fa la stessa cosa di @Service ma è più generica(perchè non è un vero e proprio servizio, è più una utility utilizzata da un servizio)
//TODO DONE
@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final AccessTokenApp accessTokenApp;
    private final RefreshTokenApp refreshTokenApp;
    private final HttpServletResponse httpServletResponse;

    @Override
    public String extractAccessJwt() {
        return accessTokenApp.getAccessJwtFromHeader();
    }

    @Override
    public String extractRefreshJwt() {
        return refreshTokenApp.getJwtFromCookie();
    }

    public void setAuthorizationHeader(String accessToken) {
        httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);
    }

    public void generateRefreshCookie(String username){
        ResponseCookie cookie = refreshTokenApp.generateCookie(username);
        setRefreshTokenCookie(cookie);
    }

    public void setRefreshTokenCookie(ResponseCookie refreshToken) {
        httpServletResponse.addCookie(new Cookie(refreshToken.getName(), refreshToken.getValue()));
    }

    public ResponseCookie getCleanJwtCookie() {
        return refreshTokenApp.getCleanJwtCookie();
    }
}
