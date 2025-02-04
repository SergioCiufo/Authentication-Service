package com.example.autenticationservice.application.service;

import com.example.autenticationservice.application.jwt.AccessTokenJwt;
import com.example.autenticationservice.application.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.api.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final AccessTokenJwt accessTokenJwt;
    private final RefreshTokenJwt refreshTokenJwt;
    private final HttpServletResponse httpServletResponse;

    @Override
    public String generateAccessToken(String username) {
        return accessTokenJwt.generateToken(username);
    }

    @Override
    public String getUsernameFromAccessToken(String token) {
        return accessTokenJwt.getUsernameFromToken(token);
    }

    @Override
    public String extractAccessJwt() {
        return accessTokenJwt.getAccessJwtFromHeader();
    }

    @Override
    public String generateRefreshToken(String username) {
        return refreshTokenJwt.generateToken(username);
    }

    @Override
    public String extractRefreshJwt() {
        return refreshTokenJwt.getJwtFromCookie();
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return refreshTokenJwt.validateToken(token);
    }

    @Override
    public int getExpirationDate() {
       return refreshTokenJwt.getExpirationDate();
    }

    public void setAuthorizationHeader(String accessToken) {
        httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);
    }

    public void generateRefreshCookie(String username){
        ResponseCookie cookie = refreshTokenJwt.generateCookie(username);
        setRefreshTokenCookie(cookie);
    }

    public void setRefreshTokenCookie(ResponseCookie refreshToken) {
        httpServletResponse.addCookie(new Cookie(refreshToken.getName(), refreshToken.getValue()));
    }

    public ResponseCookie getCleanJwtCookie() {
        return refreshTokenJwt.getCleanJwtCookie();
    }
}
