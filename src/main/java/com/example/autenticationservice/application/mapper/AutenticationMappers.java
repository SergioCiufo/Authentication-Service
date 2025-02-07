package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.autentication.FourthStepLogoutResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.autentication.ThridStepResendOtpRequest;
import com.example.autenticationservice.domain.model.autentication.ThirdStepResendOtpResponse;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.autentication.SecondStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.autentication.SecondStepVerifyOtpResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.generated.application.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    SecondStepVerifyOtpRequest convertToDomain(VerifyOTPRequest verifyOTPRequest);
    VerifyOTP200Response convertFromDomain(SecondStepVerifyOtpResponse secondStepVerifyOtpResponse);

    //RE-SEND OTP
    ThridStepResendOtpRequest convertToDomain(ReSendOtpRequest request);
    ReSendOtp200Response convertFromDomain(ThirdStepResendOtpResponse resendOtpResponse);

    //VERIFY TOKEN
    VerifyToken200Response convertFromDomain(FirstStepVerifyTokenResponse response);

    //GET ACCESSTOKEN BY REFRESH TOKEN
    SecondStepGetAccessTokenByRefreshTokenRequest convertToDomain(RefreshTokenRequest refreshTokenRequest);
    RefreshToken200Response convertFromDomain(SecondStepGetAccessTokenByRefreshTokenResponse secondStepGetAccessTokenByRefreshTokenResponse);

    //LOGOUT
    Logout200Response convertFromDomain(FourthStepLogoutResponse response);

}
