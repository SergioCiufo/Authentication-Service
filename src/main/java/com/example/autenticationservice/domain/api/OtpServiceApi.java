package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;

import java.util.Optional;

public interface OtpServiceApi {
    void addOtp(Otp otp);
    void updateOtp(Otp otp);
    Optional<Otp> getValidOtpBySessionId(String sessionId);
    Otp getNewOtp(String sessionId, String username);
}
