package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.*;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
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
import com.example.autenticationservice.domain.service.*;

import com.example.autenticationservice.generated.application.api.ServizioAutenticazioneApiDelegate;
import com.example.autenticationservice.generated.application.model.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //creami costruttore con parametri richiesti (final) @Service
@RestController
public class ServizioAutenticazioneApiDelegateImpl implements ServizioAutenticazioneApiDelegate {

    private final AutenticationMappers autenticationMappers;
    private final AutenticationService autenticationService;

    private final HttpSession httpSession; //ho aggiunto la session per il sessionId
    private final HttpServletResponse httpServletResponseResponse;
    private final HttpServletRequest httpServletRequestRequest;



    @Override
    public ResponseEntity<Login200Response> login(LoginRequest loginRequest){
        FirstStepLoginRequest request = autenticationMappers.convertToDomain(loginRequest);
        FirstStepLoginResponse response = autenticationService.firstStepLogin(request, httpSession); //ho aggiunto la session per il sessionId
        Login200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Register200Response> register(RegisterRequest registerRequest){
        FirstStepRegisterRequest request = autenticationMappers.convertToDomain(registerRequest);
        FirstStepRegisterResponse response = autenticationService.register(request);
        Register200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyOTP200Response> verifyOTP(VerifyOTPRequest verifyOTPRequest){
        FirstStepVerifyOtpRequest request = autenticationMappers.convertToDomain(verifyOTPRequest);
        FirstStepVerifyOtpResponse response = autenticationService.firstStepVerifyOtp(request, httpSession, httpServletResponseResponse);
        VerifyOTP200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Logout200Response> logout(){
        FirstStepLogoutResponse response = autenticationService.firstStepLogout(httpSession, httpServletResponseResponse, httpServletRequestRequest);
        Logout200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);

    }

    @Override
    public ResponseEntity<ReSendOtp200Response> reSendOtp(ReSendOtpRequest reSendOtpRequest){
        FirstStepResendOtpRequest request = autenticationMappers.convertToDomain(reSendOtpRequest);
        FirstStepResendOtpResponse response = autenticationService.firstStepResendOtp(httpSession);
        ReSendOtp200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyToken200Response>  verifyToken(){
        FirstStepVerifyTokenResponse response = autenticationService.firstStepVerifyToken(httpSession, httpServletRequestRequest, httpServletResponseResponse);
        VerifyToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<RefreshToken200Response>  refreshToken(RefreshTokenRequest refreshTokenRequest){
        FirstStepNewAccessTokenByRefreshTokenRequest request = autenticationMappers.convertToDomain(refreshTokenRequest);
        FirstStepNewAccessTokenByRefreshTokenResponse response = autenticationService.firstStepGetNewAccessToken(request, httpServletRequestRequest, httpSession, httpServletResponseResponse);
        RefreshToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }
}
