package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface LogoutService {
    public FirstStepLogoutResponse firstStep(HttpSession session, HttpServletResponse response);
}
