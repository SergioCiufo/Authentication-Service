package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import jakarta.servlet.http.HttpSession;

public interface ResendOtpService {
    public FirstStepResendOtpResponse firstStep(HttpSession session);
}
