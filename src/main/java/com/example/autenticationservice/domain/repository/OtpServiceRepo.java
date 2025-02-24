package com.example.autenticationservice.domain.repository;

import com.example.autenticationservice.domain.model.Otp;

import java.util.Optional;

public interface OtpServiceRepo {
    void saveOtp(Otp otp);
    void updateOtp(Otp otp);
    Optional<Otp> getValidOtpBySessionId(String sessionId);
    void invalidateOtp(Otp otp);
}
