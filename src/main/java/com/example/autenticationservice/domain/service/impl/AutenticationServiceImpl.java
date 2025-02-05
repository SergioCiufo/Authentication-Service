package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.api.JwtService;
import com.example.autenticationservice.domain.exceptions.*;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.SecondStepGetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.domain.service.*;
import com.example.autenticationservice.domain.util.JwtUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@Log4j2
public class AutenticationServiceImpl implements AutenticationService {

    private final UserService userService;
    private final RegisterService registerService;
    private final EmailService emailService;
    private final OtpUtil otpUtil;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    public StepRegisterResponse register(StepRegisterRequest stepRegisterRequest) {

        User newUser = User.builder()
                .name(stepRegisterRequest.getName())
                .username(stepRegisterRequest.getUsername())
                .email(stepRegisterRequest.getEmail())
                .password(stepRegisterRequest.getPassword())
                .otpList(new ArrayList<>())
                .build();

        registerService.registerUser(newUser);

        return StepRegisterResponse.builder()
                .message("Registrazione effettuata")
                .build();
    }

    @Override
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest firstStepLoginRequest) {
        String username = firstStepLoginRequest.getUsername();
        String password = firstStepLoginRequest.getPassword();
        String sessionId = firstStepLoginRequest.getSessionId(); //UUID

        //!servizio db
        //validazione credenziali
        User user = userService.validateCredentials(username, password);

        //generazione otp
        Otp otp = otpService.generateOtp(user, sessionId);

        user.getOtpList().add(otp);

        userService.updateUserOtpList(user);

        otpService.add(otp);
        //!

        //invio opt per email
        emailService.sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());

        log.info("OTP generato {} e inviato a: {}", otp.getOtp(), user.getEmail());

        return FirstStepLoginResponse.builder()
                .message("Login effettuato, OTP inviato")
                .sessionId(sessionId)
                .build();
    }

