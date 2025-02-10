package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.RefreshTokenServiceApi;
import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
//import com.example.autenticationservice.domain.util.RefreshTokenListUtil;

import com.example.autenticationservice.infrastructure.service.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class RefreshTokenService {
    private final RefreshTokenJwt refreshTokenJwt;
    private final RefreshTokenServiceApi refreshTokenServiceApi;

    public RefreshToken addRefreshToken(String refreshToken, User user) {
        int maxAgeInt = refreshTokenJwt.getExpirationDate();
        Duration maxAge = Duration.ofSeconds(maxAgeInt);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenEnd = now.plus(maxAge);

        if(user == null) {
            log.error("User does not exist: {}", user.getUsername());
            //mettere una throw
        }
        RefreshToken refreshJwt = new RefreshToken(null, user ,refreshToken, now, refreshTokenEnd, true);
        refreshTokenServiceApi.addRefreshToken(refreshJwt);
        return refreshJwt;
    }

    public RefreshToken getRefreshToken(String refreshTokenString) {
        Optional<RefreshToken> refreshToken = refreshTokenServiceApi.getRefreshToken(refreshTokenString);
        if(refreshToken.isEmpty()) {
            throw new MissingTokenException("Missing refresh token, please Login");
        }
        return refreshToken.get();
    }

    public void invalidateRefreshToken(String refreshTokenString) {
        refreshTokenServiceApi.invalidateRefreshToken(refreshTokenString);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenJwt.validateToken(token);
    }
}
