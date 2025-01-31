package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.newAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.newAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpRequest;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.domain.model.login.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.login.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.generated.application.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AutenticationMappers {

    //REGISTER
    FirstStepRegisterRequest convertToDomain(RegisterRequest request);
    Register200Response convertFromDomain(FirstStepRegisterResponse response);

    //LOGIN
    //@Mapping(target = "username", source = "request.username") nel caso dovessero avere nomi diversi o rompere delle incosistenze
    FirstStepLoginRequest convertToDomain(LoginRequest request);
    @Mapping(target = "sessionOtp", source = "response.sessionId")
    Login200Response convertFromDomain(FirstStepLoginResponse response);

    //VERIFY OTP
    FirstStepVerifyOtpRequest convertToDomain(VerifyOTPRequest verifyOTPRequest);
    VerifyOTP200Response convertFromDomain(FirstStepVerifyOtpResponse firstStepVerifyOtpResponse);

    //RE-SEND OTP
    FirstStepResendOtpRequest convertToDomain(ReSendOtpRequest request);
    ReSendOtp200Response convertFromDomain(FirstStepResendOtpResponse resendOtpResponse);

    //VERIFY TOKEN
    VerifyToken200Response convertFromDomain(FirstStepVerifyTokenResponse response);

    //GET ACCESSTOKEN BY REFRESH TOKEN
    FirstStepNewAccessTokenByRefreshTokenRequest convertToDomain(RefreshTokenRequest refreshTokenRequest);
    RefreshToken200Response convertFromDomain(FirstStepNewAccessTokenByRefreshTokenResponse firstStepNewAccessTokenByRefreshTokenResponse);

    //LOGOUT
    Logout200Response convertFromDomain(FirstStepLogoutResponse response);

}