@Override
public SecondStepVerifyOtpResponse secondStepVerifyOtp(SecondStepVerifyOtpRequest secondStepVerifyOtpRequest) {
    String otp = secondStepVerifyOtpRequest.getOtp();
    String sessionId = secondStepVerifyOtpRequest.getSessionId();
    String username = secondStepVerifyOtpRequest.getUsername();

    //parte logica persistenza

    final int MAX_OTP_ATTEMPTS = 3;

    Otp checkOtp = otpService.getOtpBySessionId(sessionId);

    Integer otpAttempt = checkOtp.getAttempts();
    //otpAttempt preso dalla sessione è null? allora imposta a 0, sennò metti il valore
    otpAttempt = (otpAttempt == null) ? 0 : otpAttempt;

    if (otpAttempt >= MAX_OTP_ATTEMPTS){
        otpService.setOtpInvalid(checkOtp);
        log.error("Tentativi inserimento OTP esauriti");
        throw new ExpireOtpException("Tentativi inserimento OTP esauriti");
    }

    if (!checkOtp.getOtp().equals(otp)) {
        otpService.updateAttempt(checkOtp, otpAttempt+1);
        throw new InvalidCredentialsException("OTP non valido");
    }

    long otpExpireTime = checkOtp.getExpiresAt();

    if (otpUtil.isOtpExpired(otpExpireTime)) {
        otpService.setOtpInvalid(checkOtp);
        log.error("OTP scaduto");
        throw new ExpireOtpException("OTP scaduto");
    }

    String refreshToken = jwtService.generateRefreshToken(username);

    String accessToken = jwtUtil.generateAccessToken(username);

    log.info("Access Token: {}", accessToken);
    log.info("Refresh Token: {}", refreshToken);

    User user = userService.getUserFromUsername(username);
    if(user == null) {
        log.error("Utente non esistente");
    }

    RefreshToken refreshJwt = refreshTokenService.addRefreshToken(refreshToken, user);
    log.debug("Oggetto Refresh -> User: {}, Token: {}", refreshJwt.getUser().getUsername(), refreshJwt.getRefreshToken());

    return SecondStepVerifyOtpResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
}

    @Override
    public ThirdStepResendOtpResponse thirdStepResendOtp(ThridStepResendOtpRequest thridStepResendOtpRequest) {
        String sessionId = thridStepResendOtpRequest.getSessionId();
        String username = thridStepResendOtpRequest.getUsername();

        //servzio db
        //ce lo prendiamo dal db tramite campo idSessione di otp

        Otp otpToInvalidate = otpService.getOtpBySessionId(sessionId);
        log.debug("OTP da annullare: {}", otpToInvalidate.getOtp());
        otpService.setOtpInvalid(otpToInvalidate);

        //creiamo il nuovo otp
        User user = userService.getUserFromUsername(username);

        if(user == null) {
            log.warn("Utente non trovato per username: {}", username);
            throw new InvalidSessionException("Utente non valido o inesistente");
        }

        Otp newOtp = otpService.generateOtp(user, sessionId);
        otpService.add(newOtp);
        //

        String emailReceiver = user.getEmail();
        String emailSubject = "Chat4Me - OTP code";
        emailService.sendEmail(emailReceiver, emailSubject, newOtp.getOtp());

        log.info("New otp: {}", newOtp.getOtp());

        return ThirdStepResendOtpResponse.builder()
                .message("Nuovo Otp inviato")
                .build();
    }

    @Override
    public FirstStepVerifyTokenResponse firstStepVerifyToken() {
        //voglio recuperarel 'access token
        String accessToken = jwtService.extractAccessJwt();

        if (accessToken == null || accessToken.isEmpty()) {
            log.error("Access token mancante");
            throw new MissingTokenException("Token mancante o inesistente");
        }

        log.debug("Access token: {}",accessToken);

        try{
            jwtService.validateRefreshToken(accessToken);
        }catch (ExpiredJwtException e){
            log.error("Access token scaduto, prova ottenimento nuovo tramite refresh token");
            throw new TokenExpiredException("Access token scaduto, prova ottenimento nuovo tramite refresh token");
        }

        String username = jwtService.getUsernameFromAccessToken(accessToken);
        log.debug("Username dall'accessToken: {}",username);

        return FirstStepVerifyTokenResponse.builder()
                .username(username)
                .build();
    }

    @Override
    public SecondStepGetAccessTokenByRefreshTokenResponse secondStepGetNewAccessToken(SecondStepGetAccessTokenByRefreshTokenRequest firstStepRequest) {
        String refreshTokenString = jwtService.extractRefreshJwt();

        if (refreshTokenString == null || refreshTokenString.isEmpty()) {
            log.error("Refresh token mancante");
            throw new MissingTokenException("Refresh Token mancante, effettuare login");
        }

        RefreshToken refreshToken = refreshTokenService.getRefreshTokenList(refreshTokenString);

        log.debug("Refresh token: {}",refreshTokenString);

        if (!jwtUtil.validateRefreshToken(refreshTokenString)) {
            log.error("Refresh token non valido");
            throw new MissingTokenException("Refresh Token non valido, effettuare login");
        }

        String username = refreshToken.getUser().getUsername();

        String accessToken = jwtService.generateAccessToken(username);

        log.info("Access Token: {}", accessToken);

        return SecondStepGetAccessTokenByRefreshTokenResponse.builder()
                .message("Access Token Rigenerato")
                .accessToken(accessToken)
                .build();
    }

    @Override
    public FourthStepLogoutResponse fourthStepLogout() {
        String refreshTokenString = jwtService.extractRefreshJwt();

        if (!(refreshTokenString == null || refreshTokenString.isEmpty())) {
            RefreshToken refreshToken = refreshTokenService.getRefreshTokenList(refreshTokenString);
            refreshTokenService.invalidateRefreshToken(refreshToken);
        }

        log.debug("Logged out successfully");
        return FourthStepLogoutResponse.builder()
                .message("Logout effettuato con successo. Token invalidati.")
                .build();
    }
}
