package com.example.autenticationservice.domain.repository;

import com.example.autenticationservice.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenServiceRepo {
    void addRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> getRefreshToken(String refreshToken);
    void invalidateRefreshToken(String refreshToken);
}
