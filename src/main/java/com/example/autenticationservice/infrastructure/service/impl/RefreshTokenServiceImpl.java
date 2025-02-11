package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.RefreshTokenServiceApi;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.infrastructure.service.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenServiceApi {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    @Transactional //svolge tutto in una transazione
    public void invalidateRefreshToken(String refreshTokenString) {
        refreshTokenRepository.invalidateRefreshToken(refreshTokenString);
    }
}
