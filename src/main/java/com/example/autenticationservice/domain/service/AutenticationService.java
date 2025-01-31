package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.newAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.newAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.domain.model.login.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.login.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface AutenticationService {

    public FirstStepRegisterResponse register(FirstStepRegisterRequest registerRequest);
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest firstStepLoginRequest, HttpSession session);
    public FirstStepVerifyOtpResponse firstStepVerifyOtp(FirstStepVerifyOtpRequest request, HttpSession session, HttpServletResponse response);
    public FirstStepResendOtpResponse firstStepResendOtp(HttpSession session);
    public FirstStepVerifyTokenResponse firstStepVerifyToken(HttpSession session, HttpServletRequest request, HttpServletResponse response);
    public FirstStepNewAccessTokenByRefreshTokenResponse firstStepGetNewAccessToken(FirstStepNewAccessTokenByRefreshTokenRequest firstStepRequest, HttpServletRequest request, HttpSession session, HttpServletResponse response);
    public FirstStepLogoutResponse firstStepLogout(HttpSession session, HttpServletResponse response, HttpServletRequest request);

}
