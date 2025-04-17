package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;

import java.util.List;

public interface AutenticationService {

    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest);
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest request);
    public SecondStepLoginResponse secondStepLogin(SecondStepLoginRequest request);
    public ResendOtpResponse resendOtp(ResendOtpRequest request);
    public VerifyTokenResponse verifyToken();
    public GetAccessTokenByRefreshTokenResponse getNewAccessToken(GetAccessTokenByRefreshTokenRequest request);
    public LogoutResponse logout(FirstStepLogoutRequest request);
    public List<GetUsernameResponse> getUsername();

}
