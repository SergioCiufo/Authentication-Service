package com.example.autenticationservice.application.service;

import com.example.autenticationservice.application.jwt.AccessTokenApp;
import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import com.example.autenticationservice.domain.api.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

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
