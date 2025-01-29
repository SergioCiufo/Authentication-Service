package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.ExpireOtpException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import com.example.autenticationservice.domain.service.VerifyOtpService;
import com.example.autenticationservice.domain.util.JwtUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VerifyOtpServiceImpl implements VerifyOtpService {

    private final Logger logger = LoggerFactory.getLogger(VerifyOtpServiceImpl.class);

    private final OtpUtil otpUtil;
    private final JwtUtil jwtUtil;

    private final int MAX_OTP_ATTEMPTS = 3;


    @Override
    public FirstStepVerifyOtpResponse firstStep(FirstStepVerifyOtpRequest firstStepVerifyOtpRequest, HttpSession session, HttpServletResponse response) {

        String otp = firstStepVerifyOtpRequest.getOtp();
        //String sessionId = session.getId();

        //otp salvato in sessione per controllare l'otp che inserisce l'utente
        String checkOtp= (String)session.getAttribute("otp");

        //sessionId salvato in sessione per controllare se corrisponde con la sessione corrente
        String checksessionId= (String)session.getAttribute("sessionId");

        //controlla se ci sono dei dati nella sessione (non far schiantare il programma)
        if (checkOtp == null || checksessionId == null) {
            logger.error("Sessione non valida");
            throw new InvalidCredentialsException("Sessione non valida");
        }

        //ci prendiamo otpAttempt dalla sessione
        Integer otpAttempt = (Integer) session.getAttribute("otpAttempt");
        //otpAttempt preso dalla sessione è null? allora imposta a 0, sennò metti il valore
        otpAttempt = (otpAttempt == null) ? 0 : otpAttempt;

        if (otpAttempt >= MAX_OTP_ATTEMPTS){
            session.invalidate();
            logger.error("Tentativi inserimento OTP esauriti");
            throw new ExpireOtpException("Tentativi inserimento OTP esauriti");
        }

        if (!(checkOtp.equals(otp) /*&& checksessionId.equals(sessionId)*/)) {
            session.setAttribute("otpAttempt", otpAttempt + 1);
            throw new InvalidCredentialsException("OTP non valido");
        }

        //todo if otp scaduto da sistemare
        long otpExpireTime = (long) session.getAttribute("otpExpireTime");

        if (otpUtil.isOtpExpired(otpExpireTime)) {
            session.invalidate();
            logger.error("OTP scaduto");
            throw new ExpireOtpException("OTP scaduto");
        }

        logger.info("Tutto corretto. Generare Token");
        String username = session.getAttribute("username").toString();

        ResponseCookie refreshToken = jwtUtil.generateRefreshToken(username);

        String accessToken = jwtUtil.generateAccessToken(username);
        response.setHeader("Authorization", "Bearer " + accessToken);

        response.addCookie(new Cookie(refreshToken.getName(), refreshToken.getValue()));

        logger.info(String.format("Access Token: %s",accessToken));
        logger.info(String.format("Refresh Token: %s",refreshToken.getValue()));

        //TODO DA CANCELLARE
        //SCOPO ESEMPIO IN ASSENZA DB
        String refreshTokenValue = refreshToken.getValue();
        Duration maxAge = refreshToken.getMaxAge();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenEnd = now.plus(maxAge);


        User user = null;
        Optional<User> optionalUser = (Optional<User>) session.getAttribute("user");
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            logger.error("Utente non esistente");
        }

        RefreshToken refreshJwt = new RefreshToken(null, user ,refreshTokenValue, now, refreshTokenEnd);
        logger.info(String.format("Oggetto Refresh Token: %s",refreshJwt));
        // FINE ESEMPIO

        //TODO vedere bene il sessionID come confrontarlo al meglio
        //TODO vedere cambiare nomi allo swagger

        otpUtil.removeOtpFromSession(session);
        session.removeAttribute("username");

        return FirstStepVerifyOtpResponse.builder()
                .token(accessToken)
                .build();
    }
}
