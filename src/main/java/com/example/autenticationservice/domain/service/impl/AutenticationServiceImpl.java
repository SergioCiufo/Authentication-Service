package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.api.JwtService;
import com.example.autenticationservice.domain.exceptions.*;
import com.example.autenticationservice.domain.jwt.AccessTokenJwt;
import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationservice.domain.service.*;
import com.example.autenticationservice.domain.util.OtpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class AutenticationServiceImpl implements AutenticationService {

    private final EmailService emailService;
    private final OtpUtil otpUtil;
    private final RefreshTokenJwt refreshTokenJwt;
    private final AccessTokenJwt accessTokenJwt;
    private final JwtService jwtService;

    //prova
    private final UserService userService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;


    @Override
    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest) {

        User newUser = User.builder()
                .name(stepRegisterRequest.getName())
                .username(stepRegisterRequest.getUsername())
                .email(stepRegisterRequest.getEmail())
                .password(stepRegisterRequest.getPassword())
                .otpList(new ArrayList<>())
                .build();

        userService.register(newUser);

        return StepRegisterResponse.builder()
                .message("Registration completed")
                .build();
    }

    @Override
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest firstStepLoginRequest) {
        String username = firstStepLoginRequest.getUsername();
        String password = firstStepLoginRequest.getPassword();

        String sessionId = UUID.randomUUID().toString(); //UUID
        Otp otp = otpService.validateUserAndGenerateOtp(username, password, sessionId);

        //invio opt per email
        emailService.sendEmail(otp.getUser().getEmail(), "Chat4Me - OTP code", otp.getOtp());

        log.info("OTP generated {} and sent to: {}", otp.getOtp(), otp.getUser().getEmail());

        return FirstStepLoginResponse.builder()
                .message("Login successful, OTP sent")
                .sessionId(sessionId)
                .build();
    }

    @Override
    public SecondStepLoginResponse secondStepLogin(SecondStepLoginRequest secondStepLoginRequest) {
        String otp = secondStepLoginRequest.getOtp();
        String sessionId = secondStepLoginRequest.getSessionId();
        String username = secondStepLoginRequest.getUsername();

        User user = userService.getUserByUsername(username);

        final int MAX_OTP_ATTEMPTS = 2; //parte da 0

        Otp checkOtp = otpService.getOtpBySessionId(sessionId);

        Integer otpAttempt = checkOtp.getAttempts();


        if (otpAttempt >= MAX_OTP_ATTEMPTS) {
            checkOtp.setValid(false);
            otpService.updateOtp(checkOtp);
            log.error("OTP entry attempts exhausted");
            throw new ExpireOtpException("OTP entry attempts exhausted");
        }

        if (!checkOtp.getOtp().equals(otp)) {
            checkOtp.setAttempts(otpAttempt + 1);
            otpService.updateOtp(checkOtp);
            throw new InvalidCredentialsException("OTP not valid");
        }

        long otpExpireTime = checkOtp.getExpiresAt();

        if (otpUtil.isOtpExpired(otpExpireTime)) {
            checkOtp.setValid(false);
            otpService.updateOtp(checkOtp);
            log.error("OTP expired");
            throw new ExpireOtpException("OTP expired");
        }

        String refreshToken = refreshTokenJwt.generateToken(username);

        String accessToken = accessTokenJwt.generateToken(username);

        log.debug("Access Token: {}", accessToken);
        log.debug("Refresh Token: {}", refreshToken);

        RefreshToken refreshJwt = refreshTokenService.addRefreshToken(refreshToken, user);
        //
        log.debug("Object RefreshToken -> User: {}, Token: {}", refreshJwt.getUser().getUsername(), refreshJwt.getRefreshToken());

        checkOtp.setValid(false);
        otpService.updateOtp(checkOtp);

        return SecondStepLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public ResendOtpResponse resendOtp(ResendOtpRequest resendOtpRequest) {
        String sessionId = resendOtpRequest.getSessionId();
        String username = resendOtpRequest.getUsername();

//        Otp oldOtp = otpService.getOtpBySessionId(sessionId);
//        oldOtp.setValid(false);
//        otpService.updateOtp(oldOtp);
//
//        User user = userService.getUserByUsername(username);
//
//        Otp newOtp = otpUtil.generateOtp(user, sessionId);
//
//        otpService.addOtp(newOtp);
//
//        String emailReceiver = user.getEmail();
//        String emailSubject = "Chat4Me - OTP code";
//        emailService.sendEmail(emailReceiver, emailSubject, newOtp.getOtp());

        Otp newOtp = otpService.getNewOtp(sessionId, username);

        String emailReceiver = newOtp.getUser().getEmail();
        String emailSubject = "Chat4Me - OTP code";
        emailService.sendEmail(emailReceiver, emailSubject, newOtp.getOtp());

        log.info("New otp: {}", newOtp.getOtp());

        return ResendOtpResponse.builder()
                .message("New OTP sent")
                .build();
    }

    @Override
    public VerifyTokenResponse verifyToken() {
        //voglio recuperarel 'access token
        String accessToken = jwtService.extractAccessJwt();

        if (accessToken == null || accessToken.isEmpty()) {
            log.error("Missing Access token");
            throw new MissingTokenException("Missing or non-existent token");
        }

        log.debug("Access token: {}", accessToken);

        try {
            refreshTokenJwt.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.error("Access token expired, attempting to obtain a new one via refresh token");
            throw new TokenExpiredException("Access token expired, attempting to obtain a new one via refresh token");
        }

        String username = accessTokenJwt.getUsernameFromToken(accessToken);
        log.debug("Username from accessToken: {}", username);

        return VerifyTokenResponse.builder()
                .username(username)
                .build();
    }

    @Override
    public GetAccessTokenByRefreshTokenResponse getNewAccessToken(GetAccessTokenByRefreshTokenRequest firstStepRequest) {
        String refreshTokenString = jwtService.extractRefreshJwt();

        if (refreshTokenString == null || refreshTokenString.isEmpty()) {
            log.error("Missing Refresh token");
            throw new MissingTokenException("Missing refresh token, please Login");
        }

        RefreshToken refreshToken = refreshTokenService.getRefreshToken(refreshTokenString);
        log.debug("Refresh token: {}", refreshTokenString);

        if (!refreshTokenService.validateRefreshToken(refreshTokenString)) {
                log.error("Refresh token not valid");
                throw new MissingTokenException("Missing refresh token, please Login");
        }

        String username = refreshToken.getUser().getUsername();

        String accessToken = accessTokenJwt.generateToken(username);
        log.info("Access Token: {}", accessToken);

        return GetAccessTokenByRefreshTokenResponse.builder()
                .message("Access Token regenerated")
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LogoutResponse logout() {
        String refreshTokenString = jwtService.extractRefreshJwt();

        if (!(refreshTokenString == null || refreshTokenString.isEmpty())) {
            refreshTokenService.invalidateRefreshToken(refreshTokenString);

        }

        log.debug("Logged out successfully");
        return LogoutResponse.builder()
                .message("Logout successful. Tokens invalidated.")
                .build();
    }
}
