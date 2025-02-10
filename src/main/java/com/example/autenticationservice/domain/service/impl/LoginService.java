package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.LoginServiceApi;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class LoginService {
    private final LoginServiceApi loginServiceApi;

    public Otp validateUserAndGenerateOtp(String username, String password, String sessionId) {
        return loginServiceApi.validateUserAndGenerateOtp(username, password, sessionId);
    }

    public RefreshToken checkOtpAndGenerateRefreshToken(String username, String otp, String sessionId) {
        return loginServiceApi.checkOtpAndGenerateRefreshToken(username, otp, sessionId);
    }
}
