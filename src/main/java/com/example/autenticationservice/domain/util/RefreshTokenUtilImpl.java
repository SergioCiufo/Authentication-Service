package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.service.impl.RefreshTokenService;
import com.example.autenticationservice.infrastructure.api.RefreshTokenJwtInf;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RefreshTokenUtilImpl implements RefreshTokenJwtInf {
    private final RefreshTokenJwt refreshTokenJwt;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RefreshToken refreshToken(User user) {
        String refreshTokenString = refreshTokenJwt.generateToken(user.getUsername());
        return refreshTokenService.addRefreshToken(refreshTokenString, user);
    }
}
