package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.RefreshTokenServiceRepo;
import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.entities.RefreshToken;
import com.example.autenticationservice.domain.model.entities.User;
//import com.example.autenticationservice.domain.util.RefreshTokenListUtil;

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
    private final RefreshTokenServiceRepo refreshTokenServiceRepo;

    public RefreshToken getRefreshToken(String refreshTokenString) {
        Optional<RefreshToken> refreshToken = refreshTokenServiceRepo.getRefreshToken(refreshTokenString);
        if(refreshToken.isEmpty()) {
            throw new MissingTokenException("Missing refresh token, please Login");
        }
        return refreshToken.get();
    }

    public void invalidateRefreshToken(String refreshTokenString) {
        refreshTokenServiceRepo.invalidateRefreshToken(refreshTokenString);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenJwt.validateToken(token);
    }
}
