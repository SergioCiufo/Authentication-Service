package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.RefreshTokenListUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Log4j2
public class TokenService {
    private final RefreshTokenListUtil refreshTokenListUtil;
    private final RefreshTokenJwt refreshTokenJwt;

    public RefreshToken addRefreshToken(String refreshToken, User user) {
        int maxAgeInt = refreshTokenJwt.getExpirationDate();
        Duration maxAge = Duration.ofSeconds(maxAgeInt);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenEnd = now.plus(maxAge);

        if(user == null) {
            log.error("User does not exist");
        }

        RefreshToken refreshJwt = new RefreshToken(null, user ,refreshToken, now, refreshTokenEnd, true);
        refreshTokenListUtil.add(refreshJwt);
        return refreshJwt;
    }

    public RefreshToken getRefreshToken(String refreshTokenString) {
        return refreshTokenListUtil.getRefreshToken(refreshTokenString);
    }

    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenListUtil.invalidate(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenJwt.validateToken(token);
    }
}
