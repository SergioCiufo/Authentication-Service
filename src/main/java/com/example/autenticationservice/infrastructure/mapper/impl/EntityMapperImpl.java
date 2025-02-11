package com.example.autenticationservice.infrastructure.mapper.impl;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.mapper.EntityMappers;
import com.example.autenticationservice.infrastructure.model.OtpEntity;
import com.example.autenticationservice.infrastructure.model.RefreshTokenEntity;
import com.example.autenticationservice.infrastructure.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntityMapperImpl implements EntityMappers {
    @Override
    public User convertToDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .otpList(mapOtpEntitiesToDomain(userEntity.getOtpList()))
                .refreshTokenList(mapRefreshTokenEntitiesToDomain(userEntity.getRefreshTokenList()))
                .build();
    }

    @Override
    public UserEntity convertFromDomain(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .otpList(mapOtpToEntities(user.getOtpList()))
                .refreshTokenList(mapRefreshTokenToEntities(user.getRefreshTokenList()))
                .build();
    }

    @Override
    public Otp convertToDomain(OtpEntity otpEntity) {
        return Otp.builder()
                .id(otpEntity.getId())
                .user(convertToDomain(otpEntity.getUser()))
                .otp(otpEntity.getOtp())
                .sessionId(otpEntity.getSessionId())
                .createdAt(otpEntity.getCreatedAt())
                .expiresAt(otpEntity.getExpiresAt())
                .attempts(otpEntity.getAttempts())
                .valid(otpEntity.isValid())
                .build();
    }

    @Override
    public OtpEntity convertFromDomain(Otp otp) {
        return OtpEntity.builder()
                .id(otp.getId())
                .user(convertFromDomain(otp.getUser()))
                .otp(otp.getOtp())
                .sessionId(otp.getSessionId())
                .createdAt(otp.getCreatedAt())
                .expiresAt(otp.getExpiresAt())
                .attempts(otp.getAttempts())
                .valid(otp.isValid())
                .build();
    }

    @Override
    public RefreshToken convertToDomain(RefreshTokenEntity refreshTokenEntity) {
        return RefreshToken.builder()
                .id(refreshTokenEntity.getId())
                .user(convertToDomain(refreshTokenEntity.getUser()))
                .refreshToken(refreshTokenEntity.getRefreshToken())
                .createdAt(refreshTokenEntity.getCreatedAt())
                .expireDate(refreshTokenEntity.getExpireDate())
                .valid(refreshTokenEntity.getValid())
                .build();
    }

    @Override
    public RefreshTokenEntity convertFromDomain(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .id(refreshToken.getId())
                .user(convertFromDomain(refreshToken.getUser()))
                .refreshToken(refreshToken.getRefreshToken())
                .createdAt(refreshToken.getCreatedAt())
                .expireDate(refreshToken.getExpireDate())
                .valid(refreshToken.getValid())
                .build();
    }

    public List<Otp> mapOtpEntitiesToDomain(List<OtpEntity> otpEntities) {
        return (otpEntities == null) ? List.of() : otpEntities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    public List<OtpEntity> mapOtpToEntities(List<Otp> otps) {
        return (otps == null) ? List.of() : otps.stream()
                .map(this::convertFromDomain)
                .collect(Collectors.toList());
    }

    public List<RefreshToken> mapRefreshTokenEntitiesToDomain(List<RefreshTokenEntity> refreshTokenEntities) {
        return (refreshTokenEntities == null) ? List.of() : refreshTokenEntities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    public List<RefreshTokenEntity> mapRefreshTokenToEntities(List<RefreshToken> refreshTokens) {
        return (refreshTokens == null) ? List.of() : refreshTokens.stream()
                .map(this::convertFromDomain)
                .collect(Collectors.toList());
    }

}
