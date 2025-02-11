package com.example.autenticationservice.infrastructure.api;

import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;

public interface RefreshTokenJwtInf {
    RefreshToken refreshToken(User user);
}
