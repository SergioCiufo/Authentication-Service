package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.autentication.SecondStepLoginRequest;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationService.generated.application.model.*;
import org.mapstruct.Mapper;

@Mapper
public interface AutenticationMappers {

    //REGISTER
    StepRegisterRequest convertToDomain(RegisterRequest request);
    Register200Response convertFromDomain(StepRegisterResponse response);

    //LOGIN
    //FirstStepLoginRequest convertToDomain(LoginRequest request, String sessionId); SCOPO DI ESEMPIO
    FirstStepLoginRequest convertToDomain(LoginRequest request);
    //@Mapping(target = "sessionOtp", source = "response.sessionId") //si usa per cambiare nome dallo swagger alla classe response
    Login200Response convertFromDomain(FirstStepLoginResponse response);

    //VERIFY OTP
    SecondStepLoginRequest convertToDomain(VerifyOTPRequest verifyOTPRequest);
    VerifyOTP200Response convertFromDomain(SecondStepLoginResponse secondStepLoginResponse);

    //RE-SEND OTP
    ResendOtpRequest convertToDomain(ReSendOtpRequest request);
    ReSendOtp200Response convertFromDomain(ResendOtpResponse resendOtpResponse);

    //VERIFY TOKEN
    VerifyToken200Response convertFromDomain(VerifyTokenResponse response);

    //GET ACCESSTOKEN BY REFRESH TOKEN
    GetAccessTokenByRefreshTokenRequest convertToDomain(RefreshTokenRequest refreshTokenRequest);
    RefreshToken200Response convertFromDomain(GetAccessTokenByRefreshTokenResponse getAccessTokenByRefreshTokenResponse);

    //LOGOUT
    FirstStepLogoutRequest convertToDomain(LogoutRequest logoutRequest);
    Logout200Response convertFromDomain(LogoutResponse response);

    //GET USERNAME LIST
    GetUsernameList200ResponseInner convertFromDomain(GetUsernameResponse response);

}
