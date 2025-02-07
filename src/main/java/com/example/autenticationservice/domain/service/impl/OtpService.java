package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidSessionException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.OtpListUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
// TODO questa e le classi sotto sono implementazioni non interfacce, mettile in impl(anche se non implementano nessuna interfaccia, nel service teniamo le interfacce in service.impl le implementazioni)
//TODO DONE
@Service
@AllArgsConstructor
@Log4j2
public class OtpService {
    private final OtpListUtil otpListUtil;
    private final OtpUtil otpUtil;

    public Otp getOtpByUserOtp(String userOtp){
        List<Otp> otpList = otpListUtil.getOtpList();

        return otpList.stream()
                .filter(otp -> otp.getOtp().equals(userOtp) && otp.isValid()) //se esiste ed è valido
                .findFirst() // Prende il primo trovato
                .orElse(null); //sennò da null
    }

    public Otp generateOtp(User user, String sessionId){
        String otpGenerated = otpUtil.generateOtp();
        long creationDate = Instant.now().toEpochMilli();
        long expirationDate = otpUtil.calculateOtpExpirationTime();
        int attempts = 0;
        Otp otp = new Otp(null, user, otpGenerated, sessionId,creationDate, expirationDate, attempts, true);
        return otp;
    }

    public void add(Otp otp) {
        otpListUtil.getOtpList().add(otp);
    }

    public Otp getOtpBySessionId(String sessionId) {
        List<Otp> otpList = otpListUtil.getOtpList();

        return otpList.stream()
                .filter(otp -> otp.getSessionId().equals(sessionId) && otp.isValid())
                .findFirst()
                .orElse(null);
    }

    public void setOtpInvalid(Otp otp) {
        if (otp != null) {
            otp.setValid(false);
        }
    }

    public void updateAttempt(Otp otp, Integer attempt) {
        if (otp != null) {
            otp.setAttempts(attempt);
        }
    }

    public void invalidateOtp(String sessionId) {
        Otp otpToInvalidate = getOtpBySessionId(sessionId);
        if (otpToInvalidate == null) {
            throw new InvalidSessionException("Invalid or non-existent session");
        }
        log.debug("OTP to cancel: {}", otpToInvalidate.getOtp());
        setOtpInvalid(otpToInvalidate);
    }
}
