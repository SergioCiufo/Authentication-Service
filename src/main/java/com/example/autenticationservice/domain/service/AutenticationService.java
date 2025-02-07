package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;

public interface AutenticationService {

    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest);
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest request);
    public SecondStepLoginResponse secondStepVerifyOtp(SecondStepLoginRequest request);
    public ResendOtpResponse thirdStepResendOtp(ResendOtpRequest request);
    public VerifyTokenResponse firstStepVerifyToken();
    public GetAccessTokenByRefreshTokenResponse secondStepGetNewAccessToken(GetAccessTokenByRefreshTokenRequest request);
    public LogoutResponse fourthStepLogout();

}
