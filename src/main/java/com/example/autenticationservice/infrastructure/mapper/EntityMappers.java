package com.example.autenticationservice.infrastructure.mapper;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.model.OtpEntity;
import com.example.autenticationservice.infrastructure.model.RefreshTokenEntity;
import com.example.autenticationservice.infrastructure.model.UserEntity;

public interface EntityMappers {
    User convertToDomain(UserEntity userEntity);
    UserEntity convertFromDomain(User user);

    Otp convertToDomain(OtpEntity otpEntity);
    OtpEntity convertFromDomain(Otp otp);

    RefreshToken convertToDomain(RefreshTokenEntity refreshTokenEntity);
    RefreshTokenEntity convertFromDomain(RefreshToken refreshToken);
}
