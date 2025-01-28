package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.*;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpRequest;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.domain.model.login.FirstStepRequest;
import com.example.autenticationservice.domain.model.login.FirstStepResponse;
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
    private final LoginService loginService;
    private final LoginMappers loginMappers;

    private final HttpSession httpSession; //ho aggiunto la session per il sessionId
    private final HttpServletResponse httpServletResponseResponse;
    private final HttpServletRequest httpServletRequestRequest;

    private final RegisterService registerService;
    private final RegisterMappers registerMappers;

    private final VerifyOtpService verifyOtpService;
    private final VerifyOtpMappers verifyOtpMappers;

    private final LogoutService logoutService;
    private final LogoutMappers logoutMappers;

    private final ResendOtpService resendOtpService;
    private final ResendOtpMappers resendOtpMappers;

    private final VerifyTokenService verifyTokenService;
    private final VerifyTokenMappers verifyTokenMappers;

    private final NewAccessTokenByRefreshTokenService newAccessTokenByRefreshTokenService;
    private final NewAccessTokenByRefreshTokenMappers newAccessTokenByRefreshTokenMappers;



    @Override
    public ResponseEntity<GetOTP200Response> getOTP(GetOTPRequest getOTPRequest){
        FirstStepRequest request = loginMappers.convertToDomain(getOTPRequest);
        FirstStepResponse response = loginService.firstStep(request, httpSession); //ho aggiunto la session per il sessionId
        GetOTP200Response convertedResponse = loginMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Register200Response> register(RegisterRequest registerRequest){
        FirstStepRegisterRequest request = registerMappers.convertToDomain(registerRequest);
        FirstStepRegisterResponse response = registerService.firstStep(request);
        Register200Response convertedResponse = registerMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyOTP200Response> verifyOTP(VerifyOTPRequest verifyOTPRequest){
        FirstStepVerifyOtpRequest request = verifyOtpMappers.convertToDomain(verifyOTPRequest);
        FirstStepVerifyOtpResponse response = verifyOtpService.firstStep(request, httpSession, httpServletResponseResponse);
        VerifyOTP200Response convertedResponse = verifyOtpMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<Logout200Response> logout(){
        FirstStepLogoutResponse response = logoutService.firstStep(httpSession, httpServletResponseResponse, httpServletRequestRequest);
        Logout200Response convertedResponse = logoutMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);

    }

    @Override
    public ResponseEntity<ReSendOtp200Response> reSendOtp(ReSendOtpRequest reSendOtpRequest){
        FirstStepResendOtpRequest request = resendOtpMappers.convertToDomain(reSendOtpRequest);
        FirstStepResendOtpResponse response = resendOtpService.firstStep(httpSession);
        ReSendOtp200Response convertedResponse = resendOtpMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<VerifyToken200Response>  verifyToken(){
        FirstStepVerifyTokenResponse response = verifyTokenService.firstStep(httpSession, httpServletRequestRequest, httpServletResponseResponse);
        VerifyToken200Response convertedResponse = verifyTokenMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }

    @Override
    public ResponseEntity<RefreshToken200Response>  refreshToken(RefreshTokenRequest refreshTokenRequest){
        FirstStepNewAccessTokenByRefreshTokenRequest request = newAccessTokenByRefreshTokenMappers.convertToDomain(refreshTokenRequest);
        FirstStepNewAccessTokenByRefreshTokenResponse response = newAccessTokenByRefreshTokenService.firstStep(request, httpServletRequestRequest, httpSession, httpServletResponseResponse);
        RefreshToken200Response convertedResponse = newAccessTokenByRefreshTokenMappers.convertFromDomain(response);
        return ResponseEntity.ok(convertedResponse);
    }
}
