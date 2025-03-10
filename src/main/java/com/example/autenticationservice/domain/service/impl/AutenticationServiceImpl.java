package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.api.JwtService;
import com.example.autenticationservice.domain.exceptions.*;
import com.example.autenticationservice.domain.util.jwt.AccessTokenJwt;
import com.example.autenticationservice.domain.util.jwt.RefreshTokenJwt;
import com.example.autenticationservice.domain.model.GetUsernameResponse;
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
import com.example.autenticationservice.domain.util.HashUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class AutenticationServiceImpl implements AutenticationService {

    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserService userService;
    private final OtpService otpService;
    private final TokenService tokenService;
    private final OtpUtil otpUtil;
    private final HashUtil hashUtil;


    @Override
    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest) {
        String passwordSha1 = hashUtil.stringToSha1(stepRegisterRequest.getPassword());
        User newUser = User.builder()
                .name(stepRegisterRequest.getName())
                .username(stepRegisterRequest.getUsername())
                .email(stepRegisterRequest.getEmail())
                .password(passwordSha1)
                .otpList(new ArrayList<>())
                .build();

        userService.register(newUser);

        return StepRegisterResponse.builder()
                .message("Registration completed")
                .build();
    }

    @Override
    @Transactional
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest firstStepLoginRequest) {
        String username = firstStepLoginRequest.getUsername();
        //String password = firstStepLoginRequest.getPassword();
        String passwordSha1 = hashUtil.stringToSha1(firstStepLoginRequest.getPassword());

        String sessionId = UUID.randomUUID().toString(); //UUID

        User user = userService.getUserByUsernameAndPassword(username, passwordSha1);

        Otp otp = otpUtil.generateOtp(user, sessionId);

        otpService.saveOtp(otp);

        emailService.sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());

        log.info("OTP generated {} and sent to: {}", otp.getOtp(), user.getEmail());

        return FirstStepLoginResponse.builder()
                .message("Login successful, OTP sent")
                .sessionId(sessionId)
                .build();
    }

    @Override
    @Transactional
    public SecondStepLoginResponse secondStepLogin(SecondStepLoginRequest secondStepLoginRequest) {
        String requestOtp = secondStepLoginRequest.getOtp();
        String sessionId = secondStepLoginRequest.getSessionId();
        String username = secondStepLoginRequest.getUsername();

        User user = userService.getUserByUsername(username);

        Otp dbOtp = otpService.getOtpBySessionId(sessionId);

        //controlli validità Otp
        if(otpUtil.checkOtpMaxAttempt(dbOtp)){
            otpService.invalidateOtp(dbOtp);
            throw new ExpireOtpException("OTP entry attemps exhausted");
        }

        if(!requestOtp.equals(dbOtp.getOtp())){
            otpUtil.increaseOtpAttempt(dbOtp);
            otpService.updateOtp(dbOtp);
            throw new InvalidCredentialsException("OTP not valid");
        }

        if(otpUtil.isOtpExpired(dbOtp)){
            otpService.invalidateOtp(dbOtp);
            throw new ExpireOtpException("OTP expired");
        }

        //se tutto va bene l'otp è verificato, lo invalidiamo e andiamo avanti con la generazione del token
        otpService.invalidateOtp(dbOtp);

        RefreshToken refreshToken = tokenService.generateRefreshToken(user);
        tokenService.addRefreshToken(refreshToken);

        String accessToken = tokenService.generateAccessToken(username);

        log.debug("Access Token: {}", accessToken);
        log.debug("Refresh Token: {}", refreshToken.getRefreshToken());

        //
        log.debug("Object RefreshToken -> User: {}, Token: {}", user.getUsername(), refreshToken.getRefreshToken());

        return SecondStepLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    @Override
    @Transactional
    public ResendOtpResponse resendOtp(ResendOtpRequest resendOtpRequest) {
        String sessionId = resendOtpRequest.getSessionId();
        String username = resendOtpRequest.getUsername();

        User user = userService.getUserByUsername(username);
        Otp oldOtp = otpService.getOtpBySessionId(sessionId);
        otpService.invalidateOtp(oldOtp);

        Otp newOtp = otpUtil.generateOtp(user, sessionId);
        otpService.saveOtp(newOtp);

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

//        if (accessToken == null || accessToken.isEmpty()) { isBlank sostituisce questo
        if(StringUtils.isBlank(accessToken)){
            log.error("Missing Access token");
            throw new MissingTokenException("Missing or non-existent token");
        }

        log.debug("Access token: {}", accessToken);

        try {
//            refreshTokenJwt.validateToken(accessToken);
            tokenService.validateRefreshToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.error("Access token expired, attempting to obtain a new one via refresh token");
            throw new TokenExpiredException("Access token expired, attempting to obtain a new one via refresh token");
        }

        String username = tokenService.getUsernameFromToken(accessToken);
        log.debug("Username from accessToken: {}", username);

        return VerifyTokenResponse.builder()
                .username(username)
                .build();
    }

    @Override
    public GetAccessTokenByRefreshTokenResponse getNewAccessToken(GetAccessTokenByRefreshTokenRequest firstStepRequest) {
        String refreshTokenString = jwtService.extractRefreshJwt();

        if(StringUtils.isBlank(refreshTokenString)){
            log.error("Missing Refresh token");
            throw new MissingTokenException("Missing refresh token, please Login");
        }

        RefreshToken refreshToken = tokenService.getRefreshToken(refreshTokenString);
        log.debug("Refresh token: {}", refreshTokenString);

        if (!tokenService.validateRefreshToken(refreshTokenString)) {
                log.error("Refresh token not valid");
                throw new MissingTokenException("Missing refresh token, please Login");
        }

        String username = refreshToken.getUser().getUsername();

        String accessToken = tokenService.generateAccessToken(username);
        log.info("Access Token: {}", accessToken);

        return GetAccessTokenByRefreshTokenResponse.builder()
                .message("Access Token regenerated")
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LogoutResponse logout() {
        String refreshTokenString = jwtService.extractRefreshJwt();

//        if (!(refreshTokenString == null || refreshTokenString.isEmpty())) {
        if(!StringUtils.isBlank(refreshTokenString)){
            tokenService.invalidateRefreshToken(refreshTokenString);
        }

        log.debug("Logged out successfully");
        return LogoutResponse.builder()
                .message("Logout successful. Tokens invalidated.")
                .build();
    }

    @Override
    public List<GetUsernameResponse> getUsername() {
        List<User> usernameList = userService.getUserList();
        //prende l'oggetto user e ne ricava solo l'username e l'idUtente
        List<GetUsernameResponse> responseList = usernameList.stream()
                .map(user -> GetUsernameResponse.builder()
                        .username(user.getUsername())
                        .build())
                .toList();

        return responseList;
    }
}
