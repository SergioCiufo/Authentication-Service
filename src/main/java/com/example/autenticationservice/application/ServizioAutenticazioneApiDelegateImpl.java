package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.*;
import com.example.autenticationservice.application.service.JwtServiceImpl;
import com.example.autenticationservice.application.service.SessionService;
import com.example.autenticationservice.domain.model.autentication.ThirdStepResendOtpResponse;
import com.example.autenticationservice.domain.model.autentication.ThridStepResendOtpRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.autentication.FourthStepLogoutResponse;
import com.example.autenticationservice.domain.model.autentication.SecondStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.autentication.SecondStepVerifyOtpResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.domain.service.*;

import com.example.autenticationservice.generated.application.api.ServizioAutenticazioneApiDelegate;
import com.example.autenticationservice.generated.application.model.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //creami costruttore con parametri richiesti (final) @Service
@RestController
public class ServizioAutenticazioneApiDelegateImpl implements ServizioAutenticazioneApiDelegate {

    private final AutenticationMappers autenticationMappers;
    private final AutenticationService autenticationService;
    private final SessionService sessionService;
    private final JwtServiceImpl jwtServiceImpl;

//    @Override
//    public ResponseEntity<VerifyOTP200Response> verifyOTP(VerifyOTPRequest verifyOTPRequest){
//        SecondStepVerifyOtpRequest request = autenticationMappers.convertToDomain(verifyOTPRequest);
//        SecondStepVerifyOtpResponse response = autenticationService.firstStepVerifyOtp(request, httpSession, httpServletResponseResponse);
//        VerifyOTP200Response convertedResponse = autenticationMappers.convertFromDomain(response);
//        return ResponseEntity.ok(convertedResponse);
//    }
//
//    @Override
//    public ResponseEntity<Logout200Response> logout(){
//        FourthStepLogoutResponse response = autenticationService.firstStepLogout(httpSession, httpServletResponseResponse, httpServletRequestRequest);
//        Logout200Response convertedResponse = autenticationMappers.convertFromDomain(response);
//        return ResponseEntity.ok(convertedResponse);
//    }
//
//    @Override
//    public ResponseEntity<ReSendOtp200Response> reSendOtp(ReSendOtpRequest reSendOtpRequest){
//        ThridStepResendOtpRequest request = autenticationMappers.convertToDomain(reSendOtpRequest);
//        ThirdStepResendOtpResponse response = autenticationService.firstStepResendOtp(httpSession);
//        ReSendOtp200Response convertedResponse = autenticationMappers.convertFromDomain(response);
//        return ResponseEntity.ok(convertedResponse);
//    }
//
//    @Override
//    public ResponseEntity<VerifyToken200Response>  verifyToken(){
//        FirstStepVerifyTokenResponse response = autenticationService.firstStepVerifyToken(httpSession, httpServletRequestRequest, httpServletResponseResponse);
//        VerifyToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
//        return ResponseEntity.ok(convertedResponse);
//    }
//
//    @Override
//    public ResponseEntity<RefreshToken200Response>  refreshToken(RefreshTokenRequest refreshTokenRequest){
//        SecondStepGetAccessTokenByRefreshTokenRequest request = autenticationMappers.convertToDomain(refreshTokenRequest);
//        SecondStepGetAccessTokenByRefreshTokenResponse response = autenticationService.firstStepGetNewAccessToken(request, httpServletRequestRequest, httpSession, httpServletResponseResponse);
//        RefreshToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
//        return ResponseEntity.ok(convertedResponse);
//    }

    @Override
    public ResponseEntity<Register200Response> register(RegisterRequest registerRequest){
        StepRegisterRequest request = autenticationMappers.convertToDomain(registerRequest);
        StepRegisterResponse response = autenticationService.register(request);
        Register200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Login200Response> login(LoginRequest loginRequest){
        FirstStepLoginRequest request = autenticationMappers.convertToDomain(loginRequest);

        //prendiamo la sessione e la settiamo nella request
        String sessionId = sessionService.getSessionId();
        request.setSessionId(sessionId);

        FirstStepLoginResponse response = autenticationService.firstStepLogin(request);

        //prendiamo l'username
        sessionService.setUsername(request.getUsername());

        Login200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyOTP200Response> verifyOTP(VerifyOTPRequest verifyOTPRequest){
        SecondStepVerifyOtpRequest request = autenticationMappers.convertToDomain(verifyOTPRequest);

        //prendiamo la sessione e la settiamo nella request
        String sessionId = sessionService.getSessionId();
        request.setSessionId(sessionId);

        String username = sessionService.getUsername();
        request.setUsername(username);

        SecondStepVerifyOtpResponse response = autenticationService.secondStepVerifyOtp(request);

        //impostiamo l'accessToken nell'header (bearer token)
        jwtServiceImpl.setAuthorizationHeader(response.getAccessToken());
        jwtServiceImpl.generateRefreshCookie(username);

        sessionService.removeUsername();

        VerifyOTP200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Logout200Response> logout(){
        FourthStepLogoutResponse response = autenticationService.fourthStepLogout();

        sessionService.invalidateSession();

        ResponseCookie cleanRefreshCookie = jwtServiceImpl.getCleanJwtCookie();

        Logout200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "")
                .body(convertedResponse);
    }

    @Override
    public ResponseEntity<ReSendOtp200Response> reSendOtp(ReSendOtpRequest reSendOtpRequest){
        ThridStepResendOtpRequest request = autenticationMappers.convertToDomain(reSendOtpRequest);

        //prendiamo la sessione e la settiamo nella request
        String sessionId = sessionService.getSessionId();
        request.setSessionId(sessionId);

        String username = sessionService.getUsername();
        request.setUsername(username);

        ThirdStepResendOtpResponse response = autenticationService.thirdStepResendOtp(request);
        ReSendOtp200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyToken200Response>  verifyToken(){
        FirstStepVerifyTokenResponse response = autenticationService.firstStepVerifyToken();
        VerifyToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<RefreshToken200Response>  refreshToken(RefreshTokenRequest refreshTokenRequest){
        SecondStepGetAccessTokenByRefreshTokenRequest request = autenticationMappers.convertToDomain(refreshTokenRequest);
        SecondStepGetAccessTokenByRefreshTokenResponse response = autenticationService.secondStepGetNewAccessToken(request);

        //impostiamo l'accessToken nell'header (bearer token)
        jwtServiceImpl.setAuthorizationHeader(response.getAccessToken());

        RefreshToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }
}
