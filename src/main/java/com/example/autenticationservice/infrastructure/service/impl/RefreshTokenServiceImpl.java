package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.RefreshTokenServiceRepo;
import com.example.autenticationservice.domain.model.entities.RefreshToken;
import com.example.autenticationservice.infrastructure.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenServiceRepo {
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
    public void invalidateRefreshToken(String refreshTokenString) {
        refreshTokenRepository.invalidateRefreshToken(refreshTokenString);
    }
}
