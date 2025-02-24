//package com.example.autenticationservice.domain.service.impl;
//
//import com.example.autenticationservice.domain.repository.RefreshTokenServiceRepo;
//import com.example.autenticationservice.domain.exceptions.MissingTokenException;
//import com.example.autenticationservice.domain.util.jwt.RefreshTokenJwt;
//import com.example.autenticationservice.domain.model.RefreshToken;
////import com.example.autenticationservice.domain.util.RefreshTokenListUtil;
//
//import com.example.autenticationservice.domain.model.User;
//import lombok.AllArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//@Service
//@AllArgsConstructor
//@Log4j2
//public class RefreshTokenService {
//    private final RefreshTokenJwt refreshTokenJwt;
//    private final RefreshTokenServiceRepo refreshTokenServiceRepo;
//
//    public void addRefreshToken(RefreshToken refreshToken) {
//        refreshTokenServiceRepo.addRefreshToken(refreshToken);
//    }
//
//    public RefreshToken generateRefreshToken(User user) {
//        String refreshToken = refreshTokenJwt.generateToken(user.getUsername());
//        int maxAgeInt = refreshTokenJwt.getExpirationDate();
//        Duration maxAge = Duration.ofSeconds(maxAgeInt);
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime expiresAt = now.plus(maxAge);
//
//        return RefreshToken.builder()
//                .user(user)
//                .refreshToken(refreshToken)
//                .createdAt(now)
//                .expireDate(expiresAt)
//                .valid(true)
//                .build();
//    }
//
//    public RefreshToken getRefreshToken(String refreshTokenString) {
//        return refreshTokenServiceRepo.getRefreshToken(refreshTokenString)
//                .orElseThrow(() -> new MissingTokenException("Missing refresh token, please Login"));
//    }
//
//    public void invalidateRefreshToken(String refreshTokenString) {
//        refreshTokenServiceRepo.invalidateRefreshToken(refreshTokenString);
//    }
//
//    public boolean validateRefreshToken(String token) {
//        return refreshTokenJwt.validateToken(token);
//    }
//}
