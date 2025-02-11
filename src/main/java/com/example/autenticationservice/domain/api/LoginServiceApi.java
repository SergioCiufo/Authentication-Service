package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;

public interface LoginServiceApi {
    Otp validateUserAndGenerateOtp(String username, String password, String sessionId);

    RefreshToken validateOtpAndGenerateToken(String username, String sessionId, String requestOtp);

}
