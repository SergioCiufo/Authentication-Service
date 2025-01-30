package com.example.autenticationservice.domain.service;


import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.RefreshTokenListUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class RefreshTokenService {
    private final RefreshTokenListUtil refreshTokenListUtil;
    private final RefreshTokenJwt refreshTokenJwt;

    public RefreshToken addRefreshToken(ResponseCookie refreshToken, User user) {
        String refreshTokenValue = refreshToken.getValue();
        Duration maxAge = refreshToken.getMaxAge();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenEnd = now.plus(maxAge);

        if(user == null) {
            log.error("Utente non esistente");
        }

        RefreshToken refreshJwt = new RefreshToken(null, user ,refreshTokenValue, now, refreshTokenEnd, true);
        refreshTokenListUtil.getRefreshTokenList().add(refreshJwt);
        return refreshJwt;
    }

    public RefreshToken getRefreshTokenList(String refreshTokenString) {
        List<RefreshToken> refreshTokenList = refreshTokenListUtil.getRefreshTokenList();

        return refreshTokenList.stream()
                .filter(refToken -> refToken.getRefreshToken().equals(refreshTokenString))
                .findFirst()
                .orElse(null);
    }

    public void invalidateRefreshToken(RefreshToken refreshToken) {
        if(refreshToken != null) {
            refreshToken.setValid(false);
        }
    }

}
