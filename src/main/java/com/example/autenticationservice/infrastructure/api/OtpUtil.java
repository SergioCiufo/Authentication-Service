package com.example.autenticationservice.infrastructure.api;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;

public interface OtpUtil {
    Otp generateOtp(User user, String sessionId);
}
