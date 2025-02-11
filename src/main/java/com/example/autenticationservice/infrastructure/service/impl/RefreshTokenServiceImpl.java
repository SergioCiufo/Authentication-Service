package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.RefreshTokenServiceRepo;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.infrastructure.mapper.EntityMappers;
import com.example.autenticationservice.infrastructure.model.RefreshTokenEntity;
import com.example.autenticationservice.infrastructure.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenServiceRepo {
    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityMappers entityMappers;

    @Override
    public void addRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = entityMappers.convertFromDomain(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(entityMappers::convertToDomain);
    }

    @Override
    public void invalidateRefreshToken(String refreshTokenString) {
        refreshTokenRepository.invalidateRefreshToken(refreshTokenString);
    }
}
