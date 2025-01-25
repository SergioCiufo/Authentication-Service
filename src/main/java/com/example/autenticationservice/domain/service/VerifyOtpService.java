package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface VerifyOtpService {
    public FirstStepVerifyOtpResponse firstStep(FirstStepVerifyOtpRequest request, HttpSession session, HttpServletResponse response);
}
