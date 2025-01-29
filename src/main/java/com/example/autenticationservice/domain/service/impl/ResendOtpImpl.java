package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidSessionException;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.service.ResendOtpService;
import com.example.autenticationservice.domain.util.OtpUtil;
import com.example.autenticationservice.domain.util.UserListUtil;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResendOtpImpl implements ResendOtpService {

    private final Logger logger = LoggerFactory.getLogger(ResendOtpImpl.class);

    private final UserListUtil userListUtil;
    private final OtpUtil otpUtil;
    private final EmailService emailService;

    //TODO il codice dell''otp generazione, exiper etc è uguale alla login, creare classe/funzione per alleggerire il codice
    @Override
    public FirstStepResendOtpResponse firstStep(HttpSession session) {

        logger.info("OTP da annullare: {}", session.getAttribute("otp"));

        // annulliamo l'otp precedente
        session.removeAttribute("otp");
        session.removeAttribute("otpExpireTime");
        session.removeAttribute("otpAttempt");
        logger.info("OTP cancellato");

        //creiamo il nuovo otp
        String newOtp = otpUtil.generateOtp();
        session.setAttribute("otp", newOtp);
        logger.info("New otp: {}", newOtp);

        long otpExpireTime = System.currentTimeMillis() + 1*60*1000; //durata 1 minuti
        session.setAttribute("otpExpireTime", otpExpireTime);
        logger.info("OTP Expire Time: {}",
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
            logger.warn("Utente non trovato per username: {}", username);
            throw new InvalidSessionException("Utente non valido o inesistente");
        }

        return FirstStepResendOtpResponse.builder()
                .message("Nuovo Otp inviato")
                .build();
    }
}
