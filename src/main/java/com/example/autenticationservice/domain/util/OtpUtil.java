package com.example.autenticationservice.domain.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;

@Component
public class OtpUtil {
    private final String WORDS = "0123456789";
    private final int OTP_LENGTH = 6;

    //gestisce la scadenza dell'otp
    //private final long OTP_EXPIRATION = 5 * 60 * 1000; //5 minuti

    public String generateOtp() {
        //random più sicuro usato per generare chiavi
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(WORDS.charAt(secureRandom.nextInt(WORDS.length())));
        }

        return otp.toString();
    }

    public boolean isOtpExpired(long otpExpireTime) {
        long currentTime = Instant.now().toEpochMilli();
        return (currentTime > otpExpireTime);
    }

    public void removeOtpFromSession(HttpSession session) {
        session.removeAttribute("otp");
        session.removeAttribute("otpAttempt");
        session.removeAttribute("otpExpireTime");
    }
}
