package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.*;
import com.example.autenticationservice.application.service.JwtServiceImpl;
import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.autentication.SecondStepLoginResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationservice.domain.service.*;

import com.example.autenticationservice.generated.application.api.ServizioAutenticazioneApiDelegate;
import com.example.autenticationservice.generated.application.model.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor //creami costruttore con parametri richiesti (final) @Service
@RestController
public class ServizioAutenticazioneApiDelegateImpl implements ServizioAutenticazioneApiDelegate {

    private final AutenticationMappers autenticationMappers;
    private final AutenticationService autenticationService;
    private final JwtServiceImpl jwtServiceImpl;

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

        FirstStepLoginResponse response = autenticationService.firstStepLogin(request);

        Login200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyOTP200Response> verifyOTP(VerifyOTPRequest verifyOTPRequest){
        SecondStepLoginRequest request = autenticationMappers.convertToDomain(verifyOTPRequest);
        SecondStepLoginResponse response = autenticationService.secondStepLogin(request);

        //impostiamo l'accessToken nell'header (bearer token)
        jwtServiceImpl.setAuthorizationHeader(response.getAccessToken());
        jwtServiceImpl.generateRefreshCookie(response.getRefreshToken());

        VerifyOTP200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<ReSendOtp200Response> reSendOtp(ReSendOtpRequest reSendOtpRequest){
        ResendOtpRequest request = autenticationMappers.convertToDomain(reSendOtpRequest);
        ResendOtpResponse response = autenticationService.resendOtp(request);
        ReSendOtp200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyToken200Response>  verifyToken(){
        VerifyTokenResponse response = autenticationService.verifyToken();
        VerifyToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<RefreshToken200Response>  refreshToken(RefreshTokenRequest refreshTokenRequest){
        GetAccessTokenByRefreshTokenRequest request = autenticationMappers.convertToDomain(refreshTokenRequest);
        GetAccessTokenByRefreshTokenResponse response = autenticationService.getNewAccessToken(request);

        //impostiamo l'accessToken nell'header (bearer token)
        jwtServiceImpl.setAuthorizationHeader(response.getAccessToken());

        RefreshToken200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Logout200Response> logout(){
        LogoutResponse response = autenticationService.logout();

        ResponseCookie cleanRefreshCookie = jwtServiceImpl.getCleanJwtCookie();

        Logout200Response convertedResponse = autenticationMappers.convertFromDomain(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "")
                .body(convertedResponse);
    }

    @Override
    public ResponseEntity<List<GetUsernameList200ResponseInner>> getUsernameList(){
        List<GetUsernameResponse> response = autenticationService.getUsername();
        List<GetUsernameList200ResponseInner> username = response.stream()
                .map(autenticationMappers::convertFromDomain)
                .toList();
        return ResponseEntity.ok(username);
    }
}
