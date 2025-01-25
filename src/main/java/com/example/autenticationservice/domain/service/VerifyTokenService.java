package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface VerifyTokenService {
    public FirstStepVerifyTokenResponse firstStep(HttpSession session, HttpServletRequest request, HttpServletResponse response);
}
