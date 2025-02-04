package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;

public interface AutenticationService {

    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest);
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest request);
    public SecondStepVerifyOtpResponse secondStepVerifyOtp(SecondStepVerifyOtpRequest request);
    public ThirdStepResendOtpResponse thirdStepResendOtp(ThridStepResendOtpRequest request);
    public FirstStepVerifyTokenResponse firstStepVerifyToken();
    public SecondStepGetAccessTokenByRefreshTokenResponse secondStepGetNewAccessToken(SecondStepGetAccessTokenByRefreshTokenRequest request);
    public FourthStepLogoutResponse fourthStepLogout();

}
