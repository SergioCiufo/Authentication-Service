package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenServiceRepo {
    void addRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> getRefreshToken(String refreshToken);
    void invalidateRefreshToken(String refreshToken);
}
