package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.application.mapper.AutenticationMappers;
import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.exceptions.*;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.login.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.login.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.domain.service.AutenticationService;
import com.example.autenticationservice.domain.service.RegisterService;
import com.example.autenticationservice.domain.service.UserService;
import com.example.autenticationservice.domain.util.JwtUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import com.example.autenticationservice.domain.util.UserListUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class AutenticationServiceImpl implements AutenticationService {

    private final UserService userService;
    private final UserListUtil userListUtil;
    private final RegisterService registerService;
    private final EmailService emailService;
    private final OtpUtil otpUtil;
    private final JwtUtil jwtUtil;

    @Override
    public FirstStepRegisterResponse register(FirstStepRegisterRequest firstStepRegisterRequest) {
        String name = firstStepRegisterRequest.getName();
        String username = firstStepRegisterRequest.getUsername();
        String email = firstStepRegisterRequest.getEmail();
        String password = firstStepRegisterRequest.getPassword();

        User newUser = new User(null, name, username, email, password, null);

        String registerValid = registerService.registerValid(newUser);
        if(registerValid != null) {
            log.error(registerValid);
            throw new CredentialTakenException(registerValid);
        }
        userListUtil.add(newUser);

        return FirstStepRegisterResponse.builder()
                .message("Registrazione effettuata")
                .build();
    }

    @Override
    public FirstStepLoginResponse firstStepLogin(FirstStepLoginRequest firstStepLoginRequest, HttpSession session) {
        String username = firstStepLoginRequest.getUsername();
        String password = firstStepLoginRequest.getPassword();
        String sessionId = session.getId();

        //validazione credenziali
        User user = userService.validateCredentials(username, password);

        //generazione otp
        String otp = otpUtil.generateOtp();
        session.setAttribute("otp", otp);

        //invio opt per email
        emailService.sendEmail(user.getEmail(), "Chat4Me - OTP code", otp);

        //salvataggio utente nella sessione (da cambiare)
        session.setAttribute("username", user.getUsername());
        session.setAttribute("user", user);
        session.setAttribute("otpExpireTime", otpUtil.calculateOtpExpirationTime());

        log.info("OTP generato {} e inviato a: {}", otp, user.getEmail());

        return FirstStepLoginResponse.builder()
                .message("Login effettuato, OTP inviato")
                .sessionId(sessionId)
                .build();
    }

    @Override
    public FirstStepVerifyOtpResponse firstStepVerifyOtp(FirstStepVerifyOtpRequest firstStepVerifyOtpRequest, HttpSession session, HttpServletResponse response) {
        //private final int MAX_OTP_ATTEMPTS = 3;
        final int MAX_OTP_ATTEMPTS = 3;

        String otp = firstStepVerifyOtpRequest.getOtp();
        //String sessionId = session.getId();

        //otp salvato in sessione per controllare l'otp che inserisce l'utente
        String checkOtp= (String)session.getAttribute("otp");

        //sessionId salvato in sessione per controllare se corrisponde con la sessione corrente
        String checksessionId= (String)session.getAttribute("sessionId");

        //controlla se ci sono dei dati nella sessione (non far schiantare il programma)
        if (checkOtp == null /*|| checksessionId == null*/) {
            log.error("Sessione non valida");
            throw new InvalidCredentialsException("Sessione non valida");
        }

        //ci prendiamo otpAttempt dalla sessione
        Integer otpAttempt = (Integer) session.getAttribute("otpAttempt");
        //otpAttempt preso dalla sessione è null? allora imposta a 0, sennò metti il valore
        otpAttempt = (otpAttempt == null) ? 0 : otpAttempt;

        if (otpAttempt >= MAX_OTP_ATTEMPTS){
            session.invalidate();
            log.error("Tentativi inserimento OTP esauriti");
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
            log.error("OTP scaduto");
            throw new ExpireOtpException("OTP scaduto");
        }

        log.info("Tutto corretto. Generare Token");
        String username = session.getAttribute("username").toString();

        ResponseCookie refreshToken = jwtUtil.generateRefreshToken(username);

        String accessToken = jwtUtil.generateAccessToken(username);
        response.setHeader("Authorization", "Bearer " + accessToken);

        response.addCookie(new Cookie(refreshToken.getName(), refreshToken.getValue()));

        log.info(String.format("Access Token: %s",accessToken));
        log.info(String.format("Refresh Token: %s",refreshToken.getValue()));

        //TODO DA CANCELLARE
        //SCOPO ESEMPIO IN ASSENZA DB
        String refreshTokenValue = refreshToken.getValue();
        Duration maxAge = refreshToken.getMaxAge();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenEnd = now.plus(maxAge);


        User user = (User) session.getAttribute("user");
        if(user == null) {
            log.error("Utente non esistente");
        }

        RefreshToken refreshJwt = new RefreshToken(null, user ,refreshTokenValue, now, refreshTokenEnd);
        log.info(String.format("Oggetto Refresh Token: %s",refreshJwt));
        // FINE ESEMPIO

        //TODO creare uno storico otp e sessionId così da recuperare il dato dal db

        otpUtil.removeOtpFromSession(session);
        session.removeAttribute("username");

        return FirstStepVerifyOtpResponse.builder()
                .token(accessToken)
                .build();
    }

    @Override
    public FirstStepResendOtpResponse firstStepResendOtp(HttpSession session) {
        log.info("OTP da annullare: {}", session.getAttribute("otp"));

        // annulliamo l'otp precedente
        session.removeAttribute("otp");
        session.removeAttribute("otpExpireTime");
        session.removeAttribute("otpAttempt");
        log.info("OTP cancellato");

        //creiamo il nuovo otp
        String newOtp = otpUtil.generateOtp();
        session.setAttribute("otp", newOtp);
        log.info("New otp: {}", newOtp);

        long otpExpireTime = System.currentTimeMillis() + 1*60*1000; //durata 1 minuti
        session.setAttribute("otpExpireTime", otpExpireTime);
        log.info("OTP Expire Time: {}",
                Instant.ofEpochMilli((Long) session.getAttribute("otpExpireTime"))
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")));

        //ci prendiamo l'info dell'utente
        String username = (String) session.getAttribute("username");

        //usiamo optional per prenderci l'utente per poi pescare la mail
        Optional<User> user = userListUtil.getUserList().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (user.isPresent()) {
            String emailReceiver = user.get().getEmail();
            String emailSubject = "Chat4Me - OTP code";
            emailService.sendEmail(emailReceiver, emailSubject, newOtp);
        } else {
            log.warn("Utente non trovato per username: {}", username);
            throw new InvalidSessionException("Utente non valido o inesistente");
        }

        return FirstStepResendOtpResponse.builder()
                .message("Nuovo Otp inviato")
                .build();
    }

    @Override
    public FirstStepVerifyTokenResponse firstStepVerifyToken(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //voglio recuperarel 'access token
        String accessToken = jwtUtil.getAccessJwtFromHeader(request);


        if (accessToken == null || accessToken.isEmpty()) {
            log.error("Access token mancante");
            throw new MissingTokenException("Token mancante o inesistente");
        }

        log.info("Access token: {}",accessToken);

        try{
            jwtUtil.validateAccessToken(accessToken);
        }catch (ExpiredJwtException e){
            log.error("Access token scaduto, prova ottenimento nuovo tramite refresh token");
            throw new TokenExpiredException("Access token scaduto, prova ottenimento nuovo tramite refresh token");
        }

        log.debug("Access token prima di estrarre lo username: {}", accessToken);

        String username = jwtUtil.getUsernameFromAccessToken(accessToken);
        log.info("Username dall'accessToken: {}",username);

        return FirstStepVerifyTokenResponse.builder()
                .username(username)
                .build();
    }

    @Override
    public FirstStepNewAccessTokenByRefreshTokenResponse firstStepGetNewAccessToken(FirstStepNewAccessTokenByRefreshTokenRequest firstStepRequest, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshJwtFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            log.error("Refresh token mancante");
            session.invalidate();
            response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
            throw new MissingTokenException("Refresh Token mancante, effettuare login");
        }

        log.info("Refresh token: {}",refreshToken);

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            log.error("Refresh token non valido");
            session.invalidate();
            response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
            throw new MissingTokenException("Refresh Token non valido, effettuare login");
        }

        String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.generateAccessToken(username);
        response.setHeader("Authorization", "Bearer " + accessToken);
        log.info(String.format("Access Token: %s",accessToken));


        return FirstStepNewAccessTokenByRefreshTokenResponse.builder()
                .message("Access Token Rigenerato")
                .accessToken(accessToken)
                .build();
    }

    @Override
    public FirstStepLogoutResponse firstStepLogout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        //sostiuisce il token con un token "con scadenza immediata, rimuovendolo
        response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());

        //prendi ed invalida l'access token
        response.setHeader("Authorization", "Bearer " + null);

        //invalidazione della sessione :()
        session.invalidate();

        log.info("Logged out successfully");
        return FirstStepLogoutResponse.builder()
                .message("Logout effettuato con successo. Token invalidati.")
                .build();
    }
}
