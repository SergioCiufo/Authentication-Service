package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.Otp;

public interface LoginServiceApi {
    Otp validateUserAndGenerateOtp(String username, String password, String sessionId);

}
